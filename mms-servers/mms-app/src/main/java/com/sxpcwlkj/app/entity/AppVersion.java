package com.sxpcwlkj.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * App版本发布对象 app_version
 *
 * @author Qoder
 * @date 2026-01-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_version")
public class AppVersion extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用编码（唯一标识）
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 平台类型（android, ios, harmony, h5, flutter）
     */
    private String platform;

    /**
     * 内部版本号（用于比较，如：1001）
     */
    private String versionCode;

    /**
     * 用户版本号（如：1.2.3）
     */
    private String versionName;

    /**
     * 构建号
     */
    private String buildNumber;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件MD5校验值
     */
    private String fileMd5;

    /**
     * 更新说明
     */
    private String releaseNotes;

    /**
     * 是否强制更新（0-否，1-是）
     */
    private Integer isForceUpdate;

    /**
     * 最小支持版本
     */
    private String minRequiredVersion;

    /**
     * 发布方式（immediate, gradual, manual）
     */
    private String publishType;

    /**
     * 发布状态（draft, testing, published, offline）
     */
    private String publishStatus;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 发布人
     */
    private String publishUser;

}
