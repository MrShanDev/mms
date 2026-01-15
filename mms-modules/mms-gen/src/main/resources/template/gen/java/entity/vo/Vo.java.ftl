package ${package}.${moduleName}.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import ${package}.${moduleName}.entity.${ClassName};
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
<#list importList as i>
import ${i!};
</#list>

/**
* ${tableComment}Vo
*
* @author ${author}
* @Doc ${website}
*/

@Data
@AutoMapper(target = ${ClassName}.class)
@EqualsAndHashCode(callSuper=false)
public class ${ClassName}Vo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

<#list fieldList as field>
<#if !field.baseField>
	<#if field.fieldComment!?length gt 0>
	/**
	 * ${field.fieldComment}
	 */
	</#if>
	<#if field.attrType == 'Date'>
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	</#if>
	private ${field.attrType}<#if field.queryType == 'between'>[]</#if> ${field.attrName};
</#if>
</#list>
<#if formLayout==2 >
    private String[] ${tableId}s;
    List<${ClassName}Vo> children;
</#if>

}
