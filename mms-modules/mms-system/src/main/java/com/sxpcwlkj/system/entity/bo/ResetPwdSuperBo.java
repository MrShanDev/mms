package com.sxpcwlkj.system.entity.bo;

import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * 重置用户密码bo
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
public class ResetPwdSuperBo {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 16, message = "新密码长度在{min}到{max}个字符")
    private String password;
}
