package com.sxpcwlkj.bbs.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.bbs.entity.BbsLeve;
import com.sxpcwlkj.bbs.entity.bo.BbsLeveBo;
import com.sxpcwlkj.bbs.entity.vo.BbsLeveVo;
import com.sxpcwlkj.bbs.entity.export.BbsLeveExport;
import java.util.List;
import java.util.Set;

/**
 * 话题操作-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface BbsLeveService extends BaseService<BbsLeve, BbsLeveVo, BbsLeveBo> {
    /**
    * 导出话题操作
    * @param list 话题操作列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<BbsLeveExport> list);

    Boolean clickTopic(String id, int type, String loginId);
}
