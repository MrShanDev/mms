package com.sxpcwlkj.bbs.entity.bo;

import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import org.springframework.format.annotation.DateTimeFormat;
import com.sxpcwlkj.bbs.entity.BbsAttention;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 关注作者Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsAttention.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class BbsAttentionBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 发布者
	 */
	@NotBlank(message = "发布者不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String memberId;
	/**
	 * 被关注者
	 */
	@NotBlank(message = "被关注者不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String attentionId;
}
