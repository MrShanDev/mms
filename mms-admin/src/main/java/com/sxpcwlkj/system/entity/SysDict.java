package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统字典
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@TableName("sys_dict")
public class SysDict extends BaseEntity {

    /**
     * 字典主键
     */
    @TableId(value = "id")
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
    private String type;

}
