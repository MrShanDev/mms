
DROP TABLE IF EXISTS `gen_base_class`;

CREATE TABLE `gen_base_class` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `package_name` varchar(200) DEFAULT NULL COMMENT '基类包名',
  `code` varchar(200) DEFAULT NULL COMMENT '基类编码',
  `fields` varchar(500) DEFAULT NULL COMMENT '基类字段，多个用英文逗号分隔',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='基类管理';

/*Data for the table `gen_base_class` */

insert  into `gen_base_class`(`id`,`package_name`,`code`,`fields`,`remark`,`create_time`) values 

(1,'net.maku.framework.mybatis.entity','BaseEntity','id,creator,create_time,updater,update_time,version,deleted','使用该基类，则需要表里有这些字段。','2024-01-21 01:57:21');


DROP TABLE IF EXISTS `gen_datasource`;

CREATE TABLE `gen_datasource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `db_type` varchar(200) DEFAULT NULL COMMENT '数据库类型',
  `conn_name` varchar(200) NOT NULL COMMENT '连接名',
  `conn_url` varchar(500) DEFAULT NULL COMMENT 'URL',
  `username` varchar(200) DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源管理';


DROP TABLE IF EXISTS `gen_field_type`;

CREATE TABLE `gen_field_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `column_type` varchar(200) DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200) DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) DEFAULT NULL COMMENT '属性包名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `column_type` (`column_type`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字段类型管理';


insert  into `gen_field_type`(`id`,`column_type`,`attr_type`,`package_name`,`create_time`) values 

(1,'datetime','Date','java.util.Date','2024-01-21 01:57:20'),

(2,'date','Date','java.util.Date','2024-01-21 01:57:20'),

(3,'tinyint','Integer',NULL,'2024-01-21 01:57:20'),

(4,'smallint','Integer',NULL,'2024-01-21 01:57:20'),

(5,'mediumint','Integer',NULL,'2024-01-21 01:57:20'),

(6,'int','Integer',NULL,'2024-01-21 01:57:20'),

(7,'integer','Integer',NULL,'2024-01-21 01:57:21'),

(8,'bigint','Long',NULL,'2024-01-21 01:57:21'),

(9,'float','Float',NULL,'2024-01-21 01:57:21'),

(10,'double','Double',NULL,'2024-01-21 01:57:21'),

(11,'decimal','BigDecimal','java.math.BigDecimal','2024-01-21 01:57:21'),

(12,'bit','Boolean',NULL,'2024-01-21 01:57:21'),

(13,'char','String',NULL,'2024-01-21 01:57:21'),

(14,'varchar','String',NULL,'2024-01-21 01:57:21'),

(15,'tinytext','String',NULL,'2024-01-21 01:57:21'),

(16,'text','String',NULL,'2024-01-21 01:57:21'),

(17,'mediumtext','String',NULL,'2024-01-21 01:57:21'),

(18,'longtext','String',NULL,'2024-01-21 01:57:21'),

(19,'timestamp','Date','java.util.Date','2024-01-21 01:57:21'),

(20,'NUMBER','Integer',NULL,'2024-01-21 01:57:21'),

(21,'BINARY_INTEGER','Integer',NULL,'2024-01-21 01:57:21'),

(22,'BINARY_FLOAT','Float',NULL,'2024-01-21 01:57:21'),

(23,'BINARY_DOUBLE','Double',NULL,'2024-01-21 01:57:21'),

(24,'VARCHAR2','String',NULL,'2024-01-21 01:57:21'),

(25,'NVARCHAR','String',NULL,'2024-01-21 01:57:21'),

(26,'NVARCHAR2','String',NULL,'2024-01-21 01:57:21'),

(27,'CLOB','String',NULL,'2024-01-21 01:57:21'),

(28,'int8','Long',NULL,'2024-01-21 01:57:21'),

(29,'int4','Integer',NULL,'2024-01-21 01:57:21'),

(30,'int2','Integer',NULL,'2024-01-21 01:57:21'),

(31,'numeric','BigDecimal','java.math.BigDecimal','2024-01-21 01:57:21');

