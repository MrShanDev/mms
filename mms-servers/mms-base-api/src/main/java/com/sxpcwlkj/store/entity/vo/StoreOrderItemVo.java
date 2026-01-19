package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreOrderItem;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 订单商品明细表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreOrderItem.class)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderItemVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 商品SKU ID
	 */
	private String skuId;
	/**
	 * SKU编码
	 */
	private String skuCode;
	/**
	 * SKU名称
	 */
	private String skuName;
	/**
	 * SPU ID
	 */
	private String spuId;
	/**
	 * 商品单价
	 */
	private BigDecimal price;
	/**
	 * 购买数量
	 */
	private Integer quantity;
	/**
	 * 商品总价
	 */
	private BigDecimal totalPrice;
	/**
	 * 商品主图
	 */
	private String mainImage;

}
