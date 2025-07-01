package com.sxpcwlkj.sms.service;

import com.sxpcwlkj.common.utils.R;

import java.util.List;

/**
 * @author: mmsAdmin
 */
public interface SmsService {

    /**
     * 默认平台发生验证码
     * @param phone 手机号
     * @param content 验证码
     * @return true or false
     */
    R<Object> sendSms(String phone, String content);

    /**
     * 默认平台发生验证码
     * @param phone 手机号
     * @param templateId 短信模版ID
     * @param content 变量内容（变量名默认为${code}）
     * @return true or false
     */
    Boolean sendSms(String phone, String templateId, String content);

    /**
     * 是定平台发送验证码
     * @param configId 平台配置ID
     * @param phone 手机号
     * @param templateId 短信模
     * @param content 变量内容（变量名默认为${code}）
     * @return true or false
     */
    Boolean sendSms(String configId,String phone, String templateId, String content);

    /**
     * 默认平台群发短信
     * @param phones 手机号
     * @param message 短信内容
     * @return 发送结果
     */
    Boolean massTexting(List<String> phones, String message);

}
