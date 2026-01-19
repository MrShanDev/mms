package com.sxpcwlkj.bbs.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.vo.BbsLeveVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 话题操作Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsLeveVo.class)
@EqualsAndHashCode(callSuper=false)
public class BbsLeveExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
    @ExcelProperty("ID")
	@PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
	private  String id;
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
	 * 类型
	 */
	@Dict("ticptype")
	@ExcelProperty(value ="类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "类型", type = PrintTypeEnum.TEXT)
	private  Integer type;
}
