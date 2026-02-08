package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.code.entity.PrintObject;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.framework.utils.ExcelUtil;
import com.sxpcwlkj.system.entity.bo.SysLogBo;
import com.sxpcwlkj.system.entity.vo.SysLogVo;
import com.sxpcwlkj.system.entity.export.SysLogExport;
import com.sxpcwlkj.system.service.SysLogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 操作日志记录表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/sysLog")
public class SysLogController extends BaseController{
    private final SysLogService baseService;

    /**
    * 分页列表-操作日志记录表
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("system:sysLog:list")
    @PostMapping("/list")
    public R<PageResult<SysLogVo>> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysLogBo bo){
        return R.success(baseService.selectListVoPage(bo, bo.getPageQuery()).toPageResult());
    }

    /**
    * 根据id查询-操作日志记录表
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("system:sysLog:query")
    @GetMapping("/{id}")
    public R<SysLogVo> queryById(@PathVariable String id) {
        return R.success(baseService.selectVoById(id));
    }

    /**
    * 修改-操作日志记录表
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("system:sysLog:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) SysLogBo bo) {
        return R.success(baseService.updateByIdBase(bo));
    }

    /**
    * 新增-操作日志记录表
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("system:sysLog:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) SysLogBo bo) {
        return R.success(baseService.insert(bo));
    }

    /**
    * 删除-操作日志记录表
    * @param ids ID
    * @return true:成功 false:失败
    */
    @SaCheckPermission("system:sysLog:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return R.success(baseService.deleteById(ids));
    }

    /**
    * 模版下载-操作日志记录表
    */
    @SaCheckPermission("system:sysLog:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, SysLogExport.class, "操作日志记录表");
    }

    /**
    * 导入-操作日志记录表
    * @param file 模版文件
    */
    @MssSafety
    @SaCheckPermission("system:sysLog:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<SysLogExport> list=  ExcelUtil.imports(file, SysLogExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出-操作日志记录表
    */
    @MssSafety
    @SaCheckPermission("system:sysLog:export")
    @PostMapping("/export")
    public void export(@Validated(ValidatedGroupConfig.query.class) SysLogBo bo,HttpServletResponse response) throws IOException {
        List<SysLogVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<SysLogExport> data= MapstructUtil.convert(list,SysLogExport.class);
        ExcelUtil.export(response, SysLogExport.class, "操作日志记录表",data);
    }

    /**
    * 打印-操作日志记录表
    */
    @MssSafety
    @SaCheckPermission("system:sysLog:print")
    @PostMapping("/print")
    public R<PrintObject<SysLogExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysLogBo bo) throws Exception {
        List<SysLogVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<SysLogExport> data= MapstructUtil.convert(list,SysLogExport.class);
        PrintObject<SysLogExport>  printObject=   new PrintObject<SysLogExport>()
             .setTitle("操作日志记录表")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
