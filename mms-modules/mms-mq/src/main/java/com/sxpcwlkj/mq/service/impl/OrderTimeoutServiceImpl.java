package com.sxpcwlkj.mq.service.impl;

import com.rabbitmq.client.Channel;
import com.sxpcwlkj.mq.entity.OrderTimeoutMessage;
import com.sxpcwlkj.mq.handler.OrderTimeoutHandler;
import com.sxpcwlkj.mq.service.OrderTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单超时服务实现类
 * 提供订单超时延时取消的完整功能
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderTimeoutServiceImpl implements OrderTimeoutService {

    private final RabbitTemplate rabbitTemplate;

    // 注册的订单超时处理器
    private final Map<String, OrderTimeoutHandler> handlers = new ConcurrentHashMap<>();
    private String currentHandlerName = "default";

    // ==================== 订单超时队列配置 ====================
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

    @Value("${spring.rabbitmq.order-timeout.max-retry-count:3}")
    private int maxRetryCount;

    // ==================== 处理器管理方法 ====================

    @Override
    public void registerHandler(OrderTimeoutHandler handler) {
        if (handler != null) {
            handlers.put(handler.getHandlerName(), handler);
            log.info("✅ 注册订单超时处理器: {}", handler.getHandlerName());
        }
    }

    @Override
    public boolean setCurrentHandler(String handlerName) {
        if (handlers.containsKey(handlerName)) {
            this.currentHandlerName = handlerName;
            log.info("✅ 切换订单超时处理器为: {}", handlerName);
            return true;
        }
        log.warn("❌ 订单超时处理器不存在: {}", handlerName);
        return false;
    }

    @Override
    public String getCurrentHandler() {
        return currentHandlerName;
    }

    /**
     * 获取当前处理器
     */
    private OrderTimeoutHandler getCurrentTimeoutHandler() {
        OrderTimeoutHandler handler = handlers.get(currentHandlerName);
        if (handler == null) {
            log.warn("⚠️ 未找到处理器: {}, 使用默认处理器", currentHandlerName);
            handler = handlers.get("default");
        }
        return handler;
    }

    // ==================== 发送消息方法 ====================

    @Override
    public boolean sendOrderTimeoutMessage(OrderTimeoutMessage message) {
        return sendOrderTimeoutMessage(message, defaultDelayTime);
    }

    @Override
    public boolean sendOrderTimeoutMessage(OrderTimeoutMessage message, long delayTimeMs) {
        try {
            // 验证消息完整性
            if (!message.validateComplete()) {
                log.error("❌ 订单超时消息验证失败: orderId={}, messageId={}", 
                    message.getOrderId(), message.getMessageId());
                return false;
            }

            // 确保消息ID存在
            if (message.getMessageId() == null) {
                message.setMessageId(generateMessageId());
            }

            // 设置延时时间
            message.setDelayTime(delayTimeMs);

            // 发送到延时队列
            final long finalDelayTime = validateDelayTime(delayTimeMs);
            rabbitTemplate.convertAndSend(timeoutExchange, timeoutRoutingKey, message, msg -> {
                // 设置消息过期时间
                msg.getMessageProperties().setExpiration(String.valueOf(finalDelayTime));
                return msg;
            });

            // 记录消息到业务模块（通过处理器）
            OrderTimeoutHandler handler = getCurrentTimeoutHandler();
            if (handler != null) {
                handler.recordTimeoutMessage(message);
            }

            log.info("⏰ 订单超时消息发送成功 - orderId: {}, messageId: {}, 延时: {}ms", 
                message.getOrderId(), message.getMessageId(), finalDelayTime);
            return true;

        } catch (Exception e) {
            log.error("❌ 发送订单超时消息失败 - orderId: {}, messageId: {}", 
                message.getOrderId(), message.getMessageId(), e);
            return false;
        }
    }

    @Override
    public boolean sendQuickOrderTimeout(String orderId, String orderStatus, long delayTimeMs) {
        try {
            OrderTimeoutMessage message = OrderTimeoutMessage.buildOrderTimeout(orderId, orderStatus, delayTimeMs);
            return sendOrderTimeoutMessage(message, delayTimeMs);
        } catch (Exception e) {
            log.error("❌ 快速发送订单超时消息失败 - orderId: {}", orderId, e);
            return false;
        }
    }

    @Override
    public boolean cancelOrderTimeout(String messageId) {
        try {
            // 通过处理器标记消息为已取消
            OrderTimeoutHandler handler = getCurrentTimeoutHandler();
            if (handler != null) {
                handler.cancelTimeoutMessage(messageId);
            }
            
            log.info("✅ 订单超时任务已标记取消 - messageId: {}", messageId);
            return true;
        } catch (Exception e) {
            log.error("❌ 取消订单超时任务失败 - messageId: {}", messageId, e);
            return false;
        }
    }

    @Override
    public String getOrderTimeoutQueueStatus() {
        try {
            Message message = rabbitTemplate.receive(timeoutQueue, 100);
            if (message != null) {
                // 将消息放回队列
                rabbitTemplate.send(timeoutExchange, timeoutRoutingKey, message);
                return "🔴 订单超时队列中有消息待处理";
            } else {
                return "🟢 订单超时队列为空";
            }
        } catch (Exception e) {
            return "❌ 订单超时队列状态检查失败: " + e.getMessage();
        }
    }

    // ==================== 消息消费方法 ====================

    /**
     * 订单超时消息监听器
     * 当消息过期后自动触发，执行订单取消逻辑
     */
    @RabbitListener(queues = "${spring.rabbitmq.order-timeout.queue:order.timeout.queue}")
    public void onOrderTimeoutMessage(Message message, Channel channel,
                                     @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        String orderId = null;
        String messageId = null;
        
        try {
            log.info("🔔 收到订单超时消息 - deliveryTag: {}", deliveryTag);

            // 解析消息
            OrderTimeoutMessage timeoutMessage = parseMessage(message);
            if (timeoutMessage == null) {
                log.error("❌ 订单超时消息解析失败 - deliveryTag: {}", deliveryTag);
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            orderId = timeoutMessage.getOrderId();
            messageId = timeoutMessage.getMessageId();

            log.info("开始处理订单超时 - orderId: {}, messageId: {}, 业务类型: {}", 
                orderId, messageId, timeoutMessage.getBusinessType());

            // 获取处理器
            OrderTimeoutHandler handler = getCurrentTimeoutHandler();
            if (handler == null) {
                log.error("❌ 未找到订单超时处理器 - orderId: {}", orderId);
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            // 检查消息是否已被取消
            if (handler.isMessageCancelled(messageId)) {
                log.info("订单超时任务已取消，跳过处理 - orderId: {}, messageId: {}", orderId, messageId);
                channel.basicAck(deliveryTag, false);
                handler.clearTimeoutMessage(messageId);
                return;
            }

            // 幂等性检查
            if (handler.isMessageProcessed(messageId)) {
                log.info("订单超时消息已处理过，跳过 - orderId: {}, messageId: {}", orderId, messageId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 执行订单取消业务逻辑
            long startTime = System.currentTimeMillis();
            boolean success = handleOrderTimeout(timeoutMessage);
            long processTime = System.currentTimeMillis() - startTime;

            if (success) {
                // 处理成功，确认消息
                handler.markMessageAsProcessed(messageId);
                handler.clearTimeoutMessage(messageId);
                channel.basicAck(deliveryTag, false);
                log.info("✅ 订单超时处理成功 - orderId: {}, messageId: {}, 处理时间: {}ms", 
                    orderId, messageId, processTime);
            } else {
                // 处理失败，根据重试次数决定是否重试
                handleFailedMessage(timeoutMessage, channel, deliveryTag);
            }

        } catch (Exception e) {
            log.error("💥 处理订单超时消息异常 - orderId: {}, messageId: {}, deliveryTag: {}", 
                orderId, messageId, deliveryTag, e);
            
            try {
                OrderTimeoutMessage timeoutMessage = parseMessage(message);
                if (timeoutMessage != null) {
                    handleFailedMessage(timeoutMessage, channel, deliveryTag);
                } else {
                    channel.basicNack(deliveryTag, false, false);
                }
            } catch (Exception ex) {
                log.error("处理异常消息失败 - deliveryTag: {}", deliveryTag, ex);
                channel.basicNack(deliveryTag, false, false);
            }
        }
    }

    /**
     * 死信队列监听器（可选）
     * 处理最终失败的订单超时消息
     */
    // @RabbitListener(queues = "${spring.rabbitmq.order-timeout.dead-letter-queue:order.timeout.dlx.queue}")
    public void onDeadLetterMessage(Message message, Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.warn("💀 收到订单超时死信消息 - deliveryTag: {}", deliveryTag);
            
            OrderTimeoutMessage timeoutMessage = parseMessage(message);
            if (timeoutMessage != null) {
                log.error("死信消息详情 - orderId: {}, messageId: {}, 重试次数: {}", 
                    timeoutMessage.getOrderId(), timeoutMessage.getMessageId(), timeoutMessage.getRetryCount());
                
                // 发送告警通知
                sendAlertNotification(timeoutMessage);
            }
            
            // 确认消息，避免死信队列堆积
            channel.basicAck(deliveryTag, false);
            
        } catch (Exception e) {
            log.error("处理订单超时死信消息异常 - deliveryTag: {}", deliveryTag, e);
            channel.basicAck(deliveryTag, false);
        }
    }

    // ==================== 业务处理方法 ====================

    /**
     * 处理订单超时逻辑
     * 调用注册的处理器来处理业务逻辑
     */
    private boolean handleOrderTimeout(OrderTimeoutMessage message) {
        try {
            // 获取当前处理器
            OrderTimeoutHandler handler = getCurrentTimeoutHandler();
            
            if (handler == null) {
                log.error("❌ 未找到订单超时处理器，无法处理 - orderId: {}", message.getOrderId());
                return false;
            }

            // 检查是否已处理过
            if (handler.isMessageProcessed(message.getOrderId())) {
                log.info("订单已处理过，跳过 - orderId: {}, handler: {}", 
                    message.getOrderId(), handler.getHandlerName());
                return true;
            }
            
            log.info("执行订单超时处理 - orderId: {}, handler: {}, 取消原因: {}", 
                message.getOrderId(), handler.getHandlerName(), message.getCancelReason());

            // 调用业务处理器
            boolean success = handler.handleOrderTimeout(message);
            
            if (success) {
                log.info("✅ 订单超时处理成功 - orderId: {}, handler: {}", 
                    message.getOrderId(), handler.getHandlerName());
            } else {
                log.error("❌ 订单超时处理失败 - orderId: {}, handler: {}", 
                    message.getOrderId(), handler.getHandlerName());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("处理订单超时异常 - orderId: {}", message.getOrderId(), e);
            return false;
        }
    }

    /**
     * 处理失败的消息
     */
    private void handleFailedMessage(OrderTimeoutMessage message, Channel channel, long deliveryTag) throws IOException {
        try {
            message.incrementRetryCount();
            
            if (message.getRetryCount() >= maxRetryCount) {
                log.warn("订单超时消息重试次数已达上限，发送到死信队列 - orderId: {}, messageId: {}, 重试次数: {}", 
                    message.getOrderId(), message.getMessageId(), message.getRetryCount());
                
                // 发送到死信队列
                rabbitTemplate.convertAndSend(deadLetterExchange, deadLetterRoutingKey, message);
                channel.basicAck(deliveryTag, false);
                
                // 发送告警
                sendAlertNotification(message);
            } else {
                log.warn("订单超时消息处理失败，重新入队 - orderId: {}, messageId: {}, 重试次数: {}", 
                    message.getOrderId(), message.getMessageId(), message.getRetryCount());
                
                // 重新入队（使用原队列）
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (Exception e) {
            log.error("处理失败消息异常 - orderId: {}, messageId: {}", 
                message.getOrderId(), message.getMessageId(), e);
            channel.basicNack(deliveryTag, false, true);
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 解析消息
     */
    private OrderTimeoutMessage parseMessage(Message message) {
        try {
            Object payload = rabbitTemplate.getMessageConverter().fromMessage(message);
            if (payload instanceof OrderTimeoutMessage) {
                return (OrderTimeoutMessage) payload;
            } else {
                log.error("消息类型不匹配，期望: OrderTimeoutMessage, 实际: {}", 
                    payload != null ? payload.getClass().getName() : "null");
                return null;
            }
        } catch (Exception e) {
            log.error("解析订单超时消息失败", e);
            return null;
        }
    }

    /**
     * 验证延时时间
     */
    private long validateDelayTime(long delayTimeMs) {
        long actualDelayTime = Math.max(delayTimeMs, 1000); // 最小1秒
        if (actualDelayTime > 24 * 60 * 60 * 1000) { // 最大24小时
            actualDelayTime = 24 * 60 * 60 * 1000;
            log.warn("⚠️ 延时时间超过24小时，已自动调整为24小时");
        }
        return actualDelayTime;
    }

    /**
     * 生成消息ID
     */
    private String generateMessageId() {
        return "TIMEOUT_" + System.currentTimeMillis() + "_" + 
            java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 发送告警通知
     */
    private void sendAlertNotification(OrderTimeoutMessage message) {
        try {
            String alertContent = String.format(
                "🚨 订单超时处理失败告警\n订单ID: %s\n业务类型: %s\n重试次数: %s\n时间: %s\n请及时处理！",
                message.getOrderId(), 
                message.getBusinessType(),
                message.getRetryCount(),
                java.time.LocalDateTime.now()
            );
            log.error("发送订单超时告警: {}", alertContent);
            // TODO: 接入企业微信、钉钉等告警渠道
            // WeChatBotMessageUtil.sendListMessage(alertContent);
        } catch (Exception e) {
            log.error("发送告警通知失败", e);
        }
    }
}
