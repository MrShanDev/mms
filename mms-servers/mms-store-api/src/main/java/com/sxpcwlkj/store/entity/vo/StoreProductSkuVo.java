package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreProductSku;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 商品存量价格Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreProductSku.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductSkuVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
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
	 * 价格
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
     * 重量
     */
    private String weight;
    /**
     * 货号
     */
    private String code;

}
