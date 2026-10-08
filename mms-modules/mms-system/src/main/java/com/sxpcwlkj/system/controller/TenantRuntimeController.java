package com.sxpcwlkj.system.controller;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.sxpcwlkj.common.properties.TenantProperties;
import com.sxpcwlkj.common.utils.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("system/config/tenantRuntime")
public class TenantRuntimeController {
    private final TenantProperties properties;
    private final com.sxpcwlkj.system.service.impl.TenantAdministrationService administration;
    @SaCheckRole("super_admin")
    @GetMapping public R<Map<String,Object>> runtime() {
        return R.success(Map.of("enabled",administration.enabled(),"isolationEnabled",true,"column",properties.getColumn(),
                "exclusionTables",properties.getExclusionTable()==null ? java.util.List.of() : properties.getExclusionTable()));
    }
    public record ModeBody(@jakarta.validation.constraints.NotNull Boolean enabled) {}
    @PutMapping @com.sxpcwlkj.common.annotation.PlatformAdmin
    @cn.dev33.satoken.annotation.SaCheckPermission("system:tenant:mode")
    @com.sxpcwlkj.log.annotation.MmsLog(module="应用模式")
    public R<Void> mode(@jakarta.validation.Valid @RequestBody ModeBody body) {administration.mode(body.enabled());return R.success();}
}
