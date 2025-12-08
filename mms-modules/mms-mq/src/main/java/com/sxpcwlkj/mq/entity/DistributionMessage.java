package com.sxpcwlkj.mq.entity;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 分销消息实体类
 * 作用：用于在消息队列中传递分销计算相关的数据
 */
@Data
@Slf4j
public class DistributionMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 订单基本信息 ====================
    private String orderId;           // 订单ID（唯一标识）
    private String orderNo;           // 订单编号（展示用）
    private String parentOrderId;     // 父订单ID（用于拆单场景）
    private String orderType;         // 订单类型：NORMAL-普通订单, GROUP-团购订单, PRE-预售订单
    private BigDecimal orderAmount;   // 订单总金额
    private BigDecimal payAmount;     // 实付金额
    private BigDecimal shippingFee;   // 运费
    private BigDecimal discountAmount;// 优惠金额
    private String currency;          // 币种：CNY, USD等
    private String orderStatus;       // 订单状态：PENDING-待支付, PAID-已支付, COMPLETED-已完成, CANCELLED-已取消
    private String payMethod;         // 支付方式：WECHAT, ALIPAY, BANK等
    private Long orderTime;           // 下单时间戳
    private Long payTime;             // 支付时间戳

    // ==================== 购买者信息 ====================
    private String buyerId;           // 购买者ID
    private String buyerNo;           // 购买者编号
    private String buyerName;         // 购买者姓名
    private String buyerPhone;        // 购买者手机号
    private String buyerRole;         // 购买者角色：MEMBER-会员, DISTRIBUTOR-分销商, AGENT-代理商
    private String buyerLevel;        // 购买者等级：VIP1, VIP2等
    private String inviterId;         // 邀请人ID
    private String inviterPath;       // 邀请路径（用于多级分销）

    // ==================== 商品信息 ====================
    private List<OrderInfoEntity> orderProducts; // 订单商品列表
    private Integer productCount;     // 商品总数量
    private Boolean containsVirtual;  // 是否包含虚拟商品
    private Boolean containsPhysical; // 是否包含实物商品

    // ==================== 租户和渠道信息 ====================
    private String tenantId;          // 租户ID
    private String tenantName;        // 租户名称
    private String appId;             // 应用ID
    private String channel;           // 渠道：WECHAT_MINI, APP, H5, PC
    private String platform;          // 平台：自营平台、第三方平台等

    // ==================== 分销相关属性 ====================
    private String distributionMode;  // 分销模式：FIXED-固定比例, TIERED-分级, TEAM-团队
    private BigDecimal distributionRate; // 分销比例
    private Boolean distributionEnabled; // 是否启用分销
    private String distributionLevel; // 分销层级：LEVEL1, LEVEL2, LEVEL3
    private List<String> distributionChain; // 分销链（从上级到下级）

    // ==================== 消息控制属性 ====================
    private String messageId;         // 消息ID（用于幂等控制）
    private String messageSource;     // 消息来源：ORDER, REFUND, MANUAL等
    private Integer retryCount;       // 重试次数
    private Long timestamp;           // 消息创建时间戳
    private Long expireTime;          // 消息过期时间戳
    private String version;           // 消息版本（用于兼容性）

    // ==================== 操作信息 ====================
    private String operator;          // 操作人
    private String operatorId;        // 操作人ID
    private String operation;         // 操作类型：CREATE, UPDATE, CANCEL, REFUND
    private String requestId;         // 请求ID（用于链路追踪）

    // ==================== 业务扩展属性 ====================
    private Map<String, Object> extraParams; // 扩展参数
    private String businessType;      // 业务类型：DISTRIBUTION, SETTLEMENT, BONUS等
    private String priority;          // 优先级：HIGH, MEDIUM, LOW
    private String tags;              // 标签（用于消息过滤）

    // ==================== 计算相关属性 ====================
    private Boolean calculated;       // 是否已计算
    private Long calculateTime;       // 计算时间
    private String calculateResult;   // 计算结果
    private BigDecimal totalCommission; // 总佣金
//    private List<CommissionDetail> commissionDetails; // 佣金明细

    /**
     * 验证消息的必要字段
     */
    public boolean validate() {
        if (orderId == null || orderId.trim().isEmpty()) {
            log.warn("订单ID不能为空: orderId={}", orderId);
            return false;
        }
        if (buyerId == null || buyerId.trim().isEmpty()) {
            log.warn("购买者ID不能为空: buyerId={}", buyerId);
            return false;
        }

        if (orderAmount == null || orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("订单金额无效: orderAmount={}", orderAmount);
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

        // 检查是否过期（超过7天）
        if (timestamp != null && System.currentTimeMillis() - timestamp > 7 * 24 * 60 * 60 * 1000L) {
            log.warn("消息已过期: orderId={}, timestamp={}", orderId, timestamp);
        }

        return true;
    }

    /**
     * 设置默认值
     */
    public DistributionMessage() {
        this.timestamp = System.currentTimeMillis();
        this.retryCount = 0;
        this.distributionEnabled = true;
        this.calculated = false;
        this.currency = "CNY";
        this.version = "1.0";
        this.priority = "MEDIUM";
        // 生成默认消息ID
        this.messageId = generateMessageId();
    }

    /**
     * 快速构建方法
     */
    public static DistributionMessage buildBasic(String orderId, String buyerId, String tenantId) {
        DistributionMessage message = new DistributionMessage();
        message.setOrderId(orderId);
        message.setBuyerId(buyerId);
        message.setTenantId(tenantId);
        message.setMessageId(generateMessageId());
        return message;
    }

    /**
     * 生成消息ID
     */
    private static String generateMessageId() {
        return "MSG_" + System.currentTimeMillis() + "_" +
            java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 获取有效的支付金额（如果payAmount为空则使用orderAmount）
     */
    public BigDecimal getEffectivePayAmount() {
        return payAmount != null ? payAmount : orderAmount;
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
     * 设置过期时间（相对当前时间的毫秒数）
     */
    public void setExpireAfter(long milliseconds) {
        this.expireTime = System.currentTimeMillis() + milliseconds;
    }

    /**
     * 判断消息是否过期
     */
    public boolean isExpired() {
        return expireTime != null && System.currentTimeMillis() > expireTime;
    }
}

