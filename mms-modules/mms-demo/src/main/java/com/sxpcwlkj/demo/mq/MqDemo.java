package com.sxpcwlkj.demo.mq;

import com.sxpcwlkj.mq.entity.DistributionMessage;
import com.sxpcwlkj.mq.entity.OrderInfoEntity;
import com.sxpcwlkj.mq.entity.QueueStatus;
import com.sxpcwlkj.mq.service.MqService;
import com.sxpcwlkj.wx.utils.WeChatBotMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqDemo {

    private final MqService mqService;

    public void createOrder(OrderInfoEntity order) {
        DistributionMessage message = new DistributionMessage();
        message.setOrderId(order.getOrderId());
        message.setBuyerId(order.getBuyerId());
        message.setBusinessType("ORDER");
        message.setPriority("HIGH");

        // 发送到主队列
        mqService.sendToMainQueue(message);

        // 或者智能发送（根据业务规则自动选择队列）
        // mqService.sendSmart(message);
        // 发送到主队列
        //mqService.sendToMainQueue(message);

        // 发送到延时队列（默认1分钟）
        //mqService.sendToDelayQueue(message);

        // 发送到延时队列（自定义时间）
        //mqService.sendToDelayQueue(message, 5000); // 5秒

        // 智能发送（根据业务类型、优先级等自动选择队列）
        //mqService.sendSmart(message);

        // 发送到死信队列
        //mqService.sendToDeadLetterQueue(message);

        // 手动消费一条消息
        //String result = mqService.consumeOneMessage();

        // 批量消费消息
        //String result = mqService.consumeBatchMessages(10);

        // 处理死信队列消息
        //String result = mqService.consumeOneDeadLetterMessage();

        // 批量重新处理死信消息
        //String result = mqService.reprocessDeadLetterMessages(5);

        // 将死信消息重新投递到主队列
        //String result = mqService.redeliverDeadLetterToMainQueue(10);

        // 获取队列状态
        //QueueStatus status = mqService.getQueueStatus();

        // 检查死信队列状态
        //String dlqStatus = mqService.getDeadLetterQueueStatus();

        // 获取已注册的处理器
        //List<String> handlers = mqService.getRegisteredHandlers();

        // 切换当前处理器
        //mqService.setCurrentHandler("distribution");
    }

    // 在处理器中实现重试逻辑
    // 定时检查队列状态
    @Scheduled(fixedRate = 60000)
    public void monitorQueueHealth() {
        QueueStatus status = mqService.getQueueStatus();
        if (!"空闲".equals(status.getDeadLetterQueueStatus())) {
            // 死信队列有积压，发送告警
            WeChatBotMessageUtil.sendAlertMessage("死信队列有消息需要处理", status.getSummary());
        }
    }

}
