package com.sxpcwlkj.member.distribution;

/**
 * 会员注册完成后的分销钩子：实现由 <strong>mms.plugin.c-distribution</strong> 注册到宿主容器；
 * 开放 API（如 mms-open-api）仅依赖本接口，不 Maven 依赖分销插件 JAR。
 */
public interface MemberDistributionHook {

    /**
     * 新会员已写入 {@code store_member} 后调用；仅当请求中携带邀请码时应由调用方传入 {@link MemberDistributionRegisterRequest}。
     */
    MemberDistributionRegisterResult onMemberRegistered(MemberDistributionRegisterRequest request);
}
