package com.sxpcwlkj.member.entity.bo;

import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;

import com.sxpcwlkj.member.entity.StoreToolArea;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
 * 行政区域Bo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = StoreToolArea.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreToolAreaBo  extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String name;
    /**
     * CODE
     */
    @NotBlank(message = "CODE不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String code;
    /**
     * 父CODE
     */
    @NotBlank(message = "父CODE不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String parentCode;
    /**
     * 级别
     */
    @NotBlank(message = "级别不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String level;
}

