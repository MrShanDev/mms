package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.MmsPlugin;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptorReader;
import com.sxpcwlkj.plugin.PluginDescriptorValidator;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginHealthContributor;
import com.sxpcwlkj.plugin.PluginConstants;
import com.sxpcwlkj.plugin.PluginInstallationLayout;
import com.sxpcwlkj.plugin.PluginKind;
import com.sxpcwlkj.plugin.host.internal.DefaultPluginRuntimeContext;
import com.sxpcwlkj.plugin.host.internal.LoadedPluginInstance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringBootVersion;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 扫描 {@code mms.plugin.root-dir} 下 {@code <pluginId>/<version>/lib/*.jar}，校验并加载 SPI。
 */
@Slf4j
public class PluginLifecycleManager {

    private final PluginHostProperties properties;
    private final ObjectProvider<PluginHostDbBridge> dbBridgeProvider;
    @Getter
    private final Map<String, LoadedPluginInstance> loadedPlugins = new ConcurrentHashMap<>();

    public PluginLifecycleManager(PluginHostProperties properties, ObjectProvider<PluginHostDbBridge> dbBridgeProvider) {
        this.properties = properties;
        this.dbBridgeProvider = dbBridgeProvider;
    }

    public List<PluginEntrySummary> listSummaries() {
        List<PluginEntrySummary> list = new ArrayList<>();
        for (LoadedPluginInstance lp : loadedPlugins.values()) {
            PluginDescriptor d = lp.getDescriptor();
            list.add(new PluginEntrySummary(d.getId(), d.getVersion(), d.getName(), "LOADED", null));
        }
        list.sort((a, b) -> a.pluginId().compareToIgnoreCase(b.pluginId()));
        return Collections.unmodifiableList(list);
    }

    /**
     * 已加载插件的 descriptor 快照（含 {@code frontend}），按插件 id 排序。
     */
    public List<PluginManifestView> listManifests() {
        List<PluginManifestView> list = new ArrayList<>();
        for (LoadedPluginInstance lp : loadedPlugins.values()) {
            PluginDescriptor d = lp.getDescriptor();
            list.add(new PluginManifestView(
                    d.getId(),
                    d.getVersion(),
                    d.getName(),
                    d.getDescription(),
                    d.getKind(),
                    d.getFrontend()));
        }
        list.sort((a, b) -> a.id().compareToIgnoreCase(b.id()));
        return Collections.unmodifiableList(list);
    }

    public synchronized void reload() {
        unloadAllQuietly();
        loadAll();
    }

    public synchronized void loadAll() {
        if (!properties.isEnabled()) {
            log.info("mms.plugin.enabled=false，跳过插件扫描");
            return;
        }
        Path root = resolveRoot();
        if (!ensurePluginsRoot(root)) {
            log.warn("插件根目录不可用: {}，跳过加载", root.toAbsolutePath());
            return;
        }
        String springVer = SpringBootVersion.getVersion();
        PluginHostDbBridge bridge = dbBridgeProvider.getIfAvailable();
        if (bridge == null) {
            scanAllDiskVersions(root, springVer);
            return;
        }
        Set<String> managed = bridge.pluginIdsManagedInDatabase();
        if (managed == null || managed.isEmpty()) {
            scanAllDiskVersions(root, springVer);
            return;
        }
        List<PluginVersionCoordinate> active = bridge.activeVersionsForStartup();
        if (active != null && !active.isEmpty()) {
            log.info("插件加载：库表激活版本 {} 条", active.size());
            loadCoordinates(root, active, springVer);
        } else {
            log.warn("库中已有插件版本记录但未配置激活版本，跳过受管插件加载");
        }
        loadOrphanDiskPlugins(root, managed, springVer);
    }

    /** 磁盘上「库中无任何版本行」的插件目录：仍按全版本扫描，兼容旧数据。 */
    private void loadOrphanDiskPlugins(Path root, Set<String> managedPluginIds, String springVer) {
        try (Stream<Path> idDirs = Files.list(root)) {
            idDirs.filter(Files::isDirectory).sorted().forEach(idDir -> {
                String folderId = idDir.getFileName().toString();
                if (managedPluginIds.contains(folderId)) {
                    return;
                }
                log.info("插件目录未纳入库表，按磁盘全版本加载: {}", folderId);
                try (Stream<Path> versionDirs = Files.list(idDir)) {
                    versionDirs.filter(Files::isDirectory).sorted().forEach(verDir -> {
                        try {
                            tryLoadOne(folderId, verDir.getFileName().toString(), verDir, springVer);
                        } catch (Exception e) {
                            log.warn("插件目录加载失败 {} : {}", verDir.toAbsolutePath(), e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    log.warn("列举版本目录失败: {}", idDir, e);
                }
            });
        } catch (IOException e) {
            log.warn("列举插件根目录失败: {}", root, e);
        }
    }

    private void scanAllDiskVersions(Path root, String springVer) {
        try (Stream<Path> pluginIdDirs = Files.list(root)) {
            pluginIdDirs.filter(Files::isDirectory).sorted().forEach(idDir -> {
                try (Stream<Path> versionDirs = Files.list(idDir)) {
                    versionDirs.filter(Files::isDirectory).sorted().forEach(verDir -> {
                        try {
                            tryLoadOne(idDir.getFileName().toString(), verDir.getFileName().toString(), verDir, springVer);
                        } catch (Exception e) {
                            log.warn("插件目录加载失败 {} : {}", verDir.toAbsolutePath(), e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    log.warn("列举版本目录失败: {}", idDir, e);
                }
            });
        } catch (IOException e) {
            log.warn("列举插件根目录失败: {}", root, e);
        }
    }

    private void loadCoordinates(Path root, List<PluginVersionCoordinate> coords, String springVer) {
        for (PluginVersionCoordinate c : coords) {
            if (c.pluginId() == null || c.version() == null) {
                continue;
            }
            String dirId = PluginInstallationLayout.safeSegment(c.pluginId());
            String dirVer = PluginInstallationLayout.safeSegment(c.version());
            Path verDir = root.resolve(dirId).resolve(dirVer);
            if (!Files.isDirectory(verDir)) {
                log.warn("库中激活版本在磁盘不存在，跳过加载: {} @ {}（路径 {}）", c.pluginId(), c.version(), verDir);
                continue;
            }
            try {
                tryLoadOne(dirId, dirVer, verDir, springVer);
            } catch (Exception e) {
                log.warn("插件加载失败 {}@{}: {}", c.pluginId(), c.version(), e.getMessage());
            }
        }
    }

    private void tryLoadOne(String dirPluginId, String dirVersion, Path versionDir, String springVer) throws Exception {
        Path libDir = versionDir.resolve("lib");
        if (!Files.isDirectory(libDir)) {
            return;
        }
        List<Path> jars;
        try (Stream<Path> st = Files.list(libDir)) {
            jars = st.filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar"))
                    .sorted()
                    .toList();
        }
        if (jars.isEmpty()) {
            return;
        }
        PluginDescriptor descriptor = null;
        Path descriptorJar = null;
        for (Path jar : jars) {
            try {
                descriptor = PluginDescriptorReader.readFromJar(jar);
                descriptorJar = jar;
                break;
            } catch (PluginException ignored) {
                // 尝试下一个 jar
            }
        }
        if (descriptor == null) {
            log.debug("目录下未找到含 plugin.json 的 jar: {}", libDir);
            return;
        }
        PluginDescriptorValidator.validateStructureOrThrow(descriptor);
        if (!PluginDescriptorValidator.satisfiesHost(descriptor, properties.getHostMmsRevision(), springVer)) {
            throw new PluginException("插件与宿主版本不兼容: " + descriptor.getId() + " requiresMms 与当前 revision / Spring Boot 不匹配");
        }
        String key = descriptor.getId() + "@" + descriptor.getVersion();
        URL[] urls = toUrls(jars);
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        URLClassLoader ucl = new URLClassLoader("mms-plugin:" + key, urls, parent);
        List<MmsPlugin> entries = new ArrayList<>();
        List<PluginHealthContributor> healthEntries = new ArrayList<>();
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(ucl);
        try {
            for (MmsPlugin p : ServiceLoader.load(MmsPlugin.class, ucl)) {
                entries.add(p);
            }
            for (PluginHealthContributor h : ServiceLoader.load(PluginHealthContributor.class, ucl)) {
                healthEntries.add(h);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
        if (descriptor.getKind() == PluginKind.EXTENSION && entries.isEmpty()) {
            ucl.close();
            throw new PluginException("插件声明为 extension 但未发现 SPI 实现: " + key + "，请检查 " + "META-INF/services/com.sxpcwlkj.plugin.MmsPlugin");
        }
        DefaultPluginRuntimeContext ctx = new DefaultPluginRuntimeContext(properties.getHostMmsRevision(), springVer, versionDir.toAbsolutePath());
        Files.createDirectories(ctx.pluginDataDirectory());
        Files.createDirectories(ctx.pluginTemporaryDirectory());
        for (MmsPlugin p : entries) {
            p.onLoad(ctx);
        }
        LoadedPluginInstance loaded = new LoadedPluginInstance(descriptor, ucl, entries, healthEntries);
        LoadedPluginInstance previous = loadedPlugins.put(key, loaded);
        if (previous != null) {
            try {
                previous.close();
            } catch (Exception e) {
                log.warn("替换插件时关闭旧 ClassLoader 失败: {}", key, e);
            }
        }
        log.info("插件已加载: {} (descriptor from {})", key, descriptorJar.getFileName());
    }

    /**
     * 聚合各插件 {@link PluginHealthContributor#health()}（需 super_admin 接口保护）。
     */
    public List<PluginHealthRow> collectHealth() {
        List<PluginHealthRow> rows = new ArrayList<>();
        for (LoadedPluginInstance lp : loadedPlugins.values()) {
            String pid = lp.getDescriptor().getId();
            String ver = lp.getDescriptor().getVersion();
            if (lp.getHealthContributors().isEmpty()) {
                rows.add(new PluginHealthRow(pid, ver, null, "NO_HEALTH_SPI"));
                continue;
            }
            for (PluginHealthContributor h : lp.getHealthContributors()) {
                try {
                    rows.add(new PluginHealthRow(pid, ver, h.health(), "OK"));
                } catch (Exception e) {
                    rows.add(new PluginHealthRow(pid, ver, e.getMessage(), "ERROR"));
                }
            }
        }
        return Collections.unmodifiableList(rows);
    }

    public Path getPluginsRoot() {
        return resolveRoot();
    }

    /**
     * 当前解析后的插件根目录是否为已存在目录（否则市场页应提示无法扫描磁盘）。
     */
    public boolean isPluginsRootDirectory() {
        return ensurePluginsRoot(resolveRoot());
    }

    /**
     * 探测某插件版本的安装布局是否具备可加载的 JAR（与加载逻辑中 lib 扫描一致）。
     */
    public PluginJarLocationStatus probeVersionLayout(String pluginId, String version) {
        Path root = resolveRoot();
        if (!ensurePluginsRoot(root)) {
            return PluginJarLocationStatus.ROOT_NOT_DIRECTORY;
        }
        if (pluginId == null || pluginId.isBlank() || version == null || version.isBlank()) {
            return PluginJarLocationStatus.OK;
        }
        Path verDir = PluginInstallationLayout.pluginRoot(root, pluginId.trim(), version.trim());
        if (!Files.isDirectory(verDir)) {
            return PluginJarLocationStatus.VERSION_DIR_MISSING;
        }
        Path libDir = verDir.resolve(PluginConstants.SUBDIR_LIB);
        if (!Files.isDirectory(libDir)) {
            return PluginJarLocationStatus.LIB_DIR_MISSING;
        }
        try (Stream<Path> js = Files.list(libDir)) {
            boolean any = js.anyMatch(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar"));
            return any ? PluginJarLocationStatus.OK : PluginJarLocationStatus.JAR_NOT_FOUND;
        } catch (IOException e) {
            log.debug("probeVersionLayout 列举 lib 失败: {}", libDir, e);
            return PluginJarLocationStatus.JAR_NOT_FOUND;
        }
    }

    /**
     * 枚举磁盘上 {@code <pluginId>/<version>/lib/*.jar} 已就绪的安装槽位（不要求当前已加载）。
     */
    public List<DiskPluginSlot> listDiskSlots() {
        Path root = resolveRoot();
        List<DiskPluginSlot> list = new ArrayList<>();
        if (!ensurePluginsRoot(root)) {
            return list;
        }
        try (Stream<Path> idDirs = Files.list(root)) {
            idDirs.filter(Files::isDirectory).sorted().forEach(idDir -> {
                String pluginId = idDir.getFileName().toString();
                try (Stream<Path> verDirs = Files.list(idDir)) {
                    verDirs.filter(Files::isDirectory).sorted().forEach(verDir -> {
                        String ver = verDir.getFileName().toString();
                        Path lib = verDir.resolve("lib");
                        boolean hasJar = false;
                        if (Files.isDirectory(lib)) {
                            try (Stream<Path> js = Files.list(lib)) {
                                hasJar = js.anyMatch(p -> p.getFileName().toString()
                                        .toLowerCase(Locale.ROOT).endsWith(".jar"));
                            } catch (IOException e) {
                                log.debug("列举 lib 失败: {}", lib, e);
                            }
                        }
                        list.add(new DiskPluginSlot(pluginId, ver, hasJar));
                    });
                } catch (IOException e) {
                    log.debug("列举版本目录失败: {}", idDir, e);
                }
            });
        } catch (IOException e) {
            log.warn("列举插件根目录失败: {}", root, e);
        }
        list.sort(Comparator.comparing(DiskPluginSlot::pluginId, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(DiskPluginSlot::version, String.CASE_INSENSITIVE_ORDER));
        return Collections.unmodifiableList(list);
    }

    /**
     * 从磁盘卸载：{@code version} 为空则删除该 {@code pluginId} 下所有版本目录。
     */
    public synchronized void uninstallFromDisk(String pluginId, String versionOrNull) throws IOException {
        if (pluginId == null || pluginId.isBlank()) {
            throw new PluginException("pluginId 不能为空");
        }
        Path root = resolveRoot();
        Path base = root.resolve(PluginInstallationLayout.safeSegment(pluginId));
        if (!Files.isDirectory(base)) {
            return;
        }
        if (versionOrNull == null || versionOrNull.isBlank()) {
            deleteRecursive(base);
            return;
        }
        Path ver = base.resolve(PluginInstallationLayout.safeSegment(versionOrNull));
        deleteRecursive(ver);
        try (Stream<Path> left = Files.list(base)) {
            if (left.findAny().isEmpty()) {
                deleteRecursive(base);
            }
        } catch (IOException ignored) {
            // ignore
        }
    }

    private static void deleteRecursive(Path root) throws IOException {
        if (root == null || !Files.exists(root)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(root)) {
            List<Path> paths = walk.sorted(Comparator.reverseOrder()).toList();
            IOException first = null;
            for (Path p : paths) {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException e) {
                    if (first == null) {
                        first = e;
                    }
                }
            }
            if (first != null) {
                throw first;
            }
        }
    }

    /**
     * 校验并将上传的 JAR 写入约定目录（不写库）；调用方负责 {@link PluginHostDbBridge} 与 {@link #reload()}。
     *
     * @return 已校验通过的描述符（与磁盘写入一致）
     */
    public PluginDescriptor installJarFromUpload(Path tempJarFile) throws Exception {
        PluginDescriptor d = PluginDescriptorReader.readFromJar(tempJarFile);
        PluginDescriptorValidator.validateStructureOrThrow(d);
        String springVer = SpringBootVersion.getVersion();
        if (!PluginDescriptorValidator.satisfiesHost(d, properties.getHostMmsRevision(), springVer)) {
            throw new PluginException("插件与宿主版本不兼容: " + d.getId());
        }
        Path root = resolveRoot();
        Path libDir = PluginInstallationLayout.libDirectory(root, d.getId(), d.getVersion());
        Files.createDirectories(libDir);
        String fn = tempJarFile.getFileName().toString();
        if (!fn.toLowerCase(Locale.ROOT).endsWith(".jar")) {
            fn = PluginInstallationLayout.safeSegment(d.getId()) + "-" + d.getVersion() + ".jar";
        }
        Path target = libDir.resolve(fn);
        Files.copy(tempJarFile, target, StandardCopyOption.REPLACE_EXISTING);
        Files.createDirectories(PluginInstallationLayout.dataDirectory(root, d.getId(), d.getVersion()));
        Files.createDirectories(PluginInstallationLayout.temporaryDirectory(root, d.getId(), d.getVersion()));
        return d;
    }

    private void unloadAllQuietly() {
        for (LoadedPluginInstance lp : loadedPlugins.values()) {
            try {
                lp.close();
            } catch (Exception e) {
                log.warn("卸载插件失败: {}", lp.getDescriptor().getId(), e);
            }
        }
        loadedPlugins.clear();
    }

    private Path resolveRoot() {
        if (properties.getRootDir() != null && !properties.getRootDir().isBlank()) {
            return Path.of(properties.getRootDir().trim()).toAbsolutePath().normalize();
        }
        return Path.of(System.getProperty("user.dir", "."), "mms-plugins").toAbsolutePath().normalize();
    }

    /**
     * 若插件根路径不存在则创建目录；已存在且非目录则无法使用，返回 false。
     */
    private boolean ensurePluginsRoot(Path root) {
        try {
            if (Files.exists(root)) {
                if (!Files.isDirectory(root)) {
                    log.warn("插件根路径已存在但不是目录: {}", root.toAbsolutePath());
                    return false;
                }
                return true;
            }
            Files.createDirectories(root);
            log.info("已自动创建插件根目录: {}", root.toAbsolutePath());
            return true;
        } catch (IOException e) {
            log.warn("无法创建插件根目录 {}: {}", root.toAbsolutePath(), e.getMessage());
            return false;
        }
    }

    private static URL[] toUrls(List<Path> jars) throws MalformedURLException {
        URL[] arr = new URL[jars.size()];
        for (int i = 0; i < jars.size(); i++) {
            arr[i] = jars.get(i).toUri().toURL();
        }
        return arr;
    }
}
