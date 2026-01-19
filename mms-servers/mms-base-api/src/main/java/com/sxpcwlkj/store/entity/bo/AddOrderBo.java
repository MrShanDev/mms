package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddOrderBo {

    /**
     *  购物车IDs (购物车中有多个商品，只传入选中的购物车id)
     */
    @NotNull(message = "下单商品不能为空" ,groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private  List<String> cardIds;
    /**
     *  用户ID
     */
    private  String  userId;
    /**
     *  收货地址ID
     */
    @NotBlank(message = "请选择收货地址" ,groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private String  addressId;
    /**
     * 优惠券ID
     */
    private String  couponId;
    /**
     *  商品金额
     */
    private BigDecimal totalAmount;
    /**
     *  优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 运费
     */
    private BigDecimal freightAmount;
    /**
     *   实际支付金额
     */
    private BigDecimal payAmount;
    /**
     *  买家留言
     */
    private String buyerRemark;

    /**
     *  配送方式  1 物流配送 2 自提
     */
    private Integer  deliveryType;
}
