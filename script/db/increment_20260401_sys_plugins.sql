-- 插件市场展示表：名称、封面、功能介绍等元数据；与磁盘 ${mms.plugin.root-dir} 安装状态由接口合并返回。
-- MySQL 8：统一排序规则，避免 1267。
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `sys_plugins` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `plugin_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '与 plugin.json id 一致',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '展示名称',
  `icon_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封面/图标 URL',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '功能介绍',
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
  UNIQUE KEY `uk_plugin_tenant` (`plugin_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='插件市场展示（元数据）';

INSERT INTO `sys_plugins` (
  `id`, `plugin_id`, `name`, `icon_url`, `description`, `tenant_id`, `revision`, `status`, `sort`, `created_time`, `updated_time`
)
SELECT
  '2030400000000000101',
  'com.sxpcwlkj.plugin.sample.health',
  '示例健康插件',
  '',
  '用于联调插件宿主：校验 META-INF/mms/plugin.json、SPI 加载与 PluginHealthContributor 聚合。安装后在本页应显示「已加载」与健康检查为 OK；也可用于验证卸载与重载流程。',
  '000000',
  '0',
  1,
  1,
  NOW(),
  NOW()
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `sys_plugins` WHERE `plugin_id` = CAST('com.sxpcwlkj.plugin.sample.health' AS CHAR CHARACTER SET utf8mb4) COLLATE utf8mb4_unicode_ci AND `tenant_id` = '000000'
);
