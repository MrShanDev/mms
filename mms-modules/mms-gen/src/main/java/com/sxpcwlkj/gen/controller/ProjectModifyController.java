package com.sxpcwlkj.gen.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.io.IoUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.entity.ProjectModifyEntity;
import com.sxpcwlkj.gen.service.ProjectModifyService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目名变更
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@RestController
@RequestMapping("gen/project")
@AllArgsConstructor
public class ProjectModifyController {
    private final ProjectModifyService projectModifyService;
    @SaCheckRole("super_admin")
    @GetMapping("page")
    public TableDataInfo<ProjectModifyEntity> page(GenQueryBo query) {
        return projectModifyService.page(query);
    }
    @SaCheckRole("super_admin")
    @GetMapping("{id}")
    public R<ProjectModifyEntity> get(@PathVariable("id") Long id) {
        ProjectModifyEntity entity = projectModifyService.getById(id);
        return R.success(entity);
    }
    @SaCheckRole("super_admin")
    @PostMapping
    public R<String> save(@RequestBody ProjectModifyEntity entity) {
        projectModifyService.save(entity);
        return R.success();
    }
    @SaCheckRole("super_admin")
    @PutMapping
    public R<String> update(@RequestBody  ProjectModifyEntity entity) {
        projectModifyService.updateById(entity);
        return R.success();
    }
    @SaCheckRole("super_admin")
    @DeleteMapping
    public R<String> delete(@RequestBody List<Long> idList) {
        projectModifyService.removeByIds(idList);
        return R.success();
    }

    /**
     * 源码下载
     */
    @SaCheckRole("super_admin")
    @GetMapping("download/{id}")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        // 项目信息
        ProjectModifyEntity project = projectModifyService.getById(id);
        byte[] data = projectModifyService.download(project);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + project.getModifyProjectName() + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }
}
