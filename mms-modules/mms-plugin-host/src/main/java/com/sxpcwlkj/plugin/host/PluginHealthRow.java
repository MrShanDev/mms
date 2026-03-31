package com.sxpcwlkj.plugin.host;

/**
 * {@link com.sxpcwlkj.plugin.PluginHealthContributor} 聚合结果行。
 */
public record PluginHealthRow(
        String pluginId,
        String version,
        String body,
        String state
) {
}
