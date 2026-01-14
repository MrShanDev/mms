package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PrintObject;
import com.sxpcwlkj.common.code.entity.ThreeQueryBo;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.framework.utils.ExcelUtil;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.system.entity.bo.SysDeptBo;
import com.sxpcwlkj.system.entity.export.SysDeptExport;
import com.sxpcwlkj.system.entity.vo.SysDeptVo;
import com.sxpcwlkj.system.service.SysDeptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 系统部门
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-系统部门",description = "系统管理模块-系统部门")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/dept")
public class SysDeptController extends BaseController{
    private final SysDeptService baseService;

    /**
    * 树结构列表
    * @param bo 查询参数
    * @return 树结构列表
    */
    @SaCheckPermission("system:dept:list")
    @PostMapping("/list")
    public R<List<SysDeptVo>> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) ThreeQueryBo bo) {
        return success(baseService.queryTree(bo.getIsAll(),bo.getShowLevel()));
    }

    /**
    * 根据id查询
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("system:dept:query")
    @GetMapping("/{id}")
    public R<SysDeptVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
    * 修改
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "部门管理",
        operType = OperationType.UPDATE,
        description = "修改部门信息",
        saveBeforeData = true  // 开启修改前数据记录
    )
    @SaCheckPermission("system:dept:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) SysDeptBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
    * 新增
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "部门管理",
        operType = OperationType.INSERT,
        description = "新增部门"
    )
    @SaCheckPermission("system:dept:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) SysDeptBo bo) {
        return success(baseService.insert(bo));
    }

    /**
    * 删除
    * @param ids ID
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "部门管理",
        operType = OperationType.DELETE,
        description = "删除部门"
    )
    @SaCheckPermission("system:dept:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }

    /**
    * 模版下载
    */
    @SaCheckPermission("system:dept:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, SysDeptExport.class, "系统部门");
    }

    /**
    * 导入系统用户
    * @param file 模版文件
    */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:dept:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<SysDeptExport> list=  ExcelUtil.imports(file, SysDeptExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出系统用户
    */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:dept:export")
    @PostMapping("/export")
    public void export(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysDeptBo bo,HttpServletResponse response) throws IOException {
        List<SysDeptVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<SysDeptExport> data= MapstructUtil.convert(list,SysDeptExport.class);
        ExcelUtil.export(response, SysDeptExport.class, "系统部门",data);
    }

    /**
    * 打印系统用户
    */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:dept:print")
    @PostMapping("/print")
    public R<PrintObject<SysDeptExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysDeptBo bo) throws Exception {
        List<SysDeptVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<SysDeptExport> data= MapstructUtil.convert(list,SysDeptExport.class);
        PrintObject<SysDeptExport>  printObject=   new PrintObject<SysDeptExport>()
             .setTitle("系统部门")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
