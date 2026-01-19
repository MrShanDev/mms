package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreOrderStatusLog;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 订单状态流水表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreOrderStatusLog.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderStatusLogBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@NotBlank(message = "主键不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 订单ID
	 */
	@NotBlank(message = "订单ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderId;
	/**
	 * 原状态
	 */
	@NotNull(message = "原状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer oldStatus;
	/**
	 * 新状态
	 */
	@NotNull(message = "新状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer newStatus;
	/**
	 * 操作人
	 */
	@NotBlank(message = "操作人不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operator;
}
