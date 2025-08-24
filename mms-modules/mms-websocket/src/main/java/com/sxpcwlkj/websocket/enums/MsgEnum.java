package com.sxpcwlkj.websocket.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author mmsAdmin
 */

public enum MsgEnum implements IEnum {

    // 系统消息
    SYSTEM(0),
    // 普通文本
    TXT(1),
    // html文本
    HTML(2),
    // 图片Path
    IMAGE(3),
    // 文件Path
    FILE(4),
    // 音频Path
    AUDIO(5),
    // 视频Path
    VIDEO(6),
    // 打电话
    CALL_PHONE_VIDEO(7);
    private final Integer value;

    MsgEnum(Integer type) {
        this.value = type;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

}
