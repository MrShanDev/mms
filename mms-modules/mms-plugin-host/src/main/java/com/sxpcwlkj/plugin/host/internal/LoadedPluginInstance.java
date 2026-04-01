package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.MmsPlugin;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginHealthContributor;
import lombok.Getter;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * 卸载顺序：{@link com.sxpcwlkj.plugin.MmsPlugin#onUnload}（全部 SPI 实现，吞单条异常）→ 按注册逆序执行
 * {@link com.sxpcwlkj.plugin.PluginRuntimeContext#addUnloadHook 卸载钩子} →
 * 注销由 {@link com.sxpcwlkj.plugin.PluginBeanRegistrar} 注册到宿主容器的单例→ {@link URLClassLoader#close()}。
 * 全量重载前，{@link com.sxpcwlkj.plugin.host.PluginLifecycleManager} 会先 {@code PluginMvcRegistrar#unregister}
 * 并停子进程，再调用本 {@link #close()}。
 */
@Getter
public final class LoadedPluginInstance implements AutoCloseable {

    private final PluginDescriptor descriptor;
    private final URLClassLoader classLoader;
    private final List<MmsPlugin> entryPoints;
    private final List<PluginHealthContributor> healthContributors;
    private final List<Runnable> unloadHooks;
    private final Runnable releasePluginSpringBeans;

    public LoadedPluginInstance(
            PluginDescriptor descriptor,
            URLClassLoader classLoader,
            List<MmsPlugin> entryPoints,
            List<PluginHealthContributor> healthContributors,
            List<Runnable> unloadHooks,
            Runnable releasePluginSpringBeans) {
        this.descriptor = descriptor;
        this.classLoader = classLoader;
        this.entryPoints = List.copyOf(new ArrayList<>(entryPoints));
        this.healthContributors = List.copyOf(new ArrayList<>(healthContributors));
        this.unloadHooks = unloadHooks == null ? List.of() : List.copyOf(unloadHooks);
        this.releasePluginSpringBeans = releasePluginSpringBeans != null ? releasePluginSpringBeans : () -> {};
    }

    @Override
    public void close() throws Exception {
        Runnable popMdc = PluginMdc.pushPluginContext(descriptor.getId(), descriptor.getVersion());
        try {
            for (MmsPlugin p : entryPoints) {
                try {
                    p.onUnload();
                } catch (Exception ex) {
                    // 卸载失败不阻断关闭 ClassLoader
                }
            }
        } finally {
            popMdc.run();
        }
        for (int i = unloadHooks.size() - 1; i >= 0; i--) {
            try {
                unloadHooks.get(i).run();
            } catch (Exception ex) {
                // 钩子失败不阻断 ClassLoader 关闭
            }
        }
        releasePluginSpringBeans.run();
        classLoader.close();
    }
}
