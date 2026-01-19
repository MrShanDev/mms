package com.sxpcwlkj.store.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.vo.StoreRefundApplyVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
* 退款申请表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundApplyVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundApplyExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 退款申请ID
	 */
	@ExcelIgnore
    @ExcelProperty("退款申请ID")
	@PrintColumn(title = "退款申请ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 订单ID
	 */
    @ExcelProperty("订单ID")
	@PrintColumn(title = "订单ID", type = PrintTypeEnum.TEXT)
	private  String orderId;
	/**
	 * 订单编号
	 */
    @ExcelProperty("订单编号")
	@PrintColumn(title = "订单编号", type = PrintTypeEnum.TEXT)
	private  String orderNo;
	/**
	 * 退款单号（唯一）
	 */
    @ExcelProperty("退款单号（唯一）")
	@PrintColumn(title = "退款单号（唯一）", type = PrintTypeEnum.TEXT)
	private  String refundNo;
	/**
	 * 退款类型
	 */
	@Dict("refund_type")
	@ExcelProperty(value ="退款类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "退款类型", type = PrintTypeEnum.TEXT)
	private  Integer refundType;
	/**
	 * 退款原因
	 */
	@Dict("refund_reason")
	@ExcelProperty(value ="退款原因",converter = DictExcelConverter.class)
	@PrintColumn(title = "退款原因", type = PrintTypeEnum.TEXT)
	private  String refundReason;
	/**
	 * 退款原因描述
	 */
    @ExcelProperty("退款原因描述")
	@PrintColumn(title = "退款原因描述", type = PrintTypeEnum.TEXT)
	private  String refundReasonDesc;
	/**
	 * 申请退款金额
	 */
    @ExcelProperty("申请退款金额")
	@PrintColumn(title = "申请退款金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal refundAmount;
	/**
	 * 实际退款金额
	 */
    @ExcelProperty("实际退款金额")
	@PrintColumn(title = "实际退款金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal actualRefundAmount;
	/**
	 * 申请时间
	 */
    @ExcelProperty("申请时间")
	@PrintColumn(title = "申请时间", type = PrintTypeEnum.TEXT)
	private  Date applyTime;
	/**
	 * 申请人
	 */
    @ExcelProperty("申请人")
	@PrintColumn(title = "申请人", type = PrintTypeEnum.TEXT)
	private  String applyBy;
	/**
	 * 退款状态
	 */
	@Dict("operation_type")
	@ExcelProperty(value ="退款状态",converter = DictExcelConverter.class)
	@PrintColumn(title = "退款状态", type = PrintTypeEnum.TEXT)
	private  Integer refundStatus;
	/**
	 * 审核时间
	 */
    @ExcelProperty("审核时间")
	@PrintColumn(title = "审核时间", type = PrintTypeEnum.TEXT)
	private  Date auditTime;
	/**
	 * 审核人
	 */
    @ExcelProperty("审核人")
	@PrintColumn(title = "审核人", type = PrintTypeEnum.TEXT)
	private  String auditBy;
	/**
	 * 审核备注
	 */
    @ExcelProperty("审核备注")
	@PrintColumn(title = "审核备注", type = PrintTypeEnum.TEXT)
	private  String auditRemark;
	/**
	 * 退款时间
	 */
    @ExcelProperty("退款时间")
	@PrintColumn(title = "退款时间", type = PrintTypeEnum.TEXT)
	private  Date refundTime;
	/**
	 * 退款备注
	 */
    @ExcelProperty("退款备注")
	@PrintColumn(title = "退款备注", type = PrintTypeEnum.TEXT)
	private  String refundRemark;
	/**
	 * 退款失败原因
	 */
    @ExcelProperty("退款失败原因")
	@PrintColumn(title = "退款失败原因", type = PrintTypeEnum.TEXT)
	private  String failReason;
}
