-- MMS 开源初始化：插件市场与安装版本
-- UTF-8 / LF；MySQL 5.7.8+、8.x；InnoDB DYNAMIC / utf8mb4_unicode_ci。
-- 仅用于全新空库；已有表将报错。请先阅读同目录 README.md。

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- sys_plugins
CREATE TABLE `sys_plugins` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `plugin_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '与 plugin.json id 一致',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '展示名称',
  `icon_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封面/图标 URL',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '功能介绍',
  `listing_source` tinyint NOT NULL DEFAULT '1' COMMENT '上架来源：0官方 1用户安装登记 2预留',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '000000' COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '乐观锁',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` int DEFAULT '1' COMMENT '0下架 1上架',
  `sort` int DEFAULT '0',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plugin_tenant` (`plugin_id`,`tenant_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='插件市场展示（元数据）';


-- sys_plugin_version
CREATE TABLE `sys_plugin_version` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `plugin_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` int NOT NULL DEFAULT '0' COMMENT '1 当前宿主加载采用的版本',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '000000',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` int DEFAULT '1',
  `sort` int DEFAULT '0',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plugin_ver_tenant` (`plugin_id`,`version`,`tenant_id`),
  KEY `idx_plugin_tenant_active` (`plugin_id`,`tenant_id`,`is_active`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='插件安装版本与激活';


INSERT INTO `sys_function` (`id`, `parent_id`, `path`, `name`, `component`, `redirect_path`, `language_code`, `component_name`, `permission`, `type`, `sort`, `icon`, `status`, `visible`, `is_iframe`, `is_open_link`, `is_link`, `keep_alive`, `always_show`, `is_fast`, `remark`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES
  (2030310000000000001, '1', '/system/pluginMarket', '插件市场', 'system/pluginMarket/index', NULL, '插件市场', NULL, 'super_admin,admin', 1, 2, 'iconfont icon-application', 1, -1, -1, -1, '', 1, -1, 0, NULL, '000000', '0', '1', '2026-01-01 00:00:00', '1', '2026-01-01 00:00:00');

INSERT INTO `sys_role_function` (`id`, `role_id`, `function_id`, `remark`, `status`, `sort`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES
  ('2030310000000000002', '1', '2030310000000000001', NULL, 1, 0, '000000', '0', '1', '2026-01-01 00:00:00', '1', '2026-01-01 00:00:00'),
  ('2030310000000000006', '1744898370860208129', '2030310000000000001', NULL, 1, 0, '000000', '0', '1', '2026-01-01 00:00:00', '1', '2026-01-01 00:00:00');
