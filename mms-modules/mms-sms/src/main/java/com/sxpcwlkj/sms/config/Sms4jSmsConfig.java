package com.sxpcwlkj.sms.config;

import com.sxpcwlkj.common.code.entity.ConfigEntity;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.sms.enums.SmsTypeNum;
import com.sxpcwlkj.sms.service.SmsConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xijue
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class Sms4jSmsConfig {

   private final SmsConfigService config;
    @EventListener
    public void init(ContextRefreshedEvent event){

        // 创建SmsBlend 短信实例
        List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_sms.getKey());
        //configId 为 初始化对象时候的configId，调用的时候获取 SmsFactory.getSmsBlend("configId"); 的时候传入的configId
        AtomicReference<String> configId= new AtomicReference<>("");
        if(convert!=null){
            for(ConfigEntity smsConfigEntity:convert){
                if(smsConfigEntity.getConfigKey().equals("sys_sms_supplier_"+ SmsTypeNum.aliyun.getValue())){
                    if(smsConfigEntity.getConfigValue()!=null&&smsConfigEntity.getConfigValue().equals("1")){
                        configId.set(SmsTypeNum.aliyun.getValue());
                        break;
                    }
                }else
                if(smsConfigEntity.getConfigKey().equals("sys_sms_supplier_"+SmsTypeNum.tencent.getValue())){
                    if(smsConfigEntity.getConfigValue()!=null&&smsConfigEntity.getConfigValue().equals("1")){
                        configId.set(SmsTypeNum.tencent.getValue());
                        break;
                    }
                }else
                if(smsConfigEntity.getConfigKey().equals("sys_sms_supplier_"+SmsTypeNum.huawei.getValue())){
                    if(smsConfigEntity.getConfigValue()!=null&&smsConfigEntity.getConfigValue().equals("1")){
                        configId.set(SmsTypeNum.huawei.getValue());
                        break;
                    }
                }else
                if(smsConfigEntity.getConfigKey().equals("sys_sms_supplier_"+SmsTypeNum.ctyun.getValue())){
                    if(smsConfigEntity.getConfigValue()!=null&&smsConfigEntity.getConfigValue().equals("1")){
                        configId.set(SmsTypeNum.ctyun.getValue());
                        break;
                    }
                }else
                if(smsConfigEntity.getConfigKey().equals("sys_sms_supplier_"+SmsTypeNum.qiniu.getValue())){
                    if(smsConfigEntity.getConfigValue()!=null&&smsConfigEntity.getConfigValue().equals("1")){
                        configId.set(SmsTypeNum.qiniu.getValue());
                        break;
                    }
                }
            };
        }
        SmsFactory.createSmsBlend(config,configId.get());
        //SmsFactory.createSmsBlend(config);
        log.info("初始化 短信 配置");
    }
}
