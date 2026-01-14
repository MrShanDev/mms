package com.sxpcwlkj.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单超时队列配置
 * 配置订单超时延时取消功能所需的交换机、队列和绑定关系
 */
@Configuration
@Slf4j
public class OrderTimeoutQueueConfig {

    // ==================== 配置属性 ====================
    @Value("${spring.rabbitmq.order-timeout.exchange:order.timeout.exchange}")
    private String timeoutExchange;

    @Value("${spring.rabbitmq.order-timeout.queue:order.timeout.queue}")
    private String timeoutQueue;

    @Value("${spring.rabbitmq.order-timeout.routing-key:order.timeout.key}")
    private String timeoutRoutingKey;

    @Value("${spring.rabbitmq.order-timeout.delay-time:1800000}")
    private long defaultDelayTime; // 默认30分钟

    @Value("${spring.rabbitmq.order-timeout.dead-letter-exchange:order.timeout.dlx.exchange}")
    private String deadLetterExchange;

    @Value("${spring.rabbitmq.order-timeout.dead-letter-queue:order.timeout.dlx.queue}")
    private String deadLetterQueue;

    @Value("${spring.rabbitmq.order-timeout.dead-letter-routing-key:order.timeout.dlx.key}")
    private String deadLetterRoutingKey;

    // ==================== 交换机配置 ====================

    /**
     * 订单超时交换机
     */
    @Bean
    public DirectExchange orderTimeoutExchange() {
        return new DirectExchange(timeoutExchange, true, false);
    }

    /**
     * 订单超时死信交换机
     */
    @Bean
    public DirectExchange orderTimeoutDeadLetterExchange() {
        return new DirectExchange(deadLetterExchange, true, false);
    }

    // ==================== 队列配置 ====================

    /**
     * 订单超时队列（延时队列）
     * 消息在队列中等待指定时间后，自动转到死信交换机
     */
    @Bean
    public Queue orderTimeoutQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机：消息过期后转发到自己（形成延时效果）
        args.put("x-dead-letter-exchange", timeoutExchange);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", timeoutRoutingKey);
        // 注意：这里不设置队列级别的TTL，而是在发送消息时设置消息级别的TTL
        // 这样可以支持不同订单设置不同的超时时间
        
        return new Queue(timeoutQueue, true, false, false, args);
    }

    /**
     * 订单超时死信队列
     * 存储处理失败的超时消息
     */
    @Bean
    public Queue orderTimeoutDeadLetterQueue() {
        return new Queue(deadLetterQueue, true);
    }

    // ==================== 绑定关系 ====================

    /**
     * 绑定订单超时队列到超时交换机
     */
    @Bean
    public Binding orderTimeoutBinding() {
        return BindingBuilder.bind(orderTimeoutQueue())
            .to(orderTimeoutExchange())
            .with(timeoutRoutingKey);
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding orderTimeoutDeadLetterBinding() {
        return BindingBuilder.bind(orderTimeoutDeadLetterQueue())
            .to(orderTimeoutDeadLetterExchange())
            .with(deadLetterRoutingKey);
    }

    // ==================== 初始化日志 ====================

    @Bean
    public String initOrderTimeoutQueue() {
        log.info("✅ 订单超时队列配置初始化完成");
        log.info("📊 订单超时队列信息:");
        log.info("   - 超时队列: {}", timeoutQueue);
        log.info("   - 超时交换机: {}", timeoutExchange);
        log.info("   - 路由键: {}", timeoutRoutingKey);
        log.info("   - 默认延时: {}ms ({}分钟)", defaultDelayTime, defaultDelayTime / 60000);
        log.info("   - 死信队列: {}", deadLetterQueue);
        log.info("   - 死信交换机: {}", deadLetterExchange);
        return "订单超时队列初始化成功";
    }
}
