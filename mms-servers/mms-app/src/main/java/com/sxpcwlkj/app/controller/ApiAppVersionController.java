package com.sxpcwlkj.app.controller;

import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.sxpcwlkj.app.service.IAppVersionService;
import com.sxpcwlkj.common.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App版本发布控制器
 *
 * @author Qoder
 * @date 2026-01-22
 */
@Tag(name = "🌳商城模块-App版本管理", description = "App版本管理")
@RestController
@RequestMapping("/api/appUpgrade")
@RequiredArgsConstructor
public class ApiAppVersionController {

    private final IAppVersionService appVersionService;

    /**
     * App 升级检查接口 (适配最新后端接口规范)
     *
     * @param type 平台类型 (1: iOS, 2: Android)
     * @return 适配后的版本信息列表
     */
    @Operation(summary = "App 升级检查")
    @GetMapping("/appUpgrade")
    public R<List<Map<String, Object>>> appUpgrade(@RequestParam(required = false, defaultValue = "2") Integer type) {
        // 1. 映射平台 (规范: 1: iOS, 2: Android)
        String platform = (type != null && type == 1) ? "iOS" : "Android";
        String appCode = "mms";

        // 2. 查询数据库最新版本
        AppVersionVo latest = appVersionService.getLatestVersion(appCode, platform);

        // 3. 组装返回结果 (预期格式为 List)
        List<Map<String, Object>> dataList = new ArrayList<>();
        if (latest != null) {
            Map<String, Object> item = new HashMap<>();
            item.put("versionName", latest.getVersionName());
            item.put("upgradeContent", latest.getReleaseNotes());
            item.put("downloadUrl", latest.getDownloadUrl());
            item.put("forceUpdate", latest.getIsForceUpdate() != null ? latest.getIsForceUpdate() : 0);
            dataList.add(item);
        }

        return R.success(dataList);
    }
}
