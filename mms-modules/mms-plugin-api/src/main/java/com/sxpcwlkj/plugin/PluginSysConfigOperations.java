package com.sxpcwlkj.plugin;

import java.util.List;
import java.util.Optional;

/**
 * 宿主侧对 {@code sys_config} 的受控访问（由 {@code mms-plugin-server} 实现并注册为 Spring Bean）。
 * <p>读写必须同时传入 {@code pluginId} 与 {@code keySuffix}，由实现内部拼接
 * {@link PluginSysConfigKeys#fullKey(String, String)}，避免越权访问其他插件或系统键。</p>
 */
public interface PluginSysConfigOperations {

    /** 读取当前租户下、指定插件某后缀项的值。 */
    Optional<String> get(String tenantId, String pluginId, String keySuffix);

    /**
     * 插入或更新当前租户下指定插件的配置行（完整键由 pluginId + keySuffix 推导）。
     */
    void upsert(String tenantId, String pluginId, String configName, String keySuffix, String value);

    /**
     * 若当前租户下尚不存在该插件该项则插入；已存在则不改（不覆盖名称与值）。
     * <p>用于插件首次安装时落库默认配置。</p>
     */
    void insertIfAbsent(String tenantId, String pluginId, String configName, String keySuffix, String defaultValue);

    /** 列出某插件在当前租户下的全部配置（按库表登记 pluginId 最长前缀归属过滤）。 */
    List<PluginSysConfigRow> listForPlugin(String tenantId, String pluginId);

    /**
     * 删除<strong>所有租户</strong>下、归属指定 {@code pluginId} 的插件配置行（非简单前缀模糊删，避免误删子 id 插件键）。
     * <p>在插件从库表与磁盘<strong>彻底移除</strong>时由宿主编排调用。</p>
     */
    void deleteAllKeysForPluginAllTenants(String pluginId);
}
