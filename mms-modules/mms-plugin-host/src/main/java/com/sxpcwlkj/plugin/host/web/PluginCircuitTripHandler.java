package com.sxpcwlkj.plugin.host.web;

/**
 * HOST_MVC 连续失败达到阈值时的扩展（例如自动卸载插件）。
 */
public interface PluginCircuitTripHandler {

    void onTrip(String pluginId, int consecutiveFailures, Throwable lastError);
}
