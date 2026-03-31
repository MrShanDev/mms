package com.sxpcwlkj.plugin;

/**
 * 可选 SPI：宿主可聚合为 HTTP 探测（见 {@code mms-plugin-host} {@code /system/pluginHost/health}）。
 * 实现类可放在与 {@link MmsPlugin} 相同的 JAR 中，并注册
 * {@code META-INF/services/com.sxpcwlkj.plugin.PluginHealthContributor}。
 */
@FunctionalInterface
public interface PluginHealthContributor {

    /**
     * 简短健康结果，如 {@code UP}、{@code JSON 片段}。
     */
    String health();
}
