package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.*;
import com.sxpcwlkj.system.entity.SysFunction;
import java.util.List;

/** Cross-tenant statements exclusively used by the platform administration service. */
@Mapper
@InterceptorIgnore(tenantLine="true", dataPermission="true")
public interface TenantAdministrationMapper {
    @Select("SELECT config_value FROM sys_config WHERE id='mms-tenant-mode'")
    String mode();
    @Insert("INSERT INTO sys_config(id,config_key,config_name,config_value,config_type,status,tenant_id) VALUES('mms-tenant-mode','tenant_mode_enabled','多租户模式',#{value},0,1,'000000') ON DUPLICATE KEY UPDATE config_value=VALUES(config_value)")
    int saveMode(@Param("value") String value);
    @Select("SELECT COUNT(*) FROM sys_user WHERE tenant_id=#{tenant}")
    long userCount(@Param("tenant") String tenant);
    @Select("SELECT user_id FROM sys_user WHERE tenant_id=#{tenant}")
    List<String> userIds(@Param("tenant") String tenant);
    @Insert("INSERT INTO sys_dept(dept_id,parent_id,dept_name,status,tenant_id,sort) VALUES(#{id},'0','默认部门',1,#{tenant},0)")
    int createDepartment(@Param("id") String id,@Param("tenant") String tenant);
    @Insert("INSERT INTO sys_role(id,name,code,level,data_scope,status,tenant_id,sort) VALUES(#{id},'租户管理员','tenant_admin',1,'1',1,#{tenant},0)")
    int createRole(@Param("id") String id,@Param("tenant") String tenant);
    @Insert("INSERT INTO sys_user(user_id,user_name,nick_name,password,status,tenant_id,dept_id,user_type,sex,del_flag,sort) VALUES(#{id},#{username},'租户管理员',#{password},1,#{tenant},#{dept},'0',0,'0',0)")
    int createUser(@Param("id") String id,@Param("tenant") String tenant,@Param("username") String username,@Param("password") String password,@Param("dept") String dept);
    @Insert("INSERT INTO sys_user_role(id,user_id,role_id,status,tenant_id) VALUES(#{id},#{user},#{role},1,#{tenant})")
    int assignRole(@Param("id") String id,@Param("user") String user,@Param("role") String role,@Param("tenant") String tenant);
    @Select("SELECT * FROM sys_function WHERE tenant_id=#{tenant} AND remark='tenant-provisioned'")
    List<SysFunction> provisionedMenus(@Param("tenant") String tenant);
    @Insert("INSERT INTO sys_function(id,parent_id,path,name,component,redirect_path,language_code,component_name,permission,type,sort,icon,status,visible,is_iframe,is_open_link,is_link,keep_alive,always_show,is_fast,remark,tenant_id) SELECT #{id},#{parent},path,name,component,redirect_path,language_code,component_name,CASE WHEN type=1 THEN '' ELSE permission END,type,sort,icon,status,visible,is_iframe,is_open_link,is_link,keep_alive,always_show,is_fast,'tenant-provisioned',#{tenant} FROM sys_function WHERE id=#{source} AND tenant_id='000000'")
    int cloneMenu(@Param("source") String source,@Param("id") String id,@Param("parent") Long parent,@Param("tenant") String tenant);
    @Insert("INSERT INTO sys_role_function(id,role_id,function_id,status,tenant_id) SELECT #{id},id,#{function},1,tenant_id FROM sys_role WHERE tenant_id=#{tenant} AND code='tenant_admin'")
    int assignMenu(@Param("id") String id,@Param("function") String function,@Param("tenant") String tenant);
}
