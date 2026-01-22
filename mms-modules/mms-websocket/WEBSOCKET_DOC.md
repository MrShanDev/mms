# MMS WebSocket 核心开发与对接手册 (v2.2)

本文档旨在指导前端开发人员快速接入 MMS 消息服务，并为后端开发人员提供业务逻辑参考。系统支持**高性能实时通讯**、**消息状态管理**及**多端离线推送**。

---

## 🛠 一、 快速开始

### 1.1 连接建立
*   **WebSocket 地址**: `ws://[host]:[port]/ws?token=[auth_token]`
*   **鉴权方式**: QueryString 传参。Token 必须从系统登录接口获取。
*   **重连机制**: 建议前端实现指数退避重连策略（3s, 6s, 12s...）。

### 1.2 连接成功回执
连接成功后，服务端会立即推送 SessionID：
```json
{ "cmd": 1, "data": "sess_888888" }
```

---

## 📡 二、 通讯协议 (WebSocket)

所有消息格式固定为：`{"cmd": 指令, "data": 数据}`。部分指令的 `data` 需传入 **JSON 字符串**（即二次序列化）。

### 2.1 核心指令清单 (CMD)

| 指令 (CMD) | 名称 | 方向 | 说明 | 调用示例 |
| :--- | :--- | :--- | :--- | :--- |
| `0` | **PING** | 双向 | 心跳检测，服务端返回 `true` | `{"cmd":0,"data":"ping"}` |
| `100001` | **USER_LIST** | 客户端->服务端 | 获取活跃会话列表（首页数据） | `{"cmd":100001,"data":""}` |
| `send_private_msg`| **私聊** | 客户端->服务端 | 发送 1对1 消息 | 见 2.2 |
| `send_group_msg`  | **群聊** | 客户端->服务端 | 发送群组消息 | 见 2.3 |
| `join_group`      | **进群** | 客户端->服务端 | 加入指定聊天室 | `{"cmd":"join_group","data":"{\"chatRoomId\":\"G1\"}"}` |
| `get_online_users`| **在线列表** | 客户端->服务端 | 获取当前全平台在线 ID | `{"cmd":"get_online_users","data":""}` |

### 2.2 私聊消息示例 (send_private_msg)
```javascript
// 核心逻辑：data 字段必须是序列化后的字符串
ws.send(JSON.stringify({
    "cmd": "send_private_msg",
    "data": JSON.stringify({
        "receiverId": "user_456",
        "content": "你好，这是一条测试消息",
        "contentType": "text" // 支持 text, image, video, file
    })
}));
```

---

## 🔄 三、 业务流程场景 (Workflow)

### 3.1 场景：进入消息中心 (列表页)
1.  **拉取列表**: 调用 `GET /websocket/conversation/list/{userId}` 获取所有会话。
2.  **渲染规则**: 
    *   `isPinned == 1` 的置顶展示。
    *   显示 `unreadCount` 未读角标。
    *   显示 `lastMessageContent` 消息摘要。
3.  **实时监听**: 收到 `cmd: 2` 的推送消息时，通过 `senderId` 匹配列表项，更新内容并置顶。

### 3.2 场景：进入对话窗口 (私聊)
1.  **加载历史**: 调用 `GET /websocket/chat-history/private` 分页加载。
2.  **清空未读**: 调用 `POST /websocket/conversation/clear-unread` (消除红点)。
3.  **标记已读**: 收到新消息时，调用 `POST /websocket/mark-read` 触发对方的“已读回执”。

---

## 🌐 四、 RESTful API 接口清单 (HTTP)

| 路径 | 方法 | 功能 | 关键参数 |
| :--- | :--- | :--- | :--- |
| `/websocket/conversation/list/{userId}` | GET | 获取会话列表 | - |
| `/websocket/chat-history/private` | GET | 私聊历史记录 | `userId1`, `userId2`, `count` |
| `/websocket/mark-read` | POST | 标记单条消息已读 | `messageId`, `userId` |
| `/websocket/recall-message` | POST | 撤回消息 (2分钟内) | `messageId`, `userId` |
| `/websocket/offline-messages/{userId}` | GET | 获取未推送的离线消息 | - |
| `/websocket/conversation/pin` | POST | 会话置顶/取消置顶 | `conversationId`, `isPinned` |

---

## 💻 五、 开发者 SDK 封装与集成指南 (推荐)

为简化开发，建议前端封装一个核心 SDK 工具类，集成 **实时通信 (WS)** 与 **业务管理 (HTTP)**。

