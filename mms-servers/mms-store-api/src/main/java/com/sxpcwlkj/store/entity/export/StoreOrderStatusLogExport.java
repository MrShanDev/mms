package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreOrderStatusLogVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 订单状态流水表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreOrderStatusLogVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderStatusLogExport  extends BaseEntityVo{
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
	 * 订单ID
	 */
    @ExcelProperty("订单ID")
	@PrintColumn(title = "订单ID", type = PrintTypeEnum.TEXT)
	private  String orderId;
	/**
	 * 原状态
	 */
    @ExcelProperty("原状态")
	@PrintColumn(title = "原状态", type = PrintTypeEnum.TEXT)
	private  Integer oldStatus;
	/**
	 * 新状态
	 */
    @ExcelProperty("新状态")
	@PrintColumn(title = "新状态", type = PrintTypeEnum.TEXT)
	private  Integer newStatus;
	/**
	 * 操作人
	 */
    @ExcelProperty("操作人")
	@PrintColumn(title = "操作人", type = PrintTypeEnum.TEXT)
	private  String operator;
}
