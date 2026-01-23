package com.sxpcwlkj.bbs.entity.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 社交消息Vo
 *
 * @author mmsAdmin
 */
@Data
public class BbsMsgVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 操作会员ID
     */
    private String memberId;

    /**
     * 会员昵称
     */
    private String memberNickName;

    /**
     * 会员头像
     */
    private String memberHeadImg;

    /**
     * 话题ID
     */
    private String bbsId;

    /**
     * 话题主图
     */
    private String topicMainImg;

    /**
     * 内容 (评论内容/点赞描述等)
     */
    private String content;

    /**
     * 类型: 1-点赞, 3-收藏, 4-评论, 5-关注
     */
    private Integer type;

    /**
     * 操作时间
     */
    private Date createdTime;

    /**
     * 是否互相关注
     */
    private Boolean mutualAttention;
}
