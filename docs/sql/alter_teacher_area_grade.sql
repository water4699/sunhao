-- 教师表增加地区、年级字段，供小程序「找老师」筛选与老师入驻提交。
-- 在连接业务库后执行；若列已存在会报错，可忽略或手动跳过对应语句。
-- MySQL 5.7+ / 8.0

ALTER TABLE teacher
    ADD COLUMN area_id VARCHAR(64) NULL COMMENT '地区ID(area_jinan)' AFTER subject_id,
    ADD COLUMN grade_id VARCHAR(64) NULL COMMENT '年级ID(grade_level)' AFTER area_id;
