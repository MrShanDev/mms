package com.sxpcwlkj.plugin.data;

import com.sxpcwlkj.plugin.HostDataService;

import java.util.Optional;

/**
 * 未装配 {@link HostDataService} 桥接时的占位实现。
 */
public enum EmptyHostDataService implements HostDataService {
    INSTANCE;

    @Override
    public Optional<PluginHostUserSnapshot> tryCurrentWebUser() {
        return Optional.empty();
    }

    @Override
    public Optional<String> tryCurrentTenantId() {
        return Optional.empty();
    }

    @Override
    public Optional<PluginHostUserSnapshot> findUserByIdInCurrentScope(String userId) {
        return Optional.empty();
    }
}
