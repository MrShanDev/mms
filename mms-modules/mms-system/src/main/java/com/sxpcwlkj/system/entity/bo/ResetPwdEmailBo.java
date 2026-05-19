package com.sxpcwlkj.system.entity.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 邮箱验证码重置密码参数
 *
 * @author mmsAdmin
 */
@Data
public class ResetPwdEmailBo {

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码为6位")
    private String code;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 32, message = "新密码长度在{min}到{max}个字符")
    private String password;
}
