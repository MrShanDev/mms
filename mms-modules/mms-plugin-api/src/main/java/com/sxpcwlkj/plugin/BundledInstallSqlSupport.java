package com.sxpcwlkj.plugin;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析 {@code script/install.sql} 中 {@code INSERT INTO sys_function}，供安装前跳过已存在主键、卸载时按 id 清理。
 */
public final class BundledInstallSqlSupport {

    private static final Pattern INSERT_SYS_FUNCTION_HEAD =
            Pattern.compile("^\\s*INSERT\\s+INTO\\s+[`\"]?sys_function[`\"]?\\s*\\(", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern VALUES_KW = Pattern.compile("\\bVALUES\\b", Pattern.CASE_INSENSITIVE);

    private BundledInstallSqlSupport() {}

    /**
     * 从整段 install.sql 脚本收集所有目标菜单主键 id（去重、保序）。
     */
    public static List<String> collectAllFunctionIds(String fullSql) {
        if (fullSql == null || fullSql.isBlank()) {
            return List.of();
        }
        Set<String> seen = new LinkedHashSet<>();
        for (String raw : BundledPluginSqlSplitter.splitStatements(fullSql)) {
            String stmt = raw.trim();
            if (stmt.isEmpty() || !INSERT_SYS_FUNCTION_HEAD.matcher(stmt).find()) {
                continue;
            }
            for (String unit : expandToSingleRowInserts(stmt)) {
                String id = extractFirstFunctionIdFromInsert(unit);
                if (id != null && !id.isBlank()) {
                    seen.add(id.trim());
                }
            }
        }
        return List.copyOf(seen);
    }

    /**
     * 将多行 {@code VALUES (..),(..)} 拆成多条单行 INSERT（列清单与前缀共用）；已是单行则原样返回一条。
     */
    public static List<String> expandToSingleRowInserts(String insertStmt) {
        if (insertStmt == null) {
            return List.of();
        }
        String stmt = insertStmt.trim();
        if (stmt.isEmpty()) {
            return List.of();
        }
        Matcher vm = VALUES_KW.matcher(stmt);
        if (!vm.find()) {
            return List.of(stmt);
        }
        int valuesEnd = vm.end();
        int open = stmt.indexOf('(', valuesEnd);
        if (open < 0) {
            return List.of(stmt);
        }
        String prefix = stmt.substring(0, open).trim();
        List<String> rowLiterals = new ArrayList<>();
        int i = open;
        while (i < stmt.length()) {
            i = skipWs(stmt, i);
            if (i >= stmt.length() || stmt.charAt(i) != '(') {
                break;
            }
            int close = indexOfMatchingParen(stmt, i);
            if (close < 0) {
                break;
            }
            rowLiterals.add(stmt.substring(i, close + 1));
            i = close + 1;
            i = skipWs(stmt, i);
            if (i < stmt.length() && stmt.charAt(i) == ',') {
                i++;
            }
        }
        if (rowLiterals.size() <= 1) {
            return List.of(stmt);
        }
        List<String> out = new ArrayList<>(rowLiterals.size());
        for (String row : rowLiterals) {
            out.add(prefix + " " + row);
        }
        return out;
    }

    /**
     * 从单条 {@code INSERT INTO sys_function ... VALUES (...)} 解析首列 {@code id}（字符串或数字字面量）。
     */
    public static String extractFirstFunctionIdFromInsert(String singleRowInsert) {
        if (singleRowInsert == null) {
            return null;
        }
        Matcher vm = VALUES_KW.matcher(singleRowInsert);
        if (!vm.find()) {
            return null;
        }
        int open = singleRowInsert.indexOf('(', vm.end());
        if (open < 0) {
            return null;
        }
        return parseFirstCommaField(singleRowInsert, open + 1);
    }

    private static String parseFirstCommaField(String s, int start) {
        int i = skipWs(s, start);
        if (i >= s.length()) {
            return null;
        }
        if (s.regionMatches(true, i, "NULL", 0, 4)
                && (i + 4 >= s.length() || !Character.isLetterOrDigit(s.charAt(i + 4)))) {
            return null;
        }
        char c = s.charAt(i);
        if (c == '\'' || c == '"') {
            return readQuoted(s, i, c);
        }
        if (c == '`') {
            int end = s.indexOf('`', i + 1);
            if (end < 0) {
                return null;
            }
            return s.substring(i + 1, end);
        }
        int j = i;
        while (j < s.length()) {
            char ch = s.charAt(j);
            if (ch == ',' || ch == ')') {
                break;
            }
            j++;
        }
        String raw = s.substring(i, j).trim();
        return raw.isEmpty() ? null : raw;
    }

    private static String readQuoted(String s, int openQuote, char q) {
        StringBuilder sb = new StringBuilder();
        int j = openQuote + 1;
        while (j < s.length()) {
            char ch = s.charAt(j);
            if (ch == q) {
                if (j + 1 < s.length() && s.charAt(j + 1) == q) {
                    sb.append(q);
                    j += 2;
                    continue;
                }
                break;
            }
            sb.append(ch);
            j++;
        }
        return sb.toString();
    }

    private static int indexOfMatchingParen(String s, int openIdx) {
        int depth = 0;
        for (int i = openIdx; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            } else if (c == '\'' || c == '"') {
                i = skipStringLiteral(s, i, c);
            }
        }
        return -1;
    }

    private static int skipStringLiteral(String s, int start, char q) {
        int j = start + 1;
        while (j < s.length()) {
            char ch = s.charAt(j);
            if (ch == q) {
                if (j + 1 < s.length() && s.charAt(j + 1) == q) {
                    j += 2;
                    continue;
                }
                return j;
            }
            j++;
        }
        return s.length() - 1;
    }

    private static int skipWs(String s, int i) {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return i;
    }
}