### 5.1 核心 SDK 类封装 (`MmsChatSDK.js`)
```javascript
import axios from 'axios';

class MmsChatSDK {
    constructor() {
        this.ws = null;
        this.handlers = {}; // 消息回调池
        this.userId = '';
        this.baseUrl = 'http://localhost:8060';
    }

    /** 初始化连接与监听 */
    init(userId, token) {
        this.userId = userId;
        this.ws = new WebSocket(`ws://localhost:8060/ws?token=${token}`);
        this.ws.onmessage = (e) => {
            const res = JSON.parse(e.data);
            if (res.cmd === 2) { // 实时业务消息
                const msg = res.data;
                const handler = this.handlers[msg.contentType] || this.handlers['all'];
                if (handler) handler(msg);
            }
        };
    }

    /** 监听指定类型的消息 (text/recall/read_receipt) */
    on(type, callback) { this.handlers[type] = callback; }

    /** 发送私聊 (WS方式) */
    sendPrivate(to, content, type = 'text') {
        this.ws.send(JSON.stringify({
            cmd: "send_private_msg",
            data: JSON.stringify({ receiverId: to, content, contentType: type })
        }));
    }

    /** 撤回消息 (HTTP方式) */
    async recall(messageId) {
        return axios.post(`${this.baseUrl}/websocket/recall-message`, null, {
            params: { messageId, userId: this.userId }
        });
    }

    /** 标记已读 (HTTP方式) */
    async markRead(messageId) {
        return axios.post(`${this.baseUrl}/websocket/mark-read`, null, {
            params: { messageId, userId: this.userId }
        });
    }

    /** 清空会话未读数 (HTTP方式) */
    async clearUnread(conversationId) {
        return axios.post(`${this.baseUrl}/websocket/conversation/clear-unread`, null, {
            params: { userId: this.userId, conversationId, conversationType: 'private' }
        });
    }

    /** 获取首页会话列表 */
    async getList() {
        return (await axios.get(`${this.baseUrl}/websocket/conversation/list/${this.userId}`)).data;
    }

    /** 获取历史聊天记录 */
    async getHistory(targetId, count = 50) {
        return (await axios.get(`${this.baseUrl}/websocket/chat-history/private`, {
            params: { userId1: this.userId, userId2: targetId, count }
        })).data;
    }
}
export default new MmsChatSDK();
```

### 5.2 UniApp 专用 SDK 封装 (`MmsChatUniSDK.js`)

在 UniApp 中，必须使用 `uni.connectSocket` 代替原生 `WebSocket` 以确保跨端兼容。

```javascript
class MmsChatUniSDK {
    constructor() {
        this.socketTask = null;
        this.handlers = {};
        this.userId = '';
        this.baseUrl = 'http://localhost:8060'; // 替换为实际 API 地址
    }

    /** 初始化 UniApp Socket */
    init(userId, token) {
        this.userId = userId;
        this.socketTask = uni.connectSocket({
            url: `ws://localhost:8060/ws?token=${token}`,
            success: () => console.log('UniApp Socket 正在连接...')
        });

        uni.onSocketMessage((res) => {
            const data = JSON.parse(res.data);
            if (data.cmd === 2) {
                const msg = data.data;
                // 全局分发事件，方便各页面监听
                uni.$emit('mms_chat_msg', msg);
                // 内部处理器分发
                const handler = this.handlers[msg.contentType] || this.handlers['all'];
                if (handler) handler(msg);
            }
        });
    }

    /** 发送私聊消息 (UniApp) */
    sendPrivate(to, content, type = 'text') {
        const payload = {
            cmd: "send_private_msg",
            data: JSON.stringify({ receiverId: to, content, contentType: type })
        };
        uni.sendSocketMessage({
            data: JSON.stringify(payload)
        });
    }

    /** 业务 HTTP 接口 (封装为 Promise) */
    request(url, method, data) {
        return new Promise((resolve, reject) => {
            uni.request({
                url: `${this.baseUrl}${url}`,
                method,
                data,
                success: (res) => resolve(res.data),
                fail: reject
            });
        });
    }

    // --- 快捷业务方法 ---
    async getList() { return this.request(`/websocket/conversation/list/${this.userId}`, 'GET'); }
    async markRead(messageId) { return this.request('/websocket/mark-read', 'POST', { messageId, userId: this.userId }); }
}
export default new MmsChatUniSDK();
```

### 5.3 UniApp 业务集成示例 (Demo)

#### 1. 全局初始化 (`App.vue`)
```javascript
import sdk from '@/utils/MmsChatUniSDK.js';

export default {
    onLaunch: function() {
        // 假设已登录，获取 token 和 userId
        const token = uni.getStorageSync('token');
        if (token) {
            sdk.init('user_123', token);
        }
    }
}
```

#### 2. 聊天窗口页面 (`pages/chat/chat.vue`)
```vue
<template>
    <view class="container">
        <scroll-view scroll-y class="chat-window">
            <view v-for="msg in msgList" :key="msg.messageId">
                <text>{{ msg.senderId }}: {{ msg.content }}</text>
            </view>
        </scroll-view>
        <input v-model="text" @confirm="doSend" placeholder="请输入消息" />
    </view>
</template>

<script>
import sdk from '@/utils/MmsChatUniSDK.js';

export default {
    data() {
        return { text: '', msgList: [], targetId: 'user_456' };
    },
    onLoad() {
        // 1. 监听全局消息
        uni.$on('mms_chat_msg', (msg) => {
            if (msg.senderId === this.targetId) {
                this.msgList.push(msg);
                // 自动标记已读
                sdk.markRead(msg.messageId);
            }
        });
        // 2. 加载历史
        this.loadHistory();
    },
    onUnload() {
        uni.$off('mms_chat_msg'); // 页面销毁时务必移除监听
    },
    methods: {
        async loadHistory() {
            // 通过 SDK 的 request 方法调用
            const res = await sdk.request('/websocket/chat-history/private', 'GET', {
                userId1: sdk.userId,
                userId2: this.targetId
            });
            this.msgList = res;
        },
        doSend() {
            sdk.sendPrivate(this.targetId, this.text);
            this.text = '';
        }
    }
}
</script>
```

---

## 🛠 六、 附加参考 (Vue 3 Hook)

### 6.1 核心数据表
1.  **`chat_user_conversation`**: 存储会话元数据（置顶、免打扰、未读数）。
2.  **`chat_message`**: 存储聊天流水，支持 `status` 字段标记（normal/recall）。

### 6.2 离线消息机制
*   用户离线时，消息自动进入 Redis 队列（`socket:offline:queue:{userId}`）。
*   用户上线（建立 WS 连接）后，服务端通过 `SocketHandler` 自动下发所有离线消息。

---
**系统维护**: MMS 研发组 | **更新日期**: 2026-01-22
