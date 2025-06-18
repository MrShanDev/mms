package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.convert.Convert;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;

import com.sxpcwlkj.common.constant.Constants;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.sercice.SysSignService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.system.entity.bo.LoginBodyBo;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.service.SysLoginService;
import com.sxpcwlkj.system.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统登录
 * @author xijue
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/auth")
public class AuthController extends BaseController {

    private final SysLoginService loginService;
    private final SysUserService sysUserService;
    private  final SysSignService sysSignService;


    /**
     * 登录方法
     * @param loginBodyBo  登录参数
     * @return 登录结果
     */
    @SaIgnore
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBodyBo loginBodyBo,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        Map<String, Object> ajax = new HashMap<>(16);
        // 生成令牌
        String token = loginService.login(request,loginBodyBo);
        ajax.put(Constants.TOKEN, token);
        SysUserVo sysUser = sysUserService.getUserRoleAnfFunctionInfo(LoginObject.getLoginId());
        RedisUtil.setCacheObject(RedisConstant.ADMIN_TENANT_KEY+sysUser.getUserId(),sysUser.getTenantId(), Duration.ofHours(24));
        RedisUtil.setCacheObject(RedisConstant.ADMIN_KEY+sysUser.getUserId(),sysUser, Duration.ofHours(24));
        RedisUtil.setCacheObject(RedisConstant.ADMIN_NAME+sysUser.getUserId(),sysUser.getUserName(), Duration.ofHours(24));
        ajax.put(Constants.USERINFO, sysUserService.getUserInfo(sysUser));
        if(LoginObject.isLogin()){
            //给浏览器端设置一个 Cookie 的 clientKey值 3天
            sysSignService.loginSetCookie(request,response,1000*60*60*24*3);
        }
        return success(ajax);
    }


    /**
     * 退出登录
     * @return 退出结果
     */
    @SaIgnore
    @PostMapping("/logout")
    public R<String> logout() {
        loginService.logout();
        return success("退出成功");
    }





}
