package com.sxpcwlkj.system.entity.bo;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class TenantPackageSaveBo {
    @Size(max=32) private String id;
    @NotBlank @Size(max=100) private String name;
    @NotNull @Min(0) @Max(1) private Integer status;
    @NotEmpty @Size(max=1000) private List<@NotBlank String> functionIds;
}
