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
@Repository
public interface SysUserMapper extends BaseMapperPlus<SysUser, SysUserVo> {

    @InterceptorIgnore(tenantLine = "true")
    Page<SysUserVo> selectPageUserList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);
    @InterceptorIgnore(tenantLine = "true")
    SysUser selectByUserName(@Param("userName") String userName);
    @InterceptorIgnore(tenantLine = "true")
    SysUser selectByUserPhone(@Param("userPhone") String userPhone);
    @InterceptorIgnore(tenantLine = "true")
    SysUser selectByUserEmail(@Param("userEmail") String userEmail);
    @InterceptorIgnore(tenantLine = "true")
    int updateRsa(@Param("tenantId") String tenantId, @Param("userId") String userId, @Param("publicKey") String publicKey,@Param("priverKey") String priverKey);

    @InterceptorIgnore(tenantLine = "true")
    int updateIpById(SysUser sysUser);
}
