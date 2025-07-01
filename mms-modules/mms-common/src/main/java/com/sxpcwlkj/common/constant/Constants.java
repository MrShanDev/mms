package com.sxpcwlkj.common.constant;

/**
 * @ClassName Constants
 * @Description 基础变量
 * @Author mmsAdmin
 * @Date 2022/12/25 12:08
 */
public interface Constants {

    /**
     * UTF-8 字符集
     */
    String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    String GBK = "GBK";

    /**
     * www主域
     */
    String WWW = "www.";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    String FAIL = "1";

    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    String LOGOUT = "Logout";

    /**
     * 注册
     */
    String REGISTER = "Register";

    /**
     * 登录失败
     */
    String LOGIN_FAIL = "Error";

    /**
     * 验证码有效期（分钟）
     */
    Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    String TOKEN = "token";

    /**
     * 客户端标识
     */
    String APPID = "App-Id";

    /**
     * 客户端密钥
     */
    String SECRET_KEY ="Secret-Key";

    /**
     * userInfo 客户端保存的Key
     */
    String USERINFO="userInfo";

}
