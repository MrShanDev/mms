package com.sxpcwlkj.member.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 接口返回错误码枚举
 */
public enum DefStaticEnum implements IEnum {

    /**
     * 会员默认注册头像
     */
    MEMBER_DEF_HEADER_IMG("MEMBER_DEF_HEADER_IMG","https://tc.z.wiki/autoupload/f/XPEogUuiWaxwD_qii5_MWoNMEw9GhPtHlYmP-O-FHwSyl5f0KlZfm6UsKj-HyTuv/20250927/fjhk/200X200/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.png/webp"),

    ;



    @Getter
    private String key;
    private String value;

    DefStaticEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}

