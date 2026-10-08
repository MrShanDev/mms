package com.sxpcwlkj.log.utils;

import com.alibaba.fastjson.JSON;
import java.util.*;

/** Applies to requests, responses, lists and nested DTOs. Never falls back to raw values. */
public final class AuditSanitizer {
    private AuditSanitizer() { }
    public static String toJson(Object value, String... extraFields) {
        try {
            Set<String> extra = new HashSet<>();
            if (extraFields != null) for (String field : extraFields) extra.add(normalize(field));
            return JSON.toJSONString(sanitize(JSON.parse(JSON.toJSONString(value)), extra));
        } catch (Exception e) { return "\"[不可序列化]\""; }
    }
    private static String normalize(String key) {
        return key == null ? "" : key.toLowerCase(Locale.ROOT).replace("_", "").replace("-", "");
    }
    private static Object sanitize(Object value, Set<String> extra) {
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> clean = new LinkedHashMap<>();
            for (var entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey()); String norm = normalize(key);
                boolean sensitive = extra.contains(norm) || norm.contains("password") || norm.contains("token")
                        || norm.contains("secret") || norm.contains("privatekey") || norm.contains("aeskey")
                        || norm.contains("accesskey") || norm.equals("authorization") || norm.equals("configvalue");
                clean.put(key, sensitive ? "******" : sanitize(entry.getValue(), extra));
            }
            return clean;
        }
        if (value instanceof Collection<?> list) return list.stream().map(v -> sanitize(v, extra)).toList();
        return value;
    }
}
