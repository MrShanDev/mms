package com.sxpcwlkj.plugin;

import java.util.Locale;
import java.util.Optional;

/**
 * 解析插件包内 {@code schema.sql} 中单条 DDL 的元信息（当前仅用于 CREATE TABLE 幂等跳过）。
 */
public final class BundledPluginSchemaSupport {

    private BundledPluginSchemaSupport() {}

    /**
     * 从 {@code CREATE TABLE [IF NOT EXISTS]} 语句解析裸表名（去掉可选库名前缀）；非建表语句返回 empty。
     */
    public static Optional<String> extractCreateTableName(String stmt) {
        if (stmt == null) {
            return Optional.empty();
        }
        String s = stmt.trim();
        String u = s.toUpperCase(Locale.ROOT);
        if (!u.startsWith("CREATE TABLE")) {
            return Optional.empty();
        }
        int i = "CREATE TABLE".length();
        i = skipWs(s, i);
        if (u.regionMatches(i, "IF NOT EXISTS", 0, "IF NOT EXISTS".length())) {
            i += "IF NOT EXISTS".length();
            i = skipWs(s, i);
        }
        if (i >= s.length()) {
            return Optional.empty();
        }
        char c = s.charAt(i);
        String name;
        if (c == '`') {
            int end = s.indexOf('`', i + 1);
            if (end < 0) {
                return Optional.empty();
            }
            name = s.substring(i + 1, end);
        } else if (c == '"') {
            int end = s.indexOf('"', i + 1);
            if (end < 0) {
                return Optional.empty();
            }
            name = s.substring(i + 1, end);
        } else {
            int j = i;
            while (j < s.length()) {
                char ch = s.charAt(j);
                if (Character.isWhitespace(ch) || ch == '(') {
                    break;
                }
                j++;
            }
            if (j == i) {
                return Optional.empty();
            }
            name = s.substring(i, j);
        }
        int dot = name.indexOf('.');
        if (dot > 0) {
            name = name.substring(dot + 1);
        }
        name = name.trim();
        return name.isEmpty() ? Optional.empty() : Optional.of(name);
    }

    private static int skipWs(String str, int idx) {
        while (idx < str.length() && Character.isWhitespace(str.charAt(idx))) {
            idx++;
        }
        return idx;
    }
}
