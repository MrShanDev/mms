package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.system.entity.AdminMenuTree;
import com.sxpcwlkj.system.entity.bo.SysFunctionBo;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;
import com.sxpcwlkj.system.service.SysFunctionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统资源
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-系统资源",description = "系统管理模块-系统资源")
@RequestMapping("system/function")
@RestController
@RequiredArgsConstructor
public class SysFunctionController extends BaseController {

    private final SysFunctionService baseService;

    /**
     * 获取资源列表
     *
     * @return vo
     */
    @MssSafety
    @SaCheckPermission("system:function:list")
    @PostMapping("/list")
    public R<List<SysFunctionVo>> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysFunctionBo bo) {
        return R.success(baseService.selectPageList(bo, bo.getPageQuery()));
    }

    /**
     * 所有菜单
     */
    @SaCheckPermission("system:function:list")
    @PostMapping("/menuList")
    public R<List<AdminMenuTree>> menuList(){
        List<AdminMenuTree> functionTree = baseService.getAllMenuTree();
        return success(functionTree);
    }

    /**
     * 获取详细信息
     *
     * @return vo
     */
    @SaCheckPermission("system:function:query")
    @GetMapping("/{id}")
    public R<SysFunctionVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
     * 编辑资源
     *
     * @return vo
     */
    @MmsLog(
        module = "菜单管理",
        operType = OperationType.UPDATE,
        description = "修改菜单信息",
        saveBeforeData = true  // 开启修改前数据记录
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:function:edit")
    @PutMapping
    public R<Boolean> edit(@Validated @RequestBody SysFunctionBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
     * 新增资源
     *
     * @return vo
     */
    @MmsLog(
        module = "菜单管理",
        operType = OperationType.INSERT,
        description = "新增菜单"
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:function:insert")
    @PostMapping
    public R<Boolean> insert(@Validated @RequestBody SysFunctionBo bo) {
        return success(baseService.insert(bo));
    }

    /**
     * 删除
     *
     * @return vo
     */
    @MmsLog(
        module = "菜单管理",
        operType = OperationType.DELETE,
        description = "删除菜单"
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:function:delete")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return success(baseService.deleteById(id));
    }


}
