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
import com.sxpcwlkj.bbs.entity.vo.BbsMsgVo;
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

    @Override
    public TableDataInfo<BbsMsgVo> selectReceivedLeveList(String memberId, PageQuery pageQuery) {
        // 1. 获取我的所有话题ID
        List<BbsTopic> myTopics = baseMapper.selectList(new LambdaQueryWrapper<BbsTopic>()
            .eq(BbsTopic::getMemberId, memberId)
            .select(BbsTopic::getId));
        if (myTopics.isEmpty()) {
            return TableDataInfo.build();
        }
        List<String> topicIds = myTopics.stream().map(BbsTopic::getId).toList();

        // 2. 分页查询这些话题收到的赞和收藏 (排除自己)
        Page<BbsLeve> page = bbsLeveMapper.selectPage(pageQuery.build(), new LambdaQueryWrapper<BbsLeve>()
            .in(BbsLeve::getBbsId, topicIds)
            .ne(BbsLeve::getMemberId, memberId)
            .in(BbsLeve::getType, List.of(1, 3))
            .eq(BbsLeve::getStatus, 1)
            .orderByDesc(BbsLeve::getCreatedTime));

        List<BbsMsgVo> voList = new ArrayList<>();
        for (BbsLeve leve : page.getRecords()) {
            BbsMsgVo msgVo = new BbsMsgVo();
            msgVo.setId(leve.getId());
            msgVo.setMemberId(leve.getMemberId());
            msgVo.setBbsId(leve.getBbsId());
            msgVo.setType(leve.getType());
            msgVo.setCreatedTime(leve.getCreatedTime());
            msgVo.setContent(leve.getType() == 1 ? "赞了你的帖子" : "收藏了你的帖子");

            // 填充用户信息
            StoreMemberVo actor = storeMemberService.selectVoById(leve.getMemberId());
            if (actor != null) {
                msgVo.setMemberNickName(actor.getNickname());
                msgVo.setMemberHeadImg(actor.getHeadPortrait());
            }

            // 填充帖子主图
            msgVo.setTopicMainImg(getTopicMainImg(leve.getBbsId()));
            voList.add(msgVo);
        }

        TableDataInfo<BbsMsgVo> result = TableDataInfo.build();
        result.setRows(voList);
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public TableDataInfo<BbsMsgVo> selectFollowingList(String memberId, PageQuery pageQuery) {
        // 1. 分页查询我关注的人
        Page<BbsAttention> page = bbsAttentionMapper.selectPage(pageQuery.build(), new LambdaQueryWrapper<BbsAttention>()
            .eq(BbsAttention::getMemberId, memberId)
            .orderByDesc(BbsAttention::getCreatedTime));

        List<BbsMsgVo> voList = new ArrayList<>();
        for (BbsAttention attention : page.getRecords()) {
            BbsMsgVo msgVo = new BbsMsgVo();
            msgVo.setId(attention.getId());
            msgVo.setMemberId(attention.getAttentionId());
            msgVo.setType(5); // 关注
            msgVo.setCreatedTime(attention.getCreatedTime());

            // 填充用户信息
            StoreMemberVo actor = storeMemberService.selectVoById(attention.getAttentionId());
            if (actor != null) {
                msgVo.setMemberNickName(actor.getNickname());
                msgVo.setMemberHeadImg(actor.getHeadPortrait());
            }

            // 判断回关注状态
            Long count = bbsAttentionMapper.selectCount(new LambdaQueryWrapper<BbsAttention>()
                .eq(BbsAttention::getMemberId, attention.getAttentionId())
                .eq(BbsAttention::getAttentionId, memberId));
            msgVo.setMutualAttention(count != null && count > 0);

            // 获取该用户最新帖子的主图
            BbsTopic latestTopic = baseMapper.selectOne(new LambdaQueryWrapper<BbsTopic>()
                .eq(BbsTopic::getMemberId, attention.getAttentionId())
                .orderByDesc(BbsTopic::getCreatedTime)
                .last("LIMIT 1"));
            if (latestTopic != null) {
                msgVo.setBbsId(latestTopic.getId());
                msgVo.setTopicMainImg(getTopicMainImg(latestTopic.getId()));
            }

            voList.add(msgVo);
        }

        TableDataInfo<BbsMsgVo> result = TableDataInfo.build();
        result.setRows(voList);
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public TableDataInfo<BbsMsgVo> selectAtMeCommentList(String memberId, PageQuery pageQuery) {
        // 1. 获取我的所有话题ID
        List<BbsTopic> myTopics = baseMapper.selectList(new LambdaQueryWrapper<BbsTopic>()
            .eq(BbsTopic::getMemberId, memberId)
            .select(BbsTopic::getId));
        List<String> topicIds = myTopics.stream().map(BbsTopic::getId).toList();

        // 2. 获取我发表的所有评论ID (为了查回复)
        List<BbsComment> myComments = bbsCommentMapper.selectList(new LambdaQueryWrapper<BbsComment>()
            .eq(BbsComment::getMemberId, memberId)
            .select(BbsComment::getId));
        List<String> myCommentIds = myComments.stream().map(BbsComment::getId).toList();

        // 3. 分页查询：
        // (a) 评论我的话题的 (排除自己)
        // (b) 回复我的评论的 (排除自己)
        LambdaQueryWrapper<BbsComment> lqw = new LambdaQueryWrapper<BbsComment>();
        lqw.and(wrapper -> {
            if (!topicIds.isEmpty()) {
                wrapper.in(BbsComment::getBbsId, topicIds);
            }
            if (!myCommentIds.isEmpty()) {
                wrapper.or().in(BbsComment::getFatherId, myCommentIds);
            }
        });
        lqw.ne(BbsComment::getMemberId, memberId)
           .eq(BbsComment::getStatus, 1)
           .orderByDesc(BbsComment::getCreatedTime);

        // 如果既没有话题也没有评论，直接返回空
        if (topicIds.isEmpty() && myCommentIds.isEmpty()) {
            return TableDataInfo.build();
        }

        Page<BbsComment> page = bbsCommentMapper.selectPage(pageQuery.build(), lqw);

        List<BbsMsgVo> voList = new ArrayList<>();
        for (BbsComment comment : page.getRecords()) {
            BbsMsgVo msgVo = new BbsMsgVo();
            msgVo.setId(comment.getId());
            msgVo.setMemberId(comment.getMemberId());
            msgVo.setBbsId(comment.getBbsId());
            msgVo.setContent(comment.getComment());
            msgVo.setType(4); // 评论
            msgVo.setCreatedTime(comment.getCreatedTime());

            // 填充用户信息
            StoreMemberVo actor = storeMemberService.selectVoById(comment.getMemberId());
            if (actor != null) {
                msgVo.setMemberNickName(actor.getNickname());
                msgVo.setMemberHeadImg(actor.getHeadPortrait());
            }

            // 填充帖子主图
            msgVo.setTopicMainImg(getTopicMainImg(comment.getBbsId()));
            voList.add(msgVo);
        }

        TableDataInfo<BbsMsgVo> result = TableDataInfo.build();
        result.setRows(voList);
        result.setTotal(page.getTotal());
        return result;
    }

    private String getTopicMainImg(String bbsId) {
        List<BbsFilesVo> files = bbsFilesService.selectVoListByLqw(new LambdaQueryWrapper<BbsFiles>()
            .eq(BbsFiles::getBbsId, bbsId)
            .eq(BbsFiles::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByDesc(BbsFiles::getCreatedTime));
        if (files != null && !files.isEmpty()) {
            return files.get(0).getUrl();
        }
        return null;
    }
}
