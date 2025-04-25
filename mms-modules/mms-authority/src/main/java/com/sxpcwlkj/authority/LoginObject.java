package com.sxpcwlkj.authority;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotWebContextException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Console;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.enums.ErrorCodeEnum;
import com.sxpcwlkj.common.exception.LoginException;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

/**
 * @Description 登录对象
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Setter
@Getter
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginObject<T> {

    private Class<T> clazz;

    /**
     * 是否登录
     *
     * @return 是否登录
     */
    public static Boolean isLogin() {
        try {
            return StpUtil.isLogin();
        }catch (Exception e){
            log.error("获取登录状态失败！");
        }
        return false;
    }

    /**
     * 获取当前登录用户ID
     * @return 当前登录用户ID
     */
    public static String getLoginId() {
        try {
            SaSession session = StpUtil.getSession();
            return session != null ? session.getLoginId().toString() : null;
        }catch (NotWebContextException e){
            return null;
        }

    }

    /**
     * 获取租户号
     * @return
     */
    public static String getLoginTenant(){
        String id=getLoginId();
        if(id==null) {
            return "000000";
        }
        return RedisUtil.getCacheObject(RedisConstant.ADMIN_TENANT_KEY + id);
    }

    /**
     * 当前登录对象是否超级管理员
     * @return 当前登录对象是否超级管理员
     */
    public static Boolean getLoginSuper() {
        if (Objects.equals(getLoginId(), "1")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * 获取当前登录用户
     * @param clazz 对象
     * @return 当前登录用户
     */
    public static <T> T getLoginObject(Class<T> clazz) {
        if (isLogin()) {
            try {
                if (getLoginId() != null) {
                    Object object=null;
                    String device = StpUtil.getLoginDevice();
                    if(DeviceEnum.MOBILE.getType().equals(device)){
                        object = RedisUtil.getCacheObject(RedisConstant.MOBILE_KEY + StpUtil.getLoginIdAsLong());
                    }
                    if (DeviceEnum.ADMIN.getType().equals(device)){
                        object = RedisUtil.getCacheObject(RedisConstant.ADMIN_KEY + StpUtil.getLoginIdAsLong());
                    }

                    return MapstructUtil.convert(object, clazz);
                }
            } catch (Exception ignored) {
                log.info("获取登录对象:'{}',失败!", clazz);
            }

        }
        //请先登录
        throw new NotLoginException(ErrorCodeEnum.USER_NOT_LOGIN.getValue(), DeviceEnum.MOBILE.getType(), "0");
    }

    /**
     * 登录
     *
     * @param id       标识ID
     * @param device   登录设备
     * @param timeout  过期时间
     * @param jwtKey   JWL key
     * @param jwtValue JWT value
     * @return 登录token
     */
    public static String loginToken(String id, String device, Long timeout, String jwtKey, String jwtValue) {
        //根据用户id，进行登录
        SaLoginModel saLoginModel = new SaLoginModel();
        if (StringUtil.isNotEmpty(device)) {
            saLoginModel.setDevice(device);
        }
        if (StringUtil.isNotEmpty(timeout) && timeout > 0) {
            saLoginModel.setTimeout(timeout);
        }
        if (StringUtil.isNotEmpty(jwtKey)) {
            saLoginModel.setExtra(jwtKey, jwtValue);
        }
        StpUtil.login(id, saLoginModel);

        // 获取当前会话的token值
        return StpUtil.getTokenValue();
    }

    /**
     * 退出当前登录
     */
    public static void logout() {
        try {
            // 当前会话注销登录
            StpUtil.logout();
        } catch (NotLoginException exception) {
            throw new LoginException("Not Log In : " + exception.getMessage());
        }
    }


}
