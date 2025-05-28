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
import com.sxpcwlkj.wx.config.WxProperties;
import com.sxpcwlkj.wx.service.WxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xijue
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxServiceImpl implements WxService {

    private final ApplicationContext context;

    @Override
    public WxProperties getWxProperties() {
        WxProperties wxProperties=new WxProperties();
        List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_wx.getKey());
        if(convert!=null){
            for(ConfigEntity config:convert){
                if("sys_wx_state".equals(config.getConfigKey())){
                    wxProperties.setEnabled(config.getConfigValue() != null && "1".equals(config.getConfigValue()));
                }
                //公众号
                if("sys_wx_mp_appid".equals(config.getConfigKey())){
                    wxProperties.setAppId(config.getConfigValue());
                }
                if("sys_wx_mp_appsecret".equals(config.getConfigKey())){
                    wxProperties.setAppSecret(config.getConfigValue());
                }
                //小程序
                if("sys_wx_miniapp_appid".equals(config.getConfigKey())){
                    wxProperties.setAppIdMa(config.getConfigValue());
                }
                if("sys_wx_miniapp_appsecret".equals(config.getConfigKey())){
                    wxProperties.setAppSecretMa(config.getConfigValue());
                }

                if("sys_wx_pay_id".equals(config.getConfigKey())){
                    wxProperties.setMchId(config.getConfigValue());
                }
                if("sys_wx_pay_appsecret".equals(config.getConfigKey())){
                    wxProperties.setMchApiKey(config.getConfigValue());
                }
                if("sys_wx_pay_path".equals(config.getConfigKey())){
                    wxProperties.setKeyPath(config.getConfigValue());
                }
                if("sys_wx_pay_type".equals(config.getConfigKey())){
                    wxProperties.setVersion(config.getConfigValue());
                }
                if("sys_wx_pay_notifyUrl".equals(config.getConfigKey())){
                    wxProperties.setNotifyUrl(config.getConfigValue());
                }
                if("sys_wx_token".equals(config.getConfigKey())){
                    wxProperties.setToken(config.getConfigValue());
                }
                if("sys_wx_aesKey".equals(config.getConfigKey())){
                    wxProperties.setAesKey(config.getConfigValue());
                }
                if("sys_wx_attention_msg".equals(config.getConfigKey())){
                    wxProperties.setAttentionMsg(config.getConfigValue());
                }
                if("sys_wx_model".equals(config.getConfigKey())){
                    wxProperties.setModelType(Convert.toInt(config.getConfigValue(),1));
                }
            }
        }
        if(!wxProperties.getEnabled()){
            log.error("JavaWx 配置未启用！");
            throw new MmsException("JavaWx 配置未启用！");
        }
        // 模式处理
        if(wxProperties.getModelType()==1){
            // 公众号
        }
        if(wxProperties.getModelType()==2){
            // 小程序
            wxProperties.setAppId(wxProperties.getAppIdMa());
            wxProperties.setAppSecret(wxProperties.getAppSecretMa());
        }
        if(wxProperties.getModelType()==3){
            // 开放平台
            wxProperties.setAppId(wxProperties.getAppIdOpen());
            wxProperties.setAppSecret(wxProperties.getAppSecretOpen());
        }
        return wxProperties;
    }

    @Override
    public WxMpService getWxMpService() {
        WxProperties wxProperties = this.getWxProperties();
        WxMpService  wxMpService =  context.getBean(WxMpService.class);
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(wxProperties.getAppId());
        config.setSecret(wxProperties.getAppSecret());
        config.setToken(wxProperties.getToken());
        config.setAesKey(wxProperties.getAesKey());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    @Override
    public WxMaService getWxMaService() {
        WxProperties wxProperties = this.getWxProperties();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxProperties.getAppId());
        config.setSecret(wxProperties.getAppSecret());
        config.setToken(wxProperties.getToken());
        config.setAesKey(wxProperties.getAesKey());
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

    @Override
    public WxPayService getWxPayService(String tradeType) {
        WxProperties wxProperties = this.getWxProperties();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxProperties.getAppId());
        payConfig.setMchId(wxProperties.getMchId());
        payConfig.setMchKey(wxProperties.getMchApiKey());
        payConfig.setNotifyUrl(wxProperties.getNotifyUrl());
        payConfig.setKeyPath(wxProperties.getKeyPath());
        payConfig.setTradeType(tradeType);
        payConfig.setSignType("MD5");
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
