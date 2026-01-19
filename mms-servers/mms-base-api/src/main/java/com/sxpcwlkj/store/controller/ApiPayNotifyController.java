package com.sxpcwlkj.store.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.ijpay.core.kit.WxPayKit;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.store.service.StoreOrderService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.wx.service.WxOrderService;
import com.sxpcwlkj.wx.service.WxService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mall-api/payNotify/v1")
public class ApiPayNotifyController {
    private final WxService wxService;
    private final WxOrderService wxOrderService;
    private final StoreOrderService storeOrderService;
    /**
     * 微信支付WxPay
     * 异步通知
     */
    @RequestMapping(value = "/spuOrderPayNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @SaIgnore
    public String spuOrderPayNotify(HttpServletRequest request, @RequestBody String body) {
        log.error("支付通知=" + body);
        Map<String, String> params = WxPayKit.xmlToMap(body);
        System.out.println("支付通知=" + params);
        String returnCode = params.get("return_code");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 注意此处签名方式需与统一下单的签名类型一致
        if (wxOrderService.verifyNotify(params)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 更新订单信息
                //分转成元
                BigDecimal price=new BigDecimal(params.get("total_fee")).multiply(new BigDecimal("0.01"));
                System.out.println("支付金额=" + price);
                Date payTime= DateUtil.parse(params.get("time_end"),DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS);
                Boolean state= storeOrderService.updateSuccessPayByOrderNo(params.get("out_trade_no"),params.get("transaction_id"),params.get("mch_id"),price,payTime);
                // 发送通知等
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", state?"SUCCESS":"FAIL");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }

    /**
     * 退款回调
     * @param req req
     * @param resp resp
     * @param body body
     * @return 回调通知
     */
    @SaIgnore
    @RequestMapping("/spuOrderRefundNotify")
    @ResponseBody
    public String spuOrderRefundNotify(HttpServletRequest req, HttpServletResponse resp, @RequestBody String body) {
        WxPayRefundNotifyResult result = null;

        try {
            result = wxService.getWxPayService("JSAPI").parseRefundNotifyResult(body);;
            log.info("退款回调结果：{}", result);
            if (result.getReturnCode().equals("SUCCESS")) {
                String  lockKey = "payment:" + result.getReqInfo().getOutTradeNo();
                try {
                    if(RedisUtil.tryLock(lockKey, 30, TimeUnit.SECONDS)){
                        // 退款成功 "SUCCESS":"ABNORMAL"
                        return storeOrderService.notifyOrder(result.getReqInfo())? "SUCCESS":"ABNORMAL";
                    }else {
                        System.out.println("获取锁失败，可能其他线程正在处理");
                    }
                }finally {
                    RedisUtil.unlock(lockKey);
                }
            } else {
                // 退款失败
                return "ABNORMAL";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("退款回调异常：{}", e.getMessage());
            e.fillInStackTrace();
            return "ABNORMAL";
        }finally {
            log.info("退款回调结果：{}", result);
        }
        return "ABNORMAL";
    }

}
