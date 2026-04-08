package com.sxpcwlkj.system.plugin;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 插件 JAR 内 {@code script/install.sql} 自动执行白名单：仅允许向 {@code sys_function}、{@code sys_dict}、{@code sys_dict_data}
 * 插入（支持 {@code INSERT IGNORE}）；字典插入须通过 {@link BundledPluginSchemaSqlGuard#assertBundledDictInsertAllowed(String)}。
 */
public final class BundledPluginInstallSqlGuard {

    private static final Pattern SYS_DIR =
            Pattern.compile(
                    "\\b(information_schema|pg_catalog|mysql\\s*\\.|sys\\.|performance_schema)\\b",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern INSERT_INTO_INSTALL =
            Pattern.compile(
                    "^\\s*INSERT\\s+(IGNORE\\s+)?INTO\\s+[`\"]?(sys_function|sys_dict|sys_dict_data)[`\"]?\\s*\\(",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

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
        Matcher m = INSERT_INTO_INSTALL.matcher(s);
        if (!m.lookingAt()) {
            throw new IllegalArgumentException(
                    "install.sql 仅允许 INSERT [IGNORE] INTO sys_function、sys_dict、sys_dict_data");
        }
        String table = m.group(2).toLowerCase(Locale.ROOT);
        if ("sys_dict".equals(table) || "sys_dict_data".equals(table)) {
            BundledPluginSchemaSqlGuard.assertBundledDictInsertAllowed(s);
        }
    }
}
