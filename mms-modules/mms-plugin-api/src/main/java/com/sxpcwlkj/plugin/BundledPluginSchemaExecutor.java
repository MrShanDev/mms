package com.sxpcwlkj.plugin;

import java.util.List;

/**
 * 宿主侧（超级管理员）执行插件 JAR 内 {@link PluginConstants#SCHEMA_PATH_IN_JAR} 拆句后的 DDL。
 * <p>实现由 {@code mms-system} 在存在 {@link org.springframework.jdbc.core.JdbcTemplate} 时注册；仅允许受限 CREATE/ALTER。</p>
 */
public interface BundledPluginSchemaExecutor {

    /**
     * 按语句顺序执行；每条语句须通过白名单校验。
     *
     * @return 人类可读日志行（含成功/失败与耗时提示）
     */
    List<String> executeBundledSchema(String sql);
}
