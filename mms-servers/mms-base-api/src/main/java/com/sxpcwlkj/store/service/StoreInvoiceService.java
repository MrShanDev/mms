package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreInvoice;
import com.sxpcwlkj.store.entity.bo.StoreInvoiceBo;
import com.sxpcwlkj.store.entity.export.StoreInvoiceExport;
import com.sxpcwlkj.store.entity.vo.StoreInvoiceVo;

import java.util.Set;

/**
 * 订单发票表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreInvoiceService extends BaseService<StoreInvoice, StoreInvoiceVo, StoreInvoiceBo> {
    /**
    * 导出订单发票表
    * @param list 订单发票表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreInvoiceExport> list);
}
