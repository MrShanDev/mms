package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreAttrValueSpu;
import com.sxpcwlkj.store.entity.bo.StoreAttrValueSpuBo;
import com.sxpcwlkj.store.entity.export.StoreAttrValueSpuExport;
import com.sxpcwlkj.store.entity.vo.StoreAttrValueSpuVo;

import java.util.Set;

/**
 * 商品规格值关系表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreAttrValueSpuService extends BaseService<StoreAttrValueSpu, StoreAttrValueSpuVo, StoreAttrValueSpuBo> {
    /**
    * 导出商品规格值关系表
    * @param list 商品规格值关系表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreAttrValueSpuExport> list);
}
