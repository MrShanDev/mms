# MMS-MQ 使用教程

## 1. 概述

MMS-MQ 是一个基于 RabbitMQ 的消息队列系统，支持多种消息处理器的动态切换。本教程将详细介绍如何使用该系统进行消息的注册、发送和消费。

## 2. 消息处理器注册

### 2.1 创建自定义消息处理器

首先，你需要创建一个实现了 MqHandler 接口的类：

```java
@Service
public class CustomMqHandler implements MqHandler {
    
    @Override
    public boolean handleMessage(DistributionMessage message) {
        // 实现你的业务逻辑
        System.out.println("处理订单消息: " + message.getOrderId());
        return true; // 处理成功返回true，失败返回false
    }
    
    @Override
    public boolean isMessageProcessed(String orderId) {
        // 检查消息是否已经被处理过，用于幂等性控制
        return false; // 如果已处理返回true，未处理返回false
    }
    
    @Override
    public String getHandlerType() {
        // 返回处理器的唯一标识
        return "custom_handler";
    }
}
```

### 2.2 注册处理器

在系统启动时，你需要将你的处理器注册到系统中。这通常是通过配置类完成的：

```java
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MQRegister {
    
    private final MqService mqService;
    private final CustomMqHandler customMqHandler;
    
    @PostConstruct
    public void registerHandlers() {
        // 注册你的自定义处理器
        mqService.registerHandler(customMqHandler);
        
        // 设置当前使用的处理器
        if (mqService.setCurrentHandler("custom_handler")) {
            log.info("✅ 当前处理器设置为: custom_handler");
        }
    }
}
```

## 3. 发送消息

### 3.1 注入 MqService

在你的业务类中注入 MqService：

```java
@Service
public class OrderService {
    
    @Autowired
    private MqService mqService;
    
    // 你的业务方法
}
```

### 3.2 构造并发送消息

```java
public void createOrder(Order order) {
    // 构造消息对象
    DistributionMessage message = new DistributionMessage();
    message.setOrderId(order.getId());
    message.setBuyerId(order.getBuyerId());
    message.setBusinessType("ORDER");
    
    // 发送消息到主队列
    boolean success = mqService.sendToMainQueue(message);
    
    if (success) {
        log.info("订单消息发送成功: {}", order.getId());
    } else {
        log.error("订单消息发送失败: {}", order.getId());
    }
}
```

## 4. 消费消息与处理器匹配

### 4.1 消费流程

当消息被发送到队列后，系统会按照以下流程进行消费：

1. @RabbitListener 监听器接收到消息
2. 调用 onMessage 方法处理消息
3. 根据当前设置的处理器类型获取对应的处理器
4. 使用该处理器处理消息

### 4.2 处理器匹配机制

处理器的匹配不是基于消息内容，而是基于系统当前的配置：

```java
// 在 onMessage 方法中
MqHandler handler = getCurrentMessageHandler(); // 根据 currentHandlerType 获取处理器
boolean success = handler.handleMessage(distributionMessage); // 处理消息
```

currentHandlerType 的值决定了使用哪个处理器：
- 如果设置为 "custom_handler"，则使用你自定义的处理器
- 如果设置为 "distribution"，则使用分销处理器
- 如果设置为 "default"，则使用默认处理器

### 4.3 动态切换处理器

你可以在运行时动态切换处理器：

```java
// 切换到自定义处理器
mqService.setCurrentHandler("custom_handler");

// 切换到分销处理器
mqService.setCurrentHandler("distribution");

// 切换到默认处理器
mqService.setCurrentHandler("default");
```

## 5. 完整示例

### 5.1 自定义处理器

```java
@Service
public class OrderMqHandler implements MqHandler {
    
    @Override
    public boolean handleMessage(DistributionMessage message) {
        System.out.println("处理订单: " + message.getOrderId());
        // 实现订单处理逻辑
        return true;
    }
    
    @Override
    public boolean isMessageProcessed(String orderId) {
        // 检查订单是否已处理
        return false;
    }
    
    @Override
    public String getHandlerType() {
        return "order_handler";
    }
}
```

### 5.2 注册配置

```java
@Configuration
@RequiredArgsConstructor
public class OrderMQRegister {
    
    private final MqService mqService;
    private final OrderMqHandler orderMqHandler;
    
    @PostConstruct
    public void registerHandlers() {
        mqService.registerHandler(orderMqHandler);
        mqService.setCurrentHandler("order_handler");
    }
}
```

### 5.3 消息发送

```java
@Service
public class OrderService {
    
    @Autowired
    private MqService mqService;
    
    public void createOrder(Order order) {
        DistributionMessage message = new DistributionMessage();
        message.setOrderId(order.getId());
        message.setBuyerId(order.getBuyerId());
        
        mqService.sendToMainQueue(message);
    }
}
```

这样，当订单创建时，消息会被发送到队列，然后由你注册的 OrderMqHandler 处理器进行处理。