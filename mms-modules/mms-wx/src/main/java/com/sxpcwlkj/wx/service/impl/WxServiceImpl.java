package com.sxpcwlkj.wx.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.convert.Convert;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.sxpcwlkj.common.code.entity.ConfigEntity;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.common.properties.WxPayProperties;
import com.sxpcwlkj.wx.service.WxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mmsAdmin
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxServiceImpl implements WxService {

    private final ApplicationContext context;

    @Override
    public WxPayProperties getWxProperties() {
        WxPayProperties wxPayProperties =new WxPayProperties();
        if(wxPayProperties.getEnabled()){
            //使用yml中的配置
           return wxPayProperties;
        }
        List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_wx.getKey());
        if(convert!=null){
            for(ConfigEntity config:convert){
                if("sys_wx_state".equals(config.getConfigKey())){
                    wxPayProperties.setEnabled(config.getConfigValue() != null && "1".equals(config.getConfigValue()));
                }
                //公众号
                if("sys_wx_mp_appid".equals(config.getConfigKey())){
                    wxPayProperties.setAppId(config.getConfigValue());
                }
                if("sys_wx_mp_appsecret".equals(config.getConfigKey())){
                    wxPayProperties.setAppSecret(config.getConfigValue());
                }
                //小程序
                if("sys_wx_miniapp_appid".equals(config.getConfigKey())){
                    wxPayProperties.setAppIdMa(config.getConfigValue());
                }
                if("sys_wx_miniapp_appsecret".equals(config.getConfigKey())){
                    wxPayProperties.setAppSecretMa(config.getConfigValue());
                }

                if("sys_wx_pay_id".equals(config.getConfigKey())){
                    wxPayProperties.setMchId(config.getConfigValue());
                }
                if("sys_wx_pay_appsecret".equals(config.getConfigKey())){
                    wxPayProperties.setMchApiKey(config.getConfigValue());
                }
                if("sys_wx_pay_path".equals(config.getConfigKey())){
                    wxPayProperties.setKeyPath(config.getConfigValue());
                }
                if("sys_wx_pay_type".equals(config.getConfigKey())){
                    wxPayProperties.setVersion(config.getConfigValue());
                }
                if("sys_wx_pay_notifyUrl".equals(config.getConfigKey())){
                    wxPayProperties.setNotifyUrl(config.getConfigValue());
                }
                if("sys_wx_token".equals(config.getConfigKey())){
                    wxPayProperties.setToken(config.getConfigValue());
                }
                if("sys_wx_aesKey".equals(config.getConfigKey())){
                    wxPayProperties.setAesKey(config.getConfigValue());
                }
                if("sys_wx_attention_msg".equals(config.getConfigKey())){
                    wxPayProperties.setAttentionMsg(config.getConfigValue());
                }
                if("sys_wx_model".equals(config.getConfigKey())){
                    wxPayProperties.setModelType(Convert.toInt(config.getConfigValue(),1));
                }
            }
        }
        // 模式处理
        if(wxPayProperties.getModelType()==1){
            // 公众号
        }
        if(wxPayProperties.getModelType()==2){
            // 小程序
            wxPayProperties.setAppId(wxPayProperties.getAppIdMa());
            wxPayProperties.setAppSecret(wxPayProperties.getAppSecretMa());
        }
        if(wxPayProperties.getModelType()==3){
            // 开放平台
            wxPayProperties.setAppId(wxPayProperties.getAppIdOpen());
            wxPayProperties.setAppSecret(wxPayProperties.getAppSecretOpen());
        }
        return wxPayProperties;
    }

    @Override
    public WxMpService getWxMpService() {
        WxPayProperties wxPayProperties = this.getWxProperties();
        WxMpService  wxMpService =  context.getBean(WxMpService.class);
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(wxPayProperties.getAppId());
        config.setSecret(wxPayProperties.getAppSecret());
        config.setToken(wxPayProperties.getToken());
        config.setAesKey(wxPayProperties.getAesKey());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    @Override
    public WxMaService getWxMaService() {
        WxPayProperties wxPayProperties = this.getWxProperties();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxPayProperties.getAppId());
        config.setSecret(wxPayProperties.getAppSecret());
        config.setToken(wxPayProperties.getToken());
        config.setAesKey(wxPayProperties.getAesKey());
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

    @Override
    public WxPayService getWxPayService(String tradeType) {
        WxPayProperties wxPayProperties = this.getWxProperties();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxPayProperties.getAppId());
        payConfig.setMchId(wxPayProperties.getMchId());
        payConfig.setMchKey(wxPayProperties.getMchApiKey());
        payConfig.setNotifyUrl(wxPayProperties.getNotifyUrl());
        payConfig.setKeyPath(wxPayProperties.getKeyPath());
        payConfig.setTradeType(tradeType);
        payConfig.setSignType("MD5");
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
