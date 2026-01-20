package com.sxpcwlkj.member.entity.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 会员注册BO
 * @Description 会员注册
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Data
public class StoreMemberLoginPhoneBo {

    /**
     * 会员手机号
     */
    @NotBlank(message = "手机号不能为空")
    @NotNull
    private String phone;
    /**
     * 短信验证码
     */
    @NotBlank(message = "短信验证码不能为空")
    @NotNull
    private String smsCode;
    /**
     * 注册邀请码
     */
    private String invitationCode;

}
