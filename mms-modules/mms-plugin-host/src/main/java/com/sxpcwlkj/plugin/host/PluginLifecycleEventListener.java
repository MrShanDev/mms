package com.sxpcwlkj.plugin.host;

/**
 * 插件生命周期观察者（可选 Spring Bean）；实现类应快速返回，重活宜异步自行处理。
 */
@FunctionalInterface
public interface PluginLifecycleEventListener {

    void onPluginLifecycleEvent(PluginLifecycleEvent event);
}
