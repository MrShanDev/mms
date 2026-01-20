package com.sxpcwlkj.bbs.entity.bo;

import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import org.springframework.format.annotation.DateTimeFormat;
import com.sxpcwlkj.bbs.entity.BbsTopic;
import java.util.Date;
import java.util.List;

import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 话题Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsTopic.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class BbsTopicBo  extends BaseEntity {
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
	//@NotBlank(message = "发布者不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String memberId;
	/**
	 * 分类ID
	 */
	@NotBlank(message = "分类ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String cateId;
	/**
	 * 标题
	 */
	@NotBlank(message = "标题不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String title;
	/**
	 * 内容
	 */
	@NotBlank(message = "内容不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String contentHtml;

    /**
     * 附件
     */
    private List<BbsFilesBo> files;
}
