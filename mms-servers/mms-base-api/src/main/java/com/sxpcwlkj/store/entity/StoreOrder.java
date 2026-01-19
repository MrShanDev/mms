package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_order")
@EqualsAndHashCode(callSuper = true)
public class StoreOrder  extends BaseEntity {
	/**
	* 订单ID
	*/
	@TableId
	private String id;
	/**
	* 订单编号
	*/
	private String orderNo;
	/**
	* 用户ID
	*/
	private String userId;
	/**
	* 订单总金额
	*/
	private BigDecimal totalAmount;
	/**
	* 优惠金额
	*/
	private BigDecimal discountAmount;
    /**
     * 运费
     */
    private BigDecimal freightAmount;
    /**
     * 保险金额
     */
    private BigDecimal insuranceAmount;
    /**
     * 订单最终付款金额
     */
    private BigDecimal orderEndPrice;
	/**
	* 实际支付金额
	*/
	private BigDecimal payAmount;
	/**
	* 支付时间
	*/
	private Date payTime;
	/**
	* 支付方式
	*/
	private Integer payType;
    /**
     * 支付流水号
     */
    private String transactionNumber;
    /**
     * 收款账号
     */
    private String mchId;
    /**
     *  配送方式 2 自提 1 物流配送
     */
    private Integer  deliveryType;
	/**
	* 收货人姓名
	*/
	private String deliveryName;
	/**
	* 收货人电话
	*/
	private String deliveryPhone;
	/**
	* 收货地址
	*/
	private String deliveryAddress;
	/**
	 * 优惠券ID
	 */
	private String couponId;

}
