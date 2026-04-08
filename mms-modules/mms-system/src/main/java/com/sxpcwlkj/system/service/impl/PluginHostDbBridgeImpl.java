package com.sxpcwlkj.system.service.impl;

import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginSysConfigOperations;
import com.sxpcwlkj.plugin.host.PluginHostDbBridge;
import com.sxpcwlkj.plugin.host.PluginVersionCoordinate;
import com.sxpcwlkj.system.service.PluginOwnedMenuBootstrapService;
import com.sxpcwlkj.system.service.SysPluginVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 管理端插件与库表衔接；宿主启动加载使用租户 {@code 000000}（与默认上架数据一致）。
 * <p>安装成功时 {@link #onInstallSuccess} 会在登记租户及 {@code sys_tenant} 全量租户下补全 {@code sys_config}（仅缺键插入），
 * 并按插件 {@code plugin.json} 的 {@code menuBootstrap} 幂等同步 {@code sys_function}。
 * 插件从库表与磁盘彻底卸载后会删除各租户下该插件专属键及插件归属菜单、角色绑定。</p>
 */
@Component
@RequiredArgsConstructor
public class PluginHostDbBridgeImpl implements PluginHostDbBridge {

    public static final String PLUGIN_REGISTRY_TENANT = "000000";

    private final SysPluginVersionService sysPluginVersionService;
    private final ObjectProvider<PluginSysConfigOperations> pluginSysConfigOperations;
    private final ObjectProvider<PluginOwnedMenuBootstrapService> pluginOwnedMenuBootstrapService;
    private final AdminLoginPermissionCacheService adminLoginPermissionCacheService;

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
        adminLoginPermissionCacheService.refreshAllCachedAdminUsers();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onUninstallDiskFinished(String pluginId, String versionOrNullMeansAll) {
        sysPluginVersionService.afterUninstallFromDisk(pluginId, versionOrNullMeansAll, PLUGIN_REGISTRY_TENANT);
        if (shouldPurgeOwnedSysConfig(pluginId, versionOrNullMeansAll)) {
            purgePluginOwnedSysConfigKeys(pluginId);
            pluginOwnedMenuBootstrapService.ifAvailable(s -> s.removeAllForPlugin(pluginId));
        }
        adminLoginPermissionCacheService.refreshAllCachedAdminUsers();
    }

    @Override
    public void purgePluginOwnedSysConfigKeys(String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            return;
        }
        pluginSysConfigOperations.ifAvailable(ops -> ops.deleteAllKeysForPluginAllTenants(pluginId.trim()));
    }

    /**
     * 全部版本从库表移除、或最后一版被卸掉时需清理插件专属 sys_config。
     */
    private boolean shouldPurgeOwnedSysConfig(String pluginId, String versionOrNullMeansAll) {
        if (pluginId == null || pluginId.isBlank()) {
            return false;
        }
        String pid = pluginId.trim();
        if (versionOrNullMeansAll == null || versionOrNullMeansAll.isBlank()) {
            return true;
        }
        return !sysPluginVersionService.hasVersionRowsForPlugin(pid, PLUGIN_REGISTRY_TENANT);
    }

    @Override
    public void activateInstalledVersion(String pluginId, String version) {
        sysPluginVersionService.activateVersionOnDisk(pluginId, version, PLUGIN_REGISTRY_TENANT);
    }

    @Override
    public void syncMenuBootstrapFromDescriptor(PluginDescriptor descriptor) {
        pluginOwnedMenuBootstrapService.ifAvailable(s -> s.syncOnInstall(descriptor));
        adminLoginPermissionCacheService.refreshAllCachedAdminUsers();
    }

    @Override
    public void removeInstallSqlSysFunctionRows(List<String> sysFunctionIds) {
        if (sysFunctionIds == null || sysFunctionIds.isEmpty()) {
            return;
        }
        pluginOwnedMenuBootstrapService.ifAvailable(s -> s.removeSysFunctionRowsByIds(sysFunctionIds));
        adminLoginPermissionCacheService.refreshAllCachedAdminUsers();
    }
}
