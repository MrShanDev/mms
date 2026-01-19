package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreOrderItem;
import com.sxpcwlkj.store.entity.bo.StoreOrderItemBo;
import com.sxpcwlkj.store.entity.vo.StoreOrderItemVo;
import com.sxpcwlkj.store.entity.export.StoreOrderItemExport;
import com.sxpcwlkj.store.entity.vo.StoreOrderSpuVo;

import java.util.List;
import java.util.Set;

/**
 * 订单商品明细表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreOrderItemService extends BaseService<StoreOrderItem, StoreOrderItemVo, StoreOrderItemBo> {
    /**
    * 导出订单商品明细表
    * @param list 订单商品明细表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreOrderItemExport> list);

    /**
     * 根据订单编号获取订单商品明细表
     * @param orderId 订单ID
     * @return List<StoreOrderItemVo>
     */
    List<StoreOrderSpuVo> getStoreOrderSpuVoList(String orderId);
}
