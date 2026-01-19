package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品规格值关系表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_attr_value_spu")
@EqualsAndHashCode(callSuper = true)
public class StoreAttrValueSpu  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 商品ID
	*/
	private String spuId;
	/**
	* 属性值ID
	*/
	private String attrValueId;
	/**
	* 规格项ID
	*/
	private String attrKeyId;
	/**
	* 自定义属性值
	*/
	private String attrValueText;
	/**
	* 属性值图片
	*/
	private String attrImage;
}
