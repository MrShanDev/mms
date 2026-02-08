package com.sxpcwlkj.mq.handler;


import com.sxpcwlkj.mq.entity.DistributionMessage;

/**
 * 消息处理器接口
 * 供业务模块实现，处理具体的业务逻辑
 */
public interface MqHandler {

    /**
     * 处理消息
     * @param message 消息内容
     * @return 处理结果
     */
    boolean handleMessage(DistributionMessage message);

    /**
     * 检查消息是否已处理
     * @param orderId 订单ID
     * @return 是否已处理
     */
    boolean isMessageProcessed(String orderId);

    /**
     * 获取处理器类型
     * @return 处理器类型
     */
    String getHandlerType();
}
