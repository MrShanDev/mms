package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索记录
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_search_res")
@EqualsAndHashCode(callSuper = true)
public class StoreSearchRes  extends BaseEntity {
	@TableId
	private String id;
	private Long memberId;
	private String searchKeyword;
	/**
     * 搜索次数
     */
	private Integer searchNum;
}
