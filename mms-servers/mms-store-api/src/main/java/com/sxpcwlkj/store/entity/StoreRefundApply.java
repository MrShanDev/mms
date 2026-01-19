package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款申请表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_refund_apply")
@EqualsAndHashCode(callSuper = true)
public class StoreRefundApply  extends BaseEntity {
	/**
	* 退款申请ID
	*/
	@TableId
	private String id;
	/**
	* 订单ID
	*/
	private String orderId;
	/**
	* 订单编号
	*/
	private String orderNo;
	/**
	* 退款单号（唯一）
	*/
	private String refundNo;
	/**
	* 退款类型
	*/
	private Integer refundType;
	/**
	* 退款原因
	*/
	private String refundReason;
	/**
	* 退款原因描述
	*/
	private String refundReasonDesc;
	/**
	* 申请退款金额
	*/
	private BigDecimal refundAmount;
	/**
	* 实际退款金额
	*/
	private BigDecimal actualRefundAmount;
	/**
	* 申请时间
	*/
	private Date applyTime;
	/**
	* 申请人
	*/
	private String applyBy;
	/**
	* 售后状态：1:待审核2:审核通过3:审核拒绝4:买家退货，待卖家收货5:卖家确认收货6:卖家终止售后7:买家确认收货8:买家取消售后9:完成售后10:等待平台退款
	*/
	private Integer refundStatus;
	/**
	* 审核时间
	*/
	private Date auditTime;
	/**
	* 审核人
	*/
	private String auditBy;
	/**
	* 审核备注
	*/
	private String auditRemark;
	/**
	* 退款时间
	*/
	private Date refundTime;
	/**
	* 退款备注
	*/
	private String refundRemark;
	/**
	* 退款失败原因
	*/
	private String failReason;
}
