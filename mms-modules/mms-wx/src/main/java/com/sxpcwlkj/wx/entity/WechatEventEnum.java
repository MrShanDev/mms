package com.sxpcwlkj.wx.entity;

import lombok.Getter;

/**
 * 微信事件枚举
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Getter
public enum WechatEventEnum {
    // 消息事件
    MESSAGE("message"),
    // 关注/取消关注事件
    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unsubscribe"),
    // 扫描带参数二维码事件
    SCAN("SCAN"),
    // 上报地理位置事件
    LOCATION("location"),
    // 自定义菜单事件
    CLICK("CLICK"),
    TEXT("text"),
    VIEW("VIEW");
    // ... 其他事件类型

    private final String eventType;

    WechatEventEnum(String eventType) {
        this.eventType = eventType;
    }

    // 可以添加一个静态方法，根据事件类型字符串返回对应的枚举值
    public static WechatEventEnum fromEventType(String eventType) {
        for (WechatEventEnum event : WechatEventEnum.values()) {
            if (event.getEventType().equalsIgnoreCase(eventType)) {
                return event;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + eventType);
    }
    }
