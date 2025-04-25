package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.system.entity.SysFunction;
import com.sxpcwlkj.system.entity.SysRoleFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @Description 角色资源
* @Author sxpcwlkj
* @Version v1.0.0
*/
@Mapper
@Repository
public interface SysRoleFunctionMapper extends BaseMapper<SysRoleFunction> {

    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT f.* FROM  `sys_function` f LEFT JOIN `sys_role_function` o ON f.`id`=o.`function_id` WHERE o.`role_id`=#{roldId}   ORDER BY f.`sort` ")
    List<SysFunction> selectByRoleId(@Param("roldId") String roldId);

}
