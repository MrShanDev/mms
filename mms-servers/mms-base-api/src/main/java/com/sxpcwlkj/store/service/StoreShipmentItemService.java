package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreShipmentItem;
import com.sxpcwlkj.store.entity.bo.StoreShipmentItemBo;
import com.sxpcwlkj.store.entity.export.StoreShipmentItemExport;
import com.sxpcwlkj.store.entity.vo.StoreShipmentItemVo;

import java.util.List;
import java.util.Set;

/**
 * 发货商品明细表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreShipmentItemService extends BaseService<StoreShipmentItem, StoreShipmentItemVo, StoreShipmentItemBo> {
    /**
    * 导出发货商品明细表
    * @param list 发货商品明细表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreShipmentItemExport> list);

    /**
     * 订单发货
     * @param itemIds 发货商品明细表id
     * @param shipmentId 发货单id
     * @param shippedQuantity 发货数量
     * @param batchNo 批次号
     * @return true：成功 false ：失败
     */
    Boolean orderDelivery(List<String> itemIds,String shipmentId,Integer shippedQuantity,String batchNo);
}
