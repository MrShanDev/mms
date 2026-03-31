package com.sxpcwlkj.plugin.host;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 应用就绪后加载插件。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PluginHostRunner implements ApplicationListener<ApplicationReadyEvent> {

    private final PluginLifecycleManager pluginLifecycleManager;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            pluginLifecycleManager.loadAll();
        } catch (Exception e) {
            log.error("插件启动加载异常", e);
        }
    }
}
