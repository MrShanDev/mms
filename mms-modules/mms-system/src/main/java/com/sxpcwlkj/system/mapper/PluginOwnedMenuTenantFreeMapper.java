package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.sxpcwlkj.system.entity.SysFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 插件安装编排写入 {@code sys_function} 时须带显式 {@code tenant_id}，故忽略行级租户插件。
 */
@Mapper
@Repository
public interface PluginOwnedMenuTenantFreeMapper {

    @InterceptorIgnore(tenantLine = "true")
    SysFunction selectOneIdTenant(@Param("id") String id, @Param("tenantId") String tenantId);

    @InterceptorIgnore(tenantLine = "true")
    int insertMenu(SysFunction row);

    @InterceptorIgnore(tenantLine = "true")
    int updateMenu(SysFunction row);
}
