package com.sxpcwlkj.sms.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 短信服务商 rides缓存key
 */
public enum SmsKeyNum implements IEnum {

    sms_server_list("sms:server:list", "短信服务商列表"),
    sms_supplier("sms:supplier", "默认短信服务商"),
    sms_server_aliyun("sms:server:aliyun", "短信服务商-阿里云"),
    sms_server_tencent("sms:server:tencent", "短信服务商-腾讯云"),
    sms_server_huawei("sms:server:huawei", "短信服务商-华为云"),
    sms_server_ctyun("sms:server:ctyun", "短信服务商-天翼云"),
    sms_server_qiniu("sms:server:qiniu", "短信服务商-七牛云"),

    ;



    @Getter
    private String key;
    private String value;

    SmsKeyNum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}

