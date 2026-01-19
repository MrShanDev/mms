package com.sxpcwlkj.store.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreRefundOperationLog;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 退款日志Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreRefundOperationLog.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundOperationLogVo  extends BaseEntityVo{
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
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date operationTime;
	/**
	 * 额外数据
	 */
	private Object extraData;

}
