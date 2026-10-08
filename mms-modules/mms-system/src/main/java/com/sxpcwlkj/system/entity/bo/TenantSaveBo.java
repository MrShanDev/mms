package com.sxpcwlkj.system.entity.bo;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

@Data
public class TenantSaveBo {
    @NotBlank @Pattern(regexp="[a-z0-9][a-z0-9-]{2,31}")
    private String tenantId;
    @NotBlank @Size(max=100) private String name;
    @NotNull @Min(0) @Max(1) private Integer status;
    @Size(max=100) private String contactName;
    @Size(max=30) private String contactMobile;
    @Size(max=200) private String domain;
    @Size(max=32) private String packageId;
    private Date expireTime;
    @NotNull @Min(1) @Max(1000000) private Integer accountCount;
    @Size(max=255) private String remark;
    @Size(min=3,max=64) private String adminUsername;
    @Size(min=8,max=128) private String adminPassword;
}
