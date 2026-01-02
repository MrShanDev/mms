package com.sxpcwlkj.log.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 操作日志异步配置
 *
 * @author mmsAdmin
 */
@Slf4j
@Configuration
@EnableAsync
public class SysLogAsyncConfig {

    /**
     * 操作日志专用线程池
     * 使用虚拟线程优化性能
     */
    @Bean("operLogExecutor")
    public Executor operLogExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数
        executor.setCorePoolSize(2);
        // 最大线程数
        executor.setMaxPoolSize(5);
        // 队列容量
        executor.setQueueCapacity(1000);
        // 线程名称前缀
        executor.setThreadNamePrefix("oper-log-");
        // 线程空闲时间(秒)
        executor.setKeepAliveSeconds(60);
        // 拒绝策略:由调用线程处理
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待任务完成后关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间(秒)
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();

        log.info("操作日志异步线程池初始化完成");
        return executor;
    }
}
