# MMS WebSocket 前端对接文档

## 1. 概述

本文档介绍前端如何与MMS WebSocket服务进行对接，包括连接建立、消息发送、认证鉴权等完整流程。

## 2. 连接建立

### 2.1 WebSocket地址
```
ws://[host]:[port]/ws?token=[auth_token]
```

### 2.2 连接示例
```javascript
const token = 'your_auth_token'; // 从登录接口获取
const wsUrl = `ws://localhost:8080/ws?token=${token}`;

const ws = new WebSocket(wsUrl);

ws.onopen = function(event) {
    console.log('WebSocket连接已建立');
};

ws.onmessage = function(event) {
    const data = JSON.parse(event.data);
    console.log('收到消息:', data);
};

ws.onerror = function(error) {
    console.error('WebSocket错误:', error);
};

ws.onclose = function(event) {
    console.log('WebSocket连接已关闭');
};
```

## 3. 消息协议

### 3.1 消息格式
```json
{
  "cmd": "command_type",
  "data": {}
}
```

### 3.2 命令类型

#### 3.2.1 心跳检测 (SYS_PING)
```javascript
// 发送心跳
ws.send(JSON.stringify({
    "cmd": 0,  // CmdEnum.SYS_PING
    "data": ""
}));

// 服务器响应
{
    "cmd": 0,
    "data": "true"
}
```

#### 3.2.2 获取在线用户列表 (USER_LIST)
```javascript
// 请求在线用户列表
ws.send(JSON.stringify({
    "cmd": 1,  // CmdEnum.USER_LIST
    "data": ""
}));

// 服务器响应
{
    "cmd": 1,
    "data": [
        {
            "chatRoomId": "0",
            "chatRoomType": "SYSTEM",
            "msgInfoList": [...],
            "userList": [...]
        }
    ]
}
```

#### 3.2.3 发送私聊消息 (send_private_msg)
```javascript
// 发送私聊消息
ws.send(JSON.stringify({
    "cmd": "send_private_msg",
    "data": {
        "receiverId": "user_id_to_send",
        "content": "消息内容",
        "contentType": "text" // text, image, video, file
    }
}));

// 服务器响应
{
    "cmd": 2, // SUCCEED
    "data": "私聊消息发送成功"
}
```

#### 3.2.4 发送群聊消息 (send_group_msg)
```javascript
// 发送群聊消息
ws.send(JSON.stringify({
    "cmd": "send_group_msg",
    "data": {
        "chatRoomId": "group_id",
        "content": "群聊消息内容",
        "contentType": "text"
    }
}));

// 服务器响应
{
    "cmd": 2, // SUCCEED
    "data": "群聊消息发送成功"
}
```

#### 3.2.5 加入群组 (join_group)
```javascript
// 加入群组
ws.send(JSON.stringify({
    "cmd": "join_group",
    "data": {
        "chatRoomId": "group_id"
    }
}));

// 服务器响应
{
    "cmd": 2, // SUCCEED
    "data": "加入群组成功"
}
```

#### 3.2.6 离开群组 (leave_group)
```javascript
// 离开群组
ws.send(JSON.stringify({
    "cmd": "leave_group",
    "data": {
        "chatRoomId": "group_id"
    }
}));

// 服务器响应
{
    "cmd": 2, // SUCCEED
    "data": "离开群组成功"
}
```

#### 3.2.7 订阅消息 (subscribe)
```javascript
// 订阅特定频道
ws.send(JSON.stringify({
    "cmd": "subscribe",
    "data": {
        "channel": "channel_name"
    }
}));

// 服务器响应
{
    "cmd": 2, // SUCCEED
    "data": "订阅成功: channel_name"
}
```

#### 3.2.8 取消订阅 (unsubscribe)
```javascript
// 取消订阅
ws.send(JSON.stringify({
    "cmd": "unsubscribe",
    "data": {
        "channel": "channel_name"
    }
}));

// 服务器响应
{
    "cmd": 2, // SUCCEED
    "data": "取消订阅成功: channel_name"
}
```

## 4. 消息接收处理

### 4.1 私聊消息
```javascript
// 接收私聊消息
{
    "cmd": 2, // SUCCEED
    "data": {
        "id": null,
        "senderId": "sender_user_id",
        "receiverId": "receiver_user_id", 
        "chatRoomId": null,
        "messageType": "private",
        "content": "消息内容",
        "contentType": "text",
        "status": "normal",
        "createTime": "2025-01-19T10:30:00",
        "updateTime": "2025-01-19T10:30:00",
        "extra": null,
        "contentLength": 12
    }
}
```

### 4.2 群聊消息
```javascript
// 接收群聊消息
{
    "cmd": 2, // SUCCEED
    "data": {
        "id": null,
        "senderId": "sender_user_id",
        "receiverId": null,
        "chatRoomId": "group_id",
        "messageType": "group", 
        "content": "群聊消息内容",
        "contentType": "text",
        "status": "normal",
        "createTime": "2025-01-19T10:30:00",
        "updateTime": "2025-01-19T10:30:00", 
        "extra": null,
        "contentLength": 15
    }
}
```

## 5. 前端完整示例

```javascript
class WebSocketClient {
    constructor(token) {
        this.token = token;
        this.ws = null;
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.connect();
    }

