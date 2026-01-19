package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.store.entity.vo.StoreRefundOperationLogVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 退款日志Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundOperationLogVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundOperationLogExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ExcelIgnore
    @ExcelProperty("主键")
	@PrintColumn(title = "主键", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 退款申请ID
	 */
    @ExcelProperty("退款申请ID")
	@PrintColumn(title = "退款申请ID", type = PrintTypeEnum.TEXT)
	private  String refundApplyId;
	/**
	 * 操作类型
	 */
	@Dict("operation_type")
	@ExcelProperty(value ="操作类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "操作类型", type = PrintTypeEnum.TEXT)
	private  Integer operationType;
	/**
	 * 操作描述
	 */
    @ExcelProperty("操作描述")
	@PrintColumn(title = "操作描述", type = PrintTypeEnum.TEXT)
	private  String operationDesc;
	/**
	 * 操作人ID
	 */
    @ExcelProperty("操作人ID")
	@PrintColumn(title = "操作人ID", type = PrintTypeEnum.TEXT)
	private  String operatorId;
	/**
	 * 操作人姓名
	 */
    @ExcelProperty("操作人姓名")
	@PrintColumn(title = "操作人姓名", type = PrintTypeEnum.TEXT)
	private  String operatorName;
	/**
	 * 操作人角色
	 */
	@Dict("operator_role")
	@ExcelProperty(value ="操作人角色",converter = DictExcelConverter.class)
	@PrintColumn(title = "操作人角色", type = PrintTypeEnum.TEXT)
	private  Integer operatorRole;
	/**
	 * 操作时间
	 */
    @ExcelProperty("操作时间")
	@PrintColumn(title = "操作时间", type = PrintTypeEnum.TEXT)
	private  Date operationTime;
	/**
	 * 额外数据
	 */
    @ExcelProperty("额外数据")
	@PrintColumn(title = "额外数据", type = PrintTypeEnum.TEXT)
	private  Object extraData;
}
