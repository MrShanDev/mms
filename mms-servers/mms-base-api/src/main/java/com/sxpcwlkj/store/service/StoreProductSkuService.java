package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreProductSku;
import com.sxpcwlkj.store.entity.bo.StoreProductSkuBo;
import com.sxpcwlkj.store.entity.export.StoreProductSkuExport;
import com.sxpcwlkj.store.entity.vo.StoreProductSkuVo;

import java.util.Set;

/**
 * 商品存量价格-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreProductSkuService extends BaseService<StoreProductSku, StoreProductSkuVo, StoreProductSkuBo> {
    /**
    * 导出商品存量价格
    * @param list 商品存量价格列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreProductSkuExport> list);

    /**
     *  通过编码查询商品存量价格
     * @param code  编码
     * @return  StoreProductSkuVo
     */
    StoreProductSkuVo selectByCode(String code,String spuId);
}
