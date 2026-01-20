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
public class StoreMemberLoginBo {

    /**
     * 会员账号 (账号登录必填)
     */
    private String account;
    /**
     * 会员手机号（短信登陆必填）
     */
    private String phone;
    /**
     * 短信验证码(手机号登录必填)
     */
    private String smsCode;
    /**
     * 账号密码(账号登录必填)
     */
    private String password;
    /**
     * 注册邀请码（选填）
     */
    private String invitationCode;
    /**
     *  登录类型（1：账号登录 2：短信登录）
     *  @default 1
     */
    @NotNull(message = "登录类型不能为空")
    private Integer type;

}
