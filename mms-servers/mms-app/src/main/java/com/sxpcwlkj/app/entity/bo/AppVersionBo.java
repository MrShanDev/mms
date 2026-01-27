package com.sxpcwlkj.app.entity.bo;

import com.sxpcwlkj.app.entity.AppVersion;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
 * App版本发布表Bo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = AppVersion.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class AppVersionBo  extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
    private Long id;
    /**
     * 应用编码（唯一标识）
     */
    //@NotBlank(message = "应用编码（唯一标识）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String appCode;
    /**
     * 应用名称
     */
    //@NotBlank(message = "应用名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String appName;
    /**
     * 平台类型
     */
    @NotBlank(message = "平台类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String platform;
    /**
     * 内部版本号（用于比较，如：1001）
     */
    @NotBlank(message = "内部版本号（用于比较，如：1001）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String versionCode;
    /**
     * 用户版本号（如：1.2.3）
     */
    @NotBlank(message = "用户版本号（如：1.2.3）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String versionName;
    /**
     * 构建号
     */
    //@NotBlank(message = "构建号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String buildNumber;
    /**
     * 下载地址
     */
    @NotBlank(message = "下载地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String downloadUrl;
    /**
     * 文件大小（字节）
     */
    //@NotBlank(message = "文件大小（字节）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Long fileSize;
    /**
     * 文件MD5校验值
     */
    //@NotBlank(message = "文件MD5校验值不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String fileMd5;
    /**
     * 更新说明
     */
    @NotBlank(message = "更新说明不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String releaseNotes;
    /**
     * 是否强制更新（0-否，1-是）
     */
    @NotNull(message = "是否强制更新（0-否，1-是）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer isForceUpdate;
    /**
     * 最小支持版本
     */
    //@NotBlank(message = "最小支持版本不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String minRequiredVersion;
    /**
     * 发布方式
     */
    //@NotBlank(message = "发布方式不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String publishType;
    /**
     * 发布状态
     */
    @NotBlank(message = "发布状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String publishStatus;
    /**
     * 发布时间
     */
    //@NotNull(message = "发布时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Date publishTime;
    /**
     * 发布人
     */
    //@NotBlank(message = "发布人不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String publishUser;
}
