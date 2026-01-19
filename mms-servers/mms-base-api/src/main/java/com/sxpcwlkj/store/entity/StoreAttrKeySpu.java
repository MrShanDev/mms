package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品规格项关系表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_attr_key_spu")
@EqualsAndHashCode(callSuper = true)
public class StoreAttrKeySpu  extends BaseEntity {
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
	* 属性ID
	*/
	private String attrKeyId;
}
