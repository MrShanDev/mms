package com.sxpcwlkj.member.enums;

import lombok.Getter;

/**
 * 店铺入驻状态枚举
 */
@Getter
public enum StoreDisableStatusEnum {

    /**
     * 空状态（未申请）
     */
    EMPTY("", "可申请入驻", 0),

    /**
     * 申请中
     */
    APPLYING("APPLYING", "入驻申请提交成功，等待平台审核", 1),

    /**
     * 已开通
     */
    OPEN("OPEN", "您的店铺已经入驻成功，可正常使用！", 2),

    /**
     * 已关闭
     */
    CLOSED("CLOSED", "店铺涉嫌违规运营，已关闭！", 3),

    /**
     * 已拒绝
     */
    REFUSED("REFUSED", "审核未通过,您可重新入驻，如有疑问请联系管理员", 4);

    private final String code;        // 编码（与前端对应）
    private final String description; // 描述
    private final Integer value;      // 状态值

    StoreDisableStatusEnum(String code, String description, Integer value) {
        this.code = code;
        this.description = description;
        this.value = value;
    }

    /**
     * 根据编码获取枚举
     */
    public static StoreDisableStatusEnum getByCode(String code) {
        for (StoreDisableStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return EMPTY;
    }

    /**
     * 根据状态值获取枚举
     */
    public static StoreDisableStatusEnum getByValue(Integer value) {
        for (StoreDisableStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return EMPTY;
    }

    /**
     * 根据描述获取枚举
     */
    public static StoreDisableStatusEnum getByDescription(String description) {
        for (StoreDisableStatusEnum status : values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        return EMPTY;
    }

    /**
     * 检查是否为最终状态（不可再修改）
     */
    public boolean isFinalStatus() {
        return this == OPEN || this == CLOSED;
    }

    /**
     * 检查是否可重新申请
     */
    public boolean canReapply() {
        return this == REFUSED || this == CLOSED;
    }

    /**
     * 检查是否为审核中状态
     */
    public boolean isApplying() {
        return this == APPLYING;
    }

    /**
     * 检查是否已开通
     */
    public boolean isOpen() {
        return this == OPEN;
    }

    /**
     * 获取所有状态的数组（用于下拉选择等场景）
     */
    public static StoreDisableStatusEnum[] getAllStatus() {
        return values();
    }

    /**
     * 验证状态值是否有效
     */
    public static boolean isValidValue(Integer value) {
        for (StoreDisableStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "StoreDisableStatusEnum{" +
            "code='" + code + '\'' +
            ", description='" + description + '\'' +
            ", value=" + value +
            '}';
    }
}
