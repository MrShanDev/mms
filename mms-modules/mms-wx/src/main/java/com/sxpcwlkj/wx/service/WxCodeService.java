package com.sxpcwlkj.wx.service;

import com.sxpcwlkj.common.code.entity.WxCodeBo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 *
 * @author mmsAdmin
 */
public interface WxCodeService {


    /**
     * 形成一个带参数的二维码
     * @param wxCodeBo wxCodeBo
     * @return 图片
     */
    String getCode(WxCodeBo wxCodeBo);

    /**
     * 根据key 查询用户扫码状态
     * @param wxCodeBo  {type:要赋值}
     * @return  uuid
     */
    WxCodeBo getCodeState(WxCodeBo wxCodeBo);

    String succeed(String redisKey,String openId);

    String failing(String redisKey);

    /**
     * 公众号二维码
     * @param wxCodeBo wxCodeBo
     * @return 图片
     */
    String getMpCode(WxCodeBo wxCodeBo);

    /**
     * 小程序二维码
     * @param wxCodeBo wxCodeBo
     * @return 图片
     */
    String getMaCode(WxCodeBo wxCodeBo);

    /**
     * 开放平台二维码
     * @param wxCodeBo wxCodeBo
     * @return 图片
     */
    String getOpenCode(WxCodeBo wxCodeBo);

    /**
     * 公众号，事件回调
     *
     * @param message 微信消息
     * @return 响应消息
     */
    String scanCallBack(WxMpXmlMessage message);
}
