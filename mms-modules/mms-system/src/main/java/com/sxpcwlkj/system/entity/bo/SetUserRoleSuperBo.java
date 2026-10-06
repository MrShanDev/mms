package com.sxpcwlkj.system.entity.bo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * 管理员重置用户密码
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
public class SetUserRoleSuperBo {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotEmpty(message = "角色至少选择一个")
    private List<String> roleCodes;
}
