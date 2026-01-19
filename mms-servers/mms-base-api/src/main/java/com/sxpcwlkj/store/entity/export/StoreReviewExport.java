package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreReviewVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品评论表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreReviewVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreReviewExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 评论ID，雪花算法
	 */
	@ExcelIgnore
    @ExcelProperty("评论ID，雪花算法")
	@PrintColumn(title = "评论ID，雪花算法", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 用户ID
	 */
    @ExcelProperty("用户ID")
	@PrintColumn(title = "用户ID", type = PrintTypeEnum.TEXT)
	private  String userId;
	/**
	 * 商品ID
	 */
    @ExcelProperty("商品ID")
	@PrintColumn(title = "商品ID", type = PrintTypeEnum.TEXT)
	private  String spuId;
	/**
	 * 订单ID
	 */
    @ExcelProperty("订单ID")
	@PrintColumn(title = "订单ID", type = PrintTypeEnum.TEXT)
	private  String orderId;
	/**
	 * 评论内容
	 */
    @ExcelProperty("评论内容")
	@PrintColumn(title = "评论内容", type = PrintTypeEnum.TEXT)
	private  String content;
	/**
	 * 评分(1-5分)
	 */
    @ExcelProperty("评分(1-5分)")
	@PrintColumn(title = "评分(1-5分)", type = PrintTypeEnum.TEXT)
	private  Integer rating;
	/**
	 * 评论图片URL数组
	 */
    @ExcelProperty("评论图片URL数组")
	@PrintColumn(title = "评论图片URL数组", type = PrintTypeEnum.TEXT)
	private  String images;
	/**
	 * 评论视频URL
	 */
    @ExcelProperty("评论视频URL")
	@PrintColumn(title = "评论视频URL", type = PrintTypeEnum.TEXT)
	private  String videoUrl;
	/**
	 * 是否匿名评论
	 */
    @ExcelProperty("是否匿名评论")
	@PrintColumn(title = "是否匿名评论", type = PrintTypeEnum.TEXT)
	private  Integer isAnonymous;
	/**
	 * 是否置顶
	 */
    @ExcelProperty("是否置顶")
	@PrintColumn(title = "是否置顶", type = PrintTypeEnum.TEXT)
	private  Integer isTop;
	/**
	 * 点赞数
	 */
    @ExcelProperty("点赞数")
	@PrintColumn(title = "点赞数", type = PrintTypeEnum.TEXT)
	private  Integer likeCount;
	/**
	 * 回复数
	 */
    @ExcelProperty("回复数")
	@PrintColumn(title = "回复数", type = PrintTypeEnum.TEXT)
	private  Integer replyCount;
}
