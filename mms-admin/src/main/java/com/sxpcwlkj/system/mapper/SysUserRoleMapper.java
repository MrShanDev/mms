package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysRole;
import com.sxpcwlkj.system.entity.SysUserRole;
import com.sxpcwlkj.system.entity.vo.SysUserRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 用户角色
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Mapper
@Repository
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRole, SysUserRoleVo> {

    /**
     * 用户的角色
     *
     * @param userId
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT  r.* FROM  `sys_user_role` ur LEFT JOIN  `sys_role` r  ON ur.`role_id`=r.`id` WHERE ur.`user_id`=#{userId} ORDER BY r.`sort` ")
    List<SysRole> roleList(@Param("userId") String userId);


}
