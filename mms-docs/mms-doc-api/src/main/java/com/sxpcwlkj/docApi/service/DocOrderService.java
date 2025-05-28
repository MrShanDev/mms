package com.sxpcwlkj.docApi.service;

import com.sxpcwlkj.docApi.entity.DocOrder;
import com.sxpcwlkj.docApi.entity.bo.DocOrderBo;
import com.sxpcwlkj.docApi.entity.vo.DocOrderVo;
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

}
