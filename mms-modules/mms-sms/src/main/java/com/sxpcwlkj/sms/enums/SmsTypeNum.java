package com.sxpcwlkj.sms.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 短信服务商
 */
public enum SmsTypeNum implements IEnum {

    aliyun("aliyun", "短信服务商-阿里云"),
    tencent("tencent", "短信服务商-腾讯云"),
    huawei("huawei", "短信服务商-华为云"),
    ctyun("ctyun", "短信服务商-天翼云"),
    qiniu("qiniu", "短信服务商-七牛云"),

    ;

    private final String value;
    private String msg;

    SmsTypeNum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}

