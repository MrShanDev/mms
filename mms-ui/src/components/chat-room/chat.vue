<template>
  <div>
    <!-- 最外层页面于窗口同宽，使聊天面板居中 -->
    <div v-show="chatShow" class="home-view" :class="chatShowClass ?'home-view-show':'home-view-hide'">
      <!-- 整个聊天面板 -->
      <div  class="chat-panel">
          <div class="close-icon" @click="closeChat">
              <el-icon size="30">
                  <BottomRight color="#fff" />
              </el-icon>
          </div>
         <!-- 右侧的消息记录 -->
          <div class="message-panel iframe-wrapper">
              <iframe src="http://localhost:8080/index.html"></iframe>
          </div>
      </div>
    </div>
      <div v-show="!chatShow" class="home-icon" @click="openChat">
          <el-icon size="28" :color="socketState?'green':'red'">
              <ChatDotRound/>
          </el-icon>
      </div>
  </div>
</template>

<script setup lang="ts" name="WebSocket">
import {ref} from "vue";
import {BottomLeft, BottomRight, ChatDotRound, Orange, Picture, Promotion, VideoCamera} from "@element-plus/icons-vue";
import {Session} from "/@/utils/storage";
import {useUserInfo} from "/@/stores/userInfo";
import {storeToRefs} from "pinia";

const stores = useUserInfo();
const {userInfos} = storeToRefs(stores);
const socketState=ref(true)
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
      position: relative;
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
        right: 12px;
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

/* 关闭 */
.close-icon {
    position: absolute;
    top: 25px;
    left: 25px;
    cursor: #fff;
    z-index: 9999;
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

