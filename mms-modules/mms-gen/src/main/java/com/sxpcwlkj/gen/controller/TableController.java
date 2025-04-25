package com.sxpcwlkj.gen.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.entity.TableEntity;
import com.sxpcwlkj.gen.entity.TableFieldEntity;
import com.sxpcwlkj.gen.service.TableFieldService;
import com.sxpcwlkj.gen.service.TableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据表管理
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("gen/table")
public class TableController extends BaseController {
    private final TableService baseService;
    private final TableFieldService tableFieldService;

    /**
     * 分页
     *
     * @param query 查询参数
     */
    @SaCheckRole("super_admin")
    @GetMapping("page")
    public TableDataInfo<TableEntity> page(GenQueryBo query) {
        return baseService.page(query);
    }

    /**
     * 获取表信息
     *
     * @param id 表ID
     */
    @SaCheckRole("super_admin")
    @GetMapping("{id}")
    public R<TableEntity> get(@PathVariable("id") Long id) {
        TableEntity table = baseService.selectVoById(id);
        // 获取表的字段
        List<TableFieldEntity> fieldList = tableFieldService.getByTableId(table.getId());
        table.setFieldList(fieldList);
        SystemCommonEnum.SUPER_ADMIN.getValue();
        return R.success(table);
    }

    /**
     * 修改
     *
     * @param table 表信息
     */
    @SaCheckRole("super_admin")
    @PutMapping
    public R<String> update(@RequestBody TableEntity table) {
        baseService.updateById(table);
        return R.success();
    }

    /**
     * 删除
     *
     * @param ids 表id数组
     */
    @SaCheckRole("super_admin")
    @DeleteMapping
    public R<String> delete(@RequestBody Long[] ids) {
        baseService.deleteBatchIds(ids);
        return R.success();
    }

    /**
     * 同步表结构
     *
     * @param id 表ID
     */
    @SaCheckRole("super_admin")
    @PostMapping("sync/{id}")
    public R<String> sync(@PathVariable("id") Long id) {
        baseService.sync(id);
        return R.success();
    }

    /**
     * 导入数据源中的表
     *
     * @param datasourceId  数据源ID
     * @param tableNameList 表名列表
     */
    @SaCheckRole("super_admin")
    @PostMapping("import/{datasourceId}")
    public R<String> tableImport(@PathVariable("datasourceId") Long datasourceId, @RequestBody List<String> tableNameList) {
        for (String tableName : tableNameList) {
           baseService.tableImport(datasourceId, tableName);
        }
        return R.success();
    }

    /**
     * 修改表字段数据
     *
     * @param tableId        表ID
     * @param tableFieldList 字段列表
     */
    @SaCheckRole("super_admin")
    @PutMapping("field/{tableId}")
    public R<String> updateTableField(@PathVariable("tableId") Long tableId, @RequestBody List<TableFieldEntity> tableFieldList) {
        tableFieldService.updateTableField(tableId, tableFieldList);
        return R.success();
    }

}
