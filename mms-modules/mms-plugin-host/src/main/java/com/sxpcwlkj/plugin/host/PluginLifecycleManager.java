package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.MmsPlugin;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptorReader;
import com.sxpcwlkj.plugin.PluginDescriptorValidator;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginHealthContributor;
import com.sxpcwlkj.plugin.PluginConstants;
import com.sxpcwlkj.plugin.PluginInstallationLayout;
import com.sxpcwlkj.plugin.PluginKind;
import com.sxpcwlkj.plugin.PluginRuntimeMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.plugin.host.internal.DefaultPluginBeanRegistrar;
import com.sxpcwlkj.plugin.host.internal.DefaultPluginRuntimeContext;
import com.sxpcwlkj.plugin.host.internal.LoadedPluginInstance;
import com.sxpcwlkj.plugin.host.internal.PluginSpringBeanAttachment;
import com.sxpcwlkj.plugin.host.internal.NoopHostServices;
import com.sxpcwlkj.plugin.host.internal.PluginDependencySort;
import com.sxpcwlkj.plugin.host.internal.PluginDescriptorProbe;
import com.sxpcwlkj.plugin.host.internal.PluginLoadedPeerDependencyValidator;
import com.sxpcwlkj.plugin.host.internal.PluginMdc;
import com.sxpcwlkj.plugin.host.internal.PluginReflectionSupport;
import com.sxpcwlkj.plugin.host.internal.PluginSubprocessManager;
import com.sxpcwlkj.plugin.host.internal.PortLeaseTracker;
import com.sxpcwlkj.plugin.host.internal.PortManager;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutorRegistry;
import com.sxpcwlkj.plugin.host.web.PluginMvcRegistrar;
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
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * 扫描 {@code mms.plugin.root-dir} 下 {@code <pluginId>/<version>/lib/*.jar}，校验并加载 SPI。
 */
@Slf4j
public class PluginLifecycleManager {

    private final PluginHostProperties properties;
    private final ObjectProvider<PluginHostDbBridge> dbBridgeProvider;
    private final ObjectProvider<HostServices> hostServicesProvider;
    private final ObjectProvider<PluginMvcRegistrar> pluginMvcRegistrarProvider;
    private final ObjectProvider<PluginSpringBeanAttachment> pluginSpringBeanAttachmentProvider;
    private final ObjectProvider<PluginMvcExecutorRegistry> pluginMvcExecutorRegistryProvider;
    private final ObjectProvider<PluginLifecycleEventListener> pluginLifecycleEventListeners;
    @Getter
    private final Map<String, LoadedPluginInstance> loadedPlugins = new ConcurrentHashMap<>();

    private final PluginSubprocessManager subprocessManager = new PluginSubprocessManager();
    private final PortLeaseTracker portLeases = new PortLeaseTracker();

    public PluginLifecycleManager(
            PluginHostProperties properties,
            ObjectProvider<PluginHostDbBridge> dbBridgeProvider,
            ObjectProvider<HostServices> hostServicesProvider,
            ObjectProvider<PluginMvcRegistrar> pluginMvcRegistrarProvider,
            ObjectProvider<PluginSpringBeanAttachment> pluginSpringBeanAttachmentProvider,
            ObjectProvider<PluginMvcExecutorRegistry> pluginMvcExecutorRegistryProvider,
            ObjectProvider<PluginLifecycleEventListener> pluginLifecycleEventListeners) {
        this.properties = properties;
        this.dbBridgeProvider = dbBridgeProvider;
        this.hostServicesProvider = hostServicesProvider;
        this.pluginMvcRegistrarProvider = pluginMvcRegistrarProvider;
        this.pluginSpringBeanAttachmentProvider = pluginSpringBeanAttachmentProvider;
        this.pluginMvcExecutorRegistryProvider = pluginMvcExecutorRegistryProvider;
        this.pluginLifecycleEventListeners = pluginLifecycleEventListeners;
    }

    /**
     * 供宿主外（如 mms-system 市场删除）发布插件生命周期事件。
     */
    public void publishPluginLifecycleEvent(PluginLifecycleEvent event) {
        dispatchPluginLifecycleEvent(event);
    }

    private void dispatchPluginLifecycleEvent(PluginLifecycleEvent event) {
        if (!properties.isEnabled()) {
            return;
        }
        pluginLifecycleEventListeners.orderedStream()
                .forEach(
                        l -> CompletableFuture.runAsync(
                                () -> {
                                    try {
                                        l.onPluginLifecycleEvent(event);
                                    } catch (Throwable t) {
                                        log.debug("PluginLifecycleEventListener 失败: {}", t.getMessage());
                                    }
                                }));
    }

    public List<PluginEntrySummary> listSummaries() {
        List<PluginEntrySummary> list = new ArrayList<>();
        for (LoadedPluginInstance lp : loadedPlugins.values()) {
            PluginDescriptor d = lp.getDescriptor();
            list.add(new PluginEntrySummary(
                    d.getId(),
                    d.getVersion(),
                    d.getName(),
                    "LOADED",
                    null,
                    d.runtimeModeOrDefault().name()));
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
                    d.getFrontend(),
                    d.runtimeModeOrDefault()));
        }
        list.sort((a, b) -> a.id().compareToIgnoreCase(b.id()));
        return Collections.unmodifiableList(list);
    }

    /**
     * 超级管理员 HTTP 反射调用（args 经 {@link ObjectMapper#convertValue(Object, Class)} 按目标方法形参转换）。
     */
    public Optional<Object> invokePluginMethodForOps(
            String pluginId, String versionOrNull, String methodName, List<?> args, ObjectMapper objectMapper) {
        Object[] arr = args == null ? new Object[0] : args.toArray();
        return PluginReflectionSupport.invokeOnLoaded(
                loadedPlugins, pluginId, versionOrNull, methodName, arr, objectMapper);
    }

    public List<PluginSubprocessSnapshot> listSubprocessSnapshots() {
        List<PluginSubprocessSnapshot> raw = subprocessManager.listSnapshots();
        List<PluginSubprocessSnapshot> out = new ArrayList<>(raw.size());
        for (PluginSubprocessSnapshot s : raw) {
            out.add(enrichSubprocessSnapshot(s));
        }
        return Collections.unmodifiableList(out);
    }

    public PluginSubprocessSnapshot subprocessSnapshotFor(String pluginId, String version) {
        PluginSubprocessSnapshot s = subprocessManager.snapshotFor(pluginId, version);
        return s == null ? null : enrichSubprocessSnapshot(s);
    }

    private PluginSubprocessSnapshot enrichSubprocessSnapshot(PluginSubprocessSnapshot s) {
        Integer leased = portLeases.getLeasedPortForKey(s.pluginKey());
        int hostLeased = leased != null ? leased : 0;
        Boolean tcpBound = null;
        int port = s.effectivePort();
        if (port > 0) {
            tcpBound = !PortManager.isTcpPortAvailable(port);
        }
        return new PluginSubprocessSnapshot(
                s.pluginKey(),
                s.pluginId(),
                s.version(),
                port,
                s.pid(),
                s.alive(),
                s.lastError(),
                hostLeased,
                tcpBound);
    }

    /**
     * 卸载某插件 id 下所有已加载版本（端口租约、子进程、HOST_MVC、隔离线程池一并释放）。
     */
    public synchronized void unloadAllVersionsOfPlugin(String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            return;
        }
        String prefix = pluginId.trim() + "@";
        List<String> keys = loadedPlugins.keySet().stream()
                .filter(k -> k.regionMatches(true, 0, prefix, 0, prefix.length()))
                .toList();
        for (String key : keys) {
            unloadOneKey(key);
        }
    }

    /**
     * 激活版本切换后仅重载指定插件版本（不保证依赖拓扑；见 {@link PluginHostProperties#getActivateVersionReloadScope()}）。
     */
    public synchronized void reloadSingleActivated(String pluginId, String version) {
        if (pluginId == null || pluginId.isBlank() || version == null || version.isBlank()) {
            return;
        }
        String springVer = SpringBootVersion.getVersion();
        Path root = resolveRoot();
        if (!ensurePluginsRoot(root)) {
            log.warn("reloadSingleActivated: 插件根不可用");
            return;
        }
        String dirId = PluginInstallationLayout.safeSegment(pluginId.trim());
        String dirVer = PluginInstallationLayout.safeSegment(version.trim());
        Path verDir = root.resolve(dirId).resolve(dirVer);
        if (!Files.isDirectory(verDir)) {
            log.warn("reloadSingleActivated: 磁盘无该版本目录 {}", verDir);
            return;
        }
        unloadAllVersionsOfPlugin(pluginId.trim());
        try {
            tryLoadOne(dirId, dirVer, verDir, springVer);
        } catch (Exception e) {
            log.warn("reloadSingleActivated 加载失败 {}@{}: {}", pluginId, version, e.getMessage(), e);
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(
                            PluginLifecycleEventType.PLUGIN_RELOAD_TARGET_FAILED,
                            pluginId.trim(),
                            version.trim(),
                            e.getMessage(),
                            e));
        }
    }

    private void unloadOneKey(String key) {
        LoadedPluginInstance lp = loadedPlugins.remove(key);
        if (lp == null) {
            return;
        }
        portLeases.releaseForPluginKey(key);
        subprocessManager.stop(key, properties);
        String pid = lp.getDescriptor().getId();
        pluginMvcRegistrarProvider.ifAvailable(r -> r.unregister(pid));
        pluginMvcExecutorRegistryProvider.ifAvailable(reg -> reg.shutdownForPlugin(pid));
        String ver = lp.getDescriptor().getVersion();
        try {
            lp.close();
            Runnable popOk = PluginMdc.pushPluginContext(pid, ver);
            try {
                log.info("插件已卸载: {}", key);
            } finally {
                popOk.run();
            }
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(PluginLifecycleEventType.PLUGIN_UNLOADED, lp.getDescriptor(), "unloadOneKey", null));
        } catch (Exception e) {
            Runnable popErr = PluginMdc.pushPluginContext(pid, ver);
            try {
                log.error("卸载插件失败: {}", key, e);
            } finally {
                popErr.run();
            }
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(
                            PluginLifecycleEventType.PLUGIN_UNLOAD_FAILED, lp.getDescriptor(), e.getMessage(), e));
        }
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

    /**
     * 磁盘上「按目录名不在库表 plugin_id 集合」的版本：仍按磁盘批处理，兼容旧数据。
     * <p>
     * 若目录名与 plugin.json 的 id 不一致（例如历史上传临时目录名），仅按目录名比对会把「库表已登记的插件」误判为孤儿，
     * 在停用后仍走本批加载。此处探测 descriptor：逻辑 id 已在库表登记则跳过，仅真正未登记 id 的目录才参与孤儿加载。
     */
    private void loadOrphanDiskPlugins(Path root, Set<String> managedPluginIds, String springVer) {
        List<PluginDependencySort.PluginLoadSlot> batch = new ArrayList<>();
        try (Stream<Path> idDirs = Files.list(root)) {
            idDirs.filter(Files::isDirectory).sorted().forEach(idDir -> {
                String folderId = idDir.getFileName().toString();
                if (managedPluginIds.contains(folderId)) {
                    return;
                }
                List<PluginDependencySort.PluginLoadSlot> fromFolder = new ArrayList<>();
                try (Stream<Path> versionDirs = Files.list(idDir)) {
                    versionDirs.filter(Files::isDirectory).sorted().forEach(verDir -> {
                        try {
                            var probeOpt = PluginDescriptorProbe.tryRead(verDir);
                            if (probeOpt.isPresent()) {
                                String logicalId = probeOpt.get().descriptor().getId();
                                if (logicalId != null
                                        && managedPluginIds.contains(logicalId.trim())) {
                                    log.debug(
                                            "跳过孤儿磁盘加载（库表已登记该 plugin_id）: folder={} logicalId={} version={}",
                                            folderId,
                                            logicalId.trim(),
                                            verDir.getFileName());
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            log.debug(
                                    "孤儿目录探测 plugin.json 失败，仍参与磁盘加载: {} {}",
                                    verDir,
                                    e.getMessage());
                        }
                        fromFolder.add(
                                new PluginDependencySort.PluginLoadSlot(
                                        folderId, verDir.getFileName().toString(), verDir));
                    });
                } catch (IOException e) {
                    log.warn("列举版本目录失败: {}", idDir, e);
                }
                if (!fromFolder.isEmpty()) {
                    log.info("插件目录未纳入库表（按目录名），参与磁盘批处理加载: {}", folderId);
                    batch.addAll(fromFolder);
                }
            });
        } catch (IOException e) {
            log.warn("列举插件根目录失败: {}", root, e);
        }
        loadSlotsInDependencyOrder(batch, springVer);
    }

    private void scanAllDiskVersions(Path root, String springVer) {
        List<PluginDependencySort.PluginLoadSlot> batch = new ArrayList<>();
        try (Stream<Path> pluginIdDirs = Files.list(root)) {
            pluginIdDirs.filter(Files::isDirectory).sorted().forEach(idDir -> {
                try (Stream<Path> versionDirs = Files.list(idDir)) {
                    versionDirs.filter(Files::isDirectory).sorted().forEach(verDir -> batch.add(
                            new PluginDependencySort.PluginLoadSlot(
                                    idDir.getFileName().toString(),
                                    verDir.getFileName().toString(),
                                    verDir)));
                } catch (IOException e) {
                    log.warn("列举版本目录失败: {}", idDir, e);
                }
            });
        } catch (IOException e) {
            log.warn("列举插件根目录失败: {}", root, e);
        }
        loadSlotsInDependencyOrder(batch, springVer);
    }

    private void loadSlotsInDependencyOrder(List<PluginDependencySort.PluginLoadSlot> slots, String springVer) {
        if (slots.isEmpty()) {
            return;
        }
        List<PluginDependencySort.PluginLoadSlot> ordered = new ArrayList<>();
        try {
            ordered.addAll(PluginDependencySort.sortDiskSlots(slots));
        } catch (Exception e) {
            log.warn("磁盘插件依赖拓扑排序失败，按目录字典序加载: {}", e.getMessage());
        }
        Set<PluginDependencySort.PluginLoadSlot> seen = new LinkedHashSet<>(ordered);
        if (ordered.isEmpty()) {
            ordered.addAll(slots);
            ordered.sort(Comparator.comparing(PluginDependencySort.PluginLoadSlot::folderPluginId, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(PluginDependencySort.PluginLoadSlot::folderVersion, String.CASE_INSENSITIVE_ORDER));
        } else {
            for (PluginDependencySort.PluginLoadSlot s : slots) {
                if (!seen.contains(s)) {
                    ordered.add(s);
                }
            }
        }
        for (PluginDependencySort.PluginLoadSlot s : ordered) {
            try {
                tryLoadOne(s.folderPluginId(), s.folderVersion(), s.versionDir(), springVer);
            } catch (Exception e) {
                log.warn("插件目录加载失败 {} : {}", s.versionDir().toAbsolutePath(), e.getMessage());
            }
        }
    }

    private void loadCoordinates(Path root, List<PluginVersionCoordinate> coords, String springVer) {
        List<PluginVersionCoordinate> ordered = new ArrayList<>();
        try {
            ordered.addAll(PluginDependencySort.sortCoordinates(root, coords));
        } catch (Exception e) {
            log.warn("插件依赖拓扑排序失败，按列表原序加载: {}", e.getMessage());
        }
        Set<PluginVersionCoordinate> seen = new LinkedHashSet<>(ordered);
        for (PluginVersionCoordinate c : coords) {
            if (c.pluginId() != null && c.version() != null && !seen.contains(c)) {
                ordered.add(c);
            }
        }
        for (PluginVersionCoordinate c : ordered) {
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

    /**
     * 库表存在该 plugin_id 的任一条版本行时，仅当 descriptor 与「当前激活坐标」一致才允许加载；
     * 无库表桥接或库表无任何版本行时，保持纯磁盘全量扫描兼容。
     */
    private boolean isAllowedToLoadByDatabase(PluginDescriptor desc) {
        PluginHostDbBridge bridge = dbBridgeProvider.getIfAvailable();
        if (bridge == null) {
            return true;
        }
        Set<String> managed = bridge.pluginIdsManagedInDatabase();
        if (managed == null || managed.isEmpty()) {
            return true;
        }
        String pid = desc.getId();
        if (pid == null || pid.isBlank()) {
            return false;
        }
        String pidTrim = pid.trim();
        boolean inManaged = false;
        for (String m : managed) {
            if (m != null && pidTrim.equalsIgnoreCase(m.trim())) {
                inManaged = true;
                break;
            }
        }
        if (!inManaged) {
            return true;
        }
        List<PluginVersionCoordinate> active = bridge.activeVersionsForStartup();
        if (active == null || active.isEmpty()) {
            return false;
        }
        String ver = desc.getVersion();
        if (ver == null || ver.isBlank()) {
            return false;
        }
        String verTrim = ver.trim();
        for (PluginVersionCoordinate c : active) {
            if (c.pluginId() == null || c.version() == null) {
                continue;
            }
            if (pidTrim.equalsIgnoreCase(c.pluginId().trim())
                    && verTrim.equalsIgnoreCase(c.version().trim())) {
                return true;
            }
        }
        return false;
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
        var probeOpt = PluginDescriptorProbe.tryRead(versionDir);
        if (probeOpt.isEmpty()) {
            log.debug("目录下未找到含 plugin.json 的 jar: {}", libDir);
            return;
        }
        final PluginDescriptor desc = probeOpt.get().descriptor();
        final Path descJar = probeOpt.get().descriptorJar();
        PluginDescriptorValidator.validateStructureOrThrow(desc);
        PluginCompatibilityChecker.verifyDependencyFingerprintOrThrow(descJar, desc);
        if (!PluginDescriptorValidator.satisfiesHost(desc, properties.getHostMmsRevision(), springVer)) {
            throw new PluginException("插件与宿主版本不兼容: " + desc.getId() + " requiresMms 与当前 revision / Spring Boot 不匹配");
        }
        HostServices hostServices = hostServicesProvider.getIfAvailable();
        if (hostServices == null) {
            hostServices = NoopHostServices.INSTANCE;
        }
        PluginDescriptorValidator.validateHostServicesContractOrThrow(desc, hostServices.hostImplementedContractVersion());
        if (!isAllowedToLoadByDatabase(desc)) {
            log.info(
                    "跳过加载（库表未激活该坐标）: {}@{}，磁盘目录={}@{}",
                    desc.getId(),
                    desc.getVersion(),
                    dirPluginId,
                    dirVersion);
            return;
        }
        try {
            PluginLoadedPeerDependencyValidator.validateOrThrow(desc, loadedPlugins);
        } catch (PluginException e) {
            Runnable popWarn = PluginMdc.pushPluginContext(desc.getId(), desc.getVersion());
            try {
                log.warn("插件依赖未满足，跳过装入: {} — {}", desc.getId() + "@" + desc.getVersion(), e.getMessage());
            } finally {
                popWarn.run();
            }
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(
                            PluginLifecycleEventType.PLUGIN_DEPENDENCY_MISSING, desc, e.getMessage(), e));
            throw e;
        }
        String key = desc.getId() + "@" + desc.getVersion();
        int subprocessEffectivePort = 0;
        boolean subprocessPortLeased = false;
        if (properties.isSubprocessLaunchEnabled()
                && desc.runtimeModeOrDefault() == PluginRuntimeMode.INDEPENDENT_PROCESS) {
            if (desc.getMainClass() == null || desc.getMainClass().isBlank()) {
                throw new PluginException(
                        "INDEPENDENT_PROCESS 且开启子进程启动时须配置 mainClass: " + desc.getId());
            }
            portLeases.releaseForPluginKey(key);
            Integer decl = desc.getIndependentPort();
            if (decl == null || decl <= 0) {
                subprocessEffectivePort = portLeases.leaseInRange(
                        properties.getSubprocessPortRangeMin(),
                        properties.getSubprocessPortRangeMax(),
                        key);
            } else {
                subprocessEffectivePort = portLeases.leaseExplicit(decl, key);
            }
            subprocessPortLeased = true;
        }
        // INDEPENDENT_PROCESS：onLoad 仍在宿主 JVM 执行；子进程仅附加运行时，详见 MmsPlugin 说明。
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
        if (desc.getKind() == PluginKind.EXTENSION && entries.isEmpty()) {
            ucl.close();
            if (subprocessPortLeased) {
                portLeases.releaseForPluginKey(key);
            }
            throw new PluginException("插件声明为 extension 但未发现 SPI 实现: " + key + "，请检查 " + "META-INF/services/com.sxpcwlkj.plugin.MmsPlugin");
        }
        String loadSessionId = UUID.randomUUID().toString();
        PluginSpringBeanAttachment springAttach = pluginSpringBeanAttachmentProvider.getIfAvailable();
        DefaultPluginBeanRegistrar pluginBeanRegistrar = new DefaultPluginBeanRegistrar(springAttach, loadSessionId);
        DefaultPluginRuntimeContext ctx = new DefaultPluginRuntimeContext(
                properties.getHostMmsRevision(),
                springVer,
                versionDir.toAbsolutePath(),
                desc,
                hostServices,
                pluginBeanRegistrar,
                this::invokePeerPluginOnPeer);
        Files.createDirectories(ctx.pluginDataDirectory());
        Files.createDirectories(ctx.pluginTemporaryDirectory());
        List<Runnable> unloadHooks;
        try {
            Runnable popMdc = PluginMdc.pushPluginContext(desc.getId(), desc.getVersion());
            ClassLoader oldTccl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(ucl);
            try {
                for (MmsPlugin p : entries) {
                    p.onLoad(ctx);
                }
                log.info(
                        "插件 onLoad 成功: {} (MmsPlugin 入口 {} 个, PluginHealthContributor {} 个)",
                        key,
                        entries.size(),
                        healthEntries.size());
                if (healthEntries.isEmpty()) {
                    log.warn("插件未注册 PluginHealthContributor SPI，建议补充健康探针: {}", key);
                }
            } catch (Exception ex) {
                log.error("插件 onLoad 失败: {}", key, ex);
                throw ex;
            } finally {
                Thread.currentThread().setContextClassLoader(oldTccl);
                popMdc.run();
            }
            unloadHooks = ctx.takeUnloadHooksSnapshot();
        } catch (Exception e) {
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(PluginLifecycleEventType.PLUGIN_LOAD_FAILED, desc, e.getMessage(), e));
            if (springAttach != null) {
                springAttach.releaseSession(loadSessionId);
            }
            if (subprocessPortLeased) {
                portLeases.releaseForPluginKey(key);
            }
            try {
                ucl.close();
            } catch (Exception ignored) {
                // ignore
            }
            throw e;
        }
        try {
            if (desc.runtimeModeOrDefault() == PluginRuntimeMode.HOST_MVC) {
                pluginMvcRegistrarProvider.ifAvailable(
                        r -> {
                            try {
                                r.registerMvc(desc, ucl);
                            } catch (Exception ex) {
                                throw new PluginException("HOST_MVC 路由注册失败: " + key, ex);
                            }
                        });
            } else {
                pluginMvcRegistrarProvider.ifAvailable(r -> r.unregister(desc.getId()));
            }
            Runnable releasePluginSpringBeans =
                    () -> {
                        if (springAttach != null) {
                            springAttach.releaseSession(loadSessionId);
                        }
                    };
            LoadedPluginInstance loaded =
                    new LoadedPluginInstance(desc, ucl, entries, healthEntries, unloadHooks, releasePluginSpringBeans);
            LoadedPluginInstance previousLoaded = loadedPlugins.put(key, loaded);
            if (previousLoaded != null) {
                String prevKey =
                        previousLoaded.getDescriptor().getId() + "@" + previousLoaded.getDescriptor().getVersion();
                portLeases.releaseForPluginKey(prevKey);
                subprocessManager.stop(prevKey, properties);
                try {
                    previousLoaded.close();
                } catch (Exception e) {
                    String prevId = previousLoaded.getDescriptor().getId();
                    String prevVer = previousLoaded.getDescriptor().getVersion();
                    Runnable popPrev = PluginMdc.pushPluginContext(prevId, prevVer);
                    try {
                        log.error("替换插件时关闭旧 ClassLoader 失败: {}", prevKey, e);
                    } finally {
                        popPrev.run();
                    }
                }
                if (properties.getSubprocessRollingStopDelayMillis() > 0) {
                    try {
                        Thread.sleep(properties.getSubprocessRollingStopDelayMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            subprocessManager.startIfEnabled(properties, desc, versionDir.toAbsolutePath(), key, subprocessEffectivePort);
            Runnable popReady = PluginMdc.pushPluginContext(desc.getId(), desc.getVersion());
            try {
                String subPort =
                        subprocessEffectivePort > 0 ? String.valueOf(subprocessEffectivePort) : "—";
                log.info(
                        "插件就绪: {} runtimeMode={} 子进程监听端口={} descriptor={}",
                        key,
                        desc.runtimeModeOrDefault(),
                        subPort,
                        descJar.getFileName());
            } finally {
                popReady.run();
            }
            log.info("插件已加载: {} (descriptor from {})", key, descJar.getFileName());
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(
                            PluginLifecycleEventType.PLUGIN_LOADED,
                            desc,
                            "descriptor=" + descJar.getFileName(),
                            null));
        } catch (Exception e) {
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(PluginLifecycleEventType.PLUGIN_LOAD_FAILED, desc, e.getMessage(), e));
            throw e;
        }
    }

    private Optional<Object> invokePeerPluginOnPeer(String targetPluginId, String methodName, Object[] args) {
        return PluginReflectionSupport.invokeOnLoaded(
                loadedPlugins,
                targetPluginId,
                null,
                methodName,
                args != null ? args : new Object[0],
                null);
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
                log.debug("插件未注册 PluginHealthContributor SPI: {}@{}", pid, ver);
                rows.add(new PluginHealthRow(pid, ver, null, "NO_HEALTH_SPI"));
                continue;
            }
            for (PluginHealthContributor h : lp.getHealthContributors()) {
                try {
                    rows.add(new PluginHealthRow(pid, ver, h.health(), "OK"));
                } catch (Exception e) {
                    log.warn("插件健康探针异常: {}@{} — {}", pid, ver, e.toString());
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
        uninstallFromDisk(pluginId, versionOrNull, true);
    }

    /**
     * 从磁盘卸载：{@code version} 为空则删除该 {@code pluginId} 下所有版本目录。
     *
     * @param publishDiskUninstalledEvent 为 false 时不发 {@link PluginLifecycleEventType#PLUGIN_DISK_UNINSTALLED}
     *                                    （例如市场「删除」将另行发 {@link PluginLifecycleEventType#PLUGIN_PURGED}）。
     */
    public synchronized void uninstallFromDisk(String pluginId, String versionOrNull, boolean publishDiskUninstalledEvent)
            throws IOException {
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
            if (publishDiskUninstalledEvent) {
                dispatchPluginLifecycleEvent(
                        PluginLifecycleEvent.of(
                                PluginLifecycleEventType.PLUGIN_DISK_UNINSTALLED,
                                pluginId.trim(),
                                null,
                                "已从插件根目录删除该插件全部版本目录",
                                null));
            }
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
        if (publishDiskUninstalledEvent) {
            dispatchPluginLifecycleEvent(
                    PluginLifecycleEvent.of(
                            PluginLifecycleEventType.PLUGIN_DISK_UNINSTALLED,
                            pluginId.trim(),
                            versionOrNull,
                            "已从插件根目录删除对应安装目录",
                            null));
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
        PluginCompatibilityChecker.verifyDependencyFingerprintOrThrow(tempJarFile, d);
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

    /**
     * 顺序：停全部子进程 → 释放端口租约 → 注销 HOST_MVC 路由 → 各插件 {@link LoadedPluginInstance#close()}（onUnload → 钩子 → CL）。
     */
    private void unloadAllQuietly() {
        subprocessManager.stopAll(properties);
        pluginMvcExecutorRegistryProvider.ifAvailable(PluginMvcExecutorRegistry::shutdownAll);
        List<LoadedPluginInstance> snap = new ArrayList<>(loadedPlugins.values());
        for (LoadedPluginInstance lp : snap) {
            String k = lp.getDescriptor().getId() + "@" + lp.getDescriptor().getVersion();
            String pid = lp.getDescriptor().getId();
            String ver = lp.getDescriptor().getVersion();
            portLeases.releaseForPluginKey(k);
            pluginMvcRegistrarProvider.ifAvailable(r -> r.unregister(lp.getDescriptor().getId()));
            try {
                lp.close();
                Runnable popOk = PluginMdc.pushPluginContext(pid, ver);
                try {
                    log.info("插件已卸载: {}", k);
                } finally {
                    popOk.run();
                }
                dispatchPluginLifecycleEvent(
                        PluginLifecycleEvent.of(
                                PluginLifecycleEventType.PLUGIN_UNLOADED, lp.getDescriptor(), "reload/uninstallAll 批量卸载", null));
            } catch (Exception e) {
                Runnable popErr = PluginMdc.pushPluginContext(pid, ver);
                try {
                    log.error("卸载插件失败: {}", k, e);
                } finally {
                    popErr.run();
                }
                dispatchPluginLifecycleEvent(
                        PluginLifecycleEvent.of(
                                PluginLifecycleEventType.PLUGIN_UNLOAD_FAILED, lp.getDescriptor(), e.getMessage(), e));
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
