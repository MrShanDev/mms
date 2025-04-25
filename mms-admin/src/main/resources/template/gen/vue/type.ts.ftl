import {BaseEntity} from "/@/types/global";

/**
* 对象实体Vo
* @extends {BaseEntity}
*/
export declare interface ${FunctionName}Vo extends BaseEntity {
<#list formList as field>
    <#if !(field.attrName == 'tenantId'
    ||field.attrName == 'sort'
    ||field.attrName == 'status'
    ||field.attrName == 'revision'
    ||field.attrName == 'remark'
    ||field.attrName == 'createdBy'
    ||field.attrName == 'createdTime'
    ||field.attrName == 'updatedBy'
    ||field.attrName == 'updatedTime')>
      ${field.attrName}: string|number<#sep>; </#sep>
    </#if>
</#list>
<#if formLayout==2 >
      ${tableId}s:string;
      children: ${FunctionName}Entity[];
</#if>
}

/**
* 对象实体Bo
* @extends {BaseEntity}
*/
export declare interface ${FunctionName}Bo extends BaseEntity {
<#list formList as field>
    <#if !(field.attrName == 'tenantId'
    ||field.attrName == 'sort'
    ||field.attrName == 'status'
    ||field.attrName == 'revision'
    ||field.attrName == 'remark'
    ||field.attrName == 'createdBy'
    ||field.attrName == 'createdTime'
    ||field.attrName == 'updatedBy'
    ||field.attrName == 'updatedTime')>
        ${field.attrName}: string|number<#sep>; </#sep>
    </#if>
</#list>
<#if formLayout==2 >
    ${tableId}s:string;
    children: ${FunctionName}Entity[];
</#if>
}
