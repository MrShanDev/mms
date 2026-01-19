package com.sxpcwlkj.wx.service;

import com.sxpcwlkj.common.utils.R;

import java.math.BigDecimal;
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
     * 退款全款
     * @param transactionNumber 商户交易号
     * @param orderNo           订单号
     * @param totalFee          订单总金额
     * @return  true or false
     */
    R<Object> refund(String transactionNumber,String orderNo,BigDecimal totalFee);

    /**
     * 退款部分
     * @param transactionNumber 商户交易号
     * @param orderNo           订单号
     * @param totalFee          订单总金额
     * @param refundFee         退款金额
     * @return  true or false
     */
    R<Object> refund(String transactionNumber,String orderNo,BigDecimal totalFee,BigDecimal refundFee);


    /**
     * 清理指定订单的支付缓存信息
     *
     * @param orderNo 订单号
     * @return 是否清理成功
     */
    boolean clearPayCache(String orderNo);

    /**
     * 获取订单支付缓存信息
     *
     * @param orderNo 订单号
     * @return 缓存的支付信息
     */
    Map<String, Object> getPayCache(String orderNo);
}
