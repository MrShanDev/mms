package com.sxpcwlkj.store.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreShipmentItem;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 发货商品明细表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreShipmentItem.class)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentItemVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 发货单ID
	 */
	private String shipmentId;
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
	 * 订单数量
	 */
	private Integer orderQuantity;
	/**
	 * 本次发货数量
	 */
	private Integer shippedQuantity;
	/**
	 * 批次号
	 */
	private String batchNo;
	/**
	 * 生产日期
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date productionDate;
	/**
	 * 有效期至
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date expiryDate;

}
