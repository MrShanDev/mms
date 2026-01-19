package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreReviewRepliesVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 评论回复表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreReviewRepliesVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreReviewRepliesExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 回复ID，雪花算法
	 */
	@ExcelIgnore
    @ExcelProperty("回复ID，雪花算法")
	@PrintColumn(title = "回复ID，雪花算法", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 评论ID
	 */
    @ExcelProperty("评论ID")
	@PrintColumn(title = "评论ID", type = PrintTypeEnum.TEXT)
	private  String reviewId;
	/**
	 * 回复用户ID
	 */
    @ExcelProperty("回复用户ID")
	@PrintColumn(title = "回复用户ID", type = PrintTypeEnum.TEXT)
	private  String userId;
	/**
	 * 被回复用户ID(针对回复的回复)
	 */
    @ExcelProperty("被回复用户ID(针对回复的回复)")
	@PrintColumn(title = "被回复用户ID(针对回复的回复)", type = PrintTypeEnum.TEXT)
	private  String targetUserId;
	/**
	 * 回复内容
	 */
    @ExcelProperty("回复内容")
	@PrintColumn(title = "回复内容", type = PrintTypeEnum.TEXT)
	private  String content;
}
