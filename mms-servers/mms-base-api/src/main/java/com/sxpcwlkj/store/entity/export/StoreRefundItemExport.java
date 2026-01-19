package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreRefundItemVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 退款商品明细表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundItemVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundItemExport  extends BaseEntityVo{
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
	 * 订单明细ID
	 */
    @ExcelProperty("订单明细ID")
	@PrintColumn(title = "订单明细ID", type = PrintTypeEnum.TEXT)
	private  String orderItemId;
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
	 * 商品单价
	 */
    @ExcelProperty("商品单价")
	@PrintColumn(title = "商品单价", type = PrintTypeEnum.TEXT)
	private  BigDecimal unitPrice;
	/**
	 * 退款数量
	 */
    @ExcelProperty("退款数量")
	@PrintColumn(title = "退款数量", type = PrintTypeEnum.TEXT)
	private  Integer refundQuantity;
	/**
	 * 退款金额
	 */
    @ExcelProperty("退款金额")
	@PrintColumn(title = "退款金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal refundPrice;
	/**
	 * 商品主图
	 */
    @ExcelProperty("商品主图")
	@PrintColumn(title = "商品主图", type = PrintTypeEnum.TEXT)
	private  String mainImage;
	/**
	 * 商品退款原因
	 */
    @ExcelProperty("商品退款原因")
	@PrintColumn(title = "商品退款原因", type = PrintTypeEnum.TEXT)
	private  String refundReason;
	/**
	 * 证据图片
	 */
    @ExcelProperty("证据图片")
	@PrintColumn(title = "证据图片", type = PrintTypeEnum.TEXT)
	private  Object evidenceImages;
}
