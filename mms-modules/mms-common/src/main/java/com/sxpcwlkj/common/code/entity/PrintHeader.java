package com.sxpcwlkj.common.code.entity;

import com.sxpcwlkj.common.enums.PrintTypeEnum;
import lombok.Data;

/**
 * Table 打印对象表头
 * @author Xijue
 */
@Data
public class PrintHeader{
    private String key;
    private String title;
    private PrintTypeEnum type;
    private String width;
    private String height;
    private boolean show;
    private String align;
}
