package com.sxpcwlkj.system.entity.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.system.entity.SysNotice;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 系统公告Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = SysNotice.class)
@EqualsAndHashCode(callSuper=false)
public class SysNoticeVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 公告ID
	 */
    @ExcelProperty("公告ID")
	private String id;
	/**
	 * 公告标题
	 */
    @ExcelProperty("公告标题")
	private String title;
	/**
	 * 公告内容
	 */
    @ExcelProperty("公告内容")
	private String content;
	/**
	 * 公告类型
	 */
    @ExcelProperty("公告类型")
	private Integer type;

}
