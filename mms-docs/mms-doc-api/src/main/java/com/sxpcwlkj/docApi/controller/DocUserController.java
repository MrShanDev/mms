package com.sxpcwlkj.docApi.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.entity.WxCodeBo;
import com.sxpcwlkj.common.enums.WxCodeStatusEnum;
import com.sxpcwlkj.common.utils.*;
import com.sxpcwlkj.docApi.entity.DocOrder;
import com.sxpcwlkj.docApi.entity.DocProduct;
import com.sxpcwlkj.docApi.entity.DocUser;
import com.sxpcwlkj.docApi.entity.bo.DocOrderBo;
import com.sxpcwlkj.docApi.entity.bo.MyRequest;
import com.sxpcwlkj.docApi.entity.vo.DocOrderVo;
import com.sxpcwlkj.docApi.entity.vo.DocUserVo;
import com.sxpcwlkj.docApi.mapper.DocConfigMapper;
import com.sxpcwlkj.docApi.mapper.DocOrderMapper;
import com.sxpcwlkj.docApi.mapper.DocProductMapper;
import com.sxpcwlkj.docApi.service.DocOrderService;
import com.sxpcwlkj.docApi.service.DocUserService;
import com.sxpcwlkj.docApi.utils.DocBaseTool;
import com.sxpcwlkj.docApi.utils.DocR;
import com.sxpcwlkj.framework.utils.SignUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.wx.service.WxCodeService;
import com.sxpcwlkj.wx.service.WxOrderService;
import com.sxpcwlkj.wx.service.WxService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author shanpengnian
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/vpapi/meb")
public class DocUserController extends DocBaseTool {

    private final DocUserService docUserService;
    private final DocOrderMapper docOrderMapper;
    private final DocConfigMapper docConfigMapper;
    private final WxCodeService wxCodeService;
    private final DocProductMapper  docProductMapper;
    private final WxOrderService wxOrderService;
    private final WxService wxService;
    private final DocOrderService docOrderService;

    /**
     * 获取用户信息
     * @param request 请求
     * @param response 响应
     * @return 用户信息
     */
    @SaIgnore
    @PostMapping("/userinfo")
    public DocR<DocUserVo> userinfo(HttpServletRequest request, HttpServletResponse response){
        DocUserVo docUserVo = docUserService.selectVoById(getUserId(request));
        if(docUserVo==null){
            return DocR.error("99910","会话过期");
        }
       docOrderMapper.delete(new LambdaQueryWrapper<DocOrder>().eq(DocOrder::getUid,docUserVo.getUid())
           .eq(DocOrder::getStatus,0)
           .le(DocOrder::getCtime,DateUtil.getAddDate(new Date(),0,0,0,1,0,0,0))
       );
       List<DocOrderVo> orderVos= docOrderMapper.selectVoList(new LambdaQueryWrapper<DocOrder>()
            .eq(DocOrder::getUid,docUserVo.getUid())
            .eq(DocOrder::getStatus, 1)
            .orderByAsc(DocOrder::getCtime));
        //根据付款时间，计算累计的VIP天数

        Date expireTime=docUserVo.getCtime();
        for (DocOrderVo docOrderVo : orderVos) {
            //判断订单时间，是否大于 到期时间
            if(docOrderVo.getCtime().after(expireTime)){
                expireTime=docOrderVo.getCtime();
            }
            String day= docConfigMapper.selectByKey(docOrderVo.getProdId());
            if(day==null){
                day="0";
            }
            expireTime=DateUtil.getAddDate(expireTime,0,0, Integer.parseInt(day),0,0,0,0);
        }

        if(!new Date().after(expireTime)){
            docUserVo.setType("vip");
            docUserVo.setVip_date(expireTime);
        }else {
            docUserVo.setVip_date(DateUtil.getStrToDate("2025-01-01 00:00:00"));
            docUserVo.setType("usr");
        }
        CookieUtil.setCookie(response,"mss",getToken(docUserVo.getUid()),1000*60*60*24*7);
        return DocR.ok(docUserVo);
    }


    /**
     * 登录二维码
     * @return 二维码
     */
    @SaIgnore
    @PostMapping("/oauth-authorize")
    public DocR<Map<String,String>> oauthAuthorize(){
        Map<String,String> data= new HashMap<>();
        String state=RandomUtil.getRandomUUID();
        String codeUrl= wxCodeService.getCode(new WxCodeBo(state)
            .typeDocLogin()
            .expireTime(1000*60)
            .paramData(state));
        data.put("url",codeUrl);
        data.put("state",state);
        return DocR.ok(data);
    }

