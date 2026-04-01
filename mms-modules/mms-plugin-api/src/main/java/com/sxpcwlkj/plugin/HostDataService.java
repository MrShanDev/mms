package com.sxpcwlkj.plugin;

import com.sxpcwlkj.plugin.data.PluginHostUserSnapshot;

import java.util.Optional;

/**
 * 宿主业务数据受控读接口；插件禁止直连 {@code mms-system} Mapper。
 * <p>无 HTTP 登录上下文时（如 ApplicationReady 加载插件）各方法通常返回 {@link Optional#empty()}。</p>
 */
public interface HostDataService {

    /**
     * 当前管理端 Web 登录用户（依赖 Sa-Token 会话与 Redis 中的用户缓存）。
     */
    Optional<PluginHostUserSnapshot> tryCurrentWebUser();

    /**
     * 当前租户号；未登录时返回 empty（不返回默认租户占位，以免误判）。
     */
    Optional<String> tryCurrentTenantId();

    /**
     * 在当前登录租户/权限范围内查询用户摘要（走 {@code SysUserService}，含职级与超级管理员等既有规则）。
     */
    Optional<PluginHostUserSnapshot> findUserByIdInCurrentScope(String userId);
}
