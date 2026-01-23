package com.sxpcwlkj.app.controller;

import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.sxpcwlkj.app.service.IAppVersionService;
import com.sxpcwlkj.common.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * App版本发布控制器
 *
 * @author Qoder
 * @date 2026-01-22
 */
@Tag(name = "🌳商城模块-App版本管理",description = "App版本管理")
@RestController
@RequestMapping("/app/version")
@RequiredArgsConstructor
public class ApiAppVersionController {

    private final IAppVersionService appVersionService;

    /**
     * 查询最新版本 (适配 uni-app app-upgrade 插件)
     *
     * @param params 请求体参数，包含 versionCode (当前版本号), type (可选默认安卓，1:安卓 2:ios，3:鸿蒙), appCode (可选)
     * @return 适配后的版本信息
     */
    @Operation(summary = "查询最新版本")
    @RequestMapping(value = "/latest", method = {RequestMethod.GET, RequestMethod.POST})
    public R<Map<String, Object>> getLatestVersion(@RequestBody(required = false) Map<String, Object> params) {

        // 1. 获取参数 (默认从请求体获取)
        String appCode = "mms-mobile";
        String type = "1"; // 默认 Android
        String versionCode = null;

        if (params != null) {
            if (params.get("appCode") != null) {
                appCode = String.valueOf(params.get("appCode"));
            }
            if (params.get("type") != null) {
                type = String.valueOf(params.get("type"));
            }
            if (params.get("versionCode") != null) {
                versionCode = String.valueOf(params.get("versionCode"));
            }
        }

        // 2. 映射平台
        String platform = switch (type) {
            case "2" -> "iOS";
            case "3" -> "HarmonyOS";
            default -> "Android";
        };

        // 3. 查询数据库最新版本
        AppVersionVo latest = appVersionService.getLatestVersion(appCode, platform);

        // 4. 组装返回结果
        Map<String, Object> result = new HashMap<>();
        if (latest == null) {
            result.put("status", 0);
            return R.success(result);
        }

        // 5. 比较版本号
        int status = 1; // 默认有新版本
        if (versionCode != null && !versionCode.isEmpty() && !"null".equals(versionCode)) {
            try {
                long current = Long.parseLong(versionCode);
                long latestV = Long.parseLong(latest.getVersionCode());
                status = latestV > current ? 1 : 0;
            } catch (NumberFormatException e) {
                // 如果不是纯数字，则回退到字符串比较
                status = latest.getVersionCode().compareTo(versionCode) > 0 ? 1 : 0;
            }
        }

        // 6. 适配插件字段名
        result.put("status", status);
        result.put("changelog", latest.getReleaseNotes());
        result.put("path", latest.getDownloadUrl());

        // 补充其他有用信息
        result.put("versionName", latest.getVersionName());
        result.put("versionCode", latest.getVersionCode());
        result.put("isForceUpdate", latest.getIsForceUpdate() != null && latest.getIsForceUpdate() == 1);

        return R.success(result);
    }
}
