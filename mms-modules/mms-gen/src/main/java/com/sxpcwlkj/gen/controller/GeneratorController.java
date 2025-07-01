package com.sxpcwlkj.gen.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.io.IoUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.gen.entity.Preview;
import com.sxpcwlkj.gen.service.GeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@RestController
@RequestMapping("gen/generator")
@AllArgsConstructor
public class GeneratorController {
    private final GeneratorService generatorService;

    /**
     * 生成代码（zip压缩包）
     */
    @SaCheckRole("super_admin")
    @PostMapping("download")
    public void download(String tableIds, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        // 生成代码
        for (String tableId : tableIds.split(",")) {
            generatorService.downloadCode(Long.parseLong(tableId), zip);
        }

        IoUtil.close(zip);

        // zip压缩包数据
        byte[] data = outputStream.toByteArray();

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=MMS.zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }

    /**
     * 生成代码（自定义目录）
     */
    @SaCheckRole("super_admin")
    @ResponseBody
    @PostMapping("code")
    public R<String> code(@RequestBody Long[] tableIds) throws Exception {
        // 生成代码
        for (Long tableId : tableIds) {
            generatorService.generatorCode(tableId);
        }

        return R.success();
    }
    /**
     * 预览代码
     */
    @SaCheckRole("super_admin")
    @GetMapping("/preview/{tableId}")
    public R<List<Preview>> preview(@PathVariable("tableId") Long tableId) throws Exception {
        List<Preview> results = generatorService.preview(tableId);
        return R.success(results);
    }
}
