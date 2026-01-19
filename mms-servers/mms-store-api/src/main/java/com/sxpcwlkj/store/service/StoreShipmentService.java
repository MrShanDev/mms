package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreShipment;
import com.sxpcwlkj.store.entity.bo.StoreShipmentBo;
import com.sxpcwlkj.store.entity.export.StoreShipmentExport;
import com.sxpcwlkj.store.entity.vo.StoreShipmentVo;

import java.util.Set;

/**
 * 发货明细-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreShipmentService extends BaseService<StoreShipment, StoreShipmentVo, StoreShipmentBo> {
    /**
    * 导出发货明细
    * @param list 发货明细列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreShipmentExport> list);

    Boolean orderDelivery(StoreShipmentBo bo);

    StoreShipmentVo selectVoByOrderId(String orderId);
}