    connect() {
        const url = `ws://localhost:8080/ws?token=${this.token}`;
        this.ws = new WebSocket(url);

        this.ws.onopen = () => {
            console.log('WebSocket连接已建立');
            this.reconnectAttempts = 0;
            // 发送连接成功的回调
            this.onConnectSuccess && this.onConnectSuccess();
        };

        this.ws.onmessage = (event) => {
            try {
                const message = JSON.parse(event.data);
                this.handleMessage(message);
            } catch (error) {
                console.error('消息解析错误:', error);
            }
        };

        this.ws.onerror = (error) => {
            console.error('WebSocket错误:', error);
        };

        this.ws.onclose = (event) => {
            console.log('WebSocket连接已关闭，代码:', event.code, '原因:', event.reason);
            
            // 尝试重连
            if (this.reconnectAttempts < this.maxReconnectAttempts) {
                setTimeout(() => {
                    this.reconnectAttempts++;
                    console.log(`尝试重连 (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);
                    this.connect();
                }, 3000); // 3秒后重连
            }
        };
    }

    // 处理不同类型的消息
    handleMessage(message) {
        switch (message.cmd) {
            case 0: // 心跳响应
                console.log('收到心跳响应');
                break;
            case 1: // 用户列表
                this.onUserList && this.onUserList(message.data);
                break;
            case 2: // 操作成功
                console.log('操作成功:', message.data);
                this.onOperationSuccess && this.onOperationSuccess(message.data);
                break;
            default:
                // 其他消息类型，可能是聊天消息
                this.onChatMessage && this.onChatMessage(message);
                break;
        }
    }

    // 发送心跳
    ping() {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            this.ws.send(JSON.stringify({
                cmd: 0, // SYS_PING
                data: ""
            }));
        }
    }

    // 发送私聊消息
    sendPrivateMessage(receiverId, content, contentType = 'text') {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            const message = {
                cmd: "send_private_msg",
                data: {
                    receiverId: receiverId,
                    content: content,
                    contentType: contentType
                }
            };
            this.ws.send(JSON.stringify(message));
        }
    }

    // 发送群聊消息
    sendGroupMessage(chatRoomId, content, contentType = 'text') {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            const message = {
                cmd: "send_group_msg",
                data: {
                    chatRoomId: chatRoomId,
                    content: content,
                    contentType: contentType
                }
            };
            this.ws.send(JSON.stringify(message));
        }
    }

    // 加入群组
    joinGroup(chatRoomId) {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            const message = {
                cmd: "join_group",
                data: {
                    chatRoomId: chatRoomId
                }
            };
            this.ws.send(JSON.stringify(message));
        }
    }

    // 离开群组
    leaveGroup(chatRoomId) {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            const message = {
                cmd: "leave_group",
                data: {
                    chatRoomId: chatRoomId
                }
            };
            this.ws.send(JSON.stringify(message));
        }
    }

    // 获取用户列表
    getUserList() {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            const message = {
                cmd: 1, // USER_LIST
                data: ""
            };
            this.ws.send(JSON.stringify(message));
        }
    }

    // 关闭连接
    close() {
        if (this.ws) {
            this.ws.close();
        }
    }
}

// 使用示例
const client = new WebSocketClient('your_auth_token');

// 设置回调函数
client.onConnectSuccess = () => {
    console.log('连接成功');
    // 获取用户列表
    client.getUserList();
};

client.onUserList = (userList) => {
    console.log('收到用户列表:', userList);
};

client.onChatMessage = (message) => {
    console.log('收到聊天消息:', message);
};

client.onOperationSuccess = (result) => {
    console.log('操作成功:', result);
};

// 发送消息示例
setTimeout(() => {
    // 发送私聊消息
    client.sendPrivateMessage('target_user_id', '你好！');
    
    // 发送群聊消息
    client.sendGroupMessage('group_id', '大家好！');
}, 2000);

// 定时发送心跳
setInterval(() => {
    client.ping();
}, 30000); // 30秒发送一次心跳
```

## 6. 错误处理

### 6.1 常见错误码
- 连接失败：认证token无效
- 消息发送失败：权限不足或用户不在线
- 命令不识别：cmd值错误

### 6.2 重连机制
建议前端实现自动重连机制，重连间隔可设置为3-5秒，最大重连次数限制为5次。

## 7. 安全注意事项

1. Token应在HTTPS环境下传输
2. 定期刷新认证token
3. 验证接收到的消息格式和内容
4. 实现消息频率限制，防止刷屏

## 8. 性能优化建议

1. 合理使用心跳机制，避免频繁连接
2. 批量处理消息，减少DOM操作
3. 实现消息缓存，提升用户体验
4. 按需加载聊天记录，避免一次性加载过多数据

## 9. 测试要点

1. 连接建立与断开
2. 消息发送与接收
3. 多用户并发测试
4. 心跳保活机制
5. 重连机制
6. 权限验证
7. 错误处理

这份文档涵盖了前端与WebSocket服务对接的所有必要信息，包括连接、认证、消息格式、错误处理等完整流程。