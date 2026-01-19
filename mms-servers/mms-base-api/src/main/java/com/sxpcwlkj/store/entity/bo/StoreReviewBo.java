package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreReview;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
 * 商品评论表Bo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = StoreReview.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreReviewBo  extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @NotBlank(message = "评论ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
    private String id;
    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
    private String userId;
    /**
     * 商品ID
     */
    @NotBlank(message = "商品ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
    private String spuId;
    /**
     * 订单ID
     */
    @NotBlank(message = "订单ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String orderId;
    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String content;
    /**
     * 评分(1-5分)
     */
//    @NotEmpty(message = "评分(1-5分)不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer rating;
    /**
     * 评论图片URL数组
     */
    //@NotBlank(message = "评论图片URL数组不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String images;
    /**
     * 评论视频URL
     */
    //@NotBlank(message = "评论视频URL不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String videoUrl;
    /**
     * 是否匿名评论
     */
    //@NotBlank(message = "是否匿名评论不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer isAnonymous;
    /**
     * 是否置顶
     */
    //@NotBlank(message = "是否置顶不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer isTop;
    /**
     * 点赞数
     */
    //@NotNull(message = "点赞数不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer likeCount;
    /**
     * 回复数
     */
    //@NotNull(message = "回复数不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer replyCount;
}
