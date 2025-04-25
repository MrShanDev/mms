package com.sxpcwlkj.websocket.entity;

import lombok.Data;

/**
 * @author xijue
 * @ClassName MsgUser
 * @description: 用户
 * @date 2024年10月29日
 * @version: 1.0
 */

@Data
public class MsgUser {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 头像
     */
    private String headPortrait;
    /**
     * 名称
     */
    private String name;
    /**
     * SocketSession Id
     */
    private String sessionId;

    private String token;

    private Boolean self=false;

    public MsgUser() {
        this.userId = 0L;
        this.headPortrait = "";
        this.name = "";
        this.sessionId = "";
        this.token = "";
        this.self = false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\" :" + userId +
                ", \"headPortrait\" :\"" + headPortrait + '\"' +
                ", \"name\" :\"" + name + '\"' +
                ", \"sessionId\" :\"" + sessionId + '\"' +
                '}';
    }
}
