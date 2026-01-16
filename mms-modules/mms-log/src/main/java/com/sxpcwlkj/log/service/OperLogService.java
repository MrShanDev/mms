package com.sxpcwlkj.log.service;

import com.sxpcwlkj.log.entity.SysOperLog;
import com.sxpcwlkj.log.enums.OperationType;

/**
 * 操作日志服务接口
 *
 * @author mmsAdmin
 */
public interface OperLogService {

    /**
     * 保存操作日志(异步)
     */
    void saveLog(SysOperLog operLog);

    /**
     * 保存操作日志到数据库
     */
    void saveToDatabase(SysOperLog operLog);

    /**
     * 保存操作日志到本地文件
     */
    void saveToFile(SysOperLog operLog);

    /**
     * 快速记录日志 - 简化版
     */
    void log(String module, OperationType operType, String description);

    /**
     * 快速记录日志 - 带结果
     */
    void log(String module, OperationType operType, String description, Object result);

    /**
     * 快速记录日志 - 带异常
     */
    void logError(String module, OperationType operType, String description, Exception e);
}
