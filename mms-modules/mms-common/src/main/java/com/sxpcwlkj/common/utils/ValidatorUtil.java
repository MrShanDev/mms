package com.sxpcwlkj.common.utils;


import com.sxpcwlkj.common.exception.MmsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * ValidatorUtil
 * 验证工具类
 * @author mmsAdmin
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtil {
    private static final Validator VALIDATOR;

    // 手机号正则表达式
    private static final String MOBILE_REGEX = "^1[3-9]\\d{9}$";

    // 邮箱正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // 中文姓名正则表达式
    private static final String CHINESE_NAME_REGEX = "^[\\u4e00-\\u9fa5]{2,10}$";

    // 英文姓名正则表达式
    private static final String ENGLISH_NAME_REGEX = "^[a-zA-Z\\s]{2,50}$";

    // 银行卡号正则表达式（16-19位数字）
    private static final String BANK_CARD_REGEX = "^\\d{16,19}$";

    // 纯数字正则表达式
    private static final String PURE_NUMBER_REGEX = "^\\d+$";

    // 小数正则表达式
    private static final String DECIMAL_REGEX = "^\\d+\\.\\d+$";

    // 身份证号正则表达式
    private static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    // 车牌号正则表达式
    private static final String LICENSE_PLATE_REGEX = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4}[A-Z0-9挂学警港澳]$";

    // IP地址正则表达式
    private static final String IP_REGEX = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    // URL正则表达式
    private static final String URL_REGEX = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 验证实体对象
     * @param object 待验证对象
     * @param groups 验证组
     */
    public static void validateEntity(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> validate = VALIDATOR.validate(object, groups);
        List<String> list = new ArrayList<>();
        for(ConstraintViolation<Object> aa :validate) {
            throw new MmsException(aa.getMessage());
        }
    }

    /**
     * 验证手机号
     * @param mobile 手机号
     * @return true-有效，false-无效
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(MOBILE_REGEX, mobile.trim());
    }

    /**
     * 验证邮箱
     * @param email 邮箱地址
     * @return true-有效，false-无效
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, email.trim());
    }

    /**
     * 验证中文姓名
     * @param name 姓名
     * @return true-有效，false-无效
     */
    public static boolean isChineseName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(CHINESE_NAME_REGEX, name.trim());
    }

    /**
     * 验证英文姓名
     * @param name 姓名
     * @return true-有效，false-无效
     */
    public static boolean isEnglishName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(ENGLISH_NAME_REGEX, name.trim());
    }

    /**
     * 验证姓名（中文或英文）
     * @param name 姓名
     * @return true-有效，false-无效
     */
    public static boolean isName(String name) {
        return isChineseName(name) || isEnglishName(name);
    }

    /**
     * 验证银行卡号
     * @param bankCard 银行卡号
     * @return true-有效，false-无效
     */
    public static boolean isBankCard(String bankCard) {
        if (bankCard == null || bankCard.trim().isEmpty()) {
            return false;
        }
        String card = bankCard.trim().replaceAll("\\s", "");
        return Pattern.matches(BANK_CARD_REGEX, card) && isValidBankCardByLuhn(card);
    }

    /**
     * 使用Luhn算法验证银行卡号
     * @param cardNumber 银行卡号
     * @return true-有效，false-无效
     */
    private static boolean isValidBankCardByLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    /**
     * 验证是否为纯数字
     * @param str 字符串
     * @return true-是纯数字，false-不是纯数字
     */
    public static boolean isPureNumber(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(PURE_NUMBER_REGEX, str.trim());
    }

    /**
     * 验证是否为小数
     * @param str 字符串
     * @return true-是小数，false-不是小数
     */
    public static boolean isDecimal(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(DECIMAL_REGEX, str.trim());
    }

    /**
     * 验证是否为数字（整数或小数）
     * @param str 字符串
     * @return true-是数字，false-不是数字
     */
    public static boolean isNumber(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证身份证号
     * @param idCard 身份证号
     * @return true-有效，false-无效
     */
    public static boolean isIdCard(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        String id = idCard.trim().toUpperCase();
        if (!Pattern.matches(ID_CARD_REGEX, id)) {
            return false;
        }
        return isValidIdCardChecksum(id);
    }

    /**
     * 验证身份证号校验位
     * @param idCard 身份证号
     * @return true-有效，false-无效
     */
    private static boolean isValidIdCardChecksum(String idCard) {
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checksums = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Character.getNumericValue(idCard.charAt(i)) * weights[i];
        }

        char checksum = checksums[sum % 11];
        return checksum == idCard.charAt(17);
    }

    /**
     * 验证车牌号
     * @param licensePlate 车牌号
     * @return true-有效，false-无效
     */
    public static boolean isLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(LICENSE_PLATE_REGEX, licensePlate.trim());
    }

    /**
     * 验证IP地址
     * @param ip IP地址
     * @return true-有效，false-无效
     */
    public static boolean isIpAddress(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(IP_REGEX, ip.trim());
    }

    /**
     * 验证URL
     * @param url URL地址
     * @return true-有效，false-无效
     */
    public static boolean isUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(URL_REGEX, url.trim());
    }

    /**
     * 验证字符串长度
     * @param str 字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return true-长度有效，false-长度无效
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) {
            return minLength <= 0;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * 验证字符串是否为空或null
     * @param str 字符串
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 验证字符串是否不为空
     * @param str 字符串
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
