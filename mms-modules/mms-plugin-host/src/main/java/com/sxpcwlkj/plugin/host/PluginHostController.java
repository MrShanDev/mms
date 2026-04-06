package com.sxpcwlkj.plugin.host;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.plugin.HostDataService;
import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.BundledPluginSchemaExecutor;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptorReader;
import com.sxpcwlkj.plugin.PluginDescriptorValidator;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutionGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 插件宿主运维接口（仅超级管理员；动态 Controller 注册属后续阶段）。
 */
@RestController
@RequestMapping("system/pluginHost")
@RequiredArgsConstructor
public class PluginHostController {

    private final PluginLifecycleManager pluginLifecycleManager;
    private final PluginHostProperties pluginHostProperties;
    private final ObjectProvider<PluginHostDbBridge> pluginHostDbBridge;
    private final ObjectMapper objectMapper;
    private final ObjectProvider<HostServices> hostServicesProvider;
    private final ObjectProvider<HostDataService> hostDataServiceProvider;
    private final PluginMvcExecutionGuard pluginMvcExecutionGuard;
    private final ObjectProvider<BundledPluginSchemaExecutor> bundledPluginSchemaExecutor;
    private final ObjectProvider<JdbcTemplate> jdbcTemplate;

    private static final int BUNDLED_SCHEMA_PREVIEW_MAX_CHARS = 128_000;

