package com.sxpcwlkj.demo.mail;

import lombok.Data;

@Data
public class DemoMailEntity {

    /**
     * 验证码
     */
    private String code;
    /**
     * 邮箱地址
     */
    private String mailAddress;
    /**
     * 邮件标题
     */
    private String mailTitle;
    /**
     * 邮件内容
     */
    private String mailContent;


}
