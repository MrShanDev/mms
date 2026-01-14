package com.sxpcwlkj.log.service;

import com.sxpcwlkj.log.entity.SysLog;

/**
 * 操作日志服务接口
 *
 * @author mmsAdmin
 */
public interface SysLogService {

    /**
     * 保存操作日志(异步)
     *
     * @param operLog 操作日志对象
     */
    void saveLog(SysLog operLog);

    /**
     * 保存操作日志到数据库
     *
     * @param operLog 操作日志对象
     */
    void saveToDatabase(SysLog operLog);

    /**
     * 保存操作日志到本地文件
     *
     * @param operLog 操作日志对象
     */
    void saveToFile(SysLog operLog);
}
