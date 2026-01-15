<#assign dbTime = "now()">
<#if dbType=="SQLServer">
    <#assign dbTime = "getDate()">
</#if>

#菜单
INSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values (${menuId},'/${moduleName}/${functionName}','${tableComment}','${moduleName}/${functionName}/index','${tableComment}',1,1,'',1,-1,-1,-1,'',1,-1,'000000',${dbTime},${dbTime},1);

#列表
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-列表','${moduleName}:${functionName}:list',2,2,1,'000000',${dbTime},${dbTime},1);

#新增
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-新增','${moduleName}:${functionName}:insert',2,3,1,'000000',${dbTime},${dbTime},1);

#删除
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-删除','${moduleName}:${functionName}:delete',2,4,1,'000000',${dbTime},${dbTime},1);

#编辑
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-编辑','${moduleName}:${functionName}:edit',2,5,1,'000000',${dbTime},${dbTime},1);

#查询
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-查询','${moduleName}:${functionName}:query',2,6,1,'000000',${dbTime},${dbTime},1);

#导入
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-导入','${moduleName}:${functionName}:import',2,7,1,'000000',${dbTime},${dbTime},1);

#导出
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-导出','${moduleName}:${functionName}:export',2,8,1,'000000',${dbTime},${dbTime},1);

#打印
INSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)
values ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = '${tableComment}') AS a),'${tableComment}-打印','${moduleName}:${functionName}:print',2,9,1,'000000',${dbTime},${dbTime},1);
