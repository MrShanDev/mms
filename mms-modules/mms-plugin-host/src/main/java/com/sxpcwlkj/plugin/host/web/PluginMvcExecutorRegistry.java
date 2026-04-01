package com.sxpcwlkj.plugin.host.web;

import com.sxpcwlkj.plugin.host.PluginHostProperties;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 按 pluginId 隔离的 MVC 调用线程池，防止单插件阻塞或耗尽宿主 worker 线程。
 */
public final class PluginMvcExecutorRegistry {

    private final PluginHostProperties properties;
    private final ConcurrentHashMap<String, ExecutorService> pools = new ConcurrentHashMap<>();
    private final AtomicInteger threadSeq = new AtomicInteger();

    public PluginMvcExecutorRegistry(PluginHostProperties properties) {
        this.properties = properties;
    }

    public <T> T execute(String pluginId, Callable<T> task) throws Exception {
        int size = properties.getPluginMvcPerPluginPoolSize();
        if (size <= 0) {
            return task.call();
        }
        int timeoutSec = Math.max(1, properties.getPluginMvcInvokeTimeoutSeconds());
        ExecutorService ex = pools.computeIfAbsent(pluginId, this::newPool);
        Future<T> f = ex.submit(task);
        return f.get(timeoutSec, TimeUnit.SECONDS);
    }

    public void shutdownForPlugin(String pluginId) {
        if (pluginId == null) {
            return;
        }
        ExecutorService ex = pools.remove(pluginId.trim());
        if (ex != null) {
            ex.shutdownNow();
        }
    }

    public void shutdownAll() {
        for (ExecutorService ex : pools.values()) {
            ex.shutdownNow();
        }
        pools.clear();
    }

    private ExecutorService newPool(String pluginId) {
        int size = Math.max(1, properties.getPluginMvcPerPluginPoolSize());
        int cap = Math.max(16, properties.getPluginMvcPerPluginQueueCapacity());
        BlockingQueue<Runnable> q = new LinkedBlockingQueue<>(cap);
        String prefix = "mms-plugin-mvc-" + safeThreadName(pluginId) + "-";
        return new ThreadPoolExecutor(
                size,
                size,
                60L,
                TimeUnit.SECONDS,
                q,
                r -> {
                    Thread t = new Thread(r, prefix + threadSeq.incrementAndGet());
                    t.setDaemon(true);
                    return t;
                },
                new ThreadPoolExecutor.AbortPolicy());
    }

    private static String safeThreadName(String pluginId) {
        String s = pluginId.replace('.', '_').replace('@', '_');
        if (s.length() > 36) {
            s = s.substring(0, 36);
        }
        return s;
    }
}
