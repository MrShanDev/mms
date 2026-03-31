package com.sxpcwlkj.system.service.impl;

import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.host.PluginHostDbBridge;
import com.sxpcwlkj.plugin.host.PluginVersionCoordinate;
import com.sxpcwlkj.system.service.SysPluginVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 管理端插件与库表衔接；宿主启动加载使用租户 {@code 000000}（与默认上架数据一致）。
 */
@Component
@RequiredArgsConstructor
public class PluginHostDbBridgeImpl implements PluginHostDbBridge {

    public static final String PLUGIN_REGISTRY_TENANT = "000000";

    private final SysPluginVersionService sysPluginVersionService;

    @Override
    public Set<String> pluginIdsManagedInDatabase() {
        return sysPluginVersionService.pluginIdsManagedInTenant(PLUGIN_REGISTRY_TENANT);
    }

    @Override
    public List<PluginVersionCoordinate> activeVersionsForStartup() {
        return sysPluginVersionService.listActiveCoordinatesForTenant(PLUGIN_REGISTRY_TENANT);
    }

    @Override
    public void onInstallSuccess(PluginDescriptor descriptor) {
        sysPluginVersionService.recordInstallSuccess(descriptor, PLUGIN_REGISTRY_TENANT);
    }

    @Override
    public void onUninstallDiskFinished(String pluginId, String versionOrNullMeansAll) {
        sysPluginVersionService.afterUninstallFromDisk(pluginId, versionOrNullMeansAll, PLUGIN_REGISTRY_TENANT);
    }

    @Override
    public void activateInstalledVersion(String pluginId, String version) {
        sysPluginVersionService.activateVersionOnDisk(pluginId, version, PLUGIN_REGISTRY_TENANT);
    }
}
