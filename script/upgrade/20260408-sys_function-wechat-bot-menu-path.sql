-- 修复企业微信机器人插件菜单：path/component 为 NULL 时 getMenu 动态路由异常（超级管理员全量菜单易触发）
-- 执行前请确认 id 与现网一致；若 id 不同请按需改写 WHERE。

UPDATE `sys_function`
SET `path`        = '/system/mmsWechatBot',
    `component`   = 'system/pluginPlaceholder/index',
    `updated_time` = NOW()
WHERE `id` IN ('2030320000000000100', 2030320000000000100);
