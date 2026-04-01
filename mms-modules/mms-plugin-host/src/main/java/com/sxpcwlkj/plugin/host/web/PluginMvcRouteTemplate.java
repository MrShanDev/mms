package com.sxpcwlkj.plugin.host.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HOST_MVC 路由模板：字面量段大小写不敏感匹配，{@code {name}} 捕获路径变量（声明顺序即方法形参绑定顺序）。
 */
public final class PluginMvcRouteTemplate {

    private static final Pattern VAR = Pattern.compile("^\\{([a-zA-Z][a-zA-Z0-9_]*)\\}$");

    private final List<Segment> segments;
    private final List<String> variableNamesInOrder;

    private PluginMvcRouteTemplate(List<Segment> segments, List<String> variableNamesInOrder) {
        this.segments = List.copyOf(segments);
        this.variableNamesInOrder = List.copyOf(variableNamesInOrder);
    }

    public List<String> variableNamesInOrder() {
        return variableNamesInOrder;
    }

    public static PluginMvcRouteTemplate parse(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            return new PluginMvcRouteTemplate(List.of(), List.of());
        }
        String s = rawPath.trim().replace('\\', '/');
        while (s.startsWith("/")) {
            s = s.substring(1);
        }
        while (s.endsWith("/") && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        if (s.isEmpty()) {
            return new PluginMvcRouteTemplate(List.of(), List.of());
        }
        String[] parts = s.split("/+");
        List<Segment> segs = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (String p : parts) {
            if (p.isEmpty()) {
                continue;
            }
            Matcher vm = VAR.matcher(p);
            if (vm.matches()) {
                String nm = vm.group(1);
                segs.add(new Segment.Var(nm));
                names.add(nm);
            } else {
                segs.add(new Segment.Literal(p.toLowerCase(Locale.ROOT)));
            }
        }
        return new PluginMvcRouteTemplate(segs, names);
    }

    public Optional<Map<String, String>> match(List<String> requestSegments) {
        if (segments.size() != requestSegments.size()) {
            return Optional.empty();
        }
        Map<String, String> out = new LinkedHashMap<>();
        for (int i = 0; i < segments.size(); i++) {
            Segment seg = segments.get(i);
            String req = requestSegments.get(i);
            if (seg instanceof Segment.Literal(String lit)) {
                if (!lit.equalsIgnoreCase(req)) {
                    return Optional.empty();
                }
            } else if (seg instanceof Segment.Var(String name)) {
                out.put(name, req);
            }
        }
        return Optional.of(out);
    }

    public sealed interface Segment permits Segment.Literal, Segment.Var {
        record Literal(String valueLowercase) implements Segment {}

        record Var(String name) implements Segment {}
    }
}
