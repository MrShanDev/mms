package com.sxpcwlkj.demo.sms.impl;

import com.sxpcwlkj.demo.sms.DemoSmsService;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class DemoSmsServiceImpl implements DemoSmsService {
    @Override
    public Boolean sendSms(String phone, String code) {
        //在创建完SmsBlend实例后，再未手动调用注销的情况下框架会持有该实例，可以直接通过指定configId来获取想要的配置，如果你想使用
        //负载均衡形式获取实例，只要使用getSmsBlend的无参重载方法即可，如果你仅有一个配置，也可以使用该方法
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put("code", code);
        SmsBlend smsBlend = SmsFactory.getSmsBlend("tx1");
        SmsResponse smsResponse1 = smsBlend.sendMessage(phone,map);
        System.out.println(smsResponse1.toString());
        //SmsResponse smsResponse2 = smsBlend.sendMessage("13389180000","SMS_242811287",map);
        //System.out.println(smsResponse2.toString());
        return smsResponse1.isSuccess();
    }
}
