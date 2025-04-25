package com.sxpcwlkj.aliyun.service.impl;


import com.sxpcwlkj.aliyun.service.AliPayService;
import com.sxpcwlkj.common.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shanpengnian
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {


    @Override
    public R<Object> createPay(Map<String, Object> orderInfo) {

        try {

            //判断Map中是否有openId属性
            if (!orderInfo.containsKey("openId")) {
                return R.fail("却少:[openId]属性(公共号/小程序 所获取的微信openId)");
            }
            if (!orderInfo.containsKey("orderNo")) {
                return R.fail("却少:[orderNo]属性(唯一标识)");
            }
            if (!orderInfo.containsKey("productTitle")) {
                return R.fail("却少:[productTitle]属性(自定义)");
            }
            if (!orderInfo.containsKey("payPrice")) {
                return R.fail("却少:[payPrice]属性(单位:分)");
            }
            Object tradeType = "JSAPI";
            if (!orderInfo.containsKey("tradeType")) {
                tradeType = orderInfo.get("tradeType").toString();
            }

            Object openId = orderInfo.get("openId");
            Object orderNo = orderInfo.get("orderNo");
            Object productTitle = orderInfo.get("productTitle");
            Object payPrice = orderInfo.get("payPrice");

            // 支付
            log.info("调起微信支付");

            // 调起支付


            return R.success();
        } catch (Exception e) {
            log.error("createPay error", e);
        }
        return null;
    }

    @Override
    public R<Object> selectPay(Map<String, Object> orderInfo) {
        return null;
    }

    @Override
    public R<Object> notifyOrder(Map<String, Object> orderInfo) {
        return null;
    }
}
