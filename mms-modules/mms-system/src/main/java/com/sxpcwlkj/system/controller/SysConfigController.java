package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.sms.service.SmsService;
import com.sxpcwlkj.system.entity.bo.SysConfigBo;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import com.sxpcwlkj.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 系统配置
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-系统配置",description = "系统管理模块-系统配置")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/config")
public class SysConfigController extends BaseController{

    private final SysConfigService baseService;

    /**
     * 分页查询
     *
     * @param bo 查询条件
     * @return 分页结果
     */
    @MssSafety
    @SaCheckPermission("system:config:list")
    @PostMapping("/list")
    public R<PageResult<SysConfigVo>> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysConfigBo bo){
         return R.success(baseService.selectListVoPage(bo, bo.getPageQuery()).toPageResult());
    }

    /**
     * 查询详情
     *
     * @param id 主键id
     * @return 查询结果
     */
    @SaCheckPermission("system:config:query")
    @GetMapping("/{id}")
    public R<SysConfigVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
     * 更新
     *
     * @param bo 更新实体
     * @return 更新结果
     */
    @MmsLog(
        module = "参数管理",
        operType = OperationType.UPDATE,
        description = "修改系统配置",
        saveBeforeData = true  // 开启修改前数据记录
    )
    @SaCheckPermission("system:config:edit")
    @PutMapping
    public R<Boolean> edit(@Validated @RequestBody SysConfigBo bo) {
        return success(baseService.updateByIdBase(bo));
    }

    /**
     * 新增
     *
     * @param bo 新增实体
     * @return 新增结果
     */
    @MmsLog(
        module = "参数管理",
        operType = OperationType.INSERT,
        description = "新增系统配置"
    )
    @SaCheckPermission("system:config:insert")
    @PostMapping
    public R<Boolean> insert(@Validated @RequestBody SysConfigBo bo) {
        return success(baseService.insert(bo));
    }

    /**
     * 删除
     *
     * @param ids 主键id
     * @return 删除结果
     */
    @MmsLog(
        module = "参数管理",
        operType = OperationType.DELETE,
        description = "删除系统配置"
    )
    @SaCheckPermission("system:config:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }


    //========================系统配置扩展========================
    /**
     * 系统配置回显
     * @param bos 查询对象
     * @return 配置集合
     */
    @SaCheckRole("super_admin")
    @PostMapping("/getConfigs")
    public R<List<SysConfigVo>> getConfigs(@Validated @RequestBody List<SysConfigBo> bos) {
        return success(baseService.selectByCodes(bos));
    }

    /**
     * 编辑系统配置
     * @param bos 配置集合
     * @return 编辑结果
     */
    @SaCheckRole("super_admin")
    @PostMapping("/configs")
    public R<Boolean> configs(@Validated @RequestBody List<SysConfigBo> bos) {
        return success(baseService.updateByCodes(bos));
    }

    @SaCheckRole("super_admin")
    @GetMapping("/sendSms/{phone}")
    public R<Object> sendSms(@PathVariable String phone) {
        return R.fail("测试短信接口已停用，请使用验证码接口并配置短信服务");
    }
}
