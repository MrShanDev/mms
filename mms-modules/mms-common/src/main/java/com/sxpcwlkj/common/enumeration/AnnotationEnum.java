package com.sxpcwlkj.common.enumeration;

/**
 * 注解枚举
 *
 * @name: AnnotationEnum
 * @author: mmsAdmin
 * @date: 2022/11/30
 **/

public class AnnotationEnum {


    public enum Object {
        后端管理端("admin"), 商户管理端("merchant"), App端("app"), 小程序端("applet");

        private  String value;

        Object(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
