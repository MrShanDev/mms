package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退款日志
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_refund_operation_log")
@EqualsAndHashCode(callSuper = true)
public class StoreRefundOperationLog  extends BaseEntity {
	/**
	* 主键
	*/
	@TableId
	private String id;
	/**
	* 退款申请ID
	*/
	private String refundApplyId;
	/**
	* 操作类型
	*/
	private Integer operationType;
	/**
	* 操作描述
	*/
	private String operationDesc;
	/**
	* 操作人ID
	*/
	private String operatorId;
	/**
	* 操作人姓名
	*/
	private String operatorName;
	/**
	* 操作人角色
	*/
	private Integer operatorRole;
	/**
	* 操作时间
	*/
	private Date operationTime;
	/**
	* 额外数据
	*/
	private Object extraData;
}
