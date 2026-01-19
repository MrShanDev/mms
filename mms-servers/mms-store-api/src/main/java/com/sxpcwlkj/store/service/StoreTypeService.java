package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreType;
import com.sxpcwlkj.store.entity.bo.StoreTypeBo;
import com.sxpcwlkj.store.entity.vo.StoreTypeVo;
import com.sxpcwlkj.store.entity.export.StoreTypeExport;

import java.util.Set;

/**
 * 店铺类型-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreTypeService extends BaseService<StoreType, StoreTypeVo, StoreTypeBo> {
    /**
    * 导出店铺类型
    * @param list 店铺类型列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreTypeExport> list);
}
