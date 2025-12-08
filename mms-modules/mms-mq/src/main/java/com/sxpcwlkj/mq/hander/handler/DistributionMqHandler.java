package com.sxpcwlkj.mq.hander.handler;

import com.sxpcwlkj.mq.entity.DistributionMessage;
import com.sxpcwlkj.mq.hander.MqHandler;
import org.springframework.stereotype.Service;

@Service
public class DistributionMqHandler implements MqHandler {

    @Override
    public String getHandlerType() {
        return "distribution";
    }

    @Override
    public boolean handleMessage(DistributionMessage message) {
        // 实现具体的分销计算逻辑
        String orderId = message.getOrderId();
        // 业务处理...
        return true; // 返回处理结果
    }

    @Override
    public boolean isMessageProcessed(String orderId) {
        // 检查订单是否已处理过，避免重复处理
        return false;
    }
}
