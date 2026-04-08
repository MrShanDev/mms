package com.sxpcwlkj.plugin.member;

/**
 * 店铺会员主数据对其它插件的访问契约（SPI）。
 * <p>由 {@code mms-plugin-c-member} 注册实现；消费方（如 doc）仅依赖 {@code mms-plugin-api}，
 * 在 Spring 中通过 {@code ObjectProvider<MemberStoreAccess>} 注入，{@code getIfAvailable()} 为空时表示会员插件未加载。</p>
 * <p>与微服务无关：同进程插件间用 Spring Bean 即可；若会员与文档分进程部署，应改为内部 HTTP/OpenAPI 调用。</p>
 */
public interface MemberStoreAccess {

    /** 按微信 openId 查找会员；不存在则创建最小会员行。 */
    MemberStoreSnapshot ensureMemberByWxOpenid(String wxOpenid);

    /** 按会员主键返回只读快照；不存在则实现可返回 {@code null}。 */
    MemberStoreSnapshot getMemberSnapshot(String memberId);

    /** 更新会员城市/地区等展示字段（如 IP 解析结果）；成功返回 {@code true}。 */
    boolean updateMemberCity(String memberId, String cityOrAddressLine);
}
