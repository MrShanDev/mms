package com.sxpcwlkj.ai.type;

import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI服务提供者枚举
 * @author shanpengnian
 */
@Getter
@AllArgsConstructor
public enum AIProvider {

    OLLAMA("Ollama"),

    DEEPSEEK("DeepSeek"),

    DOCUMENT("Document");

    private final String type;

    public static AIProvider find(String databaseProductName) {
        if (StringUtil.isBlank(databaseProductName)) {
            return null;
        }
        for (AIProvider type : values()) {
            if (type.getType().equals(databaseProductName)) {
                return type;
            }
        }
        return null;
    }
}
