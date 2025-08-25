package com.sxpcwlkj.system.entity.vo;


import com.sxpcwlkj.common.properties.MmsAdminProperties;
import lombok.Data;


/**
 * 系统租户
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */

@Data
public class SysTenantVo {

    /**
     * 租户编号
     */
    private String tenantId;
    /**
     * 租户名
     */
    private String name;

    private MmsAdminProperties properties;

    private String revision;
}
