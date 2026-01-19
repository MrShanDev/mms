package com.sxpcwlkj.bbs.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.BbsComment;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
* 话题评论Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = BbsComment.class)
@EqualsAndHashCode(callSuper=false)
public class BbsCommentVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 父ID
	 */
	private String fatherId;
	/**
	 * 话题ID
	 */
	private String bbsId;
	/**
	 * 会员ID
	 */
	private String memberId;
	/**
	 * 评论
	 */
	private String comment;
    private String[] ids;
    List<BbsCommentVo> children;

    private String memberNickName;

    private String memberHeadImg;

    //点赞数
    private Long likeCount;
    private boolean like;
    /**
     * 评论等级
     */
    private Integer level;
}
