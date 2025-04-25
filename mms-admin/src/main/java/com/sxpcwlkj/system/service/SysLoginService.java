package com.sxpcwlkj.system.service;

import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.bo.LoginBodyBo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @ClassName SysLoginService
 * @Description TODO
 * @Author 西决
 * @Date 2022/12/25 12:04
 */
public interface SysLoginService {

    /**
     * 登录验证
     * @param loginBodyBo bo
     * @return vo
     */
    String login(HttpServletRequest request,LoginBodyBo loginBodyBo);

    /**
     * 用户注销
     */
    void logout();

    /**
     * 验证码验证
     * @param username
     * @param code
     * @param uuid
     */
    void verfyCaptcha(String username,String codeKey, String code, String uuid);

    /**
     * 租户验证
     * @param sysUser
     */
    void verfyTenement(SysUser sysUser);

    /**
     * 系统用户登录更新信息
     * @param sysUser
     */
    void updateSysUser(HttpServletRequest request,SysUser sysUser);
}
