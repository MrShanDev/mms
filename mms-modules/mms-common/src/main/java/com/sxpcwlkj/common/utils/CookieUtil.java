package com.sxpcwlkj.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Cookie
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Slf4j

public class CookieUtil {

    /**
     * 设置cookie
     *
     * @param name 名称
     */
    public static void setCookie(HttpServletResponse response,String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取cookie
     *
     * @param name 名称
     */
    public static String getCookie(String name) {
        Cookie[] cookies = ServletUtil.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie
     *
     * @param name 名称
     */
    public static void removeCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        ServletUtil.getResponse().addCookie(cookie);
    }

}
