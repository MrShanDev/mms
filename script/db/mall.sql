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
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `store_name` varchar(255) DEFAULT NULL COMMENT '店铺名称',
  `store_type` int DEFAULT NULL COMMENT '店铺类型;1:企业  2:个人',
  `business_state` int DEFAULT NULL COMMENT '营业状态;0.禁用 1.营业  2.休业',
  `store_address` varchar(255) DEFAULT NULL COMMENT '店铺地址',
  `store_longitude` varchar(255) DEFAULT NULL COMMENT '店铺经度',
  `store_latitude` varchar(255) DEFAULT NULL COMMENT '店铺维度',
  `store_logo` varchar(500) DEFAULT NULL COMMENT '店铺logo',
  `store_bg_img` varchar(255) DEFAULT NULL COMMENT '店铺门头',
  `store_introduction` varchar(500) DEFAULT NULL COMMENT '店铺简介',
  `store_business_time` varchar(255) DEFAULT NULL COMMENT '营业时间',
  `store_phone` varchar(255) DEFAULT NULL COMMENT '店铺电话',
  `store_community` varchar(255) DEFAULT NULL COMMENT '服务社区',
  `store_community_mark` varchar(500) DEFAULT NULL COMMENT '社区描述',
  `monthly_sales` decimal(24,6) DEFAULT '0.000000' COMMENT '月销售额',
  `distribution_price` decimal(24,6) DEFAULT '0.000000' COMMENT '配送费用',
  `store_rank` varchar(255) DEFAULT '1' COMMENT '店铺星级',
  `store_score` varchar(255) DEFAULT '5' COMMENT '店铺评分',
  `starting_price` decimal(24,6) DEFAULT '0.000000' COMMENT '起送订单金额',
  `store_audit_state` int DEFAULT '0' COMMENT '审核状态',
  `store_audit_why` varchar(255) DEFAULT NULL COMMENT '审核原因',
  `store_img_one` varchar(255) DEFAULT NULL COMMENT '店铺备用装修图片1',
  `store_img_two` varchar(255) DEFAULT NULL COMMENT '店铺备用装修图片2',
  `store_img_three` varchar(255) DEFAULT NULL COMMENT '店铺备用装修图片3',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '0' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='店铺;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_advertising`
--

DROP TABLE IF EXISTS `store_advertising`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_advertising` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `advertising_id` varchar(255) DEFAULT NULL COMMENT '广告位ID',
  `start_time` varchar(255) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(255) DEFAULT NULL COMMENT '到期时间',
  `route_url` varchar(255) DEFAULT NULL COMMENT '路由地址',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `route_parameter` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '0' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='广告;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_advertising`
--

