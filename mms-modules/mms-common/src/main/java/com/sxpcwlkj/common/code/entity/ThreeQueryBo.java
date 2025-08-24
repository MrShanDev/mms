package com.sxpcwlkj.common.code.entity;

import lombok.Data;

/**
 * 树结构查询bo
 * @Author: mmsAdmin
 */
@Data
public class ThreeQueryBo {
    //true：全部数据 false：有效数据(status=0)
    private Boolean isAll=false;
    //显示级别，0：全部 1：一级 2：二级 3：三级
    private Integer showLevel=0;
}
