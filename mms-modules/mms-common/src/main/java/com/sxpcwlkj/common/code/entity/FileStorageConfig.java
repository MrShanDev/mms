package com.sxpcwlkj.common.code.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shanpengnian
 * x-file-storage
 */
@Slf4j
@Data
public class FileStorageConfig {

    /**
     * 默认平台
     *
     */
    private String platform;
    /**
     * Key
     */
    private String accessKey;
    /**
     * Secret
     */
    private String secretKey;

    /**
     * 地域节点
     */
    private String endPoint;
    /**
     * 区域
     */
    private String region;
    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 访问域名
     */
    private String domain = "";

    /**
     * 基础路径
     */
    private String basePath = "";

    /**
     * 默认的 ACL，详情  Constant.AliyunOssACL
     */
    private String defaultAcl;
    /**
     * 本地存储路径
     */
    private String storagePath;

    /**
     * 自动分片上传阈值，达到此大小则使用分片上传，默认 128MB
     */
    private int multipartThreshold = 128 * 1024 * 1024;

    /**
     * 自动分片上传时每个分片大小，默认 32MB
     */
    private int multipartPartSize = 32 * 1024 * 1024;

    /**
     * 其它自定义配置
     */
    private Map<String, Object> attr = new LinkedHashMap<>();

}
