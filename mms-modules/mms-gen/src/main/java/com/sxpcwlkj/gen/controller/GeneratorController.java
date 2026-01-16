package com.sxpcwlkj.gen.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.io.IoUtil;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.gen.entity.Preview;
import com.sxpcwlkj.gen.service.GeneratorService;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.redis.RedisUtil;
import cn.hutool.crypto.digest.DigestUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
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
    @GetMapping("/preview/{tableId}")
    public R<List<Preview>> preview(@PathVariable("tableId") Long tableId) throws Exception {
        List<Preview> results = generatorService.preview(tableId);
        return R.success(results);
    }

    /**
     * 执行生成的SQL（仅限DML操作）
     */
    @MmsLog
    @MssSafety
    @SaCheckRole("super_admin")
    @PostMapping("/executeSql")
    public R<Object> executeSql(@RequestBody Map<String, Object> map) {
        Long tableId = map.get("tableId") != null ? Long.valueOf(map.get("tableId").toString()) : null;
        Long datasourceId = map.get("datasourceId") != null ? Long.valueOf(map.get("datasourceId").toString()) : null;
        String sql = map.get("sql").toString();

        // 生成 SQL 哈希值作为去重 key
        String sqlHash = DigestUtil.md5Hex(sql);
        String redisKey = "gen:sql:executed:" + sqlHash;

        // 检查 1 分钟内是否已执行过相同 SQL
        Object cached = RedisUtil.getCacheObject(redisKey);
        if (cached != null) {
            return R.fail("该 SQL 已在 1 分钟内执行过，请勿重复执行");
        }

        // 执行 SQL
        Object result = generatorService.executeSql(tableId, datasourceId, sql);

        // 执行成功后，将此 SQL 存入 Redis，设置 1 分钟过期
        RedisUtil.setCacheObject(redisKey, "executed", Duration.ofMinutes(1));

        return R.success(result);
    }
}
