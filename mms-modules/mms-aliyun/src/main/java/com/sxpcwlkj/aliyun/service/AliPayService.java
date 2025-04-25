package com.sxpcwlkj.aliyun.service;

import com.sxpcwlkj.common.utils.R;

import java.util.Map;

public interface AliPayService {

    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    R<Object> createPay(Map<String ,Object> orderInfo);

    /**
     * 查询订单
     * @param orderInfo
     * @return
     */
    R<Object> selectPay(Map<String ,Object> orderInfo);

    /**
     * 订单回调
     * @param orderInfo
     * @return
     */
    R<Object> notifyOrder(Map<String ,Object> orderInfo);
}
