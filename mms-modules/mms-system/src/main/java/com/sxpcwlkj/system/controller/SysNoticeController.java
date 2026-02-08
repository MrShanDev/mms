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
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.system.entity.bo.SysNoticeBo;
import com.sxpcwlkj.system.entity.export.SysNoticeExport;
import com.sxpcwlkj.system.entity.vo.SysNoticeVo;
import com.sxpcwlkj.system.service.SysNoticeService;
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
 * 系统公告
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-系统公告",description = "系统管理模块-系统公告")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/notice")
public class SysNoticeController extends BaseController{
    private final SysNoticeService baseService;

    /**
    * 分页列表
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("system:notice:list")
    @PostMapping("/list")
    public R<PageResult<SysNoticeVo>> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysNoticeBo bo){
        return R.success(baseService.selectListVoPage(bo, bo.getPageQuery()).toPageResult());
    }

    /**
    * 根据id查询
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("system:notice:query")
    @GetMapping("/{id}")
    public R<SysNoticeVo> queryById(@PathVariable String id) {
        return R.success(baseService.selectVoById(id));
    }

    /**
    * 修改
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "通知管理",
        operType = OperationType.UPDATE,
        description = "修改通知信息",
        saveBeforeData = true  // 开启修改前数据记录
    )
    @SaCheckPermission("system:notice:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) SysNoticeBo bo) {
        return R.success(baseService.updateByIdBase(bo));
    }

    /**
    * 新增
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "通知管理",
        operType = OperationType.INSERT,
        description = "新增通知"
    )
    @SaCheckPermission("system:notice:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) SysNoticeBo bo) {
        return R.success(baseService.insert(bo));
    }

    /**
    * 删除
    * @param ids ID
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "通知管理",
        operType = OperationType.DELETE,
        description = "删除通知"
    )
    @SaCheckPermission("system:notice:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return R.success(baseService.deleteById(ids));
    }

    /**
    * 模版下载
    */
    @SaCheckPermission("system:notice:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, SysNoticeExport.class, "系统公告");
    }

    /**
    * 导入系统用户
    * @param file 模版文件
    */
    @MmsLog(
        module = "通知管理",
        operType = OperationType.IMPORT,
        description = "导入通知数据"
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:notice:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<SysNoticeExport> list=  ExcelUtil.imports(file, SysNoticeExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出系统用户
    */
    @MmsLog(
        module = "通知管理",
        operType = OperationType.EXPORT,
        description = "导出通知数据"
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:notice:export")
    @PostMapping("/export")
    public void export(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysNoticeBo bo,HttpServletResponse response) throws IOException {
        List<SysNoticeVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<SysNoticeExport> data= MapstructUtil.convert(list,SysNoticeExport.class);
        ExcelUtil.export(response, SysNoticeExport.class, "系统公告",data);
    }

    /**
    * 打印系统用户
    */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:notice:print")
    @PostMapping("/print")
    public R<PrintObject<SysNoticeExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysNoticeBo bo) throws Exception {
        List<SysNoticeVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<SysNoticeExport> data= MapstructUtil.convert(list,SysNoticeExport.class);
        PrintObject<SysNoticeExport>  printObject=   new PrintObject<SysNoticeExport>()
             .setTitle("系统公告")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
