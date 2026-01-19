package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreAttrKey;
import com.sxpcwlkj.store.entity.bo.StoreAttrKeyBo;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeyVo;
import com.sxpcwlkj.store.entity.export.StoreAttrKeyExport;

import java.util.Set;

/**
 * 属性键表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreAttrKeyService extends BaseService<StoreAttrKey, StoreAttrKeyVo, StoreAttrKeyBo> {
    /**
    * 导出属性键表
    * @param list 属性键表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreAttrKeyExport> list);
}
