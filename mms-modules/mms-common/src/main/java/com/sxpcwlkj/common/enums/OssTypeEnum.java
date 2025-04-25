package com.sxpcwlkj.common.enums;

import com.sxpcwlkj.common.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 存储类型
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Getter
@AllArgsConstructor
public enum OssTypeEnum {

    /**
     * 本地
     */
    local("local-plus"),
    /**
     * 阿里云
     */
    aliyun("aliyun-oss"),
    /**
     * 腾讯云
     */
    tencent("tencent-cos"),
    /**
     * 华为云
     */
    huawei("huawei-obs"),
    /**
     * 七牛云
     */
    qiniu("qiniu-kodo"),
    /**
     * 百度云
     */
    baidu("baidu-bos"),
    /**
     * minio
     */
    minio("minio"),
    /**
     * 亚马逊
     */
    amazon("amazon-s3");

    private final String type;

    public static OssTypeEnum find(String databaseProductName) {
        if (StringUtil.isBlank(databaseProductName)) {
            return null;
        }
        for (OssTypeEnum type : values()) {
            if (type.getType().equals(databaseProductName)) {
                return type;
            }
        }
        return null;
    }
}
