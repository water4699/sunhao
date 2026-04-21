-- 课程预约字段拆分迁移。
-- 目的：把原本拼在 course.address 里的时段、上课地址、联系方式、留言、取消原因拆成结构化字段；
-- 同时增加 publish_id，用于关联老师发布的家教信息并统计预约次数。
-- 可重复执行：字段/索引已存在时只输出提示，不会再次 ALTER。

SET @schema_name := DATABASE();

SET @sql := IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'course' AND COLUMN_NAME = 'publish_id') = 0,
  'ALTER TABLE course ADD COLUMN publish_id INT UNSIGNED NULL COMMENT ''家教发布信息ID'' AFTER teacher_id',
  'SELECT ''course.publish_id already exists'''
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'course' AND COLUMN_NAME = 'time_slot') = 0,
  'ALTER TABLE course ADD COLUMN time_slot VARCHAR(64) NULL COMMENT ''预约上课时段'' AFTER address',
  'SELECT ''course.time_slot already exists'''
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'course' AND COLUMN_NAME = 'class_address') = 0,
  'ALTER TABLE course ADD COLUMN class_address VARCHAR(512) NULL COMMENT ''预约上课地址'' AFTER time_slot',
  'SELECT ''course.class_address already exists'''
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'course' AND COLUMN_NAME = 'contact_info') = 0,
  'ALTER TABLE course ADD COLUMN contact_info VARCHAR(128) NULL COMMENT ''预约联系方式'' AFTER class_address',
  'SELECT ''course.contact_info already exists'''
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'course' AND COLUMN_NAME = 'contact_note') = 0,
  'ALTER TABLE course ADD COLUMN contact_note VARCHAR(512) NULL COMMENT ''预约留言'' AFTER contact_info',
  'SELECT ''course.contact_note already exists'''
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'course' AND COLUMN_NAME = 'cancel_reason') = 0,
  'ALTER TABLE course ADD COLUMN cancel_reason VARCHAR(255) NULL COMMENT ''取消原因'' AFTER contact_note',
  'SELECT ''course.cancel_reason already exists'''
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'course' AND INDEX_NAME = 'idx_course_publish_id') = 0,
  'CREATE INDEX idx_course_publish_id ON course (publish_id)',
  'SELECT ''idx_course_publish_id already exists'''
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
