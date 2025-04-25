package com.sxpcwlkj.common.code.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码 枚举
 */
@Getter
@AllArgsConstructor
public class CaptchaEntity {

    public enum CaptchaType{

        线段干扰("LINE"),
        圆圈干扰("CIRCLE"),
        扭曲干扰("SHEAR"),
        计算题型("CALCULATE");
        private final String value;

        CaptchaType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }


    }


    public enum TYPE {
        系统注册(0),
        系统登录(1),
        系统忘记密码(2);

        private final int value;

        TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum CODE {
        CAPTCHA("captcha"),
        CAPTCHA_VALUE("captcha_value"),
        CAPTCHA_KEY("captcha_key"),
        CAPTCHA_MSG("captcha_msg"),
        UUID("uuid"),
        TYPE("type"),


        CAPTCHA_KEY_LOGIN("captcha_key_login_"),
        CAPTCHA_KEY_REGISTER("captcha_key_register_"),
        CAPTCHA_KEY_PASSWORD("captcha_key_password_");
        private final String value;

        CODE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }


}
