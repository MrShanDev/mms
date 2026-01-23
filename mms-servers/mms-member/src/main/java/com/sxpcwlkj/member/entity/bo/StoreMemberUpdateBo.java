package com.sxpcwlkj.member.entity.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class StoreMemberUpdateBo {

    /**
     * 操作类型 1:手机号 2:密码 3:昵称 4:头像 5:性别 6：账号 7:生日 8:背景图
     */
    @NotBlank
    @NotNull
    private Integer type;


    private String memberId;

    /**
     * 昵称 type=3 必传
     */
    private String nickname;
    /**
     * 账号  type=6 必传
     */
    private String account;
    /**
     * 性别  type=5 必传
     */
    private Integer sex;
    /**
     * 手机号  type=1 必传
     */
    private String phone;
    /**
     * 密码  type=2 必传
     */
    private String password;
    /**
     * 头像  type=4 必传
     */
    private String headPortrait;
    /**
     * 短信验证码 type=1 必传
     */
    private String smsCode;
    /**
     * 生日  type=7 必传
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date birthday;

    /**
     * 个人中心背景图 type=8 必传
     */
    private String memberBgImg;

}
