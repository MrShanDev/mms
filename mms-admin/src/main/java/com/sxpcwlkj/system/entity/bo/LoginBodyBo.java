package com.sxpcwlkj.system.entity.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * 登录bo
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
public class LoginBodyBo {

    /**
     * 用户名
     */
    @Schema(description = "账号",type = "string", example = "demo", maxLength = 16, minLength = 3, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 16, message = "账号长度在{min}到{max}个字符")
    private String username;

    /**
     * 用户密码
     */
    @Schema(description = "账号密码", type = "string",example = "******", maxLength = 32, minLength = 6, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度在{min}到{max}个字符")
     private String password;
    /**
     * 验证码
     */
    @Schema(description = "验证码",type = "string", example = "1234J0",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;
    /**
     * 验证码Key
     */
    @Schema(description = "验证码Key",type = "string", example = "1234J0",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String codeKey;
    /**
     * uuid
     */
    @Schema(description = "uuid",type = "string", example = "e5cd7e4891bf95d1d19206ce24a7b32e",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String uuid;
    /**
     * 记住我
     */
    @Schema(description = "记住我",type = "boolean", example = "true",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean rememberMe=true;

    /**
     * 租户ID
     */
    private String tenantId;

}
