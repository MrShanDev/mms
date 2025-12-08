package com.sxpcwlkj.mq.service.impl;

import com.rabbitmq.client.Channel;
import com.sxpcwlkj.mq.entity.QueueStatus;
import com.sxpcwlkj.mq.entity.DistributionMessage;
import com.sxpcwlkj.mq.hander.MqHandler;
import com.sxpcwlkj.mq.service.MqService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.wx.utils.WeChatBotMessageUtil;
import com.sxpcwlkj.mq.utils.MessageTraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 重构后的消息队列服务 - 接口风格
 * 作为独立模块，通过回调接口与业务模块解耦
 */
@Service
@Slf4j
public class MqServiceImpl implements MqService {

    private final RabbitTemplate rabbitTemplate;


    // 注册的消息处理器
    private final Map<String, MqHandler> messageHandlers = new ConcurrentHashMap<>();
    private final List<String> handlerTypes = new CopyOnWriteArrayList<>();
    private String currentHandlerType = "default";

    // ==================== MQ配置属性 ====================
    @Value("${spring.rabbitmq.config.queue:distribution.queue}")
    private String queueName;

    @Value("${spring.rabbitmq.config.exchange:distribution.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.config.routing-key:distribution.key}")
    private String routingKey;

    @Value("${spring.rabbitmq.config.dead-letter-queue:distribution.dlx.queue}")
    private String deadLetterQueue;

    @Value("${spring.rabbitmq.config.dead-letter-exchange:distribution.dlx.exchange}")
    private String deadLetterExchange;

    @Value("${spring.rabbitmq.config.dead-letter-routing-key:distribution.dlx.key}")
    private String deadLetterRoutingKey;

    @Value("${spring.rabbitmq.config.delay-queue:distribution.delay.queue}")
    private String delayQueue;

    @Value("${spring.rabbitmq.config.delay-exchange:distribution.delay.exchange}")
    private String delayExchange;

    @Value("${spring.rabbitmq.config.delay-routing-key:distribution.delay.key}")
    private String delayRoutingKey;

    @Value("${spring.rabbitmq.config.delay-time:60000}")
    private int delayTime;

    @Value("${spring.rabbitmq.config.retry-queue:distribution.retry.queue}")
    private String retryQueue;

    @Value("${spring.rabbitmq.config.retry-routing-key:distribution.retry.key}")
    private String retryRoutingKey;

    @Value("${spring.rabbitmq.config.retry-interval:5000}")
    private int retryInterval;

