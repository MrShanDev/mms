package com.sxpcwlkj.member.entity.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 会员登录BO
 * @Description 会员登录
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Data
public class StoreMemberEmailLoginBo {

    /**
     * 邮箱账号 (账号登录必填)
     */
    @NotNull(message = "邮箱账号不能为空")
    private String email;
    /**
     * 账号密码(账号登录必填)
     */
    @NotNull(message = "账号密码不能为空")
    private String password;


}
