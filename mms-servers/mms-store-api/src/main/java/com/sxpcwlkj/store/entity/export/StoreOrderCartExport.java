package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreOrderCartVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 购物车表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreOrderCartVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderCartExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 购物车项ID
	 */
	@ExcelIgnore
    @ExcelProperty("购物车项ID")
	@PrintColumn(title = "购物车项ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 用户ID
	 */
    @ExcelProperty("用户ID")
	@PrintColumn(title = "用户ID", type = PrintTypeEnum.TEXT)
	private  String userId;
	/**
	 * 商品SKU ID
	 */
    @ExcelProperty("商品SKU ID")
	@PrintColumn(title = "商品SKU ID", type = PrintTypeEnum.TEXT)
	private  String skuId;
	/**
	 * SKU编码
	 */
    @ExcelProperty("SKU编码")
	@PrintColumn(title = "SKU编码", type = PrintTypeEnum.TEXT)
	private  String skuCode;
	/**
	 * SKU名称
	 */
    @ExcelProperty("SKU名称")
	@PrintColumn(title = "SKU名称", type = PrintTypeEnum.TEXT)
	private  String skuName;
	/**
	 * SPU ID
	 */
    @ExcelProperty("SPU ID")
	@PrintColumn(title = "SPU ID", type = PrintTypeEnum.TEXT)
	private  String spuId;
	/**
	 * 商品单价
	 */
    @ExcelProperty("商品单价")
	@PrintColumn(title = "商品单价", type = PrintTypeEnum.TEXT)
	private  BigDecimal price;
	/**
	 * 购买数量
	 */
    @ExcelProperty("购买数量")
	@PrintColumn(title = "购买数量", type = PrintTypeEnum.TEXT)
	private  Integer quantity;
	/**
	 * 是否选中（0:未选中 1:已选中）
	 */
    @ExcelProperty("是否选中（0:未选中 1:已选中）")
	@PrintColumn(title = "是否选中（0:未选中 1:已选中）", type = PrintTypeEnum.TEXT)
	private  Integer selected;
	/**
	 * 商品主图
	 */
    @ExcelProperty("商品主图")
	@PrintColumn(title = "商品主图", type = PrintTypeEnum.TEXT)
	private  String mainImage;
}
