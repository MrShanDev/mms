package com.sxpcwlkj.common.utils;

import java.io.UnsupportedEncodingException;

/**
 * 编码工具类
 *
 * @name: EncodingUtil
 * @author: 西决
 * @date: 2022/12/01
 **/

public class EncodingUtil {
    /**
     * URL编码加密  urlencode
     * @param str
     * @return
     */
    public static String getURLToUrlencode(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL编码解密  urlencode
     * @param str
     * @return
     */
    public static String getURLDecodeToString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Unicode编码 转义 字符串
     *
     * @param unicode
     * @return
     */
    public static String getUnicodeForString(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }

}
