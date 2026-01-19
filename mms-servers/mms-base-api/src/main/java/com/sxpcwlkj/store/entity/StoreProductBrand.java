package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品品牌
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_product_brand")
@EqualsAndHashCode(callSuper = true)
public class StoreProductBrand  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 品牌名称
	*/
	private String name;
	/**
	* 品牌LOGO
	*/
	private String logo;
	/**
	* 首字符
	*/
	private String firstLetter;
}
