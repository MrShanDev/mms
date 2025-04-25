package com.sxpcwlkj.mq.queueExchange;

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
public class MqQueueExchangeProducer {


    private final RabbitTemplate rabbitTemplate;

    final String  exchangeName= "mms.queue.exchange.order";
    final String  routingKey= "mms.queue.key.order";

    /**
     * 发送消息
     *
     * @param msg 消息内容
     */
    public void sendMsg(String msg) {

        /**
         * 这里是交换机发生消息 ，如果没有交换机，则需要设置为空字符串
         */
        rabbitTemplate.convertAndSend(exchangeName,routingKey, msg);

    }


    /**
     * 消费者
     *
     * @param msg
     */
    @RabbitListener(queues = exchangeName+".one")
    public void getQueue(String msg)  {
        System.out.println(exchangeName+":消费者1：" + msg);
    }

    @RabbitListener(queues = exchangeName+".two")
    public void getQueueTwo(String msg)  {
        System.out.println(exchangeName+":消费者2：" + msg);
    }


}
