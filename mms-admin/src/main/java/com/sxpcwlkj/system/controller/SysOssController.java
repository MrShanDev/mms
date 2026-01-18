package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.oss.service.OssService;
import com.sxpcwlkj.system.entity.bo.SysOssBo;
import com.sxpcwlkj.system.entity.bo.SysOssConfigBo;
import com.sxpcwlkj.system.entity.vo.SysOssConfigVo;
import com.sxpcwlkj.system.entity.vo.SysOssVo;
import com.sxpcwlkj.system.service.SysOssConfigService;
import com.sxpcwlkj.system.service.SysOssService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 对象存储
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-对象存储",description = "系统管理模块-对象存储")
@RequestMapping("system/oss")
@RestController
@RequiredArgsConstructor
public class SysOssController extends BaseController {
    private final SysOssService baseService;
    private final FileStorageService fileStorageService;
    private final OssService ossService;
    private final SysOssConfigService sysOssConfigService;

    /**
     * 列表
     */
    @MssSafety
    @SaCheckPermission("system:oss:list")
    @PostMapping("/list")
    public TableDataInfo<SysOssVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) SysOssBo bo) {
        return baseService.selectPageList(bo, bo.getPageQuery());
    }


    /**
     * 查询
     *
     * @return
     */
    @SaCheckPermission("system:oss:query")
    @GetMapping("/{id}")
    public R<SysOssVo> queryById(@PathVariable Long id) {
        return success(baseService.selectVoById(id));
    }

    /**
     * 编辑
     *
     * @return
     */
    @MmsLog(
        module = "对象存储",
        operType = OperationType.UPDATE,
        description = "修改对象存储信息",
        saveBeforeData = true  // 开启修改前数据记录
    )
    @SaCheckPermission("system:oss:edit")
    @PutMapping
    public R<Boolean> edit(@Validated @RequestBody SysOssBo bo) {
        return success(baseService.updateById(bo));
    }

    /**
     * 新增
     *
     * @return vo
     */
    @MmsLog(
        module = "对象存储",
        operType = OperationType.INSERT,
        description = "新增对象存储文件"
    )
    @SaCheckPermission("system:oss:insert")
    @PostMapping
    public R<Boolean> insert(MultipartFile file) {
        FileInfo fileInfo=ossService.upload(file);
        SysOssBo sysOssBo = new SysOssBo();
        sysOssBo.setUrl(fileInfo.getUrl());
        sysOssBo.setFileName(fileInfo.getFilename());
        sysOssBo.setOriginalName(fileInfo.getOriginalFilename());
        sysOssBo.setFileSuffix(fileInfo.getExt());
        sysOssBo.setPlatform(fileInfo.getPlatform());
        sysOssBo.setContentType(fileInfo.getContentType());
        sysOssBo.setBasePath(fileInfo.getBasePath());
        sysOssBo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        sysOssBo.setSort(0);
        return success(baseService.insert(sysOssBo));
    }

    /**
     * 删除
     *
     * @return vo
     */
    @MmsLog(
        module = "对象存储",
        operType = OperationType.DELETE,
        description = "删除对象存储文件"
    )
    @SaCheckPermission("system:oss:delete")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        SysOssVo vo = baseService.selectVoById(id);
        if (vo != null) {
            boolean delete = fileStorageService.delete(vo.getUrl());
            if (!delete) {
                return R.fail("桶删除失败");
            }
        }
        return success(baseService.deleteById(id));
    }


    /**
     * 获取OSS配置的默认启用项
     * @return 配置项明细
     */
    @SaCheckRole("super_admin")
    @GetMapping("/config/queryDef")
    public R<String> queryDef() {
        return success("success",sysOssConfigService.queryDef());
    }

    /**
     * 获取OSS配置详情
     * @param key 配置项key
     * @return 配置项明细
     */
    @SaCheckRole("super_admin")
    @GetMapping("/config/{key}")
    public R<SysOssConfigVo> queryByKey(@PathVariable String key) {
        return success(sysOssConfigService.selectVoByKey(key));
    }

    /**
     * 编辑配置
     * @param bo 配置对象
     * @return 编辑结果
     */
    @SaCheckRole("super_admin")
    @PutMapping("/config/edit")
    public R<Boolean> edit(@Validated @RequestBody SysOssConfigBo bo) {
        return success(sysOssConfigService.updateByIdXml(bo));
    }

    /**
     * 上传文件
     */
    @SaCheckLogin
    @PostMapping("/elUpload")
    public R<FileInfo> elUpload(MultipartFile file) {
        return R.success(ossService.upload(file));
    }

    /**
     * 上传文件
     */
    @SaCheckLogin
    @PostMapping("/wangEditor")
    public R<FileInfo> wangEditor(MultipartFile file) {
        return R.success(ossService.upload(file));
    }

}
