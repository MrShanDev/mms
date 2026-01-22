package com.sxpcwlkj.bbs.entity.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.BbsTopic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* 话题Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = BbsTopic.class)
@EqualsAndHashCode(callSuper=false)
public class BbsTopicVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 发布者
	 */
	private String memberId;
	/**
	 * 分类ID
	 */
	private String cateId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String contentHtml;
    /**
     * 昵称
     */
    private String memberNickName;
    /**
     * 头像
     */
    private String memberHeadImg;

//    private Integer height;

//    private Integer width;
    /**
     * 评论数
     */
    private Long commentCount;
    /**
     * 点赞数
     */
    private Long likeCount;
    /**
     * 是否点赞
     */
    private Boolean like;

    private String fileUrl;

    private String fileType;
    /**
     * 附件
     */
    private List<BbsFilesVo> files;
    /**
     * 创建时间
     */
    private Date createdTime;
}
