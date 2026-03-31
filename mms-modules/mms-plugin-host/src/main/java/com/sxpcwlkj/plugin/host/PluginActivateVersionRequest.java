package com.sxpcwlkj.plugin.host;

/**
 * 回切/指定当前激活的已安装版本（磁盘上须存在对应目录）。
 */
public record PluginActivateVersionRequest(String pluginId, String version) {
}
