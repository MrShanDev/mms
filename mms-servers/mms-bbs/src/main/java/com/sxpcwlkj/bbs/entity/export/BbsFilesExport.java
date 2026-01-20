package com.sxpcwlkj.bbs.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.vo.BbsFilesVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 话题附件Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsFilesVo.class)
@EqualsAndHashCode(callSuper=false)
public class BbsFilesExport  extends BaseEntityVo{
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
	 * 类型
	 */
    @ExcelProperty("类型")
	@PrintColumn(title = "类型", type = PrintTypeEnum.TEXT)
	private  String type;
	/**
	 * 高度
	 */
    @ExcelProperty("高度")
	@PrintColumn(title = "高度", type = PrintTypeEnum.TEXT)
	private  Integer height;
	/**
	 * 宽度
	 */
    @ExcelProperty("宽度")
	@PrintColumn(title = "宽度", type = PrintTypeEnum.TEXT)
	private  Integer width;
	/**
	 * 大小
	 */
    @ExcelProperty("大小")
	@PrintColumn(title = "大小", type = PrintTypeEnum.TEXT)
	private  Integer size;

    /**
     * 附件地址
     */
    private String url;
}
