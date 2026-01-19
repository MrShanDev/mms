package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 属性值表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_attr_value")
@EqualsAndHashCode(callSuper = true)
public class StoreAttrValue  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 属性键ID
	*/
	private String attrKeyId;
	/**
	* 属性值
	*/
	private String value;
	/**
	* 颜色值
	*/
	private String color;
}
