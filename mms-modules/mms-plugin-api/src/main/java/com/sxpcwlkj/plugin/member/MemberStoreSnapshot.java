package com.sxpcwlkj.plugin.member;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * {@code store_member} 跨插件只读视图（定义在 plugin-api，避免消费方依赖会员 JAR）。
 *
 * @param memberId     会员主键，对应 {@code store_member.id}
 * @param wxOpenid     微信 openId，无则可为空
 * @param nickname     昵称
 * @param headPortrait 头像 URL
 * @param tenantId     租户 ID
 * @param createdTime  创建时间
 */
public record MemberStoreSnapshot(
        String memberId,
        String wxOpenid,
        String nickname,
        String headPortrait,
        String tenantId,
        Date createdTime)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
