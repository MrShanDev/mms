package com.sxpcwlkj.member.entity.vo;

import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.member.entity.StoreToolArea;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;

import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
 * 行政区域Vo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */

@Data
@AutoMapper(target = StoreToolArea.class)
@EqualsAndHashCode(callSuper=false)
public class StoreToolAreaVo  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
