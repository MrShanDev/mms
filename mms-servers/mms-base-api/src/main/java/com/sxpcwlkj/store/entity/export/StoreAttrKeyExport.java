package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeyVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 属性键表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAttrKeyVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrKeyExport  extends BaseEntityVo{
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
	 * 属性名
	 */
    @ExcelProperty("属性名")
	@PrintColumn(title = "属性名", type = PrintTypeEnum.TEXT)
	private  String name;
	/**
	 * 是否是销售属性;0-否,1-是
	 */
    @ExcelProperty("是否是销售属性;0-否,1-是")
	@PrintColumn(title = "是否是销售属性;0-否,1-是", type = PrintTypeEnum.TEXT)
	private  String isSale;
	/**
	 * 输入类型;1-下拉框,2-单行文本,3-多行文本
	 */
    @ExcelProperty("输入类型;1-下拉框,2-单行文本,3-多行文本")
	@PrintColumn(title = "输入类型;1-下拉框,2-单行文本,3-多行文本", type = PrintTypeEnum.TEXT)
	private  String inputType;
	/**
	 * 是否必填;0-否,1-是
	 */
    @ExcelProperty("是否必填;0-否,1-是")
	@PrintColumn(title = "是否必填;0-否,1-是", type = PrintTypeEnum.TEXT)
	private  String isRequired;
}
