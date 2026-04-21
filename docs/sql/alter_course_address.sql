-- 课程表增加「上课地址」，供小程序预约提交。
-- 在业务库执行；若列已存在请跳过。

ALTER TABLE course
    ADD COLUMN address VARCHAR(512) NULL COMMENT '上课地址' AFTER start_date;
