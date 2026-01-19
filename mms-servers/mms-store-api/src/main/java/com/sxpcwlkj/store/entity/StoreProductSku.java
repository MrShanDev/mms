package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品存量价格
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_product_sku")
@EqualsAndHashCode(callSuper = true)
public class StoreProductSku  extends BaseEntity {
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
	* KU编码，唯一
	*/
	private String skuCode;
	/**
	* SKU名称(SPU名称+规格值)
	*/
	private String skuName;
	/**
	* 市场价格
	*/
	private BigDecimal price;
	/**
	* 成交价
	*/
	private BigDecimal costPrice;
	/**
	* 库存
	*/
	private Integer stock;
	/**
	* 库存预警值
	*/
	private Integer stockWarning;
	/**
	* 主图
	*/
	private String mainImage;
    /**
     *  货号
     */
    private  String code;
    /**
     *   重量
     */
    private String weight;
}
