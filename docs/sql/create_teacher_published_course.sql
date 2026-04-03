CREATE TABLE IF NOT EXISTS `teacher_published_course` (
  `publish_id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '发布ID',
  `teacher_id` int unsigned NOT NULL COMMENT '教师ID',
  `subject_id` int unsigned NOT NULL COMMENT '科目ID',
  `grade_id` int unsigned NOT NULL COMMENT '年级ID',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0上架中 1已下架',
  `start_date` DATE NOT NULL COMMENT '开课日期',
  `address` VARCHAR(512) NOT NULL COMMENT '上课地址或课程说明',
  `expected_hours` SMALLINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '预期课时',
  `hourly_rate` DECIMAL(10,2) NOT NULL COMMENT '课时费',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`publish_id`),
  KEY `idx_tpc_teacher` (`teacher_id`),
  KEY `idx_tpc_subject` (`subject_id`),
  KEY `idx_tpc_grade` (`grade_id`),
  CONSTRAINT `fk_tpc_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`),
  CONSTRAINT `fk_tpc_subject` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`),
  CONSTRAINT `fk_tpc_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade_level` (`grade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老师发布课程表（与学生预约course分离）';
