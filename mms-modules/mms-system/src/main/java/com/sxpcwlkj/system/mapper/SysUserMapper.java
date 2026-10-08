package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@com.sxpcwlkj.common.annotation.DataScope(table="sys_user", userColumn="user_id", deptColumn="dept_id")
@Repository
public interface SysUserMapper extends BaseMapperPlus<SysUser, SysUserVo> {

    @InterceptorIgnore(tenantLine = "true", dataPermission = "true")
    @org.apache.ibatis.annotations.Update("UPDATE sys_user SET password=#{newHash} WHERE user_id=#{userId} AND tenant_id=#{tenantId} AND password=#{oldHash}")
    int upgradePassword(@Param("userId") String userId, @Param("tenantId") String tenantId,
                        @Param("oldHash") String oldHash, @Param("newHash") String newHash);

    @InterceptorIgnore(tenantLine = "true", dataPermission = "true")
    @org.apache.ibatis.annotations.Select("SELECT * FROM sys_user WHERE tenant_id=#{tenantId} AND user_name=#{userName} LIMIT 1")
    SysUser selectForLogin(@Param("tenantId") String tenantId, @Param("userName") String userName);

    @InterceptorIgnore(tenantLine = "true", dataPermission = "true")
    @org.apache.ibatis.annotations.Select("SELECT * FROM sys_user WHERE user_id=#{userId}")
    SysUser selectForSession(@Param("userId") String userId);

    @InterceptorIgnore(tenantLine = "true", dataPermission = "true")
    @org.apache.ibatis.annotations.Delete("DELETE FROM sys_notice_read WHERE tenant_id=#{tenantId} AND user_id=#{userId}")
    int deleteNoticeReads(@Param("tenantId") String tenantId,@Param("userId") String userId);
    @InterceptorIgnore(tenantLine = "true", dataPermission = "true")
    @org.apache.ibatis.annotations.Delete("DELETE FROM sys_user_theme_preference WHERE tenant_id=#{tenantId} AND user_id=#{userId}")
    int deleteThemePreference(@Param("tenantId") String tenantId,@Param("userId") String userId);
    @InterceptorIgnore(tenantLine = "true", dataPermission = "true")
    @org.apache.ibatis.annotations.Delete("DELETE FROM sys_sign WHERE user_id=#{userId}")
    int deleteSignSessions(@Param("userId") String userId);

    @InterceptorIgnore(dataPermission = "true")
    @org.apache.ibatis.annotations.Select("SELECT COUNT(*) FROM sys_user WHERE dept_id=#{deptId}")
    long countDepartmentReferences(@Param("deptId") String deptId);

    Page<SysUserVo> selectPageUserList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);
    SysUser selectByUserName(@Param("userName") String userName);
    SysUser selectByUserPhone(@Param("userPhone") String userPhone);
    SysUser selectByUserEmail(@Param("userEmail") String userEmail);
    @InterceptorIgnore(tenantLine = "true")
    int updateRsa(@Param("tenantId") String tenantId, @Param("userId") String userId, @Param("publicKey") String publicKey,@Param("priverKey") String priverKey);

    @InterceptorIgnore(tenantLine = "true", dataPermission = "true")
    int updateIpById(SysUser sysUser);
}
