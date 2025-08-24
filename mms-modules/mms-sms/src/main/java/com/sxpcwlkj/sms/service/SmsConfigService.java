package com.sxpcwlkj.sms.service;

import com.sxpcwlkj.common.code.entity.ConfigEntity;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.sms.enums.SmsTypeNum;
import lombok.RequiredArgsConstructor;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.qiniu.config.QiNiuConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mmsAdmin
 */
@Component
@RequiredArgsConstructor
public class SmsConfigService implements SmsReadConfig {


    @Override
    public BaseConfig getSupplierConfig(String configId) {
        //这里需要注意，UniConfig只是代表合一短信的配置类，仅做示例使用，根据不同厂商选择实例化不同的配置类
        //项目启动会将短信配置信息存入redis，此处从redis中获取配置信息
         List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_sms.getKey());
        //configId 为 初始化对象时候的configId，调用的时候获取 SmsFactory.getSmsBlend("configId"); 的时候传入的configId
        if(SmsTypeNum.aliyun.getValue().equals(configId)){
            AlibabaConfig  alibabaConfig = new AlibabaConfig();
            alibabaConfig.setConfigId(configId);
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKey_"+configId)){
                    alibabaConfig.setSdkAppId(smsConfigEntity.getConfigValue());
                    alibabaConfig.setAccessKeyId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKeySecret_"+configId)){
                    alibabaConfig.setAccessKeySecret(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_template_id_"+configId)){
                    alibabaConfig.setTemplateId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_signature_"+configId)){
                    alibabaConfig.setSignature(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            return alibabaConfig;
        }
        if(SmsTypeNum.tencent.getValue().equals(configId)){
            TencentConfig  tencentConfig = new TencentConfig();
            tencentConfig.setConfigId(configId);
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKey_"+configId)){
                    tencentConfig.setSdkAppId(smsConfigEntity.getConfigValue());
                    tencentConfig.setAccessKeyId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKeySecret_"+configId)){
                    tencentConfig.setAccessKeySecret(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_template_id_"+configId)){
                    tencentConfig.setTemplateId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_signature_"+configId)){
                    tencentConfig.setSignature(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            return tencentConfig;
        }
        if(SmsTypeNum.huawei.getValue().equals(configId)){
            HuaweiConfig  huaweiConfig = new HuaweiConfig();
            huaweiConfig.setConfigId(configId);
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKey_"+configId)){
                    huaweiConfig.setSdkAppId(smsConfigEntity.getConfigValue());
                    huaweiConfig.setAccessKeyId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKeySecret_"+configId)){
                    huaweiConfig.setAccessKeySecret(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_template_id_"+configId)){
                    huaweiConfig.setTemplateId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_signature_"+configId)){
                    huaweiConfig.setSignature(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            return huaweiConfig;
        }
        if(SmsTypeNum.ctyun.getValue().equals(configId)){
            CtyunConfig  ctyunConfig = new CtyunConfig();
            ctyunConfig.setConfigId(configId);
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKey_"+configId)){
                    ctyunConfig.setSdkAppId(smsConfigEntity.getConfigValue());
                    ctyunConfig.setAccessKeyId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKeySecret_"+configId)){
                    ctyunConfig.setAccessKeySecret(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_template_id_"+configId)){
                    ctyunConfig.setTemplateId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_signature_"+configId)){
                    ctyunConfig.setSignature(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            return ctyunConfig;
        }
        if(SmsTypeNum.qiniu.getValue().equals(configId)){
            QiNiuConfig  qiNiuConfig = new QiNiuConfig();
            qiNiuConfig.setConfigId(configId);
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKey_"+configId)){
                    qiNiuConfig.setSdkAppId(smsConfigEntity.getConfigValue());
                    qiNiuConfig.setAccessKeyId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_accessKeySecret_"+configId)){
                    qiNiuConfig.setAccessKeySecret(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_template_id_"+configId)){
                    qiNiuConfig.setTemplateId(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            convert.forEach(smsConfigEntity -> {
                if(smsConfigEntity.getConfigKey().equals("sys_sms_signature_"+configId)){
                    qiNiuConfig.setSignature(smsConfigEntity.getConfigValue());
                    return;
                };
            });
            return qiNiuConfig;
        }


        return new AlibabaConfig();
    }

    @Override
    public List<BaseConfig> getSupplierConfigList() {
        //此处仅为示例，实际环境中，数据可以来自任意位置，
        //List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_sms.getKey());
        //List<BaseConfig> endList=new ArrayList<>();
        return null;
    }
}
