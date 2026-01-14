package com.sxpcwlkj.mq.service;

import com.sxpcwlkj.mq.entity.OrderTimeoutMessage;
import com.sxpcwlkj.mq.handler.OrderTimeoutHandler;

/**
 * 订单超时服务接口
 * 提供订单超时延时取消的功能
 */
public interface OrderTimeoutService {

    /**
     * 注册订单超时处理器
     * @param handler 处理器实例
     */
    void registerHandler(OrderTimeoutHandler handler);

    /**
     * 设置当前使用的处理器
     * @param handlerName 处理器名称
     * @return 设置是否成功
     */
    boolean setCurrentHandler(String handlerName);

    /**
     * 获取当前处理器名称
     * @return 当前处理器名称
     */
    String getCurrentHandler();

    /**
     * 发送订单超时取消消息（使用默认延时时间）
     * @param message 订单超时消息
     * @return 发送是否成功
     */
    boolean sendOrderTimeoutMessage(OrderTimeoutMessage message);

    /**
     * 发送订单超时取消消息（自定义延时时间）
     * @param message 订单超时消息
     * @param delayTimeMs 延时时间（毫秒）
     * @return 发送是否成功
     */
    boolean sendOrderTimeoutMessage(OrderTimeoutMessage message, long delayTimeMs);

    /**
     * 快速发送订单超时消息
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param delayTimeMs 延时时间（毫秒）
     * @return 发送是否成功
     */
    boolean sendQuickOrderTimeout(String orderId, String orderStatus, long delayTimeMs);

    /**
     * 取消订单超时任务（通过消息ID）
     * @param messageId 消息ID
     * @return 取消是否成功
     */
    boolean cancelOrderTimeout(String messageId);

    /**
     * 获取订单超时队列状态
     * @return 状态描述
     */
    String getOrderTimeoutQueueStatus();
}
