package com.sxpcwlkj.wx.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.model.OrderQueryModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.FileUtil;
import com.sxpcwlkj.common.utils.JsonUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.wx.config.WxProperties;
import com.sxpcwlkj.wx.service.WxOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
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
    private final WxServiceImpl wxService;

    /**
     * 支付信息缓存key前缀
     */
    private static final String PAY_INFO_CACHE_PREFIX = "wx:pay:info:";

    /**
     * 支付信息缓存过期时间（分钟）
     */
    private static final int PAY_INFO_CACHE_EXPIRE_MINUTES = 30;

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
            //System.out.println("payPrice = " + payPrice);
            String notifyUrl=orderInfo.get("notifyUrl")!=null?orderInfo.get("notifyUrl").toString():"";

            WxProperties wxProperties = wxService.getWxProperties();
            if(StringUtil.isNotBlank(notifyUrl)){
                wxProperties.setNotifyUrl(notifyUrl);
            }

            WxPayApiConfig wxPayApiConfig =null;
            try {
                wxPayApiConfig = WxPayApiConfig.builder()
                        .appId(wxProperties.getAppId())
                        .mchId(wxProperties.getMchId())
                        .partnerKey(wxProperties.getMchApiKey())
                        .certPath(wxProperties.getMchApiKey())
                        .build();
            } catch (Exception e) {
                log.error("初始化微信支付失败");
            }

            assert wxPayApiConfig != null;

            // 先查询订单是否已存在
            Map<String, String> paramsQuery = OrderQueryModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .out_trade_no(orderNo)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
            String xmlResultQuery = WxPayApi.orderQuery(paramsQuery);
            log.info("查询订单:" + xmlResultQuery);

            Map<String, String> queryResult = WxPayKit.xmlToMap(xmlResultQuery);
            String queryReturnCode = queryResult.get("return_code");
            String queryResultCode = queryResult.get("result_code");

            // 定义Redis缓存key
            String payInfoCacheKey = PAY_INFO_CACHE_PREFIX + orderNo;

            // 如果订单查询成功且订单存在
            if (WxPayKit.codeIsOk(queryReturnCode) && WxPayKit.codeIsOk(queryResultCode)) {
                String tradeState = queryResult.get("trade_state");
                log.info("订单状态: {}", tradeState);

                // 如果订单未支付，从缓存获取支付信息
                if ("NOTPAY".equals(tradeState)) {
                    Map<String, Object> cachedPayInfo = RedisUtil.getCacheObject(payInfoCacheKey);
                    if (cachedPayInfo != null) {
                        log.info("从缓存获取支付信息: {}", JsonUtil.toJsonString(cachedPayInfo));
                        return R.success("获取缓存支付信息成功", cachedPayInfo.get("payData"));
                    } else {
                        return R.fail("订单已存在但支付信息已过期，请重新创建订单");
                    }
                } else if ("SUCCESS".equals(tradeState)) {
                    return R.fail("订单已支付成功，无需重复支付");
                } else if ("CLOSED".equals(tradeState) || "REVOKED".equals(tradeState)) {
                    // 订单已关闭或已撤销，可以重新下单
                    log.info("订单已关闭或撤销，可以重新下单");
                } else {
                    return R.fail("订单状态异常: " + tradeState);
                }
            }

            // 订单不存在或已关闭，进行统一下单
            Map<String, String> params = UnifiedOrderModel
                    .builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .nonce_str(WxPayKit.generateStr())
                    .body(productTitle.toString())
                    .attach(orderNo)
                    .out_trade_no(orderNo)
                    .total_fee(payPrice)
                    .spbill_create_ip(ip.toString())
                    .notify_url(wxProperties.getNotifyUrl())
                    .trade_type(tradeType.toString())
                    .openid(openId.toString())
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

            String xmlResult = WxPayApi.pushOrder(false, params);
            log.info("统一下单:" + xmlResult);
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

            // 统一下单成功，准备返回数据并缓存
            Object responseData = null;

            if (tradeType.equals(TradeType.NATIVE.getTradeType())) {
                String qrCodeUrl = result.get("code_url");
                BufferedImage qrCode = QrCodeUtil.generate(qrCodeUrl, 300, 300);
                responseData = FileUtil.bufferedImageToBase64(qrCode);
            }

            if (tradeType.equals(TradeType.JSAPI.getTradeType())) {
                // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
                String prepayId = result.get("prepay_id");
                Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayApiConfig.getAppId(), prepayId,
                    wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
                String jsonStr = JsonUtil.toJsonString(packageParams);
                log.info("小程序支付的参数:" + jsonStr);
                responseData = packageParams;
            }

            // 缓存支付信息到Redis，设置30分钟过期时间
            if (responseData != null) {
                Map<String, Object> payInfoCache = new HashMap<>();
                payInfoCache.put("orderNo", orderNo);
                payInfoCache.put("payPrice", payPrice);
                payInfoCache.put("productTitle", productTitle);
                payInfoCache.put("tradeType", tradeType);
                payInfoCache.put("payData", responseData);
                payInfoCache.put("createTime", System.currentTimeMillis());

                // 缓存支付信息
                RedisUtil.setCacheObject(payInfoCacheKey, payInfoCache, Duration.ofMinutes(PAY_INFO_CACHE_EXPIRE_MINUTES));
                log.info("支付信息已缓存到Redis，订单号: {}, 过期时间: {}分钟", orderNo, PAY_INFO_CACHE_EXPIRE_MINUTES);

                return R.success(returnMsg, responseData);
            }


        } catch (Exception e) {
            log.error("createPay error", e);
        }
        return null;
    }

    @Override
    public Boolean verifyNotify(Map<String, String> params) {
        WxProperties wxProperties = wxService.getWxProperties();
        WxPayApiConfig wxPayApiConfig =null;
        try {
            wxPayApiConfig = WxPayApiConfig.builder()
                .appId(wxProperties.getAppId())
                .mchId(wxProperties.getMchId())
                .partnerKey(wxProperties.getMchApiKey())
                .certPath(wxProperties.getMchApiKey())
                .build();
        } catch (Exception e) {
            return false;
        }

        assert wxPayApiConfig != null;
        return WxPayKit.verifyNotify(params, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
    }

    @Override
    public R<Object> selectPay(Map<String, Object> orderInfo) {
        return null;
    }

    /**
     * 清理指定订单的支付缓存信息
     *
     * @param orderNo 订单号
     * @return 是否清理成功
     */
    public boolean clearPayCache(String orderNo) {
        try {
            String payInfoCacheKey = PAY_INFO_CACHE_PREFIX + orderNo;
            boolean deleted = RedisUtil.deleteObject(payInfoCacheKey);
            log.info("清理订单支付缓存，订单号: {}, 结果: {}", orderNo, deleted);
            return deleted;
        } catch (Exception e) {
            log.error("清理订单支付缓存失败，订单号: {}", orderNo, e);
            return false;
        }
    }

    /**
     * 获取订单支付缓存信息
     *
     * @param orderNo 订单号
     * @return 缓存的支付信息
     */
    public Map<String, Object> getPayCache(String orderNo) {
        try {
            String payInfoCacheKey = PAY_INFO_CACHE_PREFIX + orderNo;
            Map<String, Object> cachedPayInfo = RedisUtil.getCacheObject(payInfoCacheKey);
            if (cachedPayInfo != null) {
                log.info("获取到订单支付缓存，订单号: {}", orderNo);
            } else {
                log.info("订单支付缓存不存在或已过期，订单号: {}", orderNo);
            }
            return cachedPayInfo;
        } catch (Exception e) {
            log.error("获取订单支付缓存失败，订单号: {}", orderNo, e);
            return null;
        }
    }

    @Override
    public R<Object> refund(String transactionNumber,String orderNo,BigDecimal totalFee){
        return refund(transactionNumber,orderNo,totalFee,totalFee);
    }
    @Override
    public R<Object> refund(String transactionNumber,String orderNo,BigDecimal totalFee,BigDecimal refundFee) {
        try {
            WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
            wxPayRefundRequest.setTransactionId(transactionNumber);
            wxPayRefundRequest.setOutRefundNo("refund_" + orderNo);
            // 元转成分
            wxPayRefundRequest.setTotalFee(totalFee.multiply(new BigDecimal(100)).intValue());
            wxPayRefundRequest.setRefundFee(refundFee.multiply(new BigDecimal(100)).intValue());
            wxPayRefundRequest.setNotifyUrl(wxService.getWxProperties().getRefundPath());
            WxPayRefundResult wxPayRefundResult;
            try {
                wxPayRefundResult = wxService.getWxPayService("JSAPI").refund(wxPayRefundRequest);
            } catch (WxPayException e) {
                e.printStackTrace();
                throw new MmsException("退款失败："+e.getErrCodeDes());
            }
            if("SUCCESS".equals(wxPayRefundResult.getReturnCode())&&"SUCCESS".equals(wxPayRefundResult.getResultCode())){
                return R.success("退款成功");
            }else{
                return R.fail("退款失败");
            }
        } catch (Exception e) {
            log.error("全额退款失败", e);
            return R.fail("退款失败: " + e.getMessage());
        }
    }


}
