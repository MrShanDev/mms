package com.sxpcwlkj.system.plugin;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 插件 JAR 内 {@code script/install.sql} 自动执行白名单：仅允许向 {@code sys_function} 插入菜单/权限行。
 */
public final class BundledPluginInstallSqlGuard {

    private static final Pattern SYS_DIR =
            Pattern.compile(
                    "\\b(information_schema|pg_catalog|mysql\\s*\\.|sys\\.|performance_schema)\\b",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern INSERT_SYS_FUNCTION =
            Pattern.compile("^\\s*INSERT\\s+INTO\\s+[`\"]?sys_function[`\"]?\\s*\\(", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private BundledPluginInstallSqlGuard() {}

    public static void assertStatementAllowed(String stmt) {
        if (stmt == null || stmt.isBlank()) {
            return;
        }
        String s = stmt.trim();
        if (s.indexOf(';') >= 0) {
            throw new IllegalArgumentException("单条语句不得包含分号");
        }
        if (SYS_DIR.matcher(s).find()) {
            throw new IllegalArgumentException("禁止引用系统目录或危险前缀");
        }
        if (!INSERT_SYS_FUNCTION.matcher(s).lookingAt()) {
            throw new IllegalArgumentException("install.sql 仅允许 INSERT INTO sys_function");
        }
    }
}
