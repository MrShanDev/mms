package com.sxpcwlkj.gen.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.entity.FieldTypeEntity;
import com.sxpcwlkj.gen.service.FieldTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;

/**
 * 字段类型管理
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@RestController
@RequestMapping("gen/fieldType")
@AllArgsConstructor
public class FieldTypeController {
    private final FieldTypeService fieldTypeService;
    @SaCheckRole("super_admin")
    @GetMapping("page")
    public TableDataInfo<FieldTypeEntity> page(GenQueryBo query) {
        return fieldTypeService.page(query);
    }
    @SaCheckRole("super_admin")
    @GetMapping("{id}")
    public R<FieldTypeEntity> get(@PathVariable("id") Long id) {
        FieldTypeEntity data = fieldTypeService.getById(id);
        return R.success(data);
    }
    @SaCheckRole("super_admin")
    @GetMapping("list")
    public R<Set<String>> list() {
        Set<String> set = fieldTypeService.getList();
        return R.success(set);
    }
    @SaCheckRole("super_admin")
    @PostMapping
    public R<String> save(@RequestBody FieldTypeEntity entity) {
        fieldTypeService.save(entity);
        return R.success();
    }
    @SaCheckRole("super_admin")
    @PutMapping
    public R<String> update(@RequestBody FieldTypeEntity entity) {
        fieldTypeService.updateById(entity);
        return R.success();
    }
    @SaCheckRole("super_admin")
    @DeleteMapping
    public R<String> delete(@RequestBody Long[] ids) {
        fieldTypeService.removeBatchByIds(Arrays.asList(ids));
        return R.success();
    }
}
