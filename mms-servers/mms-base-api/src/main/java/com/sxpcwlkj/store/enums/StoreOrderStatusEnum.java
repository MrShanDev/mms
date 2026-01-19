package com.sxpcwlkj.store.enums;

import lombok.Getter;

import java.util.*;

/**
 * 店铺订单状态枚举
 * 描述店铺视角下的订单状态流转
 */
@Getter
public enum StoreOrderStatusEnum {
    NONE(0, "无或已删除", "无", OrderStatusCategory.NONE),

    // ==================== 待处理状态 ====================
    WAITING_PAYMENT(10, "等待付款", "顾客尚未完成支付", OrderStatusCategory.PENDING),
    WAITING_CONFIRMATION(15, "等待确认", "等待商家确认订单", OrderStatusCategory.PENDING),
    WAITING_REVIEW(18, "等待审核", "订单需要人工审核", OrderStatusCategory.PENDING),

    // ==================== 进行中状态 ====================
    WAITING_SHIPMENT(20, "等待发货", "订单已确认，等待打包发货", OrderStatusCategory.PROCESSING),
    PARTIALLY_SHIPPED(25, "部分发货", "订单中部分商品已发货", OrderStatusCategory.PROCESSING),
    SHIPPED(30, "已发货", "商品已发出，运输中", OrderStatusCategory.PROCESSING),
    DELIVERED(35, "已送达", "商品已送达，等待用户确认", OrderStatusCategory.PROCESSING),

    // ==================== 完成状态 ====================
    COMPLETED(40, "交易完成", "订单已完成，等待评价", OrderStatusCategory.COMPLETED),
    REVIEWED(45, "已评价", "订单已完成评价", OrderStatusCategory.COMPLETED),

    // ==================== 售后状态 ====================
    AFTER_SALE_PROCESSING(50, "售后中", "用户已申请售后", OrderStatusCategory.AFTER_SALE),
    RETURN_PROCESSING(55, "退货中", "同意退货，等待用户寄回", OrderStatusCategory.AFTER_SALE),
    EXCHANGE_PROCESSING(60, "换货中", "同意换货，处理中", OrderStatusCategory.AFTER_SALE),
    REFUND_PROCESSING(65, "退款中", "退款处理中", OrderStatusCategory.AFTER_SALE),
    PARTIALLY_REFUNDED(68, "部分退款", "订单部分金额已退款", OrderStatusCategory.AFTER_SALE),

    // ==================== 关闭状态 ====================
    CANCELLED(70, "已取消", "订单已取消", OrderStatusCategory.CLOSED),
    CLOSED(75, "已关闭", "订单已关闭", OrderStatusCategory.CLOSED),
    REFUNDED(80, "已退款", "订单已退款完成", OrderStatusCategory.CLOSED),
    RETURNED(85, "已退货", "退货已完成", OrderStatusCategory.CLOSED),
    EXPIRED(90, "已失效", "订单已失效", OrderStatusCategory.CLOSED);

    private final int code;
    private final String description;
    private final String detail;
    private final OrderStatusCategory category;

    private static final Map<Integer, StoreOrderStatusEnum> CODE_MAP = new HashMap<>();

    static {
        for (StoreOrderStatusEnum status : values()) {
            CODE_MAP.put(status.code, status);
        }
    }

    StoreOrderStatusEnum(int code, String description, String detail, OrderStatusCategory category) {
        this.code = code;
        this.description = description;
        this.detail = detail;
        this.category = category;
    }

    public static StoreOrderStatusEnum getByCode(Integer code) {
        if (code == null) return null;
        return CODE_MAP.get(code);
    }

    public static boolean isValidCode(Integer code) {
        return code != null && CODE_MAP.containsKey(code);
    }

    // ==================== 基础状态判断 ====================

    /**
     * 判断订单是否已付款（已支付或曾经支付过）
     */
    public boolean isPaid() {
        return !(this == WAITING_PAYMENT || this == NONE);
    }

    /**
     * 判断订单是否已完成（不可再变更）
     */
    public boolean isFinalized() {
        return this.category == OrderStatusCategory.COMPLETED ||
            this.category == OrderStatusCategory.CLOSED;
    }

    /**
     * 判断订单是否正在进行中
     */
    public boolean isProcessing() {
        return this.category == OrderStatusCategory.PROCESSING;
    }

    /**
     * 判断订单是否在售后中
     */
    public boolean isAfterSale() {
        return this.category == OrderStatusCategory.AFTER_SALE;
    }

    // ==================== 操作权限判断 ====================

    /**
     * 判断订单是否可付款
     */
    public boolean canPay() {
        return this == WAITING_PAYMENT;
    }

    /**
     * 判断订单是否可取消
     */
    public boolean canCancel() {
        return this == WAITING_PAYMENT;
    }

    /**
     * 判断订单是否可标记为删除（伪删除）
     */
    public boolean canDelete() {
        return this == CANCELLED ||
            this == CLOSED ||
            this == REFUNDED ||
            this == RETURNED ||
            this == EXPIRED ||
            this == NONE;
    }

    /**
     * 判断订单是否已标记删除
     * 注意：这个状态需要配合数据库的deleted字段使用
     */
    public boolean isMarkedDeleted() {
        return this == NONE; // NONE状态表示已标记删除
    }

