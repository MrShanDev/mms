package com.sxpcwlkj.bbs.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.bbs.entity.BbsAttention;
import com.sxpcwlkj.bbs.entity.bo.BbsAttentionBo;
import com.sxpcwlkj.bbs.entity.vo.BbsAttentionVo;
import com.sxpcwlkj.bbs.entity.export.BbsAttentionExport;
import java.util.List;
import java.util.Set;

/**
 * 关注作者-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface BbsAttentionService extends BaseService<BbsAttention, BbsAttentionVo, BbsAttentionBo> {
    /**
    * 导出关注作者
    * @param list 关注作者列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<BbsAttentionExport> list);
    /**
    * 点击关注
    * @param mid mid
    * @param loginId loginId
    * @return true：成功 false ：失败
    */
    Boolean clickAttention(String mid, String loginId);
    /**
    * 根据会员ID查询关注列表
    * @param loginId loginId
    * @return 关注列表
    */
    List<String> selectAttentionListByMemberId(String loginId);
}
