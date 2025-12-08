package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 发生短信bo
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
public class SysSmsBo {

    @NotBlank(message = "手机号不能为空",groups = {ValidatedGroupConfig.query.class,ValidatedGroupConfig.del.class})
    private String phone;
    /**
     * 1:绑定邮箱
     */
    @NotBlank(message = "类型不能为空",groups = {ValidatedGroupConfig.insert.class})
    private Integer type;

    @NotBlank(message = "验证码不能为空",groups = {ValidatedGroupConfig.update.class})
    private String code;

}
