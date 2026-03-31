package com.sxpcwlkj.plugin.host;

/**
 * 卸载磁盘上的插件目录；{@code version} 为空则删除该 {@code pluginId} 下全部版本。
 */
public record PluginUninstallRequest(String pluginId, String version) {
}
