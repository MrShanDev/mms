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

    /**
     * 执行 JAR 内 {@link PluginConstants#INSTALL_SQL_PATH_IN_JAR} 拆句后的语句（安装流程自动调用）。
     * <p>白名单较 schema 更宽：仅允许对 {@code sys_function} 的 INSERT；主键冲突时跳过该条并记日志。</p>
     * <p>返回结果中的 {@link BundledInstallSqlResult#insertedSysFunctionIds()} 为<strong>本次实际执行成功</strong>的
     * 主键 id，供安装后续步骤失败时回滚 {@code sys_function}（与磁盘回滚配套）。</p>
     *
     * @throws BundledInstallSqlExecutionException 某条语句失败且此前已有成功插入时，携带已写入日志与已插入 id
     */
    BundledInstallSqlResult executeBundledInstallSql(String sql);
}
