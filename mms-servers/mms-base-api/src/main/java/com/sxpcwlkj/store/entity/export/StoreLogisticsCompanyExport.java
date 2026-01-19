package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreLogisticsCompanyVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 快递公司名称Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreLogisticsCompanyVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreLogisticsCompanyExport  extends BaseEntityVo{
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
	 * 快递公司简称
	 */
    @ExcelProperty("快递公司简称")
	@PrintColumn(title = "快递公司简称", type = PrintTypeEnum.TEXT)
	private  String code;
	/**
	 * 快递公司名称
	 */
    @ExcelProperty("快递公司名称")
	@PrintColumn(title = "快递公司名称", type = PrintTypeEnum.TEXT)
	private  String name;
	/**
	 * 快递公司类型
	 */
    @ExcelProperty("快递公司类型")
	@PrintColumn(title = "快递公司类型", type = PrintTypeEnum.TEXT)
	private  String type;
	/**
	 * 快递公司编号
	 */
    @ExcelProperty("快递公司编号")
	@PrintColumn(title = "快递公司编号", type = PrintTypeEnum.TEXT)
	private  String number;
}
