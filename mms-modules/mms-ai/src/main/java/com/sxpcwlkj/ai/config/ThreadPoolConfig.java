package com.sxpcwlkj.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池优化配置
 * @author mmsAdmin
 */
@Configuration
public class ThreadPoolConfig {

    //调整线程池拒绝策略，防止任务堆积
    @Bean("streamThreadPool")
    public ExecutorService threadPool() {
        return new ThreadPoolExecutor(
            10, 50,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            // 重要：由调用线程处理溢出任务
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
