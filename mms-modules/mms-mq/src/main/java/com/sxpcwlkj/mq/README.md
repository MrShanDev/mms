# MMS-MQ 使用教程

## 📚 目录

1. [概述](#1-概述)
2. [核心概念](#2-核心概念)
3. [快速开始](#3-快速开始)
4. [新业务对接完整流程](#4-新业务对接完整流程)
5. [订单超时延时取消（完整示例）](#5-订单超时延时取消完整示例)
6. [常见问题](#6-常见问题)

---

## 1. 概述

MMS-MQ 是一个基于 RabbitMQ 的消息队列工具模块，采用 **处理器模式** 实现业务解耦：

- ✅ **mms-mq**：工具模块，负责消息队列管理（监听、发送、重试、死信）
- ✅ **业务模块**：实现处理器接口，负责具体业务逻辑
- ✅ **完全解耦**：工具模块不操作数据库、Redis，业务模块自主控制

### 核心特性

- 🔄 支持延时消息、重试队列、死信队列
- 🎯 处理器模式，业务模块自定义逻辑
- 🛡️ 消息幂等性、失败重试、告警通知
- 📊 支持多种业务场景（分销、订单超时、支付回调等）

---

## 2. 核心概念

### 2.1 架构图

```
┌─────────────────────────────────────────────────┐
│              mms-mq (工具模块)                    │
│  ✅ RabbitMQ 队列管理                            │
│  ✅ 消息监听和分发                                │
│  ✅ 处理器接口定义                                │
│  ❌ 不操作 Redis / 数据库                        │
└─────────────────────────────────────────────────┘
                    ↓ 调用处理器
┌─────────────────────────────────────────────────┐
│           业务模块 (mms-demo/其他)                │
│  ✅ 实现处理器接口                                │
│  ✅ 操作 Redis / 数据库                          │
│  ✅ 实现具体业务逻辑                             │
└─────────────────────────────────────────────────┘
```

### 2.2 消息流转流程

```
业务模块发送消息
   ↓
RabbitMQ 队列 (mms-mq 监听)
   ↓
mms-mq 调用业务处理器
   ↓
业务模块处理消息
   ↓
返回处理结果
   ↓
mms-mq 确认/重试/死信
```

---

## 3. 快速开始

### 3.1 配置 RabbitMQ

在 `application.yml` 中配置：

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

### 3.2 使用现有功能

#### 发送分销消息

```java
@Autowired
private MqService mqService;

public void sendMessage() {
    DistributionMessage message = new DistributionMessage();
    message.setOrderId("ORDER_001");
    message.setBuyerId("USER_001");
    mqService.sendToMainQueue(message);
}
```

#### 发送订单超时消息

```java
@Autowired
private OrderTimeoutService orderTimeoutService;

public void createOrder(String orderId) {
    // 30分钟后自动取消
    orderTimeoutService.sendQuickOrderTimeout(orderId, "PENDING", 30 * 60 * 1000L);
}
```

---

## 4. 新业务对接完整流程

### 📋 对接步骤总览

```
步骤1: 在 mms-mq 中定义消息实体
  ↓
步骤2: 在 mms-mq 中定义处理器接口
  ↓
步骤3: 在 mms-mq 中创建服务接口
  ↓
步骤4: 在 mms-mq 中实现服务（监听器）
  ↓
步骤5: 在 mms-mq 中配置队列
  ↓
步骤6: 在业务模块实现处理器
  ↓
步骤7: 在业务模块注册处理器
  ↓
步骤8: 测试和使用
```

---

### 🎯 详细步骤（以支付回调为例）

#### **步骤1: 在 mms-mq 中定义消息实体**

创建 `PaymentCallbackMessage.java`：

```java
// 文件位置: mms-modules/mms-mq/src/main/java/com/sxpcwlkj/mq/entity/PaymentCallbackMessage.java
package com.sxpcwlkj.mq.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PaymentCallbackMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 基本信息
    private String orderId;          // 订单ID
    private String paymentId;        // 支付ID
    private String paymentMethod;    // 支付方式：WECHAT, ALIPAY
    private BigDecimal amount;       // 支付金额
    private String status;           // 支付状态：SUCCESS, FAILED
    
    // 消息控制
    private String messageId;        // 消息ID
    private Integer retryCount;      // 重试次数
    private Long timestamp;          // 时间戳
    
    // 扩展字段
    private String tenantId;         // 租户ID
    private String buyerId;          // 购买者ID
    
    /**
     * 验证消息
     */
    public boolean validate() {
        return orderId != null && paymentId != null && status != null;
    }
    
    /**
     * 增加重试次数
     */
    public void incrementRetryCount() {
        if (retryCount == null) retryCount = 0;
        retryCount++;
    }
}
```

---

#### **步骤2: 在 mms-mq 中定义处理器接口**

创建 `PaymentCallbackHandler.java`：

```java
// 文件位置: mms-modules/mms-mq/src/main/java/com/sxpcwlkj/mq/handler/PaymentCallbackHandler.java
package com.sxpcwlkj.mq.handler;

import com.sxpcwlkj.mq.entity.PaymentCallbackMessage;

/**
 * 支付回调处理器接口
 * 业务模块实现此接口来处理支付回调逻辑
 */
public interface PaymentCallbackHandler {

    /**
     * 处理支付回调
     * @param message 支付回调消息
     * @return 处理结果，true-成功，false-失败
     */
    boolean handlePaymentCallback(PaymentCallbackMessage message);

    /**
     * 检查消息是否已处理过（幂等性控制）
     * @param messageId 消息ID
     * @return 是否已处理
     */
    boolean isMessageProcessed(String messageId);

    /**
     * 标记消息为已处理
     * @param messageId 消息ID
     */
    void markMessageAsProcessed(String messageId);

    /**
     * 获取处理器名称
     * @return 处理器名称
     */
    default String getHandlerName() {
        return "default";
    }
}
```

---

#### **步骤3: 在 mms-mq 中创建服务接口**

创建 `PaymentCallbackService.java`：

```java
// 文件位置: mms-modules/mms-mq/src/main/java/com/sxpcwlkj/mq/service/PaymentCallbackService.java
package com.sxpcwlkj.mq.service;

import com.sxpcwlkj.mq.entity.PaymentCallbackMessage;
import com.sxpcwlkj.mq.handler.PaymentCallbackHandler;

public interface PaymentCallbackService {

    /**
     * 注册支付回调处理器
     * @param handler 处理器实例
     */
    void registerHandler(PaymentCallbackHandler handler);

    /**
     * 设置当前处理器
     * @param handlerName 处理器名称
     * @return 是否成功
     */
    boolean setCurrentHandler(String handlerName);

    /**
     * 发送支付回调消息
     * @param message 支付回调消息
     * @return 是否成功
     */
    boolean sendPaymentCallback(PaymentCallbackMessage message);

    /**
     * 获取队列状态
     * @return 状态描述
     */
    String getQueueStatus();
}
```

---

#### **步骤4: 在 mms-mq 中实现服务（监听器）**

创建 `PaymentCallbackServiceImpl.java`：

```java
// 文件位置: mms-modules/mms-mq/src/main/java/com/sxpcwlkj/mq/service/impl/PaymentCallbackServiceImpl.java
package com.sxpcwlkj.mq.service.impl;

import com.rabbitmq.client.Channel;
import com.sxpcwlkj.mq.entity.PaymentCallbackMessage;
import com.sxpcwlkj.mq.handler.PaymentCallbackHandler;
import com.sxpcwlkj.mq.service.PaymentCallbackService;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentCallbackServiceImpl implements PaymentCallbackService {

    private final RabbitTemplate rabbitTemplate;
    private final Map<String, PaymentCallbackHandler> handlers = new ConcurrentHashMap<>();
    private String currentHandlerName = "default";

    @Value("${spring.rabbitmq.payment.exchange:payment.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.payment.queue:payment.queue}")
    private String queue;

    @Value("${spring.rabbitmq.payment.routing-key:payment.key}")
    private String routingKey;

    @Value("${spring.rabbitmq.payment.max-retry-count:3}")
    private int maxRetryCount;

    // ==================== 处理器管理 ====================

    @Override
    public void registerHandler(PaymentCallbackHandler handler) {
        if (handler != null) {
            handlers.put(handler.getHandlerName(), handler);
            log.info("✅ 注册支付回调处理器: {}", handler.getHandlerName());
        }
    }

    @Override
    public boolean setCurrentHandler(String handlerName) {
        if (handlers.containsKey(handlerName)) {
            this.currentHandlerName = handlerName;
            log.info("✅ 切换支付回调处理器为: {}", handlerName);
            return true;
        }
        log.warn("❌ 支付回调处理器不存在: {}", handlerName);
        return false;
    }

    private PaymentCallbackHandler getCurrentHandler() {
        return handlers.getOrDefault(currentHandlerName, handlers.get("default"));
    }

    // ==================== 消息发送 ====================

    @Override
    public boolean sendPaymentCallback(PaymentCallbackMessage message) {
        try {
            if (!message.validate()) {
                log.error("❌ 支付回调消息验证失败: {}", message);
                return false;
            }
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("📤 支付回调消息发送成功 - orderId: {}, paymentId: {}",
                message.getOrderId(), message.getPaymentId());
            return true;
        } catch (Exception e) {
            log.error("❌ 发送支付回调消息失败", e);
            return false;
        }
    }

    // ==================== 消息监听 ====================

    @RabbitListener(queues = "${spring.rabbitmq.payment.queue:payment.queue}")
    public void onPaymentCallback(Message message, Channel channel,
                                 @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        String orderId = null;
        String messageId = null;
        try {
            log.info("🔔 收到支付回调消息 - deliveryTag: {}", deliveryTag);

            // 解析消息
            PaymentCallbackMessage callbackMessage = parseMessage(message);
            if (callbackMessage == null) {
                log.error("❌ 消息解析失败");
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            orderId = callbackMessage.getOrderId();
            messageId = callbackMessage.getMessageId();

            // 获取处理器
            PaymentCallbackHandler handler = getCurrentHandler();
            if (handler == null) {
                log.error("❌ 未找到支付回调处理器");
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            // 幂等性检查
            if (handler.isMessageProcessed(messageId)) {
                log.info("消息已处理过，跳过 - orderId: {}, messageId: {}", orderId, messageId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 调用业务处理器
            long startTime = System.currentTimeMillis();
            boolean success = handler.handlePaymentCallback(callbackMessage);
            long processTime = System.currentTimeMillis() - startTime;

            if (success) {
                // 处理成功
                handler.markMessageAsProcessed(messageId);
                channel.basicAck(deliveryTag, false);
                log.info("✅ 支付回调处理成功 - orderId: {}, 处理时间: {}ms",
                    orderId, processTime);
            } else {
                // 处理失败，重试
                handleFailedMessage(callbackMessage, channel, deliveryTag);
            }
        } catch (Exception e) {
            log.error("💥 处理支付回调异常 - orderId: {}, messageId: {}",
                orderId, messageId, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    private void handleFailedMessage(PaymentCallbackMessage message,
                                    Channel channel, long deliveryTag) throws IOException {
        message.incrementRetryCount();
        if (message.getRetryCount() >= maxRetryCount) {
            log.warn("消息重试次数已达上限，发送到死信队列 - orderId: {}", message.getOrderId());
            channel.basicNack(deliveryTag, false, false);
        } else {
            log.warn("消息处理失败，重新入队 - orderId: {}, 重试次数: {}",
                message.getOrderId(), message.getRetryCount());
            channel.basicNack(deliveryTag, false, true);
        }
    }

    private PaymentCallbackMessage parseMessage(Message message) {
        try {
            Object payload = rabbitTemplate.getMessageConverter().fromMessage(message);
            return payload instanceof PaymentCallbackMessage ?
                (PaymentCallbackMessage) payload : null;
        } catch (Exception e) {
            log.error("解析支付回调消息失败", e);
            return null;
        }
    }

    @Override
    public String getQueueStatus() {
        try {
            Message message = rabbitTemplate.receive(queue, 100);
            if (message != null) {
                rabbitTemplate.send(exchange, routingKey, message);
                return "🔴 支付回调队列中有消息";
            }
            return "🟢 支付回调队列为空";
        } catch (Exception e) {
            return "❌ 检查失败: " + e.getMessage();
        }
    }
}
```

---

#### **步骤5: 在 mms-mq 中配置队列**

创建 `PaymentQueueConfig.java`：

```java
// 文件位置: mms-modules/mms-mq/src/main/java/com/sxpcwlkj/mq/config/PaymentQueueConfig.java
package com.sxpcwlkj.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class PaymentQueueConfig {

    @Value("${spring.rabbitmq.payment.exchange:payment.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.payment.queue:payment.queue}")
    private String queue;

    @Value("${spring.rabbitmq.payment.routing-key:payment.key}")
    private String routingKey;

    @Value("${spring.rabbitmq.payment.dead-letter-exchange:payment.dlx.exchange}")
    private String deadLetterExchange;

    @Value("${spring.rabbitmq.payment.dead-letter-queue:payment.dlx.queue}")
    private String deadLetterQueue;

    @Value("${spring.rabbitmq.payment.dead-letter-routing-key:payment.dlx.key}")
    private String deadLetterRoutingKey;

    /**
     * 支付交换机
     */
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(exchange, true, false);
    }

    /**
     * 支付队列
     */
    @Bean
    public Queue paymentQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", deadLetterExchange);
        args.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        return new Queue(queue, true, false, false, args);
    }

    /**
     * 支付死信交换机
     */
    @Bean
    public DirectExchange paymentDeadLetterExchange() {
        return new DirectExchange(deadLetterExchange, true, false);
    }

    /**
     * 支付死信队列
     */
    @Bean
    public Queue paymentDeadLetterQueue() {
        return new Queue(deadLetterQueue, true);
    }

    /**
     * 绑定支付队列
     */
    @Bean
    public Binding paymentBinding() {
        return BindingBuilder.bind(paymentQueue())
            .to(paymentExchange())
            .with(routingKey);
    }

    /**
     * 绑定死信队列
     */
    @Bean
    public Binding paymentDeadLetterBinding() {
        return BindingBuilder.bind(paymentDeadLetterQueue())
            .to(paymentDeadLetterExchange())
            .with(deadLetterRoutingKey);
    }

    @Bean
    public String initPaymentQueue() {
        log.info("✅ 支付回调队列配置初始化完成");
        log.info("📊 支付回调队列信息:");
        log.info("   - 支付队列: {}", queue);
        log.info("   - 支付交换机: {}", exchange);
        log.info("   - 路由键: {}", routingKey);
        return "支付回调队列初始化成功";
    }
}
```

---

#### **步骤6: 在业务模块实现处理器**

在 mms-demo 中创建 `DemoPaymentCallbackHandler.java`：

```java
// 文件位置: mms-modules/mms-demo/src/main/java/com/sxpcwlkj/demo/mq/handler/DemoPaymentCallbackHandler.java
package com.sxpcwlkj.demo.mq.handler;

import com.sxpcwlkj.mq.entity.PaymentCallbackMessage;
import com.sxpcwlkj.mq.handler.PaymentCallbackHandler;
import com.sxpcwlkj.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Demo模块的支付回调处理器
 * 实现具体的支付回调业务逻辑
 */
@Component
@Slf4j
public class DemoPaymentCallbackHandler implements PaymentCallbackHandler {

    @Override
    public boolean handlePaymentCallback(PaymentCallbackMessage message) {
        try {
            String orderId = message.getOrderId();
            String status = message.getStatus();
            
            log.info("🎯 Demo处理器开始处理支付回调 - orderId: {}, status: {}", 
                orderId, status);
            
            // ==================== 实现你的业务逻辑 ====================
            
            if ("SUCCESS".equals(status)) {
                // 1. 更新订单状态为已支付
                // orderService.updateOrderStatus(orderId, "PAID");
                log.info("  步骤1: 更新订单状态为已支付 - orderId: {}", orderId);
                
                // 2. 记录支付流水
                // paymentService.savePaymentRecord(message);
                log.info("  步骤2: 记录支付流水 - paymentId: {}", message.getPaymentId());
                
                // 3. 发送支付成功通知
                // notificationService.sendPaymentSuccessNotice(orderId);
                log.info("  步骤3: 发送支付成功通知");
                
                // 4. 触发发货流程
                // deliveryService.createDeliveryTask(orderId);
                log.info("  步骤4: 触发发货流程");
                
            } else if ("FAILED".equals(status)) {
                // 支付失败处理
                log.info("  支付失败，释放订单 - orderId: {}", orderId);
                // orderService.cancelOrder(orderId, "支付失败");
            }
            
            log.info("✅ Demo处理器完成支付回调处理 - orderId: {}", orderId);
            return true;
            
        } catch (Exception e) {
            log.error("❌ Demo处理器处理支付回调失败 - orderId: {}", 
                message.getOrderId(), e);
            return false;
        }
    }

    @Override
    public boolean isMessageProcessed(String messageId) {
        try {
            String key = "demo:payment:processed:" + messageId;
            return Boolean.TRUE.equals(RedisUtil.hasKey(key));
        } catch (Exception e) {
            log.warn("检查消息处理状态异常 - messageId: {}", messageId, e);
            return false;
        }
    }

    @Override
    public void markMessageAsProcessed(String messageId) {
        try {
            String key = "demo:payment:processed:" + messageId;
            RedisUtil.setCacheObject(key, "1", Duration.ofDays(7));
            log.debug("标记消息为已处理 - messageId: {}", messageId);
        } catch (Exception e) {
            log.warn("标记消息处理状态异常 - messageId: {}", messageId, e);
        }
    }

    @Override
    public String getHandlerName() {
        return "demo-payment";
    }
}
```

---

#### **步骤7: 在业务模块注册处理器**

在 mms-demo 中创建 `PaymentCallbackConfig.java`：

```java
// 文件位置: mms-modules/mms-demo/src/main/java/com/sxpcwlkj/demo/mq/config/PaymentCallbackConfig.java
package com.sxpcwlkj.demo.mq.config;

import com.sxpcwlkj.demo.mq.handler.DemoPaymentCallbackHandler;
import com.sxpcwlkj.mq.service.PaymentCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 支付回调配置类
 * 注册支付回调处理器到 mms-mq 模块
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class PaymentCallbackConfig {

    private final PaymentCallbackService paymentCallbackService;
    private final DemoPaymentCallbackHandler demoPaymentCallbackHandler;

    /**
     * 应用启动完成后注册处理器
     */
    @EventListener(ApplicationReadyEvent.class)
    public void registerPaymentCallbackHandler() {
        // 注册Demo处理器
        paymentCallbackService.registerHandler(demoPaymentCallbackHandler);
        
        // 设置为当前处理器
        boolean success = paymentCallbackService.setCurrentHandler("demo-payment");
        
        if (success) {
            log.info("✅ Demo支付回调处理器注册并激活成功");
        } else {
            log.error("❌ Demo支付回调处理器激活失败");
        }
    }
}
```

---

#### **步骤8: 配置和使用**

在 `application.yml` 中添加配置：

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    
    # 支付回调队列配置
    payment:
      exchange: payment.exchange
      queue: payment.queue
      routing-key: payment.key
      dead-letter-exchange: payment.dlx.exchange
      dead-letter-queue: payment.dlx.queue
      dead-letter-routing-key: payment.dlx.key
      max-retry-count: 3
```

在业务代码中使用：

```java
@Service
public class PaymentService {
    
    @Autowired
    private PaymentCallbackService paymentCallbackService;
    
    /**
     * 接收第三方支付回调
     */
    public void handleThirdPartyCallback(ThirdPartyPaymentNotify notify) {
        // 构建支付回调消息
        PaymentCallbackMessage message = new PaymentCallbackMessage();
        message.setOrderId(notify.getOrderId());
        message.setPaymentId(notify.getPaymentId());
        message.setPaymentMethod(notify.getPaymentMethod());
        message.setAmount(notify.getAmount());
        message.setStatus(notify.getStatus());
        message.setMessageId("PAY_" + System.currentTimeMillis());
        message.setTimestamp(System.currentTimeMillis());
        
        // 发送到MQ进行异步处理
        boolean success = paymentCallbackService.sendPaymentCallback(message);
        
        if (success) {
            log.info("支付回调消息发送成功 - orderId: {}", notify.getOrderId());
        } else {
            log.error("支付回调消息发送失败 - orderId: {}", notify.getOrderId());
        }
    }
}
```

---

## 5. 订单超时延时取消（完整示例）

### 5.1 使用场景

当用户创建订单后，如果在30分钟内未支付，系统自动取消订单并释放库存。

### 5.2 完整代码示例

#### 发送超时消息

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderTimeoutService orderTimeoutService;
    
    public String createOrder(OrderCreateRequest request) {
        // 1. 创建订单
        String orderId = "ORDER_" + System.currentTimeMillis();
        // ... 保存订单到数据库
        
        // 2. 发送超时取消消息（30分钟）
        orderTimeoutService.sendQuickOrderTimeout(
            orderId, 
            "PENDING", 
            30 * 60 * 1000L
        );
        
        return orderId;
    }
    
    public void onPaymentSuccess(String orderId, String messageId) {
        // 支付成功后取消超时任务
        orderTimeoutService.cancelOrderTimeout(messageId);
    }
}
```

#### 实现处理器（mms-demo）

```java
@Component
public class DemoOrderTimeoutHandler implements OrderTimeoutHandler {
    
    @Autowired
    private OrderService orderService;
    
    @Override
    public boolean handleOrderTimeout(OrderTimeoutMessage message) {
        String orderId = message.getOrderId();
        
        // 1. 查询订单状态
        Order order = orderService.getById(orderId);
        if (!"PENDING".equals(order.getStatus())) {
            return true; // 订单已支付，无需取消
        }
        
        // 2. 取消订单
        orderService.cancelOrder(orderId, "订单超时未支付");
        
        // 3. 释放库存
        inventoryService.releaseStock(orderId);
        
        // 4. 退还优惠券
        couponService.returnCoupon(orderId);
        
        return true;
    }
    
    // ... 其他方法实现
}
```

---

## 6. 常见问题

### Q1: 为什么要分离工具模块和业务模块？

**A**: 
- ✅ **职责清晰**：mms-mq 只管消息流转，不包含业务逻辑
- ✅ **完全解耦**：工具模块不操作数据库/Redis，避免业务污染
- ✅ **易于扩展**：新业务只需实现处理器接口，无需修改 mms-mq
- ✅ **独立测试**：业务逻辑可以独立测试，不依赖 MQ

### Q2: 如何实现幂等性？

**A**: 在业务模块的处理器中实现：

```java
@Override
public boolean isMessageProcessed(String messageId) {
    String key = "mq:processed:" + messageId;
    return RedisUtil.hasKey(key);
}

@Override
public void markMessageAsProcessed(String messageId) {
    String key = "mq:processed:" + messageId;
    RedisUtil.setCacheObject(key, "1", Duration.ofDays(7));
}
```

### Q3: 消息处理失败怎么办？

**A**: 系统会自动重试（默认3次），重试失败后进入死信队列：

```yaml
spring:
  rabbitmq:
    your-business:
      max-retry-count: 3  # 最大重试次数
```

### Q4: 如何监控队列状态？

**A**: 调用服务提供的状态查询方法：

```java
String status = paymentCallbackService.getQueueStatus();
log.info("队列状态: {}", status);
```

### Q5: 支持延时消息吗？

**A**: 支持！使用订单超时功能示例：

```java
// 发送延时消息（30分钟后处理）
orderTimeoutService.sendQuickOrderTimeout(orderId, "PENDING", 30 * 60 * 1000L);
```

### Q6: 可以动态切换处理器吗？

**A**: 可以，但通常不需要：

```java
// 切换处理器
paymentCallbackService.setCurrentHandler("another-handler");
```

### Q7: 如何添加告警通知？

**A**: 在业务处理器中实现告警逻辑：

```java
@Override
public boolean handlePaymentCallback(PaymentCallbackMessage message) {
    try {
        // 业务处理...
        return true;
    } catch (Exception e) {
        // 发送告警
        alertService.sendAlert("支付回调处理失败", message.getOrderId());
        return false;
    }
}
```

---

## 📋 快速对接清单

### mms-mq 模块需要添加：

- [ ] ✅ 消息实体类 (`*Message.java`)
- [ ] ✅ 处理器接口 (`*Handler.java`)
- [ ] ✅ 服务接口 (`*Service.java`)
- [ ] ✅ 服务实现 (`*ServiceImpl.java` - 包含监听器)
- [ ] ✅ 队列配置 (`*QueueConfig.java`)

### 业务模块需要添加：

- [ ] ✅ 处理器实现 (`Demo*Handler.java`)
- [ ] ✅ 注册配置 (`*Config.java`)
- [ ] ✅ application.yml 配置

### 配置文件需要添加：

```yaml
spring:
  rabbitmq:
    your-business:  # 替换为你的业务名称
      exchange: your.exchange
      queue: your.queue
      routing-key: your.key
      dead-letter-exchange: your.dlx.exchange
      dead-letter-queue: your.dlx.queue
      dead-letter-routing-key: your.dlx.key
      max-retry-count: 3
```

---

## 🎯 总结

1. **mms-mq** 是纯工具模块，只负责消息队列管理
2. **业务模块** 实现处理器接口，包含具体业务逻辑
3. **完全解耦**，工具模块不操作数据库/Redis
4. **易于扩展**，新业务按照步骤对接即可
5. **自动重试**，失败消息自动进入死信队列
6. **幂等处理**，避免重复消费

如有问题，请参考 mms-demo 模块中的完整示例代码！
```

---