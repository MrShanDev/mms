package com.sxpcwlkj.plugin;

/**
 * JAR 插件：插件 JAR 与安装目录中的固定路径约定。
 */
public final class PluginConstants {

    private PluginConstants() {
    }

    /**
     * 描述文件在 JAR 内的首选路径（推荐）。
     */
    public static final String DESCRIPTOR_PATH_IN_JAR = "META-INF/mms/plugin.json";

    /**
     * 可选：依赖指纹清单路径（JAR 内 UTF-8 文本）。
     * <p><b>GAV 生成规范</b>：每行一条 Maven 坐标 {@code groupId:artifactId:version}（无多余空格），
     * 按字典序排序整文件后写入；{@link PluginDescriptor#getDependencyFingerprintSha256()} 为该文件<strong>原始字节</strong>
     * 的 SHA-256 十六进制（小写存库，校验时大小写不敏感）。可用 {@code mvn dependency:list} 再配合脚本排序去重后生成。</p>
     */
    public static final String DEPS_FINGERPRINT_MANIFEST = "META-INF/mms/deps-fingerprint.manifest";

    /**
     * 描述文件在 JAR 根下的兼容路径（可选用）。
     */
    public static final String DESCRIPTOR_FALLBACK_PATH_IN_JAR = "plugin.json";

    /**
     * 磁盘安装目录下与 JAR 同级的描述副本文件名（宿主可选择在安装时复制）。
     */
    public static final String DESCRIPTOR_FILE_NAME = "plugin.json";

    /**
     * Java {@link java.util.ServiceLoader} 服务协议文件（插件提供 {@link MmsPlugin} 实现类全限定名，每行一个）。
     */
    public static final String SPI_SERVICES_RESOURCE = "META-INF/services/com.sxpcwlkj.plugin.MmsPlugin";

    /**
     * 默认插件根目录相对于工作目录的名称（可通过宿主配置覆盖，此处仅作文档化常量）。
     */
    public static final String DEFAULT_PLUGINS_DIR_NAME = "mms-plugins";

    /**
     * 单个插件安装目录下的推荐子目录：依赖/字节码。
     */
    public static final String SUBDIR_LIB = "lib";

    /**
     * 运行时数据（上传、缓存、H2 本地文件等），宿主应限制插件仅写入此树。
     */
    public static final String SUBDIR_DATA = "data";

    /**
     * 临时文件，宿主可定期清理。
     */
    public static final String SUBDIR_TMP = "tmp";
}
