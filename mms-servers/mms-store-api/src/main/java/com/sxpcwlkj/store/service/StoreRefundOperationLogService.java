package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreRefundOperationLog;
import com.sxpcwlkj.store.entity.bo.StoreRefundOperationLogBo;
import com.sxpcwlkj.store.entity.export.StoreRefundOperationLogExport;
import com.sxpcwlkj.store.entity.vo.StoreRefundOperationLogVo;

import java.util.Set;

/**
 * 退款日志-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreRefundOperationLogService extends BaseService<StoreRefundOperationLog, StoreRefundOperationLogVo, StoreRefundOperationLogBo> {
    /**
    * 导出退款日志
    * @param list 退款日志列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreRefundOperationLogExport> list);
}
