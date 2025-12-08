package com.sxpcwlkj.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 配置类
 * 作用：配置RabbitMQ的连接、交换机、队列、消息转换器等
 * 版本：JDK 17 + Spring Boot 3.2.6
 */
@Configuration
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    // 从配置文件中读取MQ相关配置，如果没有配置则使用默认值
    @Value("${spring.rabbitmq.config.exchange:distribution.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.config.queue:distribution.queue}")
    private String queue;

    @Value("${spring.rabbitmq.config.routing-key:distribution.key}")
    private String routingKey;

    @Value("${spring.rabbitmq.config.dead-letter-exchange:distribution.dlx.exchange}")
    private String deadLetterExchange;

    @Value("${spring.rabbitmq.config.dead-letter-queue:distribution.dlx.queue}")
    private String deadLetterQueue;

    @Value("${spring.rabbitmq.config.dead-letter-routing-key:distribution.dlx.key}")
    private String deadLetterRoutingKey;

    // 新增延时队列配置
    @Value("${spring.rabbitmq.config.delay-queue:distribution.delay.queue}")
    private String delayQueue;

    @Value("${spring.rabbitmq.config.delay-exchange:distribution.delay.exchange}")
    private String delayExchange;

    @Value("${spring.rabbitmq.config.delay-routing-key:distribution.delay.key}")
    private String delayRoutingKey;

    @Value("${spring.rabbitmq.config.delay-time:60000}")
    private int delayTime;

    // 重试队列配置
    @Value("${spring.rabbitmq.config.retry-queue:distribution.retry.queue}")
    private String retryQueue;

    @Value("${spring.rabbitmq.config.retry-routing-key:distribution.retry.key}")
    private String retryRoutingKey;

    @Value("${spring.rabbitmq.config.retry-interval:5000}")
    private int retryInterval;

    @Value("${spring.rabbitmq.config.max-retry-count:3}")
    private int maxRetryCount;

    /**
     * 主交换机 - 直连交换机
     * 作用：负责接收生产者发送的消息，并根据路由键将消息路由到对应的队列
     */
    @Bean
    public DirectExchange distributionExchange() {
        // 参数说明：交换机名称, 是否持久化, 是否自动删除
        return new DirectExchange(exchange, true, false);
    }

    /**
     * 死信交换机
     * 作用：处理正常队列中无法消费的消息（死信）
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchange, true, false);
    }

    /**
     * 延时交换机
     * 作用：用于处理需要延迟发送的消息
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(delayExchange, true, false);
    }

    /**
     * 主队列 - 绑定死信交换机
     * 作用：存储待处理的分销消息，如果消息处理失败会转到死信队列
     */
    @Bean
    public Queue distributionQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机：当消息成为死信时，会发送到这个交换机
        args.put("x-dead-letter-exchange", deadLetterExchange);
        // 设置死信路由键：死信消息的路由键
        args.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        // 设置消息过期时间：10分钟（600000毫秒）
        args.put("x-message-ttl", 600000);

        // 参数说明：队列名称, 是否持久化, 是否排他, 是否自动删除, 其他参数
        return new Queue(queue, true, false, false, args);
    }

    /**
     * 死信队列
     * 作用：存储处理失败的消息，用于人工干预或后续处理
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(deadLetterQueue, true);
    }

    /**
     * 延时队列
     * 作用：通过死信机制实现消息延迟处理
     * 原理：消息在延时队列中等待指定时间后，会自动转发到主队列
     */
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机：消息过期后转发到主交换机
        args.put("x-dead-letter-exchange", exchange);
        // 设置死信路由键：消息过期后使用主队列的路由键
        args.put("x-dead-letter-routing-key", routingKey);
        // 设置队列级别的消息过期时间（单位：毫秒）
        args.put("x-message-ttl", delayTime);

        return new Queue(delayQueue, true, false, false, args);
    }

    /**
     * 重试队列
     * 作用：处理需要重试的消息，设置较短的重试间隔
     */
    @Bean
    public Queue retryQueue() {
        Map<String, Object> args = new HashMap<>();
        // 重试队列的死信也是主交换机
        args.put("x-dead-letter-exchange", exchange);
        args.put("x-dead-letter-routing-key", routingKey);
        // 设置重试间隔时间
        args.put("x-message-ttl", retryInterval);

        return new Queue(retryQueue, true, false, false, args);
    }

    /**
     * 绑定主队列到主交换机
     * 作用：将队列和交换机绑定，并指定路由键
     */
    @Bean
    public Binding distributionBinding() {
        return BindingBuilder.bind(distributionQueue())
            .to(distributionExchange())
            .with(routingKey);
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with(deadLetterRoutingKey);
    }

    /**
     * 绑定延时队列到延时交换机
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue())
            .to(delayExchange())
            .with(delayRoutingKey);
    }

    /**
     * 绑定重试队列到主交换机
     * 注意：重试队列也绑定到主交换机，使用不同的路由键
     */
    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(retryQueue())
            .to(distributionExchange())
            .with(retryRoutingKey);
    }

    /**
     * 创建自定义延时队列的通用方法
     * 使用场景：需要不同延时时间的业务场景
     */
    public Queue createCustomDelayQueue(String queueName, int delayTimeMs) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", exchange);
        args.put("x-dead-letter-routing-key", routingKey);
        args.put("x-message-ttl", delayTimeMs);

        return new Queue(queueName, true, false, false, args);
    }

    /**
     * 创建自定义重试队列的通用方法
     */
    public Queue createCustomRetryQueue(String queueName, int retryTimeMs) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", exchange);
        args.put("x-dead-letter-routing-key", routingKey);
        args.put("x-message-ttl", retryTimeMs);

        return new Queue(queueName, true, false, false, args);
    }

    /**
     * JSON 消息转换器
     * 作用：将Java对象和JSON字符串相互转换，方便消息的序列化和反序列化
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 自定义 RabbitTemplate
     * 作用：用于发送消息到RabbitMQ，配置消息确认和返回机制
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 设置消息转换器
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        /**
         * 消息发送确认回调
         * 作用：消息发送到Broker后的确认回调，ack=true表示成功，ack=false表示失败
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String messageId = correlationData != null ? correlationData.getId() : "unknown";
            if (ack) {
                log.debug("✅ 消息发送成功: {}", messageId);
            } else {
                log.error("❌ 消息发送失败: {}, cause: {}", messageId, cause);
                // 这里可以添加重试逻辑或告警
                // 可以根据业务需求记录到数据库或发送告警通知
            }
        });

        /**
         * 消息路由失败回调 - 使用 setReturnsCallback 替代已废弃的 setReturnCallback
         */
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("🚫 消息路由失败: 交换机: {}, 路由键: {}, 回应码: {}, 回应文本: {}",
                returned.getExchange(), returned.getRoutingKey(),
                returned.getReplyCode(), returned.getReplyText());

            // 记录路由失败的消息详情
            String messageBody = new String(returned.getMessage().getBody());
            log.error("路由失败的消息内容: {}", messageBody);

            // 这里可以添加失败处理逻辑，比如保存到数据库、发送告警等
            // saveFailedMessageToDB(returned.getExchange(), returned.getRoutingKey(), messageBody, returned.getReplyText());
        });

        // 开启强制消息投递：当消息无法路由时，会调用returnsCallback
        rabbitTemplate.setMandatory(true);

        return rabbitTemplate;
    }

    /**
     * 消费者容器工厂配置
     * 作用：配置消息消费者的行为，如并发、确认模式等
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        // 设置消息转换器
        factory.setMessageConverter(jsonMessageConverter());

        // 设置手动ACK：消费者处理完消息后需要手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        // 设置预取数量：每次从Broker拉取1条消息，处理完再拉取下一条
        factory.setPrefetchCount(1);

        // 设置并发消费者数量
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);

        // 设置消息重试机制
        factory.setMissingQueuesFatal(false); // 队列不存在时不报错

        // 设置消费者异常时的处理策略
        factory.setDefaultRequeueRejected(false); // 不重新入队，进入死信队列

        return factory;
    }

    /**
     * RabbitAdmin 配置
     * 作用：用于动态创建和管理队列、交换机等
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        // 设置自动声明队列和交换机
        rabbitAdmin.setAutoStartup(true);

        return rabbitAdmin;
    }

    /**
     * 初始化完成后声明所有组件
     * 使用 @Bean 的方式确保组件被正确声明
     */
    @Bean
    public String initializeRabbitMQComponents(RabbitAdmin rabbitAdmin) {
        try {
            // 声明交换机
            rabbitAdmin.declareExchange(distributionExchange());
            rabbitAdmin.declareExchange(deadLetterExchange());
            rabbitAdmin.declareExchange(delayExchange());

            // 声明队列
            rabbitAdmin.declareQueue(distributionQueue());
            rabbitAdmin.declareQueue(deadLetterQueue());
            rabbitAdmin.declareQueue(delayQueue());
            rabbitAdmin.declareQueue(retryQueue());

            // 声明绑定
            rabbitAdmin.declareBinding(distributionBinding());
            rabbitAdmin.declareBinding(deadLetterBinding());
            rabbitAdmin.declareBinding(delayBinding());
            rabbitAdmin.declareBinding(retryBinding());

            log.info("✅ RabbitMQ所有组件声明完成");
            log.info("📊 队列信息:");
            log.info("   - 主队列: {}", queue);
            log.info("   - 死信队列: {}", deadLetterQueue);
            log.info("   - 延时队列: {} ({}ms)", delayQueue, delayTime);
            log.info("   - 重试队列: {} ({}ms)", retryQueue, retryInterval);

            return "RabbitMQ初始化成功";

        } catch (Exception e) {
            log.error("❌ RabbitMQ组件声明失败", e);
            throw new RuntimeException("RabbitMQ配置初始化失败", e);
        }
    }

    /**
     * 获取队列信息的工具方法（用于监控和管理）
     */
    public String getQueueInfo() {
        return String.format("""
            RabbitMQ队列配置:
            主队列: %s -> %s (%s)
            死信队列: %s -> %s (%s)
            延时队列: %s -> %s (%s, %dms)
            重试队列: %s -> %s (%s, %dms)""",
            queue, exchange, routingKey,
            deadLetterQueue, deadLetterExchange, deadLetterRoutingKey,
            delayQueue, delayExchange, delayRoutingKey, delayTime,
            retryQueue, exchange, retryRoutingKey, retryInterval
        );
    }
}
