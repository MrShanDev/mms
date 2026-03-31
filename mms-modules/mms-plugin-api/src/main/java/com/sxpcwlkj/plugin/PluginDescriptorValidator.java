package com.sxpcwlkj.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link PluginDescriptor} 结构与宿主版本的兼容性校验（P0）。
 */
public final class PluginDescriptorValidator {

    private static final Pattern SEMVER_LIGHT = Pattern.compile("^\\d+\\.\\d+\\.\\d+([.-]\\S+)?$");
    private static final Pattern ID_PATTERN =
            Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_.-]*(\\.[a-zA-Z0-9][a-zA-Z0-9_.-]*)+$");

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
        return errors;
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
