package com.sxpcwlkj.system.entity.bo;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantQueryBo extends PageQuery {
    private String keyword;
    private Integer status;
}
