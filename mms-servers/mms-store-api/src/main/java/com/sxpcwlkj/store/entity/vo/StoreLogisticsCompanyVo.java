package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreLogisticsCompany;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 快递公司名称Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreLogisticsCompany.class)
@EqualsAndHashCode(callSuper=false)
public class StoreLogisticsCompanyVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 快递公司简称
	 */
	private String code;
	/**
	 * 快递公司名称
	 */
	private String name;
	/**
	 * 快递公司类型
	 */
	private String type;
	/**
	 * 快递公司编号
	 */
	private String number;

}
