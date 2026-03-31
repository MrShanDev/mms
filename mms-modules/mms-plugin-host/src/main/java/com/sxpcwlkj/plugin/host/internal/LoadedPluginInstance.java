package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.MmsPlugin;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginHealthContributor;
import lombok.Getter;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class LoadedPluginInstance implements AutoCloseable {

    private final PluginDescriptor descriptor;
    private final URLClassLoader classLoader;
    private final List<MmsPlugin> entryPoints;
    private final List<PluginHealthContributor> healthContributors;

    public LoadedPluginInstance(
            PluginDescriptor descriptor,
            URLClassLoader classLoader,
            List<MmsPlugin> entryPoints,
            List<PluginHealthContributor> healthContributors) {
        this.descriptor = descriptor;
        this.classLoader = classLoader;
        this.entryPoints = List.copyOf(new ArrayList<>(entryPoints));
        this.healthContributors = List.copyOf(new ArrayList<>(healthContributors));
    }

    @Override
    public void close() throws Exception {
        for (MmsPlugin p : entryPoints) {
            try {
                p.onUnload();
            } catch (Exception ex) {
                // 卸载失败不阻断关闭 ClassLoader
            }
        }
        classLoader.close();
    }
}
