package com.sxpcwlkj.wx.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.sxpcwlkj.wx.config.WxProperties;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * @author mmsAdmin
 */
public interface WxService {
    /**
     * 获取最新的配置信息
     *
     * @return WxProperties
     */
    WxProperties getWxProperties();

    /**
     * 获取微信公众号服务
     *
     * @return WxMpService
     */
    WxMpService getWxMpService();

    /**
     * 获取微信小程序服务
     *
     * @return WxMaService
     */
    WxMaService getWxMaService();

    /**
     * 获取微信支付服务
     * TradeType.NATIVE.getTradeType()
     * @return WxOrderService
     */
    WxPayService getWxPayService(String tradeType);




}
