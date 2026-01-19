package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发货明细
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_shipment")
@EqualsAndHashCode(callSuper = true)
public class StoreShipment  extends BaseEntity {
	/**
	* 发货ID
	*/
	@TableId
	private String id;
	/**
	* 订单ID
	*/
	private String orderId;
    /**
     * 发货类型
     */
    private Integer deliveryType;
	/**
	* 发货单号
	*/
	private String shipmentNo;
	/**
	* 发货类型
	*/
	private Integer shipmentType;
	/**
	* 物流公司
	*/
	private String logisticsCompany;
	/**
	* 物流单号
	*/
	private String logisticsNo;
	/**
	* 发货时间
	*/
	private Date shipmentTime;
	/**
	* 预计送达时间
	*/
	private Date deliveryTime;
	/**
	* 发件人ID
	*/
	private String senderId;
	/**
	* 发件人姓名（冗余）
	*/
	private String senderName;
	/**
	* 发件人电话（冗余）
	*/
	private String senderPhone;
	/**
	* 发件地址（冗余）
	*/
	private String senderAddress;
	/**
	* 收货人姓名
	*/
	private String receiverName;
	/**
	* 收货人电话
	*/
	private String receiverPhone;
	/**
	* 收货地址
	*/
	private String receiverAddress;
	/**
	* 包裹重量（kg）
	*/
	private BigDecimal shipmentWeight;
	/**
	* 包裹体积（m³）
	*/
	private BigDecimal shipmentVolume;
	/**
	* 实际运费
	*/
	private BigDecimal freightAmount;
	/**
	* 保价金额
	*/
	private BigDecimal insuranceAmount;
	/**
	* 包裹数量
	*/
	private Integer packageCount;
	/**
	* 发货状态
	*/
	private Integer shipmentStatus;
	/**
	* 异常原因
	*/
	private String exceptionReason;
	/**
	* 签收人姓名
	*/
	private String signerName;
	/**
	* 签收时间
	*/
	private Date signTime;
	/**
	* 签收备注
	*/
	private String signRemark;
}
