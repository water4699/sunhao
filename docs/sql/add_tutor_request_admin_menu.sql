-- 管理端新增「家教需求管理」菜单（与通知管理同级）
-- 幂等执行：已存在则跳过

SET @parent_id := (
  SELECT menu_id
  FROM sys_menu
  WHERE menu_name = '聚星教育'
  ORDER BY menu_id
  LIMIT 1
);
SET @parent_id := IFNULL(@parent_id, 0);

SET @menu_id := (
  SELECT menu_id
  FROM sys_menu
  WHERE perms = 'system:tutorRequest:list'
     OR component = 'my/tutorRequest/index'
  ORDER BY menu_id
  LIMIT 1
);

INSERT INTO sys_menu (
  menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache,
  menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark
)
SELECT
  '家教需求管理', @parent_id, 99, 'tutorRequest', 'my/tutorRequest/index', '', '', 1, 0,
  'C', '0', '0', 'system:tutorRequest:list', 'list', 'admin', NOW(), '', NULL, '家长需求与老师接单管理'
WHERE @menu_id IS NULL;

SET @menu_id := (
  SELECT menu_id
  FROM sys_menu
  WHERE perms = 'system:tutorRequest:list'
     OR component = 'my/tutorRequest/index'
  ORDER BY menu_id
  LIMIT 1
);

INSERT INTO sys_menu (
  menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache,
  menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark
)
SELECT '需求查询', @menu_id, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:tutorRequest:query', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'system:tutorRequest:query');

INSERT INTO sys_menu (
  menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache,
  menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark
)
SELECT '需求导出', @menu_id, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:tutorRequest:export', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'system:tutorRequest:export');

-- 赋权给管理员角色（role_id=1）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, m.menu_id
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON rm.role_id = 1 AND rm.menu_id = m.menu_id
WHERE m.perms IN ('system:tutorRequest:list', 'system:tutorRequest:query', 'system:tutorRequest:export')
  AND rm.menu_id IS NULL;
