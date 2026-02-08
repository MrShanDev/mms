package com.sxpcwlkj.gen.controller;

import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.utils.R;
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
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@RestController
@RequestMapping("gen/fieldType")
@AllArgsConstructor
public class FieldTypeController {
    private final FieldTypeService fieldTypeService;

    @GetMapping("page")
    public R<PageResult<FieldTypeEntity>> page(GenQueryBo query) {
        return R.success(fieldTypeService.page(query).toPageResult());
    }

    @GetMapping("{id}")
    public R<FieldTypeEntity> get(@PathVariable("id") Long id) {
        FieldTypeEntity data = fieldTypeService.getById(id);
        return R.success(data);
    }

    @GetMapping("list")
    public R<Set<String>> list() {
        Set<String> set = fieldTypeService.getList();
        return R.success(set);
    }

    @PostMapping
    public R<String> save(@RequestBody FieldTypeEntity entity) {
        fieldTypeService.save(entity);
        return R.success();
    }

    @PutMapping
    public R<String> update(@RequestBody FieldTypeEntity entity) {
        fieldTypeService.updateById(entity);
        return R.success();
    }

    @DeleteMapping
    public R<String> delete(@RequestBody Long[] ids) {
        fieldTypeService.removeBatchByIds(Arrays.asList(ids));
        return R.success();
    }
}
