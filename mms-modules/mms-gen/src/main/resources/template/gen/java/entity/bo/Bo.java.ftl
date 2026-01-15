package ${package}.${moduleName}.entity.bo;

import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import org.springframework.format.annotation.DateTimeFormat;
import ${package}.${moduleName}.entity.${ClassName};
<#list importList as i>
import ${i!};
</#list>
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* ${tableComment}Bo
*
* @author ${author}
* @Doc ${website}
*/
@Data
@AutoMapper(target = ${ClassName}.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class ${ClassName}Bo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

<#list fieldList as field>
<#if !field.baseField>
	/**
	 * ${field.fieldComment}
	 */
	<#if field.formRequired>
		<#if field.primaryPk>
	${field.formValidator}(message = "${field.fieldComment}不能为空" ,groups = {ValidatedGroupConfig.update.class})
		<#else>
	${field.formValidator}(message = "${field.fieldComment}不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
		</#if>
	</#if>
	<#if field.queryFormType == 'date'>
	@DateTimeFormat(pattern=DateUtil.DATE_PATTERN)
	<#elseif field.queryFormType == 'datetime'>
	@DateTimeFormat(pattern=DateUtil.DATE_TIME_PATTERN)
	</#if>
	private ${field.attrType}<#if field.queryType == 'between'>[]</#if> ${field.attrName};
</#if>
</#list>
}
