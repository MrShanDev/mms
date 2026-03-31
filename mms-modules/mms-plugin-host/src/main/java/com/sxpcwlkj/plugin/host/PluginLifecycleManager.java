package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.MmsPlugin;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptorReader;
import com.sxpcwlkj.plugin.PluginDescriptorValidator;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginHealthContributor;
import com.sxpcwlkj.plugin.PluginInstallationLayout;
import com.sxpcwlkj.plugin.PluginKind;
import com.sxpcwlkj.plugin.host.internal.DefaultPluginRuntimeContext;
import com.sxpcwlkj.plugin.host.internal.LoadedPluginInstance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 扫描 {@code mms.plugin.root-dir} 下 {@code <pluginId>/<version>/lib/*.jar}，校验并加载 SPI。
 */
@Slf4j
public class PluginLifecycleManager {

    private final PluginHostProperties properties;
    @Getter
    private final Map<String, LoadedPluginInstance> loadedPlugins = new ConcurrentHashMap<>();

    public PluginLifecycleManager(PluginHostProperties properties) {
        this.properties = properties;
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
        if (!Files.isDirectory(root)) {
            log.info("插件根目录不存在或不是目录: {}，跳过加载", root.toAbsolutePath());
            return;
        }
        String springVer = SpringBootVersion.getVersion();
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
     * 将上传的单个插件 JAR 安装到约定目录并覆盖同名文件；加载需由调用方执行 {@link #reload()}。
     */
    public void installJarFromUpload(Path tempJarFile) throws Exception {
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

    private static URL[] toUrls(List<Path> jars) throws MalformedURLException {
        URL[] arr = new URL[jars.size()];
        for (int i = 0; i < jars.size(); i++) {
            arr[i] = jars.get(i).toUri().toURL();
        }
        return arr;
    }
}
