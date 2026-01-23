package com.sxpcwlkj.bbs.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.bbs.entity.BbsAttention;
import com.sxpcwlkj.bbs.entity.BbsTopic;
import com.sxpcwlkj.bbs.entity.BbsLeve;
import com.sxpcwlkj.bbs.entity.bo.BbsCommentBo;
import com.sxpcwlkj.bbs.entity.bo.BbsTopicBo;
import com.sxpcwlkj.bbs.entity.bo.BbsAttentionBo;
import com.sxpcwlkj.bbs.entity.bo.BbsLeveBo;
import com.sxpcwlkj.bbs.entity.vo.*;
import com.sxpcwlkj.bbs.mapper.BbsAttentionMapper;
import com.sxpcwlkj.bbs.mapper.BbsTopicMapper;
import com.sxpcwlkj.bbs.mapper.BbsLeveMapper;
import com.sxpcwlkj.bbs.service.*;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 话题
 * @author mmsAdmin
 */
@Tag(name = "✅ 模块-话题",description = "")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("bbs/v1")
public class ApiBbsController extends BaseController {

    private final BbsCateService bbsCateService;
    private final BbsTopicService bbsTopicService;
    private final BbsCommentService bbsCommentService;
    private final BbsLeveService bbsLeveService;
    private final BbsAttentionService bbsAttentionService;
    private final BbsAttentionMapper bbsAttentionMapper;
    private final BbsTopicMapper bbsTopicMapper;
    private final BbsLeveMapper bbsLeveMapper;

    /**
     * 话题分类
     * @return 数据
     */
    @SaIgnore
    @GetMapping("/bbsCate")
    public R<List<BbsCateVo>> bbsCate(){
        List<BbsCateVo> list= bbsCateService.queryTree(false,0);
        if(!list.isEmpty()){
            list=list.get(0).getChildren();
        }
        return R.success(list);
    }

    /**
     * 话题列表分页
     * @param cateId 分类id  cateId=-1 我的，cateId=-2 关注 cateId=-3 附近 cateId=0 推荐
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @param latitude 纬度（附近功能使用）
     * @param longitude 经度（附近功能使用）
     * @return 数据
     */
    @SaIgnore
    @GetMapping("/topicPage")
    public R<TableDataInfo<BbsTopicVo>> bbsTopicList(
         String cateId,
         String keyWord,
         Double latitude,
         Double longitude,
         @NotNull(message = "每页大小不能为空")
         @Min(value = 1, message = "每页大小不能小于1")
         @Max(value = 100, message = "每页大小不能大于100")
         @RequestParam(defaultValue = "10") Integer pageSize,
         @NotNull(message = "页码不能为空")
         @Min(value = 1, message = "页码不能小于1")
         @RequestParam(defaultValue = "1") Integer pageNum) {

             BbsTopicBo bo=new BbsTopicBo();
             bo.setPageNum(pageNum);
             bo.setPageSize(pageSize);
             String memberId=null;
             // 我的
             if("-1".equals(cateId)){
                 memberId=LoginObject.getLoginId();
                 cateId=null;
             }
             // 关注
             List<String> bbsAttentionList = null;
             if ("-2".equals(cateId)){
                 bbsAttentionList = bbsAttentionService.selectAttentionListByMemberId(LoginObject.getLoginId());
                 cateId=null;
                 // 如果没有关注任何人，返回空列表
                 if(bbsAttentionList.isEmpty()){
                     return success(TableDataInfo.build());
                 }
             }
             //附近
             Double nearbyLatitude = null;
             Double nearbyLongitude = null;
             if ("-3".equals(cateId)){
                 cateId=null;
                 // 附近功能需要传入经纬度
                 if(latitude == null || longitude == null){
                     return fail("查询附近话题需要提供地理位置信息");
                 }
                 nearbyLatitude = latitude;
                 nearbyLongitude = longitude;
             }
             // 推荐（按点赞数排序）
            if ("0".equals(cateId)){
                cateId=null;
                // 推荐逻辑：不传分类，在XML中会按点赞数倒序排序
            }
            //指定分类
            return success(bbsTopicService.selectListVoPageXml(cateId,keyWord,memberId,bbsAttentionList,nearbyLatitude,nearbyLongitude, bo.getPageQuery(), null));
    }

    /**
     * 发布话题
     */
    @SaCheckLogin
    @PostMapping("/insertTopic")
    public R<Boolean> insert(@RequestBody BbsTopicBo bo) {
        bo.setMemberId(LoginObject.getLoginId());
        bo.setStatus(1);
        if(bo.getContentHtml().length()<3){
            throw new MmsException("内容过少请丰富内容再发布吧！");
        }
        if(bo.getTitle().isEmpty()){
            throw new MmsException("缺少作品标题");
        }
        if(bo.getCateId()==null){
            throw new MmsException("请选择话题分类");
        }
        if(bo.getFiles()==null||bo.getFiles().isEmpty()){
            throw new MmsException("请上传话题图片或视频再发布吧！");
        }
        return success(bbsTopicService.insert(bo));
    }

