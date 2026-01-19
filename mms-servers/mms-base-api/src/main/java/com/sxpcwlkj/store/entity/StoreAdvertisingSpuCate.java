package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广告商品分类组合表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_advertising_spu_cate")
@EqualsAndHashCode(callSuper = true)
public class StoreAdvertisingSpuCate  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 广告ID
	*/
	private String advertisementId;
	/**
	* 商品分类ID
	*/
	private String spuCateId;
	/**
	* 商品数量
	*/
	private Integer productNum;
	/**
	* 组合类型
	*/
	private String typeCode;
}
