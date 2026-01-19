package com.sxpcwlkj.store.entity.vo;

import com.sxpcwlkj.store.enums.StoreOrderStatusEnum;
import lombok.Data;

@Data
public class StoreOrderStateVo {

    /**
     * 是否已付款
     */
    private Boolean isPaid;
    /**
     * 是否已完成（不可再变更）
     */
    private Boolean isFinalized;

    /**
     * 是否正在进行中
     */
    private Boolean isProcessing;
    /**
     * 是否在售后中
     */
    private Boolean isAfterSale;
    /**
     * 是否可支付
     */
    private Boolean canPay;
    /**
     * 是否可取消
     */
    private Boolean canCancel;
    /**
     * 是否可删除
     */
    private Boolean canDelete;
    /**
     * 是否可发货
     */
    private Boolean canShip;
    /**
     * 是否可以确认收货（用户端操作）
     */
    private Boolean canConfirmReceipt;

    /**
     * 是否可评价
     */
    private Boolean canReview;

    /**
     * 是否可以申请售后
     */
    private Boolean canApplyAfterSale;

    /**
     * 否已评价
     */
    private Boolean isReviewed;
    /**
     * 是否已关闭
     */
    private Boolean isClosed;

    /**
     * 是否已完成
     */
    private Boolean isCompleted;

    /**
     * 是否已退款完成
     */
    private Boolean isRefundCompleted;

    /**
     * 是否正在退款中
     */
    private Boolean isRefunding;

    public StoreOrderStateVo() {
            this.canPay = false;
            this.isPaid = false;
            this.isFinalized = false;
            this.isProcessing = false;
            this.isAfterSale = false;
            this.canCancel = false;
            this.canDelete = false;
            this.canShip = false;
            this.canConfirmReceipt = false;
            this.canApplyAfterSale = false;
            this.canReview = false;
            this.isClosed = false;
            this.isCompleted = false;
            this.isReviewed = false;
            this.isRefundCompleted = false;
            this.isRefunding = false;
    }

    public StoreOrderStateVo(Integer code) {

        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(code);
        this.canPay = storeOrderStatusEnum.canPay();
        this.isPaid = storeOrderStatusEnum.isPaid();
        this.isFinalized = storeOrderStatusEnum.isFinalized();
        this.isProcessing = storeOrderStatusEnum.isProcessing();
        this.isAfterSale = storeOrderStatusEnum.isAfterSale();
        this.canCancel = storeOrderStatusEnum.canCancel();
        this.canDelete = storeOrderStatusEnum.canDelete();
        this.canShip = storeOrderStatusEnum.canShip();
        this.canConfirmReceipt = storeOrderStatusEnum.canConfirmReceipt();
        this.canReview = storeOrderStatusEnum.canReview();
        this.canApplyAfterSale = storeOrderStatusEnum.canApplyAfterSale();
        this.isClosed = storeOrderStatusEnum.isClosed();
        this.isCompleted = storeOrderStatusEnum.isCompleted();
        this.isReviewed = storeOrderStatusEnum.isReviewed();
        this.isRefundCompleted = storeOrderStatusEnum.isRefundCompleted();
        this.isRefunding = storeOrderStatusEnum.isRefunding();
    }
}
