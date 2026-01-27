package com.sxpcwlkj.member.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.member.entity.vo.StoreToolAreaVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
 * 行政区域Export
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = StoreToolAreaVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreToolAreaExport  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    @PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
    private  Long id;
    /**
     * 名称
     */
    @ExcelProperty("名称")
    @PrintColumn(title = "名称", type = PrintTypeEnum.TEXT)
    private  String name;
    /**
     * CODE
     */
    @ExcelProperty("CODE")
    @PrintColumn(title = "CODE", type = PrintTypeEnum.TEXT)
    private  String code;
    /**
     * 父CODE
     */
    @ExcelProperty("父CODE")
    @PrintColumn(title = "父CODE", type = PrintTypeEnum.TEXT)
    private  String parentCode;
    /**
     * 级别
     */
    @ExcelProperty("级别")
    @PrintColumn(title = "级别", type = PrintTypeEnum.TEXT)
    private  String level;
}
