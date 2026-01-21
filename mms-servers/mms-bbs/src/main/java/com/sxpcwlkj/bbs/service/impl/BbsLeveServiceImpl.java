package com.sxpcwlkj.bbs.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.bbs.entity.vo.BbsCommentVo;
import com.sxpcwlkj.bbs.entity.vo.BbsTopicVo;
import com.sxpcwlkj.bbs.service.BbsCommentService;
import com.sxpcwlkj.bbs.service.BbsTopicService;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.bbs.entity.BbsLeve;
import com.sxpcwlkj.bbs.entity.bo.BbsLeveBo;
import com.sxpcwlkj.bbs.entity.vo.BbsLeveVo;
import com.sxpcwlkj.bbs.entity.export.BbsLeveExport;
import com.sxpcwlkj.bbs.mapper.BbsLeveMapper;
import com.sxpcwlkj.bbs.service.BbsLeveService;
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
 * 话题操作-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("bbs_leve")
@RequiredArgsConstructor
public class BbsLeveServiceImpl extends BaseServiceImpl<BbsLeve, BbsLeveVo,BbsLeveBo> implements BbsLeveService {

   private final BbsLeveMapper baseMapper;
   private final BbsTopicService bbsTopicService;
   private final BbsLeveMapper bbsLeveMapper;
   private final BbsCommentService bbsCommentService;

    @Override
    public BaseMapperPlus<BbsLeve, BbsLeveVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(BbsLeveBo bo) {
        try {
            int row;
            bo.setId(null);
            BbsLeve obj = MapstructUtil.convert(bo, BbsLeve.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("话题操作,insert 操作失败", e);
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
            log.error("话题操作,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(BbsLeveBo bo) {
        try {
            int row;
            BbsLeve obj = MapstructUtil.convert(bo, BbsLeve.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("话题操作,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public BbsLeveVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<BbsLeveVo> selectListVoPage(BbsLeveBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BbsLeve> lqw = buildQueryWrapper(bo);
        Page<BbsLeveVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<BbsLeve> buildQueryWrapper(BbsLeveBo query){
        if(query==null){
            query=new BbsLeveBo();
        }
        LambdaQueryWrapper<BbsLeve> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getBbsId()), BbsLeve::getBbsId, query.getBbsId());
        wrapper.eq(StringUtil.isNotEmpty(query.getMemberId()), BbsLeve::getMemberId, query.getMemberId());
        wrapper.eq(StringUtil.isNotEmpty(query.getType()), BbsLeve::getType, query.getType());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<BbsLeveExport> list) {
        return true;
    }

    @Override
    public Boolean clickTopic(String id, int type, String loginId) {
        if(type==1 || type==3){
            // type=1 话题点赞, type=3 话题收藏
            BbsTopicVo vo=bbsTopicService.selectVoById(id);
            if(vo==null){
                throw  new MmsException("话题不存在");
            }
        }
        if(type==2){
            BbsCommentVo bbsCommentVo= bbsCommentService.selectVoById(id);
            if(bbsCommentVo==null){
                throw  new MmsException("评论不存在");
            }
        }
        BbsLeveVo leveVo= baseMapper.selectVoOne(new LambdaQueryWrapper<BbsLeve>()
            .eq(BbsLeve::getBbsId,id)
            .eq(BbsLeve::getMemberId,loginId)
            .eq(BbsLeve::getType,type)
        );
        if(leveVo!=null){
            return baseMapper.deleteById(leveVo.getId())>0;
        }else {
            BbsLeve leve=new BbsLeve();
            leve.setBbsId(id);
            leve.setMemberId(loginId);
            leve.setType(type);
            leve.setStatus(1);
            return baseMapper.insert(leve)>0;
        }
    }
}