DROP TABLE IF EXISTS `gen_project_modify`;

CREATE TABLE `gen_project_modify` (
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='项目名变更';


insert  into `gen_project_modify`(`id`,`project_name`,`project_code`,`project_package`,`project_path`,`modify_project_name`,`modify_project_code`,`modify_project_package`,`exclusions`,`modify_suffix`,`modify_tmp_path`,`create_time`) values 

(1,'maku-boot','maku','net.maku','D:/makunet/maku-boot','baba-boot','baba','com.baba','.git,.idea,target,logs','java,xml,yml,txt',NULL,'2024-01-21 01:57:21'),

(2,'maku-cloud','maku','net.maku','D:/makunet/maku-cloud','baba-cloud','baba','com.baba','.git,.idea,target,logs','java,xml,yml,txt',NULL,'2024-01-21 01:57:21');


DROP TABLE IF EXISTS `gen_table`;

CREATE TABLE `gen_table` (
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
  `form_layout` tinyint DEFAULT NULL COMMENT '表单布局  1：一列   2：两列',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `baseclass_id` bigint DEFAULT NULL COMMENT '基类ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_name` (`table_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成表';


insert  into `gen_table`(`id`,`table_name`,`class_name`,`table_comment`,`author`,`email`,`package_name`,`version`,`generator_type`,`backend_path`,`frontend_path`,`module_name`,`function_name`,`form_layout`,`datasource_id`,`baseclass_id`,`create_time`) values 

(3,'sys_tenant','SysTenant','系统租户;','阿沐','babamu@126.com','net.maku','1.0.0',0,'D:\\generator\\maku-boot\\maku-server','D:\\generator\\maku-admin','maku','sys_tenant',1,0,NULL,'2024-01-21 21:25:25');


DROP TABLE IF EXISTS `gen_table_field`;

CREATE TABLE `gen_table_field` (
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
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成表字段';


insert  into `gen_table_field`(`id`,`table_id`,`field_name`,`field_type`,`field_comment`,`attr_name`,`attr_type`,`package_name`,`sort`,`auto_fill`,`primary_pk`,`base_field`,`form_item`,`form_required`,`form_type`,`form_dict`,`form_validator`,`grid_item`,`grid_sort`,`query_item`,`query_type`,`query_form_type`) values 

(51,3,'tenant_id','varchar','租户编号','tenantId','String',NULL,0,'DEFAULT',1,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(52,3,'name','varchar','租户名','name','String',NULL,1,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(53,3,'contact_user_id','varchar','联系人的用户编号','contactUserId','String',NULL,2,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(54,3,'contact_name','varchar','联系人','contactName','String',NULL,3,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(55,3,'contact_mobile','varchar','联系手机','contactMobile','String',NULL,4,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(56,3,'status','int','租户状态;0正常 1停用','status','Integer',NULL,6,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(57,3,'domain','varchar','绑定域名','domain','String',NULL,5,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(58,3,'package_id','varchar','租户套餐编号','packageId','String',NULL,7,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(59,3,'expire_time','datetime','过期时间','expireTime','Date','java.util.Date',8,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(60,3,'account_count','int','账号数量','accountCount','Integer',NULL,9,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(61,3,'remark','varchar','备注','remark','String',NULL,10,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(62,3,'revision','varchar','乐观锁','revision','String',NULL,11,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(63,3,'created_by','varchar','创建人','createdBy','String',NULL,12,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(64,3,'created_time','datetime','创建时间','createdTime','Date','java.util.Date',13,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(65,3,'updated_by','varchar','更新人','updatedBy','String',NULL,14,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text'),

(66,3,'updated_time','datetime','更新时间','updatedTime','Date','java.util.Date',15,'DEFAULT',0,0,1,0,'text',NULL,NULL,1,0,0,'=','text');


DROP TABLE IF EXISTS `gen_test_student`;

CREATE TABLE `gen_test_student` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学生ID',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别',
  `age` int DEFAULT NULL COMMENT '年龄',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `version` int DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint DEFAULT NULL COMMENT '删除标识',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试2';

