package com.sxpcwlkj.wx.service.impl;

import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.sxpcwlkj.common.utils.JsonUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.wx.config.WxProperties;
import com.sxpcwlkj.wx.service.WxOrderService;
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
public class WxOrderServiceImpl implements WxOrderService {

    private final com.github.binarywang.wxpay.service.WxPayService wxPayService;
    private final WxProperties wxProperties;

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
            if (!orderInfo.containsKey("ip")) {
                return R.fail("却少:[ip]属性(真实用户请求IP)");
            }
            Object tradeType = "JSAPI";
            if (orderInfo.containsKey("tradeType")) {
                tradeType = orderInfo.get("tradeType").toString();
            }

            Object openId = orderInfo.get("openId");
            String orderNo = orderInfo.get("orderNo").toString();
            Object productTitle = orderInfo.get("productTitle");
            String payPrice = orderInfo.get("payPrice").toString();
            Object ip = orderInfo.get("ip");
            System.out.println("payPrice = " + payPrice);


            WxPayApiConfig wxPayApiConfig =null;
            try {
                wxPayApiConfig = WxPayApiConfig.builder()
                        .appId(wxProperties.getAppId())
                        .mchId(wxProperties.getMchId())
                        .partnerKey(wxProperties.getMchApiKey())
                        .certPath(wxProperties.getMchApiKey())
//                        .domain(wxPayBean.getDomain())
                        .build();
            } catch (Exception e) {

            }

            Map<String, String> params = UnifiedOrderModel
                    .builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .nonce_str(WxPayKit.generateStr())
                    .body(productTitle.toString())
                    .attach(orderNo)
                    .out_trade_no(WxPayKit.generateStr())
                    .total_fee(payPrice)
                    .spbill_create_ip(ip.toString())
                    .notify_url(wxProperties.getNotifyUrl())
                    .trade_type(TradeType.JSAPI.getTradeType())
                    .openid(openId.toString())
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

            String xmlResult = WxPayApi.pushOrder(false, params);

            log.info(xmlResult);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

            String returnCode = result.get("return_code");
            String returnMsg = result.get("return_msg");
            if (!WxPayKit.codeIsOk(returnCode)) {
                return R.fail(returnMsg);
            }
            String resultCode = result.get("result_code");
            if (!WxPayKit.codeIsOk(resultCode)) {
                return  R.fail(returnMsg);
            }
            // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
            String prepayId = result.get("prepay_id");
            Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayApiConfig.getAppId(), prepayId,
                    wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
            String jsonStr = JsonUtil.toJsonString(packageParams);

            log.info("小程序支付的参数:" + jsonStr);
            return R.success("调起微信支付成功",packageParams);

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
