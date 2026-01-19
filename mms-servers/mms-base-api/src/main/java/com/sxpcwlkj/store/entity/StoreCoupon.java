package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券管理
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_coupon")
@EqualsAndHashCode(callSuper = true)
public class StoreCoupon  extends BaseEntity {
	/**
	* 主键ID
	*/
	@TableId
	private String id;
	/**
	* 优惠券码
	*/
	private String couponCode;
	/**
	* 优惠券名称
	*/
	private String couponName;
	/**
	* 面额
	*/
	private BigDecimal faceValue;
	/**
	* 到期时间
	*/
	private Date expiryTime;
	/**
	* 优惠券类型
	*/
	private Integer couponType;
	/**
	* 最低使用金额
	*/
	private BigDecimal minOrderAmount;
	/**
	* 使用次数限制
	*/
	private Integer usageLimit;
	/**
	* 已使用次数
	*/
	private Integer usedCount;
}
