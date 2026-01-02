package com.sxpcwlkj.docAdmin.service;

import com.sxpcwlkj.docAdmin.entity.DocConfig;
import com.sxpcwlkj.docAdmin.entity.bo.DocConfigBo;
import com.sxpcwlkj.docAdmin.entity.export.DocConfigExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocConfigVo;
import com.sxpcwlkj.framework.sercice.BaseService;

import java.util.Set;

/**
 * 文档配置-接口
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface DocConfigService extends BaseService<DocConfig, DocConfigVo, DocConfigBo> {
    /**
    * 导出文档配置
    * @param list 文档配置列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<DocConfigExport> list);
}
