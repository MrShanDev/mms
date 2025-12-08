package com.sxpcwlkj.wx.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业微信机器人消息工具类 - 静态方法版本
 */
@Slf4j
@Component
public class WeChatBotMessageUtil {

    private static String webhookUrl;
    private static boolean enabled;
    private static String msgType;
    private static RestTemplate restTemplate;

    @Value("${wechat.bot.webhook-url:}")
    public void setWebhookUrl(String webhookUrl) {
        WeChatBotMessageUtil.webhookUrl = webhookUrl;
    }

    @Value("${wechat.bot.enabled:false}")
    public void setEnabled(boolean enabled) {
        WeChatBotMessageUtil.enabled = enabled;
    }

    @Value("${wechat.bot.msg-type:text}")
    public void setMsgType(String msgType) {
        WeChatBotMessageUtil.msgType = msgType;
    }

    // 通过setter方法注入RestTemplate
    public static void setRestTemplate(RestTemplate restTemplate) {
        WeChatBotMessageUtil.restTemplate = restTemplate;
    }

    /**
     * 发送列表消息（自动选择文本或Markdown格式）- 静态方法
     */
    public static boolean sendListMessage(String content) {
        if (!enabled || webhookUrl == null || webhookUrl.trim().isEmpty()) {
            log.warn("微信机器人未启用或未配置webhook地址");
            return false;
        }

        if ("markdown".equals(msgType)) {
            String markdownContent = convertToMarkdown(content);
            return sendMarkdownMessage(markdownContent);
        } else {
            return sendTextMessage(content);
        }
    }

    /**
     * 发送文本消息 - 静态方法
     */
    public static boolean sendTextMessage(String content) {
        return sendTextMessage(content, null);
    }

    /**
     * 发送文本消息（指定@的人员）- 静态方法
     */
    public static boolean sendTextMessage(String content, String[] mentionedList) {
        if (!enabled || webhookUrl == null || webhookUrl.trim().isEmpty()) {
            log.warn("微信机器人未启用或未配置webhook地址");
            return false;
        }

        try {
            Map<String, Object> message = new HashMap<>();
            message.put("msgtype", "text");

            Map<String, Object> text = new HashMap<>();
            text.put("content", content);

            if (mentionedList != null && mentionedList.length > 0) {
                text.put("mentioned_list", mentionedList);
            }

            message.put("text", text);

            return sendWebhookRequest(message);
        } catch (Exception e) {
            log.error("发送企业微信文本消息失败", e);
            return false;
        }
    }

    /**
     * 发送Markdown消息 - 静态方法
     */
    public static boolean sendMarkdownMessage(String content) {
        if (!enabled || webhookUrl == null || webhookUrl.trim().isEmpty()) {
            log.warn("微信机器人未启用或未配置webhook地址");
            return false;
        }

        try {
            Map<String, Object> message = new HashMap<>();
            message.put("msgtype", "markdown");

            Map<String, Object> markdown = new HashMap<>();
            markdown.put("content", content);

            message.put("markdown", markdown);

            return sendWebhookRequest(message);
        } catch (Exception e) {
            log.error("发送企业微信Markdown消息失败", e);
            return false;
        }
    }

    /**
     * 发送告警消息（红色高亮）- 静态方法
     */
    public static boolean sendAlertMessage(String title, String content) {
        String markdownContent = String.format(
            "<font color=\"warning\">%s</font>\n\n%s", title, content
        );
        return sendMarkdownMessage(markdownContent);
    }

    /**
     * 发送成功消息（绿色高亮）- 静态方法
     */
    public static boolean sendSuccessMessage(String title, String content) {
        String markdownContent = String.format(
            "<font color=\"info\">%s</font>\n\n%s", title, content
        );
        return sendMarkdownMessage(markdownContent);
    }

    /**
     * 发送Webhook请求 - 静态方法
     */
    private static boolean sendWebhookRequest(Map<String, Object> message) {
        if (restTemplate == null) {
            log.error("RestTemplate未初始化，无法发送消息");
            return false;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("企业微信消息发送成功");
                return true;
            } else {
                log.error("企业微信消息发送失败，状态码: {}, 响应: {}",
                    response.getStatusCode(), response.getBody());
                return false;
            }
        } catch (Exception e) {
            log.error("企业微信消息发送异常", e);
            return false;
        }
    }

    /**
     * 将普通文本转换为Markdown格式 - 静态方法
     */
    private static String convertToMarkdown(String content) {
        if (content == null) {
            return "";
        }

        StringBuilder markdown = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                markdown.append("\n");
                continue;
            }

            if (line.contains("告警") || line.contains("异常") || line.contains("错误")) {
                markdown.append("### 🚨 ").append(line).append("\n");
            } else if (line.contains("成功") || line.contains("完成") || line.contains("正常")) {
                markdown.append("### ✅ ").append(line).append("\n");
            } else if (line.contains("警告") || line.contains("注意")) {
                markdown.append("### ⚠️ ").append(line).append("\n");
            } else {
                markdown.append(line).append("\n");
            }
        }

        return markdown.toString();
    }

    /**
     * 检查微信机器人配置状态 - 静态方法
     */
    public static String checkConfigStatus() {
        if (!enabled) {
            return "❌ 微信机器人未启用";
        }

        if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
            return "❌ 未配置Webhook地址";
        }

        if (restTemplate == null) {
            return "❌ RestTemplate未初始化";
        }

        return "✅ 微信机器人配置正常";
    }
}
