package com.sxpcwlkj.websocket.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author mmsAdmin
 */

public enum ChatRoomTypeEnum implements IEnum {

    // 私人，一对一
    COMMON(1),
    // 群组 多对多
    SYSTEM(2);


    private final Integer value;

    ChatRoomTypeEnum(Integer type) {
        this.value = type;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

}
