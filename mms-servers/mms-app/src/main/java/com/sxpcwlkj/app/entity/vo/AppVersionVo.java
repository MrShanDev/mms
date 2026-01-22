package com.sxpcwlkj.app.entity.vo;

import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.app.entity.AppVersion;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * App版本发布视图对象
 *
 * @author Qoder
 * @date 2026-01-22
 */
@Data
@AutoMapper(target = AppVersion.class)
public class AppVersionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String appCode;
    private String appName;
    private String platform;
    private String versionCode;
    private String versionName;
    private String buildNumber;
    private String downloadUrl;
    private Long fileSize;
    private String fileMd5;
    private String releaseNotes;
    private Integer isForceUpdate;
    private String minRequiredVersion;
    private String publishType;
    private String publishStatus;
    private Date publishTime;
    private String publishUser;
    private Integer status;
    private Integer sort;
    private String remark;
    private Long createdBy;
    private Date createdTime;
    private Long updatedBy;
    private Date updatedTime;
    private Long revision;
    private String tenantId;

}
