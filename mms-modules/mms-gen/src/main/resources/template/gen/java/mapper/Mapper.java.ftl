package ${package}.${moduleName}.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import ${package}.${moduleName}.entity.${ClassName};
import ${package}.${moduleName}.entity.vo.${ClassName}Vo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* ${tableComment}-Mapper
*
* @author ${author}
* @Doc ${website}
*/
<#assign hasCreator=false hasDept=false>
<#list fieldList as field>
<#if field.attrName=="createdBy"><#assign hasCreator=true></#if>
<#if field.attrName=="deptId"><#assign hasDept=true></#if>
</#list>
<#if formLayout!=3 && hasCreator>
@com.sxpcwlkj.common.annotation.DataScope(table="${tableName}"<#if hasDept>, deptColumn="dept_id"</#if>)
</#if>
@Mapper
@Repository
public interface ${ClassName}Mapper extends BaseMapperPlus<${ClassName}, ${ClassName}Vo> {

}
