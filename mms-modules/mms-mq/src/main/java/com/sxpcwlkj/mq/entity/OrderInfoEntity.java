package com.sxpcwlkj.mq.entity;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品DTO
 */
@Data
public class OrderInfoEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String orderId;           // 订单ID
    private String buyerId;           // 购买者ID
    private String buyerNo;           // 购买者编号
    private String productId;         // 商品ID
    private String productName;       // 商品名称
    private Integer quantity;         // 商品数量
    private BigDecimal price;         // 商品单价
    private BigDecimal totalAmount;   // 商品总金额
}
