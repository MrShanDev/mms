package com.sxpcwlkj.plugin.host;

/**
 * 已加载插件的简要信息（运维接口返回）。
 */
public record PluginEntrySummary(
        String pluginId,
        String version,
        String name,
        String state,
        String detail
) {
}
