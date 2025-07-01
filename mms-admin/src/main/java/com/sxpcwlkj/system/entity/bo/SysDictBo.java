package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.system.entity.SysDict;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统字典bo
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysDict.class)
public class SysDictBo extends BaseEntity {

    private String id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典名称
     */
    private String fieldName;
    /**
     * 字典类型
     */
    private Integer type;


    private List<SysDictDataBo> list;
}
