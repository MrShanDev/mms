package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.system.entity.SysOss;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象存储bo
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysOss.class)
public class SysOssBo extends BaseEntity {
    private String ossId;
    /**
     * 文件名
     */

    private String fileName;
    /**
     * 原名
     */

    private String originalName;
    /**
     * 文件后缀名
     */

    private String fileSuffix;
    /**
     * URL地址
     */

    private String url;
    /**
     * MIME 类型
     */
    private String contentType;
    /**
     * 基础存储路径
     */
    private String basePath;
    /**
     * 存储平台
     */
    private String platform;

    private Boolean lookImg;
}
