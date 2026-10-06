-- 将插件市场提升为一级菜单；保留权限、组件、路由与角色授权。
-- 执行前核对查询结果，确认只包含目标菜单。
SELECT id, parent_id, path, name, sort, tenant_id
FROM sys_function WHERE path = '/system/pluginMarket';

UPDATE sys_function SET parent_id = '1', sort = 2
WHERE path = '/system/pluginMarket' AND parent_id = '3' AND type = 1;

-- 回滚前按执行前查询记录恢复 sort：
-- UPDATE sys_function SET parent_id = '3', sort = <原排序>
-- WHERE id = <目标菜单ID> AND parent_id = '1';
