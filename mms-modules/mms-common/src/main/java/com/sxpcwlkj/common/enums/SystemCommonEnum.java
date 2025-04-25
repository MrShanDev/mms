package com.sxpcwlkj.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */

@Getter
public enum SystemCommonEnum implements IEnum<Integer> {

    /**
     * 超级管理员 ID   code
     */
    SUPER_ADMIN(1, "super_admin","超级管理员ID/CODE"),

    /**
     * 系统状态：启用
     */
    SYS_COMMON_STATE_OPEN(0, "common_state","系统状态-启用/有效"),

    /**
     * 系统状态：禁用
     */
    SYS_COMMON_STATE_CLOSE(1, "common_state","系统状态-关闭/失效"),

    /**
     * 系统默认头像
     */
    SYS_USER_AVATAR(1, "https://sxpcwlkj.oss-cn-beijing.aliyuncs.com/default_avatar.png","系统默认头像");

    private final Integer value;
    @Getter
    private final String code;
    private final String msg;

    SystemCommonEnum(Integer value,String code, String msg) {
        this.value = value;
        this.code = code;
        this.msg = msg;
    }
    @Override
    public Integer getValue() {
        return this.value;
    }

}
