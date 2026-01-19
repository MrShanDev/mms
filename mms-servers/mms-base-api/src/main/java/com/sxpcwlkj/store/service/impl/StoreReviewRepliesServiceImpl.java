package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreReviewReplies;
import com.sxpcwlkj.store.entity.bo.StoreReviewRepliesBo;
import com.sxpcwlkj.store.entity.export.StoreReviewRepliesExport;
import com.sxpcwlkj.store.entity.vo.StoreReviewRepliesVo;
import com.sxpcwlkj.store.mapper.StoreReviewRepliesMapper;
import com.sxpcwlkj.store.service.StoreReviewRepliesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 评论回复表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_review_replies")
@RequiredArgsConstructor
public class StoreReviewRepliesServiceImpl extends BaseServiceImpl<StoreReviewReplies, StoreReviewRepliesVo, StoreReviewRepliesBo> implements StoreReviewRepliesService {

   private final StoreReviewRepliesMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreReviewReplies, StoreReviewRepliesVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreReviewRepliesBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreReviewReplies obj = MapstructUtil.convert(bo, StoreReviewReplies.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("评论回复表,insert 操作失败", e);
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
            log.error("评论回复表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreReviewRepliesBo bo) {
        try {
            int row;
            StoreReviewReplies obj = MapstructUtil.convert(bo, StoreReviewReplies.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("评论回复表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreReviewRepliesVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreReviewRepliesVo> selectListVoPage(StoreReviewRepliesBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreReviewReplies> lqw = buildQueryWrapper(bo);
        Page<StoreReviewRepliesVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreReviewReplies> buildQueryWrapper(StoreReviewRepliesBo query){
        if(query==null){
            query=new StoreReviewRepliesBo();
        }
        LambdaQueryWrapper<StoreReviewReplies> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreReviewRepliesExport> list) {
        return true;
    }
}
