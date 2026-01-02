package com.sxpcwlkj.docAdmin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PrintObject;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.docAdmin.entity.bo.DocProductBo;
import com.sxpcwlkj.docAdmin.entity.export.DocProductExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocProductVo;
import com.sxpcwlkj.docAdmin.service.DocProductService;
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
 * 文档商品
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("docAdmin/docProduct")
public class DocProductController extends BaseController{
    private final DocProductService baseService;

    /**
    * 分页列表-文档商品
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("docAdmin:docProduct:list")
    @PostMapping("/list")
    public TableDataInfo<DocProductVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocProductBo bo){
        return baseService.selectListVoPage(bo, bo.getPageQuery());
    }

    /**
    * 根据id查询-文档商品
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("docAdmin:docProduct:query")
    @GetMapping("/{id}")
    public R<DocProductVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
    * 修改-文档商品
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docProduct:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) DocProductBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
    * 新增-文档商品
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docProduct:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) DocProductBo bo) {
        return success(baseService.insert(bo));
    }

    /**
    * 删除-文档商品
    * @param ids ID
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docProduct:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }

    /**
    * 模版下载-文档商品
    */
    @SaCheckPermission("docAdmin:docProduct:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, DocProductExport.class, "文档商品");
    }

    /**
    * 导入-文档商品
    * @param file 模版文件
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docProduct:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<DocProductExport> list=  ExcelUtil.imports(file, DocProductExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出-文档商品
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docProduct:export")
    @PostMapping("/export")
    public void export(@Validated(ValidatedGroupConfig.query.class) DocProductBo bo,HttpServletResponse response) throws IOException {
        List<DocProductVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocProductExport> data= MapstructUtil.convert(list,DocProductExport.class);
        ExcelUtil.export(response, DocProductExport.class, "文档商品",data);
    }

    /**
    * 打印-文档商品
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docProduct:print")
    @PostMapping("/print")
    public R<PrintObject<DocProductExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocProductBo bo) throws Exception {
        List<DocProductVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocProductExport> data= MapstructUtil.convert(list,DocProductExport.class);
        PrintObject<DocProductExport>  printObject=   new PrintObject<DocProductExport>()
             .setTitle("文档商品")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
