package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreAttrKeySpu;
import com.sxpcwlkj.store.entity.bo.StoreAttrKeySpuBo;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeySpuVo;
import com.sxpcwlkj.store.entity.export.StoreAttrKeySpuExport;

import java.util.Set;

/**
 * 商品规格项关系表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreAttrKeySpuService extends BaseService<StoreAttrKeySpu, StoreAttrKeySpuVo, StoreAttrKeySpuBo> {
    /**
    * 导出商品规格项关系表
    * @param list 商品规格项关系表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreAttrKeySpuExport> list);
}
