-- sys_plugins.listing_source：官方/用户上架区分；插件市场菜单与 admin 角色
-- 幂等：重复执行可能因主键重复失败，按需跳过已执行段落

ALTER TABLE `sys_plugins`
  ADD COLUMN `listing_source` tinyint NOT NULL DEFAULT 1 COMMENT '上架来源：0官方 1用户安装登记 2预留' AFTER `description`;

UPDATE `sys_plugins` SET `listing_source` = 0 WHERE `plugin_id` = 'mms.plugin.sample-health';

UPDATE `sys_function`
SET `permission` = 'super_admin,admin', `updated_time` = NOW()
WHERE `id` = 2030310000000000001;

INSERT INTO `sys_role_function` (
  `id`, `role_id`, `function_id`, `remark`, `status`, `sort`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`
) VALUES (
  '2030310000000000006', '1744898370860208129', '2030310000000000001', NULL, 1, 0, '000000', '1', '1', NOW(), '1', NOW()
);
