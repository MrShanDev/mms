package com.sxpcwlkj.demo.mail;

import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.email.entity.EmailMessage;
import com.sxpcwlkj.email.enums.EmailTemplateType;
import com.sxpcwlkj.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 邮件发送示例控制器
 * 
 * @author mmsAdmin
 */
@RestController
@RequestMapping("/demo/email")
@RequiredArgsConstructor
@Slf4j
public class EmailDemoController {

    private final EmailService emailService;

    // ==================== 基础发送示例 ====================

    /**
     * 示例1: 发送纯文本邮件
     */
    @PostMapping("/sendText")
    public R<Object> sendTextEmail(@RequestParam String to,
                                   @RequestParam String subject,
                                   @RequestParam String content) {
        log.info("📧 示例1: 发送纯文本邮件");
        return emailService.sendTextEmail(to, subject, content);
    }

    /**
     * 示例2: 发送HTML邮件
     */
    @PostMapping("/sendHtml")
    public R<Object> sendHtmlEmail(@RequestParam String to) {
        log.info("📧 示例2: 发送HTML邮件");
        
        String subject = "HTML邮件测试";
        String htmlContent = """
            <html>
            <body style='font-family: Arial;'>
                <h2 style='color: #1890ff;'>这是HTML邮件</h2>
                <p>支持<strong>加粗</strong>、<em>斜体</em>等格式</p>
                <ul>
                    <li>列表项1</li>
                    <li>列表项2</li>
                </ul>
            </body>
            </html>
            """;
        
        return emailService.sendHtmlEmail(to, subject, htmlContent);
    }

    /**
     * 示例3: 发送完整邮件（带抄送、密送）
     */
    @PostMapping("/sendComplete")
    public R<Object> sendCompleteEmail(@RequestParam String to,
                                       @RequestParam(required = false) String cc) {
        log.info("📧 示例3: 发送完整邮件");
        
        EmailMessage message = EmailMessage.builder()
            .to(Collections.singletonList(to))
            .cc(cc != null ? Collections.singletonList(cc) : null)
            .subject("完整邮件示例")
            .htmlContent("<h3>这是一封完整的邮件</h3><p>支持抄送、密送等功能</p>")
            .isHtml(true)
            .build();
        
        return emailService.sendEmail(message);
    }

    // ==================== 模板发送示例 ====================

    /**
     * 示例4: 发送注册验证码（默认应用名称）
     */
    @PostMapping("/sendRegisterCode")
    public R<Object> sendRegisterCode(@RequestParam String email) {
        log.info("📧 示例4: 发送注册验证码");
        
        String code = generateCode();
        return emailService.sendRegisterCode(email, code);
    }

    /**
     * 示例5: 发送注册验证码（自定义应用名称）
     */
    @PostMapping("/sendRegisterCodeWithApp")
    public R<Object> sendRegisterCodeWithApp(@RequestParam String email,
                                             @RequestParam String appName) {
        log.info("📧 示例5: 发送注册验证码（自定义应用）");
        
        String code = generateCode();
        return emailService.sendRegisterCode(email, code, appName);
    }

    /**
     * 示例6: 发送密码找回邮件
     */
    @PostMapping("/sendPasswordReset")
    public R<Object> sendPasswordReset(@RequestParam String email,
                                       @RequestParam String username) {
        log.info("📧 示例6: 发送密码找回邮件");
        
        String code = generateCode();
        return emailService.sendPasswordReset(email, username, code);
    }

    /**
     * 示例7: 发送密码找回邮件（自定义应用名称）
     */
    @PostMapping("/sendPasswordResetWithApp")
    public R<Object> sendPasswordResetWithApp(@RequestParam String email,
                                              @RequestParam String username,
                                              @RequestParam String appName) {
        log.info("📧 示例7: 发送密码找回邮件（自定义应用）");
        
        String code = generateCode();
        return emailService.sendPasswordReset(email, username, code, appName);
    }

    /**
     * 示例8: 发送登录验证码（带登录信息）
     */
    @PostMapping("/sendLoginCode")
    public R<Object> sendLoginCode(@RequestParam String email) {
        log.info("📧 示例8: 发送登录验证码");
        
        String code = generateCode();
        Map<String, String> loginInfo = new HashMap<>();
        loginInfo.put("loginTime", new Date().toString());
        loginInfo.put("loginIp", "192.168.1.100");
        loginInfo.put("deviceInfo", "Chrome 浏览器 / Windows 11");
        
        return emailService.sendLoginCode(email, code, loginInfo);
    }

