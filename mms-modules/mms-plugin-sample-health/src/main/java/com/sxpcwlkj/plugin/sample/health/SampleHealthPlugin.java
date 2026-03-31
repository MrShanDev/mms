package com.sxpcwlkj.plugin.sample.health;

import com.sxpcwlkj.plugin.MmsPlugin;
import com.sxpcwlkj.plugin.PluginHealthContributor;
import com.sxpcwlkj.plugin.PluginRuntimeContext;

/**
 * 最小示例：实现 {@link MmsPlugin} 与 {@link PluginHealthContributor}，构建 JAR 后放入宿主 {@code lib/}。
 */
public class SampleHealthPlugin implements MmsPlugin, PluginHealthContributor {

    @Override
    public void onLoad(PluginRuntimeContext context) {
        System.out.println("[mms-plugin-sample-health] onLoad revision=" + context.hostMmsRevision()
                + " root=" + context.pluginInstallRoot());
    }

    @Override
    public String health() {
        return "{\"status\":\"UP\",\"sample\":\"mms-plugin-sample-health\"}";
    }
}
