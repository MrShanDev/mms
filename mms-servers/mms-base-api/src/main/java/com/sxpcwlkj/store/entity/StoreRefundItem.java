package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 退款商品明细表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_refund_item")
@EqualsAndHashCode(callSuper = true)
public class StoreRefundItem  extends BaseEntity {
	/**
	* 主键
	*/
	@TableId
	private String id;
	/**
	* 退款申请ID
	*/
	private String refundApplyId;
	/**
	* 订单明细ID
	*/
	private String orderItemId;
	/**
	* 商品SKU ID
	*/
	private String skuId;
	/**
	* SKU编码
	*/
	private String skuCode;
	/**
	* SKU名称
	*/
	private String skuName;
	/**
	* 商品单价
	*/
	private BigDecimal unitPrice;
	/**
	* 退款数量
	*/
	private Integer refundQuantity;
	/**
	* 退款金额
	*/
	private BigDecimal refundPrice;
	/**
	* 商品主图
	*/
	private String mainImage;
	/**
	* 商品退款原因
	*/
	private String refundReason;
	/**
	* 证据图片
	*/
	private Object evidenceImages;
}
