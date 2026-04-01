package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 插件 {@link com.sxpcwlkj.plugin.PluginDataAccess} 提交的 SQL 最小安全校验（非 SQL 解析器，规则可随需求收紧）。
 */
public final class PluginSqlGuard {

    private static final Pattern DENY =
            Pattern.compile(
                    "\\b("
                            + "drop|truncate|alter|create|grant|revoke|merge|exec|execute|call|information_schema|pg_catalog|sqlite_master"
                            + ")\\b",
                    Pattern.CASE_INSENSITIVE);

    /**
     * 捕获 FROM/JOIN/UPDATE 及 INSERT INTO 后的首个表名（不含 schema 前缀的 MVP）。
     */
    private static final Pattern SQL_TABLE_REF =
            Pattern.compile(
                    "(?i)(?:\\b(?:from|join|update)\\s+|\\binsert\\s+into\\s+)([a-zA-Z_][a-zA-Z0-9_]*)");

    private PluginSqlGuard() {
    }

    public static void assertSafePluginSql(String sql, String tablePrefix, Set<String> allowedFullTableNamesLowercase) {
        if (sql == null || sql.isBlank()) {
            throw new PluginException("SQL 不能为空");
        }
        String trimmed = sql.trim();
        if (trimmed.indexOf(';') >= 0) {
            throw new PluginException("禁止多语句或分号");
        }
        if (DENY.matcher(trimmed).find()) {
            throw new PluginException("SQL 含禁止关键字或系统目录");
        }
        if (tablePrefix == null || tablePrefix.isBlank()) {
            throw new PluginException("表前缀不能为空");
        }
        String lowerSql = trimmed.toLowerCase(Locale.ROOT);
        String p = tablePrefix.trim().toLowerCase(Locale.ROOT);
        if (!lowerSql.contains(p)) {
            throw new PluginException("SQL 须包含本插件声明的 pluginTablePrefix: " + tablePrefix.trim());
        }
        if (!(lowerSql.startsWith("select")
                || lowerSql.startsWith("insert")
                || lowerSql.startsWith("update")
                || lowerSql.startsWith("delete"))) {
            throw new PluginException("仅允许 SELECT / INSERT / UPDATE / DELETE 开头语句");
        }
        assertTableReferences(trimmed, tablePrefix, allowedFullTableNamesLowercase);
    }

    private static void assertTableReferences(
            String sql, String tablePrefix, Set<String> allowedFullTableNamesLowercase) {
        if (allowedFullTableNamesLowercase == null || allowedFullTableNamesLowercase.isEmpty()) {
            return;
        }
        String prefixLower = tablePrefix.trim().toLowerCase(Locale.ROOT);
        Matcher m = SQL_TABLE_REF.matcher(sql);
        boolean any = false;
        while (m.find()) {
            any = true;
            String name = m.group(1);
            String nl = name.toLowerCase(Locale.ROOT);
            if (!nl.startsWith(prefixLower)) {
                throw new PluginException("SQL 中表 " + name + " 须使用本插件前缀 " + tablePrefix.trim());
            }
            if (!allowedFullTableNamesLowercase.contains(nl)) {
                throw new PluginException("表不在 pluginDataTables 白名单: " + name);
            }
        }
        if (!any && lowerStartsWithSelect(sql)) {
            throw new PluginException("SELECT 未能解析到表引用，请使用显式 FROM 且表名满足白名单");
        }
    }

    private static boolean lowerStartsWithSelect(String sql) {
        return sql.trim().toLowerCase(Locale.ROOT).startsWith("select");
    }

    public static void assertSelect(String sql, String tablePrefix, Set<String> allowedFullTableNamesLowercase) {
        assertSafePluginSql(sql, tablePrefix, allowedFullTableNamesLowercase);
        if (!sql.trim().toLowerCase(Locale.ROOT).startsWith("select")) {
            throw new PluginException("queryForList 仅允许 SELECT");
        }
    }

    public static void assertWrite(String sql, String tablePrefix, Set<String> allowedFullTableNamesLowercase) {
        assertSafePluginSql(sql, tablePrefix, allowedFullTableNamesLowercase);
        String lower = sql.trim().toLowerCase(Locale.ROOT);
        if (lower.startsWith("select")) {
            throw new PluginException("update 不允许 SELECT");
        }
    }
}