    @Value("${spring.rabbitmq.config.max-retry-count:3}")
    private int maxRetryCount;
    // 构造函数中初始化基础组件
    public MqServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        log.info("✅ MQ服务初始化完成");
    }

    // ==================== 处理器管理方法 ====================

    @Override
    public void registerHandler(MqHandler handler) {
        if (handler != null) {
            messageHandlers.put(handler.getHandlerType(), handler);
            handlerTypes.add(handler.getHandlerType());
            log.info("✅ 注册消息处理器: {}", handler.getHandlerType());
        }
    }

    @Override
    public void registerHandlers(Map<String, MqHandler> handlers) {
        if (handlers != null) {
            handlers.forEach((key, handler) -> registerHandler(handler));
        }
    }

    @Override
    public List<String> getRegisteredHandlers() {
        return new ArrayList<>(handlerTypes);
    }

    @Override
    public boolean setCurrentHandler(String handlerType) {
        if (messageHandlers.containsKey(handlerType)) {
            this.currentHandlerType = handlerType;
            log.info("✅ 切换当前处理器为: {}", handlerType);
            return true;
        }
        log.warn("❌ 处理器不存在: {}", handlerType);
        return false;
    }

    @Override
    public String getCurrentHandler() {
        return currentHandlerType;
    }

    private MqHandler getCurrentMessageHandler() {
        return messageHandlers.getOrDefault(currentHandlerType, messageHandlers.get("default"));
    }

    // ==================== 消息发送方法 ====================

    @Override
    public boolean sendToMainQueue(DistributionMessage message) {
        try {
            // 验证消息完整性
            if (!message.validateComplete()) {
                log.error("❌ 消息验证失败: orderId={}, messageId={}", message.getOrderId(), message.getMessageId());
                return false;
            }

            ensureMessageId(message);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("📤 消息发送到主队列成功, orderId: {}, messageId: {}, handler: {}",
                message.getOrderId(), message.getMessageId(), currentHandlerType);
            return true;
        } catch (Exception e) {
            log.error("❌ 发送消息到主队列失败, orderId: {}, messageId: {}, handler: {}",
                message.getOrderId(), message.getMessageId(), currentHandlerType, e);
            return false;
        }
    }

    @Override
    public boolean sendToDelayQueue(DistributionMessage message) {
        return sendToDelayQueue(message, delayTime);
    }

    @Override
    public boolean sendToDelayQueue(DistributionMessage message, int delayTime) {
        try {
            // 验证消息完整性
            if (!message.validateComplete()) {
                log.error("❌ 消息验证失败: orderId={}, messageId={}", message.getOrderId(), message.getMessageId());
                return false;
            }

            ensureMessageId(message);
            int actualDelayTime = validateDelayTime(delayTime);

            final int finalDelayTime = actualDelayTime;
            rabbitTemplate.convertAndSend(delayExchange, delayRoutingKey, message, msg -> {
                msg.getMessageProperties().setDelay(finalDelayTime);
                msg.getMessageProperties().setExpiration(String.valueOf(finalDelayTime));
                return msg;
            });

            log.info("⏰ 消息发送到延时队列成功, orderId: {}, messageId: {}, 延时: {}ms, handler: {}",
                message.getOrderId(), message.getMessageId(), actualDelayTime, currentHandlerType);
            return true;
        } catch (Exception e) {
            log.error("❌ 发送消息到延时队列失败, orderId: {}, messageId: {}, delayTime: {}ms, handler: {}",
                message.getOrderId(), message.getMessageId(), delayTime, currentHandlerType, e);
            return false;
        }
    }

    @Override
    public boolean sendToRetryQueue(DistributionMessage message) {
        try {
            ensureMessageId(message);
            incrementRetryCount(message);
            rabbitTemplate.convertAndSend(exchange, retryRoutingKey, message);
            log.info("🔄 消息发送到重试队列成功, orderId: {}, messageId: {}, 重试次数: {}, handler: {}",
                message.getOrderId(), message.getMessageId(), message.getRetryCount(), currentHandlerType);
            return true;
        } catch (Exception e) {
            log.error("❌ 发送消息到重试队列失败, orderId: {}, handler: {}",
                message.getOrderId(), currentHandlerType, e);
            return false;
        }
    }

    @Override
    public boolean sendSmart(DistributionMessage message) {
        try {
            ensureMessageId(message);
            String queueType = determineOptimalQueue(message);
            boolean success = sendToQueueByType(message, queueType);

            if (success) {
                log.info("🤖 智能发送成功, orderId: {}, messageId: {}, 队列类型: {}, handler: {}",
                    message.getOrderId(), message.getMessageId(), queueType, currentHandlerType);
            } else {
                log.error("❌ 智能发送失败, orderId: {}, 队列类型: {}, handler: {}",
                    message.getOrderId(), queueType, currentHandlerType);
            }
            return success;
        } catch (Exception e) {
            log.error("💥 智能发送异常, orderId: {}, handler: {}", message.getOrderId(), currentHandlerType, e);
            return sendToMainQueue(message);
        }
    }

    @Override
    public boolean sendToDeadLetterQueue(DistributionMessage message) {
        try {
            ensureMessageId(message);
            rabbitTemplate.convertAndSend(deadLetterExchange, deadLetterRoutingKey, message);
            log.warn("💀 消息发送到死信队列, orderId: {}, messageId: {}, handler: {}",
                message.getOrderId(), message.getMessageId(), currentHandlerType);
            return true;
        } catch (Exception e) {
            log.error("❌ 发送消息到死信队列失败, orderId: {}, handler: {}",
                message.getOrderId(), currentHandlerType, e);
            return false;
        }
    }

    // ==================== 消息消费方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String consumeOneMessage() {
        try {
            log.info("开始手动消费一条消息... handler: {}", currentHandlerType);
            Message message = rabbitTemplate.receive(queueName);

            if (message == null) {
                return "❌ 队列中没有消息";
            }

            DistributionMessage distributionMessage = parseMessage(message);
            if (distributionMessage == null) {
                rabbitTemplate.send(exchange, routingKey, message);
                return "❌ 消息解析失败，消息已重新入队";
            }

            return processDistributionMessage(distributionMessage, message);
        } catch (Exception e) {
            log.error("手动消费消息异常, handler: {}", currentHandlerType, e);
            return "❌ 消费异常: " + e.getMessage();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String consumeBatchMessages(int count) {
        return processMessagesInBatch(count, this::consumeOneMessage, "批量消费");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String consumeOneDeadLetterMessage() {
        try {
            log.info("开始从死信队列消费一条消息... handler: {}", currentHandlerType);
            Message message = rabbitTemplate.receive(deadLetterQueue);

            if (message == null) {
                return "❌ 死信队列中没有消息";
            }

            DistributionMessage distributionMessage = parseMessage(message);
            if (distributionMessage == null) {
                log.error("死信消息解析失败，直接丢弃");
                return "❌ 死信消息解析失败，直接丢弃";
            }

            String orderId = distributionMessage.getOrderId();
            log.info("处理死信消息, orderId: {}, handler: {}", orderId, currentHandlerType);

            MqHandler handler = getCurrentMessageHandler();
            if (handler.isMessageProcessed(orderId)) {
                log.info("死信订单已处理过，直接确认, orderId: {}, handler: {}", orderId, currentHandlerType);
                return "✅ 死信订单已处理过，直接确认: " + orderId;
            }

            boolean success = handler.handleMessage(distributionMessage);
            if (success) {
                log.info("✅ 死信消息处理成功, orderId: {}, handler: {}", orderId, currentHandlerType);
                return "✅ 死信消息处理成功: " + orderId;
            } else {
                rabbitTemplate.send(exchange, routingKey, message);
                log.error("❌ 死信消息处理失败，重新放回死信队列, orderId: {}, handler: {}", orderId, currentHandlerType);
                return "❌ 死信消息处理失败，重新放回死信队列: " + orderId;
            }
        } catch (Exception e) {
            log.error("处理死信消息异常, handler: {}", currentHandlerType, e);
            return "❌ 处理死信消息异常: " + e.getMessage();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reprocessDeadLetterMessages(int count) {
        return processMessagesInBatch(count, this::consumeOneDeadLetterMessage, "死信队列重新处理");
    }

    @Override
    public String redeliverDeadLetterToMainQueue(int count) {
        List<String> results = new ArrayList<>();
        int successCount = 0;

        log.info("开始将死信消息重新投递到主队列，数量: {}, handler: {}", count, currentHandlerType);

        for (int i = 0; i < count; i++) {
            try {
                Message message = rabbitTemplate.receive(deadLetterQueue);
                if (message == null) {
                    results.add("❌ 死信队列已空");
                    break;
                }

                rabbitTemplate.send(exchange, routingKey, message);
                successCount++;
                results.add((i + 1) + ": ✅ 死信消息重新投递到主队列成功");
                log.info("死信消息重新投递到主队列成功: {}, handler: {}", i + 1, currentHandlerType);
            } catch (Exception e) {
                results.add((i + 1) + ": ❌ 死信消息重新投递失败: " + e.getMessage());
                log.error("死信消息重新投递失败, handler: {}", currentHandlerType, e);
            }
        }

        String summary = String.format("死信消息重新投递完成: 成功=%d, 总计=%d, handler=%s",
            successCount, results.size(), currentHandlerType);
        results.add(0, summary);
        log.info(summary);
        return String.join("\n", results);
    }

    // ==================== 自动监听器 ====================

    /**
     * 自动消费监听器 - 主队列
     */
    @RabbitListener(queues = "${spring.rabbitmq.config.queue:distribution.queue}")
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(Message message, Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        String orderId = null;
        String messageId = null;
        String traceId = null;
        try {
            // 设置消息追踪
            traceId = MessageTraceUtil.generateTraceId();
            MessageTraceUtil.setTraceId(traceId);

            log.info("🔔 自动消费者收到消息, deliveryTag: {}, handler: {}", deliveryTag, currentHandlerType);

            if (message == null) {
                log.error("❌ 收到空消息");
                channel.basicAck(deliveryTag, false);
                return;
            }

            DistributionMessage distributionMessage = parseMessage(message);
            if (distributionMessage == null) {
                log.error("❌ 消息解析失败, deliveryTag: {}", deliveryTag);
                // 解析失败的消息进入死信队列
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            orderId = distributionMessage.getOrderId();
            messageId = distributionMessage.getMessageId();

            log.info("开始处理消息 - 订单ID: {}, 消息ID: {}, 追踪ID: {}, handler: {}",
                orderId, messageId, traceId, currentHandlerType);

            // 幂等性检查
            if (isMessageProcessed(messageId)) {
                log.info("消息已处理过，直接确认, orderId: {}, messageId: {}, 追踪ID: {}, handler: {}",
                    orderId, messageId, traceId, currentHandlerType);
                channel.basicAck(deliveryTag, false);
                return;
            }

            log.info("自动消费消息, orderId: {}, messageId: {}, deliveryTag: {}, 追踪ID: {}, handler: {}",
                orderId, messageId, deliveryTag, traceId, currentHandlerType);

            MqHandler handler = getCurrentMessageHandler();
            long startTime = System.currentTimeMillis();
            boolean success = handler.handleMessage(distributionMessage);
            long processTime = System.currentTimeMillis() - startTime;

            // 记录处理统计
            recordMessageProcessing(orderId, messageId, success, processTime);

            if (success) {
                // 处理成功，标记为已处理并确认消息
                markMessageAsProcessed(messageId);
                channel.basicAck(deliveryTag, false);
                log.info("✅ 自动消费成功, orderId: {}, messageId: {}, deliveryTag: {}, 处理时间: {}ms, 追踪ID: {}, handler: {}",
                    orderId, messageId, deliveryTag, processTime, traceId, currentHandlerType);
            } else {
                // 处理失败，根据重试次数决定是否重试
                handleFailedMessage(distributionMessage, channel, deliveryTag, traceId);
            }
        } catch (Exception e) {
            log.error("💥 自动消费异常, orderId: {}, messageId: {}, deliveryTag: {}, 追踪ID: {}, handler: {}",
                orderId, messageId, deliveryTag, traceId, currentHandlerType, e);
            // 异常情况下根据重试次数决定是否重试
            try {
                DistributionMessage distributionMessage = parseMessage(message);
                if (distributionMessage != null) {
                    handleFailedMessage(distributionMessage, channel, deliveryTag, traceId);
                } else {
                    channel.basicNack(deliveryTag, false, false);
                }
            } catch (Exception ex) {
                log.error("处理异常消息失败, deliveryTag: {}, 追踪ID: {}", deliveryTag, traceId, ex);
                channel.basicNack(deliveryTag, false, false);
            }
        } finally {
            // 清理追踪上下文
            MessageTraceUtil.clearTraceId();
        }
    }

    /**
     * 处理失败的消息
     * @param message 消息
     * @param channel 通道
     * @param deliveryTag 交付标签
     * @param traceId 追踪ID
     */
    private void handleFailedMessage(DistributionMessage message, Channel channel, long deliveryTag, String traceId) throws IOException {
        try {
            // 递增重试次数
            incrementRetryCount(message);

            // 检查是否超过最大重试次数
            if (message.getRetryCount() >= maxRetryCount) {
                log.warn("消息重试次数已达上限，发送到死信队列, orderId: {}, messageId: {}, 重试次数: {}, 追踪ID: {}, handler: {}",
                    message.getOrderId(), message.getMessageId(), message.getRetryCount(), traceId, currentHandlerType);

                // 发送到死信队列
                sendToDeadLetterQueue(message);
                // 确认原消息
                channel.basicAck(deliveryTag, false);

                // 发送告警通知
                sendAlertNotification(message.getOrderId(), message);
            } else {
                log.warn("消息处理失败，发送到重试队列, orderId: {}, messageId: {}, 重试次数: {}, 追踪ID: {}, handler: {}",
                    message.getOrderId(), message.getMessageId(), message.getRetryCount(), traceId, currentHandlerType);

                // 发送到重试队列
                sendToRetryQueue(message);
                // 确认原消息
                channel.basicAck(deliveryTag, false);
            }
        } catch (Exception e) {
            log.error("处理失败消息异常, orderId: {}, messageId: {}, 追踪ID: {}, handler: {}",
                message.getOrderId(), message.getMessageId(), traceId, currentHandlerType, e);
            // 如果处理失败消息也异常，拒绝消息并重新入队
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 死信队列自动监听器
     */
    //@RabbitListener(queues = "${spring.rabbitmq.config.dead-letter-queue:distribution.dlx.queue}")
    public void onDeadLetterMessage(Message message, Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.warn("💀 收到死信队列消息, deliveryTag: {}, handler: {}", deliveryTag, currentHandlerType);

            DistributionMessage distributionMessage = parseMessage(message);
            if (distributionMessage != null) {
                String orderId = distributionMessage.getOrderId();
                String messageId = distributionMessage.getMessageId();
                log.error("死信消息详情 - 订单ID: {}, 消息ID: {}, 购买者: {}, 租户: {}, 重试次数: {}, handler: {}",
                    orderId, messageId, distributionMessage.getBuyerId(),
                    distributionMessage.getTenantId(), distributionMessage.getRetryCount(), currentHandlerType);
                sendAlertNotification(orderId, distributionMessage);

                // 尝试手动处理死信消息
                try {
                    MqHandler handler = getCurrentMessageHandler();
                    boolean manualSuccess = handler.handleMessage(distributionMessage);
                    if (manualSuccess) {
                        log.info("死信消息手动处理成功, orderId: {}, messageId: {}, handler: {}",
                            orderId, messageId, currentHandlerType);
                        // 处理成功则确认消息
                        channel.basicAck(deliveryTag, false);
                        return;
                    }
                } catch (Exception e) {
                    log.error("死信消息手动处理异常, orderId: {}, messageId: {}, handler: {}",
                        orderId, messageId, currentHandlerType, e);
                }
            } else {
                log.error("死信消息解析失败, deliveryTag: {}, handler: {}", deliveryTag, currentHandlerType);
            }

            // 死信消息也需要确认，避免一直堆积
            channel.basicAck(deliveryTag, false);
            log.info("✅ 死信消息已确认, deliveryTag: {}, handler: {}", deliveryTag, currentHandlerType);
        } catch (Exception e) {
            log.error("处理死信消息异常, deliveryTag: {}, handler: {}", deliveryTag, currentHandlerType, e);
            // 死信队列处理异常也需确认，防止阻塞
            channel.basicAck(deliveryTag, false);
        }
    }

    // ==================== 状态监控方法 ====================

    @Override
    public QueueStatus getQueueStatus() {
        QueueStatus status = new QueueStatus();
        try {
            status.setMainQueueStatus(checkQueueStatus(queueName));
            status.setDeadLetterQueueStatus(checkQueueStatus(deadLetterQueue));
            status.setDelayQueueStatus(checkQueueStatus(delayQueue));
            status.setRetryQueueStatus(checkQueueStatus(retryQueue));
            status.setCheckTime(new Date());
            status.setServiceStatus("正常");
            status.setTotalMessages(estimateTotalMessageCount());
            status.setCurrentHandler(currentHandlerType);
            status.setRegisteredHandlers(new ArrayList<>(handlerTypes));

            log.debug("📊 队列状态检查完成: 主队列={}, 死信队列={}, 延时队列={}, 重试队列={}, handler={}",
                status.getMainQueueStatus(), status.getDeadLetterQueueStatus(),
                status.getDelayQueueStatus(), status.getRetryQueueStatus(), currentHandlerType);
        } catch (Exception e) {
            log.error("❌ 获取队列状态异常, handler: {}", currentHandlerType, e);
            status.setServiceStatus("异常: " + e.getMessage());
        }
        return status;
    }

    @Override
    public String getDeadLetterQueueStatus() {
        try {
            Message message = rabbitTemplate.receive(deadLetterQueue, 100);
            if (message != null) {
                rabbitTemplate.send(exchange, routingKey, message);
                return "🔴 死信队列中有消息需要处理";
            } else {
                return "🟢 死信队列为空";
            }
        } catch (Exception e) {
            return "❌ 死信队列状态检查失败: " + e.getMessage();
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 记录消息处理统计
     */
    private void recordMessageProcessing(String orderId, String messageId, boolean success, long processTime) {
        try {
            String key = "mq:stats:" + LocalDate.now().toString();
            String field = success ? "success" : "failed";

            // 使用Redis记录处理统计（过期时间24小时）
            String countKey = key + ":" + field;
            Long currentCount = RedisUtil.getCacheObject(countKey);
            if (currentCount == null) {
                currentCount = 0L;
            }
            RedisUtil.setCacheObject(countKey, currentCount + 1, Duration.ofHours(24));

            // 使用现有的 expire 方法
            RedisUtil.expire(key, Duration.ofHours(24));

            // 记录处理时间统计
            String timeKey = "mq:time:" + LocalDate.now().toString();
            String totalTimeKey = timeKey + ":total";
            String countTimeKey = timeKey + ":count";

            Long total = RedisUtil.getCacheObject(totalTimeKey);
            if (total == null) {
                total = 0L;
            }
            RedisUtil.setCacheObject(totalTimeKey, total + processTime, Duration.ofHours(24));

            Long timeCount = RedisUtil.getCacheObject(countTimeKey);
            if (timeCount == null) {
                timeCount = 0L;
            }
            RedisUtil.setCacheObject(countTimeKey, timeCount + 1, Duration.ofHours(24));

            // 使用现有的 expire 方法
            RedisUtil.expire(timeKey, Duration.ofHours(24));

            if (!success) {
                log.warn("消息处理失败统计 - 订单ID: {}, 消息ID: {}, 处理时间: {}ms", orderId, messageId, processTime);
            }
        } catch (Exception e) {
            log.warn("记录消息处理统计异常", e);
        }
    }

    /**
     * 处理分销消息
     */
    private String processDistributionMessage(DistributionMessage distributionMessage, Message message) {
        String orderId = distributionMessage.getOrderId();
        String messageId = distributionMessage.getMessageId();
        long startTime = System.currentTimeMillis();

        log.info("手动消费消息, orderId: {}, messageId: {}, handler: {}", orderId, messageId, currentHandlerType);

        // 幂等性检查
        if (isMessageProcessed(messageId)) {
            log.info("订单已处理过，跳过计算, orderId: {}, messageId: {}, handler: {}", orderId, messageId, currentHandlerType);
            return "✅ 订单已处理过，跳过计算: " + orderId;
        }

        MqHandler handler = getCurrentMessageHandler();
        boolean success = handler.handleMessage(distributionMessage);
        long processTime = System.currentTimeMillis() - startTime;

        // 记录处理统计
        recordMessageProcessing(orderId, messageId, success, processTime);

        if (success) {
            // 处理成功，标记为已处理
            markMessageAsProcessed(messageId);
            log.info("✅ 手动消费成功, orderId: {}, messageId: {}, 处理时间: {}ms, handler: {}",
                orderId, messageId, processTime, currentHandlerType);
            return "✅ 手动消费成功: " + orderId;
        } else {
            // 处理失败，重新入队
            rabbitTemplate.send(exchange, routingKey, message);
            log.error("❌ 手动消费失败，消息已重新入队, orderId: {}, messageId: {}, 处理时间: {}ms, handler: {}",
                orderId, messageId, processTime, currentHandlerType);
            return "❌ 处理失败，消息已重新入队: " + orderId;
        }
    }

    /**
     * 批量处理消息
     */
    private String processMessagesInBatch(int count, MessageProcessor processor, String processName) {
        List<String> results = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        log.info("开始{}，数量: {}, handler: {}", processName, count, currentHandlerType);

        for (int i = 0; i < count; i++) {
            String result = processor.process();
            results.add((i + 1) + ": " + result);

            if (result.startsWith("✅")) {
                successCount++;
            } else if (result.startsWith("❌")) {
                failCount++;
            }

            // 控制处理速度
            if ((i + 1) % (processName.contains("死信") ? 5 : 10) == 0) {
                try {
                    Thread.sleep(processName.contains("死信") ? 200 : 100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        String summary = String.format("%s完成: 成功=%d, 失败=%d, 总计=%d, handler=%s",
            processName, successCount, failCount, results.size(), currentHandlerType);
        results.add(0, summary);
        log.info(summary);
        return String.join("\n", results);
    }

    /**
     * 解析消息体
     */
    private DistributionMessage parseMessage(Message message) {
        try {
            Object payload = rabbitTemplate.getMessageConverter().fromMessage(message);
            if (payload instanceof DistributionMessage) {
                return (DistributionMessage) payload;
            } else {
                log.error("消息类型不匹配，期望: DistributionMessage, 实际: {}, handler: {}",
                    payload != null ? payload.getClass().getName() : "null", currentHandlerType);
                return null;
            }
        } catch (Exception e) {
            log.error("消息解析失败, handler: {}", currentHandlerType, e);
            return null;
        }
    }

    /**
     * 发送告警通知
     */
    private void sendAlertNotification(String orderId, DistributionMessage message) {
        String alertContent = String.format(
            "🚨 分销计算失败告警\n订单ID: %s\n购买者ID: %s\n租户ID: %s\n处理器: %s\n时间: %s\n请及时处理！",
            orderId, message.getBuyerId(), message.getTenantId(), currentHandlerType, new Date()
        );
        WeChatBotMessageUtil.sendListMessage(alertContent);
        log.error("发送告警通知: {}, handler: {}", alertContent, currentHandlerType);
    }

    /**
     * 确保消息ID存在
     */
    private void ensureMessageId(DistributionMessage message) {
        if (message.getMessageId() == null) {
            message.setMessageId(generateMessageId());
            log.debug("生成消息ID: {}, handler: {}", message.getMessageId(), currentHandlerType);
        }
    }

    /**
     * 验证延时时间有效性
     */
    private int validateDelayTime(int delayTime) {
        int actualDelayTime = Math.max(delayTime, 1000);
        if (actualDelayTime > 24 * 60 * 60 * 1000) {
            actualDelayTime = 24 * 60 * 60 * 1000;
            log.warn("⚠️ 延时时间超过24小时，已自动调整为24小时, handler: {}", currentHandlerType);
        }
        return actualDelayTime;
    }

    /**
     * 递增重试次数
     */
    private void incrementRetryCount(DistributionMessage message) {
        int currentRetryCount = message.getRetryCount() != null ? message.getRetryCount() : 0;
        message.setRetryCount(currentRetryCount + 1);
        log.debug("递增重试次数: {}, handler: {}", message.getRetryCount(), currentHandlerType);
    }

    /**
     * 根据业务规则确定最优队列
     */
    private String determineOptimalQueue(DistributionMessage message) {
        // 重试次数检查
        if (message.getRetryCount() != null && message.getRetryCount() > 0) {
            if (message.getRetryCount() >= maxRetryCount) {
                log.debug("达到最大重试次数，进入死信队列, handler: {}", currentHandlerType);
                return "DEAD_LETTER";
            }
            log.debug("消息需要重试，进入重试队列, handler: {}", currentHandlerType);
            return "RETRY";
        }

        // 业务类型判断
        if (message.getBusinessType() != null) {
            switch (message.getBusinessType().toUpperCase()) {
                case "PRE_ORDER":
                case "RESERVATION":
                    log.debug("预订单业务，使用中延时队列, handler: {}", currentHandlerType);
                    return "DELAY_MEDIUM";
                case "REFUND":
                case "CANCEL":
                    log.debug("退款取消业务，使用主队列, handler: {}", currentHandlerType);
                    return "MAIN";
                case "SETTLEMENT":
                    log.debug("结算业务，使用长延时队列, handler: {}", currentHandlerType);
                    return "DELAY_LONG";
                case "NOTIFICATION":
                    log.debug("通知业务，使用短延时队列, handler: {}", currentHandlerType);
                    return "DELAY_SHORT";
            }
        }

        // 优先级判断
        if (message.getPriority() != null) {
            switch (message.getPriority().toUpperCase()) {
                case "HIGH":
                    log.debug("高优先级消息，使用主队列, handler: {}", currentHandlerType);
                    return "MAIN";
                case "MEDIUM":
                    log.debug("中优先级消息，使用短延时队列, handler: {}", currentHandlerType);
                    return "DELAY_SHORT";
                case "LOW":
                    log.debug("低优先级消息，使用中延时队列, handler: {}", currentHandlerType);
                    return "DELAY_MEDIUM";
            }
        }

        // 时间判断
        if (isNonWorkingHours()) {
            log.debug("非工作时间，使用中延时队列, handler: {}", currentHandlerType);
            return "DELAY_MEDIUM";
        }

        log.debug("默认使用主队列, handler: {}", currentHandlerType);
        return "MAIN";
    }

    /**
     * 根据队列类型发送消息
     */
    private boolean sendToQueueByType(DistributionMessage message, String queueType) {
        log.debug("根据队列类型发送消息: {}, handler: {}", queueType, currentHandlerType);
        switch (queueType) {
            case "MAIN": return sendToMainQueue(message);
            case "DELAY_SHORT": return sendToDelayQueue(message, 30000);
            case "DELAY_MEDIUM": return sendToDelayQueue(message, 300000);
            case "DELAY_LONG": return sendToDelayQueue(message, 1800000);
            case "RETRY": return sendToRetryQueue(message);
            case "DEAD_LETTER": return sendToDeadLetterQueue(message);
            default:
                log.warn("未知队列类型，使用主队列: {}, handler: {}", queueType, currentHandlerType);
                return sendToMainQueue(message);
        }
    }

    /**
     * 检查队列状态
     */
    private String checkQueueStatus(String queueName) {
        try {
            Message message = rabbitTemplate.receive(queueName, 100);
            if (message != null) {
                rabbitTemplate.send(exchange, getRoutingKeyByQueue(queueName), message);
                log.debug("队列 {} 有消息, handler: {}", queueName, currentHandlerType);
                return "有消息";
            } else {
                log.debug("队列 {} 空闲, handler: {}", queueName, currentHandlerType);
                return "空闲";
            }
        } catch (Exception e) {
            log.error("检查队列状态失败: {}, handler: {}", queueName, currentHandlerType, e);
            return "检查失败";
        }
    }

    /**
     * 估算队列消息数量
     */
    private long estimateQueueMessageCount(String queueName) {
        try {
            int messageCount = 0;
            for (int i = 0; i < 10; i++) {
                Message message = rabbitTemplate.receive(queueName, 10);
                if (message != null) {
                    rabbitTemplate.send(exchange, getRoutingKeyByQueue(queueName), message);
                    messageCount++;
                } else {
                    break;
                }
            }
            long estimatedCount = messageCount == 10 ? 20 : messageCount;
            log.debug("估算队列 {} 消息数量: {}, handler: {}", queueName, estimatedCount, currentHandlerType);
            return estimatedCount;
        } catch (Exception e) {
            log.error("估算队列消息数量失败: {}, handler: {}", queueName, currentHandlerType, e);
            return 0;
        }
    }

    /**
     * 估算总消息数量
     */
    private long estimateTotalMessageCount() {
        long total = estimateQueueMessageCount(queueName) +
            estimateQueueMessageCount(deadLetterQueue) +
            estimateQueueMessageCount(delayQueue) +
            estimateQueueMessageCount(retryQueue);
        log.debug("估算总消息数量: {}, handler: {}", total, currentHandlerType);
        return total;
    }

    /**
     * 根据队列名称获取对应的路由键
     */
    private String getRoutingKeyByQueue(String queueName) {
        if (queueName.equals(this.queueName)) return routingKey;
        if (queueName.equals(delayQueue)) return delayRoutingKey;
        if (queueName.equals(retryQueue)) return retryRoutingKey;
        if (queueName.equals(deadLetterQueue)) return deadLetterRoutingKey;
        log.debug("队列 {} 使用默认路由键, handler: {}", queueName, currentHandlerType);
        return routingKey;
    }

    /**
     * 判断是否是非工作时间
     */
    private boolean isNonWorkingHours() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        boolean isNonWorking = today.getDayOfWeek().getValue() >= 6 ||
            now.isAfter(LocalTime.of(18, 0)) ||
            now.isBefore(LocalTime.of(9, 0));
        log.debug("时间判断: 当前时间={}, 是否非工作时间={}, handler: {}", now, isNonWorking, currentHandlerType);
        return isNonWorking;
    }

    /**
     * 生成消息ID
     */
    private String generateMessageId() {
        String messageId = "MSG_" + System.currentTimeMillis() + "_" +
            UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.debug("生成消息ID: {}, handler: {}", messageId, currentHandlerType);
        return messageId;
    }

    /**
     * 检查消息是否已处理（基于Redis的幂等性校验）
     * @param messageId 消息ID
     * @return 是否已处理
     */
    private boolean isMessageProcessed(String messageId) {
        if (messageId == null) return false;

        String key = "mq:processed:" + messageId;
        try {
            // 检查消息是否已处理（7天过期）
            // 使用现有的 hasKey 方法
            Boolean processed = RedisUtil.hasKey(key);
            if (Boolean.TRUE.equals(processed)) {
                log.info("消息已处理过，跳过: {}", messageId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("检查消息处理状态异常: {}", messageId, e);
            return false;
        }
    }

    /**
     * 标记消息为已处理
     * @param messageId 消息ID
     */
    private void markMessageAsProcessed(String messageId) {
        if (messageId == null) return;

        String key = "mq:processed:" + messageId;
        try {
            // 标记为已处理，7天过期
            RedisUtil.setCacheObject(key, "1",  Duration.ofDays(7));
        } catch (Exception e) {
            log.warn("标记消息为已处理异常: {}", messageId, e);
        }
    }

    /**
     * 消息处理器接口（函数式接口）
     */
    @FunctionalInterface
    private interface MessageProcessor {
        String process();
    }
}
