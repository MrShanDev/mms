package com.sxpcwlkj.websocket.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum CmdEnum implements IEnum {

    /**
     * 心跳检测
     */
    SYS_PING(0),
    /**
     * 连接成功
     */
    SUCCEED(1),
    /**
     * 获取在线用户列表
     */
    USER_LIST(100001),

    /**
     * 发送消息
     */
    SEND_MSG(100002);



    private final Integer value;

    CmdEnum(Integer cmd) {
        this.value = cmd;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

}
