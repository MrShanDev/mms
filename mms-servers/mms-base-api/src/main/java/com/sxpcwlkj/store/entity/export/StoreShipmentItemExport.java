package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.vo.StoreShipmentItemVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 发货商品明细表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreShipmentItemVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentItemExport  extends BaseEntityVo{
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
	 * 发货单ID
	 */
    @ExcelProperty("发货单ID")
	@PrintColumn(title = "发货单ID", type = PrintTypeEnum.TEXT)
	private  String shipmentId;
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
	 * 订单数量
	 */
    @ExcelProperty("订单数量")
	@PrintColumn(title = "订单数量", type = PrintTypeEnum.TEXT)
	private  Integer orderQuantity;
	/**
	 * 本次发货数量
	 */
    @ExcelProperty("本次发货数量")
	@PrintColumn(title = "本次发货数量", type = PrintTypeEnum.TEXT)
	private  Integer shippedQuantity;
	/**
	 * 批次号
	 */
    @ExcelProperty("批次号")
	@PrintColumn(title = "批次号", type = PrintTypeEnum.TEXT)
	private  String batchNo;
	/**
	 * 生产日期
	 */
    @ExcelProperty("生产日期")
	@PrintColumn(title = "生产日期", type = PrintTypeEnum.TEXT)
	private  Date productionDate;
	/**
	 * 有效期至
	 */
    @ExcelProperty("有效期至")
	@PrintColumn(title = "有效期至", type = PrintTypeEnum.TEXT)
	private  Date expiryDate;
}
