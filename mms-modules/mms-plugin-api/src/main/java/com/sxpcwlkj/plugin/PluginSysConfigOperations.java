package com.sxpcwlkj.plugin;

import java.util.List;
import java.util.Optional;

/**
 * 宿主侧对 {@code sys_config} 的受控访问（由 {@code mms-system} 实现并注册为 Spring Bean）。
 * <p>仅允许键名符合 {@link PluginSysConfigKeys#fullKey(String, String)} 规则。</p>
 */
public interface PluginSysConfigOperations {

    Optional<String> get(String tenantId, String fullConfigKey);

    void upsert(String tenantId, String configName, String fullConfigKey, String value);

    /** 列出某插件在当前租户下的全部配置（键前缀 {@link PluginSysConfigKeys#keyPrefix(String)}）。 */
    List<PluginSysConfigRow> listForPlugin(String tenantId, String pluginId);
}
