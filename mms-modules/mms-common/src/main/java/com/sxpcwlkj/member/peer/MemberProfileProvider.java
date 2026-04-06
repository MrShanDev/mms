package com.sxpcwlkj.member.peer;

import java.io.Serializable;

/**
 * 供其它 C 端插件在运行期解析会员展示信息，替代对 {@code mms-plugin-c-member} 的 Maven 依赖。
 * <p>实现类由 <strong>会员域插件</strong>注册到宿主 Spring 容器；依赖方仅依赖本接口（位于 {@code mms-common}）。</p>
 */
public interface MemberProfileProvider {

    /**
     * @param memberId 会员主键
     * @return 展示信息；不存在时返回 {@code null}
     */
    MemberProfileView findViewById(Serializable memberId);
}
