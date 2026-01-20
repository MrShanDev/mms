package com.sxpcwlkj.bbs.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.bbs.entity.BbsFiles;
import com.sxpcwlkj.bbs.entity.bo.BbsFilesBo;
import com.sxpcwlkj.bbs.entity.vo.BbsFilesVo;
import com.sxpcwlkj.bbs.entity.export.BbsFilesExport;
import java.util.List;
import java.util.Set;

/**
 * 话题附件-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface BbsFilesService extends BaseService<BbsFiles, BbsFilesVo, BbsFilesBo> {
    /**
    * 导出话题附件
    * @param list 话题附件列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<BbsFilesExport> list);
}
