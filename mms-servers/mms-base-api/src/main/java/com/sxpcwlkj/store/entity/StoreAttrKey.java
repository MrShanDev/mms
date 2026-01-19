package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 属性键表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_attr_key")
@EqualsAndHashCode(callSuper = true)
public class StoreAttrKey  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 属性名
	*/
	private String name;
	/**
	* 是否是销售属性;0-否,1-是
	*/
	private String isSale;
	/**
	* 输入类型;1-下拉框,2-单行文本,3-多行文本
	*/
	private String inputType;
	/**
	* 是否必填;0-否,1-是
	*/
	private String isRequired;
}
