package com.sxpcwlkj.system.service;

import com.sxpcwlkj.system.entity.SysTenantPackage;

import java.util.Optional;

/**
 * 租户套餐（SaaS 菜单能力包），与 {@code sys_tenant.package_id} 关联。
 */
public interface SysTenantPackageService {

    Optional<SysTenantPackage> findById(String packageId);
}
