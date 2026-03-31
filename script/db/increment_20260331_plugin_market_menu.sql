-- 插件宿主市场：菜单 + 绑定超级管理员角色（role_id='1'）
-- permission 列存「可见角色键」，与其它菜单一致；本页仅 super_admin。
-- 若 id 或 path 已存在则跳过，可安全重复执行。
-- 如需给 admin 同显，将 permission 改为 'super_admin,admin'，并在 sys_role_function 中为 admin 角色补一行。
--
-- MySQL 8：避免 utf8mb4_unicode_ci（表字段）与连接默认 utf8mb4_0900_ai_ci 混用导致 1267。
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

SET @mms_plugin_market_fid := 2030310000000000001;
SET @mms_plugin_market_fid_str := CAST(@mms_plugin_market_fid AS CHAR CHARACTER SET utf8mb4)
  COLLATE utf8mb4_unicode_ci;
SET @mms_plugin_market_rfid := CAST('2030310000000000002' AS CHAR CHARACTER SET utf8mb4)
  COLLATE utf8mb4_unicode_ci;
SET @mms_plugin_market_path := CAST('/system/pluginMarket' AS CHAR CHARACTER SET utf8mb4)
  COLLATE utf8mb4_unicode_ci;

INSERT INTO `sys_function` (
  `id`, `parent_id`, `path`, `name`, `component`, `redirect_path`, `language_code`, `component_name`,
  `permission`, `type`, `sort`, `icon`, `status`, `visible`, `is_iframe`, `is_open_link`, `is_link`,
  `keep_alive`, `always_show`, `is_fast`, `remark`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`
)
SELECT
  @mms_plugin_market_fid, '3', '/system/pluginMarket', '插件宿主', 'system/pluginMarket/index', NULL, '插件市场', NULL,
  'super_admin', 1, 99, 'iconfont icon-application', 1, -1, -1, -1, '',
  1, -1, 0, NULL, '000000', 1, '1', NOW(), '1', NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `sys_function` WHERE `id` = @mms_plugin_market_fid)
  AND NOT EXISTS (SELECT 1 FROM `sys_function` WHERE `path` = @mms_plugin_market_path);

INSERT INTO `sys_role_function` (
  `id`, `role_id`, `function_id`, `remark`, `status`, `sort`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`
)
SELECT
  @mms_plugin_market_rfid, '1', @mms_plugin_market_fid_str, NULL, 1, 0, '000000', '0', '1', NOW(), '1', NOW()
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `sys_role_function`
  WHERE `role_id` = '1' AND `function_id` = @mms_plugin_market_fid_str
);
