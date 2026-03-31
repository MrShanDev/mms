package com.sxpcwlkj.plugin.host.config;

import com.sxpcwlkj.plugin.host.PluginHostDbBridge;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 自动装配插件宿主 Bean（由 Spring Boot 导入列表加载）。
 * {@code PluginHostController}、{@code PluginHostRunner} 由主应用对 {@code com.sxpcwlkj} 的组件扫描注册。
 */
@AutoConfiguration
@EnableConfigurationProperties(PluginHostProperties.class)
public class PluginHostAutoConfiguration {

    @Bean
    public PluginLifecycleManager pluginLifecycleManager(
            PluginHostProperties properties,
            ObjectProvider<PluginHostDbBridge> dbBridge) {
        return new PluginLifecycleManager(properties, dbBridge);
    }
}
