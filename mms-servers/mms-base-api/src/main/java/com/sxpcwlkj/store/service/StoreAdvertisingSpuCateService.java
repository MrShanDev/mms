package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreAdvertisingSpuCate;
import com.sxpcwlkj.store.entity.bo.StoreAdvertisingSpuCateBo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingSpuCateVo;
import com.sxpcwlkj.store.entity.export.StoreAdvertisingSpuCateExport;

import java.util.Set;

/**
 * 广告商品分类组合表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreAdvertisingSpuCateService extends BaseService<StoreAdvertisingSpuCate, StoreAdvertisingSpuCateVo, StoreAdvertisingSpuCateBo> {
    /**
    * 导出广告商品分类组合表
    * @param list 广告商品分类组合表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreAdvertisingSpuCateExport> list);
}
