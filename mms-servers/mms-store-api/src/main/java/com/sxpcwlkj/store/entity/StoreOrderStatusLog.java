package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单状态流水表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_order_status_log")
@EqualsAndHashCode(callSuper = true)
public class StoreOrderStatusLog  extends BaseEntity {
	/**
	* 主键
	*/
	@TableId
	private String id;
	/**
	* 订单ID
	*/
	private String orderId;
	/**
	* 原状态
	*/
	private Integer oldStatus;
	/**
	* 新状态
	*/
	private Integer newStatus;
	/**
	* 操作人
	*/
	private String operator;
}
