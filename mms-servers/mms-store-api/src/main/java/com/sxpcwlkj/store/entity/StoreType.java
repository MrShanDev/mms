package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺类型
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_type")
@EqualsAndHashCode(callSuper = true)
public class StoreType  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 名称
	*/
	private String storeTypeName;
	/**
	* 图标片
	*/
	private String storeTypeIcon;
	/**
	* 小图标
	*/
	private String storeTypeIco;
}
