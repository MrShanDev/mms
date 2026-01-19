package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreRefundApply;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
* 退款申请表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundApply.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundApplyBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 退款申请ID
	 */
	@NotBlank(message = "退款申请ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 订单ID
	 */
	@NotBlank(message = "订单ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderId;
	/**
	 * 订单编号
	 */
	@NotBlank(message = "订单编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderNo;
	/**
	 * 退款单号（唯一）
	 */
	@NotBlank(message = "退款单号（唯一）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundNo;
	/**
	 * 退款类型
	 */
	@NotNull(message = "退款类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer refundType;
	/**
	 * 退款原因
	 */
	@NotBlank(message = "退款原因不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundReason;
	/**
	 * 退款原因描述
	 */
	@NotBlank(message = "退款原因描述不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundReasonDesc;
	/**
	 * 申请退款金额
	 */
	@NotNull(message = "申请退款金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal refundAmount;
	/**
	 * 实际退款金额
	 */
	//@NotNull(message = "实际退款金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal actualRefundAmount;
	/**
	 * 申请时间
	 */
	@NotNull(message = "申请时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date applyTime;
	/**
	 * 申请人
	 */
	@NotBlank(message = "申请人不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String applyBy;
	/**
	 * 退款状态
	 */
	@NotNull(message = "退款状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer refundStatus;
	/**
	 * 审核时间
	 */
	//@NotNull(message = "审核时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date auditTime;
	/**
	 * 审核人
	 */
	//@NotBlank(message = "审核人不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String auditBy;
	/**
	 * 审核备注
	 */
	//@NotBlank(message = "备注不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String auditRemark;
	/**
	 * 退款时间
	 */
	//@NotNull(message = "退款时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date refundTime;
	/**
	 * 退款备注
	 */
	//@NotBlank(message = "退款备注不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundRemark;
	/**
	 * 退款失败原因
	 */
	//@NotBlank(message = "退款失败原因不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String failReason;
}
