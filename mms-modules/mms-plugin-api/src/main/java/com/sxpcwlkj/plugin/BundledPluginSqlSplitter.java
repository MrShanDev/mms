package com.sxpcwlkj.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 拆分插件包内 {@code schema.sql} / {@code script/install.sql} 文本为单条语句（去行注释、按分号断句）。
 */
public final class BundledPluginSqlSplitter {

    private BundledPluginSqlSplitter() {}

    public static List<String> splitStatements(String sql) {
        String cleaned = stripLineComments(sql);
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        for (String line : cleaned.split("\\R")) {
            String t = line.trim();
            if (t.isEmpty()) {
                continue;
            }
            if (t.contains("--")) {
                int i = t.indexOf("--");
                t = t.substring(0, i).trim();
                if (t.isEmpty()) {
                    continue;
                }
            }
            if (t.endsWith(";")) {
                cur.append(t.substring(0, t.length() - 1));
                String s = cur.toString().trim();
                if (!s.isEmpty()) {
                    out.add(s);
                }
                cur.setLength(0);
            } else {
                if (cur.length() > 0) {
                    cur.append('\n');
                }
                cur.append(t);
            }
        }
        String last = cur.toString().trim();
        if (!last.isEmpty()) {
            out.add(last);
        }
        return out;
    }

    private static String stripLineComments(String sql) {
        if (sql == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String line : sql.split("\\R")) {
            String t = line;
            int idx = t.indexOf("--");
            if (idx >= 0) {
                t = t.substring(0, idx);
            }
            sb.append(t).append('\n');
        }
        return sb.toString();
    }
}
