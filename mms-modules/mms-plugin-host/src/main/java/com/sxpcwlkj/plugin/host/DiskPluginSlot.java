package com.sxpcwlkj.plugin.host;

/**
 * 磁盘上已安装的插件版本目录（{@code lib} 下存在至少一个 jar 时视为可加载）。
 */
public record DiskPluginSlot(String pluginId, String version, boolean libHasJars) {
}
