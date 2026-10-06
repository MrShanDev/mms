-- 仅新增个人外观配置表，不修改已有业务表和数据。
CREATE TABLE IF NOT EXISTS sys_user_theme_preference (
  tenant_id varchar(64) NOT NULL,
  user_id varchar(64) NOT NULL,
  theme_json json NOT NULL,
  updated_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (tenant_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户外观与控制台偏好';
START TRANSACTION;
UPDATE sys_function SET name='部门管理' WHERE id=1856208397166739485 AND tenant_id='000000' AND path='/system/dept' AND name='系统部门';
UPDATE sys_function SET name='通知公告' WHERE id=1856208397166739494 AND tenant_id='000000' AND path='/system/notice' AND name='系统公告';
COMMIT;
