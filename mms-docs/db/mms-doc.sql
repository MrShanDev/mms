-- MySQL dump 10.13  Distrib 8.0.41, for macos15.2 (arm64)
--
-- Host: 119.28.10.247    Database: mms
-- ------------------------------------------------------
-- Server version	8.0.24

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
-- Table structure for table `doc_config`
--

DROP TABLE IF EXISTS `doc_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doc_config` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `key` varchar(255) DEFAULT NULL COMMENT 'KEY',
  `value` varchar(255) DEFAULT NULL COMMENT '值',
  `ctime` datetime DEFAULT NULL COMMENT '创建时间',
  `mtime` datetime DEFAULT NULL COMMENT '更新时间',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '1' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文档配置;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doc_config`
--

LOCK TABLES `doc_config` WRITE;
/*!40000 ALTER TABLE `doc_config` DISABLE KEYS */;
INSERT INTO `doc_config` VALUES ('1','WEEK','7','2025-05-28 16:29:54','2025-05-28 16:30:03',1,1,'1','0',NULL,NULL,NULL,NULL,NULL),('2','MONTH','30','2025-05-28 16:29:59','2025-05-28 16:30:03',1,2,'1','0',NULL,NULL,NULL,NULL,NULL),('3','PERPETUAL','999999999','2025-05-28 16:30:03','2025-05-28 16:30:03',1,3,'1','0',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `doc_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doc_order`
--

DROP TABLE IF EXISTS `doc_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doc_order` (
  `order_id` varchar(32) NOT NULL COMMENT '订单编号',
  `uid` varchar(32) DEFAULT NULL COMMENT '用户编号',
  `txn_amt` decimal(24,2) DEFAULT NULL COMMENT '订单金额',
  `pay_mchid` varchar(255) DEFAULT NULL COMMENT '支付商户号',
  `pay_no` varchar(255) DEFAULT NULL COMMENT '支付平台流水号',
  `pay_timeout` varchar(255) DEFAULT NULL COMMENT '支付超时时间',
  `prod_id` varchar(255) DEFAULT NULL COMMENT '产品编号',
  `prod_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `prod_price` decimal(24,2) DEFAULT NULL COMMENT '产品价格',
  `prod_type` varchar(255) DEFAULT NULL COMMENT '产品类型',
  `ctime` datetime DEFAULT NULL COMMENT '创建时间',
  `mtime` datetime DEFAULT NULL COMMENT '更新时间',
  `status` int DEFAULT '0' COMMENT '订单状态;unpaid：待支付 paysuc：已支付 refund：已退款 cancel：已取消 finish：已完成',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '1' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文档订单;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doc_order`
--

LOCK TABLES `doc_order` WRITE;
/*!40000 ALTER TABLE `doc_order` DISABLE KEYS */;
INSERT INTO `doc_order` VALUES ('1927764361010667522','1927763462003548162',990.00,NULL,'1748449813818','1748449993821','1','周卡',990.00,'新手体验者','2025-05-29 00:30:14','2025-05-29 00:30:28',1,0,'2',NULL,NULL,'2025-05-29 00:30:14',NULL,'2025-05-29 00:30:28',NULL),('1930657734985965569','1927763462003548162',2990.00,NULL,'1749139648123','1749139828123','2','月卡',2990.00,'使用MMS项目者','2025-06-06 00:07:29','2025-06-06 00:07:54',1,0,'2','default_tenant','1927763462003548162','2025-06-06 00:07:29','1927763462003548162','2025-06-06 00:07:54',NULL),('1930666662113239042','1927763462003548162',990.00,NULL,'1749141776504','1749141956504','1','周卡',990.00,'新手体验者','2025-06-06 00:42:57','2025-06-06 00:43:15',1,0,'2','default_tenant','1927763462003548162','2025-06-06 00:42:57','1927763462003548162','2025-06-06 00:43:15',NULL);
/*!40000 ALTER TABLE `doc_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doc_product`
--

DROP TABLE IF EXISTS `doc_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doc_product` (
  `prod_id` varchar(255) NOT NULL COMMENT '产品编号',
  `prod_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `unit_price` varchar(255) DEFAULT NULL COMMENT '销售单价',
  `mark_price` varchar(255) DEFAULT NULL COMMENT '市场价格',
  `type` varchar(255) DEFAULT NULL COMMENT '产品类型',
  `code` varchar(100) DEFAULT NULL COMMENT '商品编码',
  `ctime` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `mtime` varchar(255) DEFAULT NULL COMMENT '更新时间',
  `status` int DEFAULT '0' COMMENT '商品状态;up：上架 un：下降 rm：删除',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '1' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文档商品;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doc_product`
--

LOCK TABLES `doc_product` WRITE;
/*!40000 ALTER TABLE `doc_product` DISABLE KEYS */;
INSERT INTO `doc_product` VALUES ('1','周卡','990','1900','新手体验者','WEEK',NULL,NULL,1,0,'1','0',NULL,NULL,NULL,NULL,NULL),('2','月卡','2990','9900','使用MMS项目者','MONTH',NULL,NULL,1,0,'1','0',NULL,NULL,NULL,NULL,NULL),('3','终身卡','9900','19900','忠实的MMS粉丝','PERPETUAL',NULL,NULL,1,0,'1','0',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `doc_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doc_user`
--

DROP TABLE IF EXISTS `doc_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doc_user` (
  `uid` varchar(32) NOT NULL COMMENT '用户编号',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `type` varchar(255) DEFAULT NULL COMMENT '用户类型;usr=普通用户 vip=会员用户',
  `open_id` varchar(100) DEFAULT NULL COMMENT '微信ID',
  `vip_date` datetime DEFAULT NULL COMMENT 'VIP到期时间',
  `ctime` datetime DEFAULT NULL COMMENT '创建时间',
  `mtime` datetime DEFAULT NULL COMMENT '更新时间',
  `status` int DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `revision` varchar(32) DEFAULT '1' COMMENT '乐观锁',
  `tenant_id` varchar(32) DEFAULT '0' COMMENT '租户号',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文档用户;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doc_user`
