package com.sxpcwlkj.member.enums;

import lombok.Getter;

import java.util.*;

/**
 * 售后订单状态枚举
 * 描述售后订单的状态流转
 */
@Getter
public enum AfterSaleOrderStatusEnum {
    WAITING_REVIEW(1, "待审核", "等待商家审核售后申请", AfterSaleStatusCategory.PENDING),
    REVIEW_APPROVED(2, "审核通过", "商家审核通过售后申请", AfterSaleStatusCategory.PROCESSING),
    REVIEW_REJECTED(3, "审核拒绝", "商家拒绝售后申请", AfterSaleStatusCategory.CLOSED),
    WAITING_SELLER_RECEIVE(4, "买家退货，待卖家收货", "买家已退货，等待卖家确认收货", AfterSaleStatusCategory.PROCESSING),
    SELLER_CONFIRMED_RECEIPT(5, "卖家确认收货", "卖家已确认收到退货商品", AfterSaleStatusCategory.PROCESSING),
    SELLER_TERMINATED(6, "卖家终止售后", "卖家终止了售后流程", AfterSaleStatusCategory.CLOSED),
    BUYER_CONFIRMED_RECEIPT(7, "买家确认收货", "买家确认收到换货商品", AfterSaleStatusCategory.PROCESSING),
    BUYER_CANCELLED(8, "买家取消售后", "买家主动取消售后申请", AfterSaleStatusCategory.CLOSED),
    WAITING_PLATFORM_REFUND(9, "平台退款中", "等待平台执行退款操作", AfterSaleStatusCategory.PROCESSING),
    COMPLETED(10, "完成售后", "售后流程已完成", AfterSaleStatusCategory.COMPLETED);

    private final int code;
    private final String description;
    private final String detail;
    private final AfterSaleStatusCategory category;

    private static final Map<Integer, AfterSaleOrderStatusEnum> CODE_MAP = new HashMap<>();

    static {
        for (AfterSaleOrderStatusEnum status : values()) {
            CODE_MAP.put(status.code, status);
        }
    }

    AfterSaleOrderStatusEnum(int code, String description, String detail, AfterSaleStatusCategory category) {
        this.code = code;
        this.description = description;
        this.detail = detail;
        this.category = category;
    }

    public static AfterSaleOrderStatusEnum getByCode(Integer code) {
        if (code == null) return null;
        return CODE_MAP.get(code);
    }

    public static boolean isValidCode(Integer code) {
        return code != null && CODE_MAP.containsKey(code);
    }

    // ==================== 基础状态判断 ====================

    /**
     * 判断售后是否已结束（不可再变更）
     */
    public boolean isFinalized() {
        return this.category == AfterSaleStatusCategory.COMPLETED ||
            this.category == AfterSaleStatusCategory.CLOSED;
    }

    /**
     * 判断售后是否正在进行中
     */
    public boolean isProcessing() {
        return this.category == AfterSaleStatusCategory.PROCESSING;
    }

    /**
     * 判断售后是否待处理
     */
    public boolean isPending() {
        return this.category == AfterSaleStatusCategory.PENDING;
    }

    // ==================== 操作权限判断 ====================

    /**
     * 判断售后是否可审核
     */
    public boolean canReview() {
        return this == WAITING_REVIEW;
    }

    /**
     * 判断售后是否可取消（买家端）
     */
    public boolean canCancel() {
        return this == WAITING_REVIEW ||
            this == REVIEW_APPROVED;
    }

    /**
     * 判断卖家是否可以确认收货
     */
    public boolean canConfirmReceipt() {
        return this == WAITING_SELLER_RECEIVE;
    }

    /**
     * 判断卖家是否可以终止售后
     */
    public boolean canTerminate() {
        return this == WAITING_REVIEW ||
            this == REVIEW_APPROVED ||
            this == WAITING_SELLER_RECEIVE;
    }

    /**
     * 判断买家是否可以确认收货（换货场景）
     */
    public boolean canBuyerConfirmReceipt() {
        return this == SELLER_CONFIRMED_RECEIPT; // 假设卖家发货后买家确认收货
    }

    /**
     * 判断是否可以执行退款
     */
    public boolean canRefund() {
        return this == SELLER_CONFIRMED_RECEIPT ||
            this == REVIEW_APPROVED; // 仅退款场景
    }

    // ==================== 业务状态判断 ====================

