package com.sxpcwlkj.framework.entity;

import com.sxpcwlkj.common.annotation.IgnoreSign;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * SpringWeb 基础实体类
 *
 * @author mmsAdmin
 **/
@EqualsAndHashCode(callSuper = false)
@Data
public class BaseEntityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排序
     */
    // 忽略加签的注解
    @IgnoreSign
    private Integer sort;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status ;
    /**
     * 租户号
     */
    private String tenantId ;

    /**
     * 版本号
     */
    private Long revision;

}