--

LOCK TABLES `doc_user` WRITE;
/*!40000 ALTER TABLE `doc_user` DISABLE KEYS */;
INSERT INTO `doc_user` VALUES ('1927763462003548162','浪漫的狼86','https://picsum.photos/30/30','usr','oW-WO0Ve8-hrIY42Y8cywfDbD-nk',NULL,'2025-05-29 00:26:40','2025-05-29 00:26:40',0,0,'1',NULL,NULL,'2025-05-29 00:26:40',NULL,'2025-05-29 00:26:40',NULL),('1929438111712075777','冷静的狼36','https://picsum.photos/30/30','usr','oW-WO0Tt3NOzBm5JOyaU-e8Cu_ks',NULL,'2025-06-02 15:21:08','2025-06-02 15:21:08',0,0,'1',NULL,NULL,'2025-06-02 15:21:08',NULL,'2025-06-02 15:21:08',NULL),('1929719140427423745','冷静的狼','https://picsum.photos/30/30','usr','oW-WO0dTlbRAnPxUzYUlgokbsXK0',NULL,'2025-06-03 09:57:50','2025-06-03 09:57:50',0,0,'1',NULL,NULL,'2025-06-03 09:57:50',NULL,'2025-06-03 09:57:50',NULL),('1930451907675086849','神秘的飞鸟','https://picsum.photos/30/30','usr','oW-WO0cfKTtqm9AemJRRXJgS7nQc',NULL,'2025-06-05 10:29:36','2025-06-05 10:29:36',0,0,'1',NULL,NULL,'2025-06-05 10:29:36',NULL,'2025-06-05 10:29:36',NULL),('1930754299545182210','幽默的猎豹582','https://picsum.photos/30/30','usr','oW-WO0d0gtSZOR3U71eGsHq0xNaU',NULL,'2025-06-06 06:31:11','2025-06-06 06:31:11',0,0,'1',NULL,NULL,'2025-06-06 06:31:11',NULL,'2025-06-06 06:31:11',NULL),('1930806934159679489','威武的雄鹰32','https://picsum.photos/30/30','usr','oW-WO0XGxmGIjc8Mo_hpulOT618U',NULL,'2025-06-06 10:00:20','2025-06-06 10:00:20',0,0,'1',NULL,NULL,'2025-06-06 10:00:20',NULL,'2025-06-06 10:00:20',NULL);
/*!40000 ALTER TABLE `doc_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-06 16:21:29
