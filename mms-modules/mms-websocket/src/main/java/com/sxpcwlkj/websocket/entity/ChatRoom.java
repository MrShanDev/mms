package com.sxpcwlkj.websocket.entity;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.websocket.WebSocketService;
import com.sxpcwlkj.websocket.enums.ChatRoomTypeEnum;
import com.sxpcwlkj.websocket.enums.MsgEnum;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xijue
 * @ClassName ChatMsg
 * @description: 聊天室
 * @date 2024年10月25日
 * @version: 1.0
 */
@Data
public class ChatRoom {
    /**
     * 聊天室ID
     */
    private Long chatRoomId;
    /**
     * 目标用户 WebSocketSession
     */
    //private WebSocketSession session;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 聊天室用户
     */
    private List<Long> chatUserIds;
    /**
     * 聊天室类型
     */
    private Integer chatRoomType;
    /**
     * 聊天室头像
     */
    private String headPortrait;
    /**
     * 聊天室标题
     */
    private String title;
    /**
     * 未读数量
     */
    private Integer count;
    /**
     * 消息列表
     */
    private List<MsgInfoVo> msgInfoList;

    /**
     * 是否处于聊天选中窗口中
     */
    private boolean checked = false;


    /**
     * 创建聊天室
     * @param sendUserId 发送用户ID
     * @param session 接收用户WebSocketSession
     * @param title 聊天室标题
     * @return 聊天室
     */
    public ChatRoom createdRoom(String sendUserId, WebSocketSession session,String title){
        this.title = title;
        return createdRoom(sendUserId,session);
    }

    /**
     * 创建聊天室
     *
     * @param sendUserId 发送用户ID
     * @param session    接收用户WebSocketSession
     * @return 聊天室
     */
    public ChatRoom createdRoom(String sendUserId, WebSocketSession session) {
        this.userId = Convert.toLong(sendUserId);
        //this.session = session;
        List<Long> ids = new ArrayList<>();
        ids.add(Convert.toLong(WebSocketUtil.getUserId(session.getId())));
        ids.add(Convert.toLong(sendUserId));
        //ids 升序排序
        Collections.sort(ids);
        //聊天室成员
        this.chatUserIds = ids;
        //聊天室唯一ID
        this.chatRoomId = (long) ids.hashCode();
        //消息类型
        this.chatRoomType = ChatRoomTypeEnum.COMMON.getValue();
        //聊天室头像
        this.headPortrait = "https://picsum.photos/seed/picsum/30/30";
        //聊天室标题
        if(StringUtil.isEmpty(title)){
            this.title = this.chatRoomId + "";
        }
        //未读数量
        this.count = 0;
        //消息列表
        this.msgInfoList = new ArrayList<>();
        return this;
    }

    /**
     * 聊天室类型
     * @param chatRoomType 聊天室类型
     * @return
     */
    public ChatRoom setChatRoomType(ChatRoomTypeEnum chatRoomType){
        this.chatRoomType = chatRoomType.getValue();
        return this;
    }

    /**
     * 添加消息
     *
     * @param msg 消息
     * @return 聊天室
     */
    public ChatRoom addMsg(MsgInfo msg) {
        return addMsg(msg, MsgEnum.TXT);
    }

    /**
     * 添加消息
     *
     * @param msg 消息
     * @return 聊天室
     */
    public ChatRoom addMsg(MsgInfo msg, MsgEnum msgEnum) {

//        msg.setMsgUser(this.chatUserIds).setMsgType(msgEnum);
        {
//            msg.createMsg(this.chatRoomId).setMsgGroup();
            //找出当前聊天室的聊天记录
            List<MsgInfoVo> msgInfoList = WebSocketService.getChatRoomMsg(this.chatRoomId);
            if (msgInfoList == null) {
                msgInfoList = new ArrayList<>();
            }
            //添加消息到聊天记录
            MsgInfoVo infoVo = Convert.convert(MsgInfoVo.class, msg);
            msgInfoList.add(infoVo);

            //更新聊天记录
            WebSocketService.setChatRoomMsg(this.chatRoomId, msgInfoList);
            //添加消息到消息列表
            this.msgInfoList = msgInfoList;
        }
        return this;
    }


    public String build() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"chatRoomId\":").append(this.chatRoomId).append(",");
//        sb.append("\"userId\":").append(this.userId).append(",");
//        sb.append("\"chatUserIds\":").append(this.chatUserIds).append(",");
//        sb.append("\"chatRoomType\":").append(this.chatRoomType).append(",");
//        sb.append("\"headPortrait\":\"").append(this.headPortrait).append("\",");
//        sb.append("\"title\":\"").append(this.title).append("\",");
//        sb.append("\"count\":").append(this.count).append(",");
//
//        sb.append("\"msgList\":[");
//        for (int i = 0; i < this.msgInfoList.size(); i++) {
//            MsgInfoVo msgInfo = this.msgInfoList.get(i);
//            sb.append(msgInfo.toString());
//            if (i < this.msgInfoList.size() - 1) {
//                sb.append(",");
//            }
//        }
//        sb.append("],");
//        sb.append("\"checked\":").append(this.checked).append("}");
//        return sb.toString();
    return JSONUtil.toJsonStr(this);
    }
}
