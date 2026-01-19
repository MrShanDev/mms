package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreCoupon;
import com.sxpcwlkj.store.entity.bo.StoreCouponBo;
import com.sxpcwlkj.store.entity.vo.StoreCouponVo;
import com.sxpcwlkj.store.entity.export.StoreCouponExport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 优惠券管理-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreCouponService extends BaseService<StoreCoupon, StoreCouponVo, StoreCouponBo> {
    /**
    * 导出优惠券管理
    * @param list 优惠券管理列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreCouponExport> list);

    /**
     * 验证优惠券是否可用
     * @param couponId 优惠券ID
     * @param totalAmount 订单总金额
     * @param productIds 商品ID列表（用于验证指定商品优惠券）
     * @return 优惠券信息
     */
    StoreCouponVo validateCoupon(String couponId, BigDecimal totalAmount, List<String> productIds);

    /**
     * 使用优惠券（更新已使用次数）
     * @param couponId 优惠券ID
     * @return true：成功 false：失败
     */
    Boolean useCoupon(String couponId);
}
