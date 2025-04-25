package com.sxpcwlkj.websocket;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.*;
import com.sxpcwlkj.websocket.enums.ChatRoomTypeEnum;
import com.sxpcwlkj.websocket.enums.CmdEnum;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xijue
 * @ClassName WebSocketService
 * @description: websocket操作相关服务类
 * @date 2024年10月23日
 * @version: 1.0
 */
@Service
@Slf4j
public class WebSocketService {

    /**
     * 接收到消息
     *
     * @param session 发送人session对象
     * @param dataMsg 发送人发送的内容
     */
    public static void receiverMsg(WebSocketSession session, DataMsgVo dataMsg) {
        try {
            if (dataMsg == null) {
                return;
            }
            //心跳,返回sessionId
            if (dataMsg.getCmd().equals(CmdEnum.SYS_PING.getValue())) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SYS_PING).setData(Boolean.TRUE.toString()));
            }
            //聊天列表
            else if (dataMsg.getCmd().equals(CmdEnum.USER_LIST.getValue())) {
                //聊天列表
                List<ChatRoom> chatRoomList = new ArrayList<>();

                //=====================AI聊天室===================
                ChatRoom chatRoom = new ChatRoom()
                        //创建聊天室
                        .createdRoom("0", session, "MMS-AI客服")
                        //设置聊天室类型,获取在线人员
                        .setChatRoomType(ChatRoomTypeEnum.SYSTEM);
                // 根据聊天室ID获取历史缓存数据
                List<MsgInfoVo> msgInfoVos = getChatRoomMsg(chatRoom.getChatRoomId());
                //如果空，新增第一条信息
                if (msgInfoVos.isEmpty()) {
                    chatRoomList.add(
                            chatRoom.addMsg(new MsgInfo(chatRoom.getChatRoomId())
                                    .setUse(0L, Convert.toLong(WebSocketUtil.getUserId(session.getId())))
                                    .setMsgContent("您好,欢迎使用MMS聊天室！"))
                    );
                } else {
                    // 返回历史缓存信息
                    chatRoom.setMsgInfoList(msgInfoVos);
                    chatRoomList.add(chatRoom);
                }
                //=====================AI聊天室===================

                //TODO  添加其他在线聊天室
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.USER_LIST)
                        .setData(chatRoomList));
            } else if (dataMsg.getCmd().equals(CmdEnum.SEND_MSG.getValue())) {
                String msgData = dataMsg.getData();
                DataMsgInfoVo convert = BeanUtil.toBean(JSONUtil.toBean(msgData, JSONObject.class), DataMsgInfoVo.class);
                if (convert != null) {
                    MsgInfoVo msgInfoVo = convert.getData();
                    MsgUser sendUser = msgInfoVo.getSendUser();
                    Object objId = StpUtil.getLoginIdByToken(sendUser.getToken());
                    sendUser.setUserId(Convert.toLong(objId));
                    sendUser.setSessionId(session.getId());
                    sendUser.setToken("");
                    msgInfoVo.setEveryone(false);
                    msgInfoVo.setRecipientUser(new MsgUser());
                    msgInfoVo.setSendUser(sendUser);
//                    List<MsgInfoVo> msgInfoVos = getChatRoomMsg(msgInfoVo.getChatRoomId());
                    List<MsgInfoVo> msgInfoVos = new ArrayList<>();
                    msgInfoVos.add(msgInfoVo);

                    setChatRoomMsg(msgInfoVo.getChatRoomId(), msgInfoVos);
                }
                //log.info(JSONUtil.toJsonStr(convert));
            }
            //其他
            else {
                log.error("X");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取聊天室消息
     *
     * @param chatRoomId 聊天室id
     * @return 聊天室消息
     */
    public static List<MsgInfoVo> getChatRoomMsg(Long chatRoomId) {
        List<MsgInfoVo> list = new ArrayList<>();
        try {
            list = RedisUtil.getCacheList("socket:room*" );

            list = RedisUtil.getCacheList(SocketConstant.SOCKET_ROOM + chatRoomId.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 设置聊天室消息
     *
     * @param chatRoomId  聊天室id
     * @param msgInfoList 聊天室消息
     */
    public static void setChatRoomMsg(Long chatRoomId, List<MsgInfoVo> msgInfoList) {
        RedisUtil.setCacheList(SocketConstant.SOCKET_ROOM + chatRoomId.toString(), msgInfoList);
    }
}
