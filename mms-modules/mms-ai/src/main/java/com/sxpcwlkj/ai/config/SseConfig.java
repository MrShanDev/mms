package com.sxpcwlkj.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * SSE配置
 * @author mmsAdmin
 */
@Configuration
public class SseConfig {
    @Bean(name = "sseThreadPool")
    public ExecutorService sseThreadPool() {
        return new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
    }
}
