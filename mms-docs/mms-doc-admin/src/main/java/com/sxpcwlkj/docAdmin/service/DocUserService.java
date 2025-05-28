package com.sxpcwlkj.docAdmin.service;

import com.sxpcwlkj.framework.sercice.BaseService;
import com.sxpcwlkj.docAdmin.entity.DocUser;
import com.sxpcwlkj.docAdmin.entity.bo.DocUserBo;
import com.sxpcwlkj.docAdmin.entity.vo.DocUserVo;
import com.sxpcwlkj.docAdmin.entity.export.DocUserExport;
import java.util.List;
import java.util.Set;

/**
 * 文档用户-接口
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface DocUserService extends BaseService<DocUser, DocUserVo, DocUserBo> {
    /**
    * 导出文档用户
    * @param list 文档用户列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<DocUserExport> list);
}
