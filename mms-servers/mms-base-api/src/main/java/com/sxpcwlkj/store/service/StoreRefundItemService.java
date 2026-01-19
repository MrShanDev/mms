package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreRefundItem;
import com.sxpcwlkj.store.entity.bo.StoreRefundItemBo;
import com.sxpcwlkj.store.entity.vo.StoreRefundItemVo;
import com.sxpcwlkj.store.entity.export.StoreRefundItemExport;

import java.util.Set;

/**
 * 退款商品明细表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreRefundItemService extends BaseService<StoreRefundItem, StoreRefundItemVo, StoreRefundItemBo> {
    /**
    * 导出退款商品明细表
    * @param list 退款商品明细表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreRefundItemExport> list);
}
