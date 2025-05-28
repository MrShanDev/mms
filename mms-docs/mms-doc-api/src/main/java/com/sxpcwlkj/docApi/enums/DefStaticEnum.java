package com.sxpcwlkj.docApi.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 接口返回错误码枚举
 */
public enum DefStaticEnum implements IEnum {

    /**
     * 会员默认注册头像
     */
    MEMBER_DEF_HEADER_IMG("MEMBER_DEF_HEADER_IMG","https://jifugou.oss-cn-zhangjiakou.aliyuncs.com/01_default/defHeadImg.png"),

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

