package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreRefundOperationLog;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 退款日志Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundOperationLog.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundOperationLogBo  extends BaseEntity {
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
	@NotBlank(message = "退款申请ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundApplyId;
	/**
	 * 操作类型 1:待审核2:审核通过3:审核拒绝4:买家退货，待卖家收货5:卖家确认收货6:卖家终止售后7:买家确认收货8:买家取消售后9:完成售后10:等待平台退款
	 */
	@NotNull(message = "操作类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer operationType;
	/**
	 * 操作描述
	 */
	@NotBlank(message = "操作描述不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operationDesc;
	/**
	 * 操作人ID
	 */
	@NotBlank(message = "操作人ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operatorId;
	/**
	 * 操作人姓名
	 */
	@NotBlank(message = "操作人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operatorName;
	/**
	 * 操作人角色 （1:用户 2:客服 3:管理员 4:系统）
	 */
	@NotNull(message = "操作人角色不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer operatorRole;
	/**
	 * 操作时间
	 */
	@NotNull(message = "操作时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date operationTime;
	/**
	 * 额外数据
	 */
	@NotBlank(message = "额外数据不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Object extraData;
}
