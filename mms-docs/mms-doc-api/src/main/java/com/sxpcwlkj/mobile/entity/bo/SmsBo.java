package com.sxpcwlkj.mobile.entity.bo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class SmsBo {

    @NotBlank(message = "手机号不能为空")
    private String phone;
    /**
     * 1:注册 2:登录 3:修改密码 4:支付密码 5:更换手机号
     */
    @NotBlank(message = "类型不能为空")
    private Integer type;
}
