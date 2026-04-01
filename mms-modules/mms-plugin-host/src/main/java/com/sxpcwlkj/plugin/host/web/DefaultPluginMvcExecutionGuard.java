package com.sxpcwlkj.plugin.host.web;

import com.sxpcwlkj.plugin.host.PluginHostProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 按 pluginId：连续失败熔断 / 自动卸载（可选）、HOST_MVC 与运维 invoke 分离限流窗口。
 */
@RequiredArgsConstructor
public final class DefaultPluginMvcExecutionGuard implements PluginMvcExecutionGuard {

    private final PluginHostProperties properties;
    private final ObjectProvider<PluginCircuitTripHandler> tripHandlerProvider;

    private final ConcurrentHashMap<String, AtomicInteger> consecutiveFailures = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> breakerOpenUntilMillis = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, long[]> rateWindowMvc = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, long[]> rateWindowOps = new ConcurrentHashMap<>();

    @Override
    public boolean allowBeforeInvoke(String pluginId) {
        return allowBefore(pluginId, properties.getPluginMvcRateLimitPerMinute(), rateWindowMvc);
    }

    @Override
    public boolean allowBeforeOpsInvoke(String pluginId) {
        return allowBefore(pluginId, properties.getPluginInvokeRateLimitPerMinute(), rateWindowOps);
    }

    private boolean allowBefore(String pluginId, int rpm, ConcurrentHashMap<String, long[]> window) {
        if (pluginId == null || pluginId.isBlank()) {
            return true;
        }
        long now = System.currentTimeMillis();
        Long openUntil = breakerOpenUntilMillis.get(pluginId);
        if (openUntil != null && now < openUntil) {
            return false;
        }
        if (rpm > 0) {
            long minute = now / 60_000L;
            long[] slot = window.computeIfAbsent(pluginId, k -> new long[] {minute, 0});
            synchronized (slot) {
                if (slot[0] != minute) {
                    slot[0] = minute;
                    slot[1] = 0;
                }
                if (slot[1] >= rpm) {
                    return false;
                }
                slot[1]++;
            }
        }
        return true;
    }

    @Override
    public void afterSuccessfulInvoke(String pluginId) {
        afterSuccess(pluginId);
    }

    @Override
    public void afterSuccessfulOpsInvoke(String pluginId) {
        afterSuccess(pluginId);
    }

    private void afterSuccess(String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            return;
        }
        consecutiveFailures.remove(pluginId);
    }

    @Override
    public void afterFailedInvoke(String pluginId, Throwable error) {
        afterFailed(pluginId, error);
    }

    @Override
    public void afterFailedOpsInvoke(String pluginId, Throwable error) {
        afterFailed(pluginId, error);
    }

    private void afterFailed(String pluginId, Throwable error) {
        if (pluginId == null || pluginId.isBlank()) {
            return;
        }
        int threshold = properties.getPluginMvcCircuitFailureThreshold();
        if (threshold <= 0) {
            return;
        }
        int n = consecutiveFailures
                .computeIfAbsent(pluginId, k -> new AtomicInteger())
                .incrementAndGet();
        if (n >= threshold) {
            consecutiveFailures.remove(pluginId);
            if (properties.isPluginMvcCircuitTripUnloadsPlugin()) {
                tripHandlerProvider.ifAvailable(h -> h.onTrip(pluginId, n, error));
            } else {
                long until =
                        System.currentTimeMillis() + properties.getPluginMvcCircuitOpenSeconds() * 1000L;
                breakerOpenUntilMillis.put(pluginId, until);
            }
        }
    }
}
