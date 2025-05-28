package com.sxpcwlkj.mobile.entity.bo;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


@Data
public class AdvertisingBo {
    @NotEmpty(message = "name不能为空")
    @NotNull(message = "desc不能为空")
    private String code;
}
