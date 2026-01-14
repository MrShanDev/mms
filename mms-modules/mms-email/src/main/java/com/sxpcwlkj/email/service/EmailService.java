package com.sxpcwlkj.email.service;

import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.email.entity.EmailMessage;
import com.sxpcwlkj.email.enums.EmailTemplateType;

import java.util.Map;

/**
 * 邮件服务接口
 * 
 * @author shanpengnian
 * @author mmsAdmin
 */
public interface EmailService {

    // ==================== 基础发送方法 ====================

    /**
     * 发送纯文本邮件
     * 
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容（纯文本）
     * @return 发送结果
     */
    R<Object> sendTextEmail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     * 
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     * @return 发送结果
     */
    R<Object> sendHtmlEmail(String to, String subject, String htmlContent);

    /**
     * 发送邮件（通用方法）
     * 
     * @param emailMessage 邮件消息对象
     * @return 发送结果
     */
    R<Object> sendEmail(EmailMessage emailMessage);

    // ==================== 模板发送方法 ====================

    /**
     * 使用模板发送邮件
     * 
     * @param to 收件人邮箱
     * @param templateType 模板类型
     * @param variables 模板变量
     * @return 发送结果
     */
    R<Object> sendTemplateEmail(String to, EmailTemplateType templateType, Map<String, Object> variables);

    /**
     * 使用模板发送邮件（支持自定义应用名称）
     * 
     * @param to 收件人邮箱
     * @param templateType 模板类型
     * @param variables 模板变量
     * @param appName 应用名称
     * @return 发送结果
     */
    R<Object> sendTemplateEmail(String to, EmailTemplateType templateType, Map<String, Object> variables, String appName);

    // ==================== 快捷方法 ====================

    /**
     * 发送注册验证码邮件
     * 
     * @param email 收件人邮箱
     * @param code 验证码
     * @return 发送结果
     */
    R<Object> sendRegisterCode(String email, String code);

    /**
     * 发送注册验证码邮件（自定义应用名称）
     * 
     * @param email 收件人邮箱
     * @param code 验证码
     * @param appName 应用名称
     * @return 发送结果
     */
    R<Object> sendRegisterCode(String email, String code, String appName);

    /**
     * 发送密码找回邮件
     * 
     * @param email 收件人邮箱
     * @param username 用户名
     * @param code 验证码
     * @return 发送结果
     */
    R<Object> sendPasswordReset(String email, String username, String code);

    /**
     * 发送密码找回邮件（自定义应用名称）
     * 
     * @param email 收件人邮箱
     * @param username 用户名
     * @param code 验证码
     * @param appName 应用名称
     * @return 发送结果
     */
    R<Object> sendPasswordReset(String email, String username, String code, String appName);

    /**
     * 发送登录验证码邮件
     * 
     * @param email 收件人邮箱
     * @param code 验证码
     * @param loginInfo 登录信息（时间、IP、设备）
     * @return 发送结果
     */
    R<Object> sendLoginCode(String email, String code, Map<String, String> loginInfo);

    /**
     * 发送欢迎邮件
     * 
     * @param email 收件人邮箱
     * @param username 用户名
     * @return 发送结果
     */
    R<Object> sendWelcomeEmail(String email, String username);

    /**
     * 发送欢迎邮件（自定义应用名称和功能列表）
     * 
     * @param email 收件人邮箱
     * @param username 用户名
     * @param appName 应用名称
     * @param features 功能列表
     * @return 发送结果
     */
    R<Object> sendWelcomeEmail(String email, String username, String appName, String[] features);

    // ==================== 兼容旧方法 ====================

    /**
     * 发送邮箱验证码（兼容旧方法）
     * 
     * @param email 收件人邮箱
     * @param code 验证码
     * @return 发送结果
     * @deprecated 请使用 {@link #sendRegisterCode(String, String)}
     */
    @Deprecated
    R<Object> sendEmailCode(String email, String code);
}
