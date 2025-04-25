package com.sxpcwlkj.system.service;

import java.util.Map;

public interface CaptchaService {

    /**
     * 获取验证码
     * @param type  0：注册  1：登录
     * @param code  对象唯一code
     * @return
     */
    Map<String, Object> getCaptcha(Integer type, String code);



}
