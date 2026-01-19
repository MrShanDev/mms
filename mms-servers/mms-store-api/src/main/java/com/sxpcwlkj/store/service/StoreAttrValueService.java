package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreAttrValue;
import com.sxpcwlkj.store.entity.bo.StoreAttrValueBo;
import com.sxpcwlkj.store.entity.vo.StoreAttrValueVo;
import com.sxpcwlkj.store.entity.export.StoreAttrValueExport;

import java.util.Set;

/**
 * 属性值表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreAttrValueService extends BaseService<StoreAttrValue, StoreAttrValueVo, StoreAttrValueBo> {
    /**
    * 导出属性值表
    * @param list 属性值表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreAttrValueExport> list);
}
