package com.sxpcwlkj.article.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.article.entity.StoreArticle;
import com.sxpcwlkj.article.entity.bo.StoreArticleBo;
import com.sxpcwlkj.article.entity.export.StoreArticleExport;
import com.sxpcwlkj.article.entity.vo.StoreArticleVo;

import java.util.Set;

/**
 * 店铺文章-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreArticleService extends BaseService<StoreArticle, StoreArticleVo, StoreArticleBo> {
    /**
    * 导出店铺文章
    * @param list 店铺文章列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreArticleExport> list);

    /**
     * 根据条件查询文章数量
     * @param bo 文章查询对象
     * @return 文章数量
     */
    Long count(StoreArticleBo bo);
}
