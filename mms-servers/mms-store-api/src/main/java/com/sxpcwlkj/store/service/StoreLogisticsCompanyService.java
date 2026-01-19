package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreLogisticsCompany;
import com.sxpcwlkj.store.entity.bo.StoreLogisticsCompanyBo;
import com.sxpcwlkj.store.entity.vo.StoreLogisticsCompanyVo;
import com.sxpcwlkj.store.entity.export.StoreLogisticsCompanyExport;

import java.util.Set;

/**
 * 快递公司名称-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreLogisticsCompanyService extends BaseService<StoreLogisticsCompany, StoreLogisticsCompanyVo, StoreLogisticsCompanyBo> {
    /**
    * 导出快递公司名称
    * @param list 快递公司名称列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreLogisticsCompanyExport> list);
}
