package com.sxpcwlkj.gen.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.gen.config.template.GeneratorConfig;
import com.sxpcwlkj.gen.config.template.GeneratorInfo;
import com.sxpcwlkj.gen.config.template.TemplateInfo;
import com.sxpcwlkj.gen.entity.BaseClassEntity;
import com.sxpcwlkj.gen.entity.Preview;
import com.sxpcwlkj.gen.entity.TableEntity;
import com.sxpcwlkj.gen.entity.TableFieldEntity;
import com.sxpcwlkj.gen.service.*;
import com.sxpcwlkj.gen.utils.TemplateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 代码生成
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Service
@Slf4j
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {
    private final DataSourceService datasourceService;
    private final FieldTypeService fieldTypeService;
    private final BaseClassService baseClassService;
    private final GeneratorConfig generatorConfig;
    private final TableService tableService;
    private final TableFieldService tableFieldService;

    @Override
    public void downloadCode(Long tableId, ZipOutputStream zip) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);

        // 代码生成器信息
        GeneratorInfo generator = generatorConfig.getGeneratorConfig(Convert.toInt(dataModel.get("formLayout"),1));

        // 渲染模板并输出
        for (TemplateInfo template : generator.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            dataModel.put("website", generator.getDeveloper().getWebsite());
            String content = TemplateUtils.getContent(template.getTemplateContent(), dataModel);
            String path = TemplateUtils.getContent(template.getGeneratorPath(), dataModel);

            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(path));
                IoUtil.writeUtf8(zip, false, content);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                throw new MmsException("模板写入失败：" + path, e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generatorCode(Long tableId) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);

        // 代码生成器信息
        GeneratorInfo generator = generatorConfig.getGeneratorConfig(Convert.toInt(dataModel.get("formLayout"),1));

        // 渲染模板并输出
        for (TemplateInfo template : generator.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = TemplateUtils.getContent(template.getTemplateContent(), dataModel);
            String path = TemplateUtils.getContent(template.getGeneratorPath(), dataModel);

            FileUtil.writeUtf8String(content, path);
        }
    }

    /**
     * 获取渲染的数据模型
     *
     * @param tableId 表ID
     */
    private Map<String, Object> getDataModel(Long tableId) {
        // 表信息
        TableEntity table = tableService.selectVoById(tableId);
        List<TableFieldEntity> fieldList = tableFieldService.getByTableId(tableId);
        table.setFieldList(fieldList);

        // 数据模型
        Map<String, Object> dataModel = new HashMap<>();

        // 找出主键处理primaryPk=1的
        List<TableFieldEntity> primaryKeys = fieldList.stream().filter(TableFieldEntity::isPrimaryPk).toList();
        if(primaryKeys.isEmpty()){
            dataModel.put("tableId", "id");
            dataModel.put("TableId", "Id");
        }else {
            //将驼峰命名改为
            dataModel.put("tableId", DataUtil.underlineToCamel(primaryKeys.get(0).getFieldName(),false));
            dataModel.put("TableId", DataUtil.underlineToCamel(primaryKeys.get(0).getFieldName(),true));
        }
        dataModel.put("tableLabel", table.getTableLabel());
        List<TableFieldEntity> parentIds = fieldList.stream().filter(item->item.getAttrName().equals(table.getParentId())).toList();
        if(parentIds.isEmpty()){
            dataModel.put("tableParentId", "parentId");
            dataModel.put("TableParentId", "ParentId");
        }else {
            dataModel.put("tableParentId", DataUtil.underlineToCamel(parentIds.get(0).getFieldName(),false));
            dataModel.put("TableParentId", DataUtil.underlineToCamel(parentIds.get(0).getFieldName(),true));
        }

        dataModel.put("span", table.getSpan());

        // 获取数据库类型
        String dbType = datasourceService.getDatabaseProductName(table.getDatasourceId());
        dataModel.put("dbType", dbType);

        // 项目信息
        dataModel.put("package", table.getPackageName());
        dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
        dataModel.put("version", table.getVersion());
        dataModel.put("moduleName", table.getModuleName());
        dataModel.put("ModuleName", StrUtil.upperFirst(table.getModuleName()));
        dataModel.put("functionName", table.getFunctionName());
        dataModel.put("FunctionName", StrUtil.upperFirst(table.getFunctionName()));
        dataModel.put("formLayout", table.getFormLayout());
        dataModel.put("menuId", table.getMenuId());

        // 开发者信息
        dataModel.put("author", table.getAuthor());
        dataModel.put("email", table.getEmail());
        dataModel.put("datetime", DateUtil.format(new Date(), DateUtil.DATE_TIME_PATTERN));
        dataModel.put("date", DateUtil.format(new Date(), DateUtil.DATE_PATTERN));



        // 设置字段分类
        setFieldTypeList(dataModel, table);

        // 设置基类信息
        setBaseClass(dataModel, table);

        // 导入的包列表
        Set<String> importList = fieldTypeService.getPackageByTableId(table.getId());
        dataModel.put("importList", importList);

        // 表信息
        dataModel.put("tableName", table.getTableName());
        dataModel.put("tableComment", table.getTableComment());
        dataModel.put("className", StrUtil.lowerFirst(table.getClassName()));
        dataModel.put("ClassName", table.getClassName());
        dataModel.put("fieldList", table.getFieldList());

        // 生成路径
        dataModel.put("backendPath", table.getBackendPath());
        dataModel.put("frontendPath", table.getFrontendPath());

        return dataModel;
    }

    /**
     * 设置基类信息
     *
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setBaseClass(Map<String, Object> dataModel, TableEntity table) {
        if (table.getBaseclassId() == null) {
            return;
        }

        // 基类
        BaseClassEntity baseClass = baseClassService.getById(table.getBaseclassId());
        baseClass.setPackageName(baseClass.getPackageName());
        dataModel.put("baseClass", baseClass);

        // 基类字段
        String[] fields = baseClass.getFields().split(",");

        // 标注为基类字段
        for (TableFieldEntity field : table.getFieldList()) {
            if (ArrayUtil.contains(fields, field.getFieldName())) {
                field.setBaseField(true);
            }
        }
    }

    /**
     * 设置字段分类信息
     *
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setFieldTypeList(Map<String, Object> dataModel, TableEntity table) {
        // 主键列表 (支持多主键)
        List<TableFieldEntity> primaryList = new ArrayList<>();
        // 表单列表
        List<TableFieldEntity> formList = new ArrayList<>();
        // 网格列表
        List<TableFieldEntity> gridList = new ArrayList<>();
        // 查询列表
        List<TableFieldEntity> queryList = new ArrayList<>();

        List<String> fastList=new ArrayList<>();

        for (TableFieldEntity field : table.getFieldList()) {
            if (field.isPrimaryPk()) {
                primaryList.add(field);
            }
            if (field.isFormItem()) {
                formList.add(field);
            }
            if (field.isGridItem()) {
                gridList.add(field);
            }
            if (field.isQueryItem()) {
                queryList.add(field);
            }
            if(field.isFormItem()){
                fastList.add(field.getFormType());
            }
        }
        fastList=fastList.stream().distinct().collect(Collectors.toList());
        dataModel.put("primaryList", primaryList);
        dataModel.put("formList", formList);
        dataModel.put("gridList", gridList);
        dataModel.put("queryList", queryList);
        dataModel.put("fastList",fastList);
    }

    /**
     * 代码预览
     *
     * @param tableId 表ID
     * @return 预览内容
     */
    @Override
    public List<Preview> preview(Long tableId) {
        Map<String, Object> dataModel = getDataModel(tableId);
        // 代码生成器信息
        GeneratorInfo generator = generatorConfig.getGeneratorConfig(Convert.toInt(dataModel.get("formLayout"),1));
        return generator.getTemplates().stream().map(t -> {
            dataModel.put("templateName", t.getTemplateName());
            dataModel.put("website", generator.getDeveloper().getWebsite());
            String content = TemplateUtils.getContent(t.getTemplateContent(), dataModel);
            String fileName = t.getGeneratorPath().substring(t.getGeneratorPath().lastIndexOf("/") + 1);
            fileName = TemplateUtils.getContent(fileName, dataModel);
            return new Preview(fileName, content);
        }).collect(Collectors.toList());
    }
}
