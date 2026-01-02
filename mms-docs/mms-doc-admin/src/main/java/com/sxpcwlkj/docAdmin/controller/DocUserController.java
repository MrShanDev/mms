package com.sxpcwlkj.docAdmin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PrintObject;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.docAdmin.entity.bo.DocUserBo;
import com.sxpcwlkj.docAdmin.entity.export.DocUserExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocUserVo;
import com.sxpcwlkj.docAdmin.service.DocUserService;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.framework.utils.ExcelUtil;
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
 * 文档用户
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("docAdmin/docUser")
public class DocUserController extends BaseController{
    private final DocUserService baseService;

    /**
    * 分页列表-文档用户
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("docAdmin:docUser:list")
    @PostMapping("/list")
    public TableDataInfo<DocUserVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocUserBo bo){
        return baseService.selectListVoPage(bo, bo.getPageQuery());
    }

    /**
    * 根据id查询-文档用户
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("docAdmin:docUser:query")
    @GetMapping("/{id}")
    public R<DocUserVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
    * 修改-文档用户
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docUser:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) DocUserBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
    * 新增-文档用户
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docUser:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) DocUserBo bo) {
        return success(baseService.insert(bo));
    }

    /**
    * 删除-文档用户
    * @param ids ID
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docUser:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }

    /**
    * 模版下载-文档用户
    */
    @SaCheckPermission("docAdmin:docUser:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, DocUserExport.class, "文档用户");
    }

    /**
    * 导入-文档用户
    * @param file 模版文件
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docUser:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<DocUserExport> list=  ExcelUtil.imports(file, DocUserExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出-文档用户
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docUser:export")
    @PostMapping("/export")
    public void export(@Validated(ValidatedGroupConfig.query.class) DocUserBo bo,HttpServletResponse response) throws IOException {
        List<DocUserVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocUserExport> data= MapstructUtil.convert(list,DocUserExport.class);
        ExcelUtil.export(response, DocUserExport.class, "文档用户",data);
    }

    /**
    * 打印-文档用户
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docUser:print")
    @PostMapping("/print")
    public R<PrintObject<DocUserExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocUserBo bo) throws Exception {
        List<DocUserVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocUserExport> data= MapstructUtil.convert(list,DocUserExport.class);
        PrintObject<DocUserExport>  printObject=   new PrintObject<DocUserExport>()
             .setTitle("文档用户")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
