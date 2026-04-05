package com.sxpcwlkj.plugin;

import java.util.List;

/**
 * 受限 DDL 与库表自省（契约版本 4+）。与 {@link PluginDataAccess} 分离：后者仅 DML/SELECT 且走 SQL Guard；
 * 本接口用于<strong>单条</strong> {@code CREATE TABLE} / {@code DROP TABLE} / {@code ALTER TABLE ... ADD|DROP COLUMN}，
 * 且操作对象须落在调用方插件 {@link PluginDescriptor#getPluginTablePrefix()} 命名空间内。
 */
public interface PluginSchemaAccess {

    /**
     * 执行单条 DDL（无分号、无多语句）。
     *
     * @throws PluginException 校验失败或执行失败
     */
    void executeDdl(String sql);

    /**
     * 列出当前连接库中物理表名以本插件 {@code pluginTablePrefix} 开头的表（扫描后在内存过滤，避免 JDBC 通配符与下划线语义问题）。
     */
    List<String> listOwnedTables();
}