    /**
     * 示例9: 发送欢迎邮件（默认配置）
     */
    @PostMapping("/sendWelcome")
    public R<Object> sendWelcome(@RequestParam String email,
                                 @RequestParam String username) {
        log.info("📧 示例9: 发送欢迎邮件");
        
        return emailService.sendWelcomeEmail(email, username);
    }

    /**
     * 示例10: 发送欢迎邮件（自定义应用和功能）
     */
    @PostMapping("/sendWelcomeWithFeatures")
    public R<Object> sendWelcomeWithFeatures(@RequestParam String email,
                                             @RequestParam String username,
                                             @RequestParam String appName) {
        log.info("📧 示例10: 发送欢迎邮件（自定义）");
        
        String[] features = {
            "创建和管理订单",
            "查看数据报表",
            "配置系统参数"
        };
        
        return emailService.sendWelcomeEmail(email, username, appName, features);
    }

    /**
     * 示例11: 使用自定义模板发送邮件
     */
    @PostMapping("/sendCustomTemplate")
    public R<Object> sendCustomTemplate(@RequestParam String email) {
        log.info("📧 示例11: 使用自定义模板");
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", "张三");
        variables.put("orderNo", "ORDER20260114001");
        variables.put("orderStatus", "已发货");
        variables.put("orderAmount", "299.00");
        variables.put("updateTime", new Date().toString());
        variables.put("orderMessage", "您的订单已发货，预计3天内送达。");
        
        return emailService.sendTemplateEmail(
            email, 
            EmailTemplateType.ORDER_NOTIFY, 
            variables
        );
    }

    /**
     * 示例12: 使用自定义模板发送邮件（自定义应用名称）
     */
    @PostMapping("/sendCustomTemplateWithApp")
    public R<Object> sendCustomTemplateWithApp(@RequestParam String email,
                                               @RequestParam String appName) {
        log.info("📧 示例12: 使用自定义模板（自定义应用）");
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", "李四");
        variables.put("changeType", "密码修改");
        variables.put("changeTime", new Date().toString());
        variables.put("changeContent", "您的密码已成功修改");
        
        return emailService.sendTemplateEmail(
            email, 
            EmailTemplateType.ACCOUNT_CHANGE, 
            variables,
            appName
        );
    }

    // ==================== 工具方法 ====================

    /**
     * 生成6位数字验证码
     */
    private String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    /**
     * 批量测试接口
     */
    @PostMapping("/testAll")
    public R<Map<String, Object>> testAll(@RequestParam String email) {
        log.info("📧 批量测试所有邮件功能");
        
        Map<String, Object> results = new LinkedHashMap<>();
        
        try {
            // 1. 纯文本邮件
            R<Object> r1 = sendTextEmail(email, "测试文本邮件", "这是纯文本内容");
            results.put("1_纯文本邮件", r1.getMsg());
            Thread.sleep(1000);
            
            // 2. HTML邮件
            R<Object> r2 = sendHtmlEmail(email);
            results.put("2_HTML邮件", r2.getMsg());
            Thread.sleep(1000);
            
            // 3. 注册验证码
            R<Object> r3 = emailService.sendRegisterCode(email, generateCode());
            results.put("3_注册验证码", r3.getMsg());
            Thread.sleep(1000);
            
            // 4. 密码找回
            R<Object> r4 = emailService.sendPasswordReset(email, "测试用户", generateCode());
            results.put("4_密码找回", r4.getMsg());
            Thread.sleep(1000);
            
            // 5. 欢迎邮件
            R<Object> r5 = emailService.sendWelcomeEmail(email, "测试用户");
            results.put("5_欢迎邮件", r5.getMsg());
            
            results.put("总体结果", "✅ 批量测试完成");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("批量测试被中断", e);
            results.put("错误", "批量测试被中断");
        } catch (RuntimeException e) {
            log.error("批量测试失败", e);
            results.put("错误", e.getMessage());
        }
        
        return R.success(results);
    }
}
