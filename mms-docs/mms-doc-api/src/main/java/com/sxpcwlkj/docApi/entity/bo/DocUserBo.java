package com.sxpcwlkj.docApi.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.docApi.entity.DocUser;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 文档用户Bo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocUser.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class DocUserBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@NotBlank(message = "用户编号不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String uid;
	/**
	 * 昵称
	 */
	@NotBlank(message = "昵称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String nickname;
	/**
	 * 头像
	 */
	@NotBlank(message = "头像不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String avatar;
	/**
	 * 用户类型
	 */
	@NotBlank(message = "用户类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String type;
	/**
	 * 创建时间
	 */
	@NotNull(message = "创建时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date ctime;
	/**
	 * 更新时间
	 */
	@NotNull(message = "更新时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date mtime;
}
