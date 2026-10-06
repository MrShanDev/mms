-- 审核后手动执行：只软停用基础框架旧菜单，不删除网站配置值。
UPDATE sys_function SET status = 0 WHERE path = '/system/websiteConfig' AND component = 'system/websiteConfig/index';
