package com.sxpcwlkj.plugin.host;

/**
 * 清空（截断）插件独立日志文件；{@code version} 为空时按已加载插件自动推断，规则同 {@link PluginHostController#pluginLogTail}。
 */
public record PluginLogClearRequest(String pluginId, String version) {}
