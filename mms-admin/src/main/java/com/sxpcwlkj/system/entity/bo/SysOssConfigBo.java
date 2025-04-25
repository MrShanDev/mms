package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.system.entity.SysOssConfig;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 对象存储配置表bo
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AutoMapper(target = SysOssConfig.class)
public class SysOssConfigBo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主建
     */
    private Long id;

    /**
     * 配置key
     */
    @NotBlank(message = "配置key不能为空")
    private String configKey;

    /**
     * accessKey
     */
    @NotBlank(message = "accessKey不能为空")
    private String accessKey;

    /**
     * 秘钥
     */
    @NotBlank(message = "秘钥不能为空")
    private String secretKey;

    /**
     * 桶名称
     */
    @NotBlank(message = "桶名称不能为空")
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
    @NotBlank(message = "自定义域名不能为空")
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
     * 扩展字段
     */

    private String ext1;

    private Integer status;

}
