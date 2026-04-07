-- 管理端全屏「插件安装向导」路由（mms-ui：system/pluginInstallWizard/index）
-- 需在已存在「系统管理」菜单（parent_id=3）的前提下执行；权限与插件市场一致为 super_admin。
-- 若 id 冲突，请改为未占用的 bigint 或改用菜单管理界面新增。

INSERT INTO `sys_function` (
  `id`, `parent_id`, `path`, `name`, `component`, `redirect_path`, `language_code`, `component_name`,
  `permission`, `type`, `sort`, `icon`, `status`, `visible`, `is_iframe`, `is_open_link`, `is_link`,
  `keep_alive`, `always_show`, `is_fast`, `remark`, `tenant_id`, `revision`,
  `created_by`, `created_time`, `updated_by`, `updated_time`
) VALUES (
  2030310000000000002, '3', '/system/pluginInstallWizard', '插件安装向导', 'system/pluginInstallWizard/index', NULL, '插件安装向导', NULL,
  'super_admin', 1, 98, 'iconfont icon-Upload', 1, -1, -1, -1, '',
  1, -1, 0, NULL, '000000', 1,
  '1', NOW(), '1', NOW()
);
