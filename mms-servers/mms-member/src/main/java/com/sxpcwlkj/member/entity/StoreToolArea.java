package com.sxpcwlkj.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 行政区域
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_tool_area")
@EqualsAndHashCode(callSuper = true)
public class StoreToolArea  extends BaseEntity {
    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * CODE
     */
    private String code;
    /**
     * 父CODE
     */
    private String parentCode;
    /**
     * 级别
     */
    private String level;
}
