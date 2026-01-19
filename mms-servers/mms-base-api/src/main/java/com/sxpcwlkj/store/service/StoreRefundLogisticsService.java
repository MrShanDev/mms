package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreRefundLogistics;
import com.sxpcwlkj.store.entity.bo.StoreRefundLogisticsBo;
import com.sxpcwlkj.store.entity.vo.StoreRefundLogisticsVo;
import com.sxpcwlkj.store.entity.export.StoreRefundLogisticsExport;

import java.util.Set;

/**
 * 退货物流信息表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreRefundLogisticsService extends BaseService<StoreRefundLogistics, StoreRefundLogisticsVo, StoreRefundLogisticsBo> {
    /**
    * 导出退货物流信息表
    * @param list 退货物流信息表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreRefundLogisticsExport> list);

    /**
     * 新增退货物流
     * @param bo bo
     * @param loginId 登录ID
     * @return true：成功 false ：失败
     */
    Boolean insertXml(StoreRefundLogisticsBo bo, String loginId);

    StoreRefundLogisticsVo selectVoByrefundApplyId(String id);
}
