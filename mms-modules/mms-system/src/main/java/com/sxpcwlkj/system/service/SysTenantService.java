package com.sxpcwlkj.system.service;

import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.entity.vo.SysTenantVo;

import java.util.List;

public interface SysTenantService {

    /**
     * 查询出系统启用的租户列表
     * @return
     */
    List<SysTenantVo> selectOpenList();

    /**
     * 租户查询
     * @param tenantId
     * @return
     */
    SysTenant selectById(String tenantId);
}
