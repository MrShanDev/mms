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

    List<SysPluginVersion> listVersionsForPlugin(String pluginId, String tenantId);
}
