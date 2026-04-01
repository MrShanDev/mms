package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 宿主 JVM 内端口租约：已分配给某 pluginKey 的端口在显式端口与自动分配时互斥，卸载时释放。
 */
public final class PortLeaseTracker {

    private final Map<Integer, String> portToKey = new ConcurrentHashMap<>();
    private final Map<String, Integer> keyToPort = new ConcurrentHashMap<>();

    public int leaseInRange(int min, int max, String pluginKey) throws PluginException {
        releaseForPluginKey(pluginKey);
        for (int p = min; p <= max; p++) {
            if (portToKey.containsKey(p)) {
                continue;
            }
            if (!PortManager.isTcpPortAvailable(p)) {
                continue;
            }
            if (portToKey.putIfAbsent(p, pluginKey) == null) {
                keyToPort.put(pluginKey, p);
                return p;
            }
        }
        throw new PluginException("端口范围 " + min + "-" + max + " 内无可用或均已租约占用端口: " + pluginKey);
    }

    public int leaseExplicit(int port, String pluginKey) throws PluginException {
        releaseForPluginKey(pluginKey);
        if (!PortManager.isTcpPortAvailable(port)) {
            throw new PluginException("端口不可用: " + port);
        }
        String prev = portToKey.putIfAbsent(port, pluginKey);
        if (prev != null && !prev.equals(pluginKey)) {
            throw new PluginException("端口 " + port + " 已被插件 " + prev + " 占用");
        }
        keyToPort.put(pluginKey, port);
        return port;
    }

    public void releaseForPluginKey(String pluginKey) {
        if (pluginKey == null) {
            return;
        }
        Integer p = keyToPort.remove(pluginKey);
        if (p != null) {
            portToKey.remove(p, pluginKey);
        }
    }

    /** 当前插件 key 在宿主内的租约端口；无则 null。 */
    public Integer getLeasedPortForKey(String pluginKey) {
        if (pluginKey == null) {
            return null;
        }
        return keyToPort.get(pluginKey);
    }
}
