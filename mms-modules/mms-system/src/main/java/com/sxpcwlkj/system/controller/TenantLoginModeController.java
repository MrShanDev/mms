package com.sxpcwlkj.system.controller;
import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.system.service.impl.TenantAdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequiredArgsConstructor
@RequestMapping("system/visitor/tenantMode")
public class TenantLoginModeController {
    private final TenantAdministrationService administration;
    @GetMapping @SaIgnore
    public R<Map<String,Boolean>> mode() {return R.success(Map.of("enabled",administration.enabled()));}
}
