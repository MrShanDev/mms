package com.sxpcwlkj.system.entity.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * 登录bo
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Data
public class LoginBodyBo {

    /**
     * 用户名
     */
    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 16, message = "账号长度在{min}到{max}个字符")
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度在{min}到{max}个字符")
     private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码Key
     */
    private String codeKey;
    /**
     * uuid
     */
    private String uuid;

    /**
     * 租户ID(改为租户由后端控制)
     */
    private String tenantId;

    /**
     * 记住我
     */
    private Boolean rememberMe=true;

}
