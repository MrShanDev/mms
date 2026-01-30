-- MySQL dump 10.13  Distrib 8.0.41, for macos15.2 (arm64)
--
-- Host: 127.0.0.1    Database: mms
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_gen_base_class`
--

DROP TABLE IF EXISTS `sys_gen_base_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_gen_base_class` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `package_name` varchar(200) DEFAULT NULL COMMENT '基类包名',
  `code` varchar(200) DEFAULT NULL COMMENT '基类编码',
  `fields` varchar(500) DEFAULT NULL COMMENT '基类字段，多个用英文逗号分隔',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1865243692293963778 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='基类管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_base_class`
--

LOCK TABLES `sys_gen_base_class` WRITE;
/*!40000 ALTER TABLE `sys_gen_base_class` DISABLE KEYS */;
INSERT INTO `sys_gen_base_class` VALUES (1,'com.sxpcwlkj.datasource.entity.BaseEntity','BaseEntity','status,sort,revision,tenant_id,created_by,created_time,updated_by,updated_time,remark','使用该基类，则需要表里有这些字段。','2024-01-21 01:57:21'),(1865243692293963777,'com.sxpcwlkj.framework.entity','BaseEntityVo','sort,created_time,remark','vo基类','2024-12-07 11:55:25');
/*!40000 ALTER TABLE `sys_gen_base_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gen_datasource`
--

DROP TABLE IF EXISTS `sys_gen_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_gen_datasource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `db_type` varchar(200) DEFAULT NULL COMMENT '数据库类型',
  `conn_name` varchar(200) NOT NULL COMMENT '连接名',
  `conn_url` varchar(500) DEFAULT NULL COMMENT 'URL',
  `username` varchar(200) DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_datasource`
--

LOCK TABLES `sys_gen_datasource` WRITE;
/*!40000 ALTER TABLE `sys_gen_datasource` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_gen_datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gen_field_type`
--

DROP TABLE IF EXISTS `sys_gen_field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_gen_field_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `column_type` varchar(200) DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200) DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) DEFAULT NULL COMMENT '属性包名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `column_type` (`column_type`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字段类型管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_field_type`
--

LOCK TABLES `sys_gen_field_type` WRITE;
/*!40000 ALTER TABLE `sys_gen_field_type` DISABLE KEYS */;
INSERT INTO `sys_gen_field_type` VALUES (1,'datetime','Date','java.util.Date','2024-01-21 01:57:20'),(2,'date','Date','java.util.Date','2024-01-21 01:57:20'),(3,'tinyint','Integer',NULL,'2024-01-21 01:57:20'),(4,'smallint','Integer',NULL,'2024-01-21 01:57:20'),(5,'mediumint','Integer',NULL,'2024-01-21 01:57:20'),(6,'int','Integer',NULL,'2024-01-21 01:57:20'),(7,'integer','Integer',NULL,'2024-01-21 01:57:21'),(8,'bigint','Long',NULL,'2024-01-21 01:57:21'),(9,'float','Float',NULL,'2024-01-21 01:57:21'),(10,'double','Double',NULL,'2024-01-21 01:57:21'),(11,'decimal','BigDecimal','java.math.BigDecimal','2024-01-21 01:57:21'),(12,'bit','Boolean',NULL,'2024-01-21 01:57:21'),(13,'char','String',NULL,'2024-01-21 01:57:21'),(14,'varchar','String',NULL,'2024-01-21 01:57:21'),(15,'tinytext','String',NULL,'2024-01-21 01:57:21'),(16,'text','String',NULL,'2024-01-21 01:57:21'),(17,'mediumtext','String',NULL,'2024-01-21 01:57:21'),(18,'longtext','String',NULL,'2024-01-21 01:57:21'),(19,'timestamp','Date','java.util.Date','2024-01-21 01:57:21'),(20,'NUMBER','Integer',NULL,'2024-01-21 01:57:21'),(21,'BINARY_INTEGER','Integer',NULL,'2024-01-21 01:57:21'),(22,'BINARY_FLOAT','Float',NULL,'2024-01-21 01:57:21'),(23,'BINARY_DOUBLE','Double',NULL,'2024-01-21 01:57:21'),(24,'VARCHAR2','String',NULL,'2024-01-21 01:57:21'),(25,'NVARCHAR','String',NULL,'2024-01-21 01:57:21'),(26,'NVARCHAR2','String',NULL,'2024-01-21 01:57:21'),(27,'CLOB','String',NULL,'2024-01-21 01:57:21'),(28,'int8','Long',NULL,'2024-01-21 01:57:21'),(29,'int4','Integer',NULL,'2024-01-21 01:57:21'),(30,'int2','Integer',NULL,'2024-01-21 01:57:21'),(31,'numeric','BigDecimal','java.math.BigDecimal','2024-01-21 01:57:21');
/*!40000 ALTER TABLE `sys_gen_field_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gen_project_modify`
--

DROP TABLE IF EXISTS `sys_gen_project_modify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='项目名变更';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_project_modify`
--

LOCK TABLES `sys_gen_project_modify` WRITE;
/*!40000 ALTER TABLE `sys_gen_project_modify` DISABLE KEYS */;
INSERT INTO `sys_gen_project_modify` VALUES (1,'MMS','mms','com.sxpcwlkj','D:/mms','mms','mms','com.sxpcwlkj','.git,.idea,target,logs','java,xml,yml,txt',NULL,'2024-01-21 01:57:21');
/*!40000 ALTER TABLE `sys_gen_project_modify` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gen_table`
--

DROP TABLE IF EXISTS `sys_gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  `form_layout` tinyint DEFAULT NULL COMMENT '表单布局  1：一列   2：两列',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `baseclass_id` bigint DEFAULT NULL COMMENT '基类ID',
  `menu_id` varchar(32) DEFAULT NULL COMMENT '菜单ID',
  `parent_id` varchar(32) DEFAULT NULL /*!80023 INVISIBLE */ COMMENT '父级节点',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `table_label` varchar(32) DEFAULT NULL COMMENT '节点Label',
  `span` int DEFAULT '24' COMMENT '表单排列',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_name` (`table_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1913853443831947266 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_table`
--

LOCK TABLES `sys_gen_table` WRITE;
/*!40000 ALTER TABLE `sys_gen_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gen_table_field`
--

DROP TABLE IF EXISTS `sys_gen_table_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1913853444012302338 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_table_field`
--

LOCK TABLES `sys_gen_table_field` WRITE;
/*!40000 ALTER TABLE `sys_gen_table_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_gen_table_field` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-25 20:44:52
