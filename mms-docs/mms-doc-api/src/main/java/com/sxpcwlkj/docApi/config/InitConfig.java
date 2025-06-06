package com.sxpcwlkj.docApi.config;

import com.typesafe.config.Config;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 西决
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class InitConfig {
    private final ApplicationContext applicationContext;
    /**
     * 初始化配置参数到缓存中
     */
    @PostConstruct
    private void init() {
        Map<Long, Config> configMap = new HashMap<>();
        applicationContext.publishEvent(new ContextRefreshedEvent(applicationContext));
        log.info("项目初始化....");
    }
}
