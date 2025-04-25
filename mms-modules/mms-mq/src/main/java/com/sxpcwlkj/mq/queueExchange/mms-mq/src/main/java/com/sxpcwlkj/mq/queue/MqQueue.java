package com.sxpcwlkj.mq.queueExchange.mms;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置队列
 * @author: xijue
 * @date: 2019/11/14 14:44
 * @description:
 */
@Configuration
public class MqQueue {

    // 1.queueName: 队列名称
    final String  queueName= "mms.queue";


    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue getQueue(){
        /**
         * 1.name: 队列名称
         * 2.durable: 是否持久化（true持久化，false非持久化）默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
         * 3.exclusive: 是否排他（true 排外，false不排外）默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
         * 4.autoDelete: 是否自动删除（消费者断开之后：true删除，false不删除）是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
         */

        return new Queue(queueName+".one",Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);
    }



}
