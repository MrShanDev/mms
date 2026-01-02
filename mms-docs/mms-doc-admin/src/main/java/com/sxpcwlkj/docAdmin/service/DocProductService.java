package com.sxpcwlkj.docAdmin.service;

import com.sxpcwlkj.docAdmin.entity.DocProduct;
import com.sxpcwlkj.docAdmin.entity.bo.DocProductBo;
import com.sxpcwlkj.docAdmin.entity.export.DocProductExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocProductVo;
import com.sxpcwlkj.framework.sercice.BaseService;

import java.util.Set;

/**
 * 文档商品-接口
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface DocProductService extends BaseService<DocProduct, DocProductVo, DocProductBo> {
    /**
    * 导出文档商品
    * @param list 文档商品列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<DocProductExport> list);
}
