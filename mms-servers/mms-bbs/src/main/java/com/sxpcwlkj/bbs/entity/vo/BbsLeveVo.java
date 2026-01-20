package com.sxpcwlkj.bbs.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.BbsLeve;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
* 话题操作Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = BbsLeve.class)
@EqualsAndHashCode(callSuper=false)
public class BbsLeveVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 话题ID
	 */
	private String bbsId;
	/**
	 * 会员ID
	 */
	private String memberId;
	/**
	 * 类型
	 */
	private Integer type;

}
