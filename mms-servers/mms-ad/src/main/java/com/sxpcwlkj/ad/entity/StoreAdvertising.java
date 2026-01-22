package com.sxpcwlkj.ad.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广告
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_advertising")
@EqualsAndHashCode(callSuper = true)
public class StoreAdvertising  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 广告位ID
	*/
	private String advertisingId;
	/**
	* 开始时间
	*/
	private String startTime;
	/**
	* 到期时间
	*/
	private String endTime;
	/**
	* 路由地址
	*/
	private String routeUrl;
	/**
	* 图片地址
	*/
	private String imageUrl;
	/**
	* 路由参数
	*/
	private String routeParameter;
	/**
	* 扩展参数一
	*/
	private String extendedParameterOne;
	/**
	* 扩展参数三
	*/
	private String extendedParameterThree;
	/**
	* 扩展参数二
	*/
	private String extendedParameterTwo;
	/**
	* 扩展参数四
	*/
	private String extendedParameterFour;
	/**
	* 扩展参数五
	*/
	private String extendedParameterFive;
}
