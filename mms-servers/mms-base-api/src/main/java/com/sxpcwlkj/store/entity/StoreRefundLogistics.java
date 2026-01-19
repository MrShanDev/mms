package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 退货物流信息表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_refund_logistics")
@EqualsAndHashCode(callSuper = true)
public class StoreRefundLogistics  extends BaseEntity {
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
	* 物流公司
	*/
	private String logisticsCompany;
	/**
	* 物流单号
	*/
	private String logisticsNo;
	/**
	* 寄件人姓名
	*/
	private String senderName;
	/**
	* 寄件人电话
	*/
	private String senderPhone;
	/**
	* 寄件地址
	*/
	private String senderAddress;
	/**
	* 收件人姓名
	*/
	private String receiverName;
	/**
	* 收件人电话
	*/
	private String receiverPhone;
	/**
	* 收件地址
	*/
	private String receiverAddress;
	/**
	* 寄出时间
	*/
	private Date shippingTime;
	/**
	* 签收时间
	*/
	private Date receiveTime;
	/**
	* 物流状态
	*/
	private Integer logisticsStatus;
	/**
	* 异常原因
	*/
	private String exceptionReason;
}
