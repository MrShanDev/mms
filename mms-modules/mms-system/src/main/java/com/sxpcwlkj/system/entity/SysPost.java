package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统岗位
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@TableName("sys_dept")
public class SysPost extends BaseEntity {

    /**
     * 岗位ID
     */
    @TableId(value = "id")
    private String id;
    /**
     * 岗位编码
     */

    private String code;
    /**
     * 岗位名称
     */

    private String name;
    /**
     * 显示顺序
     */

    private Integer sort;
    /**
     * 状态;0正常 1停用
     */

    private Integer status;

}
