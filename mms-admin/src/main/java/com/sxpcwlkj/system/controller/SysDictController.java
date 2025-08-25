package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.system.entity.bo.SysDictBo;
import com.sxpcwlkj.system.entity.vo.SysDictVo;
import com.sxpcwlkj.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统字典
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@RequestMapping("system/dict")
@RestController
@RequiredArgsConstructor
public class SysDictController extends BaseController {

    private final SysDictService baseService;

    /**
     * 分页查询字典列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页条件
     * @return 分页结果
     */
    @MssSafety
    @SaCheckPermission("system:dict:list")
    @PostMapping("/list")
    public TableDataInfo<SysDictVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class)SysDictBo bo) {
        return baseService.selectPageList(bo, bo.getPageQuery());
    }


    /**
     * 获取字典详细信息
     *
     * @return 字典详细信息
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping("/{id}")
    public R<SysDictVo> queryById(@PathVariable Long id) {
        return success(baseService.selectVoById(id));
    }

    /**
     * 编辑字典
     * @param bo 字典信息
     * @return 成功、失败
     */
    @SaCheckPermission("system:dict:edit")
    @PutMapping
    public R<Boolean> edit(@Validated @RequestBody(required = false) SysDictBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
     * 新增字典
     * @param bo 字典信息
     * @return 成功、失败
     */
    @SaCheckPermission("system:dict:insert")
    @PostMapping
    public R<Boolean> insert(@Validated @RequestBody(required = false) SysDictBo bo) {
        return success(baseService.insert(bo));
    }

    /**
     * 删除字典
     * @param id 字典ID
     * @return vo
     */
    @SaCheckPermission("system:dict:delete")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return success(baseService.deleteById(id));
    }


}
