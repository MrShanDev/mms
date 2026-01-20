package com.sxpcwlkj.member.utils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * 优惠券码生成工具类
 */
public class CouponCodeGeneratorUtil {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String LETTERS_NUMBERS = LETTERS + NUMBERS;
    private static final SecureRandom random = new SecureRandom();

    /**
     * 生成优惠券码
     * @param length 优惠券码长度
     * @param prefix 前缀
     * @param includeLetters 是否包含字母
     * @param includeNumbers 是否包含数字
     * @param pattern 模式，如 "XXXX-XXXX-XXXX"
     * @return 优惠券码
     */
    public static String generateCouponCode(int length, String prefix,
                                            boolean includeLetters,
                                            boolean includeNumbers,
                                            String pattern) {
        String charset = getCharset(includeLetters, includeNumbers);

        if (pattern != null && !pattern.isEmpty()) {
            return generateByPattern(pattern, charset, prefix);
        } else {
            return generateRandom(length, charset, prefix);
        }
    }

    /**
     * 简化版本 - 生成标准优惠券码
     * @return 12位优惠券码
     */
    public static String generateCouponCode() {
        return generateCouponCode(12, "", true, true, null);
    }

    /**
     * 生成指定格式的优惠券码
     * @param pattern 格式，如 "XXXX-XXXX-XXXX"
     * @return 优惠券码
     */
    public static String generateCouponCode(String pattern) {
        return generateCouponCode(0, "", true, true, pattern);
    }

    /**
     * 批量生成优惠券码（确保不重复）
     * @param count 生成数量
     * @param options 生成选项
     * @return 优惠券码集合
     */
    public static Set<String> generateBatchCouponCodes(int count, CouponOptions options) {
        Set<String> codes = new HashSet<>();
        int maxAttempts = count * 100; // 最大尝试次数

        while (codes.size() < count) {
            String code = generateCouponCode(
                options.getLength(),
                options.getPrefix(),
                options.isIncludeLetters(),
                options.isIncludeNumbers(),
                options.getPattern()
            );

            codes.add(code);

            if (codes.size() >= maxAttempts) {
                throw new RuntimeException("生成优惠券码失败，可能字符组合太少");
            }
        }

        return codes;
    }

    /**
     * 生成带校验位的优惠券码
     * @param baseLength 基础长度
     * @return 带校验位的优惠券码
     */
    public static String generateCouponCodeWithChecksum(int baseLength) {
        String baseCode = generateCouponCode(baseLength, "", true, true, null);
        char checksum = calculateChecksum(baseCode);
        return baseCode + checksum;
    }

    // 私有方法
    private static String getCharset(boolean includeLetters, boolean includeNumbers) {
        if (includeLetters && includeNumbers) {
            return LETTERS_NUMBERS;
        } else if (includeLetters) {
            return LETTERS;
        } else if (includeNumbers) {
            return NUMBERS;
        } else {
            return NUMBERS; // 默认使用数字
        }
    }

    private static String generateRandom(int length, String charset, String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charset.length());
            sb.append(charset.charAt(index));
        }
        return sb.toString();
    }

    private static String generateByPattern(String pattern, String charset, String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == 'X' || c == 'x') {
                int index = random.nextInt(charset.length());
                sb.append(charset.charAt(index));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static char calculateChecksum(String code) {
        int sum = 0;
        for (char c : code.toCharArray()) {
            sum += c;
        }
        int checksumValue = sum % 36;
        if (checksumValue < 10) {
            return (char) ('0' + checksumValue);
        } else {
            return (char) ('A' + (checksumValue - 10));
        }
    }

    /**
     * 优惠券生成选项类
     */
    public static class CouponOptions {
        private int length = 12;
        private String prefix = "";
        private boolean includeLetters = true;
        private boolean includeNumbers = true;
        private String pattern;

        // 构造器
        public CouponOptions() {}

        public CouponOptions(int length, String prefix) {
            this.length = length;
            this.prefix = prefix;
        }

        // Getter和Setter
        public int getLength() { return length; }
        public void setLength(int length) { this.length = length; }

        public String getPrefix() { return prefix; }
        public void setPrefix(String prefix) { this.prefix = prefix; }

        public boolean isIncludeLetters() { return includeLetters; }
        public void setIncludeLetters(boolean includeLetters) { this.includeLetters = includeLetters; }

        public boolean isIncludeNumbers() { return includeNumbers; }
        public void setIncludeNumbers(boolean includeNumbers) { this.includeNumbers = includeNumbers; }

        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
    }

    /**
     * 常用优惠券码格式生成器
     */
    public static class StandardFormats {
        // 标准格式：XXXX-XXXX-XXXX
        public static String standard() {
            return generateCouponCode("XXXX-XXXX-XXXX");
        }

        // 数字格式：0000-0000-0000
        public static String numeric() {
            return generateCouponCode(0, "", false, true, "0000-0000-0000");
        }

        // 促销格式：SAVE10-XXXXXX
        public static String promotional() {
            return generateCouponCode(6, "SAVE10-", true, true, null);
        }

        // 会员格式：VIP-XXXX-XXXX
        public static String vip() {
            return generateCouponCode(0, "VIP-", true, true, "XXXX-XXXX");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 基本使用 ===");
        System.out.println("12位随机码: " + CouponCodeGeneratorUtil.generateCouponCode());
        System.out.println("8位数字码: " + CouponCodeGeneratorUtil.generateCouponCode(8, "", false, true, null));
        System.out.println("带前缀: " + CouponCodeGeneratorUtil.generateCouponCode(10, "COUPON_", true, true, null));
        System.out.println("模式生成: " + CouponCodeGeneratorUtil.generateCouponCode("XXXX-XXXX-XXXX"));

        System.out.println("\n=== 标准格式 ===");
        System.out.println("标准格式: " + StandardFormats.standard());
        System.out.println("数字格式: " + StandardFormats.numeric());
        System.out.println("促销格式: " + StandardFormats.promotional());
        System.out.println("VIP格式: " + StandardFormats.vip());

        System.out.println("\n=== 批量生成 ===");
        CouponOptions options = new CouponOptions();
        options.setLength(8);
        options.setIncludeLetters(true);
        options.setIncludeNumbers(true);

        Set<String> batchCodes = CouponCodeGeneratorUtil.generateBatchCouponCodes(5, options);
        System.out.println("批量生成: " + batchCodes);

        System.out.println("\n=== 带校验位 ===");
        System.out.println("带校验位: " + CouponCodeGeneratorUtil.generateCouponCodeWithChecksum(11));
    }
}
