package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统登录
 *
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统登录",description = "系统登录,鉴权")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/auth")
public class AuthController extends BaseController {

    private final SysLoginService loginService;
    private final SysUserService sysUserService;
    private final SysSignService sysSignService;


    /**
     * 登录方法
     *
     * @param loginBodyBo 登录参数
     * @return 登录结果
     */
    @Operation(summary = "登录方法", description = "登录方法,账号、密码、验证码验证登录")
    @SaIgnore
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBodyBo loginBodyBo, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ajax = new HashMap<>(16);
        // 生成令牌
        String token = loginService.login(request, loginBodyBo);
        ajax.put(Constants.TOKEN, token);
        SysUserVo sysUser = sysUserService.getUserRoleAnfFunctionInfo(LoginObject.getLoginId());
        RedisUtil.setCacheObject(RedisConstant.ADMIN_TENANT_KEY + sysUser.getUserId(), sysUser.getTenantId(), Duration.ofHours(24));
        RedisUtil.setCacheObject(RedisConstant.ADMIN_KEY + sysUser.getUserId(), sysUser, Duration.ofHours(24));
        RedisUtil.setCacheObject(RedisConstant.ADMIN_NAME + sysUser.getUserId(), sysUser.getUserName(), Duration.ofHours(24));
        ajax.put(Constants.USERINFO, sysUserService.getUserInfo(sysUser));
        if (LoginObject.isLogin()) {
            //给浏览器端设置一个 Cookie 的 clientKey值 3天
            sysSignService.loginSetCookie(request, response, 1000 * 60 * 60 * 24 * 3);
        }
        return success(ajax);
    }


    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @Operation(summary = "退出登录", description = "退出当前登录会话")
    @PostMapping("/logout")
    public R<String> logout() {
        loginService.logout();
        return success("退出成功");
    }


}
