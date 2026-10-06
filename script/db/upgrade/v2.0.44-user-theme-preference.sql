-- 仅新增个人外观配置表，不修改已有业务表和数据。
CREATE TABLE IF NOT EXISTS sys_user_theme_preference (
  tenant_id varchar(64) NOT NULL,
  user_id varchar(64) NOT NULL,
  theme_json json NOT NULL,
  updated_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (tenant_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户外观与控制台偏好';
