package com.sxpcwlkj.websocket.entity;

import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.websocket.enums.CmdEnum;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author mmsAdmin
 * @ClassName SendMsg
 * @description: 响应对象
 * @date 2024年10月25日
 * @version: 1.0
 */

@Data
public class DataMsg {

    /**
     * 通信命令
     */
    private Integer cmd;

    /**
     * 消息内容
     */
    private Object data;

    /**
     * 初始化构造器
     *
     * @param cmd cmd
     */
    public DataMsg(CmdEnum cmd) {
        this.cmd = cmd.getValue();
    }

    /**
     * 设置Data
     *
     * @param data 内容字符串类型
     * @return 响应对象
     */
    public String setData(String data) {
        this.data = data;
        return buildResponse();
    }

    /**
     * 设置Data
     *
     * @param data 任意对象类型
     * @return 响应对象
     */
    public String setData(Object data) {
        this.data = data;
        return buildResponse();
    }

    /**
     * 设置Data
     *
     * @param msgInfo 消息类型
     * @return 响应对象
     */
    public String setData(MsgInfo msgInfo) {
        this.data = msgInfo;
        return buildResponse();
    }

    /**
     * 设置Data
     *
     * @param chatRoomList 聊天室类型
     * @return 响应对象
     */
    public String setData(List<ChatRoom> chatRoomList) {
        this.data = chatRoomList;
        return buildResponse();
    }
    
    /**
     * 设置Data
     *
     * @param stringSet 字符串集合类型
     * @return 响应对象
     */
    public String setData(Set<String> stringSet) {
        this.data = stringSet;
        return buildResponse();
    }

    /**
     * 构建响应JSON字符串
     * @return JSON字符串
     */
    private String buildResponse() {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("cmd", this.cmd);
        response.put("data", this.data);
        return JSONUtil.toJsonStr(response);
    }
}
