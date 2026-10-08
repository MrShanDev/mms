-- Platform tenant menu only; safe to run again without duplicating rows.
INSERT INTO sys_function(id,parent_id,path,name,component,language_code,permission,type,sort,icon,status,visible,is_iframe,is_open_link,is_link,keep_alive,always_show,is_fast,tenant_id,revision,created_by,created_time,updated_by,updated_time)
SELECT 2061000000000000101,'3','/system/tenant','租户管理','system/tenant/index','租户管理','super_admin',1,8,'iconfont icon-application',1,-1,-1,-1,'',1,-1,0,'000000',0,'1',NOW(),'1',NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_function WHERE tenant_id='000000' AND path='/system/tenant');
INSERT INTO sys_function(id,parent_id,name,component,permission,type,sort,status,visible,tenant_id,revision,created_by,created_time,updated_by,updated_time)
SELECT 2061000000000000102,CAST(id AS CHAR),'租户查询','','system:tenant:list',2,1,1,-1,'000000',0,'1',NOW(),'1',NOW()
FROM sys_function p WHERE p.tenant_id='000000' AND p.path='/system/tenant'
AND NOT EXISTS (SELECT 1 FROM sys_function WHERE tenant_id='000000' AND permission='system:tenant:list' AND type=2);
UPDATE sys_function SET language_code='租户管理',permission='super_admin',icon='iconfont icon-application' WHERE tenant_id='000000' AND path='/system/tenant' AND component='system/tenant/index';
