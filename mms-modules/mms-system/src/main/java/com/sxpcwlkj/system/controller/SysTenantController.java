package com.sxpcwlkj.system.controller;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.PlatformAdmin;
import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.entity.bo.TenantQueryBo;
import com.sxpcwlkj.system.service.impl.TenantQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@PlatformAdmin
@RequestMapping("system/tenant")
public class SysTenantController {
    private final TenantQueryService service;
    private final com.sxpcwlkj.system.service.impl.TenantAdministrationService administration;
    @PostMapping("list")
    @SaCheckPermission("system:tenant:list")
    public R<PageResult<SysTenant>> list(@RequestBody TenantQueryBo query) {
        return R.success(service.list(query));
    }
    @GetMapping("{id}") @SaCheckPermission("system:tenant:query")
    public R<SysTenant> query(@PathVariable String id) { return R.success(administration.query(id)); }
    @PostMapping @SaCheckPermission("system:tenant:insert")
    @com.sxpcwlkj.log.annotation.MmsLog(module="租户管理",saveRequestData=false)
    public R<Void> insert(@jakarta.validation.Valid @RequestBody com.sxpcwlkj.system.entity.bo.TenantSaveBo bo) { administration.saveTenant(bo,true);return R.success(); }
    @PutMapping @SaCheckPermission("system:tenant:edit")
    @com.sxpcwlkj.log.annotation.MmsLog(module="租户管理",saveRequestData=false)
    public R<Void> edit(@jakarta.validation.Valid @RequestBody com.sxpcwlkj.system.entity.bo.TenantSaveBo bo) { administration.saveTenant(bo,false);return R.success(); }
}
