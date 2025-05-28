package com.sxpcwlkj.wx.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.wx.service.WxCodeService;
import com.sxpcwlkj.wx.service.WxService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author xijue
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class WeChatController {

    private final WxService wxService;
    private final WxCodeService wxCodeService;
//    @GetMapping("/wx")
//    @SaIgnore
    public void test() throws WxErrorException {

        try {
            String openId="oW-WO0Tt3NOzBm5JOyaU-e8Cu_ks";
//            String send= wxService.getWxMpService().getKefuService().sendKefuMessageWithResponse(
//                WxMpKefuMessage.TEXT()
//                    .content("55555555555555555")
//                    .toUser(openId)
//                    .build());
//
//            log.info(send);

//            WxMaKefuMessage kefuMessage = new WxMaKefuMessage();
//            kefuMessage.setToUser("oVEkI7d2fd5BsGAbkQ6RXOXGJx6w");
//            String recoverMsg = "有新的实名认证申请，请及时审核！";
//            WxMaKefuMessage.KfText text = new WxMaKefuMessage.KfText(recoverMsg);
//            kefuMessage.setText(text);
//            kefuMessage.setMsgType(WxMaConstants.KefuMsgType.TEXT);
//            Boolean send= wxService.getWxMpService().sendKefuMsg(kefuMessage);

//            Map<String, String> dataMap=new HashMap<>();
//            dataMap.put("number2", "11111");
//            dataMap.put("thing3", "00000");
//            WxMpSubscribeMessage templateMessage = WxMpSubscribeMessage.builder()
//                .title("123456")
//                .contentColor("000000000")
//                .toUser(openId)
//                .templateId("QtJwbYV4fKCarqRj3e_FrxLrEX8gIcClZMcgGoVN0mU")
//                .url("https://www.mmsadmin.cn/")
//                .dataMap(dataMap)
//                .build();

//            wxMpService.getSubscribeMsgService().send(templateMessage);
//            log.info("发送订阅消息成功");

            String lang = "zh_CN";

//            WxMpUser user = wxMpService.getUserService().userInfo(openId, lang);
//            System.out.println(user.toString());

//            WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
//            massMessage.setMsgType("text");
//            massMessage.setContent("有新的系统认证需要处理!");
//            massMessage.getToUsers().add(openId);
//            massMessage.getToUsers().add("oW-WO0XLwjE5F7_0qoX2pQV_HaYQ");
//
//            WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
//            log.info(massResult.toString());


            JSONObject msg=new JSONObject();
            msg.put("touser",openId);
            msg.put("id","1");
            WxMpQrCodeTicket ticket = wxService.getWxMpService().getQrcodeService().qrCodeCreateTmpTicket(JSONUtil.toJsonStr(msg), 2592000);
            File file = wxService.getWxMpService().getQrcodeService().qrCodePicture(ticket);
            log.info(file.getPath());
            log.info(file.getAbsolutePath());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 微信公众号服器认证
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
     * 微信公众号扫码后的回调
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
            log.error("\n消息内容为：\n{} ", inMessage.toString());
            return wxCodeService.scanCallBack(inMessage);
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpService().getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            log.error("\n消息解密后内容为：\n{} ", inMessage.toString());
            return wxCodeService.scanCallBack(inMessage);
        }
        return "";
    }
}
