package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreProductBrand;
import com.sxpcwlkj.store.entity.bo.StoreProductBrandBo;
import com.sxpcwlkj.store.entity.export.StoreProductBrandExport;
import com.sxpcwlkj.store.entity.vo.StoreProductBrandVo;

import java.util.Set;

/**
 * 商品品牌-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreProductBrandService extends BaseService<StoreProductBrand, StoreProductBrandVo, StoreProductBrandBo> {
    /**
    * 导出商品品牌
    * @param list 商品品牌列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreProductBrandExport> list);
}
