package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreReview;
import com.sxpcwlkj.store.entity.bo.StoreReviewBo;
import com.sxpcwlkj.store.entity.export.StoreReviewExport;
import com.sxpcwlkj.store.entity.vo.StoreReviewVo;

import java.util.Set;

/**
 * 商品评论表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreReviewService extends BaseService<StoreReview, StoreReviewVo, StoreReviewBo> {
    /**
    * 导出商品评论表
    * @param list 商品评论表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreReviewExport> list);

    /**
     * 新增商品评论表
     * @param bo 商品评论表
     * @return true：成功 false ：失败
     */
    Boolean addReview(StoreReviewBo bo);
}
