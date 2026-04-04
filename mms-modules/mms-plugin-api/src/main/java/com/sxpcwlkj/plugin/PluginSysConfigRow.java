package com.sxpcwlkj.plugin;

/**
 * 插件 {@code sys_config} 一行（供列表与宿主返回）。
 */
public record PluginSysConfigRow(String fullConfigKey, String keySuffix, String configName, String configValue) {}
