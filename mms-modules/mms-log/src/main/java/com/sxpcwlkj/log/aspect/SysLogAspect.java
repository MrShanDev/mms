package com.sxpcwlkj.log.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import cn.dev33.satoken.stp.StpUtil;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.entity.SysOperLog;
import com.sxpcwlkj.log.enums.LogSavePolicy;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.log.service.SysLogService;
import com.sxpcwlkj.framework.utils.AddressUtil;
import com.sxpcwlkj.log.utils.IpUtils;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志切面
 * 支持智能识别:
 * 1. 只写 @MmsLog - 自动识别 module、operType、description
 * 2. 手动指定 - 按照指定的值走
 *
 * @author mmsAdmin
 */
@Slf4j
@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class SysLogAspect {

    private final SysLogService sysLogService;

    /**
     * ThreadLocal 存储开始时间
     */
    private static final ThreadLocal<Long> TIME_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * ThreadLocal 存储日志信息
     */
    private static final ThreadLocal<SysOperLog> LOG_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * ThreadLocal 存储响应数据配置
     */
    private static final ThreadLocal<Boolean> SAVE_RESPONSE_DATA = new ThreadLocal<>();

    /**
     * ThreadLocal 存储保存策略
     */
    private static final ThreadLocal<LogSavePolicy> SAVE_POLICY = new ThreadLocal<>();

    /**
     * 前置通知
     */
    @Before("@annotation(mmsLog)")
    public void doBefore(JoinPoint joinPoint, MmsLog mmsLog) {
        try {
            // 记录开始时间
            TIME_THREAD_LOCAL.set(System.currentTimeMillis());

            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();

            // 构建日志对象
            SysOperLog sysOperLog = new SysOperLog();

            // 获取方法信息用于智能识别
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

            // 获取日志配置信息 (支持自动识别)
            String module = mmsLog.module();
            OperationType operType = mmsLog.operType();
            String description = mmsLog.description();

            // 如果 module 为空，自动识别
            if (StrUtil.isBlank(module)) {
                module = getModuleFromMethod(method);
            }

            // 如果 operType 是 OTHER，尝试自动识别
            if (operType == OperationType.OTHER) {
                Integer autoType = getOperTypeFromMethod(method);
                if (autoType != OperationType.OTHER.getCode()) {
                    operType = OperationType.values()[autoType];
                }
            }

            // 如果 description 为空，自动识别
            if (StrUtil.isBlank(description)) {
                description = getDescriptionFromMethod(method);
            }

            // 设置基本信息
            sysOperLog.setOperTime(LocalDateTime.now());
            sysOperLog.setModule(module);
            sysOperLog.setOperType(operType.getCode());
            sysOperLog.setDescription(description);
            sysOperLog.setRequestMethod(request.getMethod());
            sysOperLog.setOperUrl(request.getRequestURI());

            // 获取方法信息
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            sysOperLog.setMethod(className + "." + methodName);

            // 获取用户信息
            try {
                if (LoginObject.isLogin()) {
                    String userId = LoginObject.getLoginId();
                    sysOperLog.setUserId(userId);

                    // 从 Redis 获取用户信息
                    String userName = RedisUtil.getCacheObject(RedisConstant.ADMIN_NAME + userId);
                    sysOperLog.setUserName(userName != null ? userName : "未知");

                    Long tenantId = RedisUtil.getCacheObject(RedisConstant.ADMIN_TENANT_KEY + userId);
                    sysOperLog.setTenantId(tenantId != null ? tenantId : 0L);

                    // 获取角色信息
                    try {
                        List<String> roleList = StpUtil.getRoleList();
                        sysOperLog.setUserRoles(roleList != null ? String.join(",", roleList) : "");
                    } catch (Exception e) {
                        log.warn("获取用户角色失败", e);
                        sysOperLog.setUserRoles("");
                    }
                }
            } catch (Exception e) {
                log.warn("获取用户信息失败", e);
                sysOperLog.setUserId("");
                sysOperLog.setUserName("匿名");
                sysOperLog.setTenantId(0L);
            }

            // 获取IP和地理位置
            String ip = IpUtils.getIpAddr(request);
            sysOperLog.setOperIp(ip);
            sysOperLog.setOperLocation(AddressUtil.getCityInfo(ip));

            // 获取浏览器和操作系统信息
            sysOperLog.setUserAgent(request.getHeader("User-Agent"));
            sysOperLog.setBrowser(IpUtils.getBrowser(request));
            sysOperLog.setOs(IpUtils.getOs(request));

            // 保存请求参数
            if (mmsLog.saveRequestData()) {
                String params = getRequestParams(joinPoint, mmsLog.excludeParams());
                sysOperLog.setOperParam(StrUtil.sub(params, 0, 2000)); // 限制长度
            }

            // 保存操作前数据
            if (mmsLog.saveBeforeData()) {
                // 这里可以根据业务需求实现,例如查询修改前的数据
                sysOperLog.setBeforeData("");
            }

            // 存储配置到 ThreadLocal
            SAVE_RESPONSE_DATA.set(mmsLog.saveResponseData());
            SAVE_POLICY.set(mmsLog.savePolicy());

            // 存储到 ThreadLocal
            LOG_THREAD_LOCAL.set(sysOperLog);
        } catch (Exception e) {
            log.error("操作日志前置处理失败", e);
        }
    }

    /**
     * 后置通知(成功)
     */
    @AfterReturning(pointcut = "@annotation(mmsLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, MmsLog mmsLog, Object result) {
        handleLog(result, null);
    }

    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "@annotation(mmsLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, MmsLog mmsLog, Exception e) {
        handleLog(null, e);
    }

    /**
     * 处理日志
     */
    private void handleLog(Object result, Exception e) {
        try {
            SysOperLog sysOperLog = LOG_THREAD_LOCAL.get();
            if (sysOperLog == null) {
                return;
            }

            // 计算消耗时间
            Long startTime = TIME_THREAD_LOCAL.get();
            if (startTime != null) {
                sysOperLog.setCostTime(System.currentTimeMillis() - startTime);
            }

            // 设置操作状态
            if (e != null) {
                sysOperLog.setStatus(1); // 失败
                sysOperLog.setErrorMsg(StrUtil.sub(ExceptionUtil.stacktraceToString(e), 0, 2000));
            } else {
                sysOperLog.setStatus(0); // 成功
            }

            // 保存响应数据
            Boolean saveResponseData = SAVE_RESPONSE_DATA.get();
            if (saveResponseData != null && saveResponseData && result != null) {
                String jsonResult = JSON.toJSONString(result);
                sysOperLog.setJsonResult(StrUtil.sub(jsonResult, 0, 2000)); // 限制长度
            }

            // 根据保存策略保存日志
            LogSavePolicy savePolicy = SAVE_POLICY.get();
            if (savePolicy == null) {
                savePolicy = LogSavePolicy.DATABASE;
            }
            switch (savePolicy) {
                case DATABASE:
                    sysLogService.saveToDatabase(sysOperLog);
                    break;
                case FILE:
                    sysLogService.saveToFile(sysOperLog);
                    break;
                case BOTH:
                    sysLogService.saveToDatabase(sysOperLog);
                    sysLogService.saveToFile(sysOperLog);
                    break;
            }
        } catch (Exception ex) {
            log.error("操作日志记录失败", ex);
        } finally {
            // 清理 ThreadLocal
            LOG_THREAD_LOCAL.remove();
            TIME_THREAD_LOCAL.remove();
            SAVE_RESPONSE_DATA.remove();
            SAVE_POLICY.remove();
        }
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(JoinPoint joinPoint, String[] excludeParams) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return "";
            }

            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null && !isFilterObject(args[i])) {
                    params.put("arg" + i, args[i]);
                }
            }

            // 排除敏感参数
            if (excludeParams != null && excludeParams.length > 0) {
                for (String key : excludeParams) {
                    params.remove(key);
                }
            }

            return JSONUtil.toJsonStr(params);
        } catch (Exception e) {
            log.warn("获取请求参数失败", e);
            return "";
        }
    }

    /**
     * 判断是否需要过滤的对象
     */
    private boolean isFilterObject(Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(HttpServletRequest.class);
        }
        return obj instanceof HttpServletRequest
                || obj instanceof jakarta.servlet.http.HttpServletResponse
                || obj instanceof org.springframework.web.multipart.MultipartFile;
    }

    /**
     * 从方法获取模块名称
     */
    private String getModuleFromMethod(Method method) {
        // 从类名推断，例如: SysUserController -> 用户管理
        String className = method.getDeclaringClass().getSimpleName();
        if (className.contains("User")) return "用户管理";
        if (className.contains("Role")) return "角色管理";
        if (className.contains("Dept")) return "部门管理";
        if (className.contains("Menu")) return "菜单管理";
        if (className.contains("Dict")) return "字典管理";
        if (className.contains("Config")) return "参数管理";
        if (className.contains("Notice")) return "通知管理";
        if (className.contains("Log")) return "日志管理";
        if (className.contains("Post")) return "岗位管理";
        return "系统管理";
    }

    /**
     * 从方法获取操作类型
     */
    private Integer getOperTypeFromMethod(Method method) {
        if (method.isAnnotationPresent(PostMapping.class)) {
            return OperationType.INSERT.getCode();
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return OperationType.UPDATE.getCode();
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return OperationType.DELETE.getCode();
        }
        return OperationType.OTHER.getCode();
    }

    /**
     * 从方法获取操作描述
     */
    private String getDescriptionFromMethod(Method method) {
        String methodName = method.getName();
        String operation = "";

        // 根据 HTTP 注解推断操作类型
        if (method.isAnnotationPresent(PostMapping.class)) {
            operation = "新增";
            if (methodName.contains("import") || methodName.contains("Import")) {
                operation = "导入";
            }
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            operation = "修改";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            operation = "删除";
        } else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class)) {
            if (methodName.contains("export") || methodName.contains("Export")) {
                operation = "导出";
            } else if (methodName.contains("list") || methodName.contains("List") ||
                       methodName.contains("page") || methodName.contains("Page")) {
                operation = "查询";
            } else {
                operation = "查询";
            }
        }

        // 根据方法名推断业务对象
        String target = "";
        if (methodName.contains("User") || methodName.contains("user")) {
            target = "用户";
        } else if (methodName.contains("Role") || methodName.contains("role")) {
            target = "角色";
        } else if (methodName.contains("Dept") || methodName.contains("dept")) {
            target = "部门";
        } else if (methodName.contains("Menu") || methodName.contains("menu")) {
            target = "菜单";
        } else if (methodName.contains("Dict") || methodName.contains("dict")) {
            target = "字典";
        } else {
            // 从类名推断
            String className = method.getDeclaringClass().getSimpleName();
            if (className.contains("User")) target = "用户";
            else if (className.contains("Role")) target = "角色";
            else if (className.contains("Dept")) target = "部门";
            else if (className.contains("Menu")) target = "菜单";
            else if (className.contains("Dict")) target = "字典";
            else target = "数据";
        }

        return operation + target;
    }
}
