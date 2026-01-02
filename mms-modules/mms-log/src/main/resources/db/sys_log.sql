-- ============================
-- 操作日志表
-- ============================
CREATE TABLE IF NOT EXISTS `sys_log` (
    `oper_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `module` VARCHAR(50) DEFAULT '' COMMENT '模块名称',
    `oper_type` INT DEFAULT 0 COMMENT '操作类型(0其它 1新增 2修改 3删除 4查询 5导出 6导入 7登录 8退出 9授权 10清空)',
    `description` VARCHAR(200) DEFAULT '' COMMENT '操作描述',
    `request_method` VARCHAR(10) DEFAULT '' COMMENT '请求方法(GET/POST/PUT/DELETE)',
    `method` VARCHAR(200) DEFAULT '' COMMENT '操作方法(类名.方法名)',
    `oper_url` VARCHAR(500) DEFAULT '' COMMENT '请求URL',
    `user_id` BIGINT DEFAULT 0 COMMENT '操作人员ID',
    `user_name` VARCHAR(50) DEFAULT '' COMMENT '操作人员账号',
    `user_roles` VARCHAR(200) DEFAULT '' COMMENT '操作人员角色',
    `oper_ip` VARCHAR(128) DEFAULT '' COMMENT '主机地址',
    `oper_location` VARCHAR(255) DEFAULT '' COMMENT '操作地点',
    `oper_param` TEXT COMMENT '请求参数',
    `before_data` TEXT COMMENT '操作前数据',
    `json_result` TEXT COMMENT '返回结果',
    `status` INT DEFAULT 0 COMMENT '操作状态(0成功 1失败)',
    `error_msg` TEXT COMMENT '错误消息',
    `oper_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `cost_time` BIGINT DEFAULT 0 COMMENT '消耗时间(毫秒)',
    `user_agent` VARCHAR(500) DEFAULT '' COMMENT '用户代理',
    `browser` VARCHAR(50) DEFAULT '' COMMENT '浏览器类型',
    `os` VARCHAR(50) DEFAULT '' COMMENT '操作系统',
    PRIMARY KEY (`oper_id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_oper_type` (`oper_type`),
    KEY `idx_oper_time` (`oper_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录表';
