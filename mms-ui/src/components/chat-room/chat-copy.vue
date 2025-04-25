<template>
  <div>
    <!-- 最外层页面于窗口同宽，使聊天面板居中 -->
    <div v-show="chatShow" class="home-view" :class="chatShowClass ?'home-view-show':'home-view-hide'">
      <!-- 整个聊天面板 -->
      <div  class="chat-panel">
        <!-- 左侧的会话列表 -->
        <div class="session-panel">
          <div class="close-icon" @click="closeChat">
            <el-icon size="20">
              <BottomRight/>
            </el-icon>
          </div>
          <div class="user-number">在线人数:{{ chatMsgList.length }}人</div>
          <div class="title">站内通信</div>
          <div class="description">让沟通变得如此简单~</div>
          <el-scrollbar height="300px">
            <div class="msg-list">
              <div v-show="false" class="msg-list-item" :class="v.checked?'checked':''" v-for="(v,i) in chatMsgList" :key="i"
                   @click="clickChatRoom(v.chatRoomId)">
                <div class="user-header">
                  <img :src="v.headPortrait" alt="">
                </div>
                <div class="user-title">
                  <div class="name">{{ v.title }}</div>
                  <div class="new-mag" v-if="v.msgInfoList!==undefined && v.msgInfoList.length>0" >{{ substring(v.msgInfoList[0].msgContent as string, 5) }}</div>
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>
        <!-- 右侧的消息记录 -->
         <div class="message-panel">
          <el-scrollbar class="msg-content" ref="scrollbarRef">
            <div ref="innerRef" v-if="sendMsgInfo.chatRoomId!=0" class="msg-list">
              <div :class="item.self? 'msg-item-right':'msg-item-left'" v-for="(item,index) in msgInfoList"
                   :key="index">
                <div class="top">
                  <div class="headerImg">
                    <img :src="item.sendUser?.headPortrait" alt=""/>
                  </div>
                  <div class="userInfo">
                    <div class="time">{{ item.sendTime }}</div>
                    <div class="name">{{ item.sendUser?.name }}</div>
                  </div>
                </div>
                <div class="content">
                  <span class="txt" v-if="item.msgType==MsgEnum.TXT" v-html="item.msgContent"></span>
                  <img class="img" v-if="item.msgType==MsgEnum.IMAGE" :src="item.msgContent as string" alt=""/>
                </div>
              </div>
            </div>
            <p v-else class="not-char">--------------请先选择聊天对象--------------</p>
          </el-scrollbar>
          <div class="msg-tool">
            <div class="tool-icon">
              <el-icon size="20">
                <Orange/>
              </el-icon>
            </div>
            <div class="tool-icon">
              <el-icon size="20">
                <Picture/>
              </el-icon>
            </div>
            <div class="tool-icon">
              <el-icon size="20">
                <VideoCamera/>
              </el-icon>
            </div>
          </div>
          <div class="msg-input">
            <div class="input-left">
              <el-input @keydown="handleKeydown" type="textarea" v-model="sendMsgInfo.msgContent"
                        placeholder="请输入..."/>
            </div>
            <div class="input-right" :class="sendMsgInfo.chatRoomId!=0? '':'forbidden'"
                 @click="sendMsg">
              <div>发送</div>
            </div>
          </div>
        </div>
      </div>

    </div>
    <div v-show="!chatShow" class="home-icon" @click="openChat">
      <el-icon size="28" :color="socketState?'green':'red'">
        <Promotion/>
      </el-icon>
    </div>
  </div>
</template>

<script setup lang="ts" name="WebSocket">
import {ChatRoom, CmdEnum, DataMsg, initWebSocket, MsgEnum, MsgInfo, send} from "/@/utils/webSocket";
import {ref} from "vue";
import {BottomRight, Orange, Picture, Promotion, VideoCamera} from "@element-plus/icons-vue";
import {Session} from "/@/utils/storage";
import {useUserInfo} from "/@/stores/userInfo";
import {storeToRefs} from "pinia";

