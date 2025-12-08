package com.sxpcwlkj.mq.hander.handler;

import com.sxpcwlkj.mq.entity.DistributionMessage;
import com.sxpcwlkj.mq.hander.MqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认消息处理器
 */
@Slf4j
@Component
public class DefaultMqHandler implements MqHandler {

    // 记录每个订单的处理次数，防止无限重试
    private final ConcurrentHashMap<String, AtomicInteger> processCounts = new ConcurrentHashMap<>();

    @Override
    public boolean handleMessage(DistributionMessage message) {
        String orderId = message.getOrderId();
        String messageId = message.getMessageId();
        Integer retryCount = message.getRetryCount() != null ? message.getRetryCount() : 0;

        // 记录处理次数
        AtomicInteger count = processCounts.computeIfAbsent(orderId, k -> new AtomicInteger(0));
        int processCount = count.incrementAndGet();

        log.warn("⚠️ 使用默认消息处理器 - 订单ID: {}, 消息ID: {}, 重试次数: {}, 处理次数: {}, 业务类型: {}",
            orderId, messageId, retryCount, processCount, message.getBusinessType());

        // 记录更详细的信息用于问题排查
        if (retryCount > 0) {
            log.error("🚨 消息多次重试失败 - 订单ID: {}, 消息ID: {}, 重试次数: {}, 处理次数: {}, 建议检查业务处理器配置",
                orderId, messageId, retryCount, processCount);
        }

        // 如果处理次数过多，直接返回成功避免无限重试
        if (processCount > 5) {
            log.error("🚨 消息处理次数过多，强制成功 - 订单ID: {}, 消息ID: {}, 处理次数: {}",
                orderId, messageId, processCount);
            processCounts.remove(orderId); // 清理计数
            return true;
        }

        // 默认处理器总是返回失败
        // 这样消息会根据配置的重试机制进入重试队列或死信队列
        return false;
    }

    @Override
    public boolean isMessageProcessed(String orderId) {
        log.debug("检查订单处理状态: {}", orderId);
        // 默认返回未处理，让消息继续处理流程
        return false;
    }

    @Override
    public String getHandlerType() {
        return "default";
    }
}
