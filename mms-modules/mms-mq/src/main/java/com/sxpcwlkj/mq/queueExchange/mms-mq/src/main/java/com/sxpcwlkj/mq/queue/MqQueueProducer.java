package com.sxpcwlkj.mq.queueExchange.mms;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * MQ 消费者
 *
 * @author sxpcwlkj
 * @date 2020/12/11
 */
@Component
@RequiredArgsConstructor
public class MqQueueProducer {


    private final RabbitTemplate rabbitTemplate;

    final String  queueName= "mms.queue";

    /**
     * 发送消息
     *
     * @param msg 消息内容
     */
    public void sendMsg(String msg) {
        /**
         * 这里是直接给队列发送消息
         * 1.对列名称
         * 2.消息内容
         */
        rabbitTemplate.convertAndSend(queueName+".one", msg);

    }


    /**
     * 消费者
     *
     * @param msg
     */
    @RabbitListener(queues = queueName+".one")
    public void getQueue(String msg)  {
        System.out.println(queueName+":消费者：" + msg);
    }


}
