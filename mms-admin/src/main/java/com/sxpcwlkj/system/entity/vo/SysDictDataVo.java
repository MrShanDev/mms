package com.sxpcwlkj.system.entity.vo;

import com.sxpcwlkj.system.entity.SysDictData;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * 字典数据
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Data
@AutoMapper(target = SysDictData.class)
public class SysDictDataVo {

    private String id;

    private String label;

    private String fieldName;

    private String value;

    private String dictType;

    private Integer sort;

    private String status;

    private String colorType;

    private String revision;
}
