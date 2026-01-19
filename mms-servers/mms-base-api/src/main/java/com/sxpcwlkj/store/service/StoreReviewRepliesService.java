package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreReviewReplies;
import com.sxpcwlkj.store.entity.bo.StoreReviewRepliesBo;
import com.sxpcwlkj.store.entity.vo.StoreReviewRepliesVo;
import com.sxpcwlkj.store.entity.export.StoreReviewRepliesExport;

import java.util.Set;

/**
 * 评论回复表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreReviewRepliesService extends BaseService<StoreReviewReplies, StoreReviewRepliesVo, StoreReviewRepliesBo> {
    /**
    * 导出评论回复表
    * @param list 评论回复表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreReviewRepliesExport> list);
}
