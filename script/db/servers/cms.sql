
/**
 * CMS 模块（网站导航、快捷入口、搜索历史与热门标签）
 * 与 mms-servers/mms-cms 实体一致
 */

DROP TABLE IF EXISTS `cms_quick_config`;
DROP TABLE IF EXISTS `cms_quick_entry`;
DROP TABLE IF EXISTS `cms_navigation`;
DROP TABLE IF EXISTS `cms_search_history`;
DROP TABLE IF EXISTS `cms_search_hot_tag`;

CREATE TABLE `cms_navigation` (
      `id` bigint NOT NULL COMMENT 'ID',
      `parent_id` bigint DEFAULT NULL COMMENT '父级ID',
      `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '导航名称',
      `path` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由路径',
      `icon` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标类名或URL',
      `is_hot` int DEFAULT NULL COMMENT '是否热门标签',
      `type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型',
      `status` int DEFAULT '0' COMMENT '状态（0=禁用，1=启用）',
      `sort` int DEFAULT '0' COMMENT '排序',
      `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
      `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户号',
      `revision` bigint DEFAULT '0' COMMENT '乐观锁',
      `created_by` bigint DEFAULT NULL COMMENT '创建者',
      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
      `updated_by` bigint DEFAULT NULL COMMENT '更新者',
      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`),
      KEY `idx_parent_id` (`parent_id`),
      KEY `idx_tenant_sort` (`tenant_id`,`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='网站导航菜单表';


CREATE TABLE `cms_quick_entry` (
      `id` bigint NOT NULL COMMENT 'ID',
      `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单名称',
      `desc` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
      `icon` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标组件或类名',
      `icon_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标类型',
      `path` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跳转路径',
      `type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入口类型',
      `position` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '显示位置',
      `is_always_show` int DEFAULT NULL COMMENT '是否始终显示',
      `show_conditions` text COLLATE utf8mb4_general_ci COMMENT '显示条件(JSON)',
      `badge_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角标类型',
      `badge_value` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角标值',
      `permission` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
      `has_popup` int DEFAULT NULL COMMENT '是否有弹出层',
      `action_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '动作类型',
      `action_value` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '动作值',
      `is_hot` int DEFAULT NULL COMMENT '是否热门',
      `click_count` int DEFAULT '0' COMMENT '点击次数',
      `status` int DEFAULT '0' COMMENT '状态（0=禁用，1=启用）',
      `sort` int DEFAULT '0' COMMENT '排序',
      `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
      `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户号',
      `revision` bigint DEFAULT '0' COMMENT '乐观锁',
      `created_by` bigint DEFAULT NULL COMMENT '创建者',
      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
      `updated_by` bigint DEFAULT NULL COMMENT '更新者',
      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`),
      KEY `idx_tenant_position_sort` (`tenant_id`,`position`,`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='快捷入口';


CREATE TABLE `cms_quick_config` (
      `id` bigint NOT NULL COMMENT 'ID',
      `entry_id` bigint DEFAULT NULL COMMENT '入口ID',
      `config_key` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置键',
      `config_value` text COLLATE utf8mb4_general_ci COMMENT '配置值',
      `config_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置类型',
      `status` int DEFAULT '0' COMMENT '状态（0=禁用，1=启用）',
      `sort` int DEFAULT '0' COMMENT '排序',
      `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
      `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户号',
      `revision` bigint DEFAULT '0' COMMENT '乐观锁',
      `created_by` bigint DEFAULT NULL COMMENT '创建者',
      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
      `updated_by` bigint DEFAULT NULL COMMENT '更新者',
      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`),
      KEY `idx_entry_id` (`entry_id`),
      KEY `idx_entry_config_key` (`entry_id`,`config_key`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='快捷入口配置表';


CREATE TABLE `cms_search_history` (
      `id` bigint NOT NULL COMMENT 'ID',
      `user_id` bigint DEFAULT NULL COMMENT '用户ID',
      `keyword` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '搜索关键词',
      `ip_address` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
      `search_result_count` int DEFAULT NULL COMMENT '搜索结果数量',
      `status` int DEFAULT '0' COMMENT '状态（0=禁用，1=启用）',
      `sort` int DEFAULT '0' COMMENT '排序',
      `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
      `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户号',
      `revision` bigint DEFAULT '0' COMMENT '乐观锁',
      `created_by` bigint DEFAULT NULL COMMENT '创建者',
      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
      `updated_by` bigint DEFAULT NULL COMMENT '更新者',
      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`),
      KEY `idx_user_created` (`user_id`,`created_time`),
      KEY `idx_keyword` (`keyword`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='搜索历史';


CREATE TABLE `cms_search_hot_tag` (
      `id` bigint NOT NULL COMMENT 'ID',
      `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签名称',
      `search_keyword` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '搜索关键词',
      `is_hot` int DEFAULT NULL COMMENT '是否热门',
      `type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签类型',
      `game_id` int DEFAULT NULL COMMENT '关联游戏ID',
      `click_count` int DEFAULT '0' COMMENT '点击次数',
      `search_count` int DEFAULT '0' COMMENT '搜索次数',
      `status` int DEFAULT '0' COMMENT '状态（0=禁用，1=启用）',
      `sort` int DEFAULT '0' COMMENT '排序',
      `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
      `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户号',
      `revision` bigint DEFAULT '0' COMMENT '乐观锁',
      `created_by` bigint DEFAULT NULL COMMENT '创建者',
      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
      `updated_by` bigint DEFAULT NULL COMMENT '更新者',
      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`),
      KEY `idx_hot_type_sort` (`is_hot`,`type`,`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='热门搜索标签表';
