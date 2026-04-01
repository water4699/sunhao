-- 家长 / 教师角色：与小程序注册 registerRole=parent|teacher 对应（role_key 须一致）
-- 执行前请备份；可重复执行（依赖 NOT EXISTS）

INSERT INTO sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
SELECT '家长', 'parent', 3, '1', 1, 1, '0', '0', 'system', NOW(), '小程序家长注册'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'parent' AND del_flag = '0');

INSERT INTO sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
SELECT '教师', 'teacher', 4, '1', 1, 1, '0', '0', 'system', NOW(), '小程序教师注册'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'teacher' AND del_flag = '0');

-- 菜单权限：与「普通角色 role_id=2」对齐，便于调用业务接口；生产环境可按需收紧
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
