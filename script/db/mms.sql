-- MySQL dump 10.13  Distrib 8.0.25, for macos11 (x86_64)
--
-- Host: 127.0.0.1    Database: mms
-- ------------------------------------------------------
-- Server version	8.0.25

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
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `config_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置名称',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置键',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置值',
  `config_type` int DEFAULT NULL COMMENT '配置类型;1：系统内置 2：用户自定义',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `status` int DEFAULT '1' /*!80023 INVISIBLE */ COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `updated_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `tenant_id`, `revision`, `remark`, `status`, `sort`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES ('1887472152748412930','多租户状态','sys_tenant_state','2',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.41'),('1887472152811327489','邮箱状态','sys_email_state','2',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.425'),('1887472152811327490','微信配置状态','sys_wx_state','2',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.425'),('1887472152828104705','短信服务默认厂商','sys_sms_supplier_aliyun','2',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.429'),('1887472152903602178','系统名称','sys_base_title','模块化管理系统',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:07:20.352'),('1887472152937156610','短信服务厂商Key','sys_sms_accessKey_aliyun','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.455'),('1887472152979099649','系统描述','sys_base_description','模块化管理系统 Modular management system  简称：MMS 一款灵活、高效、低代码模块化管理系统',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:07:20.374'),('1887472152983293954','公众号Appid','sys_wx_mp_appid','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.465'),('1887472152983293955','邮件服务器地址','sys_email_host','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.465'),('1887472153000071170','短信服务厂商密钥','sys_sms_accessKeySecret_aliyun','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.469'),('1887472153021042689','多租户排除的的表','sys_tenant_exclusion_table','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.475'),('1887472153155260417','系统LOGO','sys_base_logo','https://www.mmsadmin.cn/logo.png',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:07:20.391'),('1887472153180426241','端口','sys_email_port','465',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.513'),('1887472153188814849','微信公众号AppSecret','sys_wx_mp_appsecret','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.515'),('1887472153213980673','短信服务厂商模版ID','sys_sms_template_id_aliyun','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.521'),('1887472153213980674','登录图形验证码','sys_base_captcha_state','1',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:07:20.404'),('1887472153218174978','邮件账号','sys_email_from','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.522'),('1887472153335615490','邮箱密码','sys_email_pass','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.55'),('1887472153335615491','小程序AppId','sys_wx_miniapp_appid','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.55'),('1887472153339809794','短信服务厂商签名','sys_sms_signature_aliyun','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.55'),('1887472153348198401','系统登录方式','sys_base_login_type','1',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:07:20.415'),('1887472153595662338','登录页背景','sys_base_login_bg','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:07:20.426'),('1887472153595662339','SSL安全连接','sys_email_ssl','1',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.612'),('1887472153599856641','小程序AppSecret','sys_wx_miniapp_appsecret','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.612'),('1887472153708908546','微信商户ID','sys_wx_pay_id','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.638'),('1887472153817960449','微信商户Appid','sys_wx_pay_appid','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.665'),('1887472154002509825','微信商户秘钥','sys_wx_pay_appsecret','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.709'),('1887472154057035778','微信商户秘钥类型','sys_wx_pay_type','1',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.722'),('1887472154111561730','微信支付回调','sys_wx_pay_notifyUrl','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.735'),('1887472154182864897','微信模式','sys_wx_model','1',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.752'),('1887472154237390850','AppToken','sys_wx_token','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.765'),('1887472154312888322','AppAesKey','sys_wx_aesKey','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.783'),('1887472154379997186','扫码关注回复内容','sys_wx_attention_msg','',1,'000000','0',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.799'),('1912124966099386370','微信商户证书','sys_wx_pay_path','',1,'000000','0',NULL,0,0,'1','2025-04-15 20:44:52','1','2025-04-15 20:44:52.14'),('1912124966208438274','退款回调','sys_wx_refund_path','',1,'000000','0',NULL,0,0,'1','2025-04-15 20:44:52','1','2025-04-15 20:44:52.165');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门编号',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父级编号',
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门名称',
  `leader` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `status` int DEFAULT NULL COMMENT '部门状态;0：正常 1：禁用',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES ('1','0','集团总部','admin','13388886557','sxpcwlkj@163.com',0,'陕西省西安市雁塔区',1,'超级管理员','000000','1','1','2024-01-08 13:11:05','1','2024-12-09 15:56:22'),('2','1','陕西分公司','xijeu','13388886557','sxpcwlkj@163.com',0,'陕西省西安市雁塔区',1,'管理员','000000','1','1','2024-01-08 13:11:08','1','2024-12-08 15:09:09'),('3','2','开发部','xijeu','13388886557','sxpcwlkj@163.com',0,'陕西省西安市雁塔区',8,'管理员','000000','1','1','2024-01-08 13:11:08','1','2024-12-09 15:47:39'),('4','2','运营部','xijeu','13388886557','sxpcwlkj@163.com',0,'陕西省西安市雁塔区',4,'管理员','000000','1','1','2024-01-08 13:11:08','1','2024-03-17 13:06:25');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典名称',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典字段',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '字典类型',
  `sort` int DEFAULT '0' COMMENT '字典排序',
  `status` int DEFAULT NULL COMMENT '状态;0正常 1停用',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES ('1745461307624050210','系统状态','SYS_STATE','1',0,0,'000000','1','1','2024-01-11 23:27:17','1','2024-11-15 11:06:02','系统公告状态'),('1745469307624050689','系统是否','SYS_IS','1',0,0,'000000','1','1','2024-01-11 23:34:47','1','2024-07-07 22:52:59','系统是否'),('1789908194852012034','性别','SYS_SEX','1',0,0,'000000','1','1','2024-05-13 14:39:03','1','2024-11-13 21:08:40','性别'),('1793520884619108353','配置类型','CONFIG_TYPE','1',0,0,'000000','1','1','2024-05-23 13:54:36','1','2024-05-23 14:29:04','配置类型'),('1793548719358361602','消息类型','NITICE_TYPE','1',0,0,'000000','1','1','2024-05-23 15:45:12','1','2024-05-23 15:46:14','消息类型'),('1800779228136251394','会员状态','MEMBER_STATE','1',0,0,'000000','1','1','2024-06-12 14:36:40','1','2024-06-12 14:51:23','会员状态'),('1809847987692126210','是否HTTPS','IS_HTTPS','1',NULL,0,'000000','1','1','2024-07-07 15:12:40','1','2024-07-07 16:52:49','是否HTTPS'),('1809872908245917698','公告类型','NOTICE_TYPE','1',NULL,0,'000000','1','1','2024-07-07 16:51:42','1','2024-07-07 16:51:42','系统公告类型'),('1809966410250162178','存储平台','OSS_TYPE','1',NULL,0,'000000','1','1','2024-07-07 23:03:14','1','2024-07-09 02:59:42','存储类型'),('1857269822656126978','账号类型','USER_TYPE','1',NULL,0,'000000','1','1','2024-11-15 11:50:07','1','2024-11-15 11:50:07','账号类型');
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典编码',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典名称',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典键值',
  `dict_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '字典排序',
  `status` int DEFAULT NULL COMMENT '状态;0正常 1停用',
  `color_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '颜色类型',
  `css_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'css 样式',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统字典值';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES ('1793529560545247234','系统设置','CONFIG_TYPE','1','0',1,0,'warning',NULL,'000000','1','1','2024-05-23 14:29:04','1','2024-05-23 14:29:04',NULL),('1793529560545247235','用户自定义','CONFIG_TYPE','2','0',1,0,'success',NULL,'000000','1','1','2024-05-23 14:29:04','1','2024-05-23 14:29:04',NULL),('1793548979082248193','公告','NITICE_TYPE','1','0',1,0,'primary',NULL,'000000','1','1','2024-05-23 15:46:14','1','2024-05-23 15:46:14',NULL),('1793548979082248194','消息','NITICE_TYPE','2','0',2,0,'info',NULL,'000000','1','1','2024-05-23 15:46:14','1','2024-05-23 15:46:14',NULL),('1800782934097444865','未激活','MEMBER_STATE','0','0',0,0,'warning',NULL,'000000','1','1','2024-06-12 14:51:23','1','2024-06-12 14:51:23',NULL),('1800782934118416386','正常','MEMBER_STATE','1','0',0,0,'success',NULL,'000000','1','1','2024-06-12 14:51:23','1','2024-06-12 14:51:23',NULL),('1800782934118416387','禁用','MEMBER_STATE','2','0',0,0,'danger',NULL,'000000','1','1','2024-06-12 14:51:23','1','2024-06-12 14:51:23',NULL),('1809872908283666434','公告','NOTICE_TYPE','1','0',1,0,'primary',NULL,'000000','1','1','2024-07-07 16:51:42','1','2024-07-07 16:51:42',NULL),('1809872908296249345','通知','NOTICE_TYPE','2','0',2,0,'success',NULL,'000000','1','1','2024-07-07 16:51:42','1','2024-07-07 16:51:42',NULL),('1809872999727882242','','isHttps','','0',0,0,'',NULL,'000000','1','1','2024-07-07 16:52:04','1','2024-07-07 16:52:04',NULL),('1809873188056326145','是','IS_HTTPS','Y','0',1,0,'primary',NULL,'000000','1','1','2024-07-07 16:52:49','1','2024-07-07 16:52:49',NULL),('1809873188077297665','否','IS_HTTPS','N','0',2,0,'warning',NULL,'000000','1','1','2024-07-07 16:52:49','1','2024-07-07 16:52:49',NULL),('1809963829939507202','是','SYS_IS','1','0',1,0,'primary',NULL,'000000','1','1','2024-07-07 22:52:59','1','2024-07-07 22:52:59',NULL),('1809963829964673025','否','SYS_IS','2','0',2,0,'warning',NULL,'000000','1','1','2024-07-07 22:52:59','1','2024-07-07 22:52:59',NULL),('1810388304412119041','本地存储','OSS_TYPE','local-plus-1','0',1,0,'primary',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304433090562','阿里云','OSS_TYPE','aliyun-oss-1','0',2,0,'success',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304445673473','腾讯云','OSS_TYPE','tencent-cos-1','0',3,0,'info',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304454062082','华为云','OSS_TYPE','huawei-obs-1','0',4,0,'warning',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304466644993','七牛云','OSS_TYPE','qiniu-kodo-1','0',5,0,'danger',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304479227905','百度云','OSS_TYPE','baidu-bos-1','0',6,0,'info',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304500199426','MinIo','OSS_TYPE','minio-1','0',7,0,'success',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304508588033','AmazonS3','OSS_TYPE','amazon-s3-1','0',8,0,'primary',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1856685610085441538','男','SYS_SEX','1','1',1,0,'',NULL,'000000','1','1','2024-11-13 21:08:40','1','2024-11-13 21:08:40',NULL),('1856685610102218754','女','SYS_SEX','2','0',2,0,'',NULL,'000000','1','1','2024-11-13 21:08:40','1','2024-11-13 21:08:40',NULL),('1856685610110607362','保密','SYS_SEX','0','0',3,0,'',NULL,'000000','1','1','2024-11-13 21:08:40','1','2024-11-13 21:08:40',NULL),('1857258730441682945','正常','SYS_STATE','0','0',0,0,'success',NULL,'000000','1','1','2024-11-15 11:06:02','1','2024-11-15 11:06:02',NULL),('1857258730466848769','禁用','SYS_STATE','1','0',1,0,'danger',NULL,'000000','1','1','2024-11-15 11:06:02','1','2024-11-15 11:06:02',NULL),('1857269822685487106','系统用户','USER_TYPE','1','0',1,0,'success',NULL,'000000','1','1','2024-11-15 11:50:07','1','2024-11-15 11:50:07',NULL),('1857269822693875713','商家用户','USER_TYPE','2','0',2,0,'warning',NULL,'000000','1','1','2024-11-15 11:50:07','1','2024-11-15 11:50:07',NULL);
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1856208397166739503 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统功能';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_function`
--

