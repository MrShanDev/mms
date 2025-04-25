package com.sxpcwlkj.wx.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 品创网络
 */
@Data
@Configuration
//@ConfigurationProperties(prefix = "pay.wx")
public class WxProperties {

    private Boolean enabled=false;

    //appId  公众号
    private String appId="you appid";
    //appSecret  公众号
    private String appSecret="you appSecret";
    //appId  小程序
    private String appIdMa;
    //appSecret  小程序
    private String appSecretMa;
    //appId  开放平台
    private String appIdOpen;
    //appSecret  开放平台
    private String appSecretOpen;
    //mchId 微信商户号和appid有签约关系
    private String mchId="you mchId";
    //mchKey 微信商户秘钥
    private String mchApiKey="you mchApiKey";
    // 微信商户  证书
    private String keyPath;
    // 微信商户  证书
    private String certPath;
    // V2 版本 V3 版本
    private String version;
    // 模式1: 公众号 2: 小程序 3: 开放平台
    private Integer modelType=1;
    // 支付回调
    private String notifyUrl;
    
    private String token;

    private String aesKey;

    private String attentionMsg;
    // 退款回调
    private String refundPath;


}
