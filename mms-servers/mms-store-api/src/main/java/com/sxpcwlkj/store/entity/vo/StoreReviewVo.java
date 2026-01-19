package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreReview;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品评论表Vo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */

@Data
@AutoMapper(target = StoreReview.class)
@EqualsAndHashCode(callSuper=false)
public class StoreReviewVo  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    private String userName;
    private String userAvatar;
    private String userPhone;
    /**
     * 商品ID
     */
    private String spuId;
    private String spuTitle;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评分(1-5分)
     */
    private Integer rating;
    /**
     * 评论图片URL数组
     */
    private String images;
    /**
     * 评论视频URL
     */
    private String videoUrl;
    /**
     * 是否匿名评论
     */
    private Integer isAnonymous;
    /**
     * 是否置顶
     */
    private Integer isTop;
    /**
     * 点赞数
     */
    private Integer likeCount;
    /**
     * 回复数
     */
    private Integer replyCount;

}
