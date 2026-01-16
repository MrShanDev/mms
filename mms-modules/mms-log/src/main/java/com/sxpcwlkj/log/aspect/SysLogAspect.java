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
import com.sxpcwlkj.log.service.SysOperLogService;
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

    private final SysOperLogService sysLogService;

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
                    String userIdStr = LoginObject.getLoginId();
                    // 将String转换为Long
                    try {
                        sysOperLog.setUserId(Long.parseLong(userIdStr));
                    } catch (NumberFormatException e) {
                        log.warn("用户ID格式错误: {}", userIdStr);
                        sysOperLog.setUserId(null);
                    }

                    // 从 Redis 获取用户信息
                    String userName = RedisUtil.getCacheObject(RedisConstant.ADMIN_NAME + userIdStr);
                    sysOperLog.setUserName(userName != null ? userName : "未知");

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
                    sysOperLog.setTenantId(tenantId);

                    // 获取角色信息
                    try {
                        List<String> roleList = StpUtil.getRoleList();
                        sysOperLog.setUserRoles(roleList != null ? String.join(",", roleList) : "");
                    } catch (Exception e) {
                        log.warn("获取用户角色失败", e);
                        sysOperLog.setUserRoles("");
                    }
                } else {
                    // 未登录时设置为null
                    sysOperLog.setUserId(null);
                    sysOperLog.setUserName("匿名");
                    sysOperLog.setTenantId(0L);
                }
            } catch (Exception e) {
                log.warn("获取用户信息失败", e);
                sysOperLog.setUserId(null);
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

            // 保存操作前数据（仅针对更新操作）
            if (mmsLog.saveBeforeData() && operType == OperationType.UPDATE) {
                try {
                    String beforeData = getBeforeData(joinPoint);
                    if (StrUtil.isNotBlank(beforeData)) {
                        sysOperLog.setBeforeData(StrUtil.sub(beforeData, 0, 2000)); // 限制长度
                    }
                } catch (Exception e) {
                    log.error("获取操作前数据失败: {}", e.getMessage());
                }
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

            // 过滤敏感参数
            if (excludeParams != null && excludeParams.length > 0) {
                params = filterSensitiveData(params, excludeParams);
            }

            return JSONUtil.toJsonStr(params);
        } catch (Exception e) {
            log.warn("获取请求参数失败", e);
            return "";
        }
    }

    /**
     * 递归过滤敏感数据
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> filterSensitiveData(Map<String, Object> params, String[] excludeParams) {
        if (params == null || excludeParams == null || excludeParams.length == 0) {
            return params;
        }

        Map<String, Object> filtered = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 检查键名是否是敏感字段
            if (isSensitiveField(key, excludeParams)) {
                filtered.put(key, "******");
                continue;
            }

            // 处理值
            if (value == null) {
                filtered.put(key, null);
            } else if (value instanceof Map) {
                // 递归处理 Map
                filtered.put(key, filterSensitiveData((Map<String, Object>) value, excludeParams));
            } else if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                // 基础类型直接放入
                filtered.put(key, value);
            } else {
                // 其他对象类型，转换为 Map 再过滤
                try {
                    String json = JSONUtil.toJsonStr(value);
                    Map<String, Object> objMap = JSONUtil.toBean(json, Map.class);
                    filtered.put(key, filterSensitiveData(objMap, excludeParams));
                } catch (Exception e) {
                    // 转换失败，直接使用原值
                    filtered.put(key, value);
                }
            }
        }
        return filtered;
    }

    /**
     * 判断是否是敏感字段
     */
    private boolean isSensitiveField(String fieldName, String[] excludeParams) {
        if (fieldName == null || excludeParams == null) {
            return false;
        }
        for (String excludeParam : excludeParams) {
            if (fieldName.equalsIgnoreCase(excludeParam)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取修改前的数据
     * 适用于更新操作，通过反射获取ID字段并查询数据库
     */
    private String getBeforeData(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return "";
            }

            // 遍历参数，查找BO对象
            for (Object arg : args) {
                if (arg == null || isFilterObject(arg)) {
                    continue;
                }

                // 查找带有 "Id" 后缀的字段（如 userId, deptId, roleId 等）
                Object entityId = extractIdFromObject(arg);
                if (entityId == null) {
                    continue;
                }

                // 获取目标方法所在的Controller
                Object controller = joinPoint.getTarget();
                if (controller == null) {
                    continue;
                }

                // 从Controller中提取Service对象
                Object service = extractServiceFromController(controller);
                if (service == null) {
                    continue;
                }

                // 尝试调用 selectById 或 selectVoById 方法
                Object beforeEntity = queryBeforeData(service, entityId);
                if (beforeEntity != null) {
                    // 序列化为JSON并过滤敏感字段
                    String json = JSONUtil.toJsonStr(beforeEntity);
                    Map<String, Object> dataMap = JSONUtil.toBean(json, Map.class);
                    // 使用默认的敏感参数列表
                    String[] excludeParams = {"password", "oldPassword", "newPassword", "confirmPassword"};
                    Map<String, Object> filtered = filterSensitiveData(dataMap, excludeParams);
                    return JSONUtil.toJsonStr(filtered);
                }
            }
        } catch (Exception e) {
            log.error("获取修改前数据异常: {}", e.getMessage());
        }
        return "";
    }

    /**
     * 从对象中提取ID字段
     * 优先级：@TableId > id字段 > 以"Id"结尾的字段
     */
    private Object extractIdFromObject(Object obj) {
        try {
            Class<?> clazz = obj.getClass();

            // 1. 优先：查找带有 @TableId 注解的字段（MyBatis-Plus 主键注解）
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(com.baomidou.mybatisplus.annotation.TableId.class)) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        return value;
                    }
                }
            }

            // 2. 查找父类中的 @TableId 注解（BO/VO可能继承自 Entity）
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                for (java.lang.reflect.Field field : superClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(com.baomidou.mybatisplus.annotation.TableId.class)) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (value != null) {
                            return value;
                        }
                    }
                }
            }

            // 3. 兜底：查找以 "Id" 结尾的字段（优先级：单独的"id" > userId > 其他*Id）
            // 3.1 查找 "id" 字段
            try {
                java.lang.reflect.Field field = clazz.getDeclaredField("id");
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    return value;
                }
            } catch (NoSuchFieldException ignored) {}

            // 3.2 查找父类的 "id" 字段
            if (superClass != null && superClass != Object.class) {
                try {
                    java.lang.reflect.Field field = superClass.getDeclaredField("id");
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        return value;
                    }
                } catch (NoSuchFieldException ignored) {}
            }

            // 3.3 查找以 "Id" 结尾的字段（当前类）
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                String fieldName = field.getName();
                if (fieldName.endsWith("Id") && !fieldName.equals("tenantId") && !fieldName.equals("createdBy") && !fieldName.equals("updatedBy")) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        return value;
                    }
                }
            }

            // 3.4 查找父类的 "*Id" 字段
            if (superClass != null && superClass != Object.class) {
                for (java.lang.reflect.Field field : superClass.getDeclaredFields()) {
                    String fieldName = field.getName();
                    if (fieldName.endsWith("Id") && !fieldName.equals("tenantId") && !fieldName.equals("createdBy") && !fieldName.equals("updatedBy")) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (value != null) {
                            return value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("提取ID字段失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 从Controller中提取Service对象
     * 尝试查找名为 baseService 或 *Service 的字段
     */
    private Object extractServiceFromController(Object controller) {
        try {
            Class<?> clazz = controller.getClass();

            // 1. 优先查找 baseService 字段（最常见）
            try {
                java.lang.reflect.Field field = clazz.getDeclaredField("baseService");
                field.setAccessible(true);
                Object service = field.get(controller);
                if (service != null) {
                    return service;
                }
            } catch (NoSuchFieldException ignored) {}

            // 2. 兜底：查找以 "Service" 结尾的字段
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                if (field.getName().endsWith("Service")) {
                    field.setAccessible(true);
                    Object service = field.get(controller);
                    if (service != null) {
                        return service;
                    }
                }
            }
        } catch (Exception e) {
            log.error("从Controller提取Service失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 查询修改前的数据
     * 尝试调用 Service 的 selectById 或 selectVoById 方法
     */
    private Object queryBeforeData(Object service, Object entityId) {
        try {
            Class<?> serviceClass = service.getClass();
            java.lang.reflect.Method[] methods = serviceClass.getMethods();

            // 1. 尝试调用 selectVoById 方法
            for (java.lang.reflect.Method method : methods) {
                if ("selectVoById".equals(method.getName())) {
                    try {
                        Object result = method.invoke(service, entityId);
                        if (result != null) {
                            return result;
                        }
                    } catch (Exception e) {
                        log.error("selectVoById 调用失败: {}", e.getMessage());
                    }
                }
            }

            // 2. 尝试调用 selectById 方法
            for (java.lang.reflect.Method method : methods) {
                if ("selectById".equals(method.getName())) {
                    try {
                        Object result = method.invoke(service, entityId);
                        if (result != null) {
                            return result;
                        }
                    } catch (Exception e) {
                        log.error("selectById 调用失败: {}", e.getMessage());
                    }
                }
            }

            // 3. 尝试调用 getById 方法（MyBatis-Plus 标准方法）
            for (java.lang.reflect.Method method : methods) {
                if ("getById".equals(method.getName())) {
                    try {
                        Object result = method.invoke(service, entityId);
                        if (result != null) {
                            return result;
                        }
                    } catch (Exception e) {
                        log.error("getById 调用失败: {}", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("查询修改前数据异常: {}", e.getMessage());
        }
        return null;
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
