package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreOrderStatusLog;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 订单状态流水表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreOrderStatusLog.class)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderStatusLogVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 原状态
	 */
	private Integer oldStatus;
	/**
	 * 新状态
	 */
	private Integer newStatus;
	/**
	 * 操作人
	 */
	private String operator;

}
