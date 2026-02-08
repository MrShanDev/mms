package com.sxpcwlkj.gen.controller;

import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.entity.BaseClassEntity;
import com.sxpcwlkj.gen.service.BaseClassService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 基类管理
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@RestController
@RequestMapping("gen/baseClass")
@AllArgsConstructor
public class BaseClassController {
    private final BaseClassService baseClassService;

    @GetMapping("page")
    public R<PageResult<BaseClassEntity>> page(GenQueryBo query) {
        return R.success(baseClassService.page(query).toPageResult());
    }

    @GetMapping("list")
    public R<List<BaseClassEntity>> list() {
        List<BaseClassEntity> list = baseClassService.getList();
        return R.success(list);
    }

    @GetMapping("{id}")
    public R<BaseClassEntity> get(@PathVariable("id") Long id) {
        BaseClassEntity data = baseClassService.getById(id);
        return R.success(data);
    }

    @PostMapping
    public R<String> save(@RequestBody BaseClassEntity entity) {
        baseClassService.save(entity);
        return R.success();
    }

    @PutMapping
    public R<String> update(@RequestBody BaseClassEntity entity) {
        baseClassService.updateById(entity);
        return R.success();
    }

    @DeleteMapping
    public R<String> delete(@RequestBody Long[] ids) {
        baseClassService.removeBatchByIds(Arrays.asList(ids));
        return R.success();
    }
}
