package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * 独立进程模式下的 TCP 端口探测（先试绑定回环地址）。
 */
public final class PortManager {

    private PortManager() {
    }

    /**
     * 在 [{@code min},{@code max}] 内找第一个可绑定回环的端口。
     */
    public static int allocateFirstFreePort(int min, int max) throws PluginException {
        if (min < 1 || max > 65535 || min > max) {
            throw new PluginException("subprocess 端口范围非法: " + min + "–" + max);
        }
        for (int p = min; p <= max; p++) {
            if (isTcpPortAvailable(p)) {
                return p;
            }
        }
        throw new PluginException("在端口范围 " + min + "–" + max + " 内未找到可用 TCP 端口");
    }

    public static boolean isTcpPortAvailable(int port) {
        if (port < 1 || port > 65535) {
            return false;
        }
        try (ServerSocket socket = new ServerSocket(port, 1, InetAddress.getLoopbackAddress())) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
