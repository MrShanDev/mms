package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发货商品明细表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_shipment_item")
@EqualsAndHashCode(callSuper = true)
public class StoreShipmentItem  extends BaseEntity {
	/**
	* 主键
	*/
	@TableId
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
	private Date productionDate;
	/**
	* 有效期至
	*/
	private Date expiryDate;
}
