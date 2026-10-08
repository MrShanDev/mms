package com.sxpcwlkj.system.controller;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.PlatformAdmin;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.system.entity.*;
import com.sxpcwlkj.system.entity.bo.TenantPackageSaveBo;
import com.sxpcwlkj.system.service.impl.TenantAdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequiredArgsConstructor @PlatformAdmin
@RequestMapping("system/tenantPackage")
public class TenantPackageController {
    private final TenantAdministrationService administration;
    @GetMapping @SaCheckPermission("system:tenantPackage:list")
    public R<List<SysTenantPackage>> list() { return R.success(administration.packageList()); }
    @GetMapping("menus") @SaCheckPermission("system:tenantPackage:list")
    public R<List<SysFunction>> menus() { return R.success(administration.packageMenus()); }
    @PostMapping @SaCheckPermission("system:tenantPackage:edit")
    @com.sxpcwlkj.log.annotation.MmsLog(module="租户套餐",saveRequestData=false)
    public R<Void> save(@jakarta.validation.Valid @RequestBody TenantPackageSaveBo bo) {administration.savePackage(bo);return R.success();}
}
