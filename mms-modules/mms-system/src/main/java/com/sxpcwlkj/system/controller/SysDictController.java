package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.system.entity.bo.SysDictBo;
import com.sxpcwlkj.system.entity.vo.SysDictVo;
import com.sxpcwlkj.system.service.SysDictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统字典
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-系统字典",description = "系统管理模块-系统字典")
@RequestMapping("system/dict")
@RestController
@RequiredArgsConstructor
public class SysDictController extends BaseController {

    private final SysDictService baseService;

    /**
     * 分页查询字典列表
     *
     * @param bo 查询条件
     * @return 分页结果
     */
    @MssSafety
    @SaCheckPermission("system:dict:list")
    @PostMapping("/list")
    public R<PageResult<SysDictVo>> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class)SysDictBo bo) {
        return R.success(baseService.selectPageList(bo, bo.getPageQuery()).toPageResult());
    }


    /**
     * 获取字典详细信息
     *
     * @return 字典详细信息
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping("/{id}")
    public R<SysDictVo> queryById(@PathVariable Long id) {
        return R.success(baseService.selectVoById(id));
    }

    /**
     * 编辑字典
     * @param bo 字典信息
     * @return 成功、失败
     */
    @MmsLog(
        module = "字典管理",
        operType = OperationType.UPDATE,
        description = "修改字典信息",
        saveBeforeData = true  // 开启修改前数据记录
    )
    @SaCheckPermission("system:dict:edit")
    @PutMapping
    public R<Boolean> edit(@Validated @RequestBody(required = false) SysDictBo bo) {
        return R.success(baseService.updateByIdBase(bo));
    }

    /**
     * 新增字典
     * @param bo 字典信息
     * @return 成功、失败
     */
    @MmsLog(
        module = "字典管理",
        operType = OperationType.INSERT,
        description = "新增字典"
    )
    @SaCheckPermission("system:dict:insert")
    @PostMapping
    public R<Boolean> insert(@Validated @RequestBody(required = false) SysDictBo bo) {
        return R.success(baseService.insert(bo));
    }

    /**
     * 删除字典
     * @param id 字典ID
     * @return vo
     */
    @MmsLog(
        module = "字典管理",
        operType = OperationType.DELETE,
        description = "删除字典"
    )
    @SaCheckPermission("system:dict:delete")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.success(baseService.deleteById(id));
    }


}
