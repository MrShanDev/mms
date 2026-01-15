package ${package}.${moduleName}.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
<#list importList as i>
import ${i!};
</#list>
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ${tableComment}
 *
 * @author ${author}
 * @Doc ${website}
 */
@Data
@TableName("${tableName}")
@EqualsAndHashCode(callSuper = true)
public class ${ClassName}  extends BaseEntity {
<#list fieldList as field>
<#if !field.baseField>
	<#if field.fieldComment!?length gt 0>
	/**
	* ${field.fieldComment}
	*/
	</#if>
    <#if field.autoFill == "INSERT">
	@TableField(fill = FieldFill.INSERT)
	</#if>
	<#if field.autoFill == "INSERT_UPDATE">
	@TableField(fill = FieldFill.INSERT_UPDATE)
	</#if>
	<#if field.autoFill == "UPDATE">
	@TableField(fill = FieldFill.UPDATE)
	</#if>
    <#if field.primaryPk>
	@TableId
	</#if>
	<#if field.queryType == 'between'>
		@TableField(exist = false)
	</#if>
	private ${field.attrType}<#if field.queryType == 'between'>[]</#if> ${field.attrName};
</#if>
</#list>
}
