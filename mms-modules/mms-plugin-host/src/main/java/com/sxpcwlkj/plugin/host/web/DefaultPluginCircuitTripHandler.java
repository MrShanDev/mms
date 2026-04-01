package com.sxpcwlkj.plugin.host.web;

import com.sxpcwlkj.plugin.host.PluginHostProperties;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 熔断触发时按配置卸载插件（带告警日志）；由 {@link com.sxpcwlkj.plugin.host.config.PluginHostAutoConfiguration} 注册。
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultPluginCircuitTripHandler implements PluginCircuitTripHandler {

    private final PluginHostProperties properties;
    private final ObjectProvider<PluginLifecycleManager> lifecycleProvider;

    @Override
    public void onTrip(String pluginId, int consecutiveFailures, Throwable lastError) {
        if (!properties.isPluginMvcCircuitTripUnloadsPlugin()) {
            return;
        }
        log.error(
                "插件 {} 连续 HOST_MVC 业务失败 {} 次，触发自动卸载（pluginMvcCircuitTripUnloadsPlugin=true）",
                pluginId,
                consecutiveFailures,
                lastError);
        PluginLifecycleManager lm = lifecycleProvider.getIfAvailable();
        if (lm != null) {
            lm.unloadAllVersionsOfPlugin(pluginId);
        }
    }
}
