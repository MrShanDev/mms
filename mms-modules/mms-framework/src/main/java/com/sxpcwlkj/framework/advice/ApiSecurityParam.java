package com.sxpcwlkj.framework.advice;

import com.sxpcwlkj.framework.utils.SignUtil;
import lombok.Data;

import java.util.Map;

/**
 * API 报文对象
 * @author mmsAdmin
 */
@Data
public class ApiSecurityParam  {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 签名
     */
    private String sign;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 加密的json参数
     */
    private String data;


    public ApiSecurityParam() {
        this.timestamp=System.currentTimeMillis()+"";
    }

    public ApiSecurityParam setAppid(String appId){
        this.appId = appId;
        return this;
    }
    public ApiSecurityParam  addData(String data){
        this.data = data;
        return this;
    }
    public ApiSecurityParam  addData(Map<String, Object> paramValues, Class<?> classType){
        this.data = SignUtil.tansParams(paramValues, SignUtil.getIgnoreSignParmas(classType));
        return this;
    }
}
