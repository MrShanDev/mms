package com.sxpcwlkj.demo.controller;

import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shanpengnian
 */
@RequiredArgsConstructor
//@RestController
@Slf4j
public class OrderWxPayNotifyController {
    private final WxPayService wxPayService;

    /**
     * 支付回调
     * @param req
     * @param resp
     * @param body
     * @return
     */
//    @RequestMapping("/payNotify")
//    @ResponseBody
//    public String selectOrder(HttpServletRequest req, HttpServletResponse resp, @RequestBody String body) {
//        WxPayOrderNotifyResult result = null;
//        try {
//            System.out.println("X=:" + body);
//            result = wxPayService.parseOrderNotifyResult(body);
//            // 结果正确 outTradeNo
//            log.warn("交易单号:" + result.getTransactionId());
//            log.warn("订单号:" + result.getOutTradeNo());
//            log.warn("付款金额:" + BaseWxPayResult.fenToYuan(result.getTotalFee()));
//
//            if ("SUCCESS".equals(result.getResultCode())) {
//                log.warn("支付回调进来了:付款成功");
//                // 更新订单状态
//                // 更新库存
//                // 发送通知
//                // 发送邮件
//                // 发送短信
//                // 发送微信消息
//
//
//                return "SUCCESS";
//            } else {
//                log.warn("支付回调进来了:付款失败");
//            }
//        } catch (Exception e) {
//            log.error("商品支付报错了",e);
//        }
//        return null;
//    }


}