    /**
     * 删除话题
     * @param id 话题id
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/deleteTopic")
    public R<Boolean> deleteTopic(String id) {
        return success(bbsTopicService.deleteByIdXml(id,LoginObject.getLoginId()));
    }

    /**
     * 话题详情
     * @param id 话题id
     * @return 数据
     */
    @SaIgnore
    @GetMapping("/selectById")
    public R<BbsTopicVo> selectVoById(String id) {
        return success(bbsTopicService.selectVoById(id));
    }

    /**
     * 话题发布评论
     * @param topicId 话题id
     * @param fatherId 父级id  默认空
     * @param content 内容
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/commentAdd")
    public R<Boolean> commentAdd(
        String topicId,
        String fatherId,
        String content) {
        if(StringUtils.isBlank(topicId)){
            return fail("话题id不能为空");
        }
        if(StringUtils.isBlank(content)){
            return fail("评论内容不能为空");
        }
        BbsCommentBo bo=new BbsCommentBo();
        if (StringUtils.isBlank(fatherId)) {
            fatherId=null;
        }
        bo.setBbsId(topicId);
        bo.setFatherId(fatherId);
        bo.setComment(content);
        bo.setMemberId(LoginObject.getLoginId());
        bo.setStatus(1);
        bo.setLevel(1);
        return success(bbsCommentService.insert(bo));
    }

    /**
     * 删除评论
     * @param id 评论id
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/commentDelete")
    public R<Boolean> commentDelete(String id) {
        return success(bbsCommentService.deleteByIdXml(id,LoginObject.getLoginId()));
    }

    /**
     * 话题评论分页
     * @param topicId 话题id
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @return 数据
     */
    @SaIgnore
    @GetMapping("/commentList")
    public R<TableDataInfo<BbsCommentVo>> commentList(
                         String topicId,
                         @NotNull(message = "每页大小不能为空")
                         @Min(value = 1, message = "每页大小不能小于1")
                         @Max(value = 100, message = "每页大小不能大于100")
                         @RequestParam(defaultValue = "10") Integer pageSize,
                         @NotNull(message = "页码不能为空")
                         @Min(value = 1, message = "页码不能小于1")
                         @RequestParam(defaultValue = "1") Integer pageNum) {
        BbsCommentBo bo=new BbsCommentBo();
        bo.setPageNum(pageNum);
        bo.setPageSize(pageSize);
        bo.setBbsId(topicId);
        bo.setStatus(1);
        return success(bbsCommentService.selectListVoPage(bo,bo.getPageQuery()));
    }

