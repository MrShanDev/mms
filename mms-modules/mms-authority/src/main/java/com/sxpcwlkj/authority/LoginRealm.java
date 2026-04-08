package com.sxpcwlkj.authority;

import com.sxpcwlkj.common.enums.DeviceEnum;

/**
 * 与 {@link DeviceEnum} 对应：区分<strong>系统管理端用户</strong>（{@code sys_user} 会话）与<strong>会员/文档站</strong>会话，
 * 避免把 {@code LoginObject.getLoginObject(SysUserVo.class)} 与 {@code StoreMemberVo}/{@code DocUserVo} 混用。
 */
public enum LoginRealm {

    /** 管理端 {@code StpUtil} device=ADMIN，Redis {@code admin:}（{@code sys_user} 会话） */
    SYSTEM_ADMIN,

    /** C 端商城 PC，device=PC，Redis {@code pc:member:}，主体为 {@code store_member} */
    STORE_MEMBER_PC,

    /** 移动端会员，device=MOBILE，Redis {@code mobile:member:} */
    STORE_MEMBER_MOBILE,

    /** 文档站扫码/付费，device=DOC，Redis {@code doc:member:}；登录 id 与 {@code store_member.id} 对齐 */
    DOC_STATION;

    public static LoginRealm fromDeviceType(String deviceType) {
        if (deviceType == null || deviceType.isBlank()) {
            return null;
        }
        if (DeviceEnum.ADMIN.getType().equals(deviceType)) {
            return SYSTEM_ADMIN;
        }
        if (DeviceEnum.PC.getType().equals(deviceType)) {
            return STORE_MEMBER_PC;
        }
        if (DeviceEnum.MOBILE.getType().equals(deviceType)) {
            return STORE_MEMBER_MOBILE;
        }
        if (DeviceEnum.DOC.getType().equals(deviceType)) {
            return DOC_STATION;
        }
        return null;
    }
}
