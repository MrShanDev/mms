package com.sxpcwlkj.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.bbs.entity.BbsLeve;
import com.sxpcwlkj.bbs.mapper.BbsLeveMapper;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.bbs.entity.BbsComment;
import com.sxpcwlkj.bbs.entity.bo.BbsCommentBo;
import com.sxpcwlkj.bbs.entity.vo.BbsCommentVo;
import com.sxpcwlkj.bbs.entity.export.BbsCommentExport;
import com.sxpcwlkj.bbs.mapper.BbsCommentMapper;
import com.sxpcwlkj.bbs.service.BbsCommentService;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.member.mapper.StoreMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.*;

/**
 * 话题评论-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("bbs_comment")
@RequiredArgsConstructor
public class BbsCommentServiceImpl extends BaseServiceImpl<BbsComment, BbsCommentVo,BbsCommentBo> implements BbsCommentService {

   private final BbsCommentMapper baseMapper;
   private final StoreMemberMapper storeMemberMapper;
   private final BbsLeveMapper bbsLeveMapper;

    @Override
    public BaseMapperPlus<BbsComment, BbsCommentVo> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<BbsCommentVo> queryTree(boolean isAll,int showLevel) {
    List<BbsCommentVo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<BbsComment>()
            .eq(!isAll,BbsComment::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(BbsComment::getSort));
        return formatTree(queryTrees, "0",showLevel,0);
    }

    @Override
    public void queryListSon(String id, List<BbsCommentVo> endList) {
        BbsCommentVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            endList.add(vo);
            queryListSon(vo.getFatherId(), endList);
        }
    }
    private List<BbsCommentVo> formatTree(List<BbsCommentVo> vos, String fid, int level,int currentLevel) {
        List<BbsCommentVo> endList = new ArrayList<>();
        for (BbsCommentVo s : vos) {
            if (fid.equals(s.getFatherId())) {
                if(level > currentLevel||level==0) {
                    List<BbsCommentVo> vo = formatTree(vos, s.getId(),level,currentLevel+1);
                        s.setChildren(vo);
                        endList.add(s);
                    }
                }
        }
        return endList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(BbsCommentBo bo) {
        try {
            int row;
            bo.setId(null);


            if(StringUtil.isNotEmpty(bo.getFatherId())){
                BbsCommentVo father = baseMapper.selectVoById(bo.getFatherId());
                if(father==null){
                    throw new MmsException("父级话题不存在");
                }
                if(!Objects.equals(father.getBbsId(), bo.getBbsId())){
                    throw new MmsException("父级话题与当前话题不一致");
                }
                if (father.getFatherId()!=null){
                    log.info("父级话题的父级话题id为{}",father.getFatherId());
                    bo.setFatherId(father.getFatherId());
                    bo.setLevel(father.getLevel()+1);
                    bo.setRemark(bo.getFatherId());
                }else {
                    bo.setLevel(2);
                }
            }
            BbsComment obj = MapstructUtil.convert(bo, BbsComment.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("话题评论,insert 操作失败", e);
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
            log.error("话题评论,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(BbsCommentBo bo) {
        try {
            int row;
            BbsComment obj = MapstructUtil.convert(bo, BbsComment.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("话题评论,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public BbsCommentVo selectVoById(Serializable id) {
        BbsCommentVo vo= this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
            getIds(end,vo.getId());
            Collections.reverse(end);
            vo.setIds(end.toArray(new String[]{}));
        return vo;

    }
    private void getIds(List<String> end, String id) {
        BbsCommentVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getId());
            getIds(end, vo.getFatherId());
        }
    }
    @Override
    public TableDataInfo<BbsCommentVo> selectListVoPage(BbsCommentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BbsComment> lqw = buildQueryWrapper(bo);
        Page<BbsCommentVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        for(BbsCommentVo vo:page.getRecords()){
          StoreMemberVo memberVo=  storeMemberMapper.selectVoById(vo.getMemberId());
          if(memberVo!=null){
              vo.setMemberNickName(memberVo.getNickname());
              vo.setMemberHeadImg(memberVo.getHeadPortrait());
          }
          Long num=bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
              .eq(BbsLeve::getBbsId,vo.getId())
              .eq(BbsLeve::getStatus,SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
              .eq(BbsLeve::getType,2)
          );
          vo.setLikeCount(num==null?0:num);

            if(LoginObject.isLogin()){
                Long likeCount=  bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
                    .eq(BbsLeve::getBbsId,vo.getId())
                    .eq(BbsLeve::getStatus,SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                    .eq(BbsLeve::getType,2)
                    .eq(BbsLeve::getMemberId,LoginObject.getLoginId()));
                vo.setLike(likeCount != null && likeCount > 0);
            }else {
                vo.setLike(false);
            }

          List<BbsCommentVo> sonList=  baseMapper.selectVoList(new LambdaQueryWrapper<BbsComment>()
              .eq(BbsComment::getStatus,SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
              .eq(BbsComment::getFatherId,vo.getId()));
          for(BbsCommentVo son:sonList){
              StoreMemberVo memberVoSon=  storeMemberMapper.selectVoById(son.getMemberId());
              if(memberVoSon!=null){
                  son.setMemberNickName(memberVoSon.getNickname());
                  son.setMemberHeadImg(memberVoSon.getHeadPortrait());
              }
              Long count=bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
                  .eq(BbsLeve::getBbsId,son.getId())
                  .eq(BbsLeve::getStatus,SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                  .eq(BbsLeve::getType,2)
              );
              son.setLikeCount(count==null?0:count);

              if(LoginObject.isLogin()){
                  Long likeCount=  bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
                          .eq(BbsLeve::getBbsId,son.getId())
                          .eq(BbsLeve::getStatus,SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                          .eq(BbsLeve::getType,2)
                          .eq(BbsLeve::getMemberId,LoginObject.getLoginId()));
                  son.setLike(likeCount != null && likeCount > 0);
              }else {
                  son.setLike(false);
              }

          }
          vo.setChildren(sonList);
        }
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<BbsComment> buildQueryWrapper(BbsCommentBo query){
        if(query==null){
            query=new BbsCommentBo();
        }
        LambdaQueryWrapper<BbsComment> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BbsComment::getLevel, 1);
        wrapper.eq(StringUtil.isNotEmpty(query.getBbsId()), BbsComment::getBbsId, query.getBbsId());
        wrapper.eq(StringUtil.isNotEmpty(query.getMemberId()), BbsComment::getMemberId, query.getMemberId());
        wrapper.eq(StringUtil.isNotEmpty(query.getStatus()), BbsComment::getStatus, query.getStatus());
        wrapper.orderByDesc(BbsComment::getCreatedTime);
        return wrapper;
    }

    @Override
    public Boolean imports(Set<BbsCommentExport> list) {
        return true;
    }

    @Override
    public Boolean deleteByIdXml(String id, String loginId) {
        BbsCommentVo vo = baseMapper.selectVoById(id);
        if(vo==null){
            throw new MmsException("话题不存在");
        }
        if(vo.getMemberId().equals(loginId)){
            return deleteById(id);
        }
        throw new MmsException("无权限删除");
    }
}