LOCK TABLES `sys_function` WRITE;
/*!40000 ALTER TABLE `sys_function` DISABLE KEYS */;
INSERT INTO `sys_function` VALUES (1,'0','/','系统菜单','','/home',NULL,'home','system:model:menu',1,1,'ele-SetUp',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-11 14:41:00'),(2,'1','/index','控制台','system/home/index','','message.router.home',NULL,'super_admin,admin',1,1,'iconfont icon-laptop',0,-1,-1,-1,'',1,1,NULL,'000000',1,NULL,NULL,'1','2024-11-11 15:43:26'),(3,'1','/system','系统管理','','/system/menu','message.router.system',NULL,'super_admin,admin',1,98,'iconfont icon-cog',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:36:40'),(4,'3','/system/menu','菜单列表','system/menu/index',NULL,'message.router.systemMenu',NULL,'super_admin,admin',1,3,'iconfont icon-Directory-tree',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:38:51'),(5,'3','/system/user','用户列表','/system/user/index',NULL,'message.router.systemUser',NULL,'super_admin,admin',1,1,'iconfont icon-user-group',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:25:37'),(7,'3','/system/dict','字典列表','system/dict/index',NULL,'message.router.systemDic',NULL,'super_admin,admin',1,5,'iconfont icon-databaseplus-fill',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:39:54'),(8,'3','/system/role','角色列表','system/role/index',NULL,'message.router.systemRole',NULL,'super_admin,admin',1,2,'iconfont icon-application',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:36:27'),(9,'5','','用户列表-列表','',NULL,'用户列表',NULL,'system:user:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:28'),(10,'5','','用户列表-编辑','',NULL,'用户编辑',NULL,'system:user:edit',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:50'),(11,'5','','用户列表-查询','',NULL,'用户查询',NULL,'system:user:query',2,1,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:17'),(12,'5','','用户列表-添加','',NULL,'用户新增',NULL,'system:user:insert',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:23:06'),(13,'5','','用户列表-删除','',NULL,'用户删除',NULL,'system:user:delete',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:23:15'),(14,'7','','列表','',NULL,'','','system:dict:list',2,0,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-14 19:37:47','1','2024-01-14 19:45:32'),(15,'7','','新增','',NULL,'','','system:dict:insert',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-14 19:48:13','1','2024-07-07 16:50:27'),(18,'3','/system/generate','生成代码','system/generate/index',NULL,'message.gen.generator','','',1,10,'iconfont icon-resource',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-21 12:42:40','1','2024-12-05 11:53:51'),(25,'3','/tools/powerjob','PowerJob','tools/powerjob',NULL,'message.tools.powerjob','','',1,2,'iconfont icon-calendar-alt',0,-1,-1,-1,'/mms-job/#/welcome',-1,-1,NULL,'000000',1,'1','2024-01-22 01:38:42','1','2025-04-18 17:02:43'),(26,'3','/system/oss','对象存储','system/oss/index',NULL,'message.router.oss','','',1,6,'iconfont icon-cloudupload',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-01-22 23:11:02','1','2024-11-10 11:40:13'),(1793580098460786690,'2','/personal','个人中心','system/personal/index',NULL,'个人中心','','',1,2,'iconfont icon-account',0,1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-05-23 17:49:53','1','2024-11-11 15:42:53'),(1809850786723254273,'4','','列表','',NULL,'','','system:function:list',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:23:48','1','2024-07-07 15:26:13'),(1809850967275458562,'4','','查询','',NULL,'','','system:function:query',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:24:31','1','2024-07-07 15:24:31'),(1809851073483624450,'4','','编辑','',NULL,'','','system:function:edit',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:24:56','1','2024-07-07 15:24:56'),(1809851230254125057,'4','','新增','',NULL,'','','system:function:insert',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:25:33','1','2024-07-07 15:26:23'),(1809851347346509825,'4','','删除','',NULL,'','','system:function:delete',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:26:01','1','2024-07-07 15:26:01'),(1809852682548662273,'8','','列表','',NULL,'','','system:role:list',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:31:20','1','2024-07-07 15:31:20'),(1809852753004580866,'8','','查询','',NULL,'','','system:role:query',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:31:36','1','2024-07-07 15:31:36'),(1809852844230692866,'8','','编辑','',NULL,'','','system:role:edit',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:31:58','1','2024-07-07 15:31:58'),(1809852932239773698,'8','','新增','',NULL,'','','system:role:insert',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:32:19','1','2024-07-07 15:32:19'),(1809853053874589698,'8','','删除','',NULL,'','','system:role:delete',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:32:48','1','2024-07-07 15:32:48'),(1809854968423260162,'7','','查询','',NULL,'','','system:dict:query',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:40:25','1','2024-07-07 15:40:43'),(1809855292018008066,'7','','编辑','',NULL,'','','system:dict:edit',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:41:42','1','2024-07-07 15:41:42'),(1809855369251921921,'7','','删除','',NULL,'','','system:dict:delete',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 15:42:00','1','2024-07-07 15:42:00'),(1809954418709798913,'26','','列表','',NULL,'','','system:oss:list',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:15:35','1','2024-07-07 22:15:35'),(1809954506823737346,'26','','查询','',NULL,'','','system:oss:query',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:15:56','1','2024-07-07 22:15:56'),(1809954598532194306,'26','','编辑','',NULL,'','','system:oss:edit',2,3,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:16:18','1','2024-07-07 22:16:18'),(1809954692224557058,'26','','新增','',NULL,'','','system:oss:insert',2,4,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:16:41','1','2024-07-07 22:16:41'),(1809954777297625090,'26','','删除','',NULL,'','','system:oss:delete',2,5,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-07-07 22:17:01','1','2024-07-07 22:17:01'),(1854788379443027970,'2','','左侧菜单树','',NULL,'','','system:model:menu',2,2,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-08 15:29:45','1','2024-11-11 15:32:22'),(1855872016117329921,'2','','控制台详情','',NULL,'','','system:model:common',2,1,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-11 15:15:44','1','2024-11-11 15:32:02'),(1856203155570196481,'5','','用户列表-导入','',NULL,'','','system:user:import',2,6,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-12 13:11:34','1','2024-11-12 13:11:34'),(1856206343090241538,'5','','用户列表-导出','',NULL,'','','system:user:export',2,7,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-12 13:24:13','1','2024-11-12 13:32:45'),(1856208397166739457,'5','','用户列表-打印','',NULL,'','','system:user:print',2,8,'',0,-1,-1,-1,'',-1,-1,NULL,'000000',1,'1','2024-11-12 13:32:23','1','2024-11-12 13:32:37'),(1856208397166739476,'3','/system/config','系统配置','system/config/index',NULL,'系统配置',NULL,NULL,1,7,'iconfont icon-application',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57','1','2024-12-09 16:02:26'),(1856208397166739477,'1856208397166739476',NULL,'配置表-列表',NULL,NULL,NULL,NULL,'system:config:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739478,'1856208397166739476',NULL,'配置表-新增',NULL,NULL,NULL,NULL,'system:config:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739479,'1856208397166739476',NULL,'配置表-删除',NULL,NULL,NULL,NULL,'system:config:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739480,'1856208397166739476',NULL,'配置表-编辑',NULL,NULL,NULL,NULL,'system:config:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739481,'1856208397166739476',NULL,'配置表-查询',NULL,NULL,NULL,NULL,'system:config:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739482,'1856208397166739476',NULL,'配置表-导入',NULL,NULL,NULL,NULL,'system:config:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739483,'1856208397166739476',NULL,'配置表-导出',NULL,NULL,NULL,NULL,'system:config:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739484,'1856208397166739476',NULL,'配置表-打印',NULL,NULL,NULL,NULL,'system:config:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739485,'3','/system/dept','系统部门','system/dept/index',NULL,'系统部门',NULL,NULL,1,4,'iconfont icon-Directory-tree',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07','1','2024-12-09 16:25:06'),(1856208397166739486,'1856208397166739485',NULL,'系统部门-列表',NULL,NULL,NULL,NULL,'system:dept:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739487,'1856208397166739485',NULL,'系统部门-新增',NULL,NULL,NULL,NULL,'system:dept:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739488,'1856208397166739485',NULL,'系统部门-删除',NULL,NULL,NULL,NULL,'system:dept:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739489,'1856208397166739485',NULL,'系统部门-编辑',NULL,NULL,NULL,NULL,'system:dept:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739490,'1856208397166739485',NULL,'系统部门-查询',NULL,NULL,NULL,NULL,'system:dept:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739491,'1856208397166739485',NULL,'系统部门-导入',NULL,NULL,NULL,NULL,'system:dept:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739492,'1856208397166739485',NULL,'系统部门-导出',NULL,NULL,NULL,NULL,'system:dept:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739493,'1856208397166739485',NULL,'系统部门-打印',NULL,NULL,NULL,NULL,'system:dept:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739494,'3','/system/notice','系统公告','system/notice/index',NULL,'系统公告',NULL,NULL,1,8,'ele-ChatLineRound',0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23','1','2024-12-09 16:26:09'),(1856208397166739495,'1856208397166739494',NULL,'系统公告-列表',NULL,NULL,NULL,NULL,'system:notice:list',2,2,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739496,'1856208397166739494',NULL,'系统公告-新增',NULL,NULL,NULL,NULL,'system:notice:insert',2,3,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739497,'1856208397166739494',NULL,'系统公告-删除',NULL,NULL,NULL,NULL,'system:notice:delete',2,4,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739498,'1856208397166739494',NULL,'系统公告-编辑',NULL,NULL,NULL,NULL,'system:notice:edit',2,5,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739499,'1856208397166739494',NULL,'系统公告-查询',NULL,NULL,NULL,NULL,'system:notice:query',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739500,'1856208397166739494',NULL,'系统公告-导入',NULL,NULL,NULL,NULL,'system:notice:import',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739501,'1856208397166739494',NULL,'系统公告-导出',NULL,NULL,NULL,NULL,'system:notice:export',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739502,'1856208397166739494',NULL,'系统公告-打印',NULL,NULL,NULL,NULL,'system:notice:print',2,6,NULL,0,-1,-1,-1,'',1,-1,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24');
/*!40000 ALTER TABLE `sys_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公告标题',
  `content` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公告内容',
  `type` int DEFAULT NULL COMMENT '公告类型;1通知 2公告',
  `status` int DEFAULT NULL COMMENT '公告状态;0正常 1关闭',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sort` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统公告;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES ('1793562396983791618','测试公告','<p>MMS-V1.0.0 开源啦！！！5555😚</p>',1,0,'','000000','1','1','2024-05-23 16:36:29','1','2024-12-09 22:19:30',1);
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss`
--

DROP TABLE IF EXISTS `sys_oss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss` (
  `oss_id` bigint NOT NULL COMMENT '对象存储主键',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原名',
  `file_suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件后缀名',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'URL地址',
  `content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'MIME 类型;',
  `base_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '基础存储路径;',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '存储平台;',
  `status` int DEFAULT NULL COMMENT '状态',
  `sort` int DEFAULT NULL COMMENT '排序',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oss_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OSS对象存储表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss`
--

LOCK TABLES `sys_oss` WRITE;
/*!40000 ALTER TABLE `sys_oss` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oss` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss_config`
--

DROP TABLE IF EXISTS `sys_oss_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss_config` (
  `id` bigint NOT NULL COMMENT '主建',
  `config_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置key',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'accessKey',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '秘钥',
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '桶名称',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '前缀',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '访问站点',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '自定义域名',
  `is_https` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '域',
  `access_policy` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '桶权限类型(0=private 1=public 2=custom)',
  `ext1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` int DEFAULT '1' COMMENT '是否默认（0=是,1=否）',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对象存储配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss_config`
--

LOCK TABLES `sys_oss_config` WRITE;
/*!40000 ALTER TABLE `sys_oss_config` DISABLE KEYS */;
INSERT INTO `sys_oss_config` VALUES (1810319364730155009,'local-plus','mms','mms','mms','localFile','','http://localhost:8080','N','1','public-read','/Users/shanpengnian/mms/',1,0,'1','000000','1','2024-07-08 22:25:45','1','2025-02-06 20:11:57','');
/*!40000 ALTER TABLE `sys_oss_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位名称',
  `sort` int DEFAULT NULL COMMENT '显示顺序',
  `status` int DEFAULT NULL COMMENT '状态;0正常 1停用',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统岗位;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES ('1','manager','总经理',1,0,NULL,'000000','1','1','2024-07-06 12:29:51','1','2024-07-06 12:29:51'),('2','employee','员工',2,0,NULL,'000000','1','1','2024-07-06 12:29:51','1','2024-07-06 12:29:51');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色权限字符串',
  `sort` bigint DEFAULT NULL COMMENT '显示顺序',
  `data_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据分类范围;1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限',
  `data_scope_dept_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据部门范围;指定部门数组',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色状态;0正常 1停用',
  `level` int NOT NULL DEFAULT '0' COMMENT '级别',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES ('1','超级管理员','super_admin',1,NULL,NULL,'0',1,'无所不能','000000','1',NULL,'2024-01-10 08:59:22','1','2024-12-16 23:21:24'),('1744898370860208129','管理员','admin',2,NULL,NULL,'0',2,'','000000','1','1','2024-01-10 09:46:05','1','2025-04-15 20:43:10');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_function`
--

DROP TABLE IF EXISTS `sys_role_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_function` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键编码',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色ID',
  `function_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '功能ID',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `status` int DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色功能;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_function`
--

LOCK TABLES `sys_role_function` WRITE;
/*!40000 ALTER TABLE `sys_role_function` DISABLE KEYS */;
INSERT INTO `sys_role_function` VALUES ('1868677812449722369','1','0',NULL,0,0,'000000','1','1','2024-12-16 23:21:23','1','2024-12-16 23:21:23'),('1868677812479082497','1','1',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812500054017','1','2',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812516831234','1','1855872016117329921',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812529414146','1','1854788379443027970',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812546191361','1','1793580098460786690',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812562968578','1','3',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812579745793','1','5',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812592328706','1','11',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812613300226','1','9',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812625883137','1','10',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812642660354','1','12',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812655243266','1','13',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812672020482','1','1856203155570196481',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812688797698','1','1856206343090241538',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812701380609','1','1856208397166739457',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812713963522','1','8',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812726546433','1','1809852682548662273',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812743323650','1','1809852753004580866',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812760100866','1','1809852844230692866',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812772683778','1','1809852932239773698',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812785266689','1','1809853053874589698',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812802043905','1','4',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812810432514','1','1809850786723254273',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812827209730','1','1809850967275458562',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812839792641','1','1809851073483624450',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812852375554','1','1809851347346509825',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812864958466','1','1809851230254125057',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812877541377','1','1856208397166739485',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812890124290','1','1856208397166739486',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812906901506','1','1856208397166739487',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812919484417','1','1856208397166739488',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812932067329','1','1856208397166739489',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812944650241','1','1856208397166739490',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812961427457','1','1856208397166739491',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812974010370','1','1856208397166739492',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812986593281','1','1856208397166739493',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812999176193','1','7',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813011759105','1','14',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813024342017','1','15',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813036924929','1','1809854968423260162',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813049507841','1','1809855292018008066',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813066285057','1','1809855369251921921',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813078867970','1','26',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813091450881','1','1809954418709798913',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813104033793','1','1809954506823737346',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813125005314','1','1809954598532194306',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813137588226','1','1809954692224557058',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813150171138','1','1809954777297625090',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813166948353','1','1856208397166739476',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813179531265','1','1856208397166739477',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813192114178','1','1856208397166739478',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813204697090','1','1856208397166739479',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813221474305','1','1856208397166739480',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813234057217','1','1856208397166739481',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813246640130','1','1856208397166739482',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813259223042','1','1856208397166739483',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813276000257','1','1856208397166739484',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813292777473','1','1856208397166739494',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813305360386','1','1856208397166739495',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813317943298','1','1856208397166739496',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813334720513','1','1856208397166739497',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813364080641','1','1856208397166739498',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813376663554','1','1856208397166739499',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813389246465','1','1856208397166739500',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813401829377','1','1856208397166739501',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813414412290','1','1856208397166739502',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813426995202','1','18',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813439578113','1','23',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813452161026','1','25',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813464743938','1','24',NULL,1,0,'000000','1','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1912124536250335234','1744898370860208129','1',NULL,0,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536271306754','1744898370860208129','0',NULL,0,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536283889665','1744898370860208129','2',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536300666881','1744898370860208129','1855872016117329921',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536321638402','1744898370860208129','1854788379443027970',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536342609921','1744898370860208129','1793580098460786690',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536350998530','1744898370860208129','5',NULL,0,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536367775745','1744898370860208129','3',NULL,0,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536384552961','1744898370860208129','11',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536401330178','1744898370860208129','9',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536418107394','1744898370860208129','10',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536430690305','1744898370860208129','12',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536447467522','1744898370860208129','8',NULL,0,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536464244738','1744898370860208129','1809852682548662273',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536476827649','1744898370860208129','4',NULL,0,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536493604866','1744898370860208129','1809850786723254273',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536506187778','1744898370860208129','1809850967275458562',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536522964993','1744898370860208129','1809851073483624450',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536535547905','1744898370860208129','7',NULL,0,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536548130818','1744898370860208129','14',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536560713729','1744898370860208129','15',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536577490945','1744898370860208129','1809854968423260162',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536590073858','1744898370860208129','1809855292018008066',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536606851074','1744898370860208129','18',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536619433986','1744898370860208129','23',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536636211201','1744898370860208129','25',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10'),('1912124536648794113','1744898370860208129','24',NULL,1,0,'000000','1','1','2025-04-15 20:43:10','1','2025-04-15 20:43:10');
/*!40000 ALTER TABLE `sys_role_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_sign`
--

DROP TABLE IF EXISTS `sys_sign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_sign` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `app_id` varchar(255) DEFAULT NULL COMMENT '客户端标识id',
  `secret_key` varchar(500) DEFAULT NULL COMMENT '秘钥',
  `public_key` text COMMENT '公钥',
  `private_key` text COMMENT '私钥',
  `time_out` datetime DEFAULT NULL COMMENT '过期时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统加签;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_sign`
--

LOCK TABLES `sys_sign` WRITE;
/*!40000 ALTER TABLE `sys_sign` DISABLE KEYS */;
INSERT INTO `sys_sign` VALUES ('1911799740660654081','1','8Th0VjuO1G3mNcb7','A505F34E648BE83CAAD8CC4FAF6C4D31',NULL,NULL,'2033-07-01 23:12:32',0,0,'1','000000','1','2025-04-14 23:12:32','1','2025-04-14 23:12:32',NULL),('1912670597993906178','1','8Th0VjuO1G3mNcb7','BDCF00D3DF44FD1CE118D351AC26DACB',NULL,NULL,'2033-07-04 08:53:01',0,0,'1','000000',NULL,NULL,NULL,NULL,NULL),('1912673740215439361','1','8Th0VjuO1G3mNcb7','393294C2279A73D01881D2A2CC631CA2',NULL,NULL,'2033-07-04 09:05:30',0,0,'1','000000',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_sign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant` (
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户名',
  `contact_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人的用户编号',
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
  `contact_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系手机',
  `status` int DEFAULT NULL COMMENT '租户状态;0正常 1停用',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定域名',
  `package_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户套餐编号',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `account_count` int DEFAULT NULL COMMENT '账号数量',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统租户;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant`
--

LOCK TABLES `sys_tenant` WRITE;
/*!40000 ALTER TABLE `sys_tenant` DISABLE KEYS */;
INSERT INTO `sys_tenant` VALUES ('000001','管理员租户','1','西决','13388886557',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_tenant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tenant_package`
--

DROP TABLE IF EXISTS `sys_tenant_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant_package` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '套餐名',
  `status` int DEFAULT NULL COMMENT '租户状态;0正常 1停用',
  `function_ids` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联的菜单编号',
  `remark` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户套餐;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant_package`
--

LOCK TABLES `sys_tenant_package` WRITE;
/*!40000 ALTER TABLE `sys_tenant_package` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_tenant_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `dept_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门ID',
  `post_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位编号数组',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `user_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户类型',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `wx_openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信id',
  `wx_un_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开发者id',
  `sex` int DEFAULT NULL COMMENT '性别;0：保密 1：男2：女',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `aes_key` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对称性秘钥',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `password_strength` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '弱' /*!80023 INVISIBLE */ COMMENT '密码强度',
  `status` int DEFAULT NULL COMMENT '状态;0正常 1停用',
  `del_flag` int DEFAULT NULL COMMENT '删除标志;0代表存在 2代表删除',
  `login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后登录ip',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `public_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公钥;',
  `private_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '私钥;',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`user_id`, `dept_id`, `post_ids`, `user_name`, `nick_name`, `user_type`, `email`, `phone_number`, `wx_openid`, `wx_un_open_id`, `sex`, `avatar`, `aes_key`, `password`, `password_strength`, `status`, `del_flag`, `login_ip`, `login_date`, `public_key`, `private_key`, `remark`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`, `sort`) VALUES ('1','1','1,2','admin','MMS','1','','','',NULL,1,'https://sxpcwlkj.oss-cn-beijing.aliyuncs.com/test/boy.jpg','8AAB8216D19ADED25549DF7F156E7642','b00017516f30a24393de6b10da8512ef','一般',0,NULL,'0:0:0:0:0:0:0:1','2025-04-18 17:00:26',NULL,NULL,'无所不能...','000000','1','1','2023-03-24 10:32:10',NULL,'2025-04-18 17:00:26',1);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键编码',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色ID',
  `status` int DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES ('1','1','1',1,0,NULL,'000000','1','1','2024-01-02 16:29:08','1','2024-07-06 12:31:47');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-25 13:22:59
