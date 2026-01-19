package com.sxpcwlkj.wx.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sxpcwlkj.common.code.entity.WxCodeBo;
import com.sxpcwlkj.common.enums.WxCodeStatusEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.wx.config.WxProperties;
import com.sxpcwlkj.wx.entity.WechatEventEnum;
import com.sxpcwlkj.wx.service.WxCodeService;
import com.sxpcwlkj.wx.service.WxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

/**
 *
 * @author mmsAdmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxCodeServiceImpl implements WxCodeService {
    private final WxService wxService;
    @Override
    public String getCode(WxCodeBo wxCodeBo) {
        WxProperties wxProperties = wxService.getWxProperties();
        String codeUrl="";
        if (wxProperties.getModelType() == 1) {
            codeUrl=  getMpCode(wxCodeBo);
        }
        if (wxProperties.getModelType() == 2) {
            codeUrl= getMaCode(wxCodeBo);
        }
        if (wxProperties.getModelType() == 3) {
            codeUrl= getOpenCode(wxCodeBo);
        }
        //保存二维码
        RedisUtil.setCacheObject(wxCodeBo.getRedisKey(),wxCodeBo.codeUrl(codeUrl).toJson() );
        RedisUtil.expire(wxCodeBo.getRedisKey(), wxCodeBo.getExpireTime());
        return codeUrl;
    }

    @Override
    public WxCodeBo getCodeState(WxCodeBo wxCodeBo) {
        String data= RedisUtil.getCacheObject(wxCodeBo.getRedisKey());
        if(data!=null){
            wxCodeBo= Convert.convert(WxCodeBo.class,JSONUtil.parseObj(data));
        }else {
            wxCodeBo=new WxCodeBo(wxCodeBo.getUuid()).typeLogin().state(WxCodeStatusEnum.FAILING);
        }
        return wxCodeBo;
    }

    @Override
    public String succeed(String redisKey,String openId) {
        WxCodeBo  wxCodeBo=null;
        String data= RedisUtil.getCacheObject(redisKey);
        if(data!=null){
            wxCodeBo= Convert.convert(WxCodeBo.class,JSONUtil.parseObj(data));
            RedisUtil.setCacheObject(wxCodeBo.getRedisKey(),wxCodeBo
                .state(WxCodeStatusEnum.SUCCEED)
                .openId(openId).toJson());
            RedisUtil.expire(wxCodeBo.getRedisKey(), wxCodeBo.getExpireTime());
            return "扫码成功";
        }
        return "扫码失败,请重新扫码";
    }

    @Override
    public String failing(String redisKey) {
        WxCodeBo  wxCodeBo=new WxCodeBo(redisKey);
        String data= RedisUtil.getCacheObject(wxCodeBo.getRedisKey());
        if(data!=null){
            wxCodeBo= Convert.convert(WxCodeBo.class,JSONUtil.parseObj(data));
            RedisUtil.setCacheObject(wxCodeBo.getRedisKey(),wxCodeBo
                .state(WxCodeStatusEnum.FAILING)
                .toJson());
            RedisUtil.expire(wxCodeBo.getRedisKey(), wxCodeBo.getExpireTime());
        }
        return "扫码失败,请重新扫码";
    }

    @Override
    public String getMpCode(WxCodeBo wxCodeBo) {
        try {
            WxMpQrCodeTicket ticket = wxService.getWxMpService().getQrcodeService().qrCodeCreateTmpTicket(wxCodeBo.getParamData(), wxCodeBo.getExpireTime());
            return wxService.getWxMpService().getQrcodeService().qrCodePictureUrl(ticket.getTicket());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("获取微信二维码失败");
        }
    }

    @Override
    public String getMaCode(WxCodeBo wxCodeBo) {
        return null;
    }

    @Override
    public String getOpenCode(WxCodeBo wxCodeBo) {
        return null;
    }


    @Override
    public String scanCallBack(WxMpXmlMessage message) {
        log.info("总的message：" + JSON.toJSONString(message));
        // content 公众号回复用户的文本内容
        String content = wxService.getWxProperties().getAttentionMsg();
        //消息类型
        String messageType = message.getMsgType();
        //消息事件
        String messageEvent = message.getEvent();
        //发送者帐号
        String fromUser = message.getFromUser();
        //开发者微信号
        String toUser = message.getToUser();
        //文本消息  文本内容
        String text = message.getContent();
        //携带参数
        String eventKey = message.getEventKey();
        /**
         * 1. 关注公众号 subscribe     messageType:event
         * 2. 取消关注 unsubscribe     messageType:event
         * 3. 扫码 SCAN               messageType:event
         * 6. 语音 voice              messageType:null
         * 7. 文本 text
         * 8. 图片 image
         * 9. 位置 location
         * 10. 链接 link
         * 11. 事件推送 event
         * 12. 点击菜单 click
         * 13. 点击菜单 view
         * 14. 模板消息推送 template_msg_push
         * 15. 小程序卡片消息卡片消息 card_msg_push
         */

        // 已关注 扫码 SCAN  发生文字 null subscribe unsubscribe  event   voice text image
        log.info("消息类型:{},消息事件:{},发送者账号:{},接收者微信:{},文本消息:{}", messageType, messageEvent, fromUser, toUser, text);
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = wxService.getWxMpService().getUserService().userInfo(fromUser);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        log.info("通过用户openid获取用户信息：" + JSON.toJSONString(wxMpUser));

        if (StringUtils.equalsAnyIgnoreCase(WechatEventEnum.UNSUBSCRIBE.getEventType(), messageEvent)) {
            //取消订阅
            log.info("取消订阅");
            return this.msgStr(message, "取消关注成功！");
        }
        if (StringUtils.equalsAnyIgnoreCase(WechatEventEnum.SUBSCRIBE.getEventType(), messageEvent)) {
            log.info("成功订阅");
            if(eventKey.startsWith("qrscene_")){
                eventKey=eventKey.substring(8);;
            }
            JSONObject businessParams = JSON.parseObject(eventKey);
            if(businessParams!=null&&businessParams.containsKey("redisKey")){
                String redisKey = businessParams.getString("redisKey");
                if (StringUtils.isNotEmpty(redisKey)) {
                    if (wxMpUser != null) {
                        //已知关注公众号，扫码登录
                        String msg= this.succeed(redisKey,wxMpUser.getOpenId());
                        return this.msgStr(message, msg);
                    }
                }
            }
            return this.msgStr(message, content);
        }
        if (StringUtils.equalsAnyIgnoreCase(WechatEventEnum.SCAN.getEventType(), messageEvent)) {
            log.info("带参数的二维码");
            //从二维码参数中获取uuid通过该uuid可通过websocket前端传数据
            JSONObject businessParams = JSON.parseObject(eventKey);
            if(businessParams!=null&&businessParams.containsKey("redisKey")){
                String redisKey = businessParams.getString("redisKey");
                if (StringUtils.isNotEmpty(redisKey)) {
                    if (wxMpUser != null) {
                        //已知关注公众号，扫码登录
                        String msg= this.succeed(redisKey,wxMpUser.getOpenId());
                        return this.msgStr(message, msg);
                    }
                }
            }
            return this.msgStr(message, "操作失败，请重新扫码！");
        }
        if (StringUtils.equalsAnyIgnoreCase(WechatEventEnum.LOCATION.getEventType(), messageType)) {
            log.info("上报位置");
            return this.msgStr(message, "位置上报成功：\n"+message.getLocationX()+" | "+message.getLocationY()+"\n"+message.getLabel());
        }
        if (StringUtils.equalsAnyIgnoreCase(WechatEventEnum.CLICK.getEventType(), messageType)) {
            log.info("点击菜单");
            return this.msgStr(message, "欢迎您的关注，您可以输入关键字来获取相关信息，如：帮助!");
        }
        if (StringUtils.equalsAnyIgnoreCase(WechatEventEnum.TEXT.getEventType(), messageType)) {
            log.info("文本消息");
            if (StringUtils.equalsAnyIgnoreCase("验证码", text)) {
                log.info("验证码");
                String code = DataUtil.getRandomSIX();
                assert wxMpUser != null;
                RedisUtil.setCacheObject(RedisConstant.WX_OPENID_KEY+wxMpUser.getOpenId(), code, Duration.ofMinutes(5));
                return this.msgStr(message, "验证码："+code+"\n请在五分钟内输入");
            }
            return this.msgStr(message, content);
        }
        if (StringUtils.equalsAnyIgnoreCase(WechatEventEnum.VIEW.getEventType(), messageType)) {
            log.info("文本消息");
            return this.msgStr(message, "文本消息");
        }
        return this.msgStr(message, "欢迎您的关注，您可以输入关键字来获取相关信息，如：帮助!");
    }
    private String msgStr(WxMpXmlMessage message, String content) {
        // 根据来时的信息格式，重组返回。(注意中间不能有空格)
        return "<xml>"
            + "<ToUserName><![CDATA[" + message.getFromUser() + "]]></ToUserName>"
            + "<FromUserName><![CDATA[" + message.getToUser() + "]]></FromUserName>"
            + "<CreateTime>" + new Date().getTime() + "</CreateTime>"
            + "<MsgType><![CDATA[text]]></MsgType>"
            + "<Content><![CDATA[" + content + "]]></Content>"
            + "</xml>";
    }
}
