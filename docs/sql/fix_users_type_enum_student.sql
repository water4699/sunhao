-- 若 users.users_type 为 ENUM 且不含 student，插入 student 会失败。
-- 可重复执行（与 schema_upgrade_current_app.sql 中定义对齐）

ALTER TABLE `users` MODIFY COLUMN `users_type` ENUM('teacher','student','parent') NOT NULL DEFAULT 'student';
