package com.sxpcwlkj.wx.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.wx.service.WxCodeService;
import com.sxpcwlkj.wx.service.WxOrderService;
import com.sxpcwlkj.wx.service.WxService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.io.File;

/**
 * @author mmsAdmin
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class WeChatController {

    private final WxService wxService;
    private final WxCodeService wxCodeService;
    private final WxOrderService wxOrderService;

    /**
     * 微信公众号/服务号服器认证
     *
     * @param request 请求
     * @return 响应
     */
    @GetMapping("/wechat")
    @SaIgnore
    public String verifyWeChat( HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info("\n接收到来自微信服务器的认证消息：[signature:{}, timestamp:{}, nonce:{}, echostr:{}]", signature, timestamp, nonce, echostr);
        if (!wxService.getWxMpService().checkSignature(timestamp, nonce, signature)) {
            log.error("【无效的请求】");
            throw new MmsException("无效的请求");
        }
        return echostr;
    }


    /**
     * 微信公众号/服务号扫码后的回调
     *
     * @param request 请求
     * @param requestBody body
     * @return 响应
     */
    @SaIgnore
    @PostMapping("/wechat")
    public String scanCallBack(HttpServletRequest request, @RequestBody String requestBody) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String openid = request.getParameter("openid");
        String encType = request.getParameter("encType");
        String msgSignature = request.getParameter("msgSignature");
        log.info("\n接收到来自微信公众号扫码后的回调：[signature:{}, timestamp:{}, nonce:{}, echostr:{},encType:{},msgSignature:{}]", signature, timestamp, nonce, echostr, encType, msgSignature);
        if (!wxService.getWxMpService().checkSignature(timestamp, nonce, signature)) {
            log.error("【无效的请求】");
            throw new MmsException("无效的请求");
        }
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            log.error("\n微信公众号扫码后的回调消息内容为：\n{} ", inMessage.toString());
            return wxCodeService.scanCallBack(inMessage);
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpService().getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            log.error("\n微信公众号扫码后的回调消息解密后内容为：\n{} ", inMessage.toString());
            return wxCodeService.scanCallBack(inMessage);
        }
        return "";
    }
    
    /**
     * 创建微信支付订单
     *
     * @param orderInfo 订单信息
     * @return 支付信息
     */
    @PostMapping("/pay/create")
    @SaIgnore
    public R<Object> createPay(@RequestBody Map<String, Object> orderInfo) {
        try {
            log.info("创建微信支付订单，订单信息: {}", orderInfo);
            return wxOrderService.createPay(orderInfo);
        } catch (Exception e) {
            log.error("创建微信支付订单失败", e);
            return R.fail("创建支付订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取订单支付缓存信息
     *
     * @param orderNo 订单号
     * @return 缓存的支付信息
     */
    @GetMapping("/pay/cache/{orderNo}")
    @SaIgnore
    public R<Object> getPayCache(@PathVariable String orderNo) {
        try {
            log.info("获取订单支付缓存，订单号: {}", orderNo);
            Map<String, Object> payCache = wxOrderService.getPayCache(orderNo);
            if (payCache != null) {
                return R.success("获取支付缓存成功", payCache);
            } else {
                return R.fail("支付缓存不存在或已过期");
            }
        } catch (Exception e) {
            log.error("获取订单支付缓存失败，订单号: {}", orderNo, e);
            return R.fail("获取支付缓存失败: " + e.getMessage());
        }
    }
    
    /**
     * 清理订单支付缓存
     *
     * @param orderNo 订单号
     * @return 清理结果
     */
    @DeleteMapping("/pay/cache/{orderNo}")
    @SaIgnore
    public R<Object> clearPayCache(@PathVariable String orderNo) {
        try {
            log.info("清理订单支付缓存，订单号: {}", orderNo);
            boolean cleared = wxOrderService.clearPayCache(orderNo);
            if (cleared) {
                return R.success("清理支付缓存成功");
            } else {
                return R.fail("支付缓存不存在或清理失败");
            }
        } catch (Exception e) {
            log.error("清理订单支付缓存失败，订单号: {}", orderNo, e);
            return R.fail("清理支付缓存失败: " + e.getMessage());
        }
    }
}
