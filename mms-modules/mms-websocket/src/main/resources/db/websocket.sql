-- WebSocket 模块数据库表结构

-- 1. 用户会话表（支持置顶、免打扰等功能）
CREATE TABLE IF NOT EXISTS `chat_user_conversation` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
  `conversation_id` VARCHAR(50) NOT NULL COMMENT '会话ID（私聊是对方用户ID，群聊是群组ID）',
  `conversation_type` VARCHAR(20) NOT NULL DEFAULT 'private' COMMENT '会话类型：private私聊、group群聊',
  `is_pinned` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否置顶：0否 1是',
  `pinned_time` DATETIME NULL COMMENT '置顶时间',
  `is_muted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否免打扰：0否 1是',
  `last_message_time` DATETIME NULL COMMENT '最后一条消息时间',
  `last_message_content` VARCHAR(500) NULL COMMENT '最后一条消息内容',
  `unread_count` INT(11) NOT NULL DEFAULT 0 COMMENT '未读消息数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `extra` TEXT NULL COMMENT '扩展字段JSON',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_conversation` (`user_id`, `conversation_id`, `conversation_type`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_pinned_time` (`is_pinned`, `last_message_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户会话表';

-- 2. 聊天消息表（已存在，这里提供完整版本）
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `sender_id` VARCHAR(50) NOT NULL COMMENT '发送者ID',
  `receiver_id` VARCHAR(50) NULL COMMENT '接收者ID（私聊时使用）',
  `chat_room_id` VARCHAR(50) NULL COMMENT '聊天室ID（群聊时使用）',
  `message_type` VARCHAR(20) NOT NULL DEFAULT 'private' COMMENT '消息类型：private私聊、group群聊、broadcast广播',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `content_type` VARCHAR(20) NOT NULL DEFAULT 'text' COMMENT '内容类型：text文本、image图片、video视频、file文件、recall撤回',
  `status` VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '消息状态：normal正常、recall撤回、delete删除',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '消息更新时间',
  `extra` TEXT NULL COMMENT '扩展字段JSON格式',
  `content_length` INT(11) NULL COMMENT '消息长度',
  PRIMARY KEY (`id`),
  KEY `idx_sender_receiver_time` (`sender_id`, `receiver_id`, `create_time`),
  KEY `idx_chatroom_time` (`chat_room_id`, `create_time`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- 插入测试数据示例（可选）
-- INSERT INTO chat_user_conversation (user_id, conversation_id, conversation_type, last_message_time, last_message_content, unread_count) 
-- VALUES ('1', '2', 'private', NOW(), '你好！', 1);