    /**
     * 判断订单是否可发货
     */
    public boolean canShip() {
        return this == WAITING_SHIPMENT ||
            this == PARTIALLY_SHIPPED;
    }

    /**
     * 判断是否可以确认收货（用户端操作）
     */
    public boolean canConfirmReceipt() {
        return this == SHIPPED ||
            this == DELIVERED;
    }

    /**
     * 判断订单是否可评价
     */
    public boolean canReview() {
        return this == COMPLETED;
    }

    /**
     * 判断是否可以申请售后
     */
    public boolean canApplyAfterSale() {
        return this.code >= WAITING_SHIPMENT.getCode() &&
            this.code <= REVIEWED.getCode() &&
            !isAfterSale();
    }

    // ==================== 业务状态判断 ====================

    /**
     * 判断订单是否已关闭
     */
    public boolean isClosed() {
        return this.category == OrderStatusCategory.CLOSED;
    }

    /**
     * 判断订单是否已完成
     */
    public boolean isCompleted() {
        return this.category == OrderStatusCategory.COMPLETED;
    }

    /**
     * 判断订单是否已评价
     */
    public boolean isReviewed() {
        return this == REVIEWED;
    }

    /**
     * 判断订单是否已退款完成
     */
    public boolean isRefundCompleted() {
        return this == REFUNDED ||
            this == PARTIALLY_REFUNDED;
    }

    /**
     * 判断订单是否正在退款中
     */
    public boolean isRefunding() {
        return this == REFUND_PROCESSING;
    }

    // ==================== 发货相关状态判断 ====================

    /**
     * 判断订单是否已发货（包括部分发货、已发货、已送达等状态）
     */
    public boolean isShipped() {
        return this == PARTIALLY_SHIPPED ||
            this == SHIPPED ||
            this == DELIVERED ||
            this == COMPLETED ||
            this == REVIEWED;
    }

    /**
     * 判断订单是否完全发货（非部分发货）
     */
    public boolean isFullyShipped() {
        return this == SHIPPED ||
            this == DELIVERED ||
            this == COMPLETED ||
            this == REVIEWED;
    }

    /**
     * 判断订单是否已送达
     */
    public boolean isDelivered() {
        return this == DELIVERED ||
            this == COMPLETED ||
            this == REVIEWED;
    }

    // ==================== 工具方法 ====================

    /**
     * 获取需要商家操作的状态列表
     */
    public static List<StoreOrderStatusEnum> getActionableStatuses() {
        return Arrays.asList(
            WAITING_CONFIRMATION,
            WAITING_REVIEW,
            WAITING_SHIPMENT,
            AFTER_SALE_PROCESSING,
            RETURN_PROCESSING,
            EXCHANGE_PROCESSING,
            REFUND_PROCESSING
        );
    }

    /**
     * 获取需要提醒的状态列表
     */
    public static List<StoreOrderStatusEnum> getAlertStatuses() {
        return Arrays.asList(
            WAITING_PAYMENT,
            WAITING_CONFIRMATION,
            WAITING_SHIPMENT,
            AFTER_SALE_PROCESSING
        );
    }

    /**
     * 获取下一个可能的状态
     */
    public List<StoreOrderStatusEnum> getNextPossibleStatuses() {
        return switch (this) {
            case WAITING_PAYMENT -> Arrays.asList(WAITING_CONFIRMATION, CANCELLED);
            case WAITING_CONFIRMATION -> Arrays.asList(WAITING_SHIPMENT, WAITING_REVIEW, CANCELLED);
            case WAITING_REVIEW -> Arrays.asList(WAITING_SHIPMENT, CLOSED);
            case WAITING_SHIPMENT -> Arrays.asList(SHIPPED, PARTIALLY_SHIPPED, CANCELLED);
            case PARTIALLY_SHIPPED -> List.of(SHIPPED);
            case SHIPPED -> Arrays.asList(DELIVERED, AFTER_SALE_PROCESSING);
            case DELIVERED -> Arrays.asList(COMPLETED, AFTER_SALE_PROCESSING);
            case COMPLETED -> Arrays.asList(REVIEWED, AFTER_SALE_PROCESSING);
            case AFTER_SALE_PROCESSING ->
                    Arrays.asList(RETURN_PROCESSING, EXCHANGE_PROCESSING, REFUND_PROCESSING, COMPLETED);
            case RETURN_PROCESSING -> Arrays.asList(RETURNED, REFUNDED);
            case EXCHANGE_PROCESSING -> Arrays.asList(SHIPPED, COMPLETED);
            case REFUND_PROCESSING -> Arrays.asList(REFUNDED, PARTIALLY_REFUNDED);
            default -> Collections.emptyList();
        };
    }

    /**
     * 检查状态流转是否有效
     */
    public boolean canTransitionTo(StoreOrderStatusEnum targetStatus) {
        return getNextPossibleStatuses().contains(targetStatus);
    }

    @Override
    public String toString() {
        return description + "(" + code + ")";
    }

    /**
     * 订单状态分类
     */
    @Getter
    public enum OrderStatusCategory {
        NONE("无"),
        PENDING("待处理"),
        PROCESSING("进行中"),
        COMPLETED("已完成"),
        AFTER_SALE("售后中"),
        CLOSED("已关闭");

        private final String description;

        OrderStatusCategory(String description) {
            this.description = description;
        }

    }
}
