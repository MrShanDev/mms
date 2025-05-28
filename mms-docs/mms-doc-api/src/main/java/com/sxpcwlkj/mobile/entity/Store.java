package com.sxpcwlkj.mobile.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
 * 店铺
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-01-26
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("store")
public class Store extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String storeId;
	/**
	* 店铺名称
	*/
	private String storeName;
	/**
	* 店铺类型;1:企业  2:个人
	*/
	private Integer storeType;
	/**
	* 营业状态;0.禁用 1.营业  2.休业
	*/
	private Integer businessState;
	/**
	* 店铺地址
	*/
	private String storeAddress;
	/**
	* 店铺经度
	*/
	private String storeLongitude;
	/**
	* 店铺维度
	*/
	private String storeLatitude;
	/**
	* 店铺logo
	*/
	private String storeLogo;
	/**
	* 店铺门头
	*/
	private String storeBgImg;
	/**
	* 店铺简介
	*/
	private String storeIntroduction;
	/**
	* 营业时间
	*/
	private String storeBusinessTime;
	/**
	* 店铺电话
	*/
	private String storePhone;

}
