package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreShipmentSender;
import com.sxpcwlkj.store.entity.bo.StoreShipmentSenderBo;
import com.sxpcwlkj.store.entity.export.StoreShipmentSenderExport;
import com.sxpcwlkj.store.entity.vo.StoreShipmentSenderVo;

import java.util.Set;

/**
 * 发货人信息-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreShipmentSenderService extends BaseService<StoreShipmentSender, StoreShipmentSenderVo, StoreShipmentSenderBo> {
    /**
    * 导出发货人信息
    * @param list 发货人信息列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreShipmentSenderExport> list);
}
