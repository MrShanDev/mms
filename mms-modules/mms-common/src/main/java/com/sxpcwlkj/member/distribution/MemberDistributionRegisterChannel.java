package com.sxpcwlkj.member.distribution;

/**
 * 会员注册渠道（用于分销插件记录归因）。
 */
public enum MemberDistributionRegisterChannel {
    /** 短信验证码注册/登录一体 */
    SMS_CODE,
    /** 手机号 + 短信 + 密码注册 */
    PHONE_PASSWORD,
    /** 邮箱 + 邮箱验证码 + 密码注册 */
    EMAIL_CODE,
    /** 微信小程序：code + 手机号授权注册 */
    WECHAT_MINI_PHONE,
    /** 微信公众号 OAuth 扫码等注册 */
    WECHAT_MP_OAUTH,
    OTHER
}
