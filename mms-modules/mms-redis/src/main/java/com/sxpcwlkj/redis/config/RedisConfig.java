package com.sxpcwlkj.redis.config;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.redis.handler.KeyPrefixHandler;
import com.sxpcwlkj.redis.manager.SpringCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author mmsAdmin
 * @Date 2022/12/4 20:40
 */
@Slf4j
@Configuration
@EnableCaching
@EnableTransactionManagement
@EnableConfigurationProperties(RedissonProperties.class)
public class RedisConfig extends CachingConfigurerSupport {

    private final RedissonProperties redissonProperties;

    private final ObjectMapper objectMapper;

    public RedisConfig(RedissonProperties redissonProperties, ObjectMapper objectMapper) {
        this.redissonProperties = redissonProperties;
        this.objectMapper = objectMapper;
    }

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return config -> {
            config.setThreads(redissonProperties.getThreads())
                    .setNettyThreads(redissonProperties.getNettyThreads())
                    .setCodec(new JsonJacksonCodec(objectMapper));
            RedissonProperties.SingleServerConfig singleServerConfig = redissonProperties.getSingleServerConfig();
            if (ObjectUtil.isNotNull(singleServerConfig)) {
                // 使用单机模式
                config.useSingleServer()
                        //设置redis key前缀
                        .setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()))
                        .setTimeout(singleServerConfig.getTimeout())
                        .setClientName(singleServerConfig.getClientName())
                        .setIdleConnectionTimeout(singleServerConfig.getIdleConnectionTimeout())
                        .setSubscriptionConnectionPoolSize(singleServerConfig.getSubscriptionConnectionPoolSize())
                        .setConnectionMinimumIdleSize(singleServerConfig.getConnectionMinimumIdleSize())
                        .setConnectionPoolSize(singleServerConfig.getConnectionPoolSize());
            }
            // 集群配置方式 参考下方注释
            RedissonProperties.ClusterServersConfig clusterServersConfig = redissonProperties.getClusterServersConfig();
            if (ObjectUtil.isNotNull(clusterServersConfig)) {
                config.useClusterServers()
                        //设置redis key前缀
                        .setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()))
                        .setTimeout(clusterServersConfig.getTimeout())
                        .setClientName(clusterServersConfig.getClientName())
                        .setIdleConnectionTimeout(clusterServersConfig.getIdleConnectionTimeout())
                        .setSubscriptionConnectionPoolSize(clusterServersConfig.getSubscriptionConnectionPoolSize())
                        .setMasterConnectionMinimumIdleSize(clusterServersConfig.getMasterConnectionMinimumIdleSize())
                        .setMasterConnectionPoolSize(clusterServersConfig.getMasterConnectionPoolSize())
                        .setSlaveConnectionMinimumIdleSize(clusterServersConfig.getSlaveConnectionMinimumIdleSize())
                        .setSlaveConnectionPoolSize(clusterServersConfig.getSlaveConnectionPoolSize())
                        .setReadMode(clusterServersConfig.getReadMode())
                        .setSubscriptionMode(clusterServersConfig.getSubscriptionMode());
            }
            log.info("初始化 redis 配置");
        };
    }

    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager() {
        return new SpringCacheManager();
    }

}
