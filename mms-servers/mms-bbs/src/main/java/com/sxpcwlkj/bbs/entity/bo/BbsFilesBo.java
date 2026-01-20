package com.sxpcwlkj.bbs.entity.bo;

import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import org.springframework.format.annotation.DateTimeFormat;
import com.sxpcwlkj.bbs.entity.BbsFiles;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 话题附件Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsFiles.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class BbsFilesBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 话题ID
	 */
	@NotBlank(message = "话题ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String bbsId;
	/**
	 * 类型 1:图片 2:视频 3:音频 4:文档 5:其他
	 */
	@NotBlank(message = "类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String type;
	/**
	 * 高度
	 */
	@NotNull(message = "高度不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer height;
	/**
	 * 宽度
	 */
	@NotNull(message = "宽度不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer width;
	/**
	 * 大小
	 */
	//@NotNull(message = "大小不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer size;

    @NotBlank(message = "附件地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String url;
}
