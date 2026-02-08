package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysRole;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @Description 系统角色
* @Author sxpcwlkj
* @Version v1.0.0
*/
@Mapper
@Repository
public interface SysRoleMapper extends BaseMapperPlus<SysRole,SysRoleVo> {
    @InterceptorIgnore(tenantLine = "true")
    Page<SysRoleVo> selectPageList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);

    /**
     * 查询用户的最高的一个角色
     * @param userId id
     * @return vo
     */
    @InterceptorIgnore(tenantLine = "true")
    SysRoleVo selectByUserId(@Param("userId") String userId);
    /**
     * 查询用户所有角色
     * @param userId id
     * @return vo
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysRoleVo> selectByUserIdList(@Param("userId") String userId);

    /**
     * 根据角色编码查询
     * @param roleCode 编码
     * @return vo
     */
    @InterceptorIgnore(tenantLine = "true")
    SysRoleVo selectByCode(@Param("roleCode") String roleCode);
}
