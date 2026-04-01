package com.sxpcwlkj.plugin.host;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.plugin.HostDataService;
import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutionGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        if (pluginHostProperties.getActivateVersionReloadScope() == ActivateVersionReloadScope.SINGLE_TARGET) {
            pluginLifecycleManager.reloadSingleActivated(body.pluginId(), body.version());
        } else {
            pluginLifecycleManager.reload();
        }
        return R.success(pluginLifecycleManager.listSummaries());
    }

    /**
     * 上传 JAR：先校验 {@code plugin.json} 结构与宿主兼容性，写入磁盘成功后写入库表激活版本，再全量重载。
     */
    @SaCheckRole("super_admin")
    @PostMapping(value = "/install", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<List<PluginEntrySummary>> install(@RequestParam("file") MultipartFile file) throws Exception {
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
            return R.success(pluginLifecycleManager.listSummaries());
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
