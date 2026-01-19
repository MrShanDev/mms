package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreOrderStatusLog;
import com.sxpcwlkj.store.entity.bo.StoreOrderStatusLogBo;
import com.sxpcwlkj.store.entity.vo.StoreOrderStatusLogVo;
import com.sxpcwlkj.store.entity.export.StoreOrderStatusLogExport;

import java.util.Set;

/**
 * 订单状态流水表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreOrderStatusLogService extends BaseService<StoreOrderStatusLog, StoreOrderStatusLogVo, StoreOrderStatusLogBo> {
    /**
    * 导出订单状态流水表
    * @param list 订单状态流水表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreOrderStatusLogExport> list);

    /**
     * 查询订单售后前最后一次状态
     * @param orderId 订单ID
     * @param orderState 订单状态
     * @return 订单状态
     */
    StoreOrderStatusLogVo selectLastStatus(String orderId, Integer orderState);
}
