package com.sxpcwlkj.plugin.host;

/**
 * {@link PluginHostController#pluginLogTail} 返回：插件独立日志文件尾部（UTF-8 文本）。
 */
public record PluginLogTailVo(String pluginKey, String logPath, String text, boolean truncated, boolean fileMissing) {}
