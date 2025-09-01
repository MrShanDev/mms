package com.sxpcwlkj.docApi.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.docApi.entity.DocAuthorizeUser;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 文档授权用户Bo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocAuthorizeUser.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class DocAuthorizeUserBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 用户编号
	 */
	@NotBlank(message = "用户编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String uid;
	/**
	 * 授权平台
	 */
	@NotBlank(message = "授权平台不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String chan;
	/**
	 * 授权平台标识
	 */
	@NotBlank(message = "授权平台标识不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String appid;
	/**
	 * 授权平台用户ID
	 */
	@NotBlank(message = "授权平台用户ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String openid;
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
