package com.sxpcwlkj.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.bbs.entity.BbsAttention;
import com.sxpcwlkj.bbs.entity.BbsComment;
import com.sxpcwlkj.bbs.entity.BbsFiles;
import com.sxpcwlkj.bbs.entity.BbsLeve;
import com.sxpcwlkj.bbs.entity.vo.BbsFilesVo;
import com.sxpcwlkj.bbs.mapper.BbsAttentionMapper;
import com.sxpcwlkj.bbs.mapper.BbsCommentMapper;
import com.sxpcwlkj.bbs.mapper.BbsLeveMapper;
import com.sxpcwlkj.bbs.service.BbsFilesService;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.bbs.entity.BbsTopic;
import com.sxpcwlkj.bbs.entity.bo.BbsTopicBo;
import com.sxpcwlkj.bbs.entity.vo.BbsTopicVo;
import com.sxpcwlkj.bbs.entity.export.BbsTopicExport;
import com.sxpcwlkj.bbs.mapper.BbsTopicMapper;
import com.sxpcwlkj.bbs.service.BbsTopicService;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.member.service.StoreMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * 话题-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("bbs_topic")
@RequiredArgsConstructor
public class BbsTopicServiceImpl extends BaseServiceImpl<BbsTopic, BbsTopicVo,BbsTopicBo> implements BbsTopicService {

   private final BbsTopicMapper baseMapper;
   private final BbsFilesService bbsFilesService;
   private final StoreMemberService storeMemberService;
   private final BbsLeveMapper bbsLeveMapper;
   private final BbsCommentMapper bbsCommentMapper;
   private final BbsAttentionMapper bbsAttentionMapper;

    @Override
    public BaseMapperPlus<BbsTopic, BbsTopicVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(BbsTopicBo bo) {
        try {
            int row;
            bo.setId(null);
            BbsTopic obj = MapstructUtil.convert(bo, BbsTopic.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            if (bo.getFiles() != null && !bo.getFiles().isEmpty()){
                bo.getFiles().forEach(file -> {
                   file.setBbsId(bo.getId());
                   file.setStatus(1);
                   Boolean b= bbsFilesService.insert(file);
                });


            }
            return row > 0;
        } catch (Exception e) {
            log.error("话题,insert 操作失败", e);
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
            log.error("话题,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(BbsTopicBo bo) {
        try {
            int row;
            BbsTopic obj = MapstructUtil.convert(bo, BbsTopic.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("话题,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public BbsTopicVo selectVoById(Serializable id) {
        BbsTopicVo vo = this.getBaseMapper().selectVoById(id);
        fillTopicVo(vo);
        return vo;
    }
    @Override
    public TableDataInfo<BbsTopicVo> selectListVoPage(BbsTopicBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BbsTopic> lqw = buildQueryWrapper(bo);
        Page<BbsTopicVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        for (BbsTopicVo vo : page.getRecords()) {
            fillTopicVo(vo);
        }
        return TableDataInfo.build(page);
    }

    private void fillTopicVo(BbsTopicVo vo) {
        if (vo == null) {
            return;
        }
        StoreMemberVo memberVo = storeMemberService.selectVoById(vo.getMemberId());
        if (memberVo != null) {
            vo.setMemberNickName(memberVo.getNickname());
            vo.setMemberHeadImg(memberVo.getHeadPortrait());
        }
        vo.setFiles(bbsFilesService.selectVoListByLqw(new LambdaQueryWrapper<BbsFiles>()
            .eq(BbsFiles::getBbsId, vo.getId())
            .eq(BbsFiles::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByDesc(BbsFiles::getCreatedTime)
        ));
        if (vo.getFiles() != null && !vo.getFiles().isEmpty()) {
            BbsFilesVo filesVo = vo.getFiles().get(0);
            vo.setFileUrl(filesVo.getUrl());
            vo.setFileType(filesVo.getType());
        }

        Long num = bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
            .eq(BbsLeve::getBbsId, vo.getId())
            .eq(BbsLeve::getType, 1)
            .eq(BbsLeve::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()));
        vo.setLikeCount(num == null ? 0 : num);

        Long m = bbsCommentMapper.selectCount(new LambdaQueryWrapper<BbsComment>()
            .eq(BbsComment::getBbsId, vo.getId())
            .eq(BbsComment::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
        );
        vo.setCommentCount(m == null ? 0 : m);

        // 收藏数
        Long favoriteNum = bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
            .eq(BbsLeve::getBbsId, vo.getId())
            .eq(BbsLeve::getType, 3)
            .eq(BbsLeve::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()));
        vo.setFavoriteCount(favoriteNum == null ? 0 : favoriteNum);

        // 粉丝数
        Long attentionNum = bbsAttentionMapper.selectCount(new LambdaQueryWrapper<BbsAttention>()
            .eq(BbsAttention::getAttentionId, vo.getMemberId()));
        vo.setAttentionCount(attentionNum == null ? 0 : attentionNum);

        if (LoginObject.isLogin()) {
            String currentMemberId = LoginObject.getLoginId();
            // 是否点赞
            Long like = bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
                .eq(BbsLeve::getBbsId, vo.getId())
                .eq(BbsLeve::getType, 1)
                .eq(BbsLeve::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                .eq(BbsLeve::getMemberId, currentMemberId));
            vo.setLike(like != null && like > 0);

            // 是否收藏
            Long favorite = bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
                .eq(BbsLeve::getBbsId, vo.getId())
                .eq(BbsLeve::getType, 3)
                .eq(BbsLeve::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                .eq(BbsLeve::getMemberId, currentMemberId));
            vo.setFavorite(favorite != null && favorite > 0);

            // 是否关注
            Long attention = bbsAttentionMapper.selectCount(new LambdaQueryWrapper<BbsAttention>()
                .eq(BbsAttention::getMemberId, currentMemberId)
                .eq(BbsAttention::getAttentionId, vo.getMemberId()));
            vo.setAttention(attention != null && attention > 0);
        } else {
            vo.setLike(false);
            vo.setFavorite(false);
            vo.setAttention(false);
        }
    }

    private LambdaQueryWrapper<BbsTopic> buildQueryWrapper(BbsTopicBo query){
        if(query==null){
            query=new BbsTopicBo();
        }
        LambdaQueryWrapper<BbsTopic> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getMemberId()), BbsTopic::getMemberId, query.getMemberId());
        wrapper.eq(StringUtil.isNotEmpty(query.getCateId()), BbsTopic::getCateId, query.getCateId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<BbsTopicExport> list) {
        return true;
    }

    @Override
    public TableDataInfo<BbsTopicVo> selectListVoPageXml(String cateId, String keyWord, String memberId, List<String> attentionList, Double latitude, Double longitude, PageQuery pageQuery, List<String> topicIds) {
        Page<BbsTopicVo> page = baseMapper.selectVoPageXml(pageQuery.build(),cateId,keyWord,memberId,attentionList,latitude,longitude,topicIds);
        for (BbsTopicVo vo : page.getRecords()) {
            fillTopicVo(vo);
        }
        return TableDataInfo.build(page);
    }

    @Override
    public Boolean deleteByIdXml(String id, String loginId) {
        BbsTopicVo vo = this.getBaseMapper().selectVoById(id);
        if(vo==null){
            throw new MmsException("话题不存在");
        }
        if(vo.getMemberId().equals(loginId)){
            return this.getBaseMapper().deleteById(id)>0;
        }
        throw new MmsException("您无权删除该话题");
    }
}