    /**
     * 点赞/取消点赞
     * @param id 话题id/评论id
     * @param type 1话题点赞  2:评论点赞
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/clickTopic")
    public R<Boolean> clickTopic(String id,int type) {
        return success(bbsLeveService.clickTopic(id,type,LoginObject.getLoginId()));
    }

    /**
     * 收藏/取消收藏话题
     * @param id 话题id
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/collectTopic")
    public R<Boolean> collectTopic(String id) {
        return success(bbsLeveService.clickTopic(id,3,LoginObject.getLoginId()));
    }

    /**
     * 关注/取消关注
     * @param mid 被关注者id
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/clickAttention")
    public R<Boolean> clickAttention(String mid) {
        return success(bbsAttentionService.clickAttention(mid,LoginObject.getLoginId()));
    }

    /**
     * 我的社交数据统计
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/myDataCount")
    public R<Map<String, Long>> getMyDataCount() {
        String memberId = LoginObject.getLoginId();

        // 统计我关注的人数
        Long followingCount = bbsAttentionMapper.selectCount(new LambdaQueryWrapper<BbsAttention>()
            .eq(BbsAttention::getMemberId, memberId));

        // 统计关注我的人数（粉丝数）
        Long fansCount = bbsAttentionMapper.selectCount(new LambdaQueryWrapper<BbsAttention>()
            .eq(BbsAttention::getAttentionId, memberId));

        // 统计我发布的话题数
        Long topicCount = bbsTopicMapper.selectCount(new LambdaQueryWrapper<BbsTopic>()
            .eq(BbsTopic::getMemberId, memberId)
            .eq(BbsTopic::getStatus, 1));

        // 统计我收藏的贴子数
        Long collectCount = bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
            .eq(BbsLeve::getMemberId, memberId)
            .eq(BbsLeve::getType, 3)
            .eq(BbsLeve::getStatus, 1));

        // 统计我点赞的贴子数
        Long likeCount = bbsLeveMapper.selectCount(new LambdaQueryWrapper<BbsLeve>()
            .eq(BbsLeve::getMemberId, memberId)
            .eq(BbsLeve::getType, 1)
            .eq(BbsLeve::getStatus, 1));

        Map<String, Long> result = new HashMap<>();
        result.put("followingCount", followingCount == null ? 0L : followingCount);  // 我关注的人数
        result.put("fansCount", fansCount == null ? 0L : fansCount);            // 粉丝数
        result.put("topicCount", topicCount == null ? 0L : topicCount);          // 我发布的话题数
        result.put("collectCount", collectCount == null ? 0L : collectCount);      // 我收藏的贴子数
        result.put("likeCount", likeCount == null ? 0L : likeCount);            // 我点赞的贴子数

        return success(result);
    }

    /**
     * 我的内容列表（发布/收藏/点赞）
     * @param type 类型：1-我的发布，2-我的收藏，3-我的点赞
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/myData")
    public R<TableDataInfo<BbsTopicVo>> getMyData(
            @NotNull(message = "类型不能为空") Integer type,
            @NotNull(message = "每页大小不能为空")
            @Min(value = 1, message = "每页大小不能小于1")
            @Max(value = 100, message = "每页大小不能大于100")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @NotNull(message = "页码不能为空")
            @Min(value = 1, message = "页码不能小于1")
            @RequestParam(defaultValue = "1") Integer pageNum) {

        String memberId = LoginObject.getLoginId();
        BbsTopicBo bo = new BbsTopicBo();
        bo.setPageNum(pageNum);
        bo.setPageSize(pageSize);

        if (type == 1) {
            // 我的发布：直接查询话题表
            bo.setMemberId(memberId);
            bo.setStatus(1);
            return success(bbsTopicService.selectListVoPage(bo, bo.getPageQuery()));

        } else if (type == 2) {
            // 我的收藏：查询 bbs_leve 表中 type=3 的话题ID，然后查询话题详情
            List<BbsLeveVo> collectList = bbsLeveService.selectVoListByLqw(
                new LambdaQueryWrapper<BbsLeve>()
                    .eq(BbsLeve::getMemberId, memberId)
                    .eq(BbsLeve::getType, 3)
                    .eq(BbsLeve::getStatus, 1)
                    .select(BbsLeve::getBbsId)
            );

            if (collectList.isEmpty()) {
                return success(TableDataInfo.build());
            }

            List<String> topicIds = collectList.stream()
                .map(BbsLeveVo::getBbsId)
                .toList();

            return success(bbsTopicService.selectListVoPageXml(null, null, null, null, null, null, bo.getPageQuery(), topicIds));

        } else if (type == 3) {
            // 我的点赞：查询 bbs_leve 表中 type=1 的话题ID，然后查询话题详情
            List<BbsLeveVo> likeList = bbsLeveService.selectVoListByLqw(
                new LambdaQueryWrapper<BbsLeve>()
                    .eq(BbsLeve::getMemberId, memberId)
                    .eq(BbsLeve::getType, 1)
                    .eq(BbsLeve::getStatus, 1)
                    .select(BbsLeve::getBbsId)
            );

            if (likeList.isEmpty()) {
                return success(TableDataInfo.build());
            }

            List<String> topicIds = likeList.stream()
                .map(BbsLeveVo::getBbsId)
                .toList();

            return success(bbsTopicService.selectListVoPageXml(null, null, null, null, null, null, bo.getPageQuery(), topicIds));

        } else {
            return fail("类型参数错误，请传入 1-发布，2-收藏，3-点赞");
        }
    }

    /**
     * 我收到的赞和收藏
     */
    @SaCheckLogin
    @GetMapping("/receivedLeveList")
    public R<TableDataInfo<BbsMsgVo>> receivedLeveList(
            @NotNull(message = "每页大小不能为空")
            @Min(value = 1, message = "每页大小不能小于1")
            @Max(value = 100, message = "每页大小不能大于100")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @NotNull(message = "页码不能为空")
            @Min(value = 1, message = "页码不能小于1")
            @RequestParam(defaultValue = "1") Integer pageNum) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        return success(bbsTopicService.selectReceivedLeveList(LoginObject.getLoginId(), pageQuery));
    }

    /**
     * 关注用户的列表
     */
    @SaCheckLogin
    @GetMapping("/followingList")
    public R<TableDataInfo<BbsMsgVo>> followingList(
            @NotNull(message = "每页大小不能为空")
            @Min(value = 1, message = "每页大小不能小于1")
            @Max(value = 100, message = "每页大小不能大于100")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @NotNull(message = "页码不能为空")
            @Min(value = 1, message = "页码不能小于1")
            @RequestParam(defaultValue = "1") Integer pageNum) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        return success(bbsTopicService.selectFollowingList(LoginObject.getLoginId(), pageQuery));
    }

    /**
     * 有人评论@我的列表
     */
    @SaCheckLogin
    @GetMapping("/atMeList")
    public R<TableDataInfo<BbsMsgVo>> atMeList(
            @NotNull(message = "每页大小不能为空")
            @Min(value = 1, message = "每页大小不能小于1")
            @Max(value = 100, message = "每页大小不能大于100")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @NotNull(message = "页码不能为空")
            @Min(value = 1, message = "页码不能小于1")
            @RequestParam(defaultValue = "1") Integer pageNum) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        return success(bbsTopicService.selectAtMeCommentList(LoginObject.getLoginId(), pageQuery));
    }

}
