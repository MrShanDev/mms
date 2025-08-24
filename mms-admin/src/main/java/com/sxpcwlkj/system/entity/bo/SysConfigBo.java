package com.sxpcwlkj.system.entity.bo;


import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.system.entity.SysConfig;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统配置bo
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AutoMapper(target = SysConfig.class)
public class SysConfigBo extends BaseEntity {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 配置键
     */
    private String configKey;
    /**
     * 配置类型
     */
    private Integer configType;
    /**
     * 配置值
     */
    private String configValue;
    /**
     * 配置值
     */
    private List<String> configValues;
    /**
     * 配置值类型
     */
    private String valueType;
    /**
     * 序号
     */
    private Integer index;

}