    /**
     * 判断售后是否已关闭
     */
    public boolean isClosed() {
        return this.category == AfterSaleStatusCategory.CLOSED;
    }

    /**
     * 判断售后是否已完成
     */
    public boolean isCompleted() {
        return this.category == AfterSaleStatusCategory.COMPLETED;
    }

    /**
     * 判断售后是否已审核
     */
    public boolean isReviewed() {
        return this == REVIEW_APPROVED ||
            this == REVIEW_REJECTED;
    }

    /**
     * 判断售后是否审核通过
     */
    public boolean isReviewApproved() {
        return this == REVIEW_APPROVED;
    }

    /**
     * 判断售后是否审核拒绝
     */
    public boolean isReviewRejected() {
        return this == REVIEW_REJECTED;
    }

    /**
     * 判断是否等待买家退货
     */
    public boolean isWaitingBuyerReturn() {
        return this == REVIEW_APPROVED; // 审核通过后等待买家退货
    }

    /**
     * 判断是否等待卖家收货
     */
    public boolean isWaitingSellerReceive() {
        return this == WAITING_SELLER_RECEIVE;
    }

    /**
     * 判断是否等待平台退款
     */
    public boolean isWaitingPlatformRefund() {
        return this == WAITING_PLATFORM_REFUND;
    }

    /**
     * 判断售后是否涉及退货
     */
    public boolean involvesReturn() {
        return this == WAITING_SELLER_RECEIVE ||
            this == SELLER_CONFIRMED_RECEIPT ||
            this == WAITING_PLATFORM_REFUND;
    }

    // ==================== 工具方法 ====================

    /**
     * 获取需要商家操作的状态列表
     */
    public static List<AfterSaleOrderStatusEnum> getActionableStatuses() {
        return Arrays.asList(
            WAITING_REVIEW,
            WAITING_SELLER_RECEIVE,
            WAITING_PLATFORM_REFUND
        );
    }

    /**
     * 获取需要提醒的状态列表
     */
    public static List<AfterSaleOrderStatusEnum> getAlertStatuses() {
        return Arrays.asList(
            WAITING_REVIEW,
            WAITING_SELLER_RECEIVE,
            WAITING_PLATFORM_REFUND
        );
    }

    /**
     * 获取下一个可能的状态
     */
    public List<AfterSaleOrderStatusEnum> getNextPossibleStatuses() {
        return switch (this) {
            case WAITING_REVIEW -> Arrays.asList(REVIEW_APPROVED, REVIEW_REJECTED, BUYER_CANCELLED, SELLER_TERMINATED);
            case REVIEW_APPROVED -> Arrays.asList(WAITING_SELLER_RECEIVE, WAITING_PLATFORM_REFUND, BUYER_CANCELLED, SELLER_TERMINATED);
            case WAITING_SELLER_RECEIVE -> Arrays.asList(SELLER_CONFIRMED_RECEIPT, BUYER_CONFIRMED_RECEIPT, BUYER_CANCELLED, SELLER_TERMINATED);
            case SELLER_CONFIRMED_RECEIPT -> Arrays.asList(WAITING_PLATFORM_REFUND, COMPLETED, BUYER_CONFIRMED_RECEIPT);
            case WAITING_PLATFORM_REFUND -> List.of(COMPLETED);
            case BUYER_CONFIRMED_RECEIPT -> List.of(COMPLETED);
            default -> Collections.emptyList();
        };
    }

    /**
     * 检查状态流转是否有效
     */
    public boolean canTransitionTo(AfterSaleOrderStatusEnum targetStatus) {
        return getNextPossibleStatuses().contains(targetStatus);
    }

    /**
     * 获取可用的售后类型
     */
    public static List<AfterSaleOrderStatusEnum> getAvailableStatuses() {
        return Arrays.asList(
            WAITING_REVIEW,
            REVIEW_APPROVED,
            WAITING_SELLER_RECEIVE,
            SELLER_CONFIRMED_RECEIPT,
            WAITING_PLATFORM_REFUND,
            COMPLETED
        );
    }

    @Override
    public String toString() {
        return description + "(" + code + ")";
    }

    /**
     * 售后状态分类
     */
    @Getter
    public enum AfterSaleStatusCategory {
        PENDING("待处理"),
        PROCESSING("进行中"),
        COMPLETED("已完成"),
        CLOSED("已关闭");

        private final String description;

        AfterSaleStatusCategory(String description) {
            this.description = description;
        }
    }
}
