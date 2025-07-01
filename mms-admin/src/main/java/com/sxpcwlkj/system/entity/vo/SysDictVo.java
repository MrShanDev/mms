package com.sxpcwlkj.system.entity.vo;

import com.sxpcwlkj.system.entity.SysDict;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 系统字典
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@AutoMapper(target = SysDict.class)
public class SysDictVo {
    private String id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字典类型
     */
    private Integer type;
    /**
     * 状态;0正常 1停用
     */
    private String status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;

    private Date createdTime;

    private String revision;
    /**
     * 字典值
     */
    private List<SysDictDataVo> list;
}
