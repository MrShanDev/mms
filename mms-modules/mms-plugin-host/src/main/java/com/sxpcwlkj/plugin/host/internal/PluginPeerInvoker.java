package com.sxpcwlkj.plugin.host.internal;

import java.util.Optional;

@FunctionalInterface
public interface PluginPeerInvoker {

    Optional<Object> invokePeer(String targetPluginId, String methodName, Object[] args);
}
