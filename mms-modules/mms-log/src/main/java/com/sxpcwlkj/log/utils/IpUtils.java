package com.sxpcwlkj.log.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP 工具类
 *
 * @author mmsAdmin
 */
@Slf4j
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final int IP_LENGTH = 15;

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多级代理的情况
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        // 处理本地IP
        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IP;
        }

        return StrUtil.subPre(ip, IP_LENGTH);
    }

    /**
     * 获取本机IP
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取本机IP失败", e);
            return LOCALHOST_IP;
        }
    }

    /**
     * 获取本机主机名
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("获取本机主机名失败", e);
            return "Unknown";
        }
    }

    /**
     * 获取浏览器信息
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgentStr = request.getHeader("User-Agent");
        if (StrUtil.isBlank(userAgentStr)) {
            return "Unknown";
        }
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        return userAgent.getBrowser().getName() + " " + userAgent.getVersion();
    }

    /**
     * 获取操作系统信息
     */
    public static String getOs(HttpServletRequest request) {
        String userAgentStr = request.getHeader("User-Agent");
        if (StrUtil.isBlank(userAgentStr)) {
            return "Unknown";
        }
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        return userAgent.getOs().getName();
    }
}
