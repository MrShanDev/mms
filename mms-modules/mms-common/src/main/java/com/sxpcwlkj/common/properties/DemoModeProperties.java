package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 演示模式（全局只读）配置。
 * <p>{@code allowedUsers}：登录<strong>用户名</strong>（与 {@code LoginObject#getLoginUserName} 一致，大小写不敏感）。
 * 插件 {@code installStream} 等在 Servlet 异步线程写库时，入口线程会把当前账号绑定到 {@link com.sxpcwlkj.common.context.DemoModeContextHolder}，
 * 演示拦截器据此匹配本白名单；若账号具备 {@code super_admin}，入口处亦会绑定该标识，一般可不写白名单也能安装插件。</p>
 *
 * @author mmsAdmin
 */
@Data
@Component
@ConfigurationProperties(prefix = "demo.mode")
public class DemoModeProperties {
    private boolean enabled;
    private String messageTemplate;
    /** 白名单用户（允许在演示模式下修改）；YAML 示例：{@code demo.mode.allowed-users: admin,demo} */
    private Set<String> allowedUsers;

    /**
     * 演示模式下允许执行的 MyBatis 写语句。
     * <ul>
     *   <li>含 {@code .}：按 <strong>MappedStatement id</strong> 匹配——先 {@code equals}，再 {@code endsWith}（可写全限定名或
     *   {@code SysXxxMapper.method} 后缀，避免与其它模块同名 {@code insert}/{@code update} 一并放行）。</li>
     *   <li>不含 {@code .}：仅匹配语句 id <strong>最后一段方法短名</strong>（全库所有 Mapper 同名方法均放行，慎用）。</li>
     * </ul>
     */
    private Set<String> allowedMethods;

    public boolean isAllowedUser(String username) {
        if (username == null || username.isBlank() || allowedUsers == null || allowedUsers.isEmpty()) {
            return false;
        }
        String t = username.trim();
        for (String a : allowedUsers) {
            if (a != null && a.trim().equalsIgnoreCase(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param mappedStatementId MyBatis 完整语句 id，如 {@code com.sxpcwlkj.system.mapper.SysPluginVersionMapper.update}
     */
    public boolean isAllowedMappedStatement(String mappedStatementId) {
        if (mappedStatementId == null || allowedMethods == null || allowedMethods.isEmpty()) {
            return false;
        }
        String sid = mappedStatementId;
        String shortMethod = lastSegmentOfStatementId(sid);
        for (String raw : allowedMethods) {
            if (raw == null) {
                continue;
            }
            String e = raw.trim();
            if (e.isEmpty()) {
                continue;
            }
            if (e.indexOf('.') >= 0) {
                if (sid.equals(e) || sid.endsWith(e)) {
                    return true;
                }
            } else if (shortMethod.equals(e)) {
                return true;
            }
        }
        return false;
    }

    private static String lastSegmentOfStatementId(String statementId) {
        int lastDot = statementId.lastIndexOf('.');
        if (lastDot > 0 && lastDot < statementId.length() - 1) {
            return statementId.substring(lastDot + 1);
        }
        return statementId;
    }

}
