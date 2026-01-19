package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreProductSkuVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 商品存量价格Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreProductSkuVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductSkuExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ExcelIgnore
    @ExcelProperty("ID")
	@PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 商品ID
	 */
    @ExcelProperty("商品ID")
	@PrintColumn(title = "商品ID", type = PrintTypeEnum.TEXT)
	private  String spuId;
	/**
	 * KU编码，唯一
	 */
    @ExcelProperty("KU编码，唯一")
	@PrintColumn(title = "KU编码，唯一", type = PrintTypeEnum.TEXT)
	private  String skuCode;
	/**
	 * SKU名称(SPU名称+规格值)
	 */
    @ExcelProperty("SKU名称(SPU名称+规格值)")
	@PrintColumn(title = "SKU名称(SPU名称+规格值)", type = PrintTypeEnum.TEXT)
	private  String skuName;
	/**
	 * 价格
	 */
    @ExcelProperty("价格")
	@PrintColumn(title = "价格", type = PrintTypeEnum.TEXT)
	private  BigDecimal price;
	/**
	 * 成交价
	 */
    @ExcelProperty("成交价")
	@PrintColumn(title = "成交价", type = PrintTypeEnum.TEXT)
	private  BigDecimal costPrice;
	/**
	 * 库存
	 */
    @ExcelProperty("库存")
	@PrintColumn(title = "库存", type = PrintTypeEnum.TEXT)
	private  Integer stock;
	/**
	 * 库存预警值
	 */
    @ExcelProperty("库存预警值")
	@PrintColumn(title = "库存预警值", type = PrintTypeEnum.TEXT)
	private  Integer stockWarning;
	/**
	 * 主图
	 */
    @ExcelProperty("主图")
	@PrintColumn(title = "主图", type = PrintTypeEnum.TEXT)
	private  String mainImage;
}
