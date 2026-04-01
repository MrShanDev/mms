package com.sxpcwlkj.plugin;

/**
 * 校验 {@link PluginDependencyDescriptor#getVersionRange()} 与已加载的依赖版本是否兼容。
 * <p>支持：空/null、精确版本、{@code *}、{@code major.minor.+}；以及 Maven 式区间
 * {@code [1.0.0,2.0.0]}、{@code (1.0,3.0)}、{@code [1.0,)}、{@code (,2.0]} 等（版本段按点分数字分段比较，含 SNAPSHOT 等限定符时降级为字典序）。</p>
 */
public final class PluginVersionConstraint {

    private PluginVersionConstraint() {
    }

    /**
     * @param versionRange 可为 null/空、精确版本号、{@code major.minor.+}、{@code *}、或 Maven 区间 {@code [lo,hi]}
     * @param actualVer    非空实际版本
     */
    public static boolean satisfiedBy(String versionRange, String actualVer) {
        if (actualVer == null || actualVer.isBlank()) {
            return false;
        }
        if (versionRange == null || versionRange.isBlank()) {
            return true;
        }
        String r = versionRange.trim();
        String a = actualVer.trim();
        if (r.equals(a)) {
            return true;
        }
        if ("*".equals(r)) {
            return true;
        }
        if ((r.startsWith("[") || r.startsWith("(")) && r.length() >= 3) {
            char close = r.charAt(r.length() - 1);
            if (close == ']' || close == ')') {
                return satisfiedByMavenInterval(r, a);
            }
        }
        if (r.endsWith(".+")) {
            String prefix = r.substring(0, r.length() - 2).trim();
            return a.startsWith(prefix) && (a.length() == prefix.length() || a.charAt(prefix.length()) == '.');
        }
        return false;
    }

    private static boolean satisfiedByMavenInterval(String range, String actualVer) {
        char lowBracket = range.charAt(0);
        char highBracket = range.charAt(range.length() - 1);
        if ((lowBracket != '[' && lowBracket != '(') || (highBracket != ']' && highBracket != ')')) {
            return false;
        }
        String inner = range.substring(1, range.length() - 1);
        int comma = inner.indexOf(',');
        if (comma < 0) {
            return false;
        }
        String low = inner.substring(0, comma).trim();
        String high = inner.substring(comma + 1).trim();
        if (!low.isEmpty()) {
            int c = compareVersions(actualVer, low);
            if (lowBracket == '[' && c < 0) {
                return false;
            }
            if (lowBracket == '(' && c <= 0) {
                return false;
            }
        }
        if (!high.isEmpty()) {
            int c2 = compareVersions(actualVer, high);
            if (highBracket == ']' && c2 > 0) {
                return false;
            }
            if (highBracket == ')' && c2 >= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 与 Maven 规范接近的点分比较：纯数字段按整数；否则按字典序比较该段。
     */
    static int compareVersions(String a, String b) {
        if (a == null || a.isBlank()) {
            return b == null || b.isBlank() ? 0 : -1;
        }
        if (b == null || b.isBlank()) {
            return 1;
        }
        String[] pa = a.split("\\.");
        String[] pb = b.split("\\.");
        int n = Math.max(pa.length, pb.length);
        for (int i = 0; i < n; i++) {
            String sa = i < pa.length ? pa[i] : "";
            String sb = i < pb.length ? pb[i] : "";
            Integer na = tryParseIntSegment(sa);
            Integer nb = tryParseIntSegment(sb);
            if (na != null && nb != null) {
                if (!na.equals(nb)) {
                    return Integer.compare(na, nb);
                }
            } else {
                int c = sa.compareTo(sb);
                if (c != 0) {
                    return c;
                }
            }
        }
        return 0;
    }

    private static Integer tryParseIntSegment(String segment) {
        if (segment == null || segment.isEmpty()) {
            return 0;
        }
        int end = 0;
        while (end < segment.length() && Character.isDigit(segment.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return null;
        }
        if (end < segment.length()) {
            return null;
        }
        try {
            return Integer.parseInt(segment);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
