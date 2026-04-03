package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.HostDataService;
import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.PluginDataAccess;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginSysConfigRow;
import com.sxpcwlkj.plugin.data.EmptyHostDataService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 未装配 {@link HostServices} Bean 时的回退实现（契约版本 1，缓存为空操作）。
 */
public enum NoopHostServices implements HostServices {
    INSTANCE;

    @Override
    public int hostImplementedContractVersion() {
        return 1;
    }

    @Override
    public Optional<String> cacheGetString(String key) {
        return Optional.empty();
    }

    @Override
    public void cachePutString(String key, String value, int ttlSeconds) {
        // no-op
    }

    @Override
    public HostDataService hostData() {
        return EmptyHostDataService.INSTANCE;
    }

    @Override
    public PluginDataAccess pluginDataAccess(PluginDescriptor forPlugin) {
        throw new PluginException("未装配宿主 HostServices：无法使用 pluginDataAccess");
    }

    @Override
    public void runInWritableTransaction(Runnable work) {
        throw new PluginException("未装配宿主 HostServices：无法使用 runInWritableTransaction");
    }

    @Override
    public void runInReadOnlyTransaction(Runnable work) {
        if (work != null) {
            work.run();
        }
    }

    @Override
    public boolean hasWebPermission(String permissionCode) {
        return false;
    }

    @Override
    public Optional<String> pluginSysConfigGet(PluginDescriptor plugin, String keySuffix) {
        return Optional.empty();
    }

    @Override
    public void pluginSysConfigPut(PluginDescriptor plugin, String keySuffix, String configName, String value) {
        throw new PluginException("未装配宿主 HostServices：无法使用 pluginSysConfigPut");
    }

    @Override
    public List<PluginSysConfigRow> pluginSysConfigList(PluginDescriptor plugin) {
        return Collections.emptyList();
    }
}
