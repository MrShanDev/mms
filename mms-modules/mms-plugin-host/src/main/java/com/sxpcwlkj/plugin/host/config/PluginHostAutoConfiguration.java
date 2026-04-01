package com.sxpcwlkj.plugin.host.config;

import com.sxpcwlkj.plugin.HostDataService;
import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.host.PluginHostDbBridge;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import com.sxpcwlkj.plugin.host.internal.DefaultHostServices;
import com.sxpcwlkj.plugin.host.internal.PluginSpringBeanAttachment;
import com.sxpcwlkj.plugin.host.web.DefaultPluginCircuitTripHandler;
import com.sxpcwlkj.plugin.host.web.DefaultPluginMvcExecutionGuard;
import com.sxpcwlkj.plugin.host.web.PluginCircuitTripHandler;
import com.sxpcwlkj.plugin.host.web.PluginMvcDispatcher;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutionGuard;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutorRegistry;
import com.sxpcwlkj.plugin.host.web.PluginMvcRegistrar;
import com.sxpcwlkj.plugin.host.web.PluginMvcRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 自动装配插件宿主 Bean（由 Spring Boot 导入列表加载）。
 * {@code PluginHostController}、{@code PluginHostRunner} 由主应用对 {@code com.sxpcwlkj} 的组件扫描注册。
 */
@AutoConfiguration
@EnableConfigurationProperties(PluginHostProperties.class)
public class PluginHostAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(HostServices.class)
    public HostServices hostServices(
            ObjectProvider<StringRedisTemplate> stringRedisTemplate,
            ObjectProvider<HostDataService> hostDataService,
            ObjectProvider<DataSource> dataSource,
            ObjectProvider<PlatformTransactionManager> transactionManager,
            PluginHostProperties pluginHostProperties) {
        return new DefaultHostServices(
                stringRedisTemplate, hostDataService, dataSource, transactionManager, pluginHostProperties);
    }

    @Bean
    public PluginMvcRegistry pluginMvcRegistry() {
        return new PluginMvcRegistry();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public PluginMvcRegistrar pluginMvcRegistrar(PluginMvcRegistry pluginMvcRegistry) {
        return new PluginMvcRegistrar(pluginMvcRegistry);
    }

    @Bean
    public PluginMvcExecutorRegistry pluginMvcExecutorRegistry(PluginHostProperties pluginHostProperties) {
        return new PluginMvcExecutorRegistry(pluginHostProperties);
    }

    @Bean
    public PluginSpringBeanAttachment pluginSpringBeanAttachment(ConfigurableApplicationContext applicationContext) {
        return new PluginSpringBeanAttachment(applicationContext);
    }

    @Bean
    public PluginLifecycleManager pluginLifecycleManager(
            PluginHostProperties properties,
            ObjectProvider<PluginHostDbBridge> dbBridge,
            ObjectProvider<HostServices> hostServices,
            ObjectProvider<PluginMvcRegistrar> pluginMvcRegistrar,
            ObjectProvider<PluginSpringBeanAttachment> pluginSpringBeanAttachment,
            ObjectProvider<PluginMvcExecutorRegistry> pluginMvcExecutorRegistry) {
        return new PluginLifecycleManager(
                properties,
                dbBridge,
                hostServices,
                pluginMvcRegistrar,
                pluginSpringBeanAttachment,
                pluginMvcExecutorRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(PluginCircuitTripHandler.class)
    public PluginCircuitTripHandler pluginCircuitTripHandler(
            PluginHostProperties pluginHostProperties,
            ObjectProvider<PluginLifecycleManager> pluginLifecycleManager) {
        return new DefaultPluginCircuitTripHandler(pluginHostProperties, pluginLifecycleManager);
    }

    @Bean
    @ConditionalOnMissingBean(PluginMvcExecutionGuard.class)
    public PluginMvcExecutionGuard pluginMvcExecutionGuard(
            PluginHostProperties pluginHostProperties,
            ObjectProvider<PluginCircuitTripHandler> tripHandlerProvider) {
        return new DefaultPluginMvcExecutionGuard(pluginHostProperties, tripHandlerProvider);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public PluginMvcDispatcher pluginMvcDispatcher(
            PluginMvcRegistry pluginMvcRegistry,
            PluginMvcExecutionGuard pluginMvcExecutionGuard,
            ObjectProvider<PluginMvcExecutorRegistry> pluginMvcExecutorRegistry) {
        return new PluginMvcDispatcher(pluginMvcRegistry, pluginMvcExecutionGuard, pluginMvcExecutorRegistry);
    }
}
