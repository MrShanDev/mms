package com.sxpcwlkj.plugin.host.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.sift.SiftingAppender;
import com.sxpcwlkj.plugin.host.internal.PluginMdc;

/**
 * 仅当 MDC 存在 {@link PluginMdc#PLUGIN_KEY} 时才委托给 {@link SiftingAppender}，避免全量日志进入 sift 默认桶。
 */
public class PluginOnlySiftingAppender extends SiftingAppender {

    @Override
    public void append(ILoggingEvent event) {
        if (event == null) {
            return;
        }
        String key = event.getMDCPropertyMap().get(PluginMdc.PLUGIN_KEY);
        if (key == null || key.isBlank()) {
            return;
        }
        super.append(event);
    }
}
