package com.sxpcwlkj.demo.mq.config;

import com.sxpcwlkj.demo.mq.handler.DemoOrderTimeoutHandler;
import com.sxpcwlkj.mq.service.OrderTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 订单超时配置类
 * 注册订单超时处理器到 mms-mq 模块
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrderTimeoutConfig {

    private final OrderTimeoutService orderTimeoutService;
    private final DemoOrderTimeoutHandler demoOrderTimeoutHandler;

    /**
     * 应用启动完成后注册处理器
     */
    @EventListener(ApplicationReadyEvent.class)
    public void registerOrderTimeoutHandler() {
        // 注册Demo处理器
        orderTimeoutService.registerHandler(demoOrderTimeoutHandler);
        
        // 设置为当前处理器
        boolean success = orderTimeoutService.setCurrentHandler("demo");
        
        if (success) {
            log.info("✅ Demo订单超时处理器注册并激活成功");
        } else {
            log.error("❌ Demo订单超时处理器激活失败");
        }
    }
}
