package com.sxpcwlkj.plugin.host;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 插件宿主行为开关与路径。
 */
@Data
@ConfigurationProperties(prefix = "mms.plugin")
public class PluginHostProperties {

    /**
     * 是否在启动时加载插件并调用 {@link com.sxpcwlkj.plugin.MmsPlugin#onLoad}。
     */
    private boolean enabled = false;

    /**
     * 插件根目录；默认可由配置覆盖，为空时使用 user.dir/mms-plugins。
     */
    private String rootDir;

    /**
     * 插件按 MDC 拆分的日志目录（与 mms-admin {@code logback.xml} 中 {@code logs/plugins} 一致）。
     * 为空时使用 {@code user.dir}/logs/plugins。
     */
    private String pluginLogDir;

    /**
     * 与主工程 pom {@code revision} 及 {@link com.sxpcwlkj.plugin.PluginDescriptor} 校验一致。
     */
    private int hostMmsRevision = 21;

    /**
     * {@link com.sxpcwlkj.plugin.PluginDataAccess} 通过 {@link org.springframework.jdbc.core.JdbcTemplate#setQueryTimeout(int)} 限制语句超时（秒），≤0 表示使用驱动默认。
     */
    private int dataAccessQueryTimeoutSeconds = 30;

    /**
     * 是否为 {@link com.sxpcwlkj.plugin.PluginRuntimeMode#INDEPENDENT_PROCESS} 插件额外启动子进程（classpath 为版本目录下 {@code lib/*}）。
     */
    private boolean subprocessLaunchEnabled = false;

    /**
     * 子进程使用的 java 可执行文件（默认 {@code java}，依赖 PATH）。
     */
    private String subprocessJavaBinary = "java";

    /**
     * 预留：自动分配端口范围下界（当前仍以 descriptor.independentPort 为准）。
     */
    private int subprocessPortRangeMin = 28000;

    /**
     * 预留：自动分配端口范围上界。
     */
    private int subprocessPortRangeMax = 29999;

    /**
     * 停止子进程时先 {@link Process#destroy()} 后等待秒数，超时则 {@link Process#destroyForcibly()}。
     */
    private int subprocessShutdownGracePeriodSeconds = 15;

    /**
     * 子进程调用宿主受限接口时 HTTP Header {@code X-Mms-Plugin-Subprocess-Token}；为空则不设置环境变量且接口返回未配置。
     */
    private String subprocessAdminToken = "";

    /**
     * 写入子进程环境变量 {@code MMS_PLUGIN_HOST_CONTEXT_BASE}，便于插件拼接
     * {@code /system/pluginHost/subprocessPeer/context}；为空则由子进程缺省使用 {@code http://127.0.0.1:端口} 等自建。
     */
    private String subprocessPeerContextBaseUrl = "";

    /**
     * {@link #subprocessPeerContext} 返回给子进程的登记租户 ID（与库表插件登记默认租户一致，默认 000000）。
     */
    private String subprocessPeerRegistryTenantId = "000000";

    /**
     * fork 子进程前再次校验 TCP 端口「本机可绑定」（与 {@link PortLeaseTracker} 一致）；若已被占用则拒绝启动并写入 lastError。
     */
    private boolean subprocessPortDoubleCheckBeforeLaunch = true;

    /**
     * 停止子进程前在版本目录写入 {@code .mms-plugin-shutdown}，并设置环境变量 {@code MMS_PLUGIN_SHUTDOWN_FILE}；子进程可轮询该文件主动退出。
     */
    private boolean subprocessShutdownSignalFileEnabled = false;

    /**
     * 替换同插件版本时，在启动新子进程前额外等待（毫秒），便于旧进程释放端口；0 关闭。
     */
    private int subprocessRollingStopDelayMillis = 0;

    /**
     * {@link PluginHostController#activateVersion} 后的重载范围：默认全量；{@link ActivateVersionReloadScope#SINGLE_TARGET} 仅重载指定版本（依赖拓扑不保证）。
     */
    private ActivateVersionReloadScope activateVersionReloadScope = ActivateVersionReloadScope.FULL;

    /**
     * 连续业务失败次数达到阈值后，对该 pluginId 的 HOST_MVC 请求短期熔断；≤0 关闭。
     */
    private int pluginMvcCircuitFailureThreshold = 5;

    /**
     * 熔断打开时长（秒）。
     */
    private int pluginMvcCircuitOpenSeconds = 60;

    /**
     * 达到 {@link #pluginMvcCircuitFailureThreshold} 时是否<strong>卸载</strong>该插件所有已加载版本（告警 ERROR 日志）；为 false 时仅打开限时熔断窗口。
     */
    private boolean pluginMvcCircuitTripUnloadsPlugin = true;

    /**
     * 每 pluginId 每分钟最大 HOST_MVC 请求数；≤0 不限流。
     */
    private int pluginMvcRateLimitPerMinute = 0;

    /**
     * {@code /system/pluginHost/invoke} 每 pluginId 每分钟最大次数（独立计数）；≤0 不限流。
     */
    private int pluginInvokeRateLimitPerMinute = 0;

    /**
     * HOST_MVC 每 pluginId 隔离线程池大小；≤0 则在请求线程同步执行。
     */
    private int pluginMvcPerPluginPoolSize = 4;

    /**
     * 上述线程池队列长度。
     */
    private int pluginMvcPerPluginQueueCapacity = 200;

    /**
     * 插件控制器方法在隔离池中的最长执行时间（秒）。
     */
    private int pluginMvcInvokeTimeoutSeconds = 60;

    /**
     * 是否将插件 {@code PluginDataAccess} 执行的 SQL 记结构审计日志（INFO，含截断后的 SQL 与参数个数）。
     */
    private boolean pluginDataAccessAuditLogEnabled = true;

    /**
     * 当 {@link com.sxpcwlkj.plugin.HostDataService#tryCurrentTenantId()} 非空时，是否为 {@code SELECT/UPDATE/DELETE}
     * 自动追加租户列条件（物理列名见 {@link #pluginDataAccessTenantColumn}）；INSERT 不自动改写。
     */
    private boolean pluginDataAccessAutoTenantEnabled = true;

    /**
     * 自动租户拼接使用的列名（须为简单标识符，默认 {@code tenant_id}）。
     */
    private String pluginDataAccessTenantColumn = "tenant_id";
}
