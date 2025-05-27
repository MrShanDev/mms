package com.sxpcwlkj.doc.enums;

/**
 * 订单状态枚举
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
public enum OrderStatus {
    UNPAID("unpaid", "待支付"),
    PAYSUC("paysuc", "已支付"),
    REFUND("refund", "已退款"),
    CANCEL("cancel", "已取消"),
    FINISH("finish", "已完成");

    private final String value;
    private final String label;

    OrderStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabelByValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equals(value)) {
                return status.label;
            }
        }
        return "";
    }

    // Getters
    public String getValue() { return value; }
    public String getLabel() { return label; }
}
