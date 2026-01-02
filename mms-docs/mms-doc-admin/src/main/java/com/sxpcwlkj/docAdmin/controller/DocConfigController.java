package com.sxpcwlkj.docAdmin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PrintObject;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.docAdmin.entity.bo.DocConfigBo;
import com.sxpcwlkj.docAdmin.entity.export.DocConfigExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocConfigVo;
import com.sxpcwlkj.docAdmin.service.DocConfigService;
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
 * 文档配置
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("docAdmin/docConfig")
public class DocConfigController extends BaseController{
    private final DocConfigService baseService;

    /**
    * 分页列表-文档配置
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("docAdmin:docConfig:list")
    @PostMapping("/list")
    public TableDataInfo<DocConfigVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocConfigBo bo){
        return baseService.selectListVoPage(bo, bo.getPageQuery());
    }

    /**
    * 根据id查询-文档配置
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("docAdmin:docConfig:query")
    @GetMapping("/{id}")
    public R<DocConfigVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
    * 修改-文档配置
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docConfig:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) DocConfigBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
    * 新增-文档配置
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docConfig:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) DocConfigBo bo) {
        return success(baseService.insert(bo));
    }

    /**
    * 删除-文档配置
    * @param ids ID
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docConfig:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }

    /**
    * 模版下载-文档配置
    */
    @SaCheckPermission("docAdmin:docConfig:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, DocConfigExport.class, "文档配置");
    }

    /**
    * 导入-文档配置
    * @param file 模版文件
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docConfig:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<DocConfigExport> list=  ExcelUtil.imports(file, DocConfigExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出-文档配置
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docConfig:export")
    @PostMapping("/export")
    public void export(@Validated(ValidatedGroupConfig.query.class) DocConfigBo bo,HttpServletResponse response) throws IOException {
        List<DocConfigVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocConfigExport> data= MapstructUtil.convert(list,DocConfigExport.class);
        ExcelUtil.export(response, DocConfigExport.class, "文档配置",data);
    }

    /**
    * 打印-文档配置
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docConfig:print")
    @PostMapping("/print")
    public R<PrintObject<DocConfigExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocConfigBo bo) throws Exception {
        List<DocConfigVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocConfigExport> data= MapstructUtil.convert(list,DocConfigExport.class);
        PrintObject<DocConfigExport>  printObject=   new PrintObject<DocConfigExport>()
             .setTitle("文档配置")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
