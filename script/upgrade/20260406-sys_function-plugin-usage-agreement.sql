-- 管理端「插件使用协议」静态页（mms-ui：system/pluginUsageAgreement/index）
-- 与插件市场、安装向导配套；权限 super_admin。若 id 冲突请调整。

INSERT INTO `sys_function` (
  `id`, `parent_id`, `path`, `name`, `component`, `redirect_path`, `language_code`, `component_name`,
  `permission`, `type`, `sort`, `icon`, `status`, `visible`, `is_iframe`, `is_open_link`, `is_link`,
  `keep_alive`, `always_show`, `is_fast`, `remark`, `tenant_id`, `revision`,
  `created_by`, `created_time`, `updated_by`, `updated_time`
) VALUES (
  2030310000000000003, '3', '/system/pluginUsageAgreement', '插件使用协议', 'system/pluginUsageAgreement/index', NULL, '插件使用协议', NULL,
  'super_admin', 1, 96, 'iconfont icon-Document', 1, -1, -1, -1, '',
  1, -1, 0, NULL, '000000', 1,
  '1', NOW(), '1', NOW()
);
