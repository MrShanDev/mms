package com.sxpcwlkj.plugin;

import java.util.List;
import java.util.Optional;

/**
 * 宿主向插件暴露的受控能力门面（无 Spring 类型，便于 {@code mms-plugin-api} 零 Spring 依赖）。
 * <p>契约版本：插件在 {@code plugin.json} 中声明 {@code hostServicesContractVersion}（最低要求），
 * 宿主通过 {@link #hostImplementedContractVersion()} 声明实现版本；仅当宿主实现版本不低于插件要求时才允许加载。</p>
 * <p><b>版本 3</b>：增加 {@link #pluginSysConfigGet} / {@link #pluginSysConfigPut} / {@link #pluginSysConfigList}，
 * 读写 {@code sys_config} 中键前缀为 {@link PluginSysConfigKeys#PREFIX} 的插件专属配置（持久化）。</p>
 * <p><b>版本 4</b>：增加 {@link #pluginSchemaAccess(PluginDescriptor)}，提供本插件前缀下的受限 DDL 与表列举（见 {@link PluginSchemaAccess}）。</p>
 * <p><b>版本 5</b>：增加 {@link #pluginBackupAccess(PluginDescriptor)}（仅当插件描述 {@code backupOperator=true}），见 {@link PluginBackupAccess}。</p>
 */
public interface HostServices {

    /**
     * 当前宿主实现的 HostServices 契约版本（随破坏性 API 变更递增）。
     */
    int hostImplementedContractVersion();

    /**
     * 读取字符串缓存（通常由 Redis 支撑）；不可用或键不存在时返回 empty。
     */
    Optional<String> cacheGetString(String key);

    /**
     * 写入字符串缓存；{@code ttlSeconds &lt;= 0} 时使用宿主默认过期策略。
     */
    void cachePutString(String key, String value, int ttlSeconds);

    /**
     * 宿主系统用户/租户等读能力（由 {@code mms-system} 等模块桥接实现）。
     */
    HostDataService hostData();

    /**
     * 基于当前插件描述符的受控 JDBC；需在 {@link PluginDescriptor#getPluginTablePrefix()} 声明前缀。
     */
    PluginDataAccess pluginDataAccess(PluginDescriptor forPlugin);

    /**
     * 本插件前缀下的受限 DDL / 表列举；须在 {@code plugin.json} 声明 {@code pluginTablePrefix}，且插件要求的
     * {@link PluginDescriptor#getHostServicesContractVersion()} 不得超过宿主 {@link #hostImplementedContractVersion()}。
     */
    PluginSchemaAccess pluginSchemaAccess(PluginDescriptor forPlugin);

    /**
     * 库级逻辑备份/还原；{@link PluginDescriptor#getBackupOperator()} 须为 {@link Boolean#TRUE}。
     */
    PluginBackupAccess pluginBackupAccess(PluginDescriptor forPlugin);

    /**
     * 在宿主已配置事务管理器时，以可写事务边界执行（传播行为与默认 {@code TransactionTemplate} 一致）。
     * 无事务管理器时抛出 {@link PluginException}。
     */
    void runInWritableTransaction(Runnable work);

    /**
     * 只读事务；无事务管理器时退化为直接执行 {@code work}（适用于启动期无 DataSource 场景）。
     */
    void runInReadOnlyTransaction(Runnable work);

    /**
     * 当前 Web 会话是否具备指定权限码（与管理端 Sa-Token 权限列表一致）；未登录或校验失败返回 {@code false}。
     */
    boolean hasWebPermission(String permissionCode);

    /**
     * 读取当前租户下插件专属配置；键由 {@link PluginSysConfigKeys#fullKey(String, String)} 拼接。
     * 无登录上下文时租户按 {@code 000000} 与 {@link HostDataService#tryCurrentTenantId()} 回退规则处理。
     */
    Optional<String> pluginSysConfigGet(PluginDescriptor plugin, String keySuffix);

    /**
     * 插入或更新插件专属配置；{@code configName} 可为展示名（如「企微 Webhook」）。
     */
    void pluginSysConfigPut(PluginDescriptor plugin, String keySuffix, String configName, String value);

    /**
     * 列出当前租户下该插件前缀下全部配置项。
     */
    List<PluginSysConfigRow> pluginSysConfigList(PluginDescriptor plugin);
}
