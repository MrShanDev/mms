package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreRefundLogistics;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 退货物流信息表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundLogistics.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundLogisticsBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@NotBlank(message = "主键不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 退款申请ID
	 */
	//@NotBlank(message = "退款申请ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundApplyId;
    @NotBlank(message = "售后编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String refundNo;
	/**
	 * 物流公司
	 */
	@NotBlank(message = "物流公司不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String logisticsCompany;
	/**
	 * 物流单号
	 */
	@NotBlank(message = "物流单号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String logisticsNo;
	/**
	 * 寄件人姓名
	 */
	@NotBlank(message = "寄件人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderName;
	/**
	 * 寄件人电话
	 */
	@NotBlank(message = "寄件人电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderPhone;
	/**
	 * 寄件地址
	 */
	//@NotBlank(message = "寄件地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderAddress;
	/**
	 * 收件人姓名
	 */
	//@NotBlank(message = "收件人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String receiverName;
	/**
	 * 收件人电话
	 */
	//@NotBlank(message = "收件人电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String receiverPhone;
	/**
	 * 收件地址
	 */
	//@NotBlank(message = "收件地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String receiverAddress;
	/**
	 * 寄出时间
	 */
	@NotNull(message = "寄出时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date shippingTime;
	/**
	 * 签收时间
	 */
	//@NotNull(message = "签收时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date receiveTime;
	/**
	 * 物流状态
	 */
	//@NotNull(message = "物流状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer logisticsStatus;
	/**
	 * 异常原因
	 */
	//@NotBlank(message = "异常原因不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String exceptionReason;
}
