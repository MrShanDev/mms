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
     * <p>白名单：{@code INSERT [IGNORE] INTO sys_function | sys_dict | sys_dict_data}；字典 {@code field_name} 须带
     * {@code mms_plugin_} 前缀；主键已存在时预检跳过或重复键跳过并记日志。</p>
     * <p>返回结果中各 {@code inserted*} 列表为<strong>本次实际执行成功</strong>的主键 id；安装后续步骤失败时按
     * 「字典数据 → 字典类型 → 菜单」删除回滚（与磁盘回滚配套）。</p>
     *
     * @throws BundledInstallSqlExecutionException 某条语句失败且此前已有成功插入时，携带已写入日志与已插入 id
     */
    BundledInstallSqlResult executeBundledInstallSql(String sql);
}
