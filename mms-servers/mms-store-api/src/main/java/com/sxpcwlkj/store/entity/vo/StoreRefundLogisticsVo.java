package com.sxpcwlkj.store.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreRefundLogistics;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 退货物流信息表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreRefundLogistics.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundLogisticsVo  extends BaseEntityVo{
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
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date shippingTime;
	/**
	 * 签收时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
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
