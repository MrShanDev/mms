package com.sxpcwlkj.system.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 市场「执行插件自带 schema.sql」白名单：仅允许建表/索引/安全 ALTER。
 * 字典 {@code sys_dict}/{@code sys_dict_data} 须写入 {@code script/install.sql}，与菜单同属安装 SQL，便于回滚与卸载清理。
 */
public final class BundledPluginSchemaSqlGuard {

    /**
     * 插件经 schema 写入字典时，{@code sys_dict.field_name} / {@code sys_dict_data.field_name}（即前端 dict-type）须此前缀，避免与平台字典冲突。
     */
    public static final String BUNDLED_PLUGIN_DICT_FIELD_NAME_PREFIX = "mms_plugin_";

    private static final Pattern SYS_DIR =
            Pattern.compile(
                    "\\b(information_schema|pg_catalog|mysql\\s*\\.|sys\\.|performance_schema)\\b",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern INSERT_INTO_HEAD =
            Pattern.compile(
                    "(?is)^INSERT\\s+(IGNORE\\s+)?INTO\\s+[`\"]?([a-zA-Z0-9_]+)[`\"]?\\s*\\(");

    private BundledPluginSchemaSqlGuard() {}

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
        String u = s.toUpperCase(Locale.ROOT);
        if (u.startsWith("CREATE ")) {
            if (u.startsWith("CREATE TABLE ")
                    || u.startsWith("CREATE INDEX ")
                    || u.startsWith("CREATE UNIQUE INDEX ")
                    || u.startsWith("CREATE FULLTEXT INDEX ")) {
                return;
            }
            throw new IllegalArgumentException("CREATE 仅允许 TABLE / INDEX");
        }
        if (u.startsWith("ALTER TABLE ")) {
            String tail = u.substring("ALTER TABLE ".length()).trim();
            int sp = tail.indexOf(' ');
            if (sp > 0) {
                String rest = tail.substring(sp + 1).trim().toUpperCase(Locale.ROOT);
                if (rest.startsWith("ADD ")
                        || rest.startsWith("ADD COLUMN ")
                        || rest.startsWith("DROP COLUMN ")
                        || rest.startsWith("MODIFY ")
                        || rest.startsWith("CHANGE ")) {
                    return;
                }
            }
            throw new IllegalArgumentException("ALTER TABLE 仅允许 ADD/DROP/MODIFY/CHANGE 等列级变更");
        }
        throw new IllegalArgumentException("仅允许 CREATE TABLE/INDEX、受限 ALTER TABLE（字典请放入 script/install.sql）");
    }

