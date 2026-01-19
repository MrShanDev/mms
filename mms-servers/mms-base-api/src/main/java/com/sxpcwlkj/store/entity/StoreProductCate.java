package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_product_cate")
@EqualsAndHashCode(callSuper = true)
public class StoreProductCate  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 父ID
	*/
	private String parentId;
	/**
	* 分类名称
	*/
	private String name;
	/**
	* 店铺ID
	*/
	private String storeId;
	/**
	* 分类图标
	*/
	private String cateIcon;
	/**
	* 分类背景图片
	*/
	private String cateBgImg;
	/**
	* 层级
	*/
	private Integer level;
}
