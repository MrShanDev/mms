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
public class StoreMemberSetPasswordBo {

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


}
