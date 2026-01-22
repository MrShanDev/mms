package com.sxpcwlkj.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.article.entity.StoreArticle;
import com.sxpcwlkj.article.entity.bo.StoreArticleBo;
import com.sxpcwlkj.article.entity.bo.StoreArticleBo;
import com.sxpcwlkj.article.entity.export.StoreArticleExport;
import com.sxpcwlkj.article.entity.vo.StoreArticleVo;
import com.sxpcwlkj.article.mapper.StoreArticleMapper;
import com.sxpcwlkj.article.service.StoreArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 店铺文章-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_article")
@RequiredArgsConstructor
public class StoreArticleServiceImpl extends BaseServiceImpl<StoreArticle, StoreArticleVo, StoreArticleBo> implements StoreArticleService {

   private final StoreArticleMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreArticle, StoreArticleVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreArticleBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreArticle obj = MapstructUtil.convert(bo, StoreArticle.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("店铺文章,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
        } catch (Exception e) {
            log.error("店铺文章,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreArticleBo bo) {
        try {
            int row;
            StoreArticle obj = MapstructUtil.convert(bo, StoreArticle.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("店铺文章,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreArticleVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreArticleVo> selectListVoPage(StoreArticleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreArticle> lqw = buildQueryWrapper(bo);
        Page<StoreArticleVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreArticle> buildQueryWrapper(StoreArticleBo query){
        if(query==null){
            query=new StoreArticleBo();
        }
        LambdaQueryWrapper<StoreArticle> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtil.isNotEmpty(query.getTitle()), StoreArticle::getTitle, query.getTitle());
        wrapper.eq(StringUtil.isNotEmpty(query.getArticleCateId()), StoreArticle::getArticleCateId, query.getArticleCateId());
        wrapper.eq(query.getStatus() != null, StoreArticle::getStatus, query.getStatus());
        wrapper.eq(query.getCreatedBy() != null, StoreArticle::getCreatedBy, query.getCreatedBy());
        wrapper.eq(StringUtil.isNotEmpty(query.getMemberId()), StoreArticle::getMemberId, query.getMemberId());
        wrapper.orderByDesc(StoreArticle::getSort, StoreArticle::getCreatedTime);
        return wrapper;
    }

    @Override
    public Long count(StoreArticleBo bo) {
        return baseMapper.selectCount(buildQueryWrapper(bo).eq(StoreArticle::getMemberId, LoginObject.getLoginId()));
    }

    @Override
    public Boolean imports(Set<StoreArticleExport> list) {
        return true;
    }
}
