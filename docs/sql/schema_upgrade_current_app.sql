-- =============================================================================
-- 聚星 / RuoYi 业务库：与当前后端代码对齐的结构升级（可重复执行）
-- =============================================================================
-- 适用：从 Navicat 导出的 juxing-master 或同类库；执行前请全库备份。
-- MySQL 5.7+ / 8.0
--
-- 设计约定（与业务划分一致）：
--   sys_user / sys_role / sys_user_role：仅服务于「管理端若依」登录与后台权限。
--   家长端、教师端、订单、会员等业务用户与外键：以业务表为准（如 users、user、student 等），
--   不要将 order.user_id、teacher.user_id 等强行改为引用 sys_user，除非单独做账号合并与数据迁移。
--
-- 内容概览：
--   1) 安全补列：teacher.area_id/grade_id、course.address、product 缺列时补 stock/image
--   2) users：password 列、users_type 含 parent（业务端注册/登录）
--   3) review.status：由 enum(pending/approved/rejected) 转为与代码一致的 0/1/2
--   4) 角色：parent / teacher + 菜单（管理端 sys_role，与小程序身份展示可并存）
--   5) 系统参数：开启自助注册
--
-- 不包含：修复 order→user、user_membership→membership_card 等错误外键（需单独评估后手工改）
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ---------------------------------------------------------------------------
-- 工具过程：仅当列不存在时 ADD COLUMN（执行完自动删除）
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_add_column_if_missing;

DELIMITER $$
CREATE PROCEDURE sp_add_column_if_missing(
    IN p_table VARCHAR(64),
    IN p_column VARCHAR(64),
    IN p_definition TEXT   -- 类型与位置等，例如：VARCHAR(64) NULL COMMENT 'x' AFTER subject_id
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = p_table
          AND COLUMN_NAME = p_column
    ) THEN
        SET @ddl = CONCAT('ALTER TABLE `', p_table, '` ADD COLUMN `', p_column, '` ', p_definition);
        PREPARE stmt FROM @ddl;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$
DELIMITER ;

-- teacher：与 TeacherMapper 一致
CALL sp_add_column_if_missing('teacher', 'area_id',
    "VARCHAR(64) NULL COMMENT '地区ID(area_jinan)' AFTER subject_id");
CALL sp_add_column_if_missing('teacher', 'grade_id',
    "VARCHAR(64) NULL COMMENT '年级ID(grade_level)' AFTER area_id");

-- course：与 CourseMapper 一致（上课地址）
CALL sp_add_column_if_missing('course', 'address',
    "VARCHAR(512) NULL COMMENT '上课地址(含时段说明)' AFTER start_date");

-- product：juxing 库多数已有 stock；缺省时补全，避免下单扣库存报错
CALL sp_add_column_if_missing('product', 'stock',
    "INT NULL DEFAULT NULL COMMENT '库存(NULL表示不限制)' AFTER price");
CALL sp_add_column_if_missing('product', 'image',
    "VARCHAR(255) NULL DEFAULT NULL COMMENT '图片' AFTER created_at");

DROP PROCEDURE IF EXISTS sp_add_column_if_missing;

SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------------
-- users：业务端登录密码 + 家长类型（与 /register/app、/app/auth/*、/codeLogin 一致）
-- password 列幂等添加；users_type 若已含 parent 可忽略 ALTER 报错
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_add_column_if_missing;

DELIMITER $$
CREATE PROCEDURE sp_add_column_if_missing(
    IN p_table VARCHAR(64),
    IN p_column VARCHAR(64),
    IN p_definition TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = p_table
          AND COLUMN_NAME = p_column
    ) THEN
        SET @ddl = CONCAT('ALTER TABLE `', p_table, '` ADD COLUMN `', p_column, '` ', p_definition);
        PREPARE stmt FROM @ddl;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$
DELIMITER ;

CALL sp_add_column_if_missing('users', 'password',
    "VARCHAR(100) NULL COMMENT 'BCrypt，业务端密码登录' AFTER phone");

DROP PROCEDURE IF EXISTS sp_add_column_if_missing;

-- 由 teacher/student 扩展出 parent。若当前 users_type 为 VARCHAR 等非 enum，请手工改表后再注册 parent。
ALTER TABLE `users` MODIFY COLUMN `users_type` ENUM('teacher','student','parent') NOT NULL DEFAULT 'student';

-- ---------------------------------------------------------------------------
-- review.status：代码与小程序使用 0=待审 / 1=通过 / 2=驳回（字符串或数字比较）
-- 若当前为 enum('pending','approved','rejected')，先迁移再改类型
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_fix_review_status;

DELIMITER $$
CREATE PROCEDURE sp_fix_review_status()
BEGIN
    DECLARE col_type VARCHAR(128);
    SELECT DATA_TYPE INTO col_type
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'review'
      AND COLUMN_NAME = 'status'
    LIMIT 1;

    -- 仅处理 Navicat 旧库中的 enum；已是 char/tinyint 且存 0/1/2 时不修改
    IF col_type = 'enum' THEN
        ALTER TABLE `review` MODIFY COLUMN `status` VARCHAR(16) NOT NULL DEFAULT '0';
        UPDATE `review` SET `status` = '0' WHERE `status` IN ('pending', '0');
        UPDATE `review` SET `status` = '1' WHERE `status` IN ('approved', '1');
        UPDATE `review` SET `status` = '2' WHERE `status` IN ('rejected', '2');
        ALTER TABLE `review` MODIFY COLUMN `status` CHAR(1) NOT NULL DEFAULT '0'
            COMMENT '0待审核 1已通过 2已驳回';
    END IF;
END$$
DELIMITER ;

CALL sp_fix_review_status();
DROP PROCEDURE IF EXISTS sp_fix_review_status;

-- ---------------------------------------------------------------------------
-- 角色：学生 / 家长 / 教师（registerRole=student|parent|teacher；学生与家长同侧）
-- ---------------------------------------------------------------------------
INSERT INTO sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
SELECT '学生', 'student', 5, '1', 1, 1, '0', '0', 'system', NOW(), '业务端学生注册'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'student' AND del_flag = '0');

INSERT INTO sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
SELECT '家长', 'parent', 3, '1', 1, 1, '0', '0', 'system', NOW(), '业务端家长注册'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'parent' AND del_flag = '0');

INSERT INTO sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
SELECT '教师', 'teacher', 4, '1', 1, 1, '0', '0', 'system', NOW(), '业务端教师注册'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'teacher' AND del_flag = '0');

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
INNER JOIN sys_role_menu m ON m.role_id = 2
WHERE r.role_key = 'student' AND r.del_flag = '0'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu x WHERE x.role_id = r.role_id AND x.menu_id = m.menu_id
  );

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
INNER JOIN sys_role_menu m ON m.role_id = 2
WHERE r.role_key = 'parent' AND r.del_flag = '0'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu x WHERE x.role_id = r.role_id AND x.menu_id = m.menu_id
  );

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
INNER JOIN sys_role_menu m ON m.role_id = 2
WHERE r.role_key = 'teacher' AND r.del_flag = '0'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu x WHERE x.role_id = r.role_id AND x.menu_id = m.menu_id
  );

-- ---------------------------------------------------------------------------
-- 开启自助注册（juxing-master 导出里常为 false）
-- ---------------------------------------------------------------------------
UPDATE sys_config
SET config_value = 'true', update_time = NOW()
WHERE config_key = 'sys.account.registerUser';

-- =============================================================================
-- 可选：数据修补（按需取消注释）
-- =============================================================================

-- 老教师无地区/年级时，可按默认区占位，避免「仅筛地区」时一条都没有（请按实际字典改 ID）
-- UPDATE teacher SET area_id = '2' WHERE area_id IS NULL OR area_id = '';
-- UPDATE teacher SET grade_id = '1' WHERE grade_id IS NULL OR grade_id = '';

-- =============================================================================
-- 执行后建议手工跑下列检查（结果应为 0 行 = 无脏数据）
-- =============================================================================
-- SELECT COLUMN_NAME, DATA_TYPE FROM information_schema.COLUMNS
--   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'teacher'
--   AND COLUMN_NAME IN ('area_id','grade_id');
-- SELECT COLUMN_NAME FROM information_schema.COLUMNS
--   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'course' AND COLUMN_NAME = 'address';
-- SELECT * FROM sys_role WHERE role_key IN ('parent','teacher');
-- SELECT config_key, config_value FROM sys_config WHERE config_key = 'sys.account.registerUser';
