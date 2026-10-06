-- MMS 开源初始化：代码生成
-- UTF-8 / LF；MySQL 5.7.8+、8.x；InnoDB DYNAMIC / utf8mb4_unicode_ci。
-- 仅用于全新空库；已有表将报错。请先阅读同目录 README.md。

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- sys_gen_base_class
CREATE TABLE `sys_gen_base_class` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `package_name` varchar(200) DEFAULT NULL COMMENT '基类包名',
  `code` varchar(200) DEFAULT NULL COMMENT '基类编码',
  `fields` varchar(500) DEFAULT NULL COMMENT '基类字段，多个用英文逗号分隔',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基类管理';

INSERT INTO `sys_gen_base_class` (`id`, `package_name`, `code`, `fields`, `remark`, `create_time`) VALUES
  (1, 'com.sxpcwlkj.datasource.entity.BaseEntity', 'BaseEntity', 'status,sort,revision,tenant_id,created_by,created_time,updated_by,updated_time,remark', NULL, '2026-01-01 00:00:00');

-- sys_gen_datasource
CREATE TABLE `sys_gen_datasource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `db_type` varchar(200) DEFAULT NULL COMMENT '数据库类型',
  `conn_name` varchar(200) NOT NULL COMMENT '连接名',
  `conn_url` varchar(500) DEFAULT NULL COMMENT 'URL',
  `username` varchar(200) DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据源管理';


-- sys_gen_field_type
CREATE TABLE `sys_gen_field_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `column_type` varchar(200) DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200) DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) DEFAULT NULL COMMENT '属性包名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `column_type` (`column_type`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字段类型管理';

INSERT INTO `sys_gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `create_time`) VALUES
  (1, 'datetime', 'Date', 'java.util.Date', '2026-01-01 00:00:00'),
  (2, 'date', 'Date', 'java.util.Date', '2026-01-01 00:00:00'),
  (3, 'tinyint', 'Integer', NULL, '2026-01-01 00:00:00'),
  (4, 'smallint', 'Integer', NULL, '2026-01-01 00:00:00'),
  (5, 'mediumint', 'Integer', NULL, '2026-01-01 00:00:00'),
  (6, 'int', 'Integer', NULL, '2026-01-01 00:00:00'),
  (7, 'integer', 'Integer', NULL, '2026-01-01 00:00:00'),
  (8, 'bigint', 'Long', NULL, '2026-01-01 00:00:00'),
  (9, 'float', 'Float', NULL, '2026-01-01 00:00:00'),
  (10, 'double', 'Double', NULL, '2026-01-01 00:00:00'),
  (11, 'decimal', 'BigDecimal', 'java.math.BigDecimal', '2026-01-01 00:00:00'),
  (12, 'bit', 'Boolean', NULL, '2026-01-01 00:00:00'),
  (13, 'char', 'String', NULL, '2026-01-01 00:00:00'),
  (14, 'varchar', 'String', NULL, '2026-01-01 00:00:00'),
  (15, 'tinytext', 'String', NULL, '2026-01-01 00:00:00'),
  (16, 'text', 'String', NULL, '2026-01-01 00:00:00'),
  (17, 'mediumtext', 'String', NULL, '2026-01-01 00:00:00'),
  (18, 'longtext', 'String', NULL, '2026-01-01 00:00:00'),
  (19, 'timestamp', 'Date', 'java.util.Date', '2026-01-01 00:00:00'),
  (20, 'NUMBER', 'Integer', NULL, '2026-01-01 00:00:00'),
  (21, 'BINARY_INTEGER', 'Integer', NULL, '2026-01-01 00:00:00'),
  (22, 'BINARY_FLOAT', 'Float', NULL, '2026-01-01 00:00:00'),
  (23, 'BINARY_DOUBLE', 'Double', NULL, '2026-01-01 00:00:00'),
  (24, 'VARCHAR2', 'String', NULL, '2026-01-01 00:00:00'),
  (25, 'NVARCHAR', 'String', NULL, '2026-01-01 00:00:00'),
  (26, 'NVARCHAR2', 'String', NULL, '2026-01-01 00:00:00'),
  (27, 'CLOB', 'String', NULL, '2026-01-01 00:00:00'),
  (28, 'int8', 'Long', NULL, '2026-01-01 00:00:00'),
  (29, 'int4', 'Integer', NULL, '2026-01-01 00:00:00'),
  (30, 'int2', 'Integer', NULL, '2026-01-01 00:00:00'),
  (31, 'numeric', 'BigDecimal', 'java.math.BigDecimal', '2026-01-01 00:00:00');

-- sys_gen_project_modify
CREATE TABLE `sys_gen_project_modify` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_name` varchar(100) DEFAULT NULL COMMENT '项目名',
  `project_code` varchar(100) DEFAULT NULL COMMENT '项目标识',
  `project_package` varchar(100) DEFAULT NULL COMMENT '项目包名',
  `project_path` varchar(200) DEFAULT NULL COMMENT '项目路径',
  `modify_project_name` varchar(100) DEFAULT NULL COMMENT '变更项目名',
  `modify_project_code` varchar(100) DEFAULT NULL COMMENT '变更标识',
  `modify_project_package` varchar(100) DEFAULT NULL COMMENT '变更包名',
  `exclusions` varchar(200) DEFAULT NULL COMMENT '排除文件',
  `modify_suffix` varchar(200) DEFAULT NULL COMMENT '变更文件',
  `modify_tmp_path` varchar(100) DEFAULT NULL COMMENT '变更临时路径',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目名变更';


