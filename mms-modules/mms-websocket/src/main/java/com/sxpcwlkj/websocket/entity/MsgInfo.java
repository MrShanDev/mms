package com.sxpcwlkj.websocket.entity;

import com.sxpcwlkj.common.utils.JsonUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.enums.MsgEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author xijue
 * @ClassName Msg
 * @description: 消息对象
 * @date 2024年10月25日
 * @version: 1.0
 */
@Data
public class MsgInfo {
    /**
     * 接收聊天室id
     */
    private Long chatRoomId;
    /**
     * 是否是群发消息
     */
    private Boolean everyone = false;
    /**
     * // 系统消息
     * SYSTEM = 0,
     * // 普通文本
     * TXT = 1,
     * // html文本
     * HTML = 2,
     * // 图片Path
     * IMAGE = 3,
     * // 文件Path
     * FILE = 4,
     * // 音频Path
     * AUDIO = 5,
     * // 视频Path
     * VIDEO = 6,
     * // 打电话
     * CALL_PHONE_VIDEO = 7
     */
    private Integer msgType = 1;
    /**
     * 发送人用户
     */
    private MsgUser sendUser;
    /**
     * 接收人用户
     */
    private MsgUser recipientUser;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 发生时间
     */
    private Date sendTime;

    public MsgInfo(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
        this.sendTime = new Date();
    }

    public MsgInfo setUse(Long sendUseId, Long recipientUserId) {
        this.sendUser= RedisUtil.getCacheObject(SocketConstant.SOCKET_USER+sendUseId);
        this.recipientUser= RedisUtil.getCacheObject(SocketConstant.SOCKET_USER+recipientUserId);
        return this;
    }

    /**
     * 设置消息类型
     *
     * @param msgEnum 消息类型
     * @return 消息对象
     */
    public MsgInfo setMsgType(MsgEnum msgEnum) {
        this.msgType = msgEnum.getValue();
        this.sendTime = new Date();
        return this;
    }

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     * @return 消息对象
     */
    public MsgInfo setMsgContent(String content) {
        this.msgContent = content;
        this.sendTime = new Date();
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
