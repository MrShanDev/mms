package com.sxpcwlkj.article.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.article.entity.vo.StoreArticleVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 店铺文章Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreArticleVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreArticleExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ExcelIgnore
    @ExcelProperty("ID")
	@PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 文章标题
	 */
    @ExcelProperty("文章标题")
	@PrintColumn(title = "文章标题", type = PrintTypeEnum.TEXT)
	private  String title;
	/**
	 * 封面图片
	 */
    @ExcelProperty("封面图片")
	@PrintColumn(title = "封面图片", type = PrintTypeEnum.TEXT)
	private  String coverImg;
	/**
	 * 标签
	 */
    @ExcelProperty("标签")
	@PrintColumn(title = "标签", type = PrintTypeEnum.TEXT)
	private  String tag;
	/**
	 * 作者
	 */
    @ExcelProperty("作者")
	@PrintColumn(title = "作者", type = PrintTypeEnum.TEXT)
	private  String author;
	/**
	 * 分类ID
	 */
    @ExcelProperty("分类ID")
	@PrintColumn(title = "分类ID", type = PrintTypeEnum.TEXT)
	private  String articleCateId;
	/**
	 * 内容
	 */
    @ExcelProperty("内容")
	@PrintColumn(title = "内容", type = PrintTypeEnum.TEXT)
	private  String content;
}
