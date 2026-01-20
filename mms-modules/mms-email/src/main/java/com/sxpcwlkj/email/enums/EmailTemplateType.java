package com.sxpcwlkj.email.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 邮件模板类型枚举
 * 模板文件存放在 resources/templates/email/ 目录下
 * 
 * @author mmsAdmin
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum EmailTemplateType {
    
    /**
     * 注册验证码模板
     */
    REGISTER_CODE("register_code", "注册验证码", 
        "【{appName}】注册验证码",
        "register_code.html"),
    
    /**
     * 绑定邮箱模板
     */
    BIND_EMAIL("bind_email", "绑定邮箱", 
        "【{appName}】绑定邮箱验证",
        "bind_email.html"),
    
    /**
     * 密码找回模板
     */
    PASSWORD_RESET("password_reset", "密码找回", 
        "【{appName}】密码找回验证",
        "password_reset.html"),
    
    /**
     * 登录验证码模板
     */
    LOGIN_CODE("login_code", "登录验证码",
        "【{appName}】登录验证码",
        "login_code.html"),
    
    /**
     * 账户变动通知
     */
    ACCOUNT_CHANGE("account_change", "账户变动通知",
        "【{appName}】账户变动通知",
        "account_change.html"),
    
    /**
     * 欢迎邮件
     */
    WELCOME("welcome", "欢迎邮件",
        "欢迎加入【{appName}】",
        "welcome.html"),
    
    /**
     * 订单通知
     */
    ORDER_NOTIFY("order_notify", "订单通知",
        "【{appName}】订单通知",
        "order_notify.html");
    
    /**
     * 模板代码
     */
    private final String code;
    
    /**
     * 模板名称
     */
    private final String name;
    
    /**
     * 邮件主题模板
     */
    private final String subjectTemplate;
    
    /**
     * HTML模板文件名
     */
    private final String templateFileName;
    
    /**
     * 模板目录（相对于 resources/template/）
     */
    private static final String TEMPLATE_PATH = "email/";

    /**
     * 获取模板完整路径（相对于 resources/template/，用于 sms4j）
     */
    public String getTemplatePath() {
        return TEMPLATE_PATH + templateFileName;
    }

    /**
     * 获取HTML模板内容
     * 从 resources/template/email/ 目录读取
     * 
     * @return HTML模板内容
     */
    public String getHtmlTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("template/" + TEMPLATE_PATH + templateFileName);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            log.error("❌ 读取邮件模板失败: {}", templateFileName, e);
            return "<html><body><h3>模板加载失败</h3></body></html>";
        }
    }
    
    /**
     * 根据模板代码获取枚举
     */
    public static EmailTemplateType getByCode(String code) {
        for (EmailTemplateType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
