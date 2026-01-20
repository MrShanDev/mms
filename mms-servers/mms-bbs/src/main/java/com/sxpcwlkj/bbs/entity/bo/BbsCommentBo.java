package com.sxpcwlkj.bbs.entity.bo;

import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import org.springframework.format.annotation.DateTimeFormat;
import com.sxpcwlkj.bbs.entity.BbsComment;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 话题评论Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsComment.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class BbsCommentBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 父ID
	 */
	@NotBlank(message = "父ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String fatherId;
	/**
	 * 话题ID
	 */
	@NotBlank(message = "话题ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String bbsId;
	/**
	 * 会员ID
	 */
	@NotBlank(message = "会员ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String memberId;
	/**
	 * 评论
	 */
	@NotBlank(message = "评论不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String comment;

    private Integer level=1;
}
