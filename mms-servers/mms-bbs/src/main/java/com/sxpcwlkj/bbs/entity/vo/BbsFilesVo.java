package com.sxpcwlkj.bbs.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.BbsFiles;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

/**
* 话题附件Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = BbsFiles.class)
@EqualsAndHashCode(callSuper=false)
public class BbsFilesVo implements Serializable {
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
	 * 类型
	 */
	private String type;
	/**
	 * 高度
	 */
	private Integer height;
	/**
	 * 宽度
	 */
	private Integer width;
	/**
	 * 大小
	 */
	private Integer size;
    /**
     * 附件地址
     */
    private String url;
    /**
     * 创建时间
     */
    private Date createdTime;
}
