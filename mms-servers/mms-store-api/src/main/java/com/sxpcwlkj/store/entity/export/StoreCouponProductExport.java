package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreCouponProductVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 优惠券适用商品关系表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreCouponProductVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreCouponProductExport  extends BaseEntityVo{
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
	 * 优惠券ID
	 */
    @ExcelProperty("优惠券ID")
	@PrintColumn(title = "优惠券ID", type = PrintTypeEnum.TEXT)
	private  String couponId;
	/**
	 * 商品ID
	 */
    @ExcelProperty("商品ID")
	@PrintColumn(title = "商品ID", type = PrintTypeEnum.TEXT)
	private  String productId;
	/**
	 * 商品名称(冗余字段)
	 */
    @ExcelProperty("商品名称(冗余字段)")
	@PrintColumn(title = "商品名称(冗余字段)", type = PrintTypeEnum.TEXT)
	private  String productName;
}
