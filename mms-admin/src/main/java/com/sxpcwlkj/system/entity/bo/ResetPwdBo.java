package com.sxpcwlkj.system.entity.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 重置密码bo
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
public class ResetPwdBo {
   @NotBlank(message = "旧密码不能为空")
   @Size(min = 4, max = 16, message = "旧密码长度在{min}到{max}个字符")
   private String oldPassword;

   @NotBlank(message = "新密码不能为空")
   @Size(min = 4, max = 16, message = "新密码长度在{min}到{max}个字符")
   private String password;


}
