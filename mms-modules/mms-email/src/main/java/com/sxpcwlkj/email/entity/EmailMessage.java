package com.sxpcwlkj.email.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 邮件消息实体
 * 
 * @author mmsAdmin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 收件人邮箱（单个或多个）
     */
    private List<String> to;
    
    /**
     * 抄送人邮箱
     */
    private List<String> cc;
    
    /**
     * 密送人邮箱
     */
    private List<String> bcc;
    
    /**
     * 邮件主题
     */
    private String subject;
    
    /**
     * 邮件内容（纯文本）
     */
    private String content;
    
    /**
     * HTML内容（优先级高于纯文本）
     */
    private String htmlContent;
    
    /**
     * 模板路径（若使用模板引擎则设置此项）
     */
    private String templatePath;
    
    /**
     * 模板变量
     */
    private Map<String, Object> templateVariables;
    
    /**
     * 附件文件路径列表
     */
    private Map<String, String> attachments;
    
    /**
     * 是否HTML邮件
     */
    private Boolean isHtml;
    
    /**
     * 应用名称（用于模板变量）
     */
    private String appName;
    
    /**
     * 验证消息有效性
     */
    public boolean validate() {
        return to != null && !to.isEmpty() && subject != null;
    }
    
    /**
     * 获取第一个收件人（用于单收件人场景）
     */
    public String getFirstRecipient() {
        return to != null && !to.isEmpty() ? to.get(0) : null;
    }
}
