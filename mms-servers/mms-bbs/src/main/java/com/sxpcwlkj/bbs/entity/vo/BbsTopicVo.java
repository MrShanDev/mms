package com.sxpcwlkj.bbs.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.bbs.entity.bo.BbsFilesBo;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.BbsTopic;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
* 话题Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = BbsTopic.class)
@EqualsAndHashCode(callSuper=false)
public class BbsTopicVo  extends BaseEntityVo{
	@Serial
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

    private String memberNickName;

    private String memberHeadImg;

    private Integer height;

    private Integer width;
    // 评论数
    private Long commentCount;
    //点赞数
    private Long likeCount;
    private Boolean like;

    private String fileUrl;

    private String fileType;
    /**
     * 附件
     */
    private List<BbsFilesVo> files;
}
