package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreSearchRes;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 搜索记录Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreSearchRes.class)
@EqualsAndHashCode(callSuper=false)
public class StoreSearchResVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	private String id;
	private Long memberId;
    private String memberName;
	private String searchKeyword;
	private Integer searchNum;
    private String content;
    private Integer rating;
}
