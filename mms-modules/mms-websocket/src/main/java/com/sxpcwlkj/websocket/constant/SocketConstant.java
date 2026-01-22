package com.sxpcwlkj.websocket.constant;

/**
 * socket常量
 */
public class SocketConstant {

    // Redis键前缀定义
    public static final String SOCKET_ROOM = "socket:room:";
    public static final String SOCKET_USER = "socket:user:";
    public static final String SOCKET_USER_ID = "socket:user:id:";
    public static final String SOCKET_SESSION_ID = "socket:session:id:";
    public static final String SOCKET_TOKEN = "token";
    public static final String SOCKET_ID = "id";
    
    // 新增常量
    public static final String SOCKET_USER_SESSION_MAP = "socket:user:session:map";           // 用户ID到SessionID的映射
    public static final String SOCKET_SESSION_USER_MAP = "socket:session:user:map";           // SessionID到用户ID的映射
    public static final String SOCKET_ONLINE_USERS = "socket:online:users";                   // 在线用户集合
    public static final String SOCKET_CHATROOM_MEMBERS_PREFIX = "socket:chatroom:members:";    // 聊天室成员集合前缀
    public static final String SOCKET_USER_PERMISSIONS_PREFIX = "socket:user:permissions:";   // 用户权限前缀
    public static final String SOCKET_MESSAGE_HISTORY_PREFIX = "socket:message:history:";     // 消息历史前缀
    public static final String SOCKET_USER_SUBSCRIPTIONS_PREFIX = "socket:user:subscriptions:"; // 用户订阅前缀
    public static final String SOCKET_GROUP_INFO_PREFIX = "socket:group:info:";               // 群组信息前缀
    public static final String SOCKET_PRIVATE_CHATS_PREFIX = "socket:private:chats:";         // 私聊会话前缀
    
    // 消息类型
    public static final String MSG_TYPE_PRIVATE = "private";    // 私聊消息
    public static final String MSG_TYPE_GROUP = "group";        // 群聊消息
    public static final String MSG_TYPE_BROADCAST = "broadcast"; // 广播消息
    
    // 消息命令类型
    public static final String CMD_SUBSCRIBE = "subscribe";     // 订阅
    public static final String CMD_UNSUBSCRIBE = "unsubscribe"; // 取消订阅
    public static final String CMD_JOIN_GROUP = "join_group";   // 加入群组
    public static final String CMD_LEAVE_GROUP = "leave_group"; // 离开群组
    public static final String CMD_GET_ONLINE_USERS = "get_online_users"; // 获取在线用户
    public static final String CMD_SEND_PRIVATE_MSG = "send_private_msg"; // 发送私聊消息
    public static final String CMD_SEND_GROUP_MSG = "send_group_msg";     // 发送群聊消息
    
    // 权限相关
    public static final String PERMISSION_CHAT_READ = "chat:read";    // 读取消息权限
    public static final String PERMISSION_CHAT_WRITE = "chat:write";   // 发送消息权限
    public static final String PERMISSION_CHAT_CREATE = "chat:create";  // 创建聊天权限
    public static final String PERMISSION_CHAT_MANAGE = "chat:manage";  // 管理聊天权限
    
    // 性能监控相关常量
    public static final String SOCKET_PERFORMANCE_STATS_PREFIX = "socket:performance:stats:"; // 性能统计前缀
    public static final String SOCKET_USER_LAST_ACTIVE = "socket:user:last_active:"; // 用户最后活跃时间前缀
    
    // 消息计数相关常量
    public static final String SOCKET_MESSAGE_COUNT_PREFIX = "socket:message:count:"; // 消息计数前缀
    
    // 离线消息队列
    public static final String SOCKET_OFFLINE_MESSAGE_QUEUE_PREFIX = "socket:offline:queue:"; // 离线消息队列前缀
}
