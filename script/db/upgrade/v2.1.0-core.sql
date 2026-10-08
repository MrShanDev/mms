-- Run on a backed up existing database only after duplicate preflight.
-- Nullable empty phone/email are normalized to NULL so multiple unbound accounts remain valid.
UPDATE sys_user SET phone_number=NULL WHERE phone_number='';
UPDATE sys_user SET email=NULL WHERE email='';
ALTER TABLE sys_user ADD UNIQUE KEY uk_user_tenant_name (tenant_id,user_name),
 ADD UNIQUE KEY uk_user_tenant_phone (tenant_id,phone_number),
 ADD UNIQUE KEY uk_user_tenant_email (tenant_id,email),
 ADD KEY ix_user_tenant_dept (tenant_id,dept_id),
 ADD KEY ix_user_tenant_status_created (tenant_id,status,created_time);
ALTER TABLE sys_role ADD UNIQUE KEY uk_role_tenant_code (tenant_id,code);
ALTER TABLE sys_dict ADD UNIQUE KEY uk_dict_tenant_field (tenant_id,field_name);
ALTER TABLE sys_config ADD UNIQUE KEY uk_config_tenant_key (tenant_id,config_key);
ALTER TABLE sys_user_role ADD UNIQUE KEY uk_user_role (tenant_id,user_id,role_id), ADD KEY ix_user_role_role (tenant_id,role_id);
ALTER TABLE sys_role_function ADD UNIQUE KEY uk_role_function (tenant_id,role_id,function_id);
ALTER TABLE sys_notice ADD KEY ix_notice_tenant_status_time (tenant_id,status,created_time);
ALTER TABLE sys_log ADD KEY ix_log_tenant_time (tenant_id,oper_time);
CREATE TABLE sys_notice_read (
 tenant_id varchar(32) NOT NULL, user_id varchar(32) NOT NULL, notice_id varchar(32) NOT NULL,
 read_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (tenant_id,user_id,notice_id), KEY ix_notice_read_notice (tenant_id,notice_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO sys_function(parent_id,name,permission,type,status,tenant_id,revision,created_time,updated_time)
 SELECT id,'运行监控','system:monitor:query',2,1,tenant_id,1,NOW(),NOW()
 FROM sys_function WHERE id='2';

ALTER TABLE sys_oss ADD COLUMN file_info_json LONGTEXT COMMENT '存储服务完整元数据';

ALTER TABLE sys_sign ADD KEY idx_sign_user_expiry(user_id,time_out);
