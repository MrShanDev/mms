package com.sxpcwlkj.plugin.host;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.context.DemoModeContextHolder;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.plugin.HostDataService;
import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.BundledInstallSqlExecutionException;
import com.sxpcwlkj.plugin.BundledInstallSqlResult;
import com.sxpcwlkj.plugin.BundledInstallSqlSupport;
import com.sxpcwlkj.plugin.BundledPluginSchemaExecutor;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptorReader;
import com.sxpcwlkj.plugin.PluginDescriptorValidator;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginInstallStreamResultCode;
import com.sxpcwlkj.plugin.PluginSysConfigDef;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutionGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
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
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 插件宿主运维接口（超级管理员与管理员；动态 Controller 注册属后续阶段）。
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

    private static final MediaType NDJSON_UTF8 = MediaType.parseMediaType("application/x-ndjson;charset=UTF-8");

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

    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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

    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @PostMapping("/reload")
    public R<List<PluginEntrySummary>> reload() {
        pluginLifecycleManager.reload();
        return R.success(pluginLifecycleManager.listSummaries());
    }

    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @GetMapping("/health")
    public R<List<PluginHealthRow>> health() {
        return R.success(pluginLifecycleManager.collectHealth());
    }

    /**
     * 插件安装向导第一步：数据源与宿主侧能力探测（不读上传文件）。
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @GetMapping("/manifests")
    public R<List<PluginManifestView>> manifests() {
        return R.success(pluginLifecycleManager.listManifests());
    }

    /**
     * 读取插件独立日志尾部（{@code logback} {@code plugin_sift} → {@code logs/plugins/{pluginId}@{version}.log}）。
     * 仅收录在插件 MDC（{@code pluginKey}）下输出的 <strong>INFO 及以上</strong> 日志；宿主其它日志不在此文件。
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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

    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @PostMapping("/uninstall")
    public R<Void> uninstall(@RequestBody PluginUninstallRequest body) throws Exception {
        if (body == null || body.pluginId() == null || body.pluginId().isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        BundledInstallSqlSupport.DeclaredInstallSqlIds installIds =
                pluginLifecycleManager.collectBundledInstallSqlDeclaredIds(body.pluginId(), body.version());
        pluginHostDbBridge.ifAvailable(
                b ->
                        b.removeBundledInstallSqlInsertRows(
                                installIds.sysFunctionIds(),
                                installIds.sysDictIds(),
                                installIds.sysDictDataIds()));
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
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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
     * <p>若 {@code skipBundledSchemaExecution=false}（默认），则依次自动执行 JAR 内 {@code META-INF/mms/schema.sql}（白名单 DDL）、
     * {@code script/install.sql}（白名单：{@code INSERT [IGNORE] INTO sys_function | sys_dict | sys_dict_data}；主键已存在则跳过），再落盘安装。</p>
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @PostMapping(value = "/install", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Map<String, Object>> install(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "skipBundledSchemaExecution", defaultValue = "false") boolean skipBundledSchemaExecution)
            throws Exception {
        if (file == null || file.isEmpty()) {
            return R.fail("file 不能为空");
        }
        String orig = file.getOriginalFilename();
        if (orig == null || !orig.toLowerCase(Locale.ROOT).endsWith(".jar")) {
            return R.fail("仅支持 .jar 插件包");
        }
        Path temp = Files.createTempFile("mms-plugin-upload-", ".jar");
        try {
            file.transferTo(temp);
            InstallRunResult r = runPluginInstall(temp, skipBundledSchemaExecution, null);
            if (!r.ok()) {
                return R.fail(r.message());
            }
            return R.success(r.body());
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    /**
     * 与 {@link #install} 等价，但以 NDJSON 流式输出安装过程（每行一个 JSON 对象），便于前端实时展示。
     * <p>行格式：</p>
     * <ul>
     *   <li>{@code {"type":"line","level":"info|warn|error","text":"..."}}</li>
     *   <li>{@code {"type":"done","ok":true,"code":"PLUGIN_INSTALL_SUCCESS","data":{...}}} 与 {@code /install} 成功体一致（含 summaries、bundledSchemaExecutionLog、bundledInstallSqlExecutionLog、pluginId、version）</li>
     *   <li>{@code {"type":"done","ok":false,"code":"PLUGIN_INSTALL_...","msg":"..."}} — {@code code} 见 {@link PluginInstallStreamResultCode}</li>
     * </ul>
     * <p><b>鉴权</b>：{@link StreamingResponseBody} 会触发 Servlet 异步二次派发，{@code @SaCheckRole} 在派发线程上无 Sa-Token 上下文；
     * 故本接口使用 {@link SaIgnore} 交由拦截器跳过，并在方法入口 {@link StpUtil#checkRole(String...)}（仅首线程执行）。</p>
     * <p><b>演示模式</b>：在派发线程写库前，将当前登录主体传入 {@link DemoModeContextHolder#setPropagatedDemoPrincipal}，
     * 使 {@code DemoModeInterceptor} 与同步请求一致地识别 {@code demo.mode.allowed-users} 与 {@code super_admin}。</p>
     */
    @SaIgnore
    @PostMapping(value = "/installStream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/x-ndjson;charset=UTF-8")
    public ResponseEntity<StreamingResponseBody> installStream(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "skipBundledSchemaExecution", defaultValue = "false") boolean skipBundledSchemaExecution) {
        if (!StpUtil.hasRole("super_admin") && !StpUtil.hasRole("admin")) {
            return badNdjsonDone(PluginInstallStreamResultCode.REQUEST_FORBIDDEN, "需要超级管理员或管理员角色");
        }
        if (file == null || file.isEmpty()) {
            return badNdjsonDone(PluginInstallStreamResultCode.REQUEST_FILE_EMPTY, "file 不能为空");
        }
        String orig = file.getOriginalFilename();
        if (orig == null || !orig.toLowerCase(Locale.ROOT).endsWith(".jar")) {
            return badNdjsonDone(PluginInstallStreamResultCode.REQUEST_NOT_JAR, "仅支持 .jar 插件包");
        }
        Path temp;
        try {
            temp = Files.createTempFile("mms-plugin-upload-stream-", ".jar");
            file.transferTo(temp);
        } catch (Exception e) {
            return badNdjsonDone(
                    PluginInstallStreamResultCode.REQUEST_UPLOAD_FAILED, "接收上传失败: " + e.getMessage());
        }
        Path tempFinal = temp;
        final String demoPropagateUser = captureDemoModeUsernameForPropagate();
        final boolean demoPropagateSuper = captureDemoModeSuperForPropagate();
        StreamingResponseBody stream = outputStream -> {
            DemoModeContextHolder.setPropagatedDemoPrincipal(demoPropagateUser, demoPropagateSuper);
            try {
                try {
                    ProgressEmitter pe =
                            (level, text) -> {
                                try {
                                    writeNdjsonLine(outputStream, level, text);
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            };
                    InstallRunResult r = runPluginInstall(tempFinal, skipBundledSchemaExecution, pe);
                    if (r.ok()) {
                        Map<String, Object> done = new HashMap<>();
                        done.put("type", "done");
                        done.put("ok", true);
                        done.put("code", r.code());
                        done.put("data", r.body());
                        writeNdjsonRaw(outputStream, done);
                    } else {
                        Map<String, Object> done = new HashMap<>();
                        done.put("type", "done");
                        done.put("ok", false);
                        done.put("code", r.code());
                        done.put("msg", r.message());
                        writeNdjsonRaw(outputStream, done);
                    }
                } catch (UncheckedIOException e) {
                    try {
                        Map<String, Object> done = new HashMap<>();
                        done.put("type", "done");
                        done.put("ok", false);
                        done.put("code", PluginInstallStreamResultCode.STREAM_IO_FAILED);
                        done.put("msg", e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                        writeNdjsonRaw(outputStream, done);
                    } catch (IOException ignored) {
                    }
                } catch (Exception e) {
                    try {
                        Map<String, Object> done = new HashMap<>();
                        done.put("type", "done");
                        done.put("ok", false);
                        done.put("code", PluginInstallStreamResultCode.STREAM_INTERNAL_ERROR);
                        done.put("msg", e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                        writeNdjsonRaw(outputStream, done);
                    } catch (IOException ignored) {
                    }
                } finally {
                    try {
                        Files.deleteIfExists(tempFinal);
                    } catch (IOException ignored) {
                    }
                    try {
                        outputStream.flush();
                    } catch (IOException ignored) {
                    }
                }
            } finally {
                DemoModeContextHolder.clearPropagatedDemoPrincipal();
            }
        };
        return ResponseEntity.ok().contentType(NDJSON_UTF8).body(stream);
    }

    private ResponseEntity<StreamingResponseBody> badNdjsonDone(String code, String msg) {
        StreamingResponseBody stream = os -> {
            try {
                Map<String, Object> done = new HashMap<>();
                done.put("type", "done");
                done.put("ok", false);
                done.put("code", code);
                done.put("msg", msg);
                writeNdjsonRaw(os, done);
            } catch (IOException ignored) {
            }
        };
        return ResponseEntity.badRequest().contentType(NDJSON_UTF8).body(stream);
    }

    private void writeNdjsonLine(OutputStream os, String level, String text) throws IOException {
        Map<String, Object> m = new HashMap<>();
        m.put("type", "line");
        m.put("level", level);
        m.put("text", text);
        writeNdjsonRaw(os, m);
    }

    private void writeNdjsonRaw(OutputStream os, Map<String, Object> payload) throws IOException {
        os.write(objectMapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8));
        os.write('\n');
        os.flush();
    }

    @FunctionalInterface
    private interface ProgressEmitter {
        void line(String level, String text);
    }

    private record InstallRunResult(boolean ok, String code, String message, Map<String, Object> body) {
        static InstallRunResult success(Map<String, Object> body) {
            return new InstallRunResult(true, PluginInstallStreamResultCode.SUCCESS, null, body);
        }

        static InstallRunResult failure(String code, String message) {
            return new InstallRunResult(false, code, message, null);
        }
    }

    /**
     * install.sql 经 JdbcTemplate 逐条自动提交，与磁盘/入库不在同一事务；安装后续失败时需按主键删除本次实际插入的字典与菜单行。
     */
    private void rollbackBundledInstallSqlInsertedRows(
            List<String> insertedSysFunctionIds,
            List<String> insertedSysDictIds,
            List<String> insertedSysDictDataIds,
            ProgressEmitter progress) {
        boolean any =
                (insertedSysDictDataIds != null && !insertedSysDictDataIds.isEmpty())
                        || (insertedSysDictIds != null && !insertedSysDictIds.isEmpty())
                        || (insertedSysFunctionIds != null && !insertedSysFunctionIds.isEmpty());
        if (!any) {
            return;
        }
        pluginHostDbBridge.ifAvailable(
                b ->
                        b.removeBundledInstallSqlInsertRows(
                                insertedSysFunctionIds, insertedSysDictIds, insertedSysDictDataIds));
        if (progress != null) {
            int n =
                    (insertedSysDictDataIds != null ? insertedSysDictDataIds.size() : 0)
                            + (insertedSysDictIds != null ? insertedSysDictIds.size() : 0)
                            + (insertedSysFunctionIds != null ? insertedSysFunctionIds.size() : 0);
            progress.line("info", "安装失败：已回滚本次 install.sql 实际插入的行（共 " + n + " 条主键）");
        }
    }

    /**
     * 与历史 {@code /install} 行为一致；{@code progress} 非空时输出分步说明（用于 {@code /installStream}）。
     */
    private InstallRunResult runPluginInstall(
            Path tempJar, boolean skipBundledSchemaExecution, ProgressEmitter progress) {
        BiConsumer<String, String> emit =
                (level, text) -> {
                    if (progress != null) {
                        progress.line(level, text);
                    }
                };

        emit.accept("info", "已接收插件包，开始安装流程…");
        List<String> schemaLog = null;
        List<String> installSqlLog = null;
        /** 本次 install.sql 实际 INSERT 成功的主键，后续步骤失败时用于删行回滚 */
        List<String> installSqlInsertedFunctionIds = List.of();
        List<String> installSqlInsertedDictIds = List.of();
        List<String> installSqlInsertedDictDataIds = List.of();
        if (!skipBundledSchemaExecution) {
            Optional<String> schemaOpt = pluginLifecycleManager.readBundledSchemaSql(tempJar);
            Optional<String> installOpt = pluginLifecycleManager.readBundledInstallSql(tempJar);
            boolean hasSchema = schemaOpt.isPresent() && !schemaOpt.get().isBlank();
            boolean hasInstallSql = installOpt.isPresent() && !installOpt.get().isBlank();
            if (hasSchema || hasInstallSql) {
                BundledPluginSchemaExecutor executor = bundledPluginSchemaExecutor.getIfAvailable();
                if (executor == null) {
                    emit.accept(
                            "error",
                            "JAR 内含 schema.sql 或 script/install.sql，但未启用 BundledPluginSchemaExecutor，无法自动执行");
                    return InstallRunResult.failure(
                            PluginInstallStreamResultCode.SCHEMA_EXECUTOR_MISSING,
                            "JAR 内含包内 SQL，但当前环境未启用 BundledPluginSchemaExecutor（需 mms-system + JdbcTemplate）。可设置 skipBundledSchemaExecution=true 跳过后再装，或手工执行 SQL。");
                }
                if (hasSchema) {
                    emit.accept("info", "建表：正在执行 META-INF/mms/schema.sql…");
                    try {
                        schemaLog = executor.executeBundledSchema(schemaOpt.get());
                        emit.accept("info", "建表：schema.sql 已执行完成");
                        if (schemaLog != null) {
                            for (String line : schemaLog) {
                                if (line != null && !line.isBlank()) {
                                    emit.accept("info", "[DDL] " + line.trim());
                                }
                            }
                        }
                    } catch (Exception ddlEx) {
                        emit.accept("error", "建表：执行 schema.sql 失败 — " + ddlEx.getMessage());
                        return InstallRunResult.failure(
                                PluginInstallStreamResultCode.SCHEMA_EXECUTION_FAILED,
                                "执行 schema.sql 失败: " + ddlEx.getMessage());
                    }
                } else {
                    emit.accept("info", "建表：已跳过（JAR 内无 schema.sql）");
                }
                if (hasInstallSql) {
                    emit.accept("info", "菜单 SQL：正在执行 script/install.sql…");
                    try {
                        BundledInstallSqlResult installOutcome =
                                executor.executeBundledInstallSql(installOpt.get());
                        installSqlLog = installOutcome.logLines();
                        installSqlInsertedFunctionIds = installOutcome.insertedSysFunctionIds();
                        installSqlInsertedDictIds = installOutcome.insertedSysDictIds();
                        installSqlInsertedDictDataIds = installOutcome.insertedSysDictDataIds();
                        emit.accept("info", "菜单 SQL：install.sql 已处理完成");
                        if (installSqlLog != null) {
                            for (String line : installSqlLog) {
                                if (line != null && !line.isBlank()) {
                                    emit.accept("info", "[INSTALL] " + line.trim());
                                }
                            }
                        }
                    } catch (BundledInstallSqlExecutionException partial) {
                        installSqlLog = partial.getLogLines();
                        installSqlInsertedFunctionIds = partial.getInsertedFunctionIds();
                        installSqlInsertedDictIds = partial.getInsertedDictIds();
                        installSqlInsertedDictDataIds = partial.getInsertedDictDataIds();
                        if (installSqlLog != null) {
                            for (String line : installSqlLog) {
                                if (line != null && !line.isBlank()) {
                                    emit.accept("info", "[INSTALL] " + line.trim());
                                }
                            }
                        }
                        rollbackBundledInstallSqlInsertedRows(
                                installSqlInsertedFunctionIds,
                                installSqlInsertedDictIds,
                                installSqlInsertedDictDataIds,
                                progress);
                        emit.accept("error", "install.sql：执行失败 — " + partial.getMessage());
                        return InstallRunResult.failure(
                                PluginInstallStreamResultCode.INSTALL_SQL_EXECUTION_FAILED,
                                "执行 script/install.sql 失败: " + partial.getMessage());
                    } catch (Exception sqlEx) {
                        emit.accept("error", "install.sql：执行失败 — " + sqlEx.getMessage());
                        return InstallRunResult.failure(
                                PluginInstallStreamResultCode.INSTALL_SQL_EXECUTION_FAILED,
                                "执行 script/install.sql 失败: " + sqlEx.getMessage());
                    }
                } else {
                    emit.accept("info", "install.sql：已跳过（JAR 内无 script/install.sql）");
                }
            } else {
                emit.accept("info", "包内 SQL：无 schema.sql 与 install.sql，跳过执行步骤");
            }
        } else {
            emit.accept("info", "已跳过 JAR 内 schema.sql 与 script/install.sql（skipBundledSchemaExecution=true）");
        }

        PluginDescriptor installed;
        try {
            emit.accept("info", "正在校验 plugin.json、宿主兼容性与依赖指纹，并写入插件目录…");
            installed = pluginLifecycleManager.installJarFromUpload(tempJar);
            emit.accept("info", "JAR 已落盘：" + installed.getId() + " @ " + installed.getVersion());
        } catch (PluginException ex) {
            rollbackBundledInstallSqlInsertedRows(
                    installSqlInsertedFunctionIds,
                    installSqlInsertedDictIds,
                    installSqlInsertedDictDataIds,
                    progress);
            emit.accept("error", "磁盘安装失败 — " + ex.getMessage());
            return InstallRunResult.failure(
                    PluginInstallStreamResultCode.JAR_VALIDATION_FAILED, "插件包未通过校验: " + ex.getMessage());
        } catch (Exception ex) {
            rollbackBundledInstallSqlInsertedRows(
                    installSqlInsertedFunctionIds,
                    installSqlInsertedDictIds,
                    installSqlInsertedDictDataIds,
                    progress);
            emit.accept("error", "磁盘安装失败 — " + ex.getMessage());
            return InstallRunResult.failure(
                    PluginInstallStreamResultCode.JAR_INSTALL_FAILED, "插件包解析或校验失败: " + ex.getMessage());
        }

        try {
            PluginHostDbBridge bridge = pluginHostDbBridge.getIfAvailable();
            if (bridge != null) {
                emit.accept("info", "正在写入数据库版本登记，并同步菜单/配置（若 plugin.json 已声明）…");
                bridge.onInstallSuccess(installed);
                emit.accept("info", "数据库登记与同步已完成");
            } else {
                emit.accept("warn", "未启用插件库表桥接：跳过数据库登记（仅完成磁盘安装）");
            }
        } catch (Exception ex) {
            emit.accept("error", "数据库登记失败，正在回滚磁盘 — " + ex.getMessage());
            try {
                pluginLifecycleManager.uninstallFromDisk(installed.getId(), installed.getVersion());
            } catch (Exception ignored) {
            }
            rollbackBundledInstallSqlInsertedRows(
                    installSqlInsertedFunctionIds,
                    installSqlInsertedDictIds,
                    installSqlInsertedDictDataIds,
                    progress);
            return InstallRunResult.failure(
                    PluginInstallStreamResultCode.DATABASE_REGISTRATION_FAILED,
                    "已回滚磁盘与本次 install.sql 写入。入库失败: " + ex.getMessage());
        }

        emit.accept("info", "正在全量重载插件（类加载、路由、联邦资源等）…");
        pluginLifecycleManager.reload();
        emit.accept("info", "插件安装已完成");

        Map<String, Object> body = new HashMap<>();
        body.put("summaries", pluginLifecycleManager.listSummaries());
        body.put("bundledSchemaExecutionLog", schemaLog);
        body.put("bundledInstallSqlExecutionLog", installSqlLog);
        body.put("pluginId", installed.getId());
        body.put("version", installed.getVersion());
        return InstallRunResult.success(body);
    }

    /**
     * 在仍有 Sa Web 上下文的线程调用（如 installStream 入口）：供演示模式拦截器在异步线程匹配 {@code demo.mode.allowed-users}。
     */
    private static String captureDemoModeUsernameForPropagate() {
        try {
            String u = LoginObject.getLoginUserName();
            if (u != null && !u.isBlank()) {
                return u.trim();
            }
        } catch (Exception ignored) {
        }
        try {
            if (StpUtil.isLogin()) {
                Object id = StpUtil.getLoginId();
                if (id != null) {
                    String s = id.toString().trim();
                    if (!s.isEmpty()) {
                        return s;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static boolean captureDemoModeSuperForPropagate() {
        try {
            if (Boolean.TRUE.equals(LoginObject.getLoginSuper())) {
                return true;
            }
        } catch (Exception ignored) {
        }
        try {
            return StpUtil.isLogin() && StpUtil.hasRole(SystemCommonEnum.SUPER_ADMIN.getCode());
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 安装前预览：读取 JAR 内 {@code META-INF/mms/schema.sql} 全文（过长则截断），不写入磁盘、不执行 SQL。
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
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
            Optional<String> installOpt = pluginLifecycleManager.readBundledInstallSql(temp);
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
            boolean hasInstallSql = installOpt.isPresent() && !installOpt.get().isBlank();
            body.put("hasBundledInstallSql", hasInstallSql);
            if (!hasInstallSql) {
                body.put("installSql", "");
                body.put("installSqlTruncated", false);
                body.put("installSqlByteLength", 0);
            } else {
                String isql = installOpt.get();
                body.put("installSqlByteLength", isql.getBytes(java.nio.charset.StandardCharsets.UTF_8).length);
                boolean instTrunc = isql.length() > BUNDLED_SCHEMA_PREVIEW_MAX_CHARS;
                body.put("installSqlTruncated", instTrunc);
                body.put("installSql", instTrunc ? isql.substring(0, BUNDLED_SCHEMA_PREVIEW_MAX_CHARS) : isql);
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
            List<String> sysConfigConfigNames = new ArrayList<>();
            if (d.getSysConfig() != null) {
                for (PluginSysConfigDef row : d.getSysConfig()) {
                    if (row == null) {
                        continue;
                    }
                    String suffix = row.getKeySuffix();
                    if (suffix == null || suffix.isBlank()) {
                        continue;
                    }
                    String cn = row.getConfigName();
                    sysConfigConfigNames.add(
                            cn != null && !cn.isBlank() ? cn.trim() : suffix.trim());
                }
            }
            body.put("sysConfigConfigNames", sysConfigConfigNames);
            body.put(
                    "requiresMmsRevisionMin",
                    d.getRequiresMms() != null ? d.getRequiresMms().getRevisionMin() : null);
            return R.success(body);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
