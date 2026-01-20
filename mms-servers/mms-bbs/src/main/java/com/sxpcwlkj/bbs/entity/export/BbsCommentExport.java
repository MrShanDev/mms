package com.sxpcwlkj.bbs.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.vo.BbsCommentVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 话题评论Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsCommentVo.class)
@EqualsAndHashCode(callSuper=false)
public class BbsCommentExport  extends BaseEntityVo{
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
	 * 父ID
	 */
    @ExcelProperty("父ID")
	@PrintColumn(title = "父ID", type = PrintTypeEnum.TEXT)
	private  String fatherId;
	/**
	 * 话题ID
	 */
    @ExcelProperty("话题ID")
	@PrintColumn(title = "话题ID", type = PrintTypeEnum.TEXT)
	private  String bbsId;
	/**
	 * 会员ID
	 */
    @ExcelProperty("会员ID")
	@PrintColumn(title = "会员ID", type = PrintTypeEnum.TEXT)
	private  String memberId;
	/**
	 * 评论
	 */
    @ExcelProperty("评论")
	@PrintColumn(title = "评论", type = PrintTypeEnum.TEXT)
	private  String comment;
}