    /**
     * 登录二维码轮询
     * @param request 请求
     * @return 登录状态
     */
    @SaIgnore
    @PostMapping("/oauth-polling")
    public DocR<Map<String,String>> oauthPolling(@RequestBody MyRequest bo){
        String state = bo.getState();

        Map<String,String> data= new HashMap<>();
        data.put("status","0");
        if(state==null){
            return DocR.error("50001","state不能为空！");
        }
        //登录二维码
        WxCodeBo wxCodeBo= wxCodeService.getCodeState(new WxCodeBo(state).typeDocLogin());
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SUCCEED.getValue())){
            log.info(wxCodeBo.getOpenId());
            DocUser docUser= docUserService.bindingOpenId(wxCodeBo.getOpenId());
            data.put("status","1");
            data.put("token",getToken(docUser.getUid()));
        }
        return DocR.ok(data);
    }


    /**
     * 商品列表
     * @param request 请求
     * @return 商品列表
     */
    @SaIgnore
    @PostMapping("/product-list")
    public DocR<Map<String,Object>> productList(HttpServletRequest request){
        String uid= getUserId(request);
        List<DocProduct> docProducts= docProductMapper.selectList(new LambdaQueryWrapper<DocProduct>().eq(DocProduct::getStatus,1).orderByAsc(DocProduct::getSort));
        Map<String,Object> endData= new HashMap<>();
        List<Map<String,String>> data= new ArrayList<>();
        for (DocProduct docProduct : docProducts) {
            Map<String,String> map= new HashMap<>();
            map.put("prod_id",docProduct.getProdId());
            map.put("prod_name",docProduct.getProdName());
            map.put("unit_price",docProduct.getUnitPrice());
            map.put("mark_price",docProduct.getMarkPrice());
            map.put("intro",docProduct.getType());

            DocUserVo docUserVo= docUserService.selectVoById(uid);
            if(docUserVo==null){
                return DocR.error("99910","会话过期");
            }
            Map<String,Object> orderInfo= new HashMap<>();
            orderInfo.put("openId",docUserVo.getOpenId());
            orderInfo.put("orderNo",System.currentTimeMillis()+"");
            orderInfo.put("productTitle",docProduct.getProdName());
            orderInfo.put("productId",docProduct.getProdId());
            orderInfo.put("productType",docProduct.getType());
            orderInfo.put("payPrice",docProduct.getUnitPrice());
            orderInfo.put("ip",IPUtil.getIp(request));
            orderInfo.put("tradeType", TradeType.NATIVE.getTradeType());
            orderInfo.put("mchId","");
            orderInfo.put("expireTime",System.currentTimeMillis()+1000*60*3);
            orderInfo.put("notifyUrl","https://mmsadmin.cn/vpapi/meb/payNotify");
            R<Object> r= wxOrderService.createPay(orderInfo);
            map.put("buy_url",r.getData().toString());
            data.add(map);

            docOrderService.create(docUserVo,orderInfo);
        }
        endData.put("items",data);

        return DocR.ok(endData);
    }


    /**
     * 付款状态轮询
     * @param request 请求
     * @return 登录状态
     */
    @SaIgnore
    @PostMapping("/product-buy-qry")
    public DocR<Map<String,String>> productBuyQry(@RequestBody MyRequest bo,HttpServletRequest request){
        String prodId = bo.getProd_id();

        Map<String,String> data= new HashMap<>();
        data.put("status","0");
        if(prodId==null){
            return DocR.error("50001","prodId不能为空！");
        }
        data.put("status",docOrderService.selectPayState(prodId,getUserId(request)));
        return DocR.ok(data);
    }

    @SaIgnore
    @PostMapping("/notify")
    public String notify(HttpServletRequest req, HttpServletResponse resp, @RequestBody String body){
                WxPayOrderNotifyResult result = null;
        try {
            System.out.println("X=:" + body);
            result = wxService.getWxPayService(TradeType.NATIVE.getTradeType()).parseOrderNotifyResult(body,TradeType.NATIVE.getTradeType());
            // 结果正确 outTradeNo
            log.warn("交易单号:" + result.getTransactionId());
            log.warn("订单号:" + result.getOutTradeNo());
            log.warn("付款金额:" + BaseWxPayResult.fenToYuan(result.getTotalFee()));

            if ("SUCCESS".equals(result.getResultCode())) {
                log.warn("支付回调进来了:付款成功");
                // 更新订单状态
                // 更新库存
                // 发送通知
                // 发送邮件
                // 发送短信
                // 发送微信消息
                docOrderService.updateByOrderNo(result.getTransactionId());
                return "SUCCESS";
            } else {
                log.warn("支付回调进来了:付款失败");
            }
        } catch (Exception e) {
            log.error("商品支付报错了",e);
        }
        return null;
    }


    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @SaIgnore
    public String payNotify(HttpServletRequest request,@RequestBody String body) {
        String xmlMsg = body;
        log.info("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 注意此处签名方式需与统一下单的签名类型一致
        if (wxOrderService.verifyNotify(params)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 更新订单信息
                docOrderService.updateByOrderNo(params.get("out_trade_no"));
                // 发送通知等
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }



}
