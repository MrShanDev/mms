package com.sxpcwlkj.system.service;

import com.sxpcwlkj.plugin.PluginDescriptor;

/**
 * 插件在 {@code plugin.json} 中声明的 {@code menuBootstrap} 与 {@code sys_function} 同步。
 */
public interface PluginOwnedMenuBootstrapService {

    /**
     * 安装登记成功时：在各租户下按固定主键幂等插入或更新（仅当已有行的 remark 同为 {@code plugin:{pluginId}}，或为插入）。
     */
    void syncOnInstall(PluginDescriptor descriptor);

    /**
     * 插件已无库表版本记录且将清理磁盘时：删除各租户下 remark 归属该插件的菜单及角色绑定。
     */
    void removeAllForPlugin(String pluginId);
}
