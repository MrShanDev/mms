package com.sxpcwlkj.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.service.SysUserService;
import com.sxpcwlkj.framework.utils.AfterCommit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLoginPermissionCacheService {
    private final SysUserService sysUserService;
    private final com.sxpcwlkj.system.mapper.TenantAdministrationMapper tenantAdministration;

    public void revokeUsersForTenant(String tenant) {
        tenantAdministration.userIds(tenant).forEach(this::revokeUser);
    }

    public void revokeUser(String userId) {
        AfterCommit.run(() -> {
            StpUtil.kickout(userId, com.sxpcwlkj.common.enums.DeviceEnum.ADMIN.getType());
            RedisUtil.deleteObject(RedisConstant.ADMIN_KEY + userId);
            RedisUtil.deleteObject(RedisConstant.ADMIN_TENANT_KEY + userId);
            RedisUtil.deleteObject(RedisConstant.ADMIN_NAME + userId);
            RedisUtil.deleteObject(RedisConstant.ENCRYPTION_APP_ID + userId);
        });
    }

    public void refreshUsers(Collection<String> ids) {
        java.util.Set<String> snapshot = new java.util.LinkedHashSet<>(ids);
        AfterCommit.run(() -> snapshot.forEach(this::refreshUser));
    }

    public void refreshUser(String userId) {
        String key = RedisConstant.ADMIN_KEY + userId;
        if (!RedisUtil.getClient().getBucket(key).isExists()) return;
        SysUserVo vo = sysUserService.getUserRoleAnfFunctionInfo(userId);
        if (vo == null || !java.util.Objects.equals(vo.getStatus(), 1)) {
            revokeUser(userId);
            return;
        }
        long millis = RedisUtil.getClient().getBucket(key).remainTimeToLive();
        if (millis > 0) {
            Duration remaining = Duration.ofMillis(millis);
            RedisUtil.setCacheObject(key, vo, remaining);
            RedisUtil.setCacheObject(RedisConstant.ADMIN_NAME + userId, vo.getUserName(), remaining);
            RedisUtil.setCacheObject(RedisConstant.ADMIN_TENANT_KEY + userId, vo.getTenantId(), remaining);
        }
        else RedisUtil.deleteObject(key);
    }

    /** Menu changes affect all cached accounts; role/user changes use refreshUsers. */
    public void refreshAllCachedAdminUsers() {
        AfterCommit.run(() -> {
            Collection<String> keys = RedisUtil.keys(RedisConstant.ADMIN_KEY + "*");
            if (keys != null) for (String key : keys) {
                refreshUser(key.substring(RedisConstant.ADMIN_KEY.length()));
            }
        });
    }
}
