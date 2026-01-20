package com.sxpcwlkj.member.entity.bo;


import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.member.entity.StoreSysConfig;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 配置表Bo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = StoreSysConfig.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreSysConfigBo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @NotBlank(message = "主键ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
    private String id;
    /**
     * 配置名称
     */
    @NotBlank(message = "配置名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String configName;
    /**
     * 配置键
     */
    @NotBlank(message = "配置键不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String configKey;
    /**
     * 配置值
     */
    @NotBlank(message = "配置值不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String configValue;
    /**
     * 配置类型;1：系统内置 2：用户自定义
     */
    @NotNull(message = "配置类型;1：系统内置 2：用户自定义不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer configType;
}
