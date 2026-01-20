package com.sxpcwlkj.bbs.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.bbs.entity.BbsCate;
import com.sxpcwlkj.bbs.entity.bo.BbsCateBo;
import com.sxpcwlkj.bbs.entity.vo.BbsCateVo;
import com.sxpcwlkj.bbs.entity.export.BbsCateExport;
import com.sxpcwlkj.bbs.mapper.BbsCateMapper;
import com.sxpcwlkj.bbs.service.BbsCateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * 话题分类-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("bbs_cate")
@RequiredArgsConstructor
public class BbsCateServiceImpl extends BaseServiceImpl<BbsCate, BbsCateVo,BbsCateBo> implements BbsCateService {

   private final BbsCateMapper baseMapper;

    @Override
    public BaseMapperPlus<BbsCate, BbsCateVo> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<BbsCateVo> queryTree(boolean isAll,int showLevel) {
    List<BbsCateVo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<BbsCate>()
            .eq(!isAll,BbsCate::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(BbsCate::getSort));
        return formatTree(queryTrees, "0",showLevel,0);
    }

    @Override
    public void queryListSon(String id, List<BbsCateVo> endList) {
        BbsCateVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            endList.add(vo);
            queryListSon(vo.getFatherId(), endList);
        }
    }
    private List<BbsCateVo> formatTree(List<BbsCateVo> vos, String fid, int level,int currentLevel) {
        List<BbsCateVo> endList = new ArrayList<>();
        for (BbsCateVo s : vos) {
            if (fid.equals(s.getFatherId())) {
                if(level > currentLevel||level==0) {
                    List<BbsCateVo> vo = formatTree(vos, s.getId(),level,currentLevel+1);
                        s.setChildren(vo);
                        endList.add(s);
                    }
                }
        }
        return endList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(BbsCateBo bo) {
        try {
            int row;
            bo.setId(null);
            BbsCate obj = MapstructUtil.convert(bo, BbsCate.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("话题分类,insert 操作失败", e);
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
            log.error("话题分类,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(BbsCateBo bo) {
        try {
            int row;
            BbsCate obj = MapstructUtil.convert(bo, BbsCate.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("话题分类,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public BbsCateVo selectVoById(Serializable id) {
        BbsCateVo vo= this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
            getIds(end,vo.getId());
            Collections.reverse(end);
            vo.setIds(end.toArray(new String[]{}));
        return vo;

    }
    private void getIds(List<String> end, String id) {
        BbsCateVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getId());
            getIds(end, vo.getFatherId());
        }
    }
    @Override
    public TableDataInfo<BbsCateVo> selectListVoPage(BbsCateBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BbsCate> lqw = buildQueryWrapper(bo);
        Page<BbsCateVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<BbsCate> buildQueryWrapper(BbsCateBo query){
        if(query==null){
            query=new BbsCateBo();
        }
        LambdaQueryWrapper<BbsCate> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<BbsCateExport> list) {
        return true;
    }
}
