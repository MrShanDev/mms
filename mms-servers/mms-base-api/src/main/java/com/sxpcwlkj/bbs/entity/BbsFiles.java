package com.sxpcwlkj.bbs.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题附件
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("bbs_files")
@EqualsAndHashCode(callSuper = true)
public class BbsFiles  extends BaseEntity {
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
}
