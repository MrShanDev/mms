package com.sxpcwlkj.datasource.handler;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.context.DemoModeContextHolder;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.DemoModeException;
import com.sxpcwlkj.common.properties.DemoModeProperties;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mmsAdmin
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
public class DemoModeInterceptor implements InnerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(DemoModeInterceptor.class);

    private final DemoModeProperties demoModeProperties;

    public DemoModeInterceptor(DemoModeProperties demoModeProperties) {
        this.demoModeProperties = demoModeProperties;
    }

    @Override
    public boolean willDoUpdate(Executor executor, MappedStatement ms, Object parameter) {
        // 1. 检查是否演示模式
        if (!demoModeProperties.isEnabled()) {
            return true;
        }
        // 2. 请求入口已绑定到当前线程的主体（Servlet 异步 / 线程池无 Sa Web 上下文时，与下面同步规则等价）
        if (Boolean.TRUE.equals(DemoModeContextHolder.getPropagatedSuperAdmin())) {
            return true;
        }
        String propagatedUser = DemoModeContextHolder.getPropagatedUsername();
        if (propagatedUser != null
                && !propagatedUser.isBlank()
                && demoModeProperties.isAllowedUser(propagatedUser)) {
            return true;
        }
        // 3. 可信编排显式关闭（慎用）
        if (Boolean.TRUE.equals(DemoModeContextHolder.isDemoModeDisabled())) {
            return true;
        }
        // 4. 超级管理员放行：内置 admin（id=1）或与插件安装等接口一致的 Sa-Token super_admin 角色
        if (LoginObject.isLogin()) {
            if (Boolean.TRUE.equals(LoginObject.getLoginSuper())) {
                return true;
            }
            try {
                if (StpUtil.isLogin() && StpUtil.hasRole(SystemCommonEnum.SUPER_ADMIN.getCode())) {
                    return true;
                }
            } catch (Exception ex) {
                // 例如 getRoleList 曾 NPE、非 Web 线程无 Sa 上下文等，避免静默误判为未放行
                logger.debug("演示模式：Sa-Token super_admin 判定未通过: {}", ex.getMessage());
            }
            if (demoModeProperties.isAllowedUser(LoginObject.getLoginUserName())) {
                return true;
            }
        }

        // 5. 白名单语句：含「.」按 MappedStatement 全 id / 后缀匹配；否则仅方法短名（影响全库同名方法）
        if (demoModeProperties.isAllowedMappedStatement(ms.getId())) {
            return true;
        }

        // 6. 特殊处理：忽略日志表的操作（日志应该在演示模式下也能正常记录）
        String entityName = getEntityName(ms.getId());
        if ("SysLog".equals(entityName) || "SysOperLog".equals(entityName)) {
            return true;
        }

        // 7. 检查是否是修改操作
        if (!isModifyOperation(ms)) {
            return true;
        }

        // 8. 生成详细错误信息
        String errorMessage = generateErrorMessage(ms);

        // 9. 记录拦截日志
        logger.warn("拦截演示模式下的修改操作: [{}] {}",
            ms.getSqlCommandType(),
            ms.getId());

        // 10. 抛出自定义异常
        throw new DemoModeException(403100, errorMessage);
    }

    // 判断是否是修改操作
    private boolean isModifyOperation(MappedStatement ms) {
        SqlCommandType commandType = ms.getSqlCommandType();
        return commandType == SqlCommandType.INSERT ||
            commandType == SqlCommandType.UPDATE ||
            commandType == SqlCommandType.DELETE;
    }

    // 生成详细的错误消息
    private String generateErrorMessage(MappedStatement ms) {
        // 获取操作类型
        String operation = getOperationType(ms.getSqlCommandType());

        // 获取实体名称
        String entity = getEntityName(ms.getId());

        // 获取方法名称
        String method = getMethodName(ms.getId());

        // 使用配置中的消息模板
        String template = demoModeProperties.getMessageTemplate();
        if (template == null || template.isEmpty()) {
            template = "演示模式下禁止{operation}操作 [实体: {entity}, 方法: {method}]";
        }

        // 替换占位符
        return template
            .replace("{operation}", operation)
            .replace("{entity}", entity)
            .replace("{method}", method);
    }

    // 获取操作类型中文描述
    private String getOperationType(SqlCommandType commandType) {
        switch (commandType) {
            case INSERT: return "新增";
            case UPDATE: return "更新";
            case DELETE: return "删除";
            default: return "修改";
        }
    }

    // 从Mapper方法名提取实体名
    private String getEntityName(String statementId) {
        if (statementId == null) return "未知实体";

        // 示例: com.sxpcwlkj.system.mapper.SysNoticeMapper.updateById
        String[] parts = statementId.split("\\.");
        if (parts.length > 1) {
            String className = parts[parts.length - 2];
            if (className.endsWith("Mapper")) {
                return className.substring(0, className.length() - 6);
            }
            return className;
        }
        return statementId;
    }

    // 获取方法名
    private String getMethodName(String statementId) {
        if (statementId == null) return "未知方法";

        int lastDot = statementId.lastIndexOf('.');
        if (lastDot > 0 && lastDot < statementId.length() - 1) {
            return statementId.substring(lastDot + 1);
        }
        return statementId;
    }


}
