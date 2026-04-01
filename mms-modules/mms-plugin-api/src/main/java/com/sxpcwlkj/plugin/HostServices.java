package com.sxpcwlkj.plugin;

import java.util.Optional;

/**
 * 宿主向插件暴露的受控能力门面（无 Spring 类型，便于 {@code mms-plugin-api} 零 Spring 依赖）。
 * <p>契约版本：插件在 {@code plugin.json} 中声明 {@code hostServicesContractVersion}（最低要求），
 * 宿主通过 {@link #hostImplementedContractVersion()} 声明实现版本；仅当宿主实现版本不低于插件要求时才允许加载。</p>
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
}
