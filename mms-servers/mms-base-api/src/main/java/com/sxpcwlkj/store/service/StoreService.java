package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.Store;
import com.sxpcwlkj.store.entity.bo.StoreBo;
import com.sxpcwlkj.store.entity.export.StoreExport;
import com.sxpcwlkj.store.entity.vo.StoreVo;

import java.util.Set;

/**
 * 店铺-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreService extends BaseService<Store, StoreVo, StoreBo> {
    /**
    * 导出店铺
    * @param list 店铺列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreExport> list);
}
