package com.sxpcwlkj.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link PluginDescriptor} 结构与宿主版本的兼容性校验。
 */
public final class PluginDescriptorValidator {

    private static final Pattern SEMVER_LIGHT = Pattern.compile("^\\d+\\.\\d+\\.\\d+([.-]\\S+)?$");
    private static final Pattern ID_PATTERN =
            Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_.-]*(\\.[a-zA-Z0-9][a-zA-Z0-9_.-]*)+$");
    private static final Pattern TABLE_PREFIX_PATTERN =
            Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,62}_$");
    private static final Pattern FINGERPRINT_SHA256 = Pattern.compile("^[0-9a-fA-F]{64}$");
    private static final Pattern DATA_TABLE_REL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");
    private static final int MIN_EPHEMERAL_PORT = 1024;
    private static final int MAX_USER_PORT = 65535;

    private PluginDescriptorValidator() {
    }

    /**
     * 仅校验描述文件字段完整性及版本串格式，不校验与宿主匹配。
     */
    public static List<String> validateStructure(PluginDescriptor d) {
        List<String> errors = new ArrayList<>();
        if (d == null) {
            errors.add("descriptor 为空");
            return errors;
        }
        if (d.getId() == null || d.getId().isBlank()) {
            errors.add("id 不能为空");
        } else if (!ID_PATTERN.matcher(d.getId().trim()).matches()) {
            errors.add("id 建议使用反向域名风格（多段以 . 分隔），仅含字母数字 ._- ：当前值为 " + d.getId());
        }
        if (d.getVersion() == null || d.getVersion().isBlank()) {
            errors.add("version 不能为空");
        } else if (!SEMVER_LIGHT.matcher(d.getVersion().trim()).matches()) {
            errors.add("version 建议使用 semver 主.次.补丁 形式，当前: " + d.getVersion());
        }
        if (d.getRequiresMms() == null) {
            errors.add("requiresMms 块不能为空（需声明 revisionMin 等）");
        } else {
            RequiresMmsDescriptor r = d.getRequiresMms();
            if (r.getRevisionMin() == null) {
                errors.add("requiresMms.revisionMin 不能为空");
            }
            if (r.getRevisionMax() != null && r.getRevisionMin() != null
                    && r.getRevisionMax() < r.getRevisionMin()) {
                errors.add("requiresMms.revisionMax 不可小于 revisionMin");
            }
        }
        if (d.getKind() == PluginKind.EXTENSION) {
            if (d.getEntryClass() == null || d.getEntryClass().isBlank()) {
                errors.add("kind=extension 时必须提供 entryClass（SPI 入口全限定名）");
            }
        }
        if (d.getDependencies() != null) {
            int i = 0;
            for (PluginDependencyDescriptor dep : d.getDependencies()) {
                if (dep.getId() == null || dep.getId().isBlank()) {
                    errors.add("dependencies[" + i + "].id 不能为空");
                }
                i++;
            }
        }
        validateRuntimeModeBlock(d, errors);
        if (d.getHostServicesContractVersion() != null) {
            int v = d.getHostServicesContractVersion();
            if (v < 1) {
                errors.add("hostServicesContractVersion 必须 >= 1");
            }
            if (v > 999) {
                errors.add("hostServicesContractVersion 超出合理范围");
            }
        }
        if (d.getPluginTablePrefix() != null && !d.getPluginTablePrefix().isBlank()) {
            String p = d.getPluginTablePrefix().trim();
            if (!TABLE_PREFIX_PATTERN.matcher(p).matches()) {
                errors.add("pluginTablePrefix 需匹配 [a-zA-Z][a-zA-Z0-9_]{0,62}_ ，当前: " + p);
            }
        }
        if (d.getDependencyFingerprintSha256() != null && !d.getDependencyFingerprintSha256().isBlank()) {
            if (!FINGERPRINT_SHA256.matcher(d.getDependencyFingerprintSha256().trim()).matches()) {
                errors.add("dependencyFingerprintSha256 须为 64 位十六进制 SHA-256");
            }
        }
        if (d.getPluginDataTables() != null && !d.getPluginDataTables().isEmpty()) {
            if (d.getPluginTablePrefix() == null || d.getPluginTablePrefix().isBlank()) {
                errors.add("声明 pluginDataTables 时必须同时配置 pluginTablePrefix");
            }
            int j = 0;
            for (String rel : d.getPluginDataTables()) {
                if (rel == null || rel.isBlank()) {
                    errors.add("pluginDataTables[" + j + "] 不能为空");
                } else if (!DATA_TABLE_REL_PATTERN.matcher(rel.trim()).matches()) {
                    errors.add("pluginDataTables[" + j + "] 须匹配 [a-zA-Z][a-zA-Z0-9_]* ，当前: " + rel);
                }
                j++;
            }
        }
        return errors;
    }

    private static void validateRuntimeModeBlock(PluginDescriptor d, List<String> errors) {
        PluginRuntimeMode mode = d.runtimeModeOrDefault();
        Integer port = d.getIndependentPort();
        String main = d.getMainClass() == null ? null : d.getMainClass().trim();

        switch (mode) {
            case SPI_ONLY, HOST_MVC -> {
                if (port != null) {
                    errors.add("runtimeMode=" + mode + " 时不应声明 independentPort");
                }
                if (main != null && !main.isEmpty()) {
                    errors.add("runtimeMode=" + mode + " 时不应声明 mainClass（独立进程请用 INDEPENDENT_PROCESS）");
                }
            }
            case INDEPENDENT_PROCESS -> {
                if (port == null) {
                    errors.add("runtimeMode=INDEPENDENT_PROCESS 时必须声明 independentPort（或由后续版本支持宿主分配时放宽）");
                } else if (port < MIN_EPHEMERAL_PORT || port > MAX_USER_PORT) {
                    errors.add("independentPort 需在 " + MIN_EPHEMERAL_PORT + "–" + MAX_USER_PORT + " 内: " + port);
                }
                if (main == null || main.isEmpty()) {
                    errors.add("runtimeMode=INDEPENDENT_PROCESS 时必须声明 mainClass（独立进程入口）");
                }
            }
        }
    }

    /**
     * 校验插件声明的 {@link HostServices} 契约版本不高于宿主实现。
     *
     * @param hostImplementedContractVersion 宿主 {@link HostServices#hostImplementedContractVersion()}
     */
    public static void validateHostServicesContractOrThrow(PluginDescriptor d, int hostImplementedContractVersion) {
        int required = d.requiredHostServicesContractVersionOrDefault();
        if (hostImplementedContractVersion < required) {
            throw new PluginException(
                    "插件 " + d.getId() + " 需要 hostServicesContractVersion>=" + required + "，宿主当前实现为 "
                            + hostImplementedContractVersion);
        }
    }

    /**
     * 在校验结构通过后，检查当前宿主 revision 与可选 Spring Boot 字符串是否满足声明。
     */
    public static boolean satisfiesHost(PluginDescriptor d, int hostRevision, String hostSpringBootVersion) {
        RequiresMmsDescriptor r = d.getRequiresMms();
        if (r == null) {
            return false;
        }
        if (r.getRevisionMin() != null && hostRevision < r.getRevisionMin()) {
            return false;
        }
        if (r.getRevisionMax() != null && hostRevision > r.getRevisionMax()) {
            return false;
        }
        if (r.getSpringBoot() != null && !r.getSpringBoot().isBlank()) {
            if (hostSpringBootVersion == null || hostSpringBootVersion.isBlank()) {
                return false;
            }
            String need = r.getSpringBoot().trim();
            String have = hostSpringBootVersion.trim();
            if (!(have.equals(need) || have.startsWith(need) || need.startsWith(have))) {
                return false;
            }
        }
        return true;
    }

    public static void validateStructureOrThrow(PluginDescriptor d) {
        List<String> e = validateStructure(d);
        if (!e.isEmpty()) {
            throw new PluginException("plugin.json 校验失败: " + String.join("; ", e));
        }
    }
}
