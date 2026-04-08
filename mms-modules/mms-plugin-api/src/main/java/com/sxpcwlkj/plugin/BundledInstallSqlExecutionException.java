package com.sxpcwlkj.plugin;

import java.util.List;

/**
 * 执行 {@code script/install.sql} 过程中某条语句失败时抛出；携带已写入日志与<strong>此前已成功插入</strong>的
 * {@code sys_function.id}，供调用方回滚部分写入。
 */
public final class BundledInstallSqlExecutionException extends RuntimeException {

    private final List<String> logLines;
    private final List<String> insertedFunctionIds;

    public BundledInstallSqlExecutionException(
            List<String> logLines, List<String> insertedFunctionIds, String message, Throwable cause) {
        super(message, cause);
        this.logLines = logLines == null ? List.of() : List.copyOf(logLines);
        this.insertedFunctionIds = insertedFunctionIds == null ? List.of() : List.copyOf(insertedFunctionIds);
    }

    public List<String> getLogLines() {
        return logLines;
    }

    public List<String> getInsertedFunctionIds() {
        return insertedFunctionIds;
    }
}
