package com.sxpcwlkj.plugin.host;

/**
 * 独立子进程运维视图（市场卡片 / {@link PluginLifecycleManager#listSubprocessSnapshots()}）。
 *
 * @param hostLeasedPort      宿主 JVM 内 {@link com.sxpcwlkj.plugin.host.internal.PortLeaseTracker} 登记端口；0 表示无。
 * @param tcpPortAppearsBound {@code true} 表示回环探测无法绑定该端口（通常已有进程监听）；{@code null} 表示未探测（无端口）。
 */
public record PluginSubprocessSnapshot(
        String pluginKey,
        String pluginId,
        String version,
        int effectivePort,
        Long pid,
        boolean alive,
        String lastError,
        int hostLeasedPort,
        Boolean tcpPortAppearsBound) {

    public PluginSubprocessSnapshot(
            String pluginKey,
            String pluginId,
            String version,
            int effectivePort,
            Long pid,
            boolean alive,
            String lastError) {
        this(pluginKey, pluginId, version, effectivePort, pid, alive, lastError, 0, null);
    }
}
