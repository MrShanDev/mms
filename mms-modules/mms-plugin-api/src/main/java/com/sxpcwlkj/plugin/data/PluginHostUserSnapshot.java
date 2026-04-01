package com.sxpcwlkj.plugin.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 插件可见的登录用户快照（无宿主 Entity，仅基础字段）。
 */
@Getter
@EqualsAndHashCode
@ToString
public final class PluginHostUserSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String userId;
    private final String userName;
    private final String nickName;
    private final String tenantId;

    public PluginHostUserSnapshot(String userId, String userName, String nickName, String tenantId) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.tenantId = tenantId;
    }
}