    /**
     * 校验插件 {@code install.sql} 中的字典 INSERT；{@code field_name} 须带 {@link #BUNDLED_PLUGIN_DICT_FIELD_NAME_PREFIX}。
     */
    public static void assertBundledDictInsertAllowed(String s) {
        Matcher m = INSERT_INTO_HEAD.matcher(s);
        if (!m.find()) {
            throw new IllegalArgumentException("字典插入须为 INSERT [IGNORE] INTO `sys_dict`|`sys_dict_data` (`列`...) VALUES (...)，且须带显式列清单");
        }
        String table = m.group(2).toLowerCase(Locale.ROOT);
        if (!"sys_dict".equals(table) && !"sys_dict_data".equals(table)) {
            throw new IllegalArgumentException("插件 schema 的 INSERT 仅允许目标表 sys_dict、sys_dict_data");
        }
        int colsOpen = m.end() - 1;
        if (colsOpen < 0 || colsOpen >= s.length() || s.charAt(colsOpen) != '(') {
            throw new IllegalArgumentException("字典 INSERT 列清单解析失败");
        }
        int colsClose = indexOfMatchingParen(s, colsOpen);
        if (colsClose < 0) {
            throw new IllegalArgumentException("字典 INSERT 列清单括号不匹配");
        }
        List<String> columns = splitTopLevelCommaList(s, colsOpen + 1, colsClose);
        int fnIdx = -1;
        for (int i = 0; i < columns.size(); i++) {
            if ("field_name".equalsIgnoreCase(stripColumnName(columns.get(i)))) {
                fnIdx = i;
                break;
            }
        }
        if (fnIdx < 0) {
            throw new IllegalArgumentException("字典 INSERT 必须包含 field_name 列");
        }
        int valuesPos = indexOfKeywordIgnoreCase(s, "VALUES", colsClose + 1);
        if (valuesPos < 0) {
            throw new IllegalArgumentException("字典 INSERT 必须包含 VALUES");
        }
        int valsOpen = s.indexOf('(', valuesPos + 5);
        if (valsOpen < 0) {
            throw new IllegalArgumentException("字典 INSERT VALUES 后须有值列表");
        }
        int valsClose = indexOfMatchingParen(s, valsOpen);
        if (valsClose < 0) {
            throw new IllegalArgumentException("字典 INSERT 值列表括号不匹配");
        }
        int tail = valsClose + 1;
        while (tail < s.length() && Character.isWhitespace(s.charAt(tail))) {
            tail++;
        }
        if (tail < s.length()) {
            throw new IllegalArgumentException("字典 INSERT 暂不支持单语句多组 VALUES；请拆成多条 INSERT");
        }
        List<String> values = splitTopLevelCommaList(s, valsOpen + 1, valsClose);
        if (fnIdx >= values.size()) {
            throw new IllegalArgumentException("字典 INSERT 列与值数量不匹配");
        }
        String fieldNameVal = parseFieldNameStringLiteral(values.get(fnIdx));
        String lower = fieldNameVal.toLowerCase(Locale.ROOT);
        if (!lower.startsWith(BUNDLED_PLUGIN_DICT_FIELD_NAME_PREFIX.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException(
                    "sys_dict/sys_dict_data 的 field_name 必须以 "
                            + BUNDLED_PLUGIN_DICT_FIELD_NAME_PREFIX
                            + " 开头，实际为: "
                            + fieldNameVal);
        }
    }

    private static String stripColumnName(String col) {
        String t = col.trim();
        if (t.length() >= 2 && t.charAt(0) == '`' && t.charAt(t.length() - 1) == '`') {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }

    private static String parseFieldNameStringLiteral(String raw) {
        String t = raw.trim();
        if (t.length() < 2 || t.charAt(0) != '\'') {
            throw new IllegalArgumentException("field_name 必须为单引号字符串字面量");
        }
        StringBuilder sb = new StringBuilder();
        int j = 1;
        while (j < t.length()) {
            char ch = t.charAt(j);
            if (ch == '\'') {
                if (j + 1 < t.length() && t.charAt(j + 1) == '\'') {
                    sb.append('\'');
                    j += 2;
                    continue;
                }
                if (j == t.length() - 1) {
                    return sb.toString();
                }
                throw new IllegalArgumentException("field_name 字符串字面量格式错误");
            }
            sb.append(ch);
            j++;
        }
        throw new IllegalArgumentException("field_name 字符串字面量未闭合");
    }

    private static int indexOfKeywordIgnoreCase(String s, String kw, int from) {
        int i = from;
        int n = kw.length();
        while (i <= s.length() - n) {
            if (s.regionMatches(true, i, kw, 0, n)) {
                boolean leftOk = i == 0 || !Character.isLetterOrDigit(s.charAt(i - 1));
                boolean rightOk =
                        i + n >= s.length() || !Character.isLetterOrDigit(s.charAt(i + n));
                if (leftOk && rightOk) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    private static List<String> splitTopLevelCommaList(String s, int start, int end) {
        List<String> parts = new ArrayList<>();
        int segStart = start;
        int depthParen = 0;
        int i = start;
        while (i < end) {
            char c = s.charAt(i);
            if (c == '(') {
                depthParen++;
                i++;
            } else if (c == ')') {
                depthParen--;
                i++;
            } else if (c == '\'' && depthParen == 0) {
                i = skipSqlStringLiteralEnd(s, i) + 1;
            } else if (c == '`' && depthParen == 0) {
                int close = s.indexOf('`', i + 1);
                if (close < 0) {
                    break;
                }
                i = close + 1;
            } else if (c == ',' && depthParen == 0) {
                parts.add(s.substring(segStart, i).trim());
                i++;
                segStart = i;
            } else {
                i++;
            }
        }
        if (segStart < end) {
            parts.add(s.substring(segStart, end).trim());
        }
        return parts;
    }

    private static int skipSqlStringLiteralEnd(String s, int openQuoteIdx) {
        int j = openQuoteIdx + 1;
        while (j < s.length()) {
            char ch = s.charAt(j);
            if (ch == '\'') {
                if (j + 1 < s.length() && s.charAt(j + 1) == '\'') {
                    j += 2;
                    continue;
                }
                return j;
            }
            j++;
        }
        return s.length() - 1;
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
            } else if (c == '\'') {
                i = skipSqlStringLiteralEnd(s, i);
            }
        }
        return -1;
    }
}
