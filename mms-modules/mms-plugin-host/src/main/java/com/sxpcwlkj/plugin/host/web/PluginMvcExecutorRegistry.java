package com.sxpcwlkj.plugin.host.web;

import cn.dev33.satoken.context.SaTokenContextForThreadLocalStaff;
import cn.dev33.satoken.context.model.SaTokenContextModelBox;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
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
 * <p>仅在 {@code mms.plugin.plugin-mvc-per-plugin-pool-size &gt; 0} 时使用；为 0 时在请求线程同步执行。
 * 池化执行时虽已传递 Sa-Token / RequestContextHolder / MDC，但 Servlet 请求对象与多数安全组件仍以请求线程为准，
 * 仍可能出现鉴权异常，故默认池大小为 0。</p>
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
        Future<T> f = ex.submit(wrapForPluginMvcWorker(task));
        return f.get(timeoutSec, TimeUnit.SECONDS);
    }

    /**
     * 插件 MVC 在线程池线程执行时，Tomcat 请求线程上的 Sa-Token / Spring Request / MDC 不会自动传递，
     * 需在提交前捕获并在 worker 中还原，否则 {@link cn.dev33.satoken.stp.StpUtil} 等会报上下文未初始化。
     */
    private static <T> Callable<T> wrapForPluginMvcWorker(Callable<T> task) {
        SaTokenContextModelBox saBox = SaTokenContextForThreadLocalStaff.getModelBoxOrNull();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Map<String, String> mdcCopy = MDC.getCopyOfContextMap();
        return () -> {
            boolean saBound = false;
            Map<String, String> prevMdc = MDC.getCopyOfContextMap();
            RequestAttributes prevRa = RequestContextHolder.getRequestAttributes();
            try {
                if (saBox != null) {
                    SaTokenContextForThreadLocalStaff.setModelBox(
                            saBox.getRequest(), saBox.getResponse(), saBox.getStorage());
                    saBound = true;
                }
                if (requestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(requestAttributes, true);
                }
                if (mdcCopy != null) {
                    MDC.setContextMap(mdcCopy);
                } else {
                    MDC.clear();
                }
                return task.call();
            } finally {
                if (saBound) {
                    SaTokenContextForThreadLocalStaff.clearModelBox();
                }
                if (requestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(prevRa, false);
                }
                if (prevMdc != null) {
                    MDC.setContextMap(prevMdc);
                } else {
                    MDC.clear();
                }
            }
        };
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
