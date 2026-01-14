package com.sxpcwlkj.mq.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 订单超时消息实体类
 * 作用：用于订单超时自动取消的延时队列消息
 */
@Data
@Slf4j
public class OrderTimeoutMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 订单基本信息 ====================
    private String orderId;                 // 订单ID（唯一标识）
    private String orderNo;                 // 订单编号（展示用）
    private String orderStatus;             // 订单状态：PENDING-待支付, PAID-已支付, COMPLETED-已完成, CANCELLED-已取消
    private BigDecimal orderAmount;         // 订单金额
    private LocalDateTime createTime;       // 订单创建时间
    private LocalDateTime expireTime;       // 订单过期时间

    // ==================== 买家信息 ====================
    private String buyerId;                 // 购买者ID
    private String buyerName;               // 购买者姓名
    private String buyerPhone;              // 购买者手机号

    // ==================== 租户信息 ====================
    private String tenantId;                // 租户ID
    private String tenantName;              // 租户名称

    // ==================== 消息控制属性 ====================
    private String messageId;               // 消息ID（用于幂等控制）
    private String messageSource;           // 消息来源：ORDER_CREATE, PAYMENT_PENDING等
    private Integer retryCount;             // 重试次数
    private Long timestamp;                 // 消息创建时间戳
    private Long delayTime;                 // 延时时间（毫秒）

    // ==================== 业务扩展属性 ====================
    private String businessType;            // 业务类型：ORDER_TIMEOUT, PAYMENT_TIMEOUT等
    private String cancelReason;            // 取消原因
    private Map<String, Object> extraParams;// 扩展参数

    // ==================== 操作信息 ====================
    private String operator;                // 操作人
    private String operatorId;              // 操作人ID
    private String requestId;               // 请求ID（用于链路追踪）

    /**
     * 验证消息的必要字段
     */
    public boolean validate() {
        if (orderId == null || orderId.trim().isEmpty()) {
            log.warn("订单ID不能为空: orderId={}", orderId);
            return false;
        }
        if (orderStatus == null || orderStatus.trim().isEmpty()) {
            log.warn("订单状态不能为空: orderId={}", orderId);
            return false;
        }
        return true;
    }

    /**
     * 验证消息的完整性
     */
    public boolean validateComplete() {
        // 基本验证
        if (!validate()) {
            return false;
        }

        // 业务类型验证
        if (businessType == null || businessType.trim().isEmpty()) {
            log.warn("消息缺少业务类型: orderId={}", orderId);
        }

        // 时间戳验证
        if (timestamp == null || timestamp <= 0) {
            log.warn("消息时间戳无效: orderId={}", orderId);
        }

        return true;
    }

    /**
     * 设置默认值
     */
    public OrderTimeoutMessage() {
        this.timestamp = System.currentTimeMillis();
        this.retryCount = 0;
        this.businessType = "ORDER_TIMEOUT";
        this.messageSource = "ORDER_CREATE";
        // 生成默认消息ID
        this.messageId = generateMessageId();
    }

    /**
     * 快速构建方法 - 订单超时取消
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param delayTimeMs 延时时间（毫秒）
     * @return OrderTimeoutMessage
     */
    public static OrderTimeoutMessage buildOrderTimeout(String orderId, String orderStatus, long delayTimeMs) {
        OrderTimeoutMessage message = new OrderTimeoutMessage();
        message.setOrderId(orderId);
        message.setOrderStatus(orderStatus);
        message.setDelayTime(delayTimeMs);
        message.setBusinessType("ORDER_TIMEOUT");
        message.setCancelReason("订单超时未支付");
        message.setMessageId(generateMessageId());
        return message;
    }

    /**
     * 快速构建方法 - 支付超时
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param delayTimeMs 延时时间（毫秒）
     * @return OrderTimeoutMessage
     */
    public static OrderTimeoutMessage buildPaymentTimeout(String orderId, String orderStatus, long delayTimeMs) {
        OrderTimeoutMessage message = new OrderTimeoutMessage();
        message.setOrderId(orderId);
        message.setOrderStatus(orderStatus);
        message.setDelayTime(delayTimeMs);
        message.setBusinessType("PAYMENT_TIMEOUT");
        message.setCancelReason("支付超时");
        message.setMessageSource("PAYMENT_PENDING");
        message.setMessageId(generateMessageId());
        return message;
    }

    /**
     * 生成消息ID
     */
    private static String generateMessageId() {
        return "TIMEOUT_" + System.currentTimeMillis() + "_" +
            java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 增加重试次数
     */
    public void incrementRetryCount() {
        if (retryCount == null) {
            retryCount = 0;
        }
        retryCount++;
    }

    /**
     * 判断是否需要重试
     */
    public boolean shouldRetry(int maxRetryCount) {
        return retryCount == null || retryCount < maxRetryCount;
    }

    /**
     * 判断订单是否已超时
     */
    public boolean isExpired() {
        if (expireTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expireTime);
    }
}