const stores = useUserInfo();
const {userInfos} = storeToRefs(stores);
//窗口显示隐藏
const chatShow = ref(false)
//动画
const chatShowClass = ref(false)
//打开窗口
const openChat = () => {
  if (chatShow.value) return;
  chatShow.value = true
  chatShowClass.value = true;
}
//关闭窗口
const closeChat = () => {
  chatShowClass.value = false;
  setTimeout(() => {
    chatShow.value = false
  }, 1000)
}
//=======================WebSocket====================
// socketSessionId
const socketSessionId = ref<string>("")
//连接状态
const socketState = ref(false);
//初始化 webSocket对象
initWebSocket({
  open: () => {
    //console.info("连接WebSocket成功");
    socketState.value = true;
  },
  message: (event: MessageEvent) => {
    //openChat();
    //console.log("[webSocketMsg] : " + event.data);
    const data: DataMsg = JSON.parse(event.data);
    if(data.cmd==CmdEnum.SUCCEED){
      socketSessionId.value = data.data as string;
      //获取聊天列表
      send(new DataMsg(CmdEnum.USER_LIST))
    }
    //聊天室列表
    if (data.cmd == CmdEnum.USER_LIST) {
      chatMsgList.value = data.data as ChatRoom[];
    }
    //某聊天室有新消息
    if (data.cmd == CmdEnum.UPDATE_CHATROOM) {
      send(new DataMsg(CmdEnum.USER_LIST)).then(() => {
        if (sendMsgInfo.value.chatRoomId == data.data as number) {
          clickChatRoom(data.data as number)
        }
      })
    }
  },
  close: () => {
    //console.log("close");
    socketState.value = false;
  },
  error: () => {
    //console.log("error");
  }
});
//聊天列表
const chatMsgList = ref<ChatRoom[]>([]);
//聊天室消息
const msgInfoList = ref<MsgInfo[]>([]);
const scrollbarRef = ref(null)
const innerRef = ref()
//滚动到最底部
const scrollToBottom = () => {
  if (scrollbarRef.value && innerRef.value) {
    scrollbarRef.value.setScrollTop(innerRef.value.clientHeight)
  }
}
//查看聊天室
const clickChatRoom = (chatRoomId: number) => {
  sendMsgInfo.value.chatRoomId = 0;
  chatMsgList.value.forEach((v, i) => {
    if (v.chatRoomId == chatRoomId) {
      v.checked = true;
      sendMsgInfo.value.chatRoomId = v.chatRoomId;
      msgInfoList.value = v.msgInfoList;
    } else {
      v.checked = false;
    }
  })

}
//发送消息对象
const sendMsgInfo = ref<MsgInfo>({
  msgType: MsgEnum.TXT,
  self: true,
  msgContent: "",
  chatRoomId: 0,
  sendTime: new Date().getHours() + ":" + new Date().getMinutes(),
  sendUser: {name: userInfos.value.userName, headPortrait: userInfos.value.photo, userId: 0, sessionId: "", token: "" + `${Session.get('token')}`}
})
//发送消息
const sendMsg = () => {
  if (sendMsgInfo.value.chatRoomId == 0 || sendMsgInfo.value.msgContent?.length == 0) return;
  chatMsgList.value.some((v, i) => {
    if (v.checked == true) {
      v.msgInfoList.push(Object.assign({}, sendMsgInfo.value));
      send(new DataMsg(CmdEnum.SEND_MSG).addMsgData({
        type: sendMsgInfo.value.msgType,
        data: sendMsgInfo.value
      })).then((state) => {
        sendMsgInfo.value.msgContent = "";
        scrollToBottom()
      })
      return false;
    }
  })
}
const substring = (str: string, len: number) => {
  if (str.length <= len) {
    return str
  }
  return str.substring(0, len) + '...'
}
//回车键
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter') {
    // 阻止表单提交（如果输入框在表单内，且表单没有阻止默认行为）
    event.preventDefault();
    sendMsg();
  }
}
</script>

<style scoped lang="scss">
.home-icon {
  position: absolute;
  right: 40px;
  bottom: 100px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fff;
  cursor: pointer;
  box-shadow: 0 0 20px 20px rgba(black, 0.05);
  animation: shutter-in-bottom 1s linear 0s 1 normal none;
}

@keyframes shutter-in-bottom {

  0% {
    transform: rotateX(100deg);
    transform-origin: bottom;
    opacity: 0;
  }
  100% {
    transform: rotateX(0);
    transform-origin: bottom;
    opacity: 1;
  }
}