-- sys_gen_table
CREATE TABLE `sys_gen_table` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_name` varchar(200) DEFAULT NULL COMMENT '表名',
  `class_name` varchar(200) DEFAULT NULL COMMENT '类名',
  `table_comment` varchar(200) DEFAULT NULL COMMENT '说明',
  `author` varchar(200) DEFAULT NULL COMMENT '作者',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `package_name` varchar(200) DEFAULT NULL COMMENT '项目包名',
  `version` varchar(200) DEFAULT NULL COMMENT '项目版本号',
  `generator_type` tinyint DEFAULT NULL COMMENT '生成方式  0：zip压缩包   1：自定义目录',
  `backend_path` varchar(500) DEFAULT NULL COMMENT '后端生成路径',
  `frontend_path` varchar(500) DEFAULT NULL COMMENT '前端生成路径',
  `module_name` varchar(200) DEFAULT NULL COMMENT '模块名',
  `function_name` varchar(200) DEFAULT NULL COMMENT '功能名',
  `form_layout` tinyint DEFAULT NULL COMMENT '页面布局 1：列表 2：树 3：单表单',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `baseclass_id` bigint DEFAULT NULL COMMENT '基类ID',
  `menu_id` varchar(32) DEFAULT NULL COMMENT '菜单ID',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父级节点',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `table_label` varchar(32) DEFAULT NULL COMMENT '节点Label',
  `span` int DEFAULT '24' COMMENT '表单排列',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_name` (`table_name`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代码生成表';


-- sys_gen_table_field
CREATE TABLE `sys_gen_table_field` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_id` bigint DEFAULT NULL COMMENT '表ID',
  `field_name` varchar(200) DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(200) DEFAULT NULL COMMENT '字段类型',
  `field_comment` varchar(200) DEFAULT NULL COMMENT '字段说明',
  `attr_name` varchar(200) DEFAULT NULL COMMENT '属性名',
  `attr_type` varchar(200) DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) DEFAULT NULL COMMENT '属性包名',
  `sort` int DEFAULT NULL COMMENT '排序',
  `auto_fill` varchar(20) DEFAULT NULL COMMENT '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE',
  `primary_pk` tinyint DEFAULT NULL COMMENT '主键 0：否  1：是',
  `base_field` tinyint DEFAULT NULL COMMENT '基类字段 0：否  1：是',
  `form_item` tinyint DEFAULT NULL COMMENT '表单项 0：否  1：是',
  `form_required` tinyint DEFAULT NULL COMMENT '表单必填 0：否  1：是',
  `form_type` varchar(200) DEFAULT NULL COMMENT '表单类型',
  `form_dict` varchar(200) DEFAULT NULL COMMENT '表单字典类型',
  `form_validator` varchar(200) DEFAULT NULL COMMENT '表单效验',
  `grid_item` tinyint DEFAULT NULL COMMENT '列表项 0：否  1：是',
  `grid_sort` tinyint DEFAULT NULL COMMENT '列表排序 0：否  1：是',
  `query_item` tinyint DEFAULT NULL COMMENT '查询项 0：否  1：是',
  `query_type` varchar(200) DEFAULT NULL COMMENT '查询方式',
  `query_form_type` varchar(200) DEFAULT NULL COMMENT '查询表单类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代码生成表字段';
