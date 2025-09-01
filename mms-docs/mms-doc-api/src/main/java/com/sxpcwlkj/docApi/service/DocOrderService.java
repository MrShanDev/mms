package com.sxpcwlkj.docApi.service;

import com.sxpcwlkj.docApi.entity.DocOrder;
import com.sxpcwlkj.docApi.entity.bo.DocOrderBo;
import com.sxpcwlkj.docApi.entity.vo.DocOrderVo;
import com.sxpcwlkj.docApi.entity.vo.DocUserVo;
import com.sxpcwlkj.framework.sercice.BaseService;

import java.util.Map;
import java.util.Set;

/**
 * 文档订单-接口
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface DocOrderService extends BaseService<DocOrder, DocOrderVo, DocOrderBo> {

    /**
     * 创建订单
     * @param docUserVo 用户信息
     * @param orderInfo 订单信息
     * @return true：成功 false ：失败
     */
    Boolean create(DocUserVo docUserVo, Map<String, Object> orderInfo);

    /**
     * 查询支付状态
     * @param orderId 订单编号
     * @param uid 用户编号
     * @return 支付状态
     */
    String selectPayState(String orderId,String uid);

    /**
     * 更新订单状态
     * @param outTradeNo 订单编号
     * @param transactionId 支付平台流水号
     * @param mchId 商户号
     * @return true：成功 false ：失败
     */
    Boolean updateByOrderNo(String outTradeNo,String transactionId,String mchId);
}
