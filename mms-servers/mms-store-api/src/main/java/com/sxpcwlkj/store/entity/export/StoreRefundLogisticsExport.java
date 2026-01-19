package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.store.entity.vo.StoreRefundLogisticsVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 退货物流信息表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundLogisticsVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundLogisticsExport  extends BaseEntityVo{
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
	 * 物流公司
	 */
    @ExcelProperty("物流公司")
	@PrintColumn(title = "物流公司", type = PrintTypeEnum.TEXT)
	private  String logisticsCompany;
	/**
	 * 物流单号
	 */
    @ExcelProperty("物流单号")
	@PrintColumn(title = "物流单号", type = PrintTypeEnum.TEXT)
	private  String logisticsNo;
	/**
	 * 寄件人姓名
	 */
    @ExcelProperty("寄件人姓名")
	@PrintColumn(title = "寄件人姓名", type = PrintTypeEnum.TEXT)
	private  String senderName;
	/**
	 * 寄件人电话
	 */
    @ExcelProperty("寄件人电话")
	@PrintColumn(title = "寄件人电话", type = PrintTypeEnum.TEXT)
	private  String senderPhone;
	/**
	 * 寄件地址
	 */
    @ExcelProperty("寄件地址")
	@PrintColumn(title = "寄件地址", type = PrintTypeEnum.TEXT)
	private  String senderAddress;
	/**
	 * 收件人姓名
	 */
    @ExcelProperty("收件人姓名")
	@PrintColumn(title = "收件人姓名", type = PrintTypeEnum.TEXT)
	private  String receiverName;
	/**
	 * 收件人电话
	 */
    @ExcelProperty("收件人电话")
	@PrintColumn(title = "收件人电话", type = PrintTypeEnum.TEXT)
	private  String receiverPhone;
	/**
	 * 收件地址
	 */
    @ExcelProperty("收件地址")
	@PrintColumn(title = "收件地址", type = PrintTypeEnum.TEXT)
	private  String receiverAddress;
	/**
	 * 寄出时间
	 */
    @ExcelProperty("寄出时间")
	@PrintColumn(title = "寄出时间", type = PrintTypeEnum.TEXT)
	private  Date shippingTime;
	/**
	 * 签收时间
	 */
    @ExcelProperty("签收时间")
	@PrintColumn(title = "签收时间", type = PrintTypeEnum.TEXT)
	private  Date receiveTime;
	/**
	 * 物流状态
	 */
	@Dict("shipmentStatus")
	@ExcelProperty(value ="物流状态",converter = DictExcelConverter.class)
	@PrintColumn(title = "物流状态", type = PrintTypeEnum.TEXT)
	private  Integer logisticsStatus;
	/**
	 * 异常原因
	 */
    @ExcelProperty("异常原因")
	@PrintColumn(title = "异常原因", type = PrintTypeEnum.TEXT)
	private  String exceptionReason;
}
