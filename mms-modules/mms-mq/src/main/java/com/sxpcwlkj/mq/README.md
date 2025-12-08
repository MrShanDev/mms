
1. yaml 中配置加载示例

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
app:
  mq:
    queue: distribution.queue
    exchange: distribution.exchange
    dead-letter-queue: distribution.dlx.queue
```

2. MqServiceImpl 初始化

```java
    // 构造函数中初始化基础组件
    public MqServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        log.info("✅ MQ服务初始化完成");
    }
```
3. 处理器注册（RabbitMQRegisterHandlerConfig）

```java
@PostConstruct
public void registerHandlers() {
    // 1. 注册默认处理器
    registerDefaultHandler();
    
    // 2. 注册分销处理器
    mqService.registerHandler(distributionMessageHandler);
    
    // 3. 设置当前处理器
    mqService.setCurrentHandler("distribution");
}
```

4. 📤 消息发布流程
   业务层发送消息
```java
@Service
public class OrderService {
    
    public void createOrder(Order order) {
        // 构建消息
        DistributionMessage message = new DistributionMessage();
        message.setOrderId(order.getId());
        message.setBuyerId(order.getBuyerId());
        message.setBusinessType("ORDER");
        
        // 发送消息
        boolean success = mqService.sendToMainQueue(message);
        
        if (success) {
            log.info("订单消息发送成功: {}", order.getId());
        }
    }
}
```
消息发送内部流程

```java
@Override
public boolean sendToMainQueue(DistributionMessage message) {
    try {
        // 1. 生成消息ID
        ensureMessageId(message);
        
        // 2. 发送到 RabbitMQ
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        
        // 3. 记录日志
        log.info("📤 消息发送到主队列成功, orderId: {}", message.getOrderId());
        return true;
    } catch (Exception e) {
        log.error("❌ 发送消息失败", e);
        return false;
    }
}
```

5. 📥 消息消费流程

自动消费监听器
```java
@RabbitListener(queues = "${spring.rabbitmq.config.queue:distribution.queue}")
@Transactional
public void onMessage(Message message, Channel channel, long deliveryTag) {
    try {
        // 1. 解析消息
        DistributionMessage distributionMessage = parseMessage(message);
        
        // 2. 获取当前处理器
        MessageHandler handler = getCurrentMessageHandler();
        
        // 3. 检查是否已处理（幂等性）
        if (handler.isMessageProcessed(orderId)) {
            channel.basicAck(deliveryTag, false); // 确认消费
            return;
        }
        
        // 4. 处理消息
        boolean success = handler.handleMessage(distributionMessage);
        
        if (success) {
            // 5. 处理成功，确认消费
            channel.basicAck(deliveryTag, false);
            log.info("✅ 消费成功");
        } else {
            // 6. 处理失败，拒绝消息（进入死信队列）
            channel.basicNack(deliveryTag, false, false);
            log.error("❌ 消费失败");
        }
    } catch (Exception e) {
        // 7. 异常情况，拒绝消息
        channel.basicNack(deliveryTag, false, false);
    }
}
```

6. 🔄 失败处理和重试机制
   处理失败时的流程
```java
@Override
public boolean handleMessage(DistributionMessage message) {
    try {
        // 业务处理逻辑
        boolean result = distributionService.calculateCommission(message);
        
        if (!result) {
            // 处理失败，检查重试次数
            if (message.getRetryCount() < maxRetryCount) {
                // 发送到重试队列
                mqService.sendToRetryQueue(message);
            } else {
                // 达到最大重试次数，发送到死信队列
                mqService.sendToDeadLetterQueue(message);
            }
            return false;
        }
        return true;
    } catch (Exception e) {
        log.error("处理消息异常", e);
        return false;
    }
}
```

重试队列处理
```java
// 重试队列的消息会延迟一段时间后重新进入主队列
public boolean sendToRetryQueue(DistributionMessage message) {
    // 递增重试次数
    incrementRetryCount(message);
    
    // 发送到重试队列（可能有延迟）
    rabbitTemplate.convertAndSend(exchange, retryRoutingKey, message);
}
```

7. ⚰️ 死信队列处理

```java
@RabbitListener(queues = "${spring.rabbitmq.config.dead-letter-queue:distribution.dlx.queue}")
public void onDeadLetterMessage(Message message, Channel channel, long deliveryTag) {
    try {
        // 1. 解析死信消息
        DistributionMessage distributionMessage = parseMessage(message);
        
        // 2. 发送告警通知
        sendAlertNotification(distributionMessage.getOrderId(), distributionMessage);
        
        // 3. 确认消费（死信消息不再重试）
        channel.basicAck(deliveryTag, false);
        
        log.warn("💀 死信消息已处理: {}", distributionMessage.getOrderId());
    } catch (Exception e) {
        log.error("处理死信消息异常", e);
    }
}
```

告警通知

```java
private void sendAlertNotification(String orderId, DistributionMessage message) {
    String alertContent = String.format(
        "🚨 分销计算失败告警\n订单ID: %s\n重试次数: %s\n时间: %s",
        orderId, message.getRetryCount(), new Date()
    );
    // 发送到企业微信等通知渠道
    WeChatBotMessageUtil.sendListMessage(alertContent);
}
```