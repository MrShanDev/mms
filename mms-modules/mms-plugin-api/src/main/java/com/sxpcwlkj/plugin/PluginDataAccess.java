package com.sxpcwlkj.plugin;

import java.util.List;
import java.util.Map;

/**
 * 插件自有表的受控 JDBC 访问；须配合 {@code plugin.json} 中的 {@code pluginTablePrefix}，
 * 由宿主校验 SQL（禁止多语句、禁止 DDL、须包含前缀）。
 * <p>宿主可对 {@code SELECT/UPDATE/DELETE} 在存在 Web 租户上下文时自动追加 {@code tenant_id = ?}（配置项见宿主
 * {@code mms.plugin.plugin-data-access-auto-tenant-enabled}）；INSERT 不自动改写。SQL 已显式包含租户列时不再重复拼接。</p>
 */
public interface PluginDataAccess {

    /**
     * 仅允许 SELECT；使用 {@code?} 占位符与参数列表（与 {@link org.springframework.jdbc.core.JdbcTemplate} 一致）。
     */
    List<Map<String, Object>> queryForList(String sql, Object... args);

    /**
     * INSERT / UPDATE / DELETE；参数同上。
     */
    int update(String sql, Object... args);
}
