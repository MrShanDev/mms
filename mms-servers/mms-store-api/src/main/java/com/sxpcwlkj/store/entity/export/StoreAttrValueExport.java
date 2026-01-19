package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreAttrValueVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 属性值表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAttrValueVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrValueExport  extends BaseEntityVo{
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
	 * 属性键ID
	 */
    @ExcelProperty("属性键ID")
	@PrintColumn(title = "属性键ID", type = PrintTypeEnum.TEXT)
	private  String attrKeyId;
	/**
	 * 属性值
	 */
    @ExcelProperty("属性值")
	@PrintColumn(title = "属性值", type = PrintTypeEnum.TEXT)
	private  String value;
	/**
	 * 颜色值
	 */
    @ExcelProperty("颜色值")
	@PrintColumn(title = "颜色值", type = PrintTypeEnum.TEXT)
	private  String color;
}
