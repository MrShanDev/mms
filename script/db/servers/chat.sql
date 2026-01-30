
/**
  *聊天消息
 */
create table chat_message
(
    id             bigint auto_increment comment '消息ID'
        primary key,
    sender_id      varchar(50)                           not null comment '发送者ID',
    receiver_id    varchar(50)                           null comment '接收者ID（私聊时使用）',
    chat_room_id   varchar(50)                           null comment '聊天室ID（群聊时使用）',
    message_type   varchar(20) default 'private'         not null comment '消息类型：private私聊、group群聊、broadcast广播',
    content        text                                  not null comment '消息内容',
    content_type   varchar(20) default 'text'            not null comment '内容类型：text文本、image图片、video视频、file文件、recall撤回',
    status         varchar(20) default 'normal'          not null comment '消息状态：normal正常、recall撤回、delete删除',
    create_time    datetime    default CURRENT_TIMESTAMP not null comment '消息发送时间',
    update_time    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '消息更新时间',
    extra          text                                  null comment '扩展字段JSON格式',
    content_length int                                   null comment '消息长度'
)
    comment '聊天消息表' charset = utf8mb4;

create index idx_chatroom_time
    on chat_message (chat_room_id, create_time);

create index idx_create_time
    on chat_message (create_time);

create index idx_sender_receiver_time
    on chat_message (sender_id, receiver_id, create_time);

create index idx_status
    on chat_message (status);

/*
 *用户会话表
 */
create table chat_user_conversation
(
    id                   bigint auto_increment comment '主键ID'
        primary key,
    user_id              varchar(50)                           not null comment '用户ID',
    conversation_id      varchar(50)                           not null comment '会话ID（私聊是对方用户ID，群聊是群组ID）',
    conversation_type    varchar(20) default 'private'         not null comment '会话类型：private私聊、group群聊',
    is_pinned            tinyint(1)  default 0                 not null comment '是否置顶：0否 1是',
    pinned_time          datetime                              null comment '置顶时间',
    is_muted             tinyint(1)  default 0                 not null comment '是否免打扰：0否 1是',
    last_message_time    datetime                              null comment '最后一条消息时间',
    last_message_content varchar(500)                          null comment '最后一条消息内容',
    unread_count         int         default 0                 not null comment '未读消息数',
    create_time          datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time          datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    extra                text                                  null comment '扩展字段JSON',
    constraint uk_user_conversation
        unique (user_id, conversation_id, conversation_type)
)
    comment '用户会话表' charset = utf8mb4;

create index idx_pinned_time
    on chat_user_conversation (is_pinned, last_message_time);

create index idx_user_id
    on chat_user_conversation (user_id);
