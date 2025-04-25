package com.sxpcwlkj.aliyun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shanpengnian
 */
@Data
@Component
@ConfigurationProperties(prefix = "pay.aliyun")
public class AliYunProperties {

    //appId: 应用编号
    private String appId;
    //privateKey: 应用私钥
    private String privateKey;
    //publicKey: 支付宝公钥，通过应用公钥上传到支付宝开放平台换取支付宝公钥(如果是证书模式，公钥与私钥在CSR目录)。
    private String publicKey;
    //appCertPath: 应用公钥证书 (证书模式必须)
    private String appCertPath;
    //aliPayCertPath: 支付宝公钥证书 (证书模式必须)
    private String aliPayCertPath;
    //aliPayRootCertPath: 支付宝根证书 (证书模式必须)
    private String aliPayRootCertPath;
    //serverUrl: 支付宝支付网关
    private String serverUrl;
    //domain: 外网访问项目的域名，支付通知中会使用
    private String notifyUrl;


}
