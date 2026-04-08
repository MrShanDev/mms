package com.sxpcwlkj.plugin;

import java.util.List;

/**
 * {@link BundledPluginSchemaExecutor#executeBundledInstallSql(String)} 的执行结果：人类可读日志行 +
 * <strong>本次实际执行成功</strong>的 {@code sys_function.id}（用于安装失败时回滚菜单行）。
 */
public record BundledInstallSqlResult(List<String> logLines, List<String> insertedSysFunctionIds) {
    public BundledInstallSqlResult {
        logLines = logLines == null ? List.of() : List.copyOf(logLines);
        insertedSysFunctionIds =
                insertedSysFunctionIds == null ? List.of() : List.copyOf(insertedSysFunctionIds);
    }
}
