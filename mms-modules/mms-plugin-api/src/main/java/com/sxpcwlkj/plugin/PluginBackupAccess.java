package com.sxpcwlkj.plugin;

import java.util.List;

/**
 * 库级逻辑备份/还原（契约版本 5+）。仅对 {@link PluginDescriptor} 中声明了 {@code backupOperator: true} 的插件暴露，
 * 避免任意插件遍历全库。
 * <p><b>当前实现基于 MySQL</b>：依赖 {@code SHOW CREATE TABLE} 与常规 DML；其它驱动请待宿主扩展。</p>
 */
public interface PluginBackupAccess {

    /**
     * 当前连接 catalog（库）下全部物理表名（{@code TABLE}，不含视图）；字典序。
     */
    List<String> listCatalogTables();

    /**
     * 导出逻辑 SQL：{@code tables} 为 null 或空表示<strong>当前库全部表</strong>；否则仅导出指定表（表名须匹配 {@code [a-zA-Z0-9_]+}）。
     *
     * @param gzip true 时返回 gzip 压缩的字节（utf-8）
     */
    byte[] exportLogicalSql(List<String> tables, boolean gzip) throws Exception;

    /**
     * 导入由 {@link #exportLogicalSql} 生成的脚本（或同风格 UTF-8 SQL：SET/CREATE/DROP TABLE IF EXISTS/INSERT/LOCK 等）。
     */
    void importLogicalSql(byte[] data, boolean gzip) throws Exception;
}
