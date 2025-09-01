package com.sxpcwlkj.docAdmin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.common.code.entity.PrintObject;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.framework.utils.ExcelUtil;
import com.sxpcwlkj.common.code.entity.ThreeQueryBo;
import com.sxpcwlkj.docAdmin.entity.bo.DocAuthorizeUserBo;
import com.sxpcwlkj.docAdmin.entity.vo.DocAuthorizeUserVo;
import com.sxpcwlkj.docAdmin.entity.export.DocAuthorizeUserExport;
import com.sxpcwlkj.docAdmin.service.DocAuthorizeUserService;
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
 * 文档授权用户
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("docAdmin/docAuthorizeUser")
public class DocAuthorizeUserController extends BaseController{
    private final DocAuthorizeUserService baseService;

    /**
    * 分页列表-文档授权用户
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("docAdmin:docAuthorizeUser:list")
    @PostMapping("/list")
    public TableDataInfo<DocAuthorizeUserVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocAuthorizeUserBo bo){
        return baseService.selectListVoPage(bo, bo.getPageQuery());
    }

    /**
    * 根据id查询-文档授权用户
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("docAdmin:docAuthorizeUser:query")
    @GetMapping("/{id}")
    public R<DocAuthorizeUserVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
    * 修改-文档授权用户
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docAuthorizeUser:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) DocAuthorizeUserBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
    * 新增-文档授权用户
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docAuthorizeUser:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) DocAuthorizeUserBo bo) {
        return success(baseService.insert(bo));
    }

    /**
    * 删除-文档授权用户
    * @param ids ID
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docAuthorizeUser:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }

    /**
    * 模版下载-文档授权用户
    */
    @SaCheckPermission("docAdmin:docAuthorizeUser:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, DocAuthorizeUserExport.class, "文档授权用户");
    }

    /**
    * 导入-文档授权用户
    * @param file 模版文件
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docAuthorizeUser:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<DocAuthorizeUserExport> list=  ExcelUtil.imports(file, DocAuthorizeUserExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出-文档授权用户
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docAuthorizeUser:export")
    @PostMapping("/export")
    public void export(@Validated(ValidatedGroupConfig.query.class) DocAuthorizeUserBo bo,HttpServletResponse response) throws IOException {
        List<DocAuthorizeUserVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocAuthorizeUserExport> data= MapstructUtil.convert(list,DocAuthorizeUserExport.class);
        ExcelUtil.export(response, DocAuthorizeUserExport.class, "文档授权用户",data);
    }

    /**
    * 打印-文档授权用户
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docAuthorizeUser:print")
    @PostMapping("/print")
    public R<PrintObject<DocAuthorizeUserExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocAuthorizeUserBo bo) throws Exception {
        List<DocAuthorizeUserVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocAuthorizeUserExport> data= MapstructUtil.convert(list,DocAuthorizeUserExport.class);
        PrintObject<DocAuthorizeUserExport>  printObject=   new PrintObject<DocAuthorizeUserExport>()
             .setTitle("文档授权用户")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
