package com.sxpcwlkj.member.peer;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 跨 C 端插件只读展示的会员展示信息（昵称、头像等），避免业务插件间 Maven 依赖对方实现 JAR。
 * 由会员域插件实现 {@link MemberProfileProvider} 提供数据。
 */
@Data
public class MemberProfileView implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nickname;
    private String headPortrait;
}
