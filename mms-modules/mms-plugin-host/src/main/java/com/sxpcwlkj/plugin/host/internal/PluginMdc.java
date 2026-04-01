package com.sxpcwlkj.plugin.host.internal;

import org.slf4j.MDC;

/**
 * 插件相关日志 MDC 键（与插件请求/反射调用对齐）。
 */
public final class PluginMdc {

    public static final String PLUGIN_ID = "pluginId";
    public static final String PLUGIN_VERSION = "pluginVersion";
    public static final String PLUGIN_KEY = "pluginKey";

    private PluginMdc() {
    }

    public static void putPlugin(String pluginId, String pluginVersion) {
        if (pluginId != null && !pluginId.isBlank()) {
            MDC.put(PLUGIN_ID, pluginId);
        }
        if (pluginVersion != null && !pluginVersion.isBlank()) {
            MDC.put(PLUGIN_VERSION, pluginVersion);
        }
        if (pluginId != null && pluginVersion != null && !pluginId.isBlank() && !pluginVersion.isBlank()) {
            MDC.put(PLUGIN_KEY, pluginId + "@" + pluginVersion);
        }
    }

    /**
     * 嵌套安全：在 {@code finally} 中执行返回的 {@link Runnable} 以还原 plugin 相关 MDC。
     */
    public static Runnable pushPluginContext(String pluginId, String pluginVersion) {
        final String prevId = MDC.get(PLUGIN_ID);
        final String prevVer = MDC.get(PLUGIN_VERSION);
        final String prevKey = MDC.get(PLUGIN_KEY);
        putPlugin(pluginId, pluginVersion);
        return () -> {
            restoreOrRemove(PLUGIN_ID, prevId);
            restoreOrRemove(PLUGIN_VERSION, prevVer);
            restoreOrRemove(PLUGIN_KEY, prevKey);
        };
    }

    private static void restoreOrRemove(String key, String previous) {
        if (previous != null) {
            MDC.put(key, previous);
        } else {
            MDC.remove(key);
        }
    }
}
