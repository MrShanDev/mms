package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 插件 DDL 白名单：单语句、禁止系统目录、表名须属于本插件前缀。
 */
public final class PluginSchemaSqlGuard {

    private static final Pattern SYS_DIR =
            Pattern.compile(
                    "\\b(information_schema|pg_catalog|sqlite_master|mysql\\s*\\.|sys\\.|performance_schema)\\b",
                    Pattern.CASE_INSENSITIVE);

    /** CREATE TABLE [IF NOT EXISTS] name ( ... */
    private static final Pattern CREATE_TABLE =
            Pattern.compile(
                    "^create\\s+table\\s+(?:if\\s+not\\s+exists\\s+)?[`\"]?([a-zA-Z0-9_]+)[`\"]?\\s*\\(",
                    Pattern.CASE_INSENSITIVE);

    /** DROP TABLE [IF EXISTS] name [;]? */
    private static final Pattern DROP_TABLE =
            Pattern.compile(
                    "^drop\\s+table\\s+(?:if\\s+exists\\s+)?[`\"]?([a-zA-Z0-9_]+)[`\"]?\\s*$",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /** ALTER TABLE name rest */
    private static final Pattern ALTER_TABLE =
            Pattern.compile(
                    "^alter\\s+table\\s+[`\"]?([a-zA-Z0-9_]+)[`\"]?\\s+(.+)$",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern ALTER_ADD_COLUMN =
            Pattern.compile("^add\\s+column\\s+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern ALTER_DROP_COLUMN =
            Pattern.compile("^drop\\s+column\\s+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private PluginSchemaSqlGuard() {}

    public static void assertDdlAllowed(String sql, String tablePrefix) {
        if (sql == null || sql.isBlank()) {
            throw new PluginException("DDL SQL 不能为空");
        }
        String trimmed = sql.trim();
        if (trimmed.indexOf(';') >= 0) {
            throw new PluginException("禁止多语句或分号");
        }
        if (SYS_DIR.matcher(trimmed).find()) {
            throw new PluginException("DDL 禁止引用系统目录或危险前缀");
        }
        if (tablePrefix == null || tablePrefix.isBlank()) {
            throw new PluginException("pluginTablePrefix 不能为空");
        }
        String pfx = tablePrefix.trim().toLowerCase(Locale.ROOT);
        String condensed = trimmed.replaceAll("[`\"]", "");

        Matcher c = CREATE_TABLE.matcher(condensed);
        if (c.find()) {
            requireTablePrefixed(c.group(1), pfx);
            return;
        }
        Matcher d = DROP_TABLE.matcher(condensed);
        if (d.find()) {
            requireTablePrefixed(d.group(1), pfx);
            return;
        }
        Matcher a = ALTER_TABLE.matcher(condensed);
        if (a.find()) {
            requireTablePrefixed(a.group(1), pfx);
            String tail = a.group(2).trim();
            if (!ALTER_ADD_COLUMN.matcher(tail).find() && !ALTER_DROP_COLUMN.matcher(tail).find()) {
                throw new PluginException("仅允许 ALTER TABLE ... ADD COLUMN 或 DROP COLUMN");
            }
            return;
        }
        throw new PluginException("仅允许 CREATE TABLE、DROP TABLE、ALTER TABLE（ADD COLUMN / DROP COLUMN）");
    }

    private static void requireTablePrefixed(String tableName, String prefixLower) {
        if (tableName == null || tableName.isBlank()) {
            throw new PluginException("无法解析 DDL 中的表名");
        }
        String t = tableName.trim().toLowerCase(Locale.ROOT);
        if (!t.startsWith(prefixLower)) {
            throw new PluginException("DDL 表名须以本插件 pluginTablePrefix 开头: " + tableName);
        }
    }
}
