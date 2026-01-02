package com.sxpcwlkj.docAdmin.service;

import com.sxpcwlkj.docAdmin.entity.DocOrder;
import com.sxpcwlkj.docAdmin.entity.bo.DocOrderBo;
import com.sxpcwlkj.docAdmin.entity.export.DocOrderExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocOrderVo;
import com.sxpcwlkj.framework.sercice.BaseService;

import java.util.Set;

/**
 * 文档订单-接口
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface DocOrderService extends BaseService<DocOrder, DocOrderVo, DocOrderBo> {
    /**
    * 导出文档订单
    * @param list 文档订单列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<DocOrderExport> list);
}
