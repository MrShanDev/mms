package com.sxpcwlkj.system.plugin;

import com.sxpcwlkj.plugin.BundledPluginSchemaExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 执行插件 JAR 内 schema.sql（由宿主拆句后传入）。
 */
@Component
@RequiredArgsConstructor
public class BundledPluginSchemaExecutorImpl implements BundledPluginSchemaExecutor {

    private static final int MAX_STATEMENTS = 500;
    private static final int MAX_STATEMENT_LEN = 64_000;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> executeBundledSchema(String sql) {
        List<String> lines = new ArrayList<>();
        List<String> stmts = splitStatements(sql);
        if (stmts.isEmpty()) {
            lines.add("（无有效 SQL 语句，跳过）");
            return lines;
        }
        if (stmts.size() > MAX_STATEMENTS) {
            throw new IllegalArgumentException("语句条数超过上限 " + MAX_STATEMENTS);
        }
        int n = 0;
        for (String raw : stmts) {
            String stmt = raw.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            if (stmt.length() > MAX_STATEMENT_LEN) {
                throw new IllegalArgumentException("单条语句过长（>" + MAX_STATEMENT_LEN + "）");
            }
            BundledPluginSchemaSqlGuard.assertStatementAllowed(stmt);
            n++;
            long t0 = System.currentTimeMillis();
            try {
                jdbcTemplate.execute((Connection c) -> {
                    try (Statement st = c.createStatement()) {
                        st.execute(stmt);
                    }
                    return null;
                });
                lines.add(String.format(Locale.ROOT, "[OK] #%d (%d ms) %s", n, System.currentTimeMillis() - t0, preview(stmt)));
            } catch (Exception e) {
                lines.add(String.format(Locale.ROOT, "[FAIL] #%d %s — %s", n, preview(stmt), e.getMessage()));
                throw new IllegalStateException("执行第 " + n + " 条失败: " + e.getMessage(), e);
            }
        }
        lines.add("共执行 " + n + " 条语句。");
        return lines;
    }

    private static String preview(String stmt) {
        String one = stmt.replace('\n', ' ').trim();
        return one.length() > 160 ? one.substring(0, 157) + "…" : one;
    }

    static List<String> splitStatements(String sql) {
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
