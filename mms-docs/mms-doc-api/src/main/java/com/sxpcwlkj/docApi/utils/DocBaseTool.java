package com.sxpcwlkj.docApi.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.lang.Singleton.put;

public class DocBaseTool {

    private final String KEY= "4548912314JKJ85HT==";

    public String getUserId(HttpServletRequest request){
        String cookie = request.getHeader("cookie");
        if(cookie!=null&&cookie.length()>10){
            cookie=cookie.substring(cookie.indexOf("=")+1);
        }
        boolean verify = JWTUtil.verify(cookie, KEY.getBytes());
        if(verify){
            final JWT jwt = JWTUtil.parseToken(cookie);
            jwt.getHeader(JWTHeader.TYPE);
            return jwt.getPayload("uid").toString();
        }
        return null;
    }

    public String getToken(String uid){
        Map<String, Object> map = new HashMap<String, Object>() {
            @Serial
            private static final long serialVersionUID = 1L;
            {
                put("uid", uid);
                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
            }
        };

        return JWTUtil.createToken(map, KEY.getBytes());
    }


}
