package com.sxpcwlkj.sms.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: xijue
 * @date: 2019/11/11 14:44
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {


    @Override
    public R<Object> sendSms(String phone, String content) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put("code", content);
        SmsBlend smsBlend = SmsFactory.getSmsBlend();
        SmsResponse smsResponse = smsBlend.sendMessage(phone,map);
        JSONObject parse = JSONUtil.parseObj(smsResponse.getData());
        log.info("sendSms:{},{}",smsResponse.isSuccess(), parse.getStr("Message"));
        return R.ok(smsResponse.isSuccess(),parse.getStr("Message"));
    }

    @Override
    public Boolean sendSms(String phone, String templateId, String content) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        //key为模板变量名称 value为模板变量值
        map.put("code", content);
        SmsBlend smsBlend = SmsFactory.getSmsBlend();
        SmsResponse smsResponse = smsBlend.sendMessage(phone,templateId,map);
        log.info("sendSms:{}",smsResponse.toString());
        return smsResponse.isSuccess();
    }

    @Override
    public Boolean sendSms(String configId,String phone, String templateId, String content) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        //key为模板变量名称 value为模板变量值
        map.put("code", content);
        SmsBlend smsBlend = SmsFactory.getSmsBlend();
        SmsResponse smsResponse = smsBlend.sendMessage(phone,templateId,map);
        log.info("sendSms:{}",smsResponse.toString());
        return smsResponse.isSuccess();
    }

    @Override
    public Boolean massTexting(List<String> phones, String message) {
        SmsBlend smsBlend = SmsFactory.getSmsBlend();
        SmsResponse smsResponse = smsBlend.massTexting(phones,message);
        log.info("sendSms:{}",smsResponse.toString());
        return smsResponse.isSuccess();
    }

}
