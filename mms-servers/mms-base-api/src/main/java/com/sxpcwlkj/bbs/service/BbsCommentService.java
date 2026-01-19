package com.sxpcwlkj.bbs.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.bbs.entity.BbsComment;
import com.sxpcwlkj.bbs.entity.bo.BbsCommentBo;
import com.sxpcwlkj.bbs.entity.vo.BbsCommentVo;
import com.sxpcwlkj.bbs.entity.export.BbsCommentExport;
import java.util.List;
import java.util.Set;

/**
 * 话题评论-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface BbsCommentService extends BaseService<BbsComment, BbsCommentVo, BbsCommentBo> {
    /**
    * 话题评论列表
    * @param isAll true：全部数据 false：有效数据(status=0)
    * @param showLevel 显示级别，0：全部 1：一级 2：二级 3：三级
    * @return 话题评论数结构列表
    */
    List<BbsCommentVo> queryTree(boolean isAll,int showLevel);

    /**
    * 按照话题评论ID，查询下级所有话题评论
    *
    * @param id      话题评论ID
    * @param endList 最终的数据
    */
    void queryListSon(String id, List<BbsCommentVo> endList);
    /**
    * 导出话题评论
    * @param list 话题评论列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<BbsCommentExport> list);

    Boolean deleteByIdXml(String id, String loginId);
}
