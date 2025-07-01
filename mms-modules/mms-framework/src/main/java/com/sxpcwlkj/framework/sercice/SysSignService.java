package com.sxpcwlkj.framework.sercice;

import com.sxpcwlkj.framework.entity.SysSign;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统加签
 * @author: mmsAdmin
 */
public interface SysSignService {

    /**
     * 客户端登录后设置Cookie
     *
     * @param request  请求
     * @param response 响应
     * @param timeout  有效时间 单位: 秒
     */
    void loginSetCookie(HttpServletRequest request, HttpServletResponse response, int timeout);

    /**
     * 新建加签
     * @param appid      客户端ID
     * @return  true: 成功  false: 失败
     */
    SysSign initSign(String appid,int timeOut);



    /**
     * 新建加签
     * @param appid      客户端ID
     * @param publicKey  公钥
     * @param privateKey 私钥
     * @return  true: 成功  false: 失败
     */
    Boolean initSign(String appid,String publicKey,String privateKey,int timeOut);

    /**
     * 获取加签
     * @return SysSign
     */
    SysSign getSign();


}
