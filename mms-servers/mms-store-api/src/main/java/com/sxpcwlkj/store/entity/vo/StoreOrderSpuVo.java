package com.sxpcwlkj.store.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品信息Vo
 */
@Data
public class StoreOrderSpuVo {
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 商品ID
     */
    private String id;
    /**
     * 商品订单ID
     */
    private String itemId;
    /**
     * 商品名称
     */
    private String title;
    /**
     * 商品主图
     */
    private String mainImage;
    /**
     * 商品规格ID
     */
    private String skuId;
    /**
     * 商品规格名称
     */
    private String skuName;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 商品单价
     */
    private BigDecimal price;
    /**
     * 商品总价
     */
    private BigDecimal totalPrice;

    /**
     * 1:正常 2：售后中 3：已退款
     */
    private Integer isRefund;
    /**
     * 退款金额
     */
    private BigDecimal refundPrice;
}
