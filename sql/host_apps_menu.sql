-- ----------------------------
-- 主机应用菜单配置
-- 用于在主机管理目录下添加"主机应用"菜单
-- 
-- 执行说明：
-- 1. 在数据库中执行此SQL脚本
-- 2. 菜单ID使用126，在主机管理子菜单序列中（123-125已使用）
-- 3. 按钮权限ID使用1093-1094
-- ----------------------------

-- 添加主机应用菜单（parent_id=7 表示属于主机管理目录）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (126, '主机应用', 7, 4, 'apps', 'business/host/apps/index', '', '', 1, 0, 'C', '0', '0', 'business:host:apps:view', 'app-group', 'admin', NOW(), '', NULL, 'Playwright示例中心，展示元器相关操作示例');

-- 添加主机应用按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1093, '主机应用查看', 126, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'business:host:apps:query', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配主机应用菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 126);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 1093);

-- 为普通用户角色分配主机应用菜单权限（可选，根据需要决定是否执行）
-- INSERT INTO sys_role_menu (role_id, menu_id) VALUES (10, 126);
-- INSERT INTO sys_role_menu (role_id, menu_id) VALUES (10, 1093);
