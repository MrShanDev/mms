package com.sxpcwlkj.member.distribution;

import java.io.Serial;
import java.io.Serializable;

/** 分销插件处理注册钩子后的结果。 */
public record MemberDistributionRegisterResult(boolean processed, boolean referrerBound, String message)
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static MemberDistributionRegisterResult noop() {
        return new MemberDistributionRegisterResult(false, false, null);
    }

    public static MemberDistributionRegisterResult bound(String message) {
        return new MemberDistributionRegisterResult(true, true, message);
    }

    public static MemberDistributionRegisterResult unbound(String message) {
        return new MemberDistributionRegisterResult(true, false, message);
    }
}
