package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.store.entity.vo.StoreOrderSpuVo;
import lombok.Data;

@Data
public class OrderAfterSalesBo {
   // 订单编号
   private String  orderNo;
   // 商品ID
   private  String  itemId;
    //售后类别 RETURN_GOODS：退货退款，EXCHANGE_GOODS：换货，RETURN_MONEY：仅退款
   private  String  serviceType;
   // 数量
   private Integer  num;
   // 理由
   private String  reason;
   // 问题描述
   private String  problemDesc;
    // 图片
    private String  images;
   // 开户行
   private String  bankDepositName;
   // 开户名
   private String  bankAccountName;
   // 银行账号
   private String  bankAccountNumber;

   private StoreOrderSpuVo afterSalesSpu;

}
