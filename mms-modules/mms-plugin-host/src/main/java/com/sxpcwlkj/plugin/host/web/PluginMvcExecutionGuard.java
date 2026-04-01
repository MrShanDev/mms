package com.sxpcwlkj.plugin.host.web;

/**
 * 插件 MVC / 运维反射调用前后韧性（熔断 / 限流）；无实现或关闭配置时为 no-op。
 */
public interface PluginMvcExecutionGuard {

    default boolean allowBeforeInvoke(String pluginId) {
        return true;
    }

    default void afterSuccessfulInvoke(String pluginId) {}

    default void afterFailedInvoke(String pluginId, Throwable error) {}

    /** {@code /system/pluginHost/invoke} 限流（独立于 HOST_MVC 计数窗口）。 */
    default boolean allowBeforeOpsInvoke(String pluginId) {
        return true;
    }

    default void afterSuccessfulOpsInvoke(String pluginId) {}

    default void afterFailedOpsInvoke(String pluginId, Throwable error) {}
}
