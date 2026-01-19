package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.store.entity.vo.StoreCouponVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
* 优惠券管理Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreCouponVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreCouponExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ExcelIgnore
    @ExcelProperty("主键ID")
	@PrintColumn(title = "主键ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 优惠券码
	 */
    @ExcelProperty("优惠券码")
	@PrintColumn(title = "优惠券码", type = PrintTypeEnum.TEXT)
	private  String couponCode;
	/**
	 * 优惠券名称
	 */
    @ExcelProperty("优惠券名称")
	@PrintColumn(title = "优惠券名称", type = PrintTypeEnum.TEXT)
	private  String couponName;
	/**
	 * 面额
	 */
    @ExcelProperty("面额")
	@PrintColumn(title = "面额", type = PrintTypeEnum.TEXT)
	private  BigDecimal faceValue;
	/**
	 * 到期时间
	 */
    @ExcelProperty("到期时间")
	@PrintColumn(title = "到期时间", type = PrintTypeEnum.TEXT)
	private  Date expiryTime;
	/**
	 * 优惠券类型
	 */
	@Dict("coupon_type")
	@ExcelProperty(value ="优惠券类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "优惠券类型", type = PrintTypeEnum.TEXT)
	private  Integer couponType;
	/**
	 * 最低使用金额
	 */
    @ExcelProperty("最低使用金额")
	@PrintColumn(title = "最低使用金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal minOrderAmount;
	/**
	 * 使用次数限制
	 */
    @ExcelProperty("使用次数限制")
	@PrintColumn(title = "使用次数限制", type = PrintTypeEnum.TEXT)
	private  Integer usageLimit;
	/**
	 * 已使用次数
	 */
    @ExcelProperty("已使用次数")
	@PrintColumn(title = "已使用次数", type = PrintTypeEnum.TEXT)
	private  Integer usedCount;
}
