package com.sxpcwlkj.plugin.host;

import java.util.List;

/**
 * {@link PluginHostController} 运维反射调用请求体（args 由 Jackson 反序列化后按目标方法形参 convertValue）。
 */
public record PluginInvokeRequest(String pluginId, String version, String methodName, List<Object> args) {
}
