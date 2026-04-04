package com.sxpcwlkj.plugin;

import java.util.regex.Pattern;

/**
 * 插件在 {@code sys_config} 中的键命名：{@code mms.plugin.{pluginId}.{suffix}}，
 * 与系统内置 {@code sys_*} 键隔离，避免冲突。
 */
public final class PluginSysConfigKeys {

    public static final String PREFIX = "mms.plugin.";

    private static final Pattern SUFFIX_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9._-]{0,190}$");

    private PluginSysConfigKeys() {}

    public static String fullKey(String pluginId, String keySuffix) {
        validatePluginId(pluginId);
        validateSuffix(keySuffix);
        return PREFIX + pluginId.trim() + "." + keySuffix.trim();
    }

    /** 用于按前缀列举某插件的全部配置。 */
    public static String keyPrefix(String pluginId) {
        validatePluginId(pluginId);
        return PREFIX + pluginId.trim() + ".";
    }

    public static void validatePluginId(String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            throw new PluginException("pluginId 不能为空");
        }
    }

    public static void validateSuffix(String keySuffix) {
        if (keySuffix == null || keySuffix.isBlank()) {
            throw new PluginException("配置项后缀 keySuffix 不能为空");
        }
        String s = keySuffix.trim();
        if (!SUFFIX_PATTERN.matcher(s).matches()) {
            throw new PluginException(
                    "keySuffix 须以字母开头，仅含字母数字 ._-，长度不超过 191：当前为 " + keySuffix);
        }
    }

    /**
     * 校验 {@code fullConfigKey} 是否属于指定插件（前缀一致）。
     */
    public static boolean belongsToPlugin(String fullConfigKey, String pluginId) {
        if (fullConfigKey == null || pluginId == null) {
            return false;
        }
        String p = keyPrefix(pluginId);
        return fullConfigKey.startsWith(p);
    }

    /** 从完整键解析出 suffix；不属于该插件前缀时返回空串。 */
    public static String extractSuffix(String pluginId, String fullConfigKey) {
        String p = keyPrefix(pluginId);
        if (fullConfigKey == null || !fullConfigKey.startsWith(p)) {
            return "";
        }
        return fullConfigKey.substring(p.length());
    }
}
