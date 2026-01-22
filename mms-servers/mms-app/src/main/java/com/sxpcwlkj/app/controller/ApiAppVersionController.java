package com.sxpcwlkj.app.controller;

import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.sxpcwlkj.app.service.IAppVersionService;
import com.sxpcwlkj.common.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 查询最新版本
     * @param appCode 应用代码 mms-mobile
     * @param type 平台 1-Android 2-iOS 3-鸿蒙
     * @return
     */
    @Operation(summary = "查询最新版本")
    @GetMapping("/latest")
    public R<AppVersionVo> getLatestVersion(@RequestParam String appCode, @RequestParam String type) {
        String platform="Android";
        if("1".equals(type)){
            platform="Android";
        }else if("2".equals(type)){
            platform="iOS";
        }else if("3".equals(type)){
            platform="HarmonyOS";
        }
        return R.success(appVersionService.getLatestVersion(appCode, platform));
    }
}
