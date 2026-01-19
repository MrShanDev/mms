package com.sxpcwlkj.store.enums;

/**
 * 付款类型枚举 - 按编码映射
 */
public enum PaymentTypeEnum {

    /**
     * 余额支付
     */
    BALANCE(0, "BALANCE", "余额支付"),

    /**
     * 微信支付
     */
    WECHAT(1, "WECHAT", "微信支付"),

    /**
     * 支付宝支付
     */
    ALIPAY(2, "ALIPAY", "支付宝支付"),

    /**
     * 银行卡快捷支付
     */
    BANK_QUICK(3, "BANK_QUICK", "银行卡快捷支付");

    private final int code;
    private final String name;
    private final String description;

    PaymentTypeEnum(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据编码获取枚举实例
     */
    public static PaymentTypeEnum getByCode(int code) {
        for (PaymentTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的支付类型编码: " + code);
    }

    /**
     * 根据名称获取枚举实例
     */
    public static PaymentTypeEnum getByName(String name) {
        for (PaymentTypeEnum type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的支付类型名称: " + name);
    }

    /**
     * 检查编码是否有效
     */
    public static boolean isValidCode(int code) {
        for (PaymentTypeEnum type : values()) {
            if (type.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return description + "(" + code + ")";
    }
}
