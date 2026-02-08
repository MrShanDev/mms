package com.sxpcwlkj.system.entity.vo;

import com.sxpcwlkj.system.entity.SysOss;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;

/**
 * 对象存储
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@AutoMapper(target = SysOss.class)
public class SysOssVo {
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


    private Integer status;

    private Date createdTime;

    private String base64;

    private String width;

    private String height;

    private String size;

    private String revision;
}
