package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreSearchResVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 搜索记录Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreSearchResVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreSearchResExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@ExcelIgnore
    @ExcelProperty("")
	@PrintColumn(title = "", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 *
	 */
    @ExcelProperty("")
	@PrintColumn(title = "", type = PrintTypeEnum.TEXT)
	private  Long memberId;
	/**
	 *
	 */
    @ExcelProperty("")
	@PrintColumn(title = "", type = PrintTypeEnum.TEXT)
	private  String searchKeyword;
	/**
	 *
	 */
    @ExcelProperty("")
	@PrintColumn(title = "", type = PrintTypeEnum.TEXT)
	private  Date addTime;
	/**
	 *
	 */
    @ExcelProperty("")
	@PrintColumn(title = "", type = PrintTypeEnum.TEXT)
	private  Integer searchNum;
	/**
	 * 创建者
	 */
    @ExcelProperty("创建者")
	@PrintColumn(title = "创建者", type = PrintTypeEnum.TEXT)
	private  Long createdby;
	/**
	 * 创建时间
	 */
    @ExcelProperty("创建时间")
	@PrintColumn(title = "创建时间", type = PrintTypeEnum.TEXT)
	private  Date createdtime;
	/**
	 * 更新者
	 */
    @ExcelProperty("更新者")
	@PrintColumn(title = "更新者", type = PrintTypeEnum.TEXT)
	private  Long updatedby;
	/**
	 * 更新时间
	 */
    @ExcelProperty("更新时间")
	@PrintColumn(title = "更新时间", type = PrintTypeEnum.TEXT)
	private  Date updatedtime;
}
