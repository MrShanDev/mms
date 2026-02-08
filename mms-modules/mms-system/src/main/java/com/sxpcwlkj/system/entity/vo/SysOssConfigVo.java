package com.sxpcwlkj.system.entity.vo;

import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.system.entity.SysOssConfig;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 对象存储配置表
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AutoMapper(target = SysOssConfig.class)
public class SysOssConfigVo extends BaseEntityVo {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主建
     */
    private Long id;
    /**
     * 配置key
     */
    private String configKey;
    /**
     * accessKey
     */
    private String accessKey;
    /**
     * 秘钥
     */
    private String secretKey;
    /**
     * 桶名称
     */
    private String bucketName;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 访问站点
     */
    private String endpoint;
    /**
     * 自定义域名
     */
    private String domain;
    /**
     * 是否https
     */
    private String isHttps;
    /**
     * 域
     */
    private String region;
    /**
     * 桶权限类型
     */
    private String accessPolicy;
    /**
     * 扩展字段 storagePath
     */
    private String ext1;

    private Integer status;
}
