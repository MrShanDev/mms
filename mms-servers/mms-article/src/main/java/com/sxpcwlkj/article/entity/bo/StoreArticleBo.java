package com.sxpcwlkj.article.entity.bo;

import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.article.entity.StoreArticle;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;

/**
* 店铺文章Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreArticle.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreArticleBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 文章标题
	 */
	@NotBlank(message = "文章标题不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	@DateTimeFormat(pattern=DateUtil.DATE_PATTERN)
	private String title;
	/**
	 * 封面图片
	 */
	//@NotBlank(message = "封面图片不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String coverImg;
	/**
	 * 标签
	 */
	//@NotBlank(message = "标签不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String tag;
	/**
	 * 作者
	 */
	//@NotBlank(message = "作者不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String author;
	/**
	 * 分类ID
	 */
	//@NotBlank(message = "请选择分类" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String articleCateId;
	/**
	 * 内容
	 */
	@NotBlank(message = "内容不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    @Size(max = 2000000000,message = "内容长度不能超过2000000000" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String content;

    /**
     * 会员ID
     */
    private String memberId;
}
