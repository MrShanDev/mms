package com.sxpcwlkj.authority.listener;


import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义侦听器的实现
 * @author mmsAdmin
 */
@Component
@Slf4j
public class MySaTokenListener implements SaTokenListener {


    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String s, Object o, String s1, SaLoginParameter saLoginParameter) {
        log.info("doLogin");
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        String device = StpUtil.getLoginDeviceTypeByToken(tokenValue);
        String idPart = loginId.toString();
        if (DeviceEnum.MOBILE.getType().equals(device)) {
            RedisUtil.deleteObject(RedisConstant.MOBILE_KEY + idPart);
        } else if (DeviceEnum.ADMIN.getType().equals(device)) {
            RedisUtil.deleteObject(RedisConstant.ADMIN_KEY + idPart);
        } else if (DeviceEnum.DOC.getType().equals(device)) {
            RedisUtil.deleteObject(RedisConstant.DOC_KEY + idPart);
        } else if (DeviceEnum.PC.getType().equals(device)) {
            RedisUtil.deleteObject(RedisConstant.PC_KEY + idPart);
        }

    }

    @Override
    public void doKickout(String s, Object o, String s1) {

    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String s, Object o, String s1) {

    }

    @Override
    public void doDisable(String s, Object o, String s1, int i, long l) {

    }

    @Override
    public void doUntieDisable(String s, Object o, String s1) {

    }

    @Override
    public void doOpenSafe(String s, String s1, String s2, long l) {

    }

    @Override
    public void doCloseSafe(String s, String s1, String s2) {

    }

    @Override
    public void doCreateSession(String s) {

    }

    @Override
    public void doLogoutSession(String s) {

    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String loginType, Object loginId, String tokenValue, long timeout) {
        // Token 续期逻辑
    }

}


