package com.sxpcwlkj.store.service;

import com.sxpcwlkj.common.code.entity.ThreeQueryBo;
import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreProductCate;
import com.sxpcwlkj.store.entity.bo.StoreProductCateBo;
import com.sxpcwlkj.store.entity.export.StoreProductCateExport;
import com.sxpcwlkj.store.entity.vo.StoreProductCateVo;

import java.util.List;
import java.util.Set;

public interface StoreProductCateService extends BaseService<StoreProductCate, StoreProductCateVo, StoreProductCateBo> {

        /**
         * 商品分类列表
         * @param isAll true：全部数据 false：有效数据(status=0)
         * @param showLevel 显示级别，0：全部 1：一级 2：二级 3：三级
         * @param fId 父级ID
         * @return 商品分类数结构列表
         */
        List<StoreProductCateVo> queryTree(ThreeQueryBo bo);

        /**
         * 按照商品分类ID，查询下级所有商品分类
         *
         * @param id      商品分类ID
         * @param endList 最终的数据
         */
        void queryListSon(String id, List<StoreProductCateVo> endList);

        Boolean imports(Set<StoreProductCateExport> list);

        StoreProductCateVo selectByCateId(String cateId);

}
