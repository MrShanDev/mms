package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.PluginDescriptor;

import java.time.Instant;

/**
 * 单次插件生命周期事件（不可变）。
 */
public record PluginLifecycleEvent(
        PluginLifecycleEventType type,
        String pluginId,
        String version,
        String pluginKey,
        String detailMessage,
        Throwable error,
        Instant occurredAt) {

    public static PluginLifecycleEvent of(
            PluginLifecycleEventType type, PluginDescriptor desc, String detailMessage, Throwable error) {
        if (desc == null) {
            throw new IllegalArgumentException("descriptor 不能为空");
        }
        String id = desc.getId() != null ? desc.getId().trim() : "";
        String ver = desc.getVersion() != null ? desc.getVersion().trim() : "";
        String key = id + "@" + ver;
        return new PluginLifecycleEvent(
                type, id, ver, key, detailMessage, error, Instant.now());
    }

    public static PluginLifecycleEvent of(
            PluginLifecycleEventType type,
            String pluginId,
            String versionOrNull,
            String detailMessage,
            Throwable error) {
        String id = pluginId != null ? pluginId.trim() : "";
        String ver = versionOrNull != null && !versionOrNull.isBlank() ? versionOrNull.trim() : "—";
        String key = ver.equals("—") ? id : id + "@" + ver;
        return new PluginLifecycleEvent(type, id, ver, key, detailMessage, error, Instant.now());
    }
}
