package com.sxpcwlkj.websocket.entity;

import com.sxpcwlkj.websocket.enums.CmdEnum;
import lombok.Data;

import java.util.List;

/**
 * @author xijue
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
    private String data;

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
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"cmd\": ").append(this.cmd).append(", ");
        sb.append("\"data\": \"").append(this.data).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 设置Data
     *
     * @param msgInfo 消息类型
     * @return 响应对象
     */
    public String setData(MsgInfo msgInfo) {
        this.data = data;
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"cmd\": ").append(this.cmd).append(", ");
        sb.append("\"data\": ").append(this.data);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 设置Data
     *
     * @param chatRoomList 聊天室类型
     * @return 响应对象
     */
    public String setData(List<ChatRoom> chatRoomList) {
        this.data = chatRoomListToString(chatRoomList);
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"cmd\": ").append(this.cmd).append(", ");
        sb.append("\"data\": ").append(this.data);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 聊天室列表 toString
     *
     * @param chatRoomList 聊天室列表
     * @return 消息
     */
    public static String chatRoomListToString(List<ChatRoom> chatRoomList) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < chatRoomList.size(); i++) {
            sb.append(chatRoomList.get(i).build());
            if (i < chatRoomList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
