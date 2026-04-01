package com.sxpcwlkj.system.pluginhost;

import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.plugin.HostDataService;
import com.sxpcwlkj.plugin.data.PluginHostUserSnapshot;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 将管理端用户/租户读能力桥接到 {@link HostDataService}，供 JAR 插件通过 {@code context.hostServices().hostData()} 访问。
 */
@Component
@RequiredArgsConstructor
public class PluginHostDataServiceBridge implements HostDataService {

    private final SysUserService sysUserService;

    @Override
    public Optional<PluginHostUserSnapshot> tryCurrentWebUser() {
        if (!Boolean.TRUE.equals(LoginObject.isLogin())) {
            return Optional.empty();
        }
        SysUserVo vo = LoginObject.getLoginObject(SysUserVo.class);
        if (vo == null) {
            return Optional.empty();
        }
        return Optional.of(toSnapshot(vo));
    }

    @Override
    public Optional<String> tryCurrentTenantId() {
        if (!Boolean.TRUE.equals(LoginObject.isLogin())) {
            return Optional.empty();
        }
        return Optional.ofNullable(LoginObject.getLoginTenant());
    }

    @Override
    public Optional<PluginHostUserSnapshot> findUserByIdInCurrentScope(String userId) {
        if (userId == null || userId.isBlank()) {
            return Optional.empty();
        }
        if (!Boolean.TRUE.equals(LoginObject.isLogin())) {
            return Optional.empty();
        }
        SysUserVo vo = sysUserService.selectVoById(userId.trim());
        if (vo == null) {
            return Optional.empty();
        }
        if (!Boolean.TRUE.equals(LoginObject.getLoginSuper())) {
            String loginTenant = LoginObject.getLoginTenant();
            if (vo.getTenantId() != null && loginTenant != null && !vo.getTenantId().equals(loginTenant)) {
                return Optional.empty();
            }
        }
        return Optional.of(toSnapshot(vo));
    }

    private static PluginHostUserSnapshot toSnapshot(SysUserVo vo) {
        return new PluginHostUserSnapshot(
                vo.getUserId(), vo.getUserName(), vo.getNickName(), vo.getTenantId());
    }
}
