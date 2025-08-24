package com.sxpcwlkj.common.enums;

import lombok.Getter;

/**
 * 脱敏
 * @author: mmsAdmin
 * @date: 2020/11/17 14:44
 * @description:
 */
@Getter
public enum SensitivityTypeEnum {

    /**
     * 姓名
     */
    NAME,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 邮箱
     */
    EMAIL,

    /**
     * 手机号
     */
    PHONE,

    /**
     *  自定义（此项需设置脱敏的前置后置长度）
     */
    CUSTOMER,
}
