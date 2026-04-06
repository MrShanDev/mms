package com.sxpcwlkj.system.service;

import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.host.PluginVersionCoordinate;
import com.sxpcwlkj.system.entity.SysPluginVersion;

import java.util.List;
import java.util.Set;

public interface SysPluginVersionService {

    List<PluginVersionCoordinate> listActiveCoordinatesForTenant(String tenantId);

    Set<String> pluginIdsManagedInTenant(String tenantId);

    void recordInstallSuccess(PluginDescriptor descriptor, String tenantId);

    void afterUninstallFromDisk(String pluginId, String versionOrNullMeansAll, String tenantId);

    void activateVersionOnDisk(String pluginId, String version, String tenantId);

    /**
     * 将该插件在租户下所有版本的 {@code is_active} 置 0；不删磁盘、不删版本行。
     * 全量重载后宿主不再加载该插件（仍属「库表受管」，不会走孤儿磁盘批）。
     */
    void deactivateAllVersionsForPlugin(String pluginId, String tenantId);

    List<SysPluginVersion> listVersionsForPlugin(String pluginId, String tenantId);

    /** registry 租户下该插件是否仍有版本登记行 */
    boolean hasVersionRowsForPlugin(String pluginId, String tenantId);
}
