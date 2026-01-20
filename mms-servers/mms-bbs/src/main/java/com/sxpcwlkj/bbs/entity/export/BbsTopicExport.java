package com.sxpcwlkj.bbs.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.vo.BbsTopicVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 话题Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsTopicVo.class)
@EqualsAndHashCode(callSuper=false)
public class BbsTopicExport  extends BaseEntityVo{
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
	 * 发布者
	 */
    @ExcelProperty("发布者")
	@PrintColumn(title = "发布者", type = PrintTypeEnum.TEXT)
	private  String memberId;
	/**
	 * 分类ID
	 */
    @ExcelProperty("分类ID")
	@PrintColumn(title = "分类ID", type = PrintTypeEnum.TEXT)
	private  String cateId;
	/**
	 * 标题
	 */
    @ExcelProperty("标题")
	@PrintColumn(title = "标题", type = PrintTypeEnum.TEXT)
	private  String title;
	/**
	 * 内容
	 */
    @ExcelProperty("内容")
	@PrintColumn(title = "内容", type = PrintTypeEnum.TEXT)
	private  String contentHtml;
}
