package com.sxpcwlkj.member.distribution;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会员注册完成后通知分销插件的上下文（邀请码为「上级分享码」原文，非会员自身 invitation_code）。
 */
public record MemberDistributionRegisterRequest(
        String newMemberId,
        /** 上级会员 id；邀请码无效或未解析到时为 null */
        String referrerMemberId,
        /** 用户填写的邀请码原文（trim 后） */
        String inviteCodeInput,
        MemberDistributionRegisterChannel channel,
        /** 分享落地路径或场景标识，可选 */
        String sharePath,
        /** 请求路径，可选 */
        String requestPath,
        String tenantId)
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
