package com.sxpcwlkj.member.entity.vo;

import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.member.entity.StoreMemberAuthentication;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;

import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
 * 会员认证Vo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */

@Data
@AutoMapper(target = StoreMemberAuthentication.class)
@EqualsAndHashCode(callSuper=false)
public class StoreMemberAuthenticationVo  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;
    /**
     * 会员ID
     */
    private String memberId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String number;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 身份证正面
     */
    private String imageFront;
    /**
     * 身份证背面
     */
    private String imageBack;
    /**
     * 营业执照
     */
    private String businessLicense;
    /**
     * 性别
     */
    private String sex;
    /**
     * 地址
     */
    private String address;
    /**
     * 生日
     */
    private String nationality;

}
