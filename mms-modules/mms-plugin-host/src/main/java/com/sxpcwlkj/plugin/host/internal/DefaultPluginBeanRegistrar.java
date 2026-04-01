package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginBeanRegistrar;
import com.sxpcwlkj.plugin.PluginException;

/**
 * 绑定到单次插件加载会话的 {@link PluginBeanRegistrar}；除非 {@link PluginSpringBeanAttachment} 为 null，否则注册会交给宿主容器。
 */
public final class DefaultPluginBeanRegistrar implements PluginBeanRegistrar {

    private final PluginSpringBeanAttachment attachment;
    private final String loadSessionId;

    public DefaultPluginBeanRegistrar(PluginSpringBeanAttachment attachment, String loadSessionId) {
        this.attachment = attachment;
        this.loadSessionId = loadSessionId;
    }

    @Override
    public void registerSingleton(String logicalName, Object bean) {
        if (attachment == null) {
            throw new PluginException(
                    "宿主未装配 PluginSpringBeanAttachment（非 ConfigurableApplicationContext？），无法 registerSingleton");
        }
        attachment.registerSingleton(loadSessionId, logicalName, bean);
    }
}
