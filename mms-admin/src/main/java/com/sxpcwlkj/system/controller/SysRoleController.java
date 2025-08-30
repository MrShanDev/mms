package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.system.entity.bo.SysRoleBo;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;
import com.sxpcwlkj.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *  系统角色
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-系统角色",description = "系统管理模块-系统角色")
@RequestMapping("system/role")
@RestController
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final SysRoleService baseService;

    /**
     * 获取列表
     */
    @MssSafety
    @SaCheckPermission("system:role:list")
    @PostMapping("/list")
    public TableDataInfo<SysRoleVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysRoleBo bo) {
        return baseService.selectPageUserList(bo, bo.getPageQuery());
    }


    /**
     * 获取详细信息
     */
    @SaCheckPermission("system:role:query")
    @GetMapping("/{id}")
    public R<SysRoleVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
     * 编辑
     */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:role:edit")
    @PutMapping
    public R<Boolean> edit(@Validated @RequestBody SysRoleBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
     * 新增
     *
     * @return vo
     */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:role:insert")
    @PostMapping
    public R<Boolean> insert(@Validated @RequestBody SysRoleBo bo) {
        return success(baseService.insert(bo));
    }

    /**
     * 删除
     *
     * @return vo
     */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:role:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }


}
