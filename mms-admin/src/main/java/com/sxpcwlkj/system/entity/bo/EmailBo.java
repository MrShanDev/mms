package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 发生邮件bo
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Data
public class EmailBo {

    @NotBlank(message = "邮箱不能为空",groups = {ValidatedGroupConfig.query.class,ValidatedGroupConfig.del.class})
    private String email;
    /**
     * 1:注册 2:登录 3:修改密码 4:支付密码 5:更换手机号
     */
    @NotBlank(message = "类型不能为空",groups = {ValidatedGroupConfig.insert.class})
    private Integer type;

    @NotBlank(message = "验证码不能为空",groups = {ValidatedGroupConfig.update.class})
    private String code;

}
