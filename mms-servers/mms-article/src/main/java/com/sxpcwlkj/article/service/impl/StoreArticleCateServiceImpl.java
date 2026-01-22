package com.sxpcwlkj.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.article.entity.StoreArticleCate;
import com.sxpcwlkj.article.entity.bo.StoreArticleCateBo;
import com.sxpcwlkj.article.entity.export.StoreArticleCateExport;
import com.sxpcwlkj.article.entity.vo.StoreArticleCateVo;
import com.sxpcwlkj.article.mapper.StoreArticleCateMapper;
import com.sxpcwlkj.article.service.StoreArticleCateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
/**
 * 店铺文章分类-接口实现
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_article_cate")
@RequiredArgsConstructor
public class StoreArticleCateServiceImpl extends BaseServiceImpl<StoreArticleCate, StoreArticleCateVo, StoreArticleCateBo> implements StoreArticleCateService {

    private final StoreArticleCateMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreArticleCate, StoreArticleCateVo> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<StoreArticleCateVo> queryTree(boolean isAll,int showLevel) {
        List<StoreArticleCateVo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<StoreArticleCate>()
            .eq(!isAll,StoreArticleCate::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(StoreArticleCate::getSort));
        return formatTree(queryTrees, "0",showLevel,0);
    }

    @Override
    public void queryListSon(String id, List<StoreArticleCateVo> endList) {
        StoreArticleCateVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            endList.add(vo);
            queryListSon(vo.getParentId(), endList);
        }
    }
    private List<StoreArticleCateVo> formatTree(List<StoreArticleCateVo> vos, String fid, int level,int currentLevel) {
        List<StoreArticleCateVo> endList = new ArrayList<>();
        for (StoreArticleCateVo s : vos) {
            if (fid.equals(s.getParentId())) {
                if(level > currentLevel||level==0) {
                    List<StoreArticleCateVo> vo = formatTree(vos, s.getId(),level,currentLevel+1);
                    s.setChildren(vo);
                    endList.add(s);
                }
            }
        }
        return endList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreArticleCateBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreArticleCate obj = MapstructUtil.convert(bo, StoreArticleCate.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("店铺文章分类,insert 操作失败", e);
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
            log.error("店铺文章分类,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreArticleCateBo bo) {
        try {
            int row;
            StoreArticleCate obj = MapstructUtil.convert(bo, StoreArticleCate.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("店铺文章分类,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreArticleCateVo selectVoById(Serializable id) {
        StoreArticleCateVo vo= this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
        getIds(end,vo.getId());
        Collections.reverse(end);
        vo.setIds(end.toArray(new String[]{}));
        return vo;

    }
    private void getIds(List<String> end, String id) {
        StoreArticleCateVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getId());
            getIds(end, vo.getParentId());
        }
    }
    @Override
    public TableDataInfo<StoreArticleCateVo> selectListVoPage(StoreArticleCateBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreArticleCate> lqw = buildQueryWrapper(bo);
        Page<StoreArticleCateVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreArticleCate> buildQueryWrapper(StoreArticleCateBo query){
        if(query==null){
            query=new StoreArticleCateBo();
        }
        LambdaQueryWrapper<StoreArticleCate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getId()), StoreArticleCate::getId, query.getId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreArticleCateExport> list) {
        return true;
    }
}
