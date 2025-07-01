package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.system.entity.SysDictData;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统字典数据
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysDictData.class)
public class SysDictDataBo extends BaseEntity {

    private String id;

    private String label;

    private String fieldName;

    private String value;

    private String dictType;



    private String colorType;
}
