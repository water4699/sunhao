CREATE TABLE IF NOT EXISTS `tutor_request` (
  `request_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '需求单ID',
  `user_id` BIGINT NOT NULL COMMENT '家长用户ID(users.users_id)',
  `subject_id` BIGINT NOT NULL COMMENT '科目ID',
  `grade_id` BIGINT NOT NULL COMMENT '年级ID',
  `area_id` BIGINT NOT NULL COMMENT '地区ID',
  `budget_min` DECIMAL(10,2) NOT NULL COMMENT '预算最小值',
  `budget_max` DECIMAL(10,2) NOT NULL COMMENT '预算最大值',
  `contact_info` VARCHAR(100) NOT NULL COMMENT '联系方式',
  `demand_desc` VARCHAR(1000) NOT NULL COMMENT '需求描述',
  `teach_mode` VARCHAR(32) DEFAULT '' COMMENT '授课方式',
  `preferred_time` VARCHAR(200) DEFAULT '' COMMENT '期望时段',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0待接单 1已接单 2已关闭 3已取消',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`request_id`),
  KEY `idx_tr_user` (`user_id`),
  KEY `idx_tr_status` (`status`),
  KEY `idx_tr_subject` (`subject_id`),
  KEY `idx_tr_grade` (`grade_id`),
  KEY `idx_tr_area` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长找家教需求单';

CREATE TABLE IF NOT EXISTS `tutor_request_bid` (
  `bid_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '接单记录ID',
  `request_id` BIGINT UNSIGNED NOT NULL COMMENT '需求单ID',
  `teacher_id` BIGINT NOT NULL COMMENT '老师ID',
  `message` VARCHAR(1000) DEFAULT '' COMMENT '接单留言',
  `expected_rate` DECIMAL(10,2) DEFAULT NULL COMMENT '期望时薪',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0待家长确认 1已确认 2已拒绝 3已撤回',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`bid_id`),
  KEY `idx_trb_request` (`request_id`),
  KEY `idx_trb_teacher` (`teacher_id`),
  KEY `idx_trb_status` (`status`),
  CONSTRAINT `fk_trb_request` FOREIGN KEY (`request_id`) REFERENCES `tutor_request` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老师接家长需求单记录';
