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
import com.sxpcwlkj.docAdmin.entity.bo.DocOrderBo;
import com.sxpcwlkj.docAdmin.entity.vo.DocOrderVo;
import com.sxpcwlkj.docAdmin.entity.export.DocOrderExport;
import com.sxpcwlkj.docAdmin.service.DocOrderService;
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
 * 文档订单
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("docAdmin/docOrder")
public class DocOrderController extends BaseController{
    private final DocOrderService baseService;

    /**
    * 分页列表-文档订单
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("docAdmin:docOrder:list")
    @PostMapping("/list")
    public TableDataInfo<DocOrderVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocOrderBo bo){
        return baseService.selectListVoPage(bo, bo.getPageQuery());
    }

    /**
    * 根据id查询-文档订单
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("docAdmin:docOrder:query")
    @GetMapping("/{id}")
    public R<DocOrderVo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
    * 修改-文档订单
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docOrder:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) DocOrderBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
    * 新增-文档订单
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docOrder:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) DocOrderBo bo) {
        return success(baseService.insert(bo));
    }

    /**
    * 删除-文档订单
    * @param ids ID
    * @return true:成功 false:失败
    */
    @SaCheckPermission("docAdmin:docOrder:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }

    /**
    * 模版下载-文档订单
    */
    @SaCheckPermission("docAdmin:docOrder:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, DocOrderExport.class, "文档订单");
    }

    /**
    * 导入-文档订单
    * @param file 模版文件
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docOrder:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<DocOrderExport> list=  ExcelUtil.imports(file, DocOrderExport.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出-文档订单
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docOrder:export")
    @PostMapping("/export")
    public void export(@Validated(ValidatedGroupConfig.query.class) DocOrderBo bo,HttpServletResponse response) throws IOException {
        List<DocOrderVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocOrderExport> data= MapstructUtil.convert(list,DocOrderExport.class);
        ExcelUtil.export(response, DocOrderExport.class, "文档订单",data);
    }

    /**
    * 打印-文档订单
    */
    @MssSafety
    @SaCheckPermission("docAdmin:docOrder:print")
    @PostMapping("/print")
    public R<PrintObject<DocOrderExport>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) DocOrderBo bo) throws Exception {
        List<DocOrderVo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<DocOrderExport> data= MapstructUtil.convert(list,DocOrderExport.class);
        PrintObject<DocOrderExport>  printObject=   new PrintObject<DocOrderExport>()
             .setTitle("文档订单")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
