package com.sxpcwlkj.bbs.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.bbs.entity.BbsTopic;
import com.sxpcwlkj.bbs.entity.bo.BbsTopicBo;
import com.sxpcwlkj.bbs.entity.vo.BbsTopicVo;
import com.sxpcwlkj.bbs.entity.export.BbsTopicExport;
import java.util.List;
import java.util.Set;

/**
 * 话题-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface BbsTopicService extends BaseService<BbsTopic, BbsTopicVo, BbsTopicBo> {
    /**
    * 导出话题
    * @param list 话题列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<BbsTopicExport> list);

    /**
     * 分页查询话题
     * @param cateId    分类id
     * @param keyWord   关键字
     * @param memberId  会员ID
     * @param pageQuery 分页查询
     * @return 分页对象
     */
    TableDataInfo<BbsTopicVo> selectListVoPageXml(String cateId, String keyWord, String memberId, PageQuery pageQuery);

    Boolean deleteByIdXml(String id, String loginId);
}
