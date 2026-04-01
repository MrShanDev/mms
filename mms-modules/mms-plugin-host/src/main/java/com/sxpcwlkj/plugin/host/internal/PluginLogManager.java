package com.sxpcwlkj.plugin.host.internal;

/**
 * 统一插件生命周期与扩展点的日志上下文（封装 {@link PluginMdc}），便于后续接入集中 MDC 策略。
 */
public final class PluginLogManager {

    private PluginLogManager() {
    }

    public static void runScoped(String pluginId, String pluginVersion, Runnable work) {
        Runnable pop = PluginMdc.pushPluginContext(pluginId, pluginVersion);
        try {
            work.run();
        } finally {
            pop.run();
        }
    }
}
