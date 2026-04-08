package com.sxpcwlkj.plugin;

import java.util.List;

/**
 * {@link BundledPluginSchemaExecutor#executeBundledInstallSql(String)} 的执行结果：人类可读日志行 +
 * <strong>本次实际执行成功</strong>插入的主键 id（安装失败时按「字典数据 → 字典类型 → 菜单」顺序回滚）。
 */
public record BundledInstallSqlResult(
        List<String> logLines,
        List<String> insertedSysFunctionIds,
        List<String> insertedSysDictIds,
        List<String> insertedSysDictDataIds) {
    public BundledInstallSqlResult {
        logLines = logLines == null ? List.of() : List.copyOf(logLines);
        insertedSysFunctionIds =
                insertedSysFunctionIds == null ? List.of() : List.copyOf(insertedSysFunctionIds);
        insertedSysDictIds = insertedSysDictIds == null ? List.of() : List.copyOf(insertedSysDictIds);
        insertedSysDictDataIds =
                insertedSysDictDataIds == null ? List.of() : List.copyOf(insertedSysDictDataIds);
    }
}
