package com.sxpcwlkj.mq.config;

import com.sxpcwlkj.mq.handler.impl.DefaultMqHandler;
import com.sxpcwlkj.mq.handler.impl.DistributionMqHandler;
import com.sxpcwlkj.mq.service.MqService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MQRegister {

    private final MqService mqService;
    private final DistributionMqHandler distributionMessageHandler;
    private final DefaultMqHandler defaultMessageHandler;

    @PostConstruct
    public void registerHandlers() {
        // 1. 注册默认处理器
        mqService.registerHandler(defaultMessageHandler);

        // 2. 注册分销处理器
        mqService.registerHandler(distributionMessageHandler);

        log.info("✅ 已注册处理器：{}", mqService.getRegisteredHandlers());

        // 3. 设置当前处理器为分销处理器
        if (mqService.setCurrentHandler("distribution")) {
            log.info("✅ 当前处理器设置为: distribution");
        } else {
            log.error("❌ 无法设置分销处理器，使用默认处理器");
        }
    }
}
