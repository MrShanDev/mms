package com.sxpcwlkj.log.utils;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class AuditSanitizerTest {
    @Test void masksSecretsInNestedListsAndResponses() {
        String json=AuditSanitizer.toJson(Map.of("rows",List.of(Map.of("password","p-secret","token","t-secret","private_key","key-secret")),"data",Map.of("configValue","config-secret"),"name","visible"));
        for(String secret:List.of("p-secret","t-secret","key-secret","config-secret")) assertFalse(json.contains(secret));
        assertTrue(json.contains("visible"));
    }
}
