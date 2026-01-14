package com.sxpcwlkj.mq.handler;

import com.sxpcwlkj.mq.entity.OrderTimeoutMessage;

/**
 * 订单超时处理器接口
 * 业务模块实现此接口来处理订单超时逻辑
 */
public interface OrderTimeoutHandler {

    /**
     * 处理订单超时
     * @param message 订单超时消息
     * @return 处理结果，true-成功，false-失败
     */
    boolean handleOrderTimeout(OrderTimeoutMessage message);

    /**
     * 检查消息是否已被取消（支付成功等场景）
     * @param messageId 消息ID
     * @return 是否已取消
     */
    boolean isMessageCancelled(String messageId);

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
     * 记录超时消息（用于后续取消）
     * @param message 超时消息
     */
    void recordTimeoutMessage(OrderTimeoutMessage message);

    /**
     * 清理超时消息记录
     * @param messageId 消息ID
     */
    void clearTimeoutMessage(String messageId);

    /**
     * 标记消息为已取消（用于支付成功等场景）
     * @param messageId 消息ID
     */
    void cancelTimeoutMessage(String messageId);

    /**
     * 获取处理器名称
     * @return 处理器名称
     */
    default String getHandlerName() {
        return "default";
    }
}
