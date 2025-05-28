package com.sxpcwlkj.wx.service;

import com.sxpcwlkj.common.utils.R;

import java.util.Map;

/**
 * @author shanpengnian
 */
public interface WxOrderService {

    /**
     * 创建订单
     * @param orderInfo 订单信息
     * @return  true or false
     */
    R<Object> createPay(Map<String ,Object> orderInfo);

    /**
     * 验证回调
     * @param params 参数
     * @return  true or false
     */
    Boolean verifyNotify(Map<String, String> params);

    /**
     * 查询订单
     * @param orderInfo 订单信息
     * @return  true or false
     */
    R<Object> selectPay(Map<String ,Object> orderInfo);

    /**
     * 订单回调
     * @param orderInfo 订单信息
     * @return  true or false
     */
    R<Object> notifyOrder(Map<String ,Object> orderInfo);
}
