package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.store.entity.vo.StoreShipmentVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
* 发货明细Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreShipmentVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 发货ID
	 */
	@ExcelIgnore
    @ExcelProperty("发货ID")
	@PrintColumn(title = "发货ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 订单ID
	 */
    @ExcelProperty("订单ID")
	@PrintColumn(title = "订单ID", type = PrintTypeEnum.TEXT)
	private  String orderId;
	/**
	 * 发货单号
	 */
    @ExcelProperty("发货单号")
	@PrintColumn(title = "发货单号", type = PrintTypeEnum.TEXT)
	private  String shipmentNo;
	/**
	 * 发货类型
	 */
	@Dict("shipment_type")
	@ExcelProperty(value ="发货类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "发货类型", type = PrintTypeEnum.TEXT)
	private  Integer shipmentType;
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
	 * 发货时间
	 */
    @ExcelProperty("发货时间")
	@PrintColumn(title = "发货时间", type = PrintTypeEnum.TEXT)
	private  Date shipmentTime;
	/**
	 * 预计送达时间
	 */
    @ExcelProperty("预计送达时间")
	@PrintColumn(title = "预计送达时间", type = PrintTypeEnum.TEXT)
	private  Date deliveryTime;
	/**
	 * 发件人ID
	 */
    @ExcelProperty("发件人ID")
	@PrintColumn(title = "发件人ID", type = PrintTypeEnum.TEXT)
	private  String senderId;
	/**
	 * 发件人姓名（冗余）
	 */
    @ExcelProperty("发件人姓名（冗余）")
	@PrintColumn(title = "发件人姓名（冗余）", type = PrintTypeEnum.TEXT)
	private  String senderName;
	/**
	 * 发件人电话（冗余）
	 */
    @ExcelProperty("发件人电话（冗余）")
	@PrintColumn(title = "发件人电话（冗余）", type = PrintTypeEnum.TEXT)
	private  String senderPhone;
	/**
	 * 发件地址（冗余）
	 */
    @ExcelProperty("发件地址（冗余）")
	@PrintColumn(title = "发件地址（冗余）", type = PrintTypeEnum.TEXT)
	private  String senderAddress;
	/**
	 * 收货人姓名
	 */
    @ExcelProperty("收货人姓名")
	@PrintColumn(title = "收货人姓名", type = PrintTypeEnum.TEXT)
	private  String receiverName;
	/**
	 * 收货人电话
	 */
    @ExcelProperty("收货人电话")
	@PrintColumn(title = "收货人电话", type = PrintTypeEnum.TEXT)
	private  String receiverPhone;
	/**
	 * 收货地址
	 */
    @ExcelProperty("收货地址")
	@PrintColumn(title = "收货地址", type = PrintTypeEnum.TEXT)
	private  String receiverAddress;
	/**
	 * 包裹重量（kg）
	 */
    @ExcelProperty("包裹重量（kg）")
	@PrintColumn(title = "包裹重量（kg）", type = PrintTypeEnum.TEXT)
	private  BigDecimal shipmentWeight;
	/**
	 * 包裹体积（m³）
	 */
    @ExcelProperty("包裹体积（m³）")
	@PrintColumn(title = "包裹体积（m³）", type = PrintTypeEnum.TEXT)
	private  BigDecimal shipmentVolume;
	/**
	 * 实际运费
	 */
    @ExcelProperty("实际运费")
	@PrintColumn(title = "实际运费", type = PrintTypeEnum.TEXT)
	private  BigDecimal freightAmount;
	/**
	 * 保价金额
	 */
    @ExcelProperty("保价金额")
	@PrintColumn(title = "保价金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal insuranceAmount;
	/**
	 * 包裹数量
	 */
    @ExcelProperty("包裹数量")
	@PrintColumn(title = "包裹数量", type = PrintTypeEnum.TEXT)
	private  Integer packageCount;
	/**
	 * 发货状态
	 */
	@Dict("shipmentStatus")
	@ExcelProperty(value ="发货状态",converter = DictExcelConverter.class)
	@PrintColumn(title = "发货状态", type = PrintTypeEnum.TEXT)
	private  Integer shipmentStatus;
	/**
	 * 异常原因
	 */
    @ExcelProperty("异常原因")
	@PrintColumn(title = "异常原因", type = PrintTypeEnum.TEXT)
	private  String exceptionReason;
	/**
	 * 签收人姓名
	 */
    @ExcelProperty("签收人姓名")
	@PrintColumn(title = "签收人姓名", type = PrintTypeEnum.TEXT)
	private  String signerName;
	/**
	 * 签收时间
	 */
    @ExcelProperty("签收时间")
	@PrintColumn(title = "签收时间", type = PrintTypeEnum.TEXT)
	private  Date signTime;
	/**
	 * 签收备注
	 */
    @ExcelProperty("签收备注")
	@PrintColumn(title = "签收备注", type = PrintTypeEnum.TEXT)
	private  String signRemark;
}
