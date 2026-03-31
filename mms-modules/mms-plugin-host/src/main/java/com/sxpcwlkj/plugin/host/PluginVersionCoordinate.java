package com.sxpcwlkj.plugin.host;

/**
 * 宿主加载插件时使用的「插件 ID + 版本」坐标（与磁盘目录 {@code safeId/safeVer} 一致）。
 */
public record PluginVersionCoordinate(String pluginId, String version) {
}
