package ${package}.${moduleName}.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import ${package}.${moduleName}.entity.vo.${ClassName}Vo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
<#list importList as i>
import ${i!};
</#list>

/**
* ${tableComment}Export
*
* @author ${author}
* @Doc ${website}
*/
@Data
@AutoMapper(target = ${ClassName}Vo.class)
@EqualsAndHashCode(callSuper=false)
public class ${ClassName}Export  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

<#list fieldList as field>
<#if !field.baseField>
	/**
	 * ${field.fieldComment}
	 */
	<#if field.primaryPk>
	@ExcelIgnore
	</#if>
	<#if field.formDict!?length gt 0>
	@Dict("${field.formDict}")
	@ExcelProperty(value ="${field.fieldComment}",converter = DictExcelConverter.class)
	<#else>
    @ExcelProperty("${field.fieldComment}")
	</#if>
	@PrintColumn(title = "${field.fieldComment}", type = PrintTypeEnum.TEXT)
	private  ${field.attrType}<#if field.queryType == 'between'>[]</#if> ${field.attrName};
</#if>
</#list>
}
