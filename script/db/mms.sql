-- MySQL dump 10.13  Distrib 8.0.31, for Linux (x86_64)
--
-- Host: localhost    Database: mms
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
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
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `tenant_id`, `revision`, `remark`, `status`, `sort`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES ('1887472152748412930','多租户状态','sys_tenant_state','2',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.41'),('1887472152811327489','邮箱状态','sys_email_state','2',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.425'),('1887472152811327490','微信配置状态','sys_wx_state','1',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-05-28 21:21:08.664'),('1887472152828104705','短信服务默认厂商','sys_sms_supplier_aliyun','1',1,'000000','6',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-04-20 19:23:10.432'),('1887472152903602178','系统名称','sys_base_title','模块化管理系统',1,'000000','10',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-08-31 00:10:29.666'),('1887472152937156610','短信服务厂商Key','sys_sms_accessKey_aliyun','您的信息key/Secret/Appid',1,'000000','6',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-04-20 19:23:10.475'),('1887472152979099649','系统描述','sys_base_description','模块化管理系统 Modular management system  简称：MMS 一款灵活、高效、低代码模块化管理系统',1,'000000','10',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-08-31 00:10:29.948'),('1887472152983293954','公众号Appid','sys_wx_mp_appid','您的信息key/Secret/Appid',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-05-28 21:21:08.698'),('1887472152983293955','邮件服务器地址','sys_email_host','',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.465'),('1887472153000071170','短信服务厂商密钥','sys_sms_accessKeySecret_aliyun','您的信息key/Secret/Appid',1,'000000','6',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-04-20 19:23:10.501'),('1887472153021042689','多租户排除的的表','sys_tenant_exclusion_table','',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:23','1','2025-02-06 20:03:23.475'),('1887472153155260417','系统LOGO','sys_base_logo','https://www.mmsadmin.cn/logo.png',1,'000000','10',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-08-31 00:10:30.217'),('1887472153180426241','端口','sys_email_port','465',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.513'),('1887472153188814849','微信公众号AppSecret','sys_wx_mp_appsecret','您的信息key/Secret/Appid',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.725'),('1887472153213980673','短信服务厂商模版ID','sys_sms_template_id_aliyun','SMS_=========',1,'000000','6',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-04-20 19:23:10.524'),('1887472153213980674','登录图形验证码','sys_base_captcha_state','0',1,'000000','10',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-08-31 00:10:30.493'),('1887472153218174978','邮件账号','sys_email_from','',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.522'),('1887472153335615490','邮箱密码','sys_email_pass','',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.55'),('1887472153335615491','小程序AppId','sys_wx_miniapp_appid','您的信息key/Secret/Appid',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.748'),('1887472153339809794','短信服务厂商签名','sys_sms_signature_aliyun','品创网络',1,'000000','6',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-04-20 19:23:10.544'),('1887472153348198401','系统登录方式','sys_base_login_type','1',1,'000000','10',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-08-31 00:10:30.772'),('1887472153595662338','登录页背景','sys_base_login_bg','',1,'000000','10',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-08-31 00:10:31.052'),('1887472153595662339','SSL安全连接','sys_email_ssl','1',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.612'),('1887472153599856641','小程序AppSecret','sys_wx_miniapp_appsecret','您的信息key/Secret/Appid',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.77'),('1887472153708908546','微信商户ID','sys_wx_pay_id','您的信息key/Secret/Appid',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.798'),('1887472153817960449','微信商户Appid','sys_wx_pay_appid','',1,'000000','1',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-02-06 20:03:23.665'),('1887472154002509825','微信商户秘钥','sys_wx_pay_appsecret','您的信息key/Secret/Appid',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.84'),('1887472154057035778','微信商户秘钥类型','sys_wx_pay_type','1',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.859'),('1887472154111561730','微信支付回调','sys_wx_pay_notifyUrl','',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.881'),('1887472154182864897','微信模式','sys_wx_model','1',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.901'),('1887472154237390850','AppToken','sys_wx_token','您的信息key/Secret/Appid',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.919'),('1887472154312888322','AppAesKey','sys_wx_aesKey','',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.938'),('1887472154379997186','扫码关注回复内容','sys_wx_attention_msg','欢迎来到\"模块化管理系统\"',1,'000000','7',NULL,0,0,'1','2025-02-06 20:03:24','1','2025-05-28 21:21:08.956'),('1912124966099386370','微信商户证书','sys_wx_pay_path','',1,'000000','7',NULL,0,0,'1','2025-04-15 20:44:52','1','2025-05-28 21:21:08.819'),('1912124966208438274','退款回调','sys_wx_refund_path','',1,'000000','7',NULL,0,0,'1','2025-04-15 20:44:52','1','2025-05-28 21:21:08.975'),('1913915840668090369','短信服务默认厂商','sys_sms_supplier_test','',1,'000000','1',NULL,0,0,'1','2025-04-20 19:21:10','1','2025-04-20 19:21:09.911'),('1913915840772947969','短信服务厂商Key','sys_sms_accessKey_test','',1,'000000','1',NULL,0,0,'1','2025-04-20 19:21:10','1','2025-04-20 19:21:09.935'),('1913915840844251138','短信服务厂商密钥','sys_sms_accessKeySecret_test','',1,'000000','1',NULL,0,0,'1','2025-04-20 19:21:10','1','2025-04-20 19:21:09.952'),('1913915840907165698','短信服务厂商模版ID','sys_sms_template_id_test','',1,'000000','1',NULL,0,0,'1','2025-04-20 19:21:10','1','2025-04-20 19:21:09.967'),('1913915840970080258','短信服务厂商签名','sys_sms_signature_test','',1,'000000','1',NULL,0,0,'1','2025-04-20 19:21:10','1','2025-04-20 19:21:09.982'),('1959912825647456258','Vue','ASDFDSSS','1321321313213212313123213',2,'000000','5','https://demo.mmsadmin.cn/#/system/config',0,0,'1','2025-08-25 17:36:46','1','2025-08-27 17:27:06.913'),('1960002183020863489','mmsAdmin','9faf9094-f1f8-4223-91ae-23b37cf72b75','https://sxpcwlkj-test.oss-accelerate.aliyuncs.com/mmsMall/upload/68ac81dce354d6c91e415ca8.png',3,'000000','1','https://mmsadmin.cn/',1,0,'1','2025-08-25 23:31:50','1','2025-08-25 23:31:50.169'),('1960003570068807681','mmsDoc','e4883a03-e11b-41a5-b2a1-61101f2482f6','https://sxpcwlkj-test.oss-accelerate.aliyuncs.com/mmsMall/upload/68ac8327e354d6c91e415ca9.png',3,'000000','1','https://mmsadmin.cn/',1,0,'1','2025-08-25 23:37:21','1','2025-08-25 23:37:20.867'),('1960540756145004546','网站标题','website_title','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.227'),('1960540756191141889','网站副标题','website_subtitle','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.292'),('1960540756228890625','网站域名','website_domain','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.352'),('1960540756270833666','网站关键词','website_keywords','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.412'),('1960540756312776705','网站描述','website_description','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.472'),('1960540756354719746','网站LOGO','website_logo','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.529'),('1960540756392468481','网站版权','website_copyright','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.592'),('1960540756434411522','备案号','website_record','',1,'000000','9',NULL,0,0,'1','2025-08-27 11:11:56','1','2025-09-08 12:54:53.652'),('1960743358040301570','品创网络','87ef324d-92a7-46fc-b303-6381a7a8f739','https://sxpcwlkj-test.oss-accelerate.aliyuncs.com/mmsMall/upload/68af3429e354d6c9489a4e6a.png',3,'000000','1','https://www.sxpcwlkj.com/',1,0,'1','2025-08-28 00:37:00','1','2025-08-28 00:37:00.064');
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
INSERT INTO `sys_dept` VALUES ('1','0','集团总部','admin','13388886557','sxpcwlkj@163.com',1,'陕西省西安市雁塔区',1,'超级管理员','000000','1','1','2024-01-08 13:11:05','1','2024-12-09 15:56:22'),('2','1','陕西分公司','xijeu','13388886557','sxpcwlkj@163.com',1,'陕西省西安市雁塔区',1,'管理员','000000','1','1','2024-01-08 13:11:08','1','2024-12-08 15:09:09'),('3','2','开发部','xijeu','13388886557','sxpcwlkj@163.com',1,'陕西省西安市雁塔区',8,'管理员','000000','3','1','2024-01-08 13:11:08','1','2025-04-25 22:04:25'),('4','2','运营部','xijeu','13388886557','sxpcwlkj@163.com',1,'陕西省西安市雁塔区',4,'管理员','000000','1','1','2024-01-08 13:11:08','1','2024-03-17 13:06:25');
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
INSERT INTO `sys_dict` VALUES ('1745461307624050210','系统状态','SYS_STATE','1',0,1,'000000','3','1','2024-01-11 23:27:17','1','2025-08-31 00:09:11','系统公告状态'),('1745469307624050689','系统是否','SYS_IS','1',0,1,'000000','1','1','2024-01-11 23:34:47','1','2024-07-07 22:52:59','系统是否'),('1789908194852012034','性别','SYS_SEX','1',0,1,'000000','1','1','2024-05-13 14:39:03','1','2024-11-13 21:08:40','性别'),('1793520884619108353','配置类型','CONFIG_TYPE','1',0,1,'000000','1','1','2024-05-23 13:54:36','1','2024-05-23 14:29:04','配置类型'),('1793548719358361602','消息类型','NITICE_TYPE','1',0,1,'000000','1','1','2024-05-23 15:45:12','1','2024-05-23 15:46:14','消息类型'),('1800779228136251394','会员状态','MEMBER_STATE','1',0,1,'000000','1','1','2024-06-12 14:36:40','1','2024-06-12 14:51:23','会员状态'),('1809847987692126210','是否HTTPS','IS_HTTPS','1',0,1,'000000','1','1','2024-07-07 15:12:40','1','2024-07-07 16:52:49','是否HTTPS'),('1809872908245917698','公告类型','NOTICE_TYPE','1',0,1,'000000','1','1','2024-07-07 16:51:42','1','2024-07-07 16:51:42','系统公告类型'),('1809966410250162178','存储平台','OSS_TYPE','1',0,1,'000000','1','1','2024-07-07 23:03:14','1','2024-07-09 02:59:42','存储类型'),('1857269822656126978','账号类型','USER_TYPE','1',0,1,'000000','1','1','2024-11-15 11:50:07','1','2024-11-15 11:50:07','账号类型'),('1913854715150655490','店铺类型','STORE_TYPE','1',0,1,'000001','2','1','2025-04-20 15:18:16','1','2025-04-20 15:28:10','店铺类型;1:企业  2:个人'),('1913855178013073410','营业状态','BUSINESS_STATE','1',0,1,'000001','1','1','2025-04-20 15:20:07','1','2025-04-20 15:20:07','营业状态;0.禁用 1.营业  2.休业 '),('1960504029850296321','会员类型','DOCTYPE','1',0,1,'000000','1','1','2025-08-27 08:46:00','1','2025-08-27 08:46:00',''),('2011709982138363906','日志类型','operType','1',0,1,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10','操作类型(0其它 1新增 2修改 3删除 4查询 5导出 6导入 7登录 8退出 9授权 10清空)');
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
INSERT INTO `sys_dict_data` VALUES ('1793529560545247234','系统设置','CONFIG_TYPE','1','0',1,1,'warning',NULL,'000000','1','1','2024-05-23 14:29:04','1','2024-05-23 14:29:04',NULL),('1793529560545247235','用户自定义','CONFIG_TYPE','2','0',1,1,'success',NULL,'000000','1','1','2024-05-23 14:29:04','1','2024-05-23 14:29:04',NULL),('1793548979082248193','公告','NITICE_TYPE','1','0',1,1,'primary',NULL,'000000','1','1','2024-05-23 15:46:14','1','2024-05-23 15:46:14',NULL),('1793548979082248194','消息','NITICE_TYPE','2','0',2,1,'info',NULL,'000000','1','1','2024-05-23 15:46:14','1','2024-05-23 15:46:14',NULL),('1800782934097444865','未激活','MEMBER_STATE','0','0',0,1,'warning',NULL,'000000','1','1','2024-06-12 14:51:23','1','2024-06-12 14:51:23',NULL),('1800782934118416386','正常','MEMBER_STATE','1','0',0,1,'success',NULL,'000000','1','1','2024-06-12 14:51:23','1','2024-06-12 14:51:23',NULL),('1800782934118416387','禁用','MEMBER_STATE','0','0',0,1,'danger',NULL,'000000','1','1','2024-06-12 14:51:23','1','2024-06-12 14:51:23',NULL),('1809872908283666434','公告','NOTICE_TYPE','1','0',1,1,'primary',NULL,'000000','1','1','2024-07-07 16:51:42','1','2024-07-07 16:51:42',NULL),('1809872908296249345','通知','NOTICE_TYPE','2','0',2,1,'success',NULL,'000000','1','1','2024-07-07 16:51:42','1','2024-07-07 16:51:42',NULL),('1809872999727882242','','isHttps','','0',0,1,'',NULL,'000000','1','1','2024-07-07 16:52:04','1','2024-07-07 16:52:04',NULL),('1809873188056326145','是','IS_HTTPS','Y','0',1,1,'primary',NULL,'000000','1','1','2024-07-07 16:52:49','1','2024-07-07 16:52:49',NULL),('1809873188077297665','否','IS_HTTPS','N','0',2,1,'warning',NULL,'000000','1','1','2024-07-07 16:52:49','1','2024-07-07 16:52:49',NULL),('1809963829939507202','是','SYS_IS','1','0',1,1,'primary',NULL,'000000','1','1','2024-07-07 22:52:59','1','2024-07-07 22:52:59',NULL),('1809963829964673025','否','SYS_IS','2','0',2,1,'warning',NULL,'000000','1','1','2024-07-07 22:52:59','1','2024-07-07 22:52:59',NULL),('1810388304412119041','本地存储','OSS_TYPE','local-plus-1','0',1,1,'primary',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304433090562','阿里云','OSS_TYPE','aliyun-oss-1','0',2,1,'success',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304445673473','腾讯云','OSS_TYPE','tencent-cos-1','0',3,1,'info',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304454062082','华为云','OSS_TYPE','huawei-obs-1','0',4,1,'warning',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304466644993','七牛云','OSS_TYPE','qiniu-kodo-1','0',5,1,'danger',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304479227905','百度云','OSS_TYPE','baidu-bos-1','0',6,1,'info',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304500199426','MinIo','OSS_TYPE','minio-1','0',7,1,'success',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1810388304508588033','AmazonS3','OSS_TYPE','amazon-s3-1','0',8,1,'primary',NULL,'000000','1','1','2024-07-09 02:59:42','1','2024-07-09 02:59:42',NULL),('1856685610085441538','男','SYS_SEX','1','1',1,1,'',NULL,'000000','1','1','2024-11-13 21:08:40','1','2024-11-13 21:08:40',NULL),('1856685610102218754','女','SYS_SEX','2','0',2,1,'',NULL,'000000','1','1','2024-11-13 21:08:40','1','2024-11-13 21:08:40',NULL),('1856685610110607362','保密','SYS_SEX','0','0',3,1,'',NULL,'000000','1','1','2024-11-13 21:08:40','1','2024-11-13 21:08:40',NULL),('1857269822685487106','系统用户','USER_TYPE','1','0',1,1,'success',NULL,'000000','1','1','2024-11-15 11:50:07','1','2024-11-15 11:50:07',NULL),('1857269822693875713','商家用户','USER_TYPE','2','0',2,1,'warning',NULL,'000000','1','1','2024-11-15 11:50:07','1','2024-11-15 11:50:07',NULL),('1913855178071793665','禁用','BUSINESS_STATE','0','0',0,1,'danger',NULL,'000001','1','1','2025-04-20 15:20:07','1','2025-04-20 15:20:07',NULL),('1913855178105348098','营业','BUSINESS_STATE','1','0',0,1,'success',NULL,'000001','1','1','2025-04-20 15:20:07','1','2025-04-20 15:20:07',NULL),('1913855178143096834','休业','BUSINESS_STATE','2','0',0,1,'warning',NULL,'000001','1','1','2025-04-20 15:20:07','1','2025-04-20 15:20:07',NULL),('1913857206592434178','企业','STORE_TYPE','1','0',0,1,'success',NULL,'000001','1','1','2025-04-20 15:28:10','1','2025-04-20 15:28:10',NULL),('1913857206655348737','个人','STORE_TYPE','2','0',0,1,'warning',NULL,'000001','1','1','2025-04-20 15:28:10','1','2025-04-20 15:28:10',NULL),('1960504029879656449','VIP会员','DOCTYPE','vip','0',1,1,'danger',NULL,'000000','1','1','2025-08-27 08:46:00','1','2025-08-27 08:46:00',NULL),('1960504029909016578','注册用户','DOCTYPE','usr','0',2,1,'info',NULL,'000000','1','1','2025-08-27 08:46:00','1','2025-08-27 08:46:00',NULL),('1961823519742509058','正常','SYS_STATE','1','0',0,1,'success',NULL,'000000','1','1','2025-08-31 00:09:11','1','2025-08-31 00:09:11',NULL),('1961823520132579330','禁用','SYS_STATE','0','0',1,1,'danger',NULL,'000000','1','1','2025-08-31 00:09:11','1','2025-08-31 00:09:11',NULL),('2011709982243221505','新增','operType','1','0',0,1,'success',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982331301890','修改','operType','2','0',0,1,'primary',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982410993665','删除','operType','3','0',0,1,'success',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982499074049','查询','operType','4','0',0,1,'warning',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982574571522','导出','operType','5','0',0,1,'danger',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982666846210','导入','operType','6','0',0,1,'primary',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982759120897','登录','operType','7','0',0,1,'success',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982843006978','退出','operType','8','0',0,1,'info',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709982926893057','授权','operType','9','0',0,1,'warning',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL),('2011709983014973442','清空','operType','10','0',0,1,'danger',NULL,'000000','1','1','2026-01-15 16:00:10','1','2026-01-15 16:00:10',NULL);
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
                                `is_fast` int DEFAULT '0' COMMENT '是否快捷菜单',
                                `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户号',
                                `revision` int DEFAULT '-1' COMMENT '乐观锁',
                                `created_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
                                `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `updated_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
                                `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1960271825199095884 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统功能';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_function`
--

LOCK TABLES `sys_function` WRITE;
/*!40000 ALTER TABLE `sys_function` DISABLE KEYS */;
INSERT INTO `sys_function` VALUES (1,'0','/','系统菜单','','/home',NULL,'home','system:model:menu',1,1,'ele-SetUp',1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-11 14:41:00'),(2,'1','/index','控制台','system/home/index','','message.router.home',NULL,'super_admin,admin',1,1,'iconfont icon-laptop',1,-1,-1,-1,'',1,1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-11 15:43:26'),(3,'1','/system','系统管理','','/system/menu','message.router.system',NULL,'super_admin,admin',1,98,'iconfont icon-cog',1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:36:40'),(4,'3','/system/menu','菜单列表','system/menu/index',NULL,'message.router.systemMenu',NULL,'super_admin,admin',1,3,'iconfont icon-Directory-tree',1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:38:51'),(5,'3','/system/user','用户列表','/system/user/index',NULL,'message.router.systemUser',NULL,'super_admin,admin',1,1,'iconfont icon-user-group',1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:25:37'),(7,'3','/system/dict','字典列表','system/dict/index',NULL,'message.router.systemDic',NULL,'super_admin,admin',1,5,'iconfont icon-databaseplus-fill',1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-10 11:39:54'),(8,'3','/system/role','角色列表','system/role/index',NULL,'message.router.systemRole',NULL,'super_admin,admin',1,2,'iconfont icon-application',1,-1,-1,-1,'',1,-1,1,NULL,'000000',2,NULL,NULL,'1','2025-08-25 22:40:36'),(9,'5','','用户列表-列表','',NULL,'用户列表',NULL,'system:user:list',2,2,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:28'),(10,'5','','用户列表-编辑','',NULL,'用户编辑',NULL,'system:user:edit',2,3,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:50'),(11,'5','','用户列表-查询','',NULL,'用户查询',NULL,'system:user:query',2,1,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:22:17'),(12,'5','','用户列表-添加','',NULL,'用户新增',NULL,'system:user:insert',2,4,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:23:06'),(13,'5','','用户列表-删除','',NULL,'用户删除',NULL,'system:user:delete',2,5,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,NULL,'1','2024-11-12 13:23:15'),(14,'7','','列表','',NULL,'','','system:dict:list',2,0,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-01-14 19:37:47','1','2024-01-14 19:45:32'),(15,'7','','新增','',NULL,'','','system:dict:insert',2,1,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-01-14 19:48:13','1','2024-07-07 16:50:27'),(18,'3','/system/generate','生成代码','system/generate/index',NULL,'message.gen.generator','','',1,10,'iconfont icon-resource',1,-1,-1,-1,'',-1,-1,1,NULL,'000000',2,'1','2024-01-21 12:42:40','1','2025-08-25 22:48:13'),(25,'3','/tools/powerjob','PowerJob','tools/powerjob',NULL,'message.tools.powerjob','','',1,9,'iconfont icon-calendar-alt',1,-1,1,1,'/mms-job/',-1,-1,0,NULL,'000000',4,'1','2024-01-22 01:38:42','1','2025-08-25 01:01:19'),(26,'3','/system/oss','对象存储','system/oss/index',NULL,'message.router.oss','','',1,6,'iconfont icon-cloudupload',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-01-22 23:11:02','1','2024-11-10 11:40:13'),(1793580098460786690,'2','/personal','个人中心','system/personal/index',NULL,'个人中心','','',1,2,'iconfont icon-account',1,1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-05-23 17:49:53','1','2024-11-11 15:42:53'),(1809850786723254273,'4','','列表','',NULL,'','','system:function:list',2,1,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:23:48','1','2024-07-07 15:26:13'),(1809850967275458562,'4','','查询','',NULL,'','','system:function:query',2,2,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:24:31','1','2024-07-07 15:24:31'),(1809851073483624450,'4','','编辑','',NULL,'','','system:function:edit',2,3,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:24:56','1','2024-07-07 15:24:56'),(1809851230254125057,'4','','新增','',NULL,'','','system:function:insert',2,5,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:25:33','1','2024-07-07 15:26:23'),(1809851347346509825,'4','','删除','',NULL,'','','system:function:delete',2,4,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:26:01','1','2024-07-07 15:26:01'),(1809852682548662273,'8','','列表','',NULL,'','','system:role:list',2,1,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:31:20','1','2024-07-07 15:31:20'),(1809852753004580866,'8','','查询','',NULL,'','','system:role:query',2,2,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:31:36','1','2024-07-07 15:31:36'),(1809852844230692866,'8','','编辑','',NULL,'','','system:role:edit',2,3,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:31:58','1','2024-07-07 15:31:58'),(1809852932239773698,'8','','新增','',NULL,'','','system:role:insert',2,4,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:32:19','1','2024-07-07 15:32:19'),(1809853053874589698,'8','','删除','',NULL,'','','system:role:delete',2,5,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:32:48','1','2024-07-07 15:32:48'),(1809854968423260162,'7','','查询','',NULL,'','','system:dict:query',2,3,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:40:25','1','2024-07-07 15:40:43'),(1809855292018008066,'7','','编辑','',NULL,'','','system:dict:edit',2,4,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:41:42','1','2024-07-07 15:41:42'),(1809855369251921921,'7','','删除','',NULL,'','','system:dict:delete',2,5,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 15:42:00','1','2024-07-07 15:42:00'),(1809954418709798913,'26','','列表','',NULL,'','','system:oss:list',2,1,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 22:15:35','1','2024-07-07 22:15:35'),(1809954506823737346,'26','','查询','',NULL,'','','system:oss:query',2,2,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 22:15:56','1','2024-07-07 22:15:56'),(1809954598532194306,'26','','编辑','',NULL,'','','system:oss:edit',2,3,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 22:16:18','1','2024-07-07 22:16:18'),(1809954692224557058,'26','','新增','',NULL,'','','system:oss:insert',2,4,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 22:16:41','1','2024-07-07 22:16:41'),(1809954777297625090,'26','','删除','',NULL,'','','system:oss:delete',2,5,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-07-07 22:17:01','1','2024-07-07 22:17:01'),(1854788379443027970,'2','','左侧菜单树','',NULL,'','','system:model:menu',2,2,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-11-08 15:29:45','1','2024-11-11 15:32:22'),(1855872016117329921,'2','','控制台详情','',NULL,'','','system:model:common',2,1,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-11-11 15:15:44','1','2024-11-11 15:32:02'),(1856203155570196481,'5','','用户列表-导入','',NULL,'','','system:user:import',2,6,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-11-12 13:11:34','1','2024-11-12 13:11:34'),(1856206343090241538,'5','','用户列表-导出','',NULL,'','','system:user:export',2,7,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-11-12 13:24:13','1','2024-11-12 13:32:45'),(1856208397166739457,'5','','用户列表-打印','',NULL,'','','system:user:print',2,8,'',1,-1,-1,-1,'',-1,-1,0,NULL,'000000',1,'1','2024-11-12 13:32:23','1','2024-11-12 13:32:37'),(1856208397166739476,'3','/system/config','系统配置','system/config/index',NULL,'系统配置',NULL,NULL,1,7,'iconfont icon-application',1,-1,-1,-1,'',1,-1,1,NULL,'000000',2,NULL,'2024-12-09 15:57:57','1','2025-08-25 22:40:52'),(1856208397166739477,'1856208397166739476',NULL,'配置表-列表',NULL,NULL,NULL,NULL,'system:config:list',2,2,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739478,'1856208397166739476',NULL,'配置表-新增',NULL,NULL,NULL,NULL,'system:config:insert',2,3,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739479,'1856208397166739476',NULL,'配置表-删除',NULL,NULL,NULL,NULL,'system:config:delete',2,4,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739480,'1856208397166739476',NULL,'配置表-编辑',NULL,NULL,NULL,NULL,'system:config:edit',2,5,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739481,'1856208397166739476',NULL,'配置表-查询',NULL,NULL,NULL,NULL,'system:config:query',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739482,'1856208397166739476',NULL,'配置表-导入',NULL,NULL,NULL,NULL,'system:config:import',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739483,'1856208397166739476',NULL,'配置表-导出',NULL,NULL,NULL,NULL,'system:config:export',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739484,'1856208397166739476',NULL,'配置表-打印',NULL,NULL,NULL,NULL,'system:config:print',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 15:57:57',NULL,'2024-12-09 15:57:57'),(1856208397166739485,'3','/system/dept','系统部门','system/dept/index',NULL,'系统部门',NULL,NULL,1,4,'iconfont icon-Directory-tree',1,-1,-1,-1,'',1,-1,1,NULL,'000000',2,NULL,'2024-12-09 16:23:07','1','2025-08-25 22:40:45'),(1856208397166739486,'1856208397166739485',NULL,'系统部门-列表',NULL,NULL,NULL,NULL,'system:dept:list',2,2,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739487,'1856208397166739485',NULL,'系统部门-新增',NULL,NULL,NULL,NULL,'system:dept:insert',2,3,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739488,'1856208397166739485',NULL,'系统部门-删除',NULL,NULL,NULL,NULL,'system:dept:delete',2,4,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739489,'1856208397166739485',NULL,'系统部门-编辑',NULL,NULL,NULL,NULL,'system:dept:edit',2,5,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739490,'1856208397166739485',NULL,'系统部门-查询',NULL,NULL,NULL,NULL,'system:dept:query',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739491,'1856208397166739485',NULL,'系统部门-导入',NULL,NULL,NULL,NULL,'system:dept:import',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739492,'1856208397166739485',NULL,'系统部门-导出',NULL,NULL,NULL,NULL,'system:dept:export',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739493,'1856208397166739485',NULL,'系统部门-打印',NULL,NULL,NULL,NULL,'system:dept:print',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:07',NULL,'2024-12-09 16:23:07'),(1856208397166739494,'3','/system/notice','系统公告','system/notice/index',NULL,'系统公告',NULL,NULL,1,8,'iconfont icon-pic-center',1,-1,-1,-1,'',1,-1,1,NULL,'000000',3,NULL,'2024-12-09 16:23:23','1','2025-09-08 13:10:53'),(1856208397166739495,'1856208397166739494',NULL,'系统公告-列表',NULL,NULL,NULL,NULL,'system:notice:list',2,2,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739496,'1856208397166739494',NULL,'系统公告-新增',NULL,NULL,NULL,NULL,'system:notice:insert',2,3,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739497,'1856208397166739494',NULL,'系统公告-删除',NULL,NULL,NULL,NULL,'system:notice:delete',2,4,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:23',NULL,'2024-12-09 16:23:23'),(1856208397166739498,'1856208397166739494',NULL,'系统公告-编辑',NULL,NULL,NULL,NULL,'system:notice:edit',2,5,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739499,'1856208397166739494',NULL,'系统公告-查询',NULL,NULL,NULL,NULL,'system:notice:query',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739500,'1856208397166739494',NULL,'系统公告-导入',NULL,NULL,NULL,NULL,'system:notice:import',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739501,'1856208397166739494',NULL,'系统公告-导出',NULL,NULL,NULL,NULL,'system:notice:export',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1856208397166739502,'1856208397166739494',NULL,'系统公告-打印',NULL,NULL,NULL,NULL,'system:notice:print',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2024-12-09 16:23:24',NULL,'2024-12-09 16:23:24'),(1960271825199095839,'3','/system/sysLog','操作日志','system/sysLog/index',NULL,'操作日志记录表',NULL,NULL,1,1,'',1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095840,'1960271825199095839',NULL,'操作日志记录表-列表',NULL,NULL,NULL,NULL,'system:sysLog:list',2,2,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095841,'1960271825199095839',NULL,'操作日志记录表-新增',NULL,NULL,NULL,NULL,'system:sysLog:insert',2,3,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095842,'1960271825199095839',NULL,'操作日志记录表-删除',NULL,NULL,NULL,NULL,'system:sysLog:delete',2,4,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095843,'1960271825199095839',NULL,'操作日志记录表-编辑',NULL,NULL,NULL,NULL,'system:sysLog:edit',2,5,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095844,'1960271825199095839',NULL,'操作日志记录表-查询',NULL,NULL,NULL,NULL,'system:sysLog:query',2,6,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095845,'1960271825199095839',NULL,'操作日志记录表-导入',NULL,NULL,NULL,NULL,'system:sysLog:import',2,7,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095846,'1960271825199095839',NULL,'操作日志记录表-导出',NULL,NULL,NULL,NULL,'system:sysLog:export',2,8,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27'),(1960271825199095847,'1960271825199095839',NULL,'操作日志记录表-打印',NULL,NULL,NULL,NULL,'system:sysLog:print',2,9,NULL,1,-1,-1,-1,'',1,-1,0,NULL,'000000',1,NULL,'2026-01-15 17:37:27',NULL,'2026-01-15 17:37:27');
/*!40000 ALTER TABLE `sys_function` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=2011709057713762307 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_table`
--

LOCK TABLES `sys_gen_table` WRITE;
/*!40000 ALTER TABLE `sys_gen_table` DISABLE KEYS */;
INSERT INTO `sys_gen_table` (`id`, `table_name`, `class_name`, `table_comment`, `author`, `email`, `package_name`, `version`, `generator_type`, `backend_path`, `frontend_path`, `module_name`, `function_name`, `form_layout`, `datasource_id`, `baseclass_id`, `menu_id`, `parent_id`, `create_time`, `table_label`, `span`) VALUES (2011709057713762306,'sys_log','SysLog','操作日志记录表','mmsAdmin','942879858@qq.com','com.sxpcwlkj','1.0.0',0,'sxpcwlkj/admin','sxpcwlkj/front','system','sysLog',1,0,1,'3','0','2026-01-15 15:56:30','0',24);
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
) ENGINE=InnoDB AUTO_INCREMENT=2011709058770726914 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_gen_table_field`
--

LOCK TABLES `sys_gen_table_field` WRITE;
/*!40000 ALTER TABLE `sys_gen_table_field` DISABLE KEYS */;
INSERT INTO `sys_gen_table_field` VALUES (2011709057847980033,2011709057713762306,'oper_id','bigint','日志主键','operId','Long',NULL,0,'DEFAULT',1,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709057910894593,2011709057713762306,'tenant_id','bigint','租户ID','tenantId','Long',NULL,1,'DEFAULT',0,1,0,0,'text',NULL,NULL,0,0,0,'=','text'),(2011709057952837634,2011709057713762306,'module','varchar','模块名称','module','String',NULL,2,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709057986392065,2011709057713762306,'oper_type','int','操作类型','operType','Integer',NULL,3,'DEFAULT',0,0,1,1,'select','operType','@NotNull',1,0,0,'=','text'),(2011709058024140801,2011709057713762306,'description','varchar','操作描述','description','String',NULL,4,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058066083842,2011709057713762306,'request_method','varchar','请求方法','requestMethod','String',NULL,5,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058112221185,2011709057713762306,'method','varchar','操作方法(类名.方法名)','method','String',NULL,6,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058154164225,2011709057713762306,'oper_url','varchar','请求URL','operUrl','String',NULL,7,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058191912961,2011709057713762306,'user_id','bigint','操作人员ID','userId','Long',NULL,8,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058233856002,2011709057713762306,'user_name','varchar','操作人员账号','userName','String',NULL,9,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058275799041,2011709057713762306,'user_roles','varchar','操作人员角色','userRoles','String',NULL,10,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058309353473,2011709057713762306,'oper_ip','varchar','主机地址','operIp','String',NULL,11,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058347102209,2011709057713762306,'oper_location','varchar','操作地点','operLocation','String',NULL,12,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058393239553,2011709057713762306,'oper_param','text','请求参数','operParam','String',NULL,13,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058426793985,2011709057713762306,'before_data','text','操作前数据','beforeData','String',NULL,14,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058460348417,2011709057713762306,'json_result','text','返回结果','jsonResult','String',NULL,15,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058510680065,2011709057713762306,'status','int','操作状态','status','Integer',NULL,16,'DEFAULT',0,0,1,1,'radio','SYS_STATE','@NotNull',1,0,0,'=','select'),(2011709058552623105,2011709057713762306,'error_msg','text','错误消息','errorMsg','String',NULL,17,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058594566145,2011709057713762306,'oper_time','datetime','操作时间','operTime','Date','java.util.Date',18,'DEFAULT',0,0,1,1,'text',NULL,'@NotNull',1,0,0,'=','text'),(2011709058644897793,2011709057713762306,'cost_time','bigint','消耗时间(毫秒)','costTime','Long',NULL,19,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058682646530,2011709057713762306,'user_agent','varchar','用户代理','userAgent','String',NULL,20,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058732978177,2011709057713762306,'browser','varchar','浏览器类型','browser','String',NULL,21,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text'),(2011709058770726913,2011709057713762306,'os','varchar','操作系统','os','String',NULL,22,'DEFAULT',0,0,1,1,'text',NULL,'@NotBlank',1,0,0,'=','text');
/*!40000 ALTER TABLE `sys_gen_table_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log` (
                           `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
                           `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
                           `module` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '模块名称',
                           `oper_type` int DEFAULT '0' COMMENT '操作类型(0其它 1新增 2修改 3删除 4查询 5导出 6导入 7登录 8退出 9授权 10清空)',
                           `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作描述',
                           `request_method` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '请求方法(GET/POST/PUT/DELETE)',
                           `method` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作方法(类名.方法名)',
                           `oper_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '请求URL',
                           `user_id` bigint DEFAULT '0' COMMENT '操作人员ID',
                           `user_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作人员账号',
                           `user_roles` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作人员角色',
                           `oper_ip` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '主机地址',
                           `oper_location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作地点',
                           `oper_param` text COLLATE utf8mb4_unicode_ci COMMENT '请求参数',
                           `before_data` text COLLATE utf8mb4_unicode_ci COMMENT '操作前数据',
                           `json_result` text COLLATE utf8mb4_unicode_ci COMMENT '返回结果',
                           `status` int DEFAULT '0' COMMENT '操作状态(0成功 1失败)',
                           `error_msg` text COLLATE utf8mb4_unicode_ci COMMENT '错误消息',
                           `oper_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                           `cost_time` bigint DEFAULT '0' COMMENT '消耗时间(毫秒)',
                           `user_agent` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户代理',
                           `browser` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '浏览器类型',
                           `os` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作系统',
                           PRIMARY KEY (`oper_id`),
                           KEY `idx_tenant_id` (`tenant_id`),
                           KEY `idx_user_id` (`user_id`),
                           KEY `idx_oper_type` (`oper_type`),
                           KEY `idx_oper_time` (`oper_time`),
                           KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
INSERT INTO `sys_log` VALUES (50,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,'{\"code\":200,\"data\":[\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\"],\"isSecurity\":false,\"msg\":\"操作成功\",\"status\":true}',0,NULL,'2026-01-15 18:02:19',122,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(51,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,NULL,1,'MmsException(code=500, message=该 SQL 已在 1 分钟内执行过，请勿重复执行, detailMessage=null)\n	at com.sxpcwlkj.gen.controller.GeneratorController.executeSql(GeneratorController.java:104)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:360)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor.invoke(AfterReturningAdviceInterceptor.java:57)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodBeforeAdviceInterceptor.java:58)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:728)\n	at com.sxpcwlkj.gen.controller.GeneratorController$$SpringCGLIB$$0.executeSql(<generated>)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.web.method.support.InvocableHandler','2026-01-15 18:02:21',26,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(52,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,NULL,1,'MmsException(code=500, message=该 SQL 已在 1 分钟内执行过，请勿重复执行, detailMessage=null)\n	at com.sxpcwlkj.gen.controller.GeneratorController.executeSql(GeneratorController.java:104)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:360)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor.invoke(AfterReturningAdviceInterceptor.java:57)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodBeforeAdviceInterceptor.java:58)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:728)\n	at com.sxpcwlkj.gen.controller.GeneratorController$$SpringCGLIB$$0.executeSql(<generated>)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.web.method.support.InvocableHandler','2026-01-15 18:02:22',24,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(53,0,'系统登录',8,'用户退出系统','POST','com.sxpcwlkj.system.controller.AuthController.logout','/system/auth/logout',0,'匿名','','127.0.0.1','内网IP|内网IP','',NULL,'{\"code\":200,\"isSecurity\":false,\"msg\":\"退出成功\",\"status\":true}',0,NULL,'2026-01-16 09:07:37',108,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(54,0,'系统登录',7,'用户登录系统','POST','com.sxpcwlkj.system.controller.AuthController.login','/system/auth/login',0,'匿名','','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"codeKey\":\"e5cd7e4891bf95d1d19206ce24a7b32e\",\"password\":\"******\",\"code\":\"\",\"rememberMe\":true,\"uuid\":\"\",\"username\":\"admin\"}}',NULL,'{\"code\":200,\"data\":{\"userInfo\":{\"passwordStrength\":\"一般\",\"authBtnList\":[\"system:dict:list\",\"system:user:query\",\"system:function:list\",\"system:role:list\",\"system:oss:list\",\"system:dict:insert\",\"system:model:common\",\"system:model:menu\",\"super_admin,admin\",\"super_admin,admin\",\"system:sysLog:list\",\"system:notice:list\",\"system:model:menu\",\"system:oss:query\",\"system:config:list\",\"system:role:query\",\"system:dept:list\",\"system:function:query\",\"super_admin,admin\",\"system:user:list\",\"system:function:edit\",\"system:notice:insert\",\"system:dept:insert\",\"system:config:insert\",\"super_admin,admin\",\"system:user:edit\",\"system:oss:edit\",\"system:sysLog:insert\",\"system:dict:query\",\"system:role:edit\",\"system:dict:edit\",\"system:config:delete\",\"system:sysLog:delete\",\"system:function:delete\",\"system:role:insert\",\"system:dept:delete\",\"system:user:insert\",\"system:oss:insert\",\"system:notice:delete\",\"system:dept:edit\",\"system:function:insert\",\"system:dict:delete\",\"system:notice:edit\",\"system:config:edit\",\"super_admin,admin\",\"system:role:delete\",\"system:oss:delete\",\"system:user:delete\",\"system:sysLog:edit\",\"system:notice:export\",\"system:notice:import\",\"system:notice:query\",\"system:notice:print\",\"system:sysLog:query\",\"system:user:import\",\"system:dept:print\",\"system:dept:export\",\"system:dept:import\",\"system:dept:query\",\"system:config:print\",\"system:config:export\",\"system:config:import\",\"system:config:query\",\"system:user:export\",\"system:sysLog:import\",\"system:sysLog:export\",\"system:user:print\",\"system:sysLog:print\",\"super_admin,admin\"],\"nickName\":\"MMS\",\"wxOpenid\":\"\",\"roles\":[\"super_admin\"],\"sex\":\"0\",\"photo\":\"https://sxpcwlkj.oss-cn-beijing.aliyuncs.com/test/boy.jpg\",\"loginDate\":\"Fri Jan 16 09:07:42 CST 2026\",\"userName\":\"admin\",\"phoneNumber\":\"133****9990\",\"loginIp\":\"0:0:0:0:0:0:0:1\",\"roleName\":\"超级管理员\",\"email\":\"8**@qq.com\"},\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiIxIiwicm5TdHIiOiJJZUVHTXNKMklXWkRSVFV2bzhpR2piUzRYQW9iQWJHaCIsImlkIjoiMSJ9.IXvF7sfGrYap_HQf5M',0,NULL,'2026-01-16 09:07:42',385,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(55,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,'{\"code\":200,\"data\":[\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\"],\"isSecurity\":false,\"msg\":\"操作成功\",\"status\":true}',0,NULL,'2026-01-16 09:08:02',68,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(56,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,NULL,1,'MmsException(code=500, message=该 SQL 已在 1 分钟内执行过，请勿重复执行, detailMessage=null)\n	at com.sxpcwlkj.gen.controller.GeneratorController.executeSql(GeneratorController.java:104)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:360)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor.invoke(AfterReturningAdviceInterceptor.java:57)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodBeforeAdviceInterceptor.java:58)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:728)\n	at com.sxpcwlkj.gen.controller.GeneratorController$$SpringCGLIB$$0.executeSql(<generated>)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.web.method.support.InvocableHandler','2026-01-16 09:08:11',30,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(57,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,NULL,1,'MmsException(code=500, message=该 SQL 已在 1 分钟内执行过，请勿重复执行, detailMessage=null)\n	at com.sxpcwlkj.gen.controller.GeneratorController.executeSql(GeneratorController.java:104)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:360)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor.invoke(AfterReturningAdviceInterceptor.java:57)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodBeforeAdviceInterceptor.java:58)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:173)\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:728)\n	at com.sxpcwlkj.gen.controller.GeneratorController$$SpringCGLIB$$0.executeSql(<generated>)\n	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\n	at java.base/java.lang.reflect.Method.invoke(Method.java:580)\n	at org.springframework.web.method.support.InvocableHandler','2026-01-16 09:08:37',22,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(58,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,'{\"code\":200,\"data\":[\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\",\"受影响行数: 1\"],\"isSecurity\":false,\"msg\":\"操作成功\",\"status\":true}',0,NULL,'2026-01-16 09:09:50',115,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(59,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,'{\"code\":500,\"isSecurity\":false,\"msg\":\"该 SQL 已在 1 分钟内执行过，请勿重复执行\",\"status\":false}',0,NULL,'2026-01-16 09:09:53',50,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX'),(60,0,'系统管理',1,'新增数据','POST','com.sxpcwlkj.gen.controller.GeneratorController.executeSql','/gen/generator/executeSql',1,'admin','super_admin','127.0.0.1','内网IP|内网IP','{\"arg0\":{\"tableId\":\"2011709057713762306\",\"sql\":\"\\n#菜单\\nINSERT INTO `sys_function`(`parent_id`,`path`,`name`,`component`,`language_code`,`type`,`sort`,`icon`,`status`,`visible`,`is_iframe`,`is_open_link`,`is_link`,`keep_alive`,`always_show`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues (3,\'/system/sysLog\',\'操作日志记录表\',\'system/sysLog/index\',\'操作日志记录表\',1,1,\'\',1,-1,-1,-1,\'\',1,-1,\'000000\',now(),now(),1);\\n\\n#列表\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-列表\',\'system:sysLog:list\',2,2,1,\'000000\',now(),now(),1);\\n\\n#新增\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-新增\',\'system:sysLog:insert\',2,3,1,\'000000\',now(),now(),1);\\n\\n#删除\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-删除\',\'system:sysLog:delete\',2,4,1,\'000000\',now(),now(),1);\\n\\n#编辑\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-编辑\',\'system:sysLog:edit\',2,5,1,\'000000\',now(),now(),1);\\n\\n#查询\\nINSERT INTO `sys_function`(`parent_id`,`name`,`permission`,`type`,`sort`,`status`,`tenant_id`,`created_time`,`updated_time`,`revision`)\\nvalues ((SELECT `min_id` FROM  (SELECT MAX(id) AS min_id FROM `sys_function` WHERE `name` = \'操作日志记录表\') AS a),\'操作日志记录表-查询\',\'system:sysLog:query\',2,6,1,\'000000\',n',NULL,'{\"code\":500,\"isSecurity\":false,\"msg\":\"该 SQL 已在 1 分钟内执行过，请勿重复执行\",\"status\":false}',0,NULL,'2026-01-16 09:09:55',27,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36','Chrome 138.0.0.0','OSX');
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
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
INSERT INTO `sys_notice` VALUES ('1793562396983791618','MMS 开源啦','<p>MMS-V1.0.0 开源啦！！！?</p>',1,1,'','000000','2','1','2024-05-23 16:36:29','1','2025-08-26 01:39:23',1),('1960033260410167297','v1.0.0 稳定版 2024-02-10','<ol><li style=\"text-align: start;\">[新增] mms-system 管理系统模块</li><li style=\"text-align: start;\">[新增] mms-common 公共模块抽取</li><li style=\"text-align: start;\">[新增] mms-redis 独立redis模块</li><li style=\"text-align: start;\">[新增] mms-framework 系统核心模块</li><li style=\"text-align: start;\">[新增] mms-sms 短信模块</li><li style=\"text-align: start;\">[新增] mms-email 邮件模块</li><li style=\"text-align: start;\">[新增] mms-oss 对象存储模块</li></ol>',1,1,'','000000','1','1','2025-08-26 01:35:20','1','2025-08-26 01:35:20',1),('1960033343134425090','v1.0.1 稳定版 2024-05-10','<ol><li style=\"text-align: start;\">[新增] mms-aliyun 阿里云生态模块</li><li style=\"text-align: start;\">[新增] mms-wx 微信生态模块</li><li style=\"text-align: start;\">[新增] mms-demo Demo模块<br></li></ol>',1,1,'','000000','1','1','2025-08-26 01:35:39','1','2025-08-26 01:35:39',1),('1960033470939062273','v1.0.2 稳定版 2024-08-02','<ol><li style=\"text-align: start;\">[升级] mms-generator 代码生产模块</li><li style=\"text-align: start;\">[新增] mms-power job 分布式定时任务模块</li><li style=\"text-align: start;\">[新增] mms-mq 消息队列模块</li><li style=\"text-align: start;\">[新增] mms-monitor 健康检测扩展模块</li><li style=\"text-align: start;\">[优化] mms-ui 优化适配<br></li></ol>',1,1,'','000000','1','1','2025-08-26 01:36:10','1','2025-08-26 01:36:10',1),('1960033553646542849','v1.0.3 稳定版 2024-10-20','<ol><li style=\"text-align: start;\">[更新] mms-ui sass文件的引入方式，由替<span style=\"color: rgb(168, 177, 255); background-color: rgba(101, 117, 133, 0.16); font-size: 14px;\"><code>@import</code></span>换为<span style=\"color: rgb(168, 177, 255); background-color: rgba(101, 117, 133, 0.16); font-size: 14px;\"><code>@use</code></span></li></ol><blockquote style=\"text-align: start;\">（Dart Sass originally used an API based on the one used by Node Sass, but replaced it with a new, modern API in Dart Sass 1.45.0. The legacy JS API is now deprecated and will be removed in Dart Sass 2.0.0.）<br></blockquote>',1,1,'','000000','1','1','2025-08-26 01:36:30','1','2025-08-26 01:36:30',1),('1960033627034279938','v1.0.4 稳定版 2024-11-14','<ol><li style=\"text-align: start;\">[新增] mms-admin， 整合 easyexcel 支持数据的导入/导出（支持字典的自动翻译和导入的逆翻译）</li><li style=\"text-align: start;\">[新增] mms-ui ，对Table列表页面进行工具栏的封装（表格数据的导出，导入，打印）等功能</li><li style=\"text-align: start;\">[优化] 系统整体的架构布局优化</li><li style=\"text-align: start;\">[预告] 接口加密，接口防抖，接口限流等技术</li></ol>',1,1,'','000000','1','1','2025-08-26 01:36:47','1','2025-08-26 01:36:47',1),('1960033748283219969','v1.0.5 稳定版 2024-11-14 ','<h2 style=\"text-align: start;\">v1.0.5 稳定版 2024-11-14</h2><ol><li style=\"text-align: start;\">[新增] mms-admin， 整合 easyexcel 支持数据的导入/导出（支持字典的自动翻译和导入的逆翻译）</li><li style=\"text-align: start;\">[新增] mms-ui ，对Table列表页面进行工具栏的封装（表格数据的导出，导入，打印）等功能</li><li style=\"text-align: start;\">[优化] 系统整体的架构布局优化</li><li style=\"text-align: start;\">[预告] 接口加密，接口防抖，接口限流等技术</li></ol>',1,1,'','000000','2','1','2025-08-26 01:37:16','1','2025-08-27 17:42:43',1),('1960034005591187457','v1.0.6 稳定版 2024-11-14','<ol><li style=\"text-align: start;\">[新增] 微信二维码扫描登录</li><li style=\"text-align: start;\">[新增] 个人中心手机号绑定</li><li style=\"text-align: start;\">[新增] 个人中心邮箱绑定</li><li style=\"text-align: start;\">[修复] 个人中心微信号绑定</li><li style=\"text-align: start;\">[预告] 修复已知BUG<br></li></ol>',1,1,'','000000','1','1','2025-08-26 01:38:17','1','2025-08-26 01:38:17',1),('1960034126479417345','v1.0.7 稳定版 2025-02-03','<ol><li style=\"text-align: start;\">[新增] 新增 <a href=\"https://mmsadmin.cn/mms-admin/rateLimit.html\" target=\"_blank\">接口的限流</a></li><li style=\"text-align: start;\">[升级] 升级mms工程的核心Maven依赖jar版本</li><li style=\"text-align: start;\">[修复] 修复已知BUG</li></ol>',1,1,'','000000','1','1','2025-08-26 01:38:46','1','2025-08-26 01:38:46',1),('1960742560774414337','v1.1.0稳定版 2025-028-28 mms-ui 更新 ','<p>1. 登录页面、控制台的界面、动效调整</p><p>2. 优化了架构的不规范性</p><p>3.实现了代码质量检测、0报警、0错误</p><p>4.实现了许多的使用工具，具体见mmsAdmin文档站。</p>',1,1,'','000000','2','1','2025-08-28 00:33:50','1','2025-08-28 00:34:26',1);
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
INSERT INTO `sys_oss_config` VALUES (1810319364730155009,'local-plus','mms','mms','mms','localFile','','http://localhost:8080','N','1','public-read','/Users/shanpengnian/mms/',1,1,'2','','1','2024-07-08 22:25:45','1','2025-09-08 12:54:37','');
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
INSERT INTO `sys_post` VALUES ('1','manager','总经理',1,1,NULL,'000000','1','1','2024-07-06 12:29:51','1','2024-07-06 12:29:51'),('2','employee','员工',2,1,NULL,'000000','1','1','2024-07-06 12:29:51','1','2024-07-06 12:29:51');
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
INSERT INTO `sys_role` VALUES ('1','超级管理员','super_admin',1,NULL,NULL,'1',1,'无所不能','000000','1','1','2024-01-10 08:59:22','1','2024-12-16 23:21:24'),('1744898370860208129','管理员','admin',2,NULL,NULL,'1',2,'','000000','2','1','2024-01-10 09:46:05','1','2025-08-25 00:18:51');
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
INSERT INTO `sys_role_function` VALUES ('1868677812449722369','1','0',NULL,0,0,'000000','0','1','2024-12-16 23:21:23','1','2024-12-16 23:21:23'),('1868677812479082497','1','1',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812500054017','1','2',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812516831234','1','1855872016117329921',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812529414146','1','1854788379443027970',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812546191361','1','1793580098460786690',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812562968578','1','3',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812579745793','1','5',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812592328706','1','11',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812613300226','1','9',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812625883137','1','10',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812642660354','1','12',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812655243266','1','13',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812672020482','1','1856203155570196481',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812688797698','1','1856206343090241538',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812701380609','1','1856208397166739457',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812713963522','1','8',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812726546433','1','1809852682548662273',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812743323650','1','1809852753004580866',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812760100866','1','1809852844230692866',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812772683778','1','1809852932239773698',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812785266689','1','1809853053874589698',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812802043905','1','4',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812810432514','1','1809850786723254273',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812827209730','1','1809850967275458562',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812839792641','1','1809851073483624450',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812852375554','1','1809851347346509825',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812864958466','1','1809851230254125057',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812877541377','1','1856208397166739485',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812890124290','1','1856208397166739486',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812906901506','1','1856208397166739487',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812919484417','1','1856208397166739488',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812932067329','1','1856208397166739489',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812944650241','1','1856208397166739490',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812961427457','1','1856208397166739491',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812974010370','1','1856208397166739492',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812986593281','1','1856208397166739493',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677812999176193','1','7',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813011759105','1','14',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813024342017','1','15',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813036924929','1','1809854968423260162',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813049507841','1','1809855292018008066',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813066285057','1','1809855369251921921',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813078867970','1','26',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813091450881','1','1809954418709798913',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813104033793','1','1809954506823737346',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813125005314','1','1809954598532194306',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813137588226','1','1809954692224557058',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813150171138','1','1809954777297625090',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813166948353','1','1856208397166739476',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813179531265','1','1856208397166739477',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813192114178','1','1856208397166739478',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813204697090','1','1856208397166739479',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813221474305','1','1856208397166739480',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813234057217','1','1856208397166739481',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813246640130','1','1856208397166739482',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813259223042','1','1856208397166739483',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813276000257','1','1856208397166739484',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813292777473','1','1856208397166739494',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813305360386','1','1856208397166739495',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813317943298','1','1856208397166739496',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813334720513','1','1856208397166739497',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813364080641','1','1856208397166739498',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813376663554','1','1856208397166739499',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813389246465','1','1856208397166739500',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813401829377','1','1856208397166739501',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813414412290','1','1856208397166739502',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813426995202','1','18',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813439578113','1','23',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813452161026','1','25',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1868677813464743938','1','24',NULL,1,0,'000000','0','1','2024-12-16 23:21:24','1','2024-12-16 23:21:24'),('1959651622798942210','1744898370860208129','1',NULL,0,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651622887022593','1744898370860208129','0',NULL,0,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651622945742849','1744898370860208129','2',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623021240321','1744898370860208129','1855872016117329921',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623096737794','1744898370860208129','1793580098460786690',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623172235265','1744898370860208129','1854788379443027970',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623239344129','1744898370860208129','3',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623314841602','1744898370860208129','5',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623386144770','1744898370860208129','11',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623461642241','1744898370860208129','9',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623537139714','1744898370860208129','10',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623616831490','1744898370860208129','12',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623696523265','1744898370860208129','13',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623767826434','1744898370860208129','1856203155570196481',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623876878337','1744898370860208129','1856206343090241538',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651623960764417','1744898370860208129','1856208397166739457',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624032067586','1744898370860208129','8',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624099176450','1744898370860208129','1809852682548662273',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624166285314','1744898370860208129','1809852753004580866',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624241782785','1744898370860208129','1809852844230692866',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624317280258','1744898370860208129','1809852932239773698',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624388583426','1744898370860208129','1809853053874589698',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624464080898','1744898370860208129','4',NULL,1,0,'000000','1','1','2025-08-25 00:18:50','1','2025-08-25 00:18:50'),('1959651624531189762','1744898370860208129','1809850786723254273',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651624602492930','1744898370860208129','1809850967275458562',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651624669601793','1744898370860208129','1809851073483624450',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651624736710658','1744898370860208129','1809851347346509825',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651624799625218','1744898370860208129','1809851230254125057',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651624866734082','1744898370860208129','1856208397166739485',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651624942231553','1744898370860208129','1856208397166739486',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625005146113','1744898370860208129','1856208397166739487',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625068060674','1744898370860208129','1856208397166739488',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625126780930','1744898370860208129','1856208397166739489',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625193889793','1744898370860208129','1856208397166739490',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625269387265','1744898370860208129','1856208397166739491',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625353273345','1744898370860208129','1856208397166739492',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625407799297','1744898370860208129','1856208397166739493',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625470713858','1744898370860208129','7',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625542017026','1744898370860208129','14',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625600737282','1744898370860208129','15',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625663651841','1744898370860208129','1809854968423260162',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625739149313','1744898370860208129','1809855292018008066',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625823035394','1744898370860208129','1809855369251921921',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625894338562','1744898370860208129','26',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651625953058817','1744898370860208129','1809954418709798913',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626024361985','1744898370860208129','1809954506823737346',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626108248066','1744898370860208129','1809954598532194306',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626171162626','1744898370860208129','1809954692224557058',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626229882881','1744898370860208129','1809954777297625090',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626296991746','1744898370860208129','1856208397166739476',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626380877825','1744898370860208129','1856208397166739477',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626452180994','1744898370860208129','1856208397166739478',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626523484162','1744898370860208129','1856208397166739479',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626598981634','1744898370860208129','1856208397166739480',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626657701889','1744898370860208129','1856208397166739481',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626737393665','1744898370860208129','1856208397166739482',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626808696833','1744898370860208129','1856208397166739483',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626871611393','1744898370860208129','1856208397166739484',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651626934525953','1744898370860208129','1856208397166739494',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627010023426','1744898370860208129','1856208397166739495',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627077132290','1744898370860208129','1856208397166739496',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627140046850','1744898370860208129','1856208397166739497',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627194572801','1744898370860208129','1856208397166739498',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627291041793','1744898370860208129','1856208397166739499',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627362344961','1744898370860208129','1856208397166739500',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627425259521','1744898370860208129','1856208397166739501',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627525922817','1744898370860208129','1856208397166739502',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627593031681','1744898370860208129','25',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51'),('1959651627655946242','1744898370860208129','18',NULL,1,0,'000000','1','1','2025-08-25 00:18:51','1','2025-08-25 00:18:51');
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
INSERT INTO `sys_sign` VALUES ('1911799740660654081','1','8Th0VjuO1G3mNcb7','A505F34E648BE83CAAD8CC4FAF6C4D31',NULL,NULL,'2033-07-01 23:12:32',1,0,'0','000000','1','2025-04-14 23:12:32','1','2025-04-14 23:12:32',NULL),('1912670597993906178','1','8Th0VjuO1G3mNcb7','BDCF00D3DF44FD1CE118D351AC26DACB',NULL,NULL,'2033-07-04 08:53:01',1,NULL,NULL,'000000','1',NULL,NULL,NULL,NULL),('1912673740215439361','1','8Th0VjuO1G3mNcb7','393294C2279A73D01881D2A2CC631CA2',NULL,NULL,'2033-07-04 09:05:30',1,NULL,NULL,'000000','1',NULL,NULL,NULL,NULL),('1959660554078576640','1959651885668556801','8Th0VjuO1G3mNcb7','24C54F2193C112F2274FBE80FB9A6240',NULL,NULL,'2033-11-11 00:54:19',1,0,'0','0','1959651885668556801','2025-08-25 00:54:20',NULL,NULL,NULL);
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
                              `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户编号;',
                              PRIMARY KEY (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统租户;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant`
--

LOCK TABLES `sys_tenant` WRITE;
/*!40000 ALTER TABLE `sys_tenant` DISABLE KEYS */;
INSERT INTO `sys_tenant` VALUES ('000000','超级管理员租户','1','西决','13388886557',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'');
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
INSERT INTO `sys_user` (`user_id`, `dept_id`, `post_ids`, `user_name`, `nick_name`, `user_type`, `email`, `phone_number`, `wx_openid`, `wx_un_open_id`, `sex`, `avatar`, `aes_key`, `password`, `password_strength`, `status`, `del_flag`, `login_ip`, `login_date`, `public_key`, `private_key`, `remark`, `tenant_id`, `revision`, `created_by`, `created_time`, `updated_by`, `updated_time`, `sort`) VALUES ('1','1','1,2','admin','MMS','1','888@qq.com','13388899990','',NULL,0,'https://sxpcwlkj.oss-cn-beijing.aliyuncs.com/test/boy.jpg','8AAB8216D19ADED25549DF7F156E7642','b00017516f30a24393de6b10da8512ef','一般',1,NULL,'0:0:0:0:0:0:0:1','2026-01-16 09:07:42',NULL,NULL,'无所不能...','000000','46','1','2023-03-24 10:32:10','1','2026-01-14 16:55:58',1),('1959651885668556801','3','','mms','MMS','1','9200@qq.com','15588888888',NULL,'',0,'https://sxpcwlkj.oss-cn-beijing.aliyuncs.com/default_avatar.png','BA63ED5DED0BDCC2A50B161A35A474DB','be763c97c01ce266791d4e0f789dea4e','中等',1,NULL,'0:0:0:0:0:0:0:1','2026-01-15 16:48:39',NULL,NULL,'管理员','000000','7','1','2025-08-25 00:19:53','1','2025-08-31 00:27:30',1);
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
INSERT INTO `sys_user_role` VALUES ('1','1','1',1,0,NULL,'000000','1','1','2024-01-02 16:29:08','1','2024-07-06 12:31:47'),('1959651923740254209','1959651885668556801','1744898370860208129',1,0,NULL,'000000','1','1','2025-08-25 00:20:02','1','2025-08-25 00:20:02');
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

-- Dump completed on 2026-01-16  9:41:16
