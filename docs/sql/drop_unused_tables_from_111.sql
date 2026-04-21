-- 基于 /Users/mac/Desktop/111.sql 与当前项目代码引用（排除 ruoyi-card 模块）比对得到
-- 目标库：sunhao
-- 执行前请先完整备份数据库

-- ============================================================================
-- A. 确定可删（当前项目代码未引用，且业务已下线）
-- ============================================================================

-- 会员卡模块相关（已下线）
DROP TABLE IF EXISTS `user_membership`;
DROP TABLE IF EXISTS `card`;

-- 旧商品订单表（当前项目使用的是 `order` 表，不使用 `product_order`）
DROP TABLE IF EXISTS `product_order`;

-- ============================================================================
-- B. 条件可删（仅当你确认不启用 Quartz JDBC 持久化时执行）
--    当前代码使用 sys_job / sys_job_log，不依赖 qrtz_* 运行。
-- ============================================================================

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
DROP TABLE IF EXISTS `qrtz_calendars`;
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
DROP TABLE IF EXISTS `qrtz_locks`;
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
DROP TABLE IF EXISTS `qrtz_triggers`;
DROP TABLE IF EXISTS `qrtz_job_details`;

