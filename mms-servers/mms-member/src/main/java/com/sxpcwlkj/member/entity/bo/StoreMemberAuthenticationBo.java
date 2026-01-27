package com.sxpcwlkj.member.entity.bo;


import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;

import com.sxpcwlkj.member.entity.StoreMemberAuthentication;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
 * 会员认证Bo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = StoreMemberAuthentication.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreMemberAuthenticationBo  extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String id;
    /**
     * 会员ID
     */
    @NotBlank(message = "会员ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String memberId;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String name;
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String number;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String phone;
    /**
     * 身份证正面
     */
    @NotBlank(message = "身份证正面不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String imageFront;
    /**
     * 身份证背面
     */
    @NotBlank(message = "身份证背面不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String imageBack;
    /**
     * 营业执照
     */
    @NotBlank(message = "营业执照不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String businessLicense;
    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String sex;
    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String address;
    /**
     * 生日
     */
    @NotBlank(message = "生日不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String nationality;
}
