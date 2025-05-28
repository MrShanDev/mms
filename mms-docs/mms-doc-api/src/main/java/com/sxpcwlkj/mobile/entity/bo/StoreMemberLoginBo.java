package com.sxpcwlkj.mobile.entity.bo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.validation.constraints.Size;

/**
 * 会员登录BO
 * @Description 会员登录
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Data
public class StoreMemberLoginBo {

    /**
     * 会员账号
     */
    //@Deprecated

    private String account;
    /**
     * 会员手机号/账号
     */
    @NotBlank(message = "会员手机号/账号不能为空")
    private String phone;
    /**
     * 短信验证码(手机号登录必填)
     */
    private String smsCode;
    /**
     * 账号密码(账号登录必填)
     */
    @NotBlank(message = "账号密码不能为空")
    private String password;
    /**
     * 注册邀请码
     */
    private String invitationCode;

}
