package com.sxpcwlkj.system.service.impl;

import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;

/**
 * 管理端登录后缓存在 Redis 的 {@code SysUserVo}（含权限码）与库表角色/菜单一致；
 * 插件安装/卸载会变更 {@code sys_function} 等，需刷新快照，避免用户必须重新登录。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLoginPermissionCacheService {

    private static final Duration ADMIN_CACHE_TTL = Duration.ofHours(24);

    private final SysUserService sysUserService;

    /**
     * 遍历当前 Redis 中已有的 {@code admin:*} 会话缓存，按库表重载权限并写回。
     */
    public void refreshAllCachedAdminUsers() {
        Collection<String> keyList;
        try {
            keyList = RedisUtil.keys(RedisConstant.ADMIN_KEY + "*");
        } catch (Exception e) {
            log.warn("刷新管理端权限缓存：列举 Redis 键失败", e);
            return;
        }
        if (keyList == null || keyList.isEmpty()) {
            return;
        }
        int ok = 0;
        int removed = 0;
        for (String key : keyList) {
            String userId = extractUserId(key);
            if (userId == null || userId.isBlank()) {
                continue;
            }
            try {
                SysUserVo vo = sysUserService.getUserRoleAnfFunctionInfo(userId);
                if (vo == null) {
                    RedisUtil.deleteObject(RedisConstant.ADMIN_KEY + userId);
                    removed++;
                    continue;
                }
                RedisUtil.setCacheObject(RedisConstant.ADMIN_KEY + userId, vo, ADMIN_CACHE_TTL);
                ok++;
            } catch (Exception e) {
                log.debug("刷新管理端权限缓存跳过 userId={}: {}", userId, e.getMessage());
            }
        }
        log.info("插件变更后已刷新管理端权限缓存：成功 {} 条，删除无效 {} 条，扫描键 {} 条", ok, removed, keyList.size());
    }

    private static String extractUserId(String redisKey) {
        if (redisKey == null) {
            return null;
        }
        String prefix = RedisConstant.ADMIN_KEY;
        if (!redisKey.startsWith(prefix)) {
            return null;
        }
        return redisKey.substring(prefix.length());
    }
}
