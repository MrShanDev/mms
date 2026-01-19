package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠券适用商品关系表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_coupon_product")
@EqualsAndHashCode(callSuper = true)
public class StoreCouponProduct  extends BaseEntity {
	/**
	* 主键ID
	*/
	@TableId
	private String id;
	/**
	* 优惠券ID
	*/
	private String couponId;
	/**
	* 商品ID
	*/
	private String productId;
	/**
	* 商品名称(冗余字段)
	*/
	private String productName;
}
