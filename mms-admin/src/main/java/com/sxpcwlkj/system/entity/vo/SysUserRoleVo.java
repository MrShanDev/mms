package com.sxpcwlkj.system.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.sxpcwlkj.system.entity.SysUserRole;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * 用户角色
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@AutoMapper(target = SysUserRole.class)
public class SysUserRoleVo {

    /**
     * 主键编码
     */
    @TableId(value = "id")
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 角色ID
     */
    private String roleId;

    private String revision;
}
