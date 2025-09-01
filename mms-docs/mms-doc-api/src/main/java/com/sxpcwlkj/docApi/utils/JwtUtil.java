package com.sxpcwlkj.docApi.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.lang.Singleton.put;

/**
 * @author shanpengnian
 */
@Slf4j
public class JwtUtil {

    private final String KEY= "4548912314JKJ85HT==";

    public String getId(HttpServletRequest request){
        String cookie = getCookieValue(request,"token");
        if(cookie!=null){
            if (StrUtil.isBlank(cookie) || cookie.split("\\.").length != 3) {
                log.error("无效的JWT格式: " + cookie);
                return "-1";
            }
            boolean verify = JWTUtil.verify(cookie, KEY.getBytes());
            if(verify){
                final JWT jwt = JWTUtil.parseToken(cookie);
                jwt.getHeader(JWTHeader.TYPE);
                return jwt.getPayload("id").toString();
            }
        }
        return "-1";
    }

    public String createToken(String id){
        Map<String, Object> map = new HashMap<String, Object>() {
            @Serial
            private static final long serialVersionUID = 1L;
            {
                put("id", id);
                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
            }
        };

        return JWTUtil.createToken(map, KEY.getBytes());
    }

    public String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
