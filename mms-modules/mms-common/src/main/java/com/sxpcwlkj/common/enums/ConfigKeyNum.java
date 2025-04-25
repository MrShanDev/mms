package com.sxpcwlkj.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 接口返回错误码枚举
 * @author xijue
 */
public enum ConfigKeyNum implements IEnum<String> {


    config_base("sys:config:base", "基础配置"),
    config_sms("sys:config:sms", "短信配置"),
    config_email("sys:config:email", "邮箱配置"),
    config_oss("sys:config:oss", "OSS配置"),
    config_wx("sys:config:wx", "微信配置"),
    ;



    @Getter
    private String key;
    private String value;

    ConfigKeyNum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}