.home-view {
  --box-width: 20vw;
  --box-height: 70vh;
  position: absolute;
  right: 15px;
  bottom: 15px;
  /* 与窗口同宽 */
  width: var(--box-width)+'px';
  height: var(--box-height);
  z-index: 9999;

  .chat-panel {
    /* 聊天面板flex布局，让会话列表和聊天记录左右展示 */
    display: flex;
    /* 让聊天面板圆润一些 */
    border-radius: 20px;
    background-color: white;
    /* 给一些阴影 */
    box-shadow: 0 0 20px 20px rgba(black, 0.05);
    /* 与上方增加一些间距 */
    margin-top: 0px;
    /* 左侧聊天会话面板*/
    .session-panel {
      //display: none;
      background-color: var(--next-color-primary-lighter);
      width: 160px;
      border-top-left-radius: 20px;
      border-bottom-left-radius: 20px;
      padding: 20px;
      position: relative;
      border-right: 1px solid rgba(black, 0.07);
      /* 标题*/
      .title {
        margin-top: 20px;
        font-size: 20px;
      }

      /* 描述*/
      .description {
        color: rgba(black, 0.7);
        font-size: 10px;
        margin-top: 10px;
      }

      /* 关闭 */
      .close-icon {
        position: absolute;
        top: 12px;
        left: 12px;
        cursor: pointer;
        z-index: 9999;
      }

      /*在线人数*/
      .user-number {
        position: absolute;
        top: 12px;
        left: 110px;
        font-size: 12px;
        color: rgba(175, 175, 175, 0.5);
        z-index: 9999;
      }

      /*列表*/
      .msg-list {
        display: flex;
        flex-direction: column;
        margin-top: 10px;

        .msg-list-item {
          display: flex;
          height: 45px;
          width: 100%;
          padding: 5px 10px;
          align-items: center;
          cursor: pointer;
          margin-top: 10px;
          border-radius: 10px;

          .user-header {
            flex: 1;

            img {
              width: 30px;
              height: 30px;
              border-radius: 50%;
            }
          }

          .user-title {
            flex: 5;
            display: flex;
            flex-direction: column;
            padding-left: 10px;

            .name {
              font-size: 14px;
              font-weight: bold;
              color: #1f1f1f;
            }

            .new-mag {
              font-size: 12px;
              color: #777777;
            }
          }
        }

        .msg-list-item:hover {
          background: #e3e1ff5e;
        }

        .checked {
          background: #e3e1ff5e;
        }
      }
    }

    /* 右侧消息记录面板*/
    .message-panel {
      width: calc(var(--box-width) + 250px);
      height: var(--box-height);


      .msg-content {
        height: 70%;
        padding: 10px;

        .msg-list {
          display: flex;
          flex-direction: column;

          .msg-item-left {
            display: flex;
            flex-direction: column;
            margin-top: 25px;

            .top {
              display: flex;

              .headerImg {
                flex: 1;
                display: flex;
                justify-content: center;

                img {
                  width: 30px;
                  height: 30px;
                  border-radius: 50%;
                }
              }

              .userInfo {
                flex: 8;
                display: flex;
                flex-direction: column;
                padding-left: 10px;

                .time {
                  font-size: 12px;
                  color: #9b9da1;
                }

                .name {
                  font-size: 14px;
                  font-weight: bold;
                }
              }
            }

            .content {
              width: 90%;
              margin-left: 10%;
              margin-top: 10px;

              .txt {
                padding: 5px;
                background-color: rgba(155, 157, 161, 0.17);
                border-radius: 5px;
              }
            }
          }

          .msg-item-right {
            display: flex;
            flex-direction: column;
            margin-top: 25px;

            .top {
              display: flex;
              flex-direction: row-reverse;

              .headerImg {
                flex: 1;
                display: flex;
                justify-content: center;

                img {
                  width: 30px;
                  height: 30px;
                  border-radius: 50%;
                }
              }

              .userInfo {
                flex: 8;
                display: flex;
                flex-direction: column;
                text-align: right;
                padding-right: 10px;

                .time {
                  font-size: 12px;
                  color: #9b9da1;
                }

                .name {
                  font-size: 14px;
                  font-weight: bold;
                }
              }
            }

            .content {
              width: 90%;
              margin-right: 10%;
              margin-top: 10px;
              display: flex;
              flex-direction: row-reverse;

              .txt {
                padding: 5px;
                background-color: rgba(170, 191, 232, 0.17);
                border-radius: 5px;
              }
            }
          }
        }

        .not-char {
          text-align: center;
          color: rgba(155, 157, 161, 0.53);
          font-size: 12px;
          position: absolute;
          bottom: 5px;
          transform: translate(50%, -50%);
        }
      }

      .msg-tool {
        display: flex;
        height: 10%;
        border-top: 1px #7a7a7a36 solid;
        padding: 10px;

        .tool-icon {
          margin: 0 5px;
        }
      }

      .msg-input {
        display: flex;
        height: 20%;

        .input-left {
          width: 85%;
          padding: 0 10px;
          display: flex;

          :deep(.el-textarea__inner) {
            height: 100%;
          }

        }

        .input-right {
          padding: 0 10px;
          width: 13%;
          display: flex;
          align-items: center;
          justify-content: center;
          background-color: var(--next-color-primary-lighter);
          border-radius: 2px 2px 10px 2px;
          cursor: pointer;
        }

        .forbidden {
          cursor: not-allowed;
          background-color: var(--next-color-warning-lighter);
        }
      }


    }
  }
}

.home-view-show {
  animation: scale-up-br 1s linear 0s 1 normal none;
}

.home-view-hide {
  animation: scale-up-br-hide 1s linear 0s 1 normal none;
}

@keyframes scale-up-br {
  0% {
    transform: scale(.2);
    transform-origin: 100% 100%;
  }
  100% {
    transform: scale(1);
    transform-origin: 100% 100%;
  }
}

@keyframes scale-up-br-hide {

  100% {
    transform: scale(.2);
    transform-origin: 100% 100%;
  }
  0% {
    transform: scale(1);
    transform-origin: 100% 100%;
  }
}

body { margin: 0; padding: 0; }
.iframe-wrapper {
    height: 100vh;
    width: 100%;
}
iframe {
    width: 100%;
    height: 100%;
    border: none;
}
</style>

