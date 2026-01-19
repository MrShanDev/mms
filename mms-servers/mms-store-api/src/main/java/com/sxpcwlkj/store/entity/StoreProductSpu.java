package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 店铺商品
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_product_spu")
@EqualsAndHashCode(callSuper = true)
public class StoreProductSpu  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 店铺ID
	*/
	private String storeId;
	/**
	* 分类
	*/
	private String cateId;
	/**
	* 品牌ID
	*/
	private String brandId;
	/**
	* 标题
	*/
	private String title;
	/**
	* 副标题
	*/
	private String subTitle;
    /**
     *   单位
     */
    private String unit;
    /**
     *  市场价格
     */
    private BigDecimal marketPrice;
	/**
	* 主图
	*/
	private String mainImage;
	/**
	* 图片集
	*/
	private String listImages;
	/**
	* 标签集
	*/
	private String tagIds;
	/**
	* 商品描述1
	*/
	private String describeOne;
	/**
	* 商品描述2
	*/
	private String describeTwo;
	/**
	* 商品描述3
	*/
	private String describeThree;
	/**
	* 商品描述4
	*/
	private String describeFour;
	/**
	* 商品描述5
	*/
	private String describeFive;
	/**
	* 详情介绍
	*/
	private String detailHtml;
}
