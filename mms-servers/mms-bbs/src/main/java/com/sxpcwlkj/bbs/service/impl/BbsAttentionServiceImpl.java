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
import com.sxpcwlkj.bbs.entity.BbsAttention;
import com.sxpcwlkj.bbs.entity.bo.BbsAttentionBo;
import com.sxpcwlkj.bbs.entity.vo.BbsAttentionVo;
import com.sxpcwlkj.bbs.entity.export.BbsAttentionExport;
import com.sxpcwlkj.bbs.mapper.BbsAttentionMapper;
import com.sxpcwlkj.bbs.service.BbsAttentionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
/**
 * 关注作者-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("bbs_attention")
@RequiredArgsConstructor
public class BbsAttentionServiceImpl extends BaseServiceImpl<BbsAttention, BbsAttentionVo,BbsAttentionBo> implements BbsAttentionService {

   private final BbsAttentionMapper baseMapper;

    @Override
    public BaseMapperPlus<BbsAttention, BbsAttentionVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(BbsAttentionBo bo) {
        try {
            int row;
            bo.setId(null);
            BbsAttention obj = MapstructUtil.convert(bo, BbsAttention.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("关注作者,insert 操作失败", e);
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
            log.error("关注作者,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(BbsAttentionBo bo) {
        try {
            int row;
            BbsAttention obj = MapstructUtil.convert(bo, BbsAttention.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("关注作者,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public BbsAttentionVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<BbsAttentionVo> selectListVoPage(BbsAttentionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BbsAttention> lqw = buildQueryWrapper(bo);
        Page<BbsAttentionVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<BbsAttention> buildQueryWrapper(BbsAttentionBo query){
        if(query==null){
            query=new BbsAttentionBo();
        }
        LambdaQueryWrapper<BbsAttention> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getMemberId()), BbsAttention::getMemberId, query.getMemberId());
        wrapper.eq(StringUtil.isNotEmpty(query.getAttentionId()), BbsAttention::getAttentionId, query.getAttentionId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<BbsAttentionExport> list) {
        return true;
    }

    @Override
    public Boolean clickAttention(String mid, String loginId) {
      BbsAttention bbsAttention=  baseMapper.selectOne(new LambdaQueryWrapper<BbsAttention>()
            .eq(BbsAttention::getMemberId, loginId)
            .eq(BbsAttention::getAttentionId, mid).last("LIMIT 1"));
      if (bbsAttention == null) {
          // 关注
          bbsAttention = new BbsAttention();
          bbsAttention.setMemberId(loginId);
          bbsAttention.setAttentionId(mid);
          return baseMapper.insert(bbsAttention) > 0;
      } else {
          // 取消关注
          return baseMapper.delete(new LambdaQueryWrapper<BbsAttention>()
              .eq(BbsAttention::getMemberId, loginId)
              .eq(BbsAttention::getAttentionId, mid)) > 0;
      }
    }

    @Override
    public List<String> selectAttentionListByMemberId(String loginId) {
       List<BbsAttentionVo> list= baseMapper.selectVoList(new LambdaQueryWrapper<BbsAttention>()
            .eq(BbsAttention::getMemberId, loginId)
            .select(BbsAttention::getAttentionId));
       if (list != null && !list.isEmpty()){
           return list.stream().map(BbsAttentionVo::getAttentionId).toList();
       }
       return Collections.emptyList();
    }
}
