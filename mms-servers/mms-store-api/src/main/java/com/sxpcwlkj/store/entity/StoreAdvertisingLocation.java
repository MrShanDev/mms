package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广告位
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_advertising_location")
@EqualsAndHashCode(callSuper = true)
public class StoreAdvertisingLocation  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 广告位名称
	*/
	private String name;
	/**
	* 广告位高度
	*/
	private String height;
	/**
	* 广告位宽度
	*/
	private String width;
	/**
	* 广告位编码
	*/
	private String code;
	/**
	* 最大显示数量
	*/
	private Integer maxNum;
}
