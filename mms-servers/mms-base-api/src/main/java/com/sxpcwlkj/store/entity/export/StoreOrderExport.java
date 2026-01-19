package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.store.entity.vo.StoreOrderVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
* 订单主表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreOrderVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	@ExcelIgnore
    @ExcelProperty("订单ID")
	@PrintColumn(title = "订单ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 订单编号
	 */
    @ExcelProperty("订单编号")
	@PrintColumn(title = "订单编号", type = PrintTypeEnum.TEXT)
	private  String orderNo;
	/**
	 * 用户ID
	 */
    @ExcelProperty("用户ID")
	@PrintColumn(title = "用户ID", type = PrintTypeEnum.TEXT)
	private  String userId;
	/**
	 * 订单总金额
	 */
    @ExcelProperty("订单总金额")
	@PrintColumn(title = "订单总金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal totalAmount;
	/**
	 * 优惠金额
	 */
    @ExcelProperty("优惠金额")
	@PrintColumn(title = "优惠金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal discountAmount;
	/**
	 * 实际支付金额
	 */
    @ExcelProperty("实际支付金额")
	@PrintColumn(title = "实际支付金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal payAmount;
	/**
	 * 支付时间
	 */
    @ExcelProperty("支付时间")
	@PrintColumn(title = "支付时间", type = PrintTypeEnum.TEXT)
	private  Date payTime;
	/**
	 * 支付方式
	 */
	@Dict("PAY-TYPE")
	@ExcelProperty(value ="支付方式",converter = DictExcelConverter.class)
	@PrintColumn(title = "支付方式", type = PrintTypeEnum.TEXT)
	private  Integer payType;
	/**
	 * 收货人姓名
	 */
    @ExcelProperty("收货人姓名")
	@PrintColumn(title = "收货人姓名", type = PrintTypeEnum.TEXT)
	private  String deliveryName;
	/**
	 * 收货人电话
	 */
    @ExcelProperty("收货人电话")
	@PrintColumn(title = "收货人电话", type = PrintTypeEnum.TEXT)
	private  String deliveryPhone;
	/**
	 * 收货地址
	 */
    @ExcelProperty("收货地址")
	@PrintColumn(title = "收货地址", type = PrintTypeEnum.TEXT)
	private  String deliveryAddress;
}
