package com.sxpcwlkj.email.config;


import com.sxpcwlkj.common.code.entity.ConfigEntity;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.email.jakarta.comm.config.MailSmtpConfig;
import org.dromara.email.jakarta.core.factory.MailFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * JavaMail 配置
 * @Author mmsAdmin
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class MailConfig {

    @EventListener
    public void init(ContextRefreshedEvent event){
        MailProperties mailProperties=new MailProperties();
        List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_email.getKey());
        if(convert!=null){
            for(ConfigEntity smsConfigEntity:convert){
                if("sys_email_state".equals(smsConfigEntity.getConfigKey())){
                    if(smsConfigEntity.getConfigValue()!=null&&smsConfigEntity.getConfigValue().equals("1")){
                        mailProperties.setEnabled(true);
                    }else {
                        mailProperties.setEnabled(false);
                    }
                }
                if("sys_email_host".equals(smsConfigEntity.getConfigKey())){
                    mailProperties.setHost(smsConfigEntity.getConfigValue());
                }
                if("sys_email_port".equals(smsConfigEntity.getConfigKey())){
                    mailProperties.setPort(Integer.valueOf(smsConfigEntity.getConfigValue()));
                }
                if("sys_email_from".equals(smsConfigEntity.getConfigKey())){
                    mailProperties.setFrom(smsConfigEntity.getConfigValue());
                }
                if("sys_email_pass".equals(smsConfigEntity.getConfigKey())){
                    mailProperties.setPass(smsConfigEntity.getConfigValue());
                }
                if("sys_email_ssl".equals(smsConfigEntity.getConfigKey())){
                    if(smsConfigEntity.getConfigValue()!=null&&smsConfigEntity.getConfigValue().equals("1")){
                        mailProperties.setSslEnable(true);
                    }else {
                        mailProperties.setSslEnable(false);
                    }
                }
            }
        }
        if(!mailProperties.getEnabled()){
            log.info("JavaMail 配置未启用！");
            return ;
        }
        //Sms4jSmsDaoConfig 执行操作监听处理
        MailSmtpConfig config = MailSmtpConfig.builder()
            .port(mailProperties.getPort().toString())
            .fromAddress(mailProperties.getFrom())
            .smtpServer(mailProperties.getHost())
            .username(mailProperties.getFrom())
            .password(mailProperties.getPass())
            .nickName("品创网络")
            .isSSL(mailProperties.getSslEnable().toString())
            .isAuth(mailProperties.getStarttlsEnable().toString())
            .retryInterval(5)
            .maxRetries(1)
            .build();
        //这里的key可以是任何可对比类型，用于后续从工厂取出邮件实现类用
        MailFactory.put("sms4jMail",config);
//        MailFactory.createMailClient("sms4jMail");
        log.info("初始化 邮箱 配置");
    }

}
