package com.sxpcwlkj.member.peer;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 文档站会话所需的会员快照（与 {@code store_member} 对齐；供 docApi 组装 {@code DocUserVo}）。
 */
public record DocSiteMemberSnapshot(
        String memberId,
        String wxOpenid,
        String nickname,
        String headPortrait,
        Date createdTime,
        String tenantId)
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
