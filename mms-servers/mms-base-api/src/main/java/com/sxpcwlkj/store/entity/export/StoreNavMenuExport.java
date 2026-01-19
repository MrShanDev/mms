package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreNavMenuVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 网站导航菜单Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreNavMenuVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreNavMenuExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ExcelIgnore
    @ExcelProperty("ID")
	@PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 名称
	 */
    @ExcelProperty("名称")
	@PrintColumn(title = "名称", type = PrintTypeEnum.TEXT)
	private  String name;
    /**
     * 父ID
     */
    private String parentId;
	/**
	 * 编码
	 */
    @ExcelProperty("编码")
	@PrintColumn(title = "编码", type = PrintTypeEnum.TEXT)
	private  String code;
	/**
	 * 路由地址
	 */
    @ExcelProperty("路由地址")
	@PrintColumn(title = "路由地址", type = PrintTypeEnum.TEXT)
	private  String routingPath;
	/**
	 * 图标
	 */
    @ExcelProperty("图标")
	@PrintColumn(title = "图标", type = PrintTypeEnum.TEXT)
	private  String icon;
	/**
	 * 样式
	 */
    @ExcelProperty("样式")
	@PrintColumn(title = "样式", type = PrintTypeEnum.TEXT)
	private  String css;
}
