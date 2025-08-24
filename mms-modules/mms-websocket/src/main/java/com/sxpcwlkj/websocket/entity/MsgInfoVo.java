package com.sxpcwlkj.websocket.entity;

import com.sxpcwlkj.common.utils.JsonUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import lombok.Data;

import java.util.Date;

/**
 * @author mmsAdmin
 * @ClassName Msg
 * @description: 消息对象
 * @date 2024年10月25日
 * @version: 1.0
 */
@Data
public class MsgInfoVo {

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
    private Integer msgType;

    /**
     * 发送人用户
     */
    private MsgUser sendUser;

    /**
     * 接收人用户
     */
    private MsgUser recipientUser;

    /**
     * 聊天室id
     */
    private Long chatRoomId;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 发生时间
     */
    private Date sendTime;
    /**
     * 是否是群发消息
     */
    private Boolean everyone;

    private Boolean self = false;

    @Override
    public String toString() {
        this.sendUser = RedisUtil.getCacheObject(SocketConstant.SOCKET_USER + this.sendUser.getUserId().toString());
        this.recipientUser = RedisUtil.getCacheObject(SocketConstant.SOCKET_USER + this.recipientUser.getUserId().toString());
        return JsonUtil.toJsonString(this);
    }
}
