package com.sxpcwlkj.log.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.sxpcwlkj.log.entity.SysLog;
import com.sxpcwlkj.log.mapper.SysLogMapper;
import com.sxpcwlkj.log.service.SysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 操作日志服务实现
 *
 * @author mmsAdmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLogServiceImpl implements SysLogService {

    private final SysLogMapper operLogMapper;

    /**
     * 日志文件存储路径
     */
    private static final String LOG_FILE_PATH = "./logs/oper/";

    /**
     * 异步保存操作日志
     */
    @Async("operLogExecutor")
    @Override
    public void saveLog(SysLog operLog) {
        try {
            // 这里不需要任何处理,由切面决定保存策略
        } catch (Exception e) {
            log.error("异步保存操作日志失败", e);
        }
    }

    /**
     * 保存到数据库
     */
    @Override
    public void saveToDatabase(SysLog operLog) {
        try {
            operLogMapper.insert(operLog);
            log.debug("操作日志已保存到数据库: {}", operLog.getDescription());
        } catch (Exception e) {
            log.error("保存操作日志到数据库失败", e);
            // 数据库保存失败时,尝试保存到文件
            saveToFile(operLog);
        }
    }

    /**
     * 保存到本地文件
     */
    @Override
    public void saveToFile(SysLog operLog) {
        try {
            // 确保目录存在
            FileUtil.mkdir(LOG_FILE_PATH);

            // 按日期生成文件名
            String date = DateUtil.format(operLog.getOperTime(), "yyyy-MM-dd");
            String fileName = LOG_FILE_PATH + "oper-log-" + date + ".log";
            File logFile = new File(fileName);

            // 构建日志内容
            StringBuilder logContent = new StringBuilder();
            logContent.append("【操作时间】").append(DateUtil.format(operLog.getOperTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
            logContent.append("【租户ID】").append(operLog.getTenantId()).append("\n");
            logContent.append("【用户ID】").append(operLog.getUserId()).append("\n");
            logContent.append("【用户名】").append(operLog.getUserName()).append("\n");
            logContent.append("【用户角色】").append(operLog.getUserRoles()).append("\n");
            logContent.append("【模块名称】").append(operLog.getModule()).append("\n");
            logContent.append("【操作类型】").append(operLog.getOperType()).append("\n");
            logContent.append("【操作描述】").append(operLog.getDescription()).append("\n");
            logContent.append("【请求方法】").append(operLog.getRequestMethod()).append("\n");
            logContent.append("【操作方法】").append(operLog.getMethod()).append("\n");
            logContent.append("【请求URL】").append(operLog.getOperUrl()).append("\n");
            logContent.append("【操作IP】").append(operLog.getOperIp()).append("\n");
            logContent.append("【操作地点】").append(operLog.getOperLocation()).append("\n");
            logContent.append("【浏览器】").append(operLog.getBrowser()).append("\n");
            logContent.append("【操作系统】").append(operLog.getOs()).append("\n");
            logContent.append("【请求参数】").append(operLog.getOperParam()).append("\n");
            if (operLog.getBeforeData() != null) {
                logContent.append("【操作前数据】").append(operLog.getBeforeData()).append("\n");
            }
            logContent.append("【返回结果】").append(operLog.getJsonResult()).append("\n");
            logContent.append("【操作状态】").append(operLog.getStatus() == 0 ? "成功" : "失败").append("\n");
            if (operLog.getErrorMsg() != null) {
                logContent.append("【错误信息】").append(operLog.getErrorMsg()).append("\n");
            }
            logContent.append("【消耗时间】").append(operLog.getCostTime()).append("ms\n");
            logContent.append("=".repeat(100)).append("\n\n");

            // 追加写入文件
            FileUtil.appendString(logContent.toString(), logFile, StandardCharsets.UTF_8);
            log.debug("操作日志已保存到文件: {}", fileName);
        } catch (Exception e) {
            log.error("保存操作日志到文件失败", e);
        }
    }
}
