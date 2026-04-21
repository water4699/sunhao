-- 下线会员卡模块（管理端）
-- 执行前请先备份数据库

-- 1) 删除会员卡按钮/子菜单/菜单（根据 perms + 组件路径 + 菜单名多条件清理）
DELETE FROM sys_menu
WHERE perms LIKE 'system:card:%'
   OR component = 'my/card/index'
   OR path = 'card'
   OR menu_name LIKE '%会员卡%';

-- 2) 清理角色菜单关系里可能残留的会员卡菜单映射
DELETE rm FROM sys_role_menu rm
LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE m.menu_id IS NULL;
