package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreCouponProduct;
import com.sxpcwlkj.store.entity.bo.StoreCouponProductBo;
import com.sxpcwlkj.store.entity.export.StoreCouponProductExport;
import com.sxpcwlkj.store.entity.vo.StoreCouponProductVo;

import java.util.Set;

/**
 * 优惠券适用商品关系表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreCouponProductService extends BaseService<StoreCouponProduct, StoreCouponProductVo, StoreCouponProductBo> {
    /**
    * 导出优惠券适用商品关系表
    * @param list 优惠券适用商品关系表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreCouponProductExport> list);
}
