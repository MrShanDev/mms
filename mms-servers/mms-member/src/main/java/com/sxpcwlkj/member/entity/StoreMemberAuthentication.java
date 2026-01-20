package com.sxpcwlkj.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.common.annotation.SensitivityEncrypt;
import com.sxpcwlkj.common.enums.SensitivityTypeEnum;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mms
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("store_member_authentication")
public class StoreMemberAuthentication extends BaseEntity {

    /** ID */
    @TableId
    private String id ;
    /** 会员ID */
    private String memberId ;
    /** 姓名 */
    @SensitivityEncrypt(type = SensitivityTypeEnum.NAME)
    private String name ;
    /** 身份证号 */
    @SensitivityEncrypt(type = SensitivityTypeEnum.ID_CARD)
    private String number ;
    /** 手机号 */
    private String phone ;
    /** 身份证正面 */
    private String imageFront ;
    /** 身份证背面 */
    private String imageBack ;
    private String sex;
    private String address;
    private String nationality;
}
