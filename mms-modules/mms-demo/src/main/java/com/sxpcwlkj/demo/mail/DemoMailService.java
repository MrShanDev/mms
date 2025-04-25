package com.sxpcwlkj.demo.mail;

public interface DemoMailService {

    /**
     * 验证码邮件发送
     */
    Boolean sendMail(DemoMailEntity demoMailEntity);

}
