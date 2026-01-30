
/**
 * 会员模块
 */
CREATE TABLE `store_member` (
      `id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
      `nickname` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
      `account` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号',
      `sex` int DEFAULT NULL COMMENT '性别',
      `phone` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
      `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
      `head_portrait` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
      `birthday` datetime DEFAULT NULL COMMENT '生日',
      `reputation_score` int DEFAULT NULL COMMENT '信用分',
      `level` int DEFAULT NULL COMMENT '级别',
      `invitation_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邀请码',
      `private_key` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '秘钥',
      `wx_openid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信openid',
      `alipay_openid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付宝openId',
      `douyin_openid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '抖音openId',
      `last_login_ip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后登录IP',
      `pay_password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付密码',
      `member_bg_img` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '背景图',
      `status` int DEFAULT '1' COMMENT '状态',
      `sort` int DEFAULT '0' COMMENT '排序',
      `revision` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '乐观锁',
      `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '租户号',
      `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
      `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
      `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
      `latitude` decimal(10,8) DEFAULT NULL COMMENT '纬度',
      `longitude` decimal(11,8) DEFAULT NULL COMMENT '经度',
      `location_updated_time` datetime DEFAULT NULL COMMENT '位置更新时间',
      `city` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所在城市（根据IP解析）',
      `signature` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '个性签名',
      `tags` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户标签（多个标签用逗号分隔）',
      PRIMARY KEY (`id`),
      KEY `idx_location` (`latitude`,`longitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员';


/**
 * 会员收货地址模块
 */
CREATE TABLE `store_member_address` (
      `id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
      `member_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会员ID',
      `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货人',
      `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货手机号',
      `country` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家',
      `province` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省',
      `city` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '市',
      `district` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区/县',
      `address` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '详细地址',
      `is_def` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否默认',
      `status` int DEFAULT '1' COMMENT '状态',
      `sort` int DEFAULT '0' COMMENT '排序',
      `revision` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '乐观锁',
      `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '租户号',
      `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
      `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
      `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员收货地址';

/**
 * 会员认证模块
 */
CREATE TABLE `store_member_authentication` (
         `id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ID',
         `member_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会员ID',
         `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
         `number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证号',
         `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
         `image_front` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证正面',
         `image_back` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证背面',
         `business_license` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '营业执照',
         `sex` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
         `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
         `nationality` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生日',
         `status` int DEFAULT '1' COMMENT '状态',
         `sort` int DEFAULT '0' COMMENT '排序',
         `revision` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '乐观锁',
         `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '租户号',
         `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
         `created_time` datetime DEFAULT NULL COMMENT '创建时间',
         `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
         `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
         `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员认证';

/**
 * 会员行政区域模块
 */
CREATE TABLE `store_tool_area` (
         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
         `name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
         `code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'CODE',
         `parent_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '父CODE',
         `level` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '级别',
         `status` int DEFAULT '0' COMMENT '状态',
         `sort` int DEFAULT '0' COMMENT '排序',
         `revision` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '乐观锁',
         `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '租户号',
         `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
         `created_time` datetime DEFAULT NULL COMMENT '创建时间',
         `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
         `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
         `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3635 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='行政区域;';

