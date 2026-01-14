package com.sxpcwlkj.email.service.impl;

import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.email.entity.EmailMessage;
import com.sxpcwlkj.email.enums.EmailTemplateType;
import com.sxpcwlkj.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.email.jakarta.api.MailClient;
import org.dromara.email.jakarta.comm.entity.MailMessage;
import org.dromara.email.jakarta.core.factory.MailFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

/**
 * 邮件服务实现类
 * 
 * @author shanpengnian
 * @author mmsAdmin
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MyEmailServiceImpl implements EmailService {

    @Value("${spring.application.name:品创网络}")
    private String defaultAppName;

    @Value("${email.support-email:support@example.com}")
    private String supportEmail;

    @Value("${email.expire-minutes:10}")
    private Integer expireMinutes;

    // ==================== 基础发送方法 ====================

    @Override
    public R<Object> sendTextEmail(String to, String subject, String content) {
        EmailMessage emailMessage = EmailMessage.builder()
            .to(Collections.singletonList(to))
            .subject(subject)
            .content(content)
            .isHtml(false)
            .build();
        return sendEmail(emailMessage);
    }

    @Override
    public R<Object> sendHtmlEmail(String to, String subject, String htmlContent) {
        EmailMessage emailMessage = EmailMessage.builder()
            .to(Collections.singletonList(to))
            .subject(subject)
            .htmlContent(htmlContent)
            .isHtml(true)
            .build();
        return sendEmail(emailMessage);
    }

    @Override
    public R<Object> sendEmail(EmailMessage emailMessage) {
        try {
            // 验证消息
            if (!emailMessage.validate()) {
                log.error("❌ 邮件消息验证失败: {}", emailMessage);
                return R.fail("邮件消息验证失败，请检查收件人和主题");
            }

            // 构建邮件消息
            MailMessage.Builder messageBuilder = MailMessage.Builder()
                .mailAddress(emailMessage.getTo())
                .title(emailMessage.getSubject());

            // 设置内容（HTML优先）
            if (emailMessage.getIsHtml() != null && emailMessage.getIsHtml() && emailMessage.getHtmlContent() != null) {
                messageBuilder.body(emailMessage.getHtmlContent());
            } else if (emailMessage.getContent() != null) {
                messageBuilder.body(emailMessage.getContent());
            }

            // 设置抄送
            if (emailMessage.getCc() != null && !emailMessage.getCc().isEmpty()) {
                messageBuilder.cc(emailMessage.getCc());
            }

            // 设置密送
            if (emailMessage.getBcc() != null && !emailMessage.getBcc().isEmpty()) {
                messageBuilder.bcc(emailMessage.getBcc());
            }

            // 设置附件
            if (emailMessage.getAttachments() != null && !emailMessage.getAttachments().isEmpty()) {
                messageBuilder.files(emailMessage.getAttachments());
            }

            MailMessage message = messageBuilder.build();

            // 发送邮件
            MailClient mail = MailFactory.createMailClient("sms4jMail");
            mail.send(message);

            log.info("✅ 邮件发送成功 - to: {}, subject: {}", 
                emailMessage.getFirstRecipient(), emailMessage.getSubject());
            return R.success("发送成功");

        } catch (Exception e) {
            log.error("❌ 邮件发送失败 - to: {}, subject: {}", 
                emailMessage.getFirstRecipient(), emailMessage.getSubject(), e);
            return R.fail("发送失败: " + e.getMessage());
        }
    }

    // ==================== 模板发送方法 ====================

    @Override
    public R<Object> sendTemplateEmail(String to, EmailTemplateType templateType, Map<String, Object> variables) {
        return sendTemplateEmail(to, templateType, variables, defaultAppName);
    }

    @Override
    public R<Object> sendTemplateEmail(String to, EmailTemplateType templateType, 
                                      Map<String, Object> variables, String appName) {
        try {
            // 添加默认变量
            Map<String, Object> allVariables = new HashMap<>(variables != null ? variables : new HashMap<>());
            allVariables.put("appName", appName);
            allVariables.put("year", Year.now().getValue());
            allVariables.put("supportEmail", supportEmail);
            allVariables.put("expireMinutes", expireMinutes);

            // 替换模板变量
            String subject = replaceVariables(templateType.getSubjectTemplate(), allVariables);
            String htmlContent = replaceVariables(templateType.getHtmlTemplate(), allVariables);

            // 发送邮件
            return sendHtmlEmail(to, subject, htmlContent);

        } catch (Exception e) {
            log.error("❌ 模板邮件发送失败 - to: {}, template: {}", 
                to, templateType.getName(), e);
            return R.fail("模板邮件发送失败: " + e.getMessage());
        }
    }

    // ==================== 快捷方法 ====================

    @Override
    public R<Object> sendRegisterCode(String email, String code) {
        return sendRegisterCode(email, code, defaultAppName);
    }

    @Override
    public R<Object> sendRegisterCode(String email, String code, String appName) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("code", code);
        return sendTemplateEmail(email, EmailTemplateType.REGISTER_CODE, variables, appName);
    }

    @Override
    public R<Object> sendPasswordReset(String email, String username, String code) {
        return sendPasswordReset(email, username, code, defaultAppName);
    }

    @Override
    public R<Object> sendPasswordReset(String email, String username, String code, String appName) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("code", code);
        return sendTemplateEmail(email, EmailTemplateType.PASSWORD_RESET, variables, appName);
    }

    @Override
    public R<Object> sendLoginCode(String email, String code, Map<String, String> loginInfo) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("code", code);
        variables.put("loginTime", loginInfo.getOrDefault("loginTime", "未知"));
        variables.put("loginIp", loginInfo.getOrDefault("loginIp", "未知"));
        variables.put("deviceInfo", loginInfo.getOrDefault("deviceInfo", "未知"));
        return sendTemplateEmail(email, EmailTemplateType.LOGIN_CODE, variables);
    }

    @Override
    public R<Object> sendWelcomeEmail(String email, String username) {
        return sendWelcomeEmail(email, username, defaultAppName, 
            new String[]{"完善个人信息", "浏览精选内容", "参与互动交流"});
    }

    @Override
    public R<Object> sendWelcomeEmail(String email, String username, String appName, String[] features) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("feature1", features.length > 0 ? features[0] : "使用各种功能");
        variables.put("feature2", features.length > 1 ? features[1] : "与他人交流");
        variables.put("feature3", features.length > 2 ? features[2] : "了解更多信息");
        variables.put("loginUrl", "https://example.com/login");
        return sendTemplateEmail(email, EmailTemplateType.WELCOME, variables, appName);
    }

    // ==================== 兼容旧方法 ====================

    @Override
    @Deprecated
    public R<Object> sendEmailCode(String email, String code) {
        log.warn("⚠️ 使用了已废弃的方法 sendEmailCode，请尽快迁移到 sendRegisterCode");
        return sendRegisterCode(email, code);
    }

    // ==================== 私有工具方法 ====================

    /**
     * 替换模板中的变量
     * 
     * @param template 模板内容
     * @param variables 变量集合
     * @return 替换后的内容
     */
    private String replaceVariables(String template, Map<String, Object> variables) {
        if (template == null || variables == null) {
            return template;
        }
        
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }
}
