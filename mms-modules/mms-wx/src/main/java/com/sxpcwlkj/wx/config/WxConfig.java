package com.sxpcwlkj.wx.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.sxpcwlkj.common.properties.WxPayProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信 配置
 *
 * @Author mmsAdmin
 */
@Data
@Configuration
@RequiredArgsConstructor
public class WxConfig {

    private final WxPayProperties wxPayProperties;


    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(wxPayProperties.getAppId());
        config.setSecret(wxPayProperties.getAppSecret());

        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(config);
        return service;
    }

    @Bean
    public WxMaService wxMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxPayProperties.getAppId());
        config.setSecret(wxPayProperties.getAppSecret());
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }


    @Bean
    public WxPayService wxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxPayProperties.getAppId());
        payConfig.setMchId(wxPayProperties.getMchId());
        payConfig.setMchKey(wxPayProperties.getMchApiKey());
        payConfig.setNotifyUrl(wxPayProperties.getNotifyUrl());
        payConfig.setKeyPath(wxPayProperties.getKeyPath());
        payConfig.setTradeType("JSAPI");
        payConfig.setSignType("MD5");
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }


}