LOCK TABLES `store_advertising` WRITE;
/*!40000 ALTER TABLE `store_advertising` DISABLE KEYS */;
INSERT INTO `store_advertising` VALUES ('1913848039957815297','1913526696238272513','2025-04-01 00:00:00','2025-12-31 00:00:00','#','https://sxpcwlkj-test.oss-accelerate.aliyuncs.com/mmsMall/upload/6804cdb67f6e6d3001df14fb.jpeg','#',0,1,'2','000001','1','2025-04-20 14:51:45','1','2025-04-20 18:34:34','');
/*!40000 ALTER TABLE `store_advertising` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_advertising_location`
--

DROP TABLE IF EXISTS `store_advertising_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_advertising_location` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '广告位名称',
  `height` varchar(255) DEFAULT NULL COMMENT '广告位高度',
  `width` varchar(255) DEFAULT NULL COMMENT '广告位宽度',
  `code` varchar(255) DEFAULT NULL COMMENT '广告位编码',
  `max_num` varchar(255) DEFAULT '1' COMMENT '最大显示数量',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '0' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='广告位;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_advertising_location`
--

LOCK TABLES `store_advertising_location` WRITE;
/*!40000 ALTER TABLE `store_advertising_location` DISABLE KEYS */;
INSERT INTO `store_advertising_location` VALUES ('1913526696238272513','小程序启动图','100vh','100vw','APPLE_STARE','1',0,1,'3','000001','1','2025-04-19 17:34:51','1','2025-04-20 11:14:19','');
/*!40000 ALTER TABLE `store_advertising_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_member`
--

DROP TABLE IF EXISTS `store_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_member` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `account` varchar(255) NOT NULL COMMENT '账号',
  `sex` int DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) NOT NULL COMMENT '手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `head_portrait` varchar(255) DEFAULT NULL COMMENT '头像',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `reputation_score` int DEFAULT NULL COMMENT '信用分',
  `level` int DEFAULT NULL COMMENT '级别',
  `invitation_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
  `private_key` varchar(255) DEFAULT NULL COMMENT '秘钥',
  `wx_openid` varchar(255) DEFAULT NULL COMMENT '微信openid',
  `alipay_openid` varchar(255) DEFAULT NULL COMMENT '支付宝openId',
  `douyin_openid` varchar(255) DEFAULT NULL COMMENT '抖音openId',
  `last_login_ip` varchar(255) DEFAULT NULL COMMENT '最后登录IP',
  `pay_password` varchar(255) DEFAULT NULL COMMENT '支付密码',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '0' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_member`
--

LOCK TABLES `store_member` WRITE;
/*!40000 ALTER TABLE `store_member` DISABLE KEYS */;
INSERT INTO `store_member` VALUES ('1913924837450182658','西决','13389186557',1,'13389186557','e10adc3949ba59abbe56e057f20f883e','https://sxpcwlkj-test.oss-accelerate.aliyuncs.com/mmsMall/upload/6804e8af7f6e5caa31665f10.png','1990-01-04 00:00:00',100,1,'M5DXHA',NULL,NULL,NULL,NULL,'0:0:0:0:0:0:0:1',NULL,1,0,'1',NULL,NULL,'2025-04-20 19:56:55',NULL,'2025-04-20 19:56:55',NULL);
/*!40000 ALTER TABLE `store_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_member_address`
--

DROP TABLE IF EXISTS `store_member_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_member_address` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `member_id` varchar(255) DEFAULT NULL COMMENT '会员ID',
  `name` varchar(255) DEFAULT NULL COMMENT '收货人',
  `phone` varchar(255) DEFAULT NULL COMMENT '收货手机号',
  `country` varchar(255) DEFAULT NULL COMMENT '国家',
  `province` varchar(255) DEFAULT NULL COMMENT '省',
  `city` varchar(255) DEFAULT NULL COMMENT '市',
  `district` varchar(255) DEFAULT NULL COMMENT '区/县',
  `address` varchar(500) DEFAULT NULL COMMENT '详细地址',
  `is_def` varchar(255) DEFAULT NULL COMMENT '是否默认',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '0' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员收货地址;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_member_address`
--

LOCK TABLES `store_member_address` WRITE;
/*!40000 ALTER TABLE `store_member_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_member_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_member_authentication`
--

DROP TABLE IF EXISTS `store_member_authentication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_member_authentication` (
  `id` varchar(255) DEFAULT NULL COMMENT 'ID',
  `member_id` varchar(255) DEFAULT NULL COMMENT '会员ID',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `number` varchar(255) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `image_front` varchar(255) DEFAULT NULL COMMENT '身份证正面',
  `image_back` varchar(255) DEFAULT NULL COMMENT '身份证背面',
  `business_license` varchar(255) DEFAULT NULL COMMENT '营业执照',
  `sex` varchar(255) DEFAULT NULL COMMENT '性别',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `nationality` varchar(255) DEFAULT NULL COMMENT '生日',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '0' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员认证;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_member_authentication`
--

LOCK TABLES `store_member_authentication` WRITE;
/*!40000 ALTER TABLE `store_member_authentication` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_member_authentication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_function`
--

DROP TABLE IF EXISTS `sys_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_function` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父菜单ID',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由地址',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单名称',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `redirect_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '重定向路由',
  `language_code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'i18n编码',
  `component_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件名',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `type` int DEFAULT '1' COMMENT '菜单类型',
  `sort` int DEFAULT '0' COMMENT '显示顺序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单图标',
  `status` int DEFAULT '0' COMMENT '菜单状态',
  `visible` int DEFAULT '-1' COMMENT '是否可见',
  `is_iframe` int DEFAULT '-1' COMMENT '是否内嵌',
  `is_open_link` int DEFAULT '-1' COMMENT '是否外链',
  `is_link` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '外链path/内嵌url',
  `keep_alive` int DEFAULT '1' COMMENT '是否缓存',
  `always_show` int DEFAULT '-1' COMMENT '是否总是显示',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户号',
  `revision` int DEFAULT '-1' COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1913853766319398951 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统功能';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_function`
--

LOCK TABLES `sys_function` WRITE;
/*!40000 ALTER TABLE `sys_function` DISABLE KEYS */;
INSERT INTO `sys_function` VALUES (1,'0','/','系统菜单','','/home',NULL,'home','system:model:menu',1,1,'ele-SetUp',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-11 14:41:00'),(2,'1','/index','控制台','system/home/index','','message.router.home',NULL,'super_admin,admin',1,1,'iconfont icon-laptop',0,-1,-1,-1,'',1,1,NULL,'000000',1,NULL,NULL,'1','2024-11-11 15:43:26'),(3,'1','/system','系统管理','','/system/menu','message.router.system',NULL,'super_admin,admin',1,98,'iconfont icon-cog',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:36:40'),(4,'3','/system/menu','菜单列表','system/menu/index',NULL,'message.router.systemMenu',NULL,'super_admin,admin',1,3,'iconfont icon-Directory-tree',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:38:51'),(5,'3','/system/user','用户列表','/system/user/index',NULL,'message.router.systemUser',NULL,'super_admin,admin',1,1,'iconfont icon-user-group',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:25:37'),(7,'3','/system/dict','字典列表','system/dict/index',NULL,'message.router.systemDic',NULL,'super_admin,admin',1,5,'iconfont icon-databaseplus-fill',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:39:54'),(8,'3','/system/role','角色列表','system/role/index',NULL,'message.router.systemRole',NULL,'super_admin,admin',1,2,'iconfont icon-application',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:36:27'),(9,'5','','用户列表-列表','',NULL,'用户列表',NULL,'system:user:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:28'),(10,'5','','用户列表-编辑','',NULL,'用户编辑',NULL,'system:user:edit',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:50'),(11,'5','','用户列表-查询','',NULL,'用户查询',NULL,'system:user:query',2,1,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:17'),(12,'5','','用户列表-添加','',NULL,'用户新增',NULL,'system:user:insert',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:23:06'),(13,'5','','用户列表-删除','',NULL,'用户删除',NULL,'system:user:delete',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:23:15'),(14,'7','','列表','',NULL,'','','system:dict:list',2,0,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-14 19:37:47','1','2024-01-14 19:45:32'),(15,'7','','新增','',NULL,'','','system:dict:insert',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-14 19:48:13','1','2024-07-07 16:50:27'),(18,'3','/system/generate','生成代码','system/generate/index',NULL,'message.gen.generator','','',1,10,'iconfont icon-resource',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-21 12:42:40','1','2024-12-05 11:53:51'),(25,'3','/tools/powerjob','PowerJob','tools/powerjob',NULL,'message.tools.powerjob','','',1,2,'iconfont icon-calendar-alt',0,-1,-1,-1,'/mms-job/#/welcome',-1,-1,NULL,'000000',1,'1','2024-01-22 01:38:42','1','2025-04-18 17:02:43'),(26,'3','/system/oss','对象存储','system/oss/index',NULL,'message.router.oss','','',1,6,'iconfont icon-cloudupload',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-22 23:11:02','1','2024-11-10 11:40:13'),(1793580098460786690,'2','/personal','个人中心','system/personal/index',NULL,'个人中心','','',1,2,'iconfont icon-account',0,1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-05-23 17:49:53','1','2024-11-11 15:42:53'),(1809850786723254273,'4','','列表','',NULL,'','','system:function:list',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:23:48','1','2024-07-07 15:26:13'),(1809850967275458562,'4','','查询','',NULL,'','','system:function:query',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:24:31','1','2024-07-07 15:24:31'),(1809851073483624450,'4','','编辑','',NULL,'','','system:function:edit',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:24:56','1','2024-07-07 15:24:56'),(1809851230254125057,'4','','新增','',NULL,'','','system:function:insert',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:25:33','1','2024-07-07 15:26:23'),(1809851347346509825,'4','','删除','',NULL,'','','system:function:delete',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:26:01','1','2024-07-07 15:26:01'),(1809852682548662273,'8','','列表','',NULL,'','','system:role:list',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:31:20','1','2024-07-07 15:31:20'),(1809852753004580866,'8','','查询','',NULL,'','','system:role:query',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:31:36','1','2024-07-07 15:31:36'),(1809852844230692866,'8','','编辑','',NULL,'','','system:role:edit',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:31:58','1','2024-07-07 15:31:58'),(1809852932239773698,'8','','新增','',NULL,'','','system:role:insert',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:32:19','1','2024-07-07 15:32:19'),(1809853053874589698,'8','','删除','',NULL,'','','system:role:delete',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:32:48','1','2024-07-07 15:32:48'),(1809854968423260162,'7','','查询','',NULL,'','','system:dict:query',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:40:25','1','2024-07-07 15:40:43'),(1809855292018008066,'7','','编辑','',NULL,'','','system:dict:edit',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:41:42','1','2024-07-07 15:41:42'),(1809855369251921921,'7','','删除','',NULL,'','','system:dict:delete',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:42:00','1','2024-07-07 15:42:00'),(1809954418709798913,'26','','列表','',NULL,'','','system:oss:list',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:15:35','1','2024-07-07 22:15:35'),(1809954506823737346,'26','','查询','',NULL,'','','system:oss:query',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:15:56','1','2024-07-07 22:15:56'),(1809954598532194306,'26','','编辑','',NULL,'','','system:oss:edit',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:16:18','1','2024-07-07 22:16:18'),(1809954692224557058,'26','','新增','',NULL,'','','system:oss:insert',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:16:41','1','2024-07-07 22:16:41'),(1809954777297625090,'26','','删除','',NULL,'','','system:oss:delete',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:17:01','1','2024-07-07 22:17:01'),(1854788379443027970,'2','','左侧菜单树','',NULL,'','','system:model:menu',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-08 15:29:45','1','2024-11-11 15:32:22'),(1855872016117329921,'2','','控制台详情','',NULL,'','','system:model:common',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-11 15:15:44','1','2024-11-11 15:32:02'),(1856203155570196481,'5','','用户列表-导入','',NULL,'','','system:user:import',2,6,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-12 13:11:34','1','2024-11-12 13:11:34'),(1856206343090241538,'5','','用户列表-导出','',NULL,'','','system:user:export',2,7,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-12 13:24:13','1','2024-11-12 13:32:45'),(1856208397166739457,'5','','用户列表-打印','',NULL,'','','system:user:print',2,8,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-12 13:32:23','1','2024-11-12 13:32:37'),(1856208397166739476,'3','/system/config','系统配置','system/config/index',NULL,'系统配置',NULL,NULL,1,7,'iconfont icon-application',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57','1','2024-12-09 16:02:26'),(1856208397166739477,'1856208397166739476',NULL,'配置表-列表',NULL,NULL,NULL,NULL,'system:config:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739478,'1856208397166739476',NULL,'配置表-新增',NULL,NULL,NULL,NULL,'system:config:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739479,'1856208397166739476',NULL,'配置表-删除',NULL,NULL,NULL,NULL,'system:config:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739480,'1856208397166739476',NULL,'配置表-编辑',NULL,NULL,NULL,NULL,'system:config:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739481,'1856208397166739476',NULL,'配置表-查询',NULL,NULL,NULL,NULL,'system:config:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739482,'1856208397166739476',NULL,'配置表-导入',NULL,NULL,NULL,NULL,'system:config:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739483,'1856208397166739476',NULL,'配置表-导出',NULL,NULL,NULL,NULL,'system:config:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739484,'1856208397166739476',NULL,'配置表-打印',NULL,NULL,NULL,NULL,'system:config:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739485,'3','/system/dept','系统部门','system/dept/index',NULL,'系统部门',NULL,NULL,1,4,'iconfont icon-Directory-tree',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07','1','2024-12-09 16:25:06'),(1856208397166739486,'1856208397166739485',NULL,'系统部门-列表',NULL,NULL,NULL,NULL,'system:dept:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739487,'1856208397166739485',NULL,'系统部门-新增',NULL,NULL,NULL,NULL,'system:dept:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739488,'1856208397166739485',NULL,'系统部门-删除',NULL,NULL,NULL,NULL,'system:dept:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739489,'1856208397166739485',NULL,'系统部门-编辑',NULL,NULL,NULL,NULL,'system:dept:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739490,'1856208397166739485',NULL,'系统部门-查询',NULL,NULL,NULL,NULL,'system:dept:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739491,'1856208397166739485',NULL,'系统部门-导入',NULL,NULL,NULL,NULL,'system:dept:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739492,'1856208397166739485',NULL,'系统部门-导出',NULL,NULL,NULL,NULL,'system:dept:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739493,'1856208397166739485',NULL,'系统部门-打印',NULL,NULL,NULL,NULL,'system:dept:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739494,'3','/system/notice','系统公告','system/notice/index',NULL,'系统公告',NULL,NULL,1,8,'ele-ChatLineRound',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23','1','2024-12-09 16:26:09'),(1856208397166739495,'1856208397166739494',NULL,'系统公告-列表',NULL,NULL,NULL,NULL,'system:notice:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739496,'1856208397166739494',NULL,'系统公告-新增',NULL,NULL,NULL,NULL,'system:notice:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739497,'1856208397166739494',NULL,'系统公告-删除',NULL,NULL,NULL,NULL,'system:notice:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739498,'1856208397166739494',NULL,'系统公告-编辑',NULL,NULL,NULL,NULL,'system:notice:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739499,'1856208397166739494',NULL,'系统公告-查询',NULL,NULL,NULL,NULL,'system:notice:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739500,'1856208397166739494',NULL,'系统公告-导入',NULL,NULL,NULL,NULL,'system:notice:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739501,'1856208397166739494',NULL,'系统公告-导出',NULL,NULL,NULL,NULL,'system:notice:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739502,'1856208397166739494',NULL,'系统公告-打印',NULL,NULL,NULL,NULL,'system:notice:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1913158778526797825,'1','/advertising','广告管理','',NULL,'广告管理','','',1,90,'iconfont icon-compass',0,-1,-1,-1,'',-1,-1,NULL,'000001',2,'1','2025-04-18 17:12:52','1','2025-04-20 15:15:18'),(1913158778526797826,'1913158778526797825','/sxpcwlkj/storeAdvertisingLocation','广告位置','sxpcwlkj/storeAdvertisingLocation/index',NULL,'广告位置',NULL,NULL,1,2,'iconfont icon-question-circle',0,-1,-1,-1,'',1,-1,NULL,'000000',3,NULL,'2025-04-18 19:57:21','1','2025-04-20 15:12:37'),(1913158778526797827,'1913158778526797826',NULL,'广告位;-列表',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797828,'1913158778526797826',NULL,'广告位;-新增',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797829,'1913158778526797826',NULL,'广告位;-删除',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797830,'1913158778526797826',NULL,'广告位;-编辑',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797831,'1913158778526797826',NULL,'广告位;-查询',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797832,'1913158778526797826',NULL,'广告位;-导入',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797833,'1913158778526797826',NULL,'广告位;-导出',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797834,'1913158778526797826',NULL,'广告位;-打印',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertisingLocation:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-18 19:57:21',NULL,'2025-04-18 19:57:21'),(1913158778526797835,'1913158778526797825','/sxpcwlkj/storeAdvertising','广告设置','sxpcwlkj/storeAdvertising/index',NULL,'广告设置',NULL,NULL,1,1,'iconfont icon-question-circle',0,-1,-1,-1,'',1,-1,NULL,'000000',3,NULL,'2025-04-19 16:56:18','1','2025-04-20 15:12:13'),(1913158778526797836,'1913158778526797835',NULL,'广告-列表',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913158778526797837,'1913158778526797835',NULL,'广告-新增',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913158778526797838,'1913158778526797835',NULL,'广告-删除',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913158778526797839,'1913158778526797835',NULL,'广告-编辑',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913158778526797840,'1913158778526797835',NULL,'广告-查询',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913158778526797841,'1913158778526797835',NULL,'广告-导入',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913158778526797842,'1913158778526797835',NULL,'广告-导出',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913158778526797843,'1913158778526797835',NULL,'广告-打印',NULL,NULL,NULL,NULL,'sxpcwlkj:storeAdvertising:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-19 16:56:18',NULL,'2025-04-19 16:56:18'),(1913853612329721857,'1','/member','会员管理','',NULL,'会员管理','','',1,10,'ele-Avatar',0,-1,-1,-1,'',-1,-1,NULL,'000001',2,'1','2025-04-20 15:13:54','1','2025-04-20 15:15:05'),(1913853766319398914,'1','/store','店铺管理','',NULL,'店铺管理','','',1,20,'iconfont icon-home',0,-1,-1,-1,'',-1,-1,NULL,'000001',1,'1','2025-04-20 15:14:30','1','2025-04-20 15:14:30'),(1913853766319398915,'1913853612329721857','/sxpcwlkj/storeMemberAddress','会员收货地址','sxpcwlkj/storeMemberAddress/index',NULL,'会员收货地址',NULL,NULL,1,4,'',0,1,-1,-1,'',1,-1,NULL,'000000',3,NULL,'2025-04-20 15:38:33','1','2025-04-20 15:42:43'),(1913853766319398916,'1913853766319398915',NULL,'会员收货地址-列表',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:33',NULL,'2025-04-20 15:38:33'),(1913853766319398917,'1913853766319398915',NULL,'会员收货地址-新增',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:33',NULL,'2025-04-20 15:38:33'),(1913853766319398918,'1913853766319398915',NULL,'会员收货地址-删除',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:33',NULL,'2025-04-20 15:38:33'),(1913853766319398919,'1913853766319398915',NULL,'会员收货地址-编辑',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:33',NULL,'2025-04-20 15:38:33'),(1913853766319398920,'1913853766319398915',NULL,'会员收货地址-查询',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:33',NULL,'2025-04-20 15:38:33'),(1913853766319398921,'1913853766319398915',NULL,'会员收货地址-导入',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:34',NULL,'2025-04-20 15:38:34'),(1913853766319398922,'1913853766319398915',NULL,'会员收货地址-导出',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:34',NULL,'2025-04-20 15:38:34'),(1913853766319398923,'1913853766319398915',NULL,'会员收货地址-打印',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAddress:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:34',NULL,'2025-04-20 15:38:34'),(1913853766319398924,'1913853612329721857','/sxpcwlkj/storeMemberAuthentication','会员认证','sxpcwlkj/storeMemberAuthentication/index',NULL,'会员认证',NULL,NULL,1,3,'',0,1,-1,-1,'',1,-1,NULL,'000000',3,NULL,'2025-04-20 15:38:47','1','2025-04-20 15:42:38'),(1913853766319398925,'1913853766319398924',NULL,'会员认证-列表',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398926,'1913853766319398924',NULL,'会员认证-新增',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398927,'1913853766319398924',NULL,'会员认证-删除',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398928,'1913853766319398924',NULL,'会员认证-编辑',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398929,'1913853766319398924',NULL,'会员认证-查询',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398930,'1913853766319398924',NULL,'会员认证-导入',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398931,'1913853766319398924',NULL,'会员认证-导出',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398932,'1913853766319398924',NULL,'会员认证-打印',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMemberAuthentication:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:38:47',NULL,'2025-04-20 15:38:47'),(1913853766319398933,'1913853612329721857','/sxpcwlkj/storeMember','会员列表','sxpcwlkj/storeMember/index',NULL,'会员列表',NULL,NULL,1,1,'',0,-1,-1,-1,'',1,-1,NULL,'000000',2,NULL,'2025-04-20 15:39:01','1','2025-04-20 15:42:18'),(1913853766319398934,'1913853766319398933',NULL,'会员-列表',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398935,'1913853766319398933',NULL,'会员-新增',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398936,'1913853766319398933',NULL,'会员-删除',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398937,'1913853766319398933',NULL,'会员-编辑',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398938,'1913853766319398933',NULL,'会员-查询',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398939,'1913853766319398933',NULL,'会员-导入',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398940,'1913853766319398933',NULL,'会员-导出',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398941,'1913853766319398933',NULL,'会员-打印',NULL,NULL,NULL,NULL,'sxpcwlkj:storeMember:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:01',NULL,'2025-04-20 15:39:01'),(1913853766319398942,'1913853766319398914','/sxpcwlkj/store','店铺列表','sxpcwlkj/store/index',NULL,'店铺列表',NULL,NULL,1,1,'',0,-1,-1,-1,'',1,-1,NULL,'000000',2,NULL,'2025-04-20 15:39:13','1','2025-04-20 15:44:43'),(1913853766319398943,'1913853766319398942',NULL,'店铺-列表',NULL,NULL,NULL,NULL,'sxpcwlkj:store:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:13',NULL,'2025-04-20 15:39:13'),(1913853766319398944,'1913853766319398942',NULL,'店铺-新增',NULL,NULL,NULL,NULL,'sxpcwlkj:store:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:13',NULL,'2025-04-20 15:39:13'),(1913853766319398945,'1913853766319398942',NULL,'店铺-删除',NULL,NULL,NULL,NULL,'sxpcwlkj:store:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:14',NULL,'2025-04-20 15:39:14'),(1913853766319398946,'1913853766319398942',NULL,'店铺-编辑',NULL,NULL,NULL,NULL,'sxpcwlkj:store:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:14',NULL,'2025-04-20 15:39:14'),(1913853766319398947,'1913853766319398942',NULL,'店铺-查询',NULL,NULL,NULL,NULL,'sxpcwlkj:store:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:14',NULL,'2025-04-20 15:39:14'),(1913853766319398948,'1913853766319398942',NULL,'店铺-导入',NULL,NULL,NULL,NULL,'sxpcwlkj:store:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:14',NULL,'2025-04-20 15:39:14'),(1913853766319398949,'1913853766319398942',NULL,'店铺-导出',NULL,NULL,NULL,NULL,'sxpcwlkj:store:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:14',NULL,'2025-04-20 15:39:14'),(1913853766319398950,'1913853766319398942',NULL,'店铺-打印',NULL,NULL,NULL,NULL,'sxpcwlkj:store:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2025-04-20 15:39:14',NULL,'2025-04-20 15:39:14');
/*!40000 ALTER TABLE `sys_function` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-20 21:24:46
