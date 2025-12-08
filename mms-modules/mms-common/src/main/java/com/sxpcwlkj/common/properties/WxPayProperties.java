package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 品创网络
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {

    private Boolean enabled = false;

    //appId  公众号
    private String appId="you appid";
    //appSecret  公众号
    private String appSecret="you appSecret";
    //appId  小程序
    private String appIdMa="you appIdMa";
    //appSecret  小程序
    private String appSecretMa="you appSecretMa";
    //appId  开放平台
    private String appIdOpen="you appIdOpen";
    //appSecret  开放平台
    private String appSecretOpen="you appSecretOpen";
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
