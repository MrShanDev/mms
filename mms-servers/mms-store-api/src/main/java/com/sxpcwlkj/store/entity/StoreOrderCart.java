package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购物车表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_order_cart")
@EqualsAndHashCode(callSuper = true)
public class StoreOrderCart  extends BaseEntity {
	/**
	* 购物车项ID
	*/
	@TableId
	private String id;
	/**
	* 用户ID
	*/
	private String userId;
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
     *  SPU名称
     */
    private String spuName;
	/**
	* 商品单价
	*/
	private BigDecimal price;
	/**
	* 购买数量
	*/
	private Integer quantity;
	/**
	* 是否选中（0:未选中 1:已选中）
	*/
	private Integer selected;
	/**
	* 商品主图
	*/
	private String mainImage;
}
