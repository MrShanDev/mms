package com.sxpcwlkj.member.peer;

/**
 * 文档站用户与 {@code store_member} 的桥接：实现由 <strong>mms-plugin-c-member</strong> 注册到宿主容器；
 * doc 插件仅依赖本接口（位于 {@code mms-common}），不直接依赖会员插件 Maven 模块。
 */
public interface MemberDocBridge {

    /**
     * 按微信 openId 查找会员；不存在则创建一条极简会员记录后返回。
     */
    DocSiteMemberSnapshot ensureByWxOpenid(String wxOpenid);

    /**
     * @param memberId {@code store_member.id}
     */
    DocSiteMemberSnapshot getByMemberId(String memberId);

    /**
     * 文档站登录成功后更新会员侧展示字段（如解析得到的地区/IP 等）。
     *
     * @param memberId {@code store_member.id}
     * @param addressLine 登录地址或 IP 解析结果等单行文本
     * @return 是否更新成功
     */
    boolean touchDocLogin(String memberId, String addressLine);
}
