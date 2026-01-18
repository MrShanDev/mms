package ${package}.${moduleName}.controller;

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
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import ${package}.${moduleName}.entity.bo.${ClassName}Bo;
import ${package}.${moduleName}.entity.vo.${ClassName}Vo;
import ${package}.${moduleName}.entity.export.${ClassName}Export;
import ${package}.${moduleName}.service.${ClassName}Service;
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
 * ${tableComment}
 *
 * @author ${author}
 * @Doc ${website}
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("${moduleName}/${functionName}")
public class ${ClassName}Controller extends BaseController{
    private final ${ClassName}Service baseService;

    <#if formLayout==1 >
    /**
    * 分页列表-${tableComment}
    * @param bo 查询条件
    * @return 分页对象
    */
    @SaCheckPermission("${moduleName}:${functionName}:list")
    @PostMapping("/list")
    public TableDataInfo<${ClassName}Vo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) ${ClassName}Bo bo){
        return baseService.selectListVoPage(bo, bo.getPageQuery());
    }
    <#else>
    /**
    * 树结构列表
    * @param bo 查询参数
    * @return 树结构列表
    */
    @SaCheckPermission("${moduleName}:${functionName}:list")
    @PostMapping("/list")
    public R<List<${ClassName}Vo>> listPage(@RequestBody ThreeQueryBo bo) {
        return success(baseService.queryTree(bo.getIsAll(),bo.getShowLevel()));
    }
    </#if>

    /**
    * 根据id查询-${tableComment}
    * @param id ID
    * @return 对象
    */
    @SaCheckPermission("${moduleName}:${functionName}:query")
    @GetMapping("/{id}")
    public R<${ClassName}Vo> queryById(@PathVariable String id) {
        return success(baseService.selectVoById(id));
    }

    /**
    * 修改-${tableComment}
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "${tableComment?replace('管理', '')}管理",
        operType = OperationType.UPDATE,
        description = "修改${tableComment?replace('管理', '')}信息",
        saveBeforeData = true  // 开启修改前数据记录
    )
    @SaCheckPermission("${moduleName}:${functionName}:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) ${ClassName}Bo bo) {
        return success(baseService.updateById(bo));
    }

    /**
    * 新增-${tableComment}
    * @param bo 对象
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "${tableComment?replace('管理', '')}管理",
        operType = OperationType.INSERT,
        description = "新增${tableComment?replace('管理', '')}"
    )
    @SaCheckPermission("${moduleName}:${functionName}:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) ${ClassName}Bo bo) {
        return success(baseService.insert(bo));
    }

    /**
    * 删除-${tableComment}
    * @param ids ID
    * @return true:成功 false:失败
    */
    @MmsLog(
        module = "${tableComment?replace('管理', '')}管理",
        operType = OperationType.DELETE,
        description = "删除${tableComment?replace('管理', '')}"
    )
    @SaCheckPermission("${moduleName}:${functionName}:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return success(baseService.deleteById(ids));
    }

    /**
    * 模版下载-${tableComment}
    */
    @SaCheckPermission("${moduleName}:${functionName}:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, ${ClassName}Export.class, "${tableComment}");
    }

    /**
    * 导入-${tableComment}
    * @param file 模版文件
    */
    @MmsLog(
        module = "${tableComment?replace('管理', '')}管理",
        operType = OperationType.IMPORT,
        description = "导入${tableComment?replace('管理', '')}数据"
    )
    @MssSafety
    @SaCheckPermission("${moduleName}:${functionName}:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
        Set<${ClassName}Export> list=  ExcelUtil.imports(file, ${ClassName}Export.class);
        Boolean state= baseService.imports(list);
        return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
    * 导出-${tableComment}
    */
    @MmsLog(
        module = "${tableComment?replace('管理', '')}管理",
        operType = OperationType.EXPORT,
        description = "导出${tableComment?replace('管理', '')}数据"
    )
    @MssSafety
    @SaCheckPermission("${moduleName}:${functionName}:export")
    @PostMapping("/export")
    public void export(@Validated(ValidatedGroupConfig.query.class) ${ClassName}Bo bo,HttpServletResponse response) throws IOException {
        List<${ClassName}Vo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<${ClassName}Export> data= MapstructUtil.convert(list,${ClassName}Export.class);
        ExcelUtil.export(response, ${ClassName}Export.class, "${tableComment}",data);
    }

    /**
    * 打印-${tableComment}
    */
    @MssSafety
    @SaCheckPermission("${moduleName}:${functionName}:print")
    @PostMapping("/print")
    public R<PrintObject<${ClassName}Export>> print(@RequestBody @Validated(ValidatedGroupConfig.query.class) ${ClassName}Bo bo) throws Exception {
        List<${ClassName}Vo> list= baseService.selectListVoPage(bo, bo.getPageQuery()).getRows();
        List<${ClassName}Export> data= MapstructUtil.convert(list,${ClassName}Export.class);
        PrintObject<${ClassName}Export>  printObject=   new PrintObject<${ClassName}Export>()
             .setTitle("${tableComment}")
             .setData(data);
             return R.response(Boolean.TRUE,printObject);
    }
}
