-- MMS 开源初始化：PowerJob 4.3.9（pj_ 前缀）
-- UTF-8 / LF；MySQL 5.7.8+、8.x；InnoDB DYNAMIC / utf8mb4_unicode_ci。
-- 仅用于全新空库；已有表将报错。请先阅读同目录 README.md。

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- pj_app_info
CREATE TABLE `pj_app_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `current_server` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx01_app_info` (`app_name`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_container_info
CREATE TABLE `pj_container_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint DEFAULT NULL,
  `container_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `last_deploy_time` datetime(6) DEFAULT NULL,
  `source_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source_type` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx01_container_info` (`app_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_instance_info
CREATE TABLE `pj_instance_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_trigger_time` bigint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `expected_trigger_time` bigint DEFAULT NULL,
  `finished_time` bigint DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `instance_id` bigint DEFAULT NULL,
  `instance_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `job_id` bigint DEFAULT NULL,
  `job_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `last_report_time` bigint DEFAULT NULL,
  `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `running_times` bigint DEFAULT NULL,
  `status` int DEFAULT NULL,
  `task_tracker_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` int DEFAULT NULL,
  `wf_instance_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx01_instance_info` (`job_id`,`status`),
  KEY `idx02_instance_info` (`app_id`,`status`),
  KEY `idx03_instance_info` (`instance_id`,`status`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_job_info
CREATE TABLE `pj_job_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `alarm_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `concurrency` int DEFAULT NULL,
  `designated_workers` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dispatch_strategy` int DEFAULT NULL,
  `execute_type` int DEFAULT NULL,
  `extra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `instance_retry_num` int DEFAULT NULL,
  `instance_time_limit` bigint DEFAULT NULL,
  `job_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `job_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `lifecycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `log_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_instance_num` int DEFAULT NULL,
  `max_worker_count` int DEFAULT NULL,
  `min_cpu_cores` double NOT NULL,
  `min_disk_space` double NOT NULL,
  `min_memory_space` double NOT NULL,
  `next_trigger_time` bigint DEFAULT NULL,
  `notify_user_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `processor_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `processor_type` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `task_retry_num` int DEFAULT NULL,
  `time_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `time_expression_type` int DEFAULT NULL,
  `advanced_runtime_config` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dispatch_strategy_config` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx01_job_info` (`app_id`,`status`,`time_expression_type`,`next_trigger_time`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_oms_lock
CREATE TABLE `pj_oms_lock` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `lock_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_lock_time` bigint DEFAULT NULL,
  `ownerip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx01_oms_lock` (`lock_name`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_server_info
CREATE TABLE `pj_server_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx01_server_info` (`ip`),
  KEY `idx01_server_info` (`gmt_modified`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_user_info
CREATE TABLE `pj_user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `web_hook` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx01_user_name` (`username`),
  KEY `uidx01_user_info` (`username`),
  KEY `uidx02_user_info` (`email`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_workflow_info
CREATE TABLE `pj_workflow_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint DEFAULT NULL,
  `extra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `lifecycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_wf_instance_num` int DEFAULT NULL,
  `next_trigger_time` bigint DEFAULT NULL,
  `notify_user_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pedag` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `status` int DEFAULT NULL,
  `time_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `time_expression_type` int DEFAULT NULL,
  `wf_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `wf_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx01_workflow_info` (`app_id`,`status`,`time_expression_type`,`next_trigger_time`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_workflow_instance_info
CREATE TABLE `pj_workflow_instance_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_trigger_time` bigint DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `dag` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `expected_trigger_time` bigint DEFAULT NULL,
  `finished_time` bigint DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `parent_wf_instance_id` bigint DEFAULT NULL,
  `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `status` int DEFAULT NULL,
  `wf_context` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `wf_init_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `wf_instance_id` bigint DEFAULT NULL,
  `workflow_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx01_wf_instance` (`wf_instance_id`),
  KEY `idx01_wf_instance` (`workflow_id`,`status`,`app_id`,`expected_trigger_time`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- pj_workflow_node_info
CREATE TABLE `pj_workflow_node_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint NOT NULL,
  `enable` bit(1) NOT NULL,
  `extra` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `gmt_create` datetime(6) NOT NULL,
  `gmt_modified` datetime(6) NOT NULL,
  `job_id` bigint DEFAULT NULL,
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `node_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `skip_when_failed` bit(1) NOT NULL,
  `type` int DEFAULT NULL,
  `workflow_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx01_workflow_node_info` (`workflow_id`,`gmt_create`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
