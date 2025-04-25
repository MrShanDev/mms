package com.sxpcwlkj.gen.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sxpcwlkj.gen.common.service.impl.BaseServiceImpl;
import com.sxpcwlkj.gen.entity.FieldTypeEntity;
import com.sxpcwlkj.gen.entity.TableFieldEntity;
import com.sxpcwlkj.gen.enums.AutoFillEnum;
import com.sxpcwlkj.gen.mapper.TableFieldMapper;
import com.sxpcwlkj.gen.service.FieldTypeService;
import com.sxpcwlkj.gen.service.TableFieldService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 表字段
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Service
@AllArgsConstructor
public class TableFieldServiceImpl extends BaseServiceImpl<TableFieldMapper, TableFieldEntity> implements TableFieldService {
    private final FieldTypeService fieldTypeService;

    @Override
    public List<TableFieldEntity> getByTableId(Long tableId) {
        return baseMapper.getByTableId(tableId);
    }

    @Override
    public void deleteBatchTableIds(Long[] tableIds) {
        baseMapper.deleteBatchTableIds(tableIds);
    }

    @Override
    public void updateTableField(Long tableId, List<TableFieldEntity> tableFieldList) {
        // 更新字段数据
        int sort = 0;
        for (TableFieldEntity tableField : tableFieldList) {
            tableField.setSort(sort++);
            this.updateById(tableField);
        }
    }

    @Override
    public void initFieldList(List<TableFieldEntity> tableFieldList) {
        // 字段类型、属性类型映射
        Map<String, FieldTypeEntity> fieldTypeMap = fieldTypeService.getMap();
        int index = 0;
        for (TableFieldEntity field : tableFieldList) {
            field.setAttrName(StringUtils.underlineToCamel(field.getFieldName()));
            // 获取字段对应的类型
            FieldTypeEntity fieldTypeMapping = fieldTypeMap.get(field.getFieldType().toLowerCase());
            if (fieldTypeMapping == null) {
                // 没找到对应的类型，则为Object类型
                field.setAttrType("Object");
            } else {
                field.setAttrType(fieldTypeMapping.getAttrType());
                field.setPackageName(fieldTypeMapping.getPackageName());
            }

            field.setAutoFill(AutoFillEnum.DEFAULT.name());

            field.setQueryType("=");
            field.setQueryFormType("text");
            field.setFormType("text");
            if("status".equalsIgnoreCase(field.getFieldName())){
                field.setFormType("radio");
                field.setFormDict("SYS_STATE");
                field.setQueryFormType("select");
            }
            if("created_time".equalsIgnoreCase(field.getFieldName())||"updated_time".equalsIgnoreCase(field.getFieldName())){
                field.setFormType("datetime");
                field.setQueryFormType("datetime");
            }
            if("remark".equalsIgnoreCase(field.getFieldName())){
                field.setFormType("textarea");
            }
            if("sort".equalsIgnoreCase(field.getFieldName())){
                field.setGridSort(true);
            }

            field.setSort(index++);

            String[] fileNameList=new String[]{"tenant_id","created_by","created_time","updated_by","updated_time","revision","remark"};
            boolean baseField=true;
            for(String fileName:fileNameList){
                if(field.getFieldName().equalsIgnoreCase(fileName)){
                    field.setBaseField(true);
                    baseField=false;
                }
            }

            //表单项
            field.setFormItem(baseField);
            //列表项
            field.setGridItem(baseField);
            //表单必填
            field.setFormRequired(baseField);
            //表单验证
            if(baseField){
                field.setFormValidator(
                        "VARCHAR".equalsIgnoreCase(field.getFieldType())?"@NotBlank":
                        "CHAR".equalsIgnoreCase(field.getFieldType())?"@NotBlank":
                        "TEXT".equalsIgnoreCase(field.getFieldType())?"@NotBlank":
                        "LONGTEXT".equalsIgnoreCase(field.getFieldType())?"@NotBlank":
                        "INT".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "INTEGER".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "DECIMAL".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "NUMERIC".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "DATE".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "DATETIME".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "BOOLEAN".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "BLOB".equalsIgnoreCase(field.getFieldType())?"@NotNull":
                        "@NotBlank"
                );
            }
        }
    }

}
