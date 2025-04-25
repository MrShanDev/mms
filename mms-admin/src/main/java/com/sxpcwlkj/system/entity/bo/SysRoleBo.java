package com.sxpcwlkj.system.entity.bo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.system.entity.SysRole;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 系统角色
 * @author xijue
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysRole.class)
public class SysRoleBo extends BaseEntity {

    /**
     * 角色ID
     */
    @TableId(value = "id")
    private String id;
    /**
     * 级别
     */
    @Min(message = "级别不能小于1",value = 1)
    private Integer level;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Length(message = "角色名称长度控制在{min}~{max}之间",min = 2, max = 20)
    private String name;

    /**
     * 角色权限字符串
     */
    @NotBlank(message = "角色权限字符串不能为空")
    @Length(message = "角色权限字符串长度控制在{min}~{max}之间",min = 4, max = 20)
    private String code;

    /**
     * 默认选中的节点
     */
    @NotNull(message = "权限节点不能为空")
    private String[] defChecked;
}
