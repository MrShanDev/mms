package com.sxpcwlkj.demo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shanpengnian
 */
@RequiredArgsConstructor
//@RestController
@Slf4j
public class OrderAliPayNotifyController {

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
//
//        try {
//            System.out.println("X=:" + body);
//
//            // 结果正确 outTradeNo
//            log.warn("交易单号:" );
//            log.warn("订单号:");
//            log.warn("付款金额:" );
//
//            if ("SUCCESS".equals("")) {
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
//

}
