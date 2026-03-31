package com.sxpcwlkj.plugin.host;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.sxpcwlkj.common.utils.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @SaCheckRole("super_admin")
    @GetMapping("/status")
    public R<Map<String, Object>> status() {
        Map<String, Object> body = new HashMap<>();
        body.put("enabled", pluginHostProperties.isEnabled());
        body.put("rootDir", pluginHostProperties.getRootDir());
        body.put("hostMmsRevision", pluginHostProperties.getHostMmsRevision());
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
     * 上传单个插件 JAR（内含 {@code META-INF/mms/plugin.json}），安装到 {@code mms.plugin.root-dir} 后全量重载。
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
        try {
            file.transferTo(temp);
            pluginLifecycleManager.installJarFromUpload(temp);
            pluginLifecycleManager.reload();
            return R.success(pluginLifecycleManager.listSummaries());
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
