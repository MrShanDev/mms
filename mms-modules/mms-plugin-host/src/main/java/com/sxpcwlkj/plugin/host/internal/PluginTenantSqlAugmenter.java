package com.sxpcwlkj.plugin.host.internal;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 在存在 Web 租户上下文时，为插件 {@code SELECT/UPDATE/DELETE} 追加 {@code tenant_id = ?}（INSERT 不自动改写，避免误伤列顺序）。
 */
public final class PluginTenantSqlAugmenter {

    private static final Pattern COLUMN_WORD = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    public record Result(String sql, Object[] args, boolean tenantAppended) {}

    private PluginTenantSqlAugmenter() {
    }

    /**
     * @param tenantColumn 物理列名，须为简单标识符
     */
    public static Result applyIfEnabled(String sql, Object[] args, String tenantColumn, String tenantId) {
        if (sql == null || sql.isBlank()) {
            return new Result(sql, args, false);
        }
        if (tenantId == null || tenantId.isBlank()
                || tenantColumn == null
                || !COLUMN_WORD.matcher(tenantColumn.trim()).matches()) {
            return new Result(sql, args, false);
        }
        String col = tenantColumn.trim();
        String trimmed = sql.trim();
        String lower = trimmed.toLowerCase(Locale.ROOT);
        if (containsColumnReference(trimmed, col)) {
            return new Result(sql, args, false);
        }
        if (lower.startsWith("insert")) {
            return new Result(sql, args, false);
        }
        if (!(lower.startsWith("select") || lower.startsWith("update") || lower.startsWith("delete"))) {
            return new Result(sql, args, false);
        }
        int split = findClauseSplitIndex(lower);
        String main = trimmed.substring(0, split).trim();
        String tail = split < trimmed.length() ? trimmed.substring(split).trim() : "";
        String augmented;
        if (main.toLowerCase(Locale.ROOT).contains(" where ")) {
            augmented = main + " AND " + col + " = ?";
        } else {
            augmented = main + " WHERE " + col + " = ?";
        }
        if (!tail.isEmpty()) {
            augmented = augmented + " " + tail;
        }
        Object[] merged = appendTenantArg(args, tenantId);
        return new Result(augmented, merged, true);
    }

    private static boolean containsColumnReference(String sql, String column) {
        return Pattern.compile("\\b" + Pattern.quote(column) + "\\b", Pattern.CASE_INSENSITIVE).matcher(sql).find();
    }

    /**
     * 在首个 ORDER BY / LIMIT / GROUP BY / HAVING / OFFSET / FOR UPDATE 之前切段，避免打断排序子句。
     */
    private static int findClauseSplitIndex(String sqlLower) {
        int best = sqlLower.length();
        String[] keys = {
            " order by ",
            " limit ",
            " group by ",
            " having ",
            " offset ",
            " for update ",
        };
        for (String k : keys) {
            int i = sqlLower.indexOf(k);
            if (i >= 0 && i < best) {
                best = i;
            }
        }
        return best;
    }

    private static Object[] appendTenantArg(Object[] args, String tenantId) {
        if (args == null || args.length == 0) {
            return new Object[] {tenantId};
        }
        Object[] n = Arrays.copyOf(args, args.length + 1);
        n[args.length] = tenantId;
        return n;
    }
}
