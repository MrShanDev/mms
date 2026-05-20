package com.sxpcwlkj.plugin;

/**
 * 插件 JAR 上传安装流（{@code POST .../installStream}，NDJSON）结束时 {@code type=done} 行中的稳定 {@code code}，
 * 供前端与自动化区分成功/失败原因（与 {@code msg} 人机文案解耦）。
 * <p>
 * 约定：成功时 {@code ok=true} 且 {@code code} 为 {@link #SUCCESS}；失败时 {@code ok=false} 且 {@code code} 为本类中某一非 SUCCESS 常量。
 * </p>
 */
public final class PluginInstallStreamResultCode {

    private PluginInstallStreamResultCode() {
    }

    /** 安装流程正常结束。 */
    public static final String SUCCESS = "PLUGIN_INSTALL_SUCCESS";

    /** 无安装权限或角色不足。 */
    public static final String REQUEST_FORBIDDEN = "PLUGIN_INSTALL_REQUEST_FORBIDDEN";

    /** 请求未携带文件或文件为空。 */
    public static final String REQUEST_FILE_EMPTY = "PLUGIN_INSTALL_REQUEST_FILE_EMPTY";

    /** 原始文件名非 {@code .jar}。 */
    public static final String REQUEST_NOT_JAR = "PLUGIN_INSTALL_REQUEST_NOT_JAR";

    /** 服务端接收 multipart 落临时文件失败。 */
    public static final String REQUEST_UPLOAD_FAILED = "PLUGIN_INSTALL_REQUEST_UPLOAD_FAILED";

    /** JAR 内含 schema.sql 但未启用 {@link BundledPluginSchemaExecutor}。 */
    public static final String SCHEMA_EXECUTOR_MISSING = "PLUGIN_INSTALL_SCHEMA_EXECUTOR_MISSING";

    /** 执行 JAR 内 {@code META-INF/mms/schema.sql} 失败。 */
    public static final String SCHEMA_EXECUTION_FAILED = "PLUGIN_INSTALL_SCHEMA_EXECUTION_FAILED";

    /** 执行 JAR 内 {@code script/install.sql} 失败（白名单校验或执行异常，不含主键重复跳过）。 */
    public static final String INSTALL_SQL_EXECUTION_FAILED = "PLUGIN_INSTALL_INSTALL_SQL_EXECUTION_FAILED";

    /** 磁盘安装阶段：{@link PluginException}（校验、兼容性、依赖指纹等）。 */
    public static final String JAR_VALIDATION_FAILED = "PLUGIN_INSTALL_JAR_VALIDATION_FAILED";

    /** 磁盘安装阶段：其它 I/O 或解析异常。 */
    public static final String JAR_INSTALL_FAILED = "PLUGIN_INSTALL_JAR_INSTALL_FAILED";

    /** 库表登记/菜单同步失败且已尝试回滚磁盘。 */
    public static final String DATABASE_REGISTRATION_FAILED = "PLUGIN_INSTALL_DATABASE_REGISTRATION_FAILED";

    /** 向客户端写入 NDJSON 失败（如连接中断）。 */
    public static final String STREAM_IO_FAILED = "PLUGIN_INSTALL_STREAM_IO_FAILED";

    /** 流式安装 lambda 内未预期的异常。 */
    public static final String STREAM_INTERNAL_ERROR = "PLUGIN_INSTALL_STREAM_INTERNAL_ERROR";
}
