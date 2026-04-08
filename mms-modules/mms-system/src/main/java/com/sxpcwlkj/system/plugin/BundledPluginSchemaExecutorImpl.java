package com.sxpcwlkj.system.plugin;

import com.sxpcwlkj.plugin.BundledInstallSqlExecutionException;
import com.sxpcwlkj.plugin.BundledInstallSqlResult;
import com.sxpcwlkj.plugin.BundledInstallSqlSupport;
import com.sxpcwlkj.plugin.BundledPluginSchemaExecutor;
import com.sxpcwlkj.plugin.BundledPluginSchemaSupport;
import com.sxpcwlkj.plugin.BundledPluginSqlSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
    public BundledInstallSqlResult executeBundledInstallSql(String sql) {
        List<String> lines = new ArrayList<>();
        List<String> insertedFunctionIds = new ArrayList<>();
        List<String> insertedDictIds = new ArrayList<>();
        List<String> insertedDictDataIds = new ArrayList<>();
        List<String> stmts = BundledPluginSqlSplitter.splitStatements(sql);
        if (stmts.isEmpty()) {
            lines.add("（install.sql 无有效 SQL 语句，跳过）");
            return new BundledInstallSqlResult(lines, insertedFunctionIds, insertedDictIds, insertedDictDataIds);
        }
        if (stmts.size() > MAX_STATEMENTS) {
            throw new IllegalArgumentException("install.sql 语句条数超过上限 " + MAX_STATEMENTS);
        }
        int unitCount = 0;
        for (String raw : stmts) {
            String stmt = raw.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            if (stmt.length() > MAX_STATEMENT_LEN) {
                throw new IllegalArgumentException("单条语句过长（>" + MAX_STATEMENT_LEN + "）");
            }
            unitCount += BundledInstallSqlSupport.expandToSingleRowInserts(stmt).size();
        }
        if (unitCount > MAX_STATEMENTS) {
            throw new IllegalArgumentException("install.sql 展开后的插入条数超过上限 " + MAX_STATEMENTS);
        }
        int n = 0;
        for (String raw : stmts) {
            String stmt = raw.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            List<String> units = BundledInstallSqlSupport.expandToSingleRowInserts(stmt);
            for (String unit : units) {
                if (unit.length() > MAX_STATEMENT_LEN) {
                    throw new IllegalArgumentException("单条语句过长（>" + MAX_STATEMENT_LEN + "）");
                }
                BundledPluginInstallSqlGuard.assertStatementAllowed(unit);
                n++;
                BundledInstallSqlSupport.InstallSqlInsertKind kind =
                        BundledInstallSqlSupport.classifyInstallInsert(unit);
                if (kind == BundledInstallSqlSupport.InstallSqlInsertKind.UNSUPPORTED) {
                    throw new IllegalArgumentException("install.sql 含不支持的 INSERT（仅允许 sys_function/sys_dict/sys_dict_data）");
                }
                String rowId = BundledInstallSqlSupport.extractFirstInsertRowId(unit);
                if (rowId != null && !rowId.isBlank()) {
                    String idTrim = rowId.trim();
                    String skipWhat = null;
                    switch (kind) {
                        case SYS_FUNCTION -> {
                            if (sysFunctionIdExists(idTrim)) {
                                skipWhat = "sys_function.id";
                            }
                        }
                        case SYS_DICT -> {
                            if (sysDictIdExists(idTrim)) {
                                skipWhat = "sys_dict.id";
                            }
                        }
                        case SYS_DICT_DATA -> {
                            if (sysDictDataIdExists(idTrim)) {
                                skipWhat = "sys_dict_data.id";
                            }
                        }
                        default -> {
                        }
                    }
                    if (skipWhat != null) {
                        lines.add(
                                String.format(
                                        Locale.ROOT,
                                        "[SKIP] #%d %s 已存在，已跳过 — %s",
                                        n,
                                        skipWhat,
                                        preview(unit)));
                        continue;
                    }
                }
                long t0 = System.currentTimeMillis();
                try {
                    jdbcTemplate.execute((Connection c) -> {
                        try (Statement st = c.createStatement()) {
                            st.execute(unit);
                        }
                        return null;
                    });
                    lines.add(String.format(Locale.ROOT, "[OK] #%d (%d ms) %s", n, System.currentTimeMillis() - t0, preview(unit)));
                    if (rowId != null && !rowId.isBlank()) {
                        String idTrim = rowId.trim();
                        switch (kind) {
                            case SYS_FUNCTION -> insertedFunctionIds.add(idTrim);
                            case SYS_DICT -> insertedDictIds.add(idTrim);
                            case SYS_DICT_DATA -> insertedDictDataIds.add(idTrim);
                            default -> {
                            }
                        }
                    }
                } catch (Exception e) {
                    if (isDuplicateKeyException(e)) {
                        lines.add(String.format(Locale.ROOT, "[SKIP] #%d 主键或唯一约束重复，已跳过 — %s", n, preview(unit)));
                        continue;
                    }
                    lines.add(String.format(Locale.ROOT, "[FAIL] #%d %s — %s", n, preview(unit), e.getMessage()));
                    throw new BundledInstallSqlExecutionException(
                            new ArrayList<>(lines),
                            new ArrayList<>(insertedFunctionIds),
                            new ArrayList<>(insertedDictIds),
                            new ArrayList<>(insertedDictDataIds),
                            "install.sql 第 " + n + " 条失败: " + e.getMessage(),
                            e);
                }
            }
        }
        lines.add("install.sql 共处理 " + n + " 条插入（含按主键预检跳过与重复键跳过）。");
        return new BundledInstallSqlResult(lines, insertedFunctionIds, insertedDictIds, insertedDictDataIds);
    }

    private boolean sysFunctionIdExists(String id) {
        try {
            Number c =
                    jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM sys_function WHERE id = ?", Number.class, id);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sysDictIdExists(String id) {
        try {
            Number c =
                    jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_dict WHERE id = ?", Number.class, id);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sysDictDataIdExists(String id) {
        try {
            Number c =
                    jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM sys_dict_data WHERE id = ?", Number.class, id);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isDuplicateKeyException(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof java.sql.SQLException se) {
                String state = se.getSQLState();
                if ("23000".equals(state) || "23505".equals(state)) {
                    return true;
                }
                int code = se.getErrorCode();
                if (code == 1062 || code == 2627) {
                    return true;
                }
            }
            String m = t.getMessage();
            if (m != null) {
                String lower = m.toLowerCase(Locale.ROOT);
                if (lower.contains("duplicate entry")
                        || lower.contains("duplicate key")
                        || lower.contains("unique constraint")
                        || lower.contains("already exists")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> executeBundledSchema(String sql) {
        List<String> lines = new ArrayList<>();
        List<String> stmts = BundledPluginSqlSplitter.splitStatements(sql);
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
            Optional<String> createTable = BundledPluginSchemaSupport.extractCreateTableName(stmt);
            if (createTable.isPresent() && physicalTableExists(createTable.get())) {
                n++;
                lines.add(
                        String.format(
                                Locale.ROOT,
                                "[SKIP] #%d 物理表已存在，跳过 CREATE TABLE — %s",
                                n,
                                preview(stmt)));
                continue;
            }
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

    private boolean physicalTableExists(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            return false;
        }
        String tn = tableName.trim();
        try {
            Number c =
                    jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND LOWER(table_name) = LOWER(?)",
                            Number.class,
                            tn);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static String preview(String stmt) {
        String one = stmt.replace('\n', ' ').trim();
        return one.length() > 160 ? one.substring(0, 157) + "…" : one;
    }
}