    /**
     * 读取插件 JAR 内 {@code META-INF/mms/logo.png}；浏览器 {@code img} 请求不带鉴权，须匿名可读。
     */
    @SaIgnore
    @GetMapping(value = "/pluginLogo", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> pluginLogo(
            @RequestParam String pluginId, @RequestParam(required = false) String version) {
        if (pluginId == null || pluginId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        String pid = pluginId.trim();
        if (pid.length() > 200 || pid.contains("..")) {
            return ResponseEntity.badRequest().build();
        }
        if (version != null && (version.length() > 120 || version.contains(".."))) {
            return ResponseEntity.badRequest().build();
        }
        Optional<byte[]> logo = pluginLifecycleManager.readBundledLogoPng(pid, version);
        if (logo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(logo.get());
    }

    @SaCheckRole("super_admin")
    @GetMapping("/status")
    public R<Map<String, Object>> status() {
        Map<String, Object> body = new HashMap<>();
        body.put("enabled", pluginHostProperties.isEnabled());
        body.put("rootDir", pluginHostProperties.getRootDir());
        body.put("hostMmsRevision", pluginHostProperties.getHostMmsRevision());
        Path resolvedRoot = pluginLifecycleManager.getPluginsRoot();
        body.put("resolvedPluginsRoot", resolvedRoot.toString());
        body.put("pluginsRootReady", Files.isDirectory(resolvedRoot));
        body.put(
                "activateVersionReloadScope",
                pluginHostProperties.getActivateVersionReloadScope().name());
        body.put("plugins", pluginLifecycleManager.listSummaries());
        return R.success(body);
    }

    @SaCheckRole("super_admin")
    @PostMapping("/reload")
    public R<List<PluginEntrySummary>> reload() {
        pluginLifecycleManager.reload();
        return R.success(pluginLifecycleManager.listSummaries());
    }

    @SaCheckRole("super_admin")
    @GetMapping("/health")
    public R<List<PluginHealthRow>> health() {
        return R.success(pluginLifecycleManager.collectHealth());
    }

    /**
     * 插件安装向导第一步：数据源与宿主侧能力探测（不读上传文件）。
     */
    @SaCheckRole("super_admin")
    @GetMapping("/installReadiness")
    public R<Map<String, Object>> installReadiness() {
        Map<String, Object> body = new HashMap<>();
        Path resolvedRoot = pluginLifecycleManager.getPluginsRoot();
        body.put("resolvedPluginsRoot", resolvedRoot.toString());
        body.put("pluginsRootReady", Files.isDirectory(resolvedRoot));
        body.put("pluginHostEnabled", pluginHostProperties.isEnabled());
        body.put("hostMmsRevision", pluginHostProperties.getHostMmsRevision());
        body.put("bundledSchemaExecutorAvailable", bundledPluginSchemaExecutor.getIfAvailable() != null);
        body.put("jdbcAvailable", jdbcTemplate.getIfAvailable() != null);
        body.put("pluginDbBridgeAvailable", pluginHostDbBridge.getIfAvailable() != null);
        return R.success(body);
    }

    /**
     * 反射调用已加载插件 {@link com.sxpcwlkj.plugin.MmsPlugin} 上的公有实例方法（参数个数与 args 一致）。
     */
    @SaCheckRole("super_admin")
    @PostMapping("/invoke")
    public R<Object> invoke(@RequestBody PluginInvokeRequest body) {
        if (body == null || body.pluginId() == null || body.pluginId().isBlank()
                || body.methodName() == null || body.methodName().isBlank()) {
            return R.fail("pluginId 与 methodName 不能为空");
        }
        if (!pluginMvcExecutionGuard.allowBeforeOpsInvoke(body.pluginId())) {
            return R.fail("运维反射调用被限流或熔断");
        }
        try {
            Object out = pluginLifecycleManager
                    .invokePluginMethodForOps(
                            body.pluginId(), body.version(), body.methodName(), body.args(), objectMapper)
                    .orElse(null);
            pluginMvcExecutionGuard.afterSuccessfulOpsInvoke(body.pluginId());
            return R.success(out);
        } catch (IllegalArgumentException ex) {
            pluginMvcExecutionGuard.afterFailedOpsInvoke(body.pluginId(), ex);
            return R.fail(ex.getMessage());
        } catch (IllegalStateException ex) {
            pluginMvcExecutionGuard.afterFailedOpsInvoke(body.pluginId(), ex);
            return R.fail(ex.getMessage());
        } catch (Exception ex) {
            pluginMvcExecutionGuard.afterFailedOpsInvoke(body.pluginId(), ex);
            String msg = ex.getMessage();
            return R.fail(msg != null && !msg.isBlank() ? msg : ex.getClass().getSimpleName());
        }
    }

    /**
     * 当前已加载插件的 manifest 列表（与 JAR 内 {@code plugin.json} 对齐，含 {@code frontend}）。
     */
    @SaCheckRole("super_admin")
    @GetMapping("/manifests")
    public R<List<PluginManifestView>> manifests() {
        return R.success(pluginLifecycleManager.listManifests());
    }

    /**
     * 读取插件独立日志尾部（{@code logback} {@code plugin_sift} → {@code logs/plugins/{pluginId}@{version}.log}）。
     * 仅收录在插件 MDC（{@code pluginKey}）下输出的 <strong>INFO 及以上</strong> 日志；宿主其它日志不在此文件。
     */
    @SaCheckRole("super_admin")
    @GetMapping("/pluginLogTail")
    public R<PluginLogTailVo> pluginLogTail(
            @RequestParam String pluginId,
            @RequestParam(required = false) String version,
            @RequestParam(required = false) Integer maxBytes) {
        int cap = maxBytes == null ? 131072 : Math.min(Math.max(maxBytes, 1024), 2_097_152);
        Optional<String> verOpt = tryResolvePluginLogVersion(pluginId, version);
        if (verOpt.isEmpty()) {
            return R.fail("请指定 version，或先加载该插件（便于自动解析版本）");
        }
        String ver = verOpt.get();
        Path dir = pluginLogsDirectory();
        try {
            String key = PluginLogFileSupport.buildPluginKey(pluginId, ver);
            Path logFile = PluginLogFileSupport.resolveLogFile(dir, key);
            if (!Files.isRegularFile(logFile)) {
                return R.success(
                        new PluginLogTailVo(
                                key,
                                logFile.toString(),
                                "暂无该日志文件。请确认进程工作目录下已生成 logs/plugins，且插件在 MDC 上下文中输出过日志（如 onLoad、HOST_MVC 请求、反射调用等）。",
                                false,
                                true));
            }
            PluginLogFileSupport.TailResult tail = PluginLogFileSupport.readTailUtf8(logFile, cap);
            String text = tail.text();
            if (text.isBlank()) {
                text = "(日志文件为空)";
            }
            return R.success(new PluginLogTailVo(key, logFile.toString(), text, tail.truncated(), false));
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        } catch (IOException ex) {
            return R.fail("读取日志失败: " + ex.getMessage());
        }
    }

    /**
     * 截断清空插件独立日志文件（0 字节）；不影响 logback 后续继续写入同一文件。
     */
    @SaCheckRole("super_admin")
    @PostMapping("/pluginLogClear")
    public R<PluginLogTailVo> pluginLogClear(@RequestBody PluginLogClearRequest body) {
        if (body == null || body.pluginId() == null || body.pluginId().isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        Optional<String> verOpt = tryResolvePluginLogVersion(body.pluginId(), body.version());
        if (verOpt.isEmpty()) {
            return R.fail("请指定 version，或先加载该插件（便于自动解析版本）");
        }
        String ver = verOpt.get();
        Path dir = pluginLogsDirectory();
        try {
            String key = PluginLogFileSupport.buildPluginKey(body.pluginId(), ver);
            Path logFile = PluginLogFileSupport.resolveLogFile(dir, key);
            PluginLogFileSupport.truncateLogFile(logFile);
            return R.success(
                    new PluginLogTailVo(
                            key,
                            logFile.toString(),
                            "(已清空，新日志将继续写入此文件)",
                            false,
                            false));
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        } catch (IOException ex) {
            return R.fail("清空日志失败: " + ex.getMessage());
        }
    }

    private Path pluginLogsDirectory() {
        String rawDir = pluginHostProperties.getPluginLogDir();
        if (rawDir != null && !rawDir.isBlank()) {
            return Path.of(rawDir.trim()).toAbsolutePath().normalize();
        }
        return PluginLogFileSupport.defaultPluginsLogDirectory();
    }

    private Optional<String> tryResolvePluginLogVersion(String pluginId, String versionOrNull) {
        if (pluginId == null || pluginId.isBlank()) {
            return Optional.empty();
        }
        String ver = versionOrNull;
        if (ver == null || ver.isBlank()) {
            ver = pluginLifecycleManager.listManifests().stream()
                    .filter(m -> pluginId.equals(m.id()))
                    .map(PluginManifestView::version)
                    .findFirst()
                    .orElse(null);
        }
        if (ver == null || ver.isBlank()) {
            ver = pluginLifecycleManager.listSummaries().stream()
                    .filter(s -> pluginId.equals(s.pluginId()))
                    .map(PluginEntrySummary::version)
                    .findFirst()
                    .orElse(null);
        }
        if (ver == null || ver.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(ver.trim());
    }

    @SaCheckRole("super_admin")
    @GetMapping("/subprocesses")
    public R<List<PluginSubprocessSnapshot>> subprocesses() {
        return R.success(pluginLifecycleManager.listSubprocessSnapshots());
    }

    /**
     * 供独立子进程通过 Token 拉取宿主最小上下文（不经 Sa 登录态）；子进程请求头
     * {@code X-Mms-Plugin-Subprocess-Token} 须与 {@code mms.plugin.subprocess-admin-token} 一致。
     */
    @SaIgnore
    @GetMapping("/subprocessPeer/context")
    public R<Map<String, Object>> subprocessPeerContext(
            @RequestHeader(value = "X-Mms-Plugin-Subprocess-Token", required = false) String token) {
        String expected = pluginHostProperties.getSubprocessAdminToken();
        if (expected == null || expected.isBlank()) {
            return R.fail("未配置 mms.plugin.subprocess-admin-token");
        }
        if (!subprocessAdminTokenMatches(token)) {
            return R.fail("无效或过期的子进程 Token");
        }
        Map<String, Object> body = new HashMap<>();
        body.put("hostMmsRevision", pluginHostProperties.getHostMmsRevision());
        body.put("pluginRegistryTenantId", pluginHostProperties.getSubprocessPeerRegistryTenantId());
        body.put("subprocessPeerPath", "/system/pluginHost/subprocessPeer/context");
        HostServices hs = hostServicesProvider.getIfAvailable();
        body.put("hostServicesContractVersion", hs != null ? hs.hostImplementedContractVersion() : 1);
        body.put("resolvedPluginsRoot", pluginLifecycleManager.getPluginsRoot().toString());
        Map<String, Object> hostDataProbe = new HashMap<>();
        HostDataService hds = hostDataServiceProvider.getIfAvailable();
        if (hds != null) {
            hostDataProbe.put("webUserPresent", hds.tryCurrentWebUser().isPresent());
            hostDataProbe.put("tenantId", hds.tryCurrentTenantId().orElse(null));
            hds.tryCurrentWebUser()
                    .ifPresent(u -> {
                        hostDataProbe.put("userId", u.getUserId());
                        hostDataProbe.put("userName", u.getUserName());
                    });
        } else {
            hostDataProbe.put("webUserPresent", false);
            hostDataProbe.put("tenantId", null);
        }
        body.put("hostData", hostDataProbe);
        return R.success(body);
    }

    /**
     * 子进程拉取已加载 manifest 列表（Token 与 {@link #subprocessPeerContext} 一致）；供跨进程对齐扩展点能力，无登录态。
     */
    @SaIgnore
    @GetMapping("/subprocessPeer/loadedManifests")
    public R<List<PluginManifestView>> subprocessPeerLoadedManifests(
            @RequestHeader(value = "X-Mms-Plugin-Subprocess-Token", required = false) String token) {
        if (!subprocessAdminTokenMatches(token)) {
            return R.fail("无效或过期的子进程 Token");
        }
        return R.success(pluginLifecycleManager.listManifests());
    }

    /**
     * 子进程经 Token 触发宿主侧运维反射（请求体 JSON 与 {@link #invoke} 相同）；参数经 Jackson 反序列化后在宿主 JVM 内 convert。
     */
    @SaIgnore
    @PostMapping("/subprocessPeer/invoke")
    public R<Object> subprocessPeerInvoke(
            @RequestHeader(value = "X-Mms-Plugin-Subprocess-Token", required = false) String token,
            @RequestBody PluginInvokeRequest body) {
        if (!subprocessAdminTokenMatches(token)) {
            return R.fail("无效或过期的子进程 Token");
        }
        return invoke(body);
    }

    private boolean subprocessAdminTokenMatches(String token) {
        String expected = pluginHostProperties.getSubprocessAdminToken();
        return expected != null && !expected.isBlank() && token != null && expected.equals(token);
    }

    /**
     * 从磁盘删除插件目录后全量重载；{@code version} 为空则删除该 {@code pluginId} 下所有版本。
     */
    @SaCheckRole("super_admin")
    @PostMapping("/uninstall")
    public R<Void> uninstall(@RequestBody PluginUninstallRequest body) throws Exception {
        if (body == null || body.pluginId() == null || body.pluginId().isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        pluginLifecycleManager.uninstallFromDisk(body.pluginId(), body.version());
        pluginHostDbBridge.ifAvailable(b -> b.onUninstallDiskFinished(body.pluginId(), body.version()));
        pluginLifecycleManager.reload();
        return R.success();
    }

    /**
     * 切换当前激活版本（磁盘上须已有该版本目录），更新库表后重载。
     * <p>范围由 {@link PluginHostProperties#getActivateVersionReloadScope()} 决定：{@link ActivateVersionReloadScope#FULL}
     * 全量 {@link PluginLifecycleManager#reload()}；{@link ActivateVersionReloadScope#SINGLE_TARGET} 仅
     * {@link PluginLifecycleManager#reloadSingleActivated}（不保证依赖拓扑）。</p>
     */
    @SaCheckRole("super_admin")
    @PostMapping("/activateVersion")
    public R<List<PluginEntrySummary>> activateVersion(@RequestBody PluginActivateVersionRequest body) {
        if (body == null || body.pluginId() == null || body.pluginId().isBlank()
                || body.version() == null || body.version().isBlank()) {
            return R.fail("pluginId 与 version 不能为空");
        }
        PluginHostDbBridge bridge = pluginHostDbBridge.getIfAvailable();
        if (bridge == null) {
            return R.fail("未启用插件库表能力，请执行 sys_plugin_version 脚本并包含 mms-system");
        }
        try {
            bridge.activateInstalledVersion(body.pluginId(), body.version());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return R.fail(ex.getMessage());
        }
        pluginLifecycleManager
                .readInstalledDescriptor(body.pluginId(), body.version())
                .ifPresent(bridge::syncMenuBootstrapFromDescriptor);
        if (pluginHostProperties.getActivateVersionReloadScope() == ActivateVersionReloadScope.SINGLE_TARGET) {
            pluginLifecycleManager.reloadSingleActivated(body.pluginId(), body.version());
        } else {
            pluginLifecycleManager.reload();
        }
        return R.success(pluginLifecycleManager.listSummaries());
    }

    /**
     * 上传 JAR：先校验 {@code plugin.json} 结构与宿主兼容性，写入磁盘成功后写入库表激活版本，再全量重载。
     * <p>若 {@code skipBundledSchemaExecution=false} 且 JAR 内含 {@code META-INF/mms/schema.sql}，则先执行该 DDL（仅超级管理员、白名单语句），再落盘安装。</p>
     */
    @SaCheckRole("super_admin")
    @PostMapping(value = "/install", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Map<String, Object>> install(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "skipBundledSchemaExecution", defaultValue = "true") boolean skipBundledSchemaExecution)
            throws Exception {
        if (file == null || file.isEmpty()) {
            return R.fail("file 不能为空");
        }
        String orig = file.getOriginalFilename();
        if (orig == null || !orig.toLowerCase(Locale.ROOT).endsWith(".jar")) {
            return R.fail("仅支持 .jar 插件包");
        }
        Path temp = Files.createTempFile("mms-plugin-upload-", ".jar");
        PluginDescriptor installed = null;
        try {
            file.transferTo(temp);
            List<String> schemaLog = null;
            if (!skipBundledSchemaExecution) {
                Optional<String> schemaOpt = pluginLifecycleManager.readBundledSchemaSql(temp);
                if (schemaOpt.isPresent() && !schemaOpt.get().isBlank()) {
                    BundledPluginSchemaExecutor executor = bundledPluginSchemaExecutor.getIfAvailable();
                    if (executor == null) {
                        return R.fail(
                                "JAR 内含 schema.sql，但当前环境未启用 BundledPluginSchemaExecutor（需 mms-system + JdbcTemplate）。请改用跳过 DDL 安装，或手工执行 SQL。");
                    }
                    try {
                        schemaLog = executor.executeBundledSchema(schemaOpt.get());
                    } catch (Exception ddlEx) {
                        return R.fail("执行 schema.sql 失败: " + ddlEx.getMessage());
                    }
                }
            }
            try {
                installed = pluginLifecycleManager.installJarFromUpload(temp);
            } catch (PluginException ex) {
                return R.fail("插件包未通过校验: " + ex.getMessage());
            } catch (Exception ex) {
                return R.fail("插件包解析或校验失败: " + ex.getMessage());
            }
            try {
                PluginHostDbBridge bridge = pluginHostDbBridge.getIfAvailable();
                if (bridge != null) {
                    bridge.onInstallSuccess(installed);
                }
            } catch (Exception ex) {
                try {
                    pluginLifecycleManager.uninstallFromDisk(installed.getId(), installed.getVersion());
                } catch (Exception ignored) {
                }
                return R.fail("已回滚磁盘写入。入库失败: " + ex.getMessage());
            }
            pluginLifecycleManager.reload();
            Map<String, Object> body = new HashMap<>();
            body.put("summaries", pluginLifecycleManager.listSummaries());
            body.put("bundledSchemaExecutionLog", schemaLog);
            return R.success(body);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    /**
     * 安装前预览：读取 JAR 内 {@code META-INF/mms/schema.sql} 全文（过长则截断），不写入磁盘、不执行 SQL。
     */
    @SaCheckRole("super_admin")
    @PostMapping(value = "/bundledSchemaPreview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Map<String, Object>> bundledSchemaPreview(@RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return R.fail("file 不能为空");
        }
        String orig = file.getOriginalFilename();
        if (orig == null || !orig.toLowerCase(Locale.ROOT).endsWith(".jar")) {
            return R.fail("仅支持 .jar 插件包");
        }
        Path temp = Files.createTempFile("mms-plugin-schema-preview-", ".jar");
        try {
            file.transferTo(temp);
            PluginDescriptor d;
            try {
                d = PluginDescriptorReader.readFromJar(temp);
                PluginDescriptorValidator.validateStructureOrThrow(d);
            } catch (PluginException ex) {
                return R.fail("插件包未通过校验: " + ex.getMessage());
            } catch (Exception ex) {
                return R.fail("插件包解析失败: " + ex.getMessage());
            }
            Optional<String> schemaOpt = pluginLifecycleManager.readBundledSchemaSql(temp);
            Map<String, Object> body = new HashMap<>();
            body.put("pluginId", d.getId());
            body.put("version", d.getVersion());
            body.put("hasSchema", schemaOpt.isPresent() && !schemaOpt.get().isBlank());
            if (schemaOpt.isEmpty() || schemaOpt.get().isBlank()) {
                body.put("schemaSql", "");
                body.put("truncated", false);
                body.put("byteLength", 0);
            } else {
                String sql = schemaOpt.get();
                body.put("byteLength", sql.getBytes(java.nio.charset.StandardCharsets.UTF_8).length);
                boolean truncated = sql.length() > BUNDLED_SCHEMA_PREVIEW_MAX_CHARS;
                body.put("truncated", truncated);
                body.put("schemaSql", truncated ? sql.substring(0, BUNDLED_SCHEMA_PREVIEW_MAX_CHARS) : sql);
            }
            body.put("bundledSchemaExecutorAvailable", bundledPluginSchemaExecutor.getIfAvailable() != null);
            body.put("name", d.getName());
            body.put("description", d.getDescription());
            body.put("runtimeMode", d.runtimeModeOrDefault().name());
            body.put("dependencies", d.getDependencies());
            body.put(
                    "hasMenuBootstrap",
                    d.getMenuBootstrap() != null
                            && d.getMenuBootstrap().getItems() != null
                            && !d.getMenuBootstrap().getItems().isEmpty());
            body.put(
                    "requiresMmsRevisionMin",
                    d.getRequiresMms() != null ? d.getRequiresMms().getRevisionMin() : null);
            return R.success(body);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
