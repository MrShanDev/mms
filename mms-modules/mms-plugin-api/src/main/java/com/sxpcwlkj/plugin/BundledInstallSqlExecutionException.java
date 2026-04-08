package com.sxpcwlkj.plugin;

import java.util.List;

/**
 * 执行 {@code script/install.sql} 过程中某条语句失败时抛出；携带已写入日志与<strong>此前已成功插入</strong>的主键 id，
 * 供调用方按「字典数据 → 字典类型 → 菜单」顺序回滚。
 */
public final class BundledInstallSqlExecutionException extends RuntimeException {

    private final List<String> logLines;
    private final List<String> insertedFunctionIds;
    private final List<String> insertedDictIds;
    private final List<String> insertedDictDataIds;

    public BundledInstallSqlExecutionException(
            List<String> logLines,
            List<String> insertedFunctionIds,
            List<String> insertedDictIds,
            List<String> insertedDictDataIds,
            String message,
            Throwable cause) {
        super(message, cause);
        this.logLines = logLines == null ? List.of() : List.copyOf(logLines);
        this.insertedFunctionIds = insertedFunctionIds == null ? List.of() : List.copyOf(insertedFunctionIds);
        this.insertedDictIds = insertedDictIds == null ? List.of() : List.copyOf(insertedDictIds);
        this.insertedDictDataIds = insertedDictDataIds == null ? List.of() : List.copyOf(insertedDictDataIds);
    }

    public List<String> getLogLines() {
        return logLines;
    }

    public List<String> getInsertedFunctionIds() {
        return insertedFunctionIds;
    }

    public List<String> getInsertedDictIds() {
        return insertedDictIds;
    }

    public List<String> getInsertedDictDataIds() {
        return insertedDictDataIds;
    }
}
