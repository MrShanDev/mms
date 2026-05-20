-- 独立「网站配置」菜单（与系统配置同级），需配合 mms-ui views/system/websiteConfig/index.vue
-- 执行前请确认 ID 未被占用；若冲突请调整 sys_function.id / sys_role_function.id

START TRANSACTION;

INSERT INTO `sys_function` (
  `id`, `parent_id`, `path`, `name`, `component`, `redirect_path`, `language_code`, `component_name`, `permission`, `type`, `sort`, `icon`, `status`, `visible`, `is_iframe`, `is_open_link`, `is_link`, `keep_alive`, `always_show`, `is_fast`, `remark`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`
) VALUES (
  2030310000000000003, '3', '/system/websiteConfig', '网站配置', 'system/websiteConfig/index', NULL, 'message.router.websiteConfig', NULL, NULL, 1, 12, 'iconfont icon-application', 1, -1, -1, -1, '', 1, -1, 1, NULL, '000000', 2, '1', '2026-05-19 10:00:00', '1', '2026-05-19 10:00:00'
);

INSERT INTO `sys_role_function` (
  `id`, `role_id`, `function_id`, `remark`, `status`, `sort`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`
) VALUES
(
  '2030310000000000004', '1', '2030310000000000003', NULL, 1, 0, '000000', '0', '1', '2026-05-19 10:00:00', '1', '2026-05-19 10:00:00'
),
(
  '2030310000000000005', '1744898370860208129', '2030310000000000003', NULL, 1, 0, '000000', '1', '1', '2026-05-19 10:00:00', '1', '2026-05-19 10:00:00'
);

COMMIT;
