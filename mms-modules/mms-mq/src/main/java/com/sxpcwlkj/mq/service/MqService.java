package com.sxpcwlkj.mq.service;

import com.sxpcwlkj.mq.entity.DistributionMessage;
import com.sxpcwlkj.mq.entity.QueueStatus;
import com.sxpcwlkj.mq.hander.MqHandler;

import java.util.List;
import java.util.Map;

/**
 * 消息队列服务接口
 * 提供消息发送、消费、状态监控等功能
 */
public interface MqService {

    // ==================== 处理器管理方法 ====================

    /**
     * 注册单个消息处理器
     * @param handler 消息处理器实例
     */
    void registerHandler(MqHandler handler);

    /**
     * 批量注册消息处理器
     * @param handlers 处理器映射表，key为处理器类型
     */
    void registerHandlers(Map<String, MqHandler> handlers);

    /**
     * 获取所有已注册的处理器类型列表
     * @return 处理器类型列表
     */
    List<String> getRegisteredHandlers();

    /**
     * 设置当前使用的处理器
     * @param handlerType 处理器类型
     * @return 设置是否成功
     */
    boolean setCurrentHandler(String handlerType);

    /**
     * 获取当前使用的处理器类型
     * @return 当前处理器类型
     */
    String getCurrentHandler();

    // ==================== 消息发送方法 ====================

    /**
     * 发送消息到主队列（立即处理）
     * @param message 分销消息
     * @return 发送是否成功
     */
    boolean sendToMainQueue(DistributionMessage message);

    /**
     * 发送消息到延时队列（使用默认延时时间）
     * @param message 分销消息
     * @return 发送是否成功
     */
    boolean sendToDelayQueue(DistributionMessage message);

    /**
     * 发送消息到延时队列（自定义延时时间）
     * @param message 分销消息
     * @param delayTime 延时时间（毫秒）
     * @return 发送是否成功
     */
    boolean sendToDelayQueue(DistributionMessage message, int delayTime);

    /**
     * 发送消息到重试队列（用于处理失败的消息）
     * @param message 分销消息
     * @return 发送是否成功
     */
    boolean sendToRetryQueue(DistributionMessage message);

    /**
     * 智能发送消息（根据业务规则自动选择最优队列）
     * @param message 分销消息
     * @return 发送是否成功
     */
    boolean sendSmart(DistributionMessage message);

    /**
     * 发送消息到死信队列（最终处理失败的消息）
     * @param message 分销消息
     * @return 发送是否成功
     */
    boolean sendToDeadLetterQueue(DistributionMessage message);

    // ==================== 消息消费方法 ====================

    /**
     * 手动消费一条消息（从主队列）
     * @return 消费结果描述
     */
    String consumeOneMessage();

    /**
     * 批量消费消息（从主队列）
     * @param count 消费数量
     * @return 批量消费结果描述
     */
    String consumeBatchMessages(int count);

    /**
     * 手动消费一条死信队列消息
     * @return 消费结果描述
     */
    String consumeOneDeadLetterMessage();

    /**
     * 重新处理死信队列消息
     * @param count 处理数量
     * @return 处理结果描述
     */
    String reprocessDeadLetterMessages(int count);

    /**
     * 将死信消息重新投递到主队列
     * @param count 投递数量
     * @return 投递结果描述
     */
    String redeliverDeadLetterToMainQueue(int count);

    // ==================== 状态监控方法 ====================

    /**
     * 获取队列状态信息
     * @return 队列状态对象
     */
    QueueStatus getQueueStatus();

    /**
     * 获取死信队列状态
     * @return 状态描述字符串
     */
    String getDeadLetterQueueStatus();
}
