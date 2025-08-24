package com.sxpcwlkj.common.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.sxpcwlkj.common.utils.StringUtil;
import lombok.Getter;

/**
 * 打印类型
 *
 * @author mmsAdmin
 */
@Getter
public enum PrintTypeEnum implements IEnum {

    /**
     * 文本
     */
    TEXT("text"),

    /**
     * 图片
     */
    IMAGE("image");

    private final  String type;

    PrintTypeEnum(String type){
        this.type = type;
    }

    @Override
    public String getValue() {
        return this.type;
    }

    public static PrintTypeEnum find(String databaseProductName) {
        if (StringUtil.isBlank(databaseProductName)) {
            return null;
        }
        for (PrintTypeEnum type : values()) {
            if (type.getType().equals(databaseProductName)) {
                return type;
            }
        }
        return null;
    }
}
