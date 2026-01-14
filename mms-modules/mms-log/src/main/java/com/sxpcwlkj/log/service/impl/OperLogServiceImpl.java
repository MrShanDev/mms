package com.sxpcwlkj.log.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.framework.utils.AddressUtil;
import com.sxpcwlkj.log.entity.SysLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.log.mapper.SysLogMapper;
import com.sxpcwlkj.log.service.OperLogService;
import com.sxpcwlkj.log.utils.IpUtils;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务实现
 *
 * @author mmsAdmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperLogServiceImpl implements OperLogService {

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
    /**
     * 快速记录日志 - 简化版
     */
    @Async("operLogExecutor")
    @Override
    public void log(String module, OperationType operType, String description) {
        log(module, operType, description, null);
    }

    /**
     * 快速记录日志 - 带结果
     */
    @Async("operLogExecutor")
    @Override
    public void log(String module, OperationType operType, String description, Object result) {
        try {
            SysLog operLog = buildQuickLog(module, operType, description);
            operLog.setStatus(0);
            if (result != null) {
                operLog.setJsonResult(JSON.toJSONString(result));
            }
            saveToDatabase(operLog);
        } catch (Exception e) {
            log.error("快速记录日志失败", e);
        }
    }

    /**
     * 快速记录日志 - 带异常
     */
    @Async("operLogExecutor")
    @Override
    public void logError(String module, OperationType operType, String description, Exception e) {
        try {
            SysLog operLog = buildQuickLog(module, operType, description);
            operLog.setStatus(1);
            operLog.setErrorMsg(e != null ? e.getMessage() : "");
            saveToDatabase(operLog);
        } catch (Exception ex) {
            log.error("快速记录错误日志失败", ex);
        }
    }

    /**
     * 构建快速日志对象
     */
    private SysLog buildQuickLog(String module, OperationType operType, String description) {
        SysLog operLog = new SysLog();
        operLog.setModule(module);
        operLog.setOperType(operType.getCode());
        operLog.setDescription(description);
        operLog.setOperTime(LocalDateTime.now());

        // 获取用户信息
        try {
            if (LoginObject.isLogin()) {
                String userIdStr = LoginObject.getLoginId();
                // 将String转换为Long
                try {
                    operLog.setUserId(Long.parseLong(userIdStr));
                } catch (NumberFormatException e) {
                    log.warn("用户ID格式错误: {}", userIdStr);
                    operLog.setUserId(null);
                }

                String userName = RedisUtil.getCacheObject(RedisConstant.ADMIN_NAME + userIdStr);
                operLog.setUserName(userName != null ? userName : "未知");

                // 安全获取租户ID，处理可能的类型转换
                Object tenantIdObj = RedisUtil.getCacheObject(RedisConstant.ADMIN_TENANT_KEY + userIdStr);
                Long tenantId = 0L;
                if (tenantIdObj != null) {
                    if (tenantIdObj instanceof Long) {
                        tenantId = (Long) tenantIdObj;
                    } else if (tenantIdObj instanceof String) {
                        try {
                            tenantId = Long.parseLong((String) tenantIdObj);
                        } catch (NumberFormatException ex) {
                            log.warn("租户ID格式错误: {}", tenantIdObj);
                        }
                    } else if (tenantIdObj instanceof Number) {
                        tenantId = ((Number) tenantIdObj).longValue();
                    }
                }
                operLog.setTenantId(tenantId);

                try {
                    List<String> roleList = StpUtil.getRoleList();
                    operLog.setUserRoles(roleList != null ? String.join(",", roleList) : "");
                } catch (Exception e) {
                    operLog.setUserRoles("");
                }
            } else {
                // 未登录时设置为null
                operLog.setUserId(null);
                operLog.setUserName("匿名");
                operLog.setTenantId(0L);
            }
        } catch (Exception e) {
            operLog.setUserId(null);
            operLog.setUserName("匿名");
            operLog.setTenantId(0L);
        }

        // 获取请求信息
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                operLog.setRequestMethod(request.getMethod());
                operLog.setOperUrl(request.getRequestURI());

                String ip = IpUtils.getIpAddr(request);
                operLog.setOperIp(ip);
                operLog.setOperLocation(AddressUtil.getCityInfo(ip));

                operLog.setUserAgent(request.getHeader("User-Agent"));
                operLog.setBrowser(IpUtils.getBrowser(request));
                operLog.setOs(IpUtils.getOs(request));
            }
        } catch (Exception e) {
            // 非 HTTP 请求环境，忽略
        }

        return operLog;
    }
}
