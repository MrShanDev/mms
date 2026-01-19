package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品评论表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_review")
@EqualsAndHashCode(callSuper = true)
public class StoreReview  extends BaseEntity {
    /**
     * 评论ID
     */
    @TableId
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商品ID
     */
    private String spuId;
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
