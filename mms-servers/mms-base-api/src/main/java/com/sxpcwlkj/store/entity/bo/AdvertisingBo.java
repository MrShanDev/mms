package com.sxpcwlkj.store.entity.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AdvertisingBo {
    @NotEmpty(message = "name不能为空")
    @NotNull(message = "desc不能为空")
    private String code;
}
