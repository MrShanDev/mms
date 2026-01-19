package com.sxpcwlkj.store.entity.vo;


import java.io.Serial;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreRefundItem;
import com.sxpcwlkj.framework.entity.BaseEntityVo;

import java.math.BigDecimal;

/**
* 退款商品明细表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreRefundItem.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundItemVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 退款申请ID
	 */
	private String refundApplyId;
	/**
	 * 订单明细ID
	 */
	private String orderItemId;
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
	 * 商品单价
	 */
	private BigDecimal unitPrice;
	/**
	 * 退款数量
	 */
	private Integer refundQuantity;
	/**
	 * 退款金额
	 */
	private BigDecimal refundPrice;
	/**
	 * 商品主图
	 */
	private String mainImage;
	/**
	 * 商品退款原因
	 */
	private String refundReason;
	/**
	 * 证据图片
	 */
	private Object evidenceImages;

    private String spuId;

}
