package com.sxpcwlkj.bbs.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.BbsCate;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
* 话题分类Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = BbsCate.class)
@EqualsAndHashCode(callSuper=false)
public class BbsCateVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 父ID
	 */
	private String fatherId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 图标
	 */
	private String icon;
    private String[] ids;
    List<BbsCateVo> children;

}
