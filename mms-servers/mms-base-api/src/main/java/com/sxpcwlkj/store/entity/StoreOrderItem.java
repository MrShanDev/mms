package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单商品明细表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_order_item")
@EqualsAndHashCode(callSuper = true)
public class StoreOrderItem  extends BaseEntity {
	/**
	* 主键
	*/
	@TableId
	private String id;
	/**
	* 订单ID
	*/
	private String orderId;
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
	* SPU ID
	*/
	private String spuId;
	/**
	* 商品单价
	*/
	private BigDecimal price;
	/**
	* 购买数量
	*/
	private Integer quantity;
	/**
	* 商品总价
	*/
	private BigDecimal totalPrice;
	/**
	* 商品主图
	*/
	private String mainImage;
}
