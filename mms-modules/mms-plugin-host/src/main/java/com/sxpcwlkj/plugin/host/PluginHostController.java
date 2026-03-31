package com.sxpcwlkj.plugin.host;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 当前已加载插件的 manifest 列表（与 JAR 内 {@code plugin.json} 对齐，含 {@code frontend}）。
     */
    @SaCheckRole("super_admin")
    @GetMapping("/manifests")
    public R<List<PluginManifestView>> manifests() {
        return R.success(pluginLifecycleManager.listManifests());
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
     * 切换当前激活版本（磁盘上须已有该版本目录），更新库表后全量重载。
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
        pluginLifecycleManager.reload();
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
