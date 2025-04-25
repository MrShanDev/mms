package com.sxpcwlkj.common.utils;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 计算密码强度
 * @author xijue
 * @Doc mmsadmin.cn
 */
public class PasswordStrengthCheckerUtil {

    // 枚举类型，表示密码强度等级
    public enum Strength implements IEnum<String> {
        VERY_WEAK("很弱"),
        WEAK("一般"),
        MEDIUM("中等"),
        STRONG("强"),
        VERY_STRONG("很强");

        private String value;

        Strength(String value) {
            this.value = value;
        }
        @Override
        public String getValue() {
            return this.value;
        }
    }

    // 检查密码强度的方法
    public static String checkStrength(String password) {
        int length = password.length();
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // 遍历密码中的每个字符
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }

        // 根据条件确定密码强度
        if (length < 6) {
            return Strength.VERY_WEAK.getValue();
        } else if (length < 8) {
            if (hasUppercase || hasLowercase || hasDigit || hasSpecialChar) {
                return Strength.WEAK.getValue();
            } else {
                return Strength.VERY_WEAK.getValue();
            }
        } else if (length < 12) {
            int criteriaMet = (hasUppercase ? 1 : 0) +
                (hasLowercase ? 1 : 0) +
                (hasDigit ? 1 : 0) +
                (hasSpecialChar ? 1 : 0);
            if (criteriaMet == 1) {
                return Strength.WEAK.getValue();
            } else if (criteriaMet == 2) {
                return Strength.MEDIUM.getValue();
            } else {
                return Strength.STRONG.getValue();
            }
        } else {
            int criteriaMet = (hasUppercase ? 1 : 0) +
                (hasLowercase ? 1 : 0) +
                (hasDigit ? 1 : 0) +
                (hasSpecialChar ? 1 : 0);
            if (criteriaMet == 2) {
                return Strength.MEDIUM.getValue();
            } else if (criteriaMet == 3) {
                return Strength.STRONG.getValue();
            } else {
                return Strength.VERY_STRONG.getValue();
            }
        }
    }

    // 主方法，用于测试
    public static void main(String[] args) {
        String[] testPasswords = {
            "abc",
            "Abc123",
            "abcdef",
            "ABCDEF",
            "abcdef123456",
            "ABCDEF!@#$%"
        };

        for (String password : testPasswords) {
            System.out.println("Password: " + password + " Strength: " + checkStrength(password));
        }
    }
}
