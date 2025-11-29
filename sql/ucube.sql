
-- MySQL
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 创建数据库（如果不存在）
-- ----------------------------
CREATE DATABASE IF NOT EXISTS `ucube` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `ucube`;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';


-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 100);
INSERT INTO `sys_role_menu` VALUES (1, 101);
INSERT INTO `sys_role_menu` VALUES (1, 102);
INSERT INTO `sys_role_menu` VALUES (1, 103);
INSERT INTO `sys_role_menu` VALUES (1, 104);
INSERT INTO `sys_role_menu` VALUES (1, 105);
INSERT INTO `sys_role_menu` VALUES (1, 106);
INSERT INTO `sys_role_menu` VALUES (1, 107);
INSERT INTO `sys_role_menu` VALUES (1, 108);
INSERT INTO `sys_role_menu` VALUES (1, 109);
INSERT INTO `sys_role_menu` VALUES (1, 110);
INSERT INTO `sys_role_menu` VALUES (1, 111);
INSERT INTO `sys_role_menu` VALUES (1, 112);
INSERT INTO `sys_role_menu` VALUES (1, 113);
INSERT INTO `sys_role_menu` VALUES (1, 114);
INSERT INTO `sys_role_menu` VALUES (1, 115);
INSERT INTO `sys_role_menu` VALUES (1, 116);
INSERT INTO `sys_role_menu` VALUES (1, 117);
INSERT INTO `sys_role_menu` VALUES (1, 500);
INSERT INTO `sys_role_menu` VALUES (1, 501);
INSERT INTO `sys_role_menu` VALUES (1, 1000);
INSERT INTO `sys_role_menu` VALUES (1, 1001);
INSERT INTO `sys_role_menu` VALUES (1, 1002);
INSERT INTO `sys_role_menu` VALUES (1, 1003);
INSERT INTO `sys_role_menu` VALUES (1, 1004);
INSERT INTO `sys_role_menu` VALUES (1, 1005);
INSERT INTO `sys_role_menu` VALUES (1, 1006);
INSERT INTO `sys_role_menu` VALUES (1, 1007);
INSERT INTO `sys_role_menu` VALUES (1, 1008);
INSERT INTO `sys_role_menu` VALUES (1, 1009);
INSERT INTO `sys_role_menu` VALUES (1, 1010);
INSERT INTO `sys_role_menu` VALUES (1, 1011);
INSERT INTO `sys_role_menu` VALUES (1, 1012);
INSERT INTO `sys_role_menu` VALUES (1, 1013);
INSERT INTO `sys_role_menu` VALUES (1, 1014);
INSERT INTO `sys_role_menu` VALUES (1, 1015);
INSERT INTO `sys_role_menu` VALUES (1, 1016);
INSERT INTO `sys_role_menu` VALUES (1, 1017);
INSERT INTO `sys_role_menu` VALUES (1, 1018);
INSERT INTO `sys_role_menu` VALUES (1, 1019);
INSERT INTO `sys_role_menu` VALUES (1, 1020);
INSERT INTO `sys_role_menu` VALUES (1, 1021);
INSERT INTO `sys_role_menu` VALUES (1, 1022);
INSERT INTO `sys_role_menu` VALUES (1, 1023);
INSERT INTO `sys_role_menu` VALUES (1, 1024);
INSERT INTO `sys_role_menu` VALUES (1, 1025);
INSERT INTO `sys_role_menu` VALUES (1, 1026);
INSERT INTO `sys_role_menu` VALUES (1, 1027);
INSERT INTO `sys_role_menu` VALUES (1, 1028);
INSERT INTO `sys_role_menu` VALUES (1, 1029);
INSERT INTO `sys_role_menu` VALUES (1, 1030);
INSERT INTO `sys_role_menu` VALUES (1, 1031);
INSERT INTO `sys_role_menu` VALUES (1, 1032);
INSERT INTO `sys_role_menu` VALUES (1, 1033);
INSERT INTO `sys_role_menu` VALUES (1, 1034);
INSERT INTO `sys_role_menu` VALUES (1, 1035);
INSERT INTO `sys_role_menu` VALUES (1, 1036);
INSERT INTO `sys_role_menu` VALUES (1, 1037);
INSERT INTO `sys_role_menu` VALUES (1, 1038);
INSERT INTO `sys_role_menu` VALUES (1, 1039);
INSERT INTO `sys_role_menu` VALUES (1, 1040);
INSERT INTO `sys_role_menu` VALUES (1, 1041);
INSERT INTO `sys_role_menu` VALUES (1, 1042);
INSERT INTO `sys_role_menu` VALUES (1, 1043);
INSERT INTO `sys_role_menu` VALUES (1, 1044);
INSERT INTO `sys_role_menu` VALUES (1, 1045);
INSERT INTO `sys_role_menu` VALUES (1, 1046);
INSERT INTO `sys_role_menu` VALUES (1, 1047);
INSERT INTO `sys_role_menu` VALUES (1, 1048);
INSERT INTO `sys_role_menu` VALUES (1, 1049);
INSERT INTO `sys_role_menu` VALUES (1, 1050);
INSERT INTO `sys_role_menu` VALUES (1, 1051);
INSERT INTO `sys_role_menu` VALUES (1, 1052);
INSERT INTO `sys_role_menu` VALUES (1, 1053);
INSERT INTO `sys_role_menu` VALUES (1, 1054);
INSERT INTO `sys_role_menu` VALUES (1, 1055);
INSERT INTO `sys_role_menu` VALUES (1, 1056);
INSERT INTO `sys_role_menu` VALUES (1, 1057);
INSERT INTO `sys_role_menu` VALUES (1, 1058);
INSERT INTO `sys_role_menu` VALUES (1, 1059);
INSERT INTO `sys_role_menu` VALUES (1, 1060);
INSERT INTO `sys_role_menu` VALUES (1, 2001);
INSERT INTO `sys_role_menu` VALUES (1, 2050);
INSERT INTO `sys_role_menu` VALUES (1, 2051);
INSERT INTO `sys_role_menu` VALUES (1, 2070);
INSERT INTO `sys_role_menu` VALUES (1, 2071);
INSERT INTO `sys_role_menu` VALUES (1, 2072);
INSERT INTO `sys_role_menu` VALUES (1, 2073);
INSERT INTO `sys_role_menu` VALUES (1, 2074);
INSERT  INTO `sys_role_menu` VALUES (1, 2100);
INSERT  INTO `sys_role_menu` VALUES (1, 2101);
INSERT INTO `sys_role_menu` VALUES (1, 2102);
INSERT  INTO `sys_role_menu` VALUES (1, 2103);
INSERT  INTO `sys_role_menu` VALUES (1, 2104);
INSERT  INTO `sys_role_menu` VALUES (1, 2105);
INSERT  INTO `sys_role_menu` VALUES (1, 2106);
INSERT  INTO `sys_role_menu` VALUES (1, 2107);
INSERT  INTO `sys_role_menu` VALUES (1, 2108);
INSERT INTO `sys_role_menu` VALUES (1, 2109);
INSERT INTO `sys_role_menu` VALUES (1, 2110);
INSERT  INTO `sys_role_menu` VALUES (1, 2111);
INSERT  INTO `sys_role_menu` VALUES (1, 2112);
INSERT  INTO `sys_role_menu` VALUES (1, 2113);
INSERT  INTO `sys_role_menu` VALUES (1, 2114);
INSERT  INTO `sys_role_menu` VALUES (1, 2115);
INSERT  INTO `sys_role_menu` VALUES (1, 2116);
INSERT  INTO `sys_role_menu` VALUES (1, 2117);
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);
INSERT INTO `sys_role_menu` VALUES (2, 2000);
INSERT INTO `sys_role_menu` VALUES (2, 2001);
INSERT INTO `sys_role_menu` VALUES (2, 2007);
INSERT INTO `sys_role_menu` VALUES (2, 2017);
INSERT INTO `sys_role_menu` VALUES (2, 2021);
INSERT INTO `sys_role_menu` VALUES (2, 2022);
INSERT INTO `sys_role_menu` VALUES (2, 2023);
INSERT INTO `sys_role_menu` VALUES (2, 2024);
INSERT INTO `sys_role_menu` VALUES (2, 2025);
INSERT INTO `sys_role_menu` VALUES (2, 2026);
INSERT INTO `sys_role_menu` VALUES (2, 2027);
INSERT INTO `sys_role_menu` VALUES (2, 2028);
INSERT INTO `sys_role_menu` VALUES (2, 2029);
INSERT INTO `sys_role_menu` VALUES (2, 2030);
INSERT INTO `sys_role_menu` VALUES (2, 2031);
INSERT INTO `sys_role_menu` VALUES (2, 2032);
INSERT INTO `sys_role_menu` VALUES (2, 2033);
INSERT INTO `sys_role_menu` VALUES (2, 2034);
INSERT INTO `sys_role_menu` VALUES (2, 2035);
INSERT INTO `sys_role_menu` VALUES (2, 2036);
INSERT INTO `sys_role_menu` VALUES (2, 2037);
INSERT INTO `sys_role_menu` VALUES (2, 2038);
INSERT INTO `sys_role_menu` VALUES (2, 2046);
INSERT INTO `sys_role_menu` VALUES (2, 2047);
INSERT INTO `sys_role_menu` VALUES (2, 2048);
INSERT INTO `sys_role_menu` VALUES (2, 2050);
INSERT INTO `sys_role_menu` VALUES (2, 2051);
INSERT INTO `sys_role_menu` VALUES (2, 2070);
INSERT INTO `sys_role_menu` VALUES (2, 2071);
INSERT INTO `sys_role_menu` VALUES (2, 2072);
INSERT INTO `sys_role_menu` VALUES (2, 2073);
INSERT INTO `sys_role_menu` VALUES (2, 2074);
INSERT  INTO `sys_role_menu` VALUES (2, 2100);
INSERT  INTO `sys_role_menu` VALUES (2, 2101);
INSERT INTO `sys_role_menu` VALUES (2, 2102);
INSERT  INTO `sys_role_menu` VALUES (2, 2103);
INSERT  INTO `sys_role_menu` VALUES (2, 2104);
INSERT  INTO `sys_role_menu` VALUES (2, 2105);
INSERT  INTO `sys_role_menu` VALUES (2, 2106);
INSERT  INTO `sys_role_menu` VALUES (2, 2107);
INSERT  INTO `sys_role_menu` VALUES (2, 2108);
INSERT INTO `sys_role_menu` VALUES (2, 2109);
INSERT INTO `sys_role_menu` VALUES (2, 2110);
INSERT  INTO `sys_role_menu` VALUES (2, 2111);
INSERT  INTO `sys_role_menu` VALUES (2, 2112);
INSERT  INTO `sys_role_menu` VALUES (2, 2113);
INSERT  INTO `sys_role_menu` VALUES (2, 2114);
INSERT  INTO `sys_role_menu` VALUES (2, 2115);
INSERT  INTO `sys_role_menu` VALUES (2, 2116);
INSERT  INTO `sys_role_menu` VALUES (2, 2117);
INSERT INTO `sys_role_menu` VALUES (101, 2000);
INSERT INTO `sys_role_menu` VALUES (101, 2017);
INSERT INTO `sys_role_menu` VALUES (101, 2021);
INSERT INTO `sys_role_menu` VALUES (101, 2028);
INSERT INTO `sys_role_menu` VALUES (101, 2029);
INSERT INTO `sys_role_menu` VALUES (101, 2030);
INSERT INTO `sys_role_menu` VALUES (101, 2031);
INSERT INTO `sys_role_menu` VALUES (101, 2032);
INSERT INTO `sys_role_menu` VALUES (101, 2033);
INSERT INTO `sys_role_menu` VALUES (101, 2034);
INSERT INTO `sys_role_menu` VALUES (101, 2035);
INSERT INTO `sys_role_menu` VALUES (101, 2036);
INSERT INTO `sys_role_menu` VALUES (101, 2037);
INSERT INTO `sys_role_menu` VALUES (101, 2038);
INSERT INTO `sys_role_menu` VALUES (101, 2046);
INSERT INTO `sys_role_menu` VALUES (101, 2047);
INSERT INTO `sys_role_menu` VALUES (101, 2048);
--


-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (101, 2021);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (101, 2028);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2029);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2030);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2031);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2032);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2033);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2034);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2035);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2036);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2037);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2038);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2046);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2047);
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)VALUES (101, 2048);
-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) DEFAULT '' COMMENT '路由名称',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2022 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2024-08-08 14:05:51', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2024-08-08 14:05:51', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2024-08-08 14:05:51', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2024-08-08 14:05:51', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2024-08-08 14:05:51', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2024-08-08 14:05:51', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2024-08-08 14:05:51', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2024-08-08 14:05:51', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2024-08-08 14:05:51', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2024-08-08 14:05:51', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2024-08-08 14:05:51', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (108, 'old日志管理', 1, 9, 'log', NULL, '', '', 1, 0, 'M', '1', '0', '', 'log', 'admin', '2024-08-08 14:05:51', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2024-08-08 14:05:51', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2024-08-08 14:05:51', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '1', 'monitor:druid:list', 'druid', 'admin', '2024-08-08 14:05:51', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2024-08-08 14:05:51', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2024-08-08 14:05:52', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2024-08-08 14:05:52', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2024-08-08 14:05:52', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2024-08-08 14:05:52', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2024-08-08 14:05:52', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2024-08-08 14:05:52', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2024-08-08 14:05:52', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1000, '用户查询', 100, 1, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1001, '用户新增', 100, 2, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1002, '用户修改', 100, 3, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1003, '用户删除', 100, 4, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1004, '用户导出', 100, 5, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1005, '用户导入', 100, 6, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1006, '重置密码', 100, 7, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1007, '角色查询', 101, 1, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1008, '角色新增', 101, 2, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1009, '角色修改', 101, 3, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1010, '角色删除', 101, 4, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1011, '角色导出', 101, 5, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1012, '菜单查询', 102, 1, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1013, '菜单新增', 102, 2, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1014, '菜单修改', 102, 3, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1015, '菜单删除', 102, 4, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1016, '部门查询', 103, 1, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1017, '部门新增', 103, 2, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1018, '部门修改', 103, 3, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1019, '部门删除', 103, 4, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1020, '岗位查询', 104, 1, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1021, '岗位新增', 104, 2, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1022, '岗位修改', 104, 3, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1023, '岗位删除', 104, 4, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1024, '岗位导出', 104, 5, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1025, '字典查询', 105, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1026, '字典新增', 105, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1027, '字典修改', 105, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1028, '字典删除', 105, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1029, '字典导出', 105, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1030, '参数查询', 106, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1031, '参数新增', 106, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1032, '参数修改', 106, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1033, '参数删除', 106, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1034, '参数导出', 106, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1035, '公告查询', 107, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1036, '公告新增', 107, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1037, '公告修改', 107, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1038, '公告删除', 107, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1039, '操作查询', 500, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1040, '操作删除', 500, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1041, '日志导出', 500, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1042, '登录查询', 501, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1043, '登录删除', 501, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1044, '日志导出', 501, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1045, '账户解锁', 501, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1046, '在线查询', 109, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1047, '批量强退', 109, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1048, '单条强退', 109, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1049, '任务查询', 110, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1050, '任务新增', 110, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1051, '任务修改', 110, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1052, '任务删除', 110, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1053, '状态修改', 110, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1054, '任务导出', 110, 6, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1055, '生成查询', 116, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1056, '生成修改', 116, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1057, '生成删除', 116, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1058, '导入代码', 116, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1059, '预览代码', 116, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1060, '生成代码', 116, 6, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2000, '内容管理', 0, 0, 'wechat', NULL, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', '2024-08-08 14:14:32', 'YangHangHang', '2024-11-12 10:59:05', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2001, '用户列表', 2051, 1, 'company', 'wechat/company/index', '', '', 1, 0, 'C', '0', '0', 'wechat:company:list', 'tree-table', 'admin', '2024-08-08 14:17:40', 'admin', '2025-05-13 09:48:53', '');
-- INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2007, '积分设置', 2001, 1, '', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:points:edit', '#', 'DuHongChao-YuanTouShe', '2024-09-13 14:35:36', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2017, 'AI精选一站通', 2000, 0, 'chrome', 'wechat/chrome/index', '', '', 1, 0, 'C', '0', '0', 'wechat:chrome:list', 'international', 'YangHangHang', '2024-11-22 09:59:55', 'admin', '2025-10-29 12:00:00', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2021, '稿库', 2000, 1, 'drafts', 'wechat/drafts/index', '', '', 1, 0, 'C', '0', '0', '', 'log', 'o3lds60Lfe6_MaGyB-COxgGcItnM', '2025-01-06 17:23:00', 'admin', '2025-06-06 08:46:16', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2022, '用户日志', 108, 1, 'userLog', 'monitor/userLog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:userLog:list', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '用户日志菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2023, '用户日志查询', 2022, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:userLog:query', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2024, '用户日志新增', 2022, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:userLog:add', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2025, '用户日志修改', 2022, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:userLog:edit', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2026, '用户日志删除', 2022, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:userLog:remove', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2027, '用户日志导出', 2022, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:userLog:export', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2028, '平台提示词配置', 2050, 1, 'callWord', 'wechat/callWord/index', '', '', 1, 0, 'C', '0', '0', 'wechat:callWord:list', 'guide', 'admin', '2025-11-28 09:39:22', '', NULL, '平台提示词配置菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2029, '平台提示词配置新增', 2028, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:callWord:add', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2030, '平台提示词配置修改', 2028, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:callWord:edit', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2031, '平台提示词配置删除', 2028, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:callWord:remove', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2032, '平台提示词配置导出', 2028, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:callWord:export', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2033, '评分模板配置', 2050, 1, 'prompt', 'wechat/prompt/index', '', '', 1, 0, 'C', '0', '0', 'wechat:prompt:list', 'star', 'admin', '2025-11-28 09:39:22', '', NULL, '评分模板配置菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2034, '评分模板配置查询', 2033, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:prompt:query', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2035, '评分模板配置新增', 2033, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:prompt:add', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2036, '评分模板配置修改', 2033, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:prompt:edit', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2037, '评分模板配置删除', 2033, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:prompt:remove', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2038, '评分模板配置导出', 2033, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:prompt:export', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2046, '内容自动化（内测）', 2000, 0, 'chrome2', 'wechat/chrome2/index', '', '', 1, 0, 'C', '1', '0', 'wechat:chrome:list', 'redis-list', 'admin', '2025-10-21 13:36:21', 'admin', '2025-10-29 12:00:00', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2047, '思路模板', 2000, 3, 'idea', 'wechat/idea/index', '', '', 1, 0, 'C', '1', '0', '', 'clipboard', 'admin', '2025-10-24 15:59:41', 'admin', '2025-10-27 17:35:02', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2048, '文章模板', 2000, 3, 'art', 'wechat/art/index', '', '', 1, 0, 'C', '1', '0', '', 'documentation', 'admin', '2025-10-24 16:00:31', 'admin', '2025-10-27 17:34:54', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2050, '提示词模板', 0, 1, 'prompt-template', NULL, '', '', 1, 0, 'M', '0', '0', '', 'skill', 'admin', '2025-10-28 00:00:00', '', NULL, '提示词模板管理目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2051, '粉丝管理', 0, 2, 'fans-manage', NULL, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', '2025-10-28 00:00:00', 'admin', '2025-11-28 01:26:03', '粉丝管理目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2070, '日志管理', 1, 10, 'unifiedLog', 'monitor/unifiedLog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:unifiedLog:list', 'documentation', 'admin', '2025-11-28 09:39:22', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2071, '日志查询', 2070, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:unifiedLog:query', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2072, '日志删除', 2070, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:unifiedLog:remove', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2073, '日志导出', 2070, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:unifiedLog:export', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2074, '日志详细', 2070, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'monitor:unifiedLog:detail', '#', 'admin', '2025-11-28 09:39:22', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2080, 'AI管理器', 1, 11, 'aiagent', 'system/aiagent/index', '', '', 1, 0, 'C', '0', '0', 'system:aiagent:list', 'link', 'admin', '2025-11-24 07:50:19', '', NULL, 'AI智能体管理菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2081, 'AI智能体查询', 2080, 1, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:aiagent:query', '#', 'admin', '2025-11-28 09:40:08', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2082, 'AI智能体列表', 2080, 2, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:aiagent:list', '#', 'admin', '2025-11-28 09:40:08', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2083, 'AI智能体新增', 2080, 3, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:aiagent:add', '#', 'admin', '2025-11-28 09:40:08', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2084, 'AI智能体修改', 2080, 4, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:aiagent:edit', '#', 'admin', '2025-11-28 09:40:08', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2085, 'AI智能体删除', 2080, 5, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:aiagent:remove', '#', 'admin', '2025-11-28 09:40:08', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2086, 'AI智能体导出', 2080, 6, '', NULL, '', '', 1, 0, 'F', '0', '0', 'system:aiagent:export', '#', 'admin', '2025-11-28 09:40:08', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2100, '我的模板箱', 2050, 10, 'templateBox', 'wechat/templateBox/index', '', '', 1, 0, 'C', '0', '0', 'wechat:templateBox:list', 'box', 'admin', '2025-11-28 09:39:22', '', NULL, '我的模板箱菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2101, '模板市场', 2050, 11, 'templateMarket', 'wechat/templateMarket/index', '', '', 1, 0, 'C', '0', '0', 'wechat:templateMarket:list', 'shopping', 'admin', '2025-11-28 09:39:23', '', NULL, '模板市场菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2102, '模板工作坊', 2050, 12, 'templateWorkshop', 'wechat/templateWorkshop/index', '', '', 1, 0, 'C', '0', '0', 'wechat:templateWorkshop:list', 'edit', 'admin', '2025-11-28 09:39:23', '', NULL, '模板工作坊菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2103, '我的模板箱查询', 2100, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:templateBox:query', '#', 'admin', '2025-11-28 09:39:23', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2104, '模板市场查询', 2101, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:templateMarket:query', '#', 'admin', '2025-11-28 09:39:23', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2105, '模板购买', 2101, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:templateMarket:purchase', '#', 'admin', '2025-11-28 09:39:23', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2106, '模板工作坊查询', 2102, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:templateWorkshop:query', '#', 'admin', '2025-11-28 09:39:23', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2107, '模板工作坊新增', 2102, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:templateWorkshop:add', '#', 'admin', '2025-11-28 09:39:23', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2108, '模板工作坊修改', 2102, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:templateWorkshop:edit', '#', 'admin', '2025-11-28 09:39:23', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2109, '模板工作坊删除', 2102, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'wechat:templateWorkshop:remove', '#', 'admin', '2025-11-28 09:39:23', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2110, '积分管理', 0, 10, 'points', NULL, '', '', 1, 0, 'M', '0', '0', '', 'star', 'admin', '2025-11-27 06:49:52', '', NULL, '积分管理目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2111, '我的积分总览', 2110, 1, 'overview', 'points/overview/index', '', '', 1, 0, 'C', '0', '0', '', 'guide', 'admin', '2025-11-27 06:52:10', '', NULL, '我的积分总览页面');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2112, '积分规则配置', 2051, 5, 'points/rule', 'points/rule/index', '', '', 1, 0, 'C', '0', '0', '', '#', 'admin', '2025-11-28 00:43:16', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2113, '积分规则查询', 2112, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-11-28 00:46:51', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2114, '积分规则新增', 2112, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-11-28 00:46:51', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2115, '积分规则修改', 2112, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-11-28 00:46:51', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2116, '积分规则删除', 2112, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-11-28 00:46:51', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2117, '积分规则导出', 2112, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-11-28 00:46:51', '', NULL, '');
COMMIT;-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';


-- Records of sys_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '性别男');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '性别女');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '通知');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '公告');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:58', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-08-08 14:05:58', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:58', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, 1, '科技传媒', '科技传媒', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (101, 2, '大消费', '大消费', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:24', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (102, 3, '出海专题', '出海专题', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (103, 4, '健康医疗', '健康医疗', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:47', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (104, 5, '金融地产', '金融地产', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (105, 6, '能源矿产', '能源矿产', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (106, 7, '工业制造', '工业制造', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:19', 'YangHangHang', '2024-09-24 13:51:32', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (107, 8, '交通物流', '交通物流', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (108, 9, '公共服务', '公共服务', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:42', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (109, 10, '农林牧渔', '农林牧渔', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (110, 11, '综合其他', '综合其他', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:52:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (111, 1, '新鲜出炉', '新鲜出炉', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (112, 2, '深度研究', '深度研究', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (113, 3, '热门', '热门', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:43', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (114, 4, '英文', '英文', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (115, 5, '行业研究', '行业研究', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (116, 6, '公司研究', '公司研究', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (117, 7, '产业政策', '产业政策', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (118, 8, 'AI', 'AI', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (119, 0, '首次登录', '200', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:52:05', 'admin', '2025-05-29 09:07:07', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (120, 1, '行业订阅', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:53:01', 'YangHangHang', '2024-09-27 13:56:50', '单位：每个行业');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (121, 2, '首次完善资料', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:53:47', 'YangHangHang', '2024-09-27 14:57:23', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (122, 3, '每日首次登录', '2', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:54:53', 'DuRuiNing', '2024-12-09 13:50:39', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (123, 4, '每日首次分享', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:55:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (124, 5, '下载干货', '-5', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:56:31', 'YangHangHang', '2024-09-27 13:58:10', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (125, 6, '每日评论', '2', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-11-18 13:45:58', 'DuRuiNing', '2024-12-09 13:52:28', '每天上限10分');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (126, 7, '插件首次登录', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'DuRuiNing', '2024-12-09 13:51:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (127, 8, '每日优立方登录', '50', 'sys_point_rule', NULL, 'default', 'N', '0', 'DuRuiNing', '2024-12-09 13:52:01', 'admin', '2025-05-28 23:18:56', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (128, 1, '元透社AI内容创作学习卡', '99', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-25 09:43:19', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-25 09:43:38', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (129, 9, '邀请新用户', '10', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:43:03', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (130, 10, '积分99充值卡', '99', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:43:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (131, 11, '解绑能力使用', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:44:35', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:47:08', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (132, 12, '收录公众号', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:45:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (133, 12, '工作站自动化使用', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:47:30', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (134, 13, '每日打卡', '1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:47:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (135, 14, '学习课程视频', '1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:49:40', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (136, 15, '公众号修改', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:54:58', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (137, 16, '社群分享内容', '1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:59:48', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (138, 9, '12D学习卡', '12', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 15:00:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (139, 17, '调用公众号智能体API', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-21 13:57:24', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-21 13:57:38', NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (140, 18, '首次绑定元宝账号', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:54:46', 'admin', '2025-03-27 15:08:57', '');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (141, 19, '模板配置', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:55:02', 'admin', '2025-03-27 15:16:53', '附加提示词配置');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (142, 20, '记忆修改', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:55:19', 'admin', '2025-03-27 15:17:00', '记忆描述配置');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (143, 21, '使用F8S', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:55:34', 'admin', '2025-03-27 15:09:21', '');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (144, 22, 'AI评分', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'admin', '2025-05-15 09:23:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (145, 22, 'AI排版', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'admin', '2025-05-15 09:23:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (146, 22, '模板上架奖励', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'admin', '2025-05-15 09:23:28', '', NULL, '{"limitType":"DAILY","limitValue":3}');
COMMIT;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint(20) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
BEGIN;
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1, 1, 'id', '主键id', 'varchar(36)', 'String', 'id', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (2, 1, 'corp_id', '企业id', 'varchar(45)', 'String', 'corpId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (3, 1, 'permanent_code', '企业永久授权码', 'varchar(512)', 'String', 'permanentCode', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 3, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (4, 1, 'corp_name', '企业名称', 'varchar(50)', 'String', 'corpName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (5, 1, 'corp_full_name', '企业全称', 'varchar(100)', 'String', 'corpFullName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 5, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (6, 1, 'subject_type', '企业类型', 'varchar(512)', 'String', 'subjectType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 6, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (7, 1, 'verified_end_time', '企业认证到期时间', 'varchar(512)', 'String', 'verifiedEndTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 7, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (8, 1, 'suite_id', '应用唯一ID', 'varchar(200)', 'String', 'suiteId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (9, 1, 'agent_id', '授权应用id', 'varchar(255)', 'String', 'agentId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (10, 1, 'status', '账户状态，-1为删除，禁用为0 启用为1', 'int(11)', 'Long', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 10, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (11, 1, 'addtime', '创建时间', 'datetime', 'Date', 'addtime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 11, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (12, 1, 'modtime', '修改时间', 'datetime', 'Date', 'modtime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 12, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (13, 1, 'rectime', '变动时间', 'datetime', 'Date', 'rectime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 13, 'admin', '2024-08-08 14:11:52', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (14, 2, 'user_id', '用户ID', 'bigint(20)', 'Long', 'userId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (15, 2, 'open_id', '微信ID', 'varchar(36)', 'String', 'openId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (16, 2, 'union_id', '开放平台ID', 'varchar(36)', 'String', 'unionId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (17, 2, 'qw_id', '企微ID', 'varchar(66)', 'String', 'qwId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (18, 2, 'dept_id', '部门ID', 'bigint(20)', 'Long', 'deptId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (19, 2, 'user_name', '用户账号', 'varchar(255)', 'String', 'userName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 6, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (20, 2, 'nick_name', '用户昵称', 'varchar(30)', 'String', 'nickName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 7, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (21, 2, 'user_type', '用户类型（00系统用户）', 'varchar(2)', 'String', 'userType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 8, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (22, 2, 'email', '用户邮箱', 'varchar(50)', 'String', 'email', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (23, 2, 'phonenumber', '手机号码', 'varchar(11)', 'String', 'phonenumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (24, 2, 'sex', '用户性别（0男 1女 2未知）', 'char(1)', 'String', 'sex', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 11, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (25, 2, 'avatar', '头像地址', 'varchar(255)', 'String', 'avatar', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (26, 2, 'password', '密码', 'varchar(100)', 'String', 'password', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (27, 2, 'status', '帐号状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 14, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (28, 2, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 15, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (29, 2, 'login_ip', '最后登录IP', 'varchar(128)', 'String', 'loginIp', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 16, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (30, 2, 'login_date', '最后登录时间', 'datetime', 'Date', 'loginDate', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 17, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (31, 2, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 18, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (32, 2, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 19, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (33, 2, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 20, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (34, 2, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 21, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (35, 2, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 22, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (36, 2, 'corp_id', '企业ID', 'varchar(36)', 'String', 'corpId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 23, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (37, 2, 'points', '积分余额', 'int(5)', 'Integer', 'points', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 24, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (38, 3, 'id', '攻略表id', 'varchar(36)', 'String', 'id', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'keweiren', '2024-11-12 11:12:47', '', '2024-11-12 15:25:26');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (39, 3, 'strategy_title', '攻略标题', 'varchar(255)', 'String', 'strategyTitle', '0', '0', '0', '1', '1', '1', '0', 'LIKE', 'input', '', 2, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (41, 3, 'strategy_content', '内容', 'varchar(2048)', 'String', 'strategyContent', '0', '0', '0', '1', '1', '1', '0', 'LIKE', 'editor', '', 3, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (42, 3, 'author', '作者', 'varchar(36)', 'String', 'author', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (43, 3, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, '1', NULL, 'EQ', 'datetime', '', 5, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (48, 3, 'comment_id', '评论id', 'varchar(36)', 'String', 'commentId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 10, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (49, 3, 'view_id', '浏览id', 'int(11)', 'Long', 'viewId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 6, '', '2024-11-12 14:04:52', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (50, 3, 'like_id', '点赞id', 'int(11)', 'Long', 'likeId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 7, '', '2024-11-12 14:04:53', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (51, 3, 'collections_id', '收藏id', 'int(11)', 'Long', 'collectionsId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 8, '', '2024-11-12 14:04:53', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (53, 3, 'tag_id', '标签id', 'varchar(255)', 'String', 'tagId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 11, '', '2024-11-12 14:04:53', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (58, 3, 'pic_url', '攻略图', 'varchar(255)', 'String', 'picUrl', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'imageUpload', '', 9, '', '2024-11-12 15:24:29', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (59, 5, 'id', '主建', 'varchar(36)', 'String', 'id', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (60, 5, 'prompt', '提示词', 'varchar(255)', 'String', 'prompt', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (61, 5, 'answer', '答案', 'varchar(1000)', 'String', 'answer', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 3, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (62, 5, 'name', 'ai名称', 'varchar(255)', 'String', 'name', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (63, 5, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, '行业分类', 'sys_industry', '0', 'YangHangHang', '2024-09-24 13:48:31', '', NULL, NULL);
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (101, '用户标签', 'sys_user_tag', '0', 'YangHangHang', '2024-09-24 13:58:45', '', NULL, NULL);
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (102, '积分规则', 'sys_point_rule', '0', 'YangHangHang', '2024-09-27 13:51:25', '', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (1, 1);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (2, 1);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (3, 1);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (4, 1);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (5, 1);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (6, 1);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (22, 2);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (47, 2);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (56, 1);
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (173, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (22, 2);
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (22, 101);
COMMIT;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post_del
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post_del`;
CREATE TABLE `sys_user_post_del` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post_del
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post_del` (`user_id`, `post_id`) VALUES (1, 2);
INSERT INTO `sys_user_post_del` (`user_id`, `post_id`) VALUES (2, 2);
INSERT INTO `sys_user_post_del` (`user_id`, `post_id`) VALUES (3, 2);
INSERT INTO `sys_user_post_del` (`user_id`, `post_id`) VALUES (4, 2);
INSERT INTO `sys_user_post_del` (`user_id`, `post_id`) VALUES (5, 2);
INSERT INTO `sys_user_post_del` (`user_id`, `post_id`) VALUES (6, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role_del
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_del`;
CREATE TABLE `sys_user_role_del` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role_del
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role_del` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `sys_user_role_del` (`user_id`, `role_id`) VALUES (2, 1);
INSERT INTO `sys_user_role_del` (`user_id`, `role_id`) VALUES (3, 1);
INSERT INTO `sys_user_role_del` (`user_id`, `role_id`) VALUES (4, 1);
INSERT INTO `sys_user_role_del` (`user_id`, `role_id`) VALUES (5, 1);
INSERT INTO `sys_user_role_del` (`user_id`, `role_id`) VALUES (6, 1);
COMMIT;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------
BEGIN;
INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `sub_table_name`, `sub_table_fk_name`, `class_name`, `tpl_category`, `tpl_web_type`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `gen_type`, `gen_path`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 'qywx_third_company', '企业微信三方应用授权公司', NULL, NULL, 'QywxThirdCompany', 'crud', '', 'com.cube.system', 'system', 'company', '企业微信三方应用授权公司', 'ruoyi', '0', '/', NULL, 'admin', '2024-08-08 14:11:52', '', NULL, NULL);
INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `sub_table_name`, `sub_table_fk_name`, `class_name`, `tpl_category`, `tpl_web_type`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `gen_type`, `gen_path`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, 'sys_user', '用户信息表', NULL, NULL, 'SysUser', 'crud', '', 'com.cube.system', 'system', 'user', '用户信息', 'ruoyi', '0', '/', NULL, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL, NULL);
INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `sub_table_name`, `sub_table_fk_name`, `class_name`, `tpl_category`, `tpl_web_type`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `gen_type`, `gen_path`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, 'wc_strategy', '攻略表', '', '', 'WcStrategy', 'crud', 'element-ui', 'com.cube.wechat.selfapp.app', 'admin', 'strategy', '攻略记录', 'keke', '0', '/', '{}', 'keweiren', '2024-11-12 11:12:47', '', '2024-11-12 15:25:26', NULL);
INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `sub_table_name`, `sub_table_fk_name`, `class_name`, `tpl_category`, `tpl_web_type`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `gen_type`, `gen_path`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, 'wc_chrome_data', '', NULL, NULL, 'WcChromeData', 'crud', '', 'com.cube.system', 'system', 'data', NULL, 'ruoyi', '0', '/', NULL, 'YangHangHang', '2024-11-22 10:01:23', '', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for geth_user_rel
-- ----------------------------
DROP TABLE IF EXISTS `geth_user_rel`;
CREATE TABLE `geth_user_rel` (
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `privateKey` varchar(255) DEFAULT NULL COMMENT '用户私钥',
  `publicKey` varchar(255) DEFAULT NULL COMMENT '公钥',
  `address` varchar(255) DEFAULT NULL COMMENT '以太坊账户'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='以太坊账户';

-- ----------------------------
-- Records of geth_user_rel
-- ----------------------------
BEGIN;
INSERT INTO `geth_user_rel` (`user_id`, `privateKey`, `publicKey`, `address`) VALUES (22, 'b8bde749d1ba80d1663cf83970327f83702015a19a07fd70dc63b27c7e8c271f', '36833e719e9d5b96ceced08ce0d1157623d112735b3f95cc468977e7e505d78eb4f44479e92b9943a375d5589874303c252009214d6539d72639a4695cf80217', '0x8a23bde7103320b2f5d9eee6c90faa0e8e1ba925');
COMMIT;

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`),
  KEY `idx_oper_time` (`oper_time` DESC),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_status` (`status`),
  KEY `idx_oper_name` (`oper_name`),
  KEY `idx_oper_ip` (`oper_ip`),
  KEY `idx_status_time` (`status`, `oper_time` DESC),
  KEY `idx_business_status` (`business_type`, `status`, `oper_time` DESC),
  KEY `idx_oper_name_time` (`oper_name`, `oper_time` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=10012 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`, `cost_time`) VALUES (10008, '个人信息', 2, 'com.cube.web.controller.system.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/system/user/profile', '127.0.0.1', '内网IP', '{\"admin\":false,\"corpId\":\"ylf100002\",\"email\":\"\",\"nickName\":\"AspireLife\",\"params\":{},\"phonenumber\":\"16637172129\",\"sex\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-05 15:36:16', 220);
INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`, `cost_time`) VALUES (10009, '菜单管理', 2, 'com.cube.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/system/menu', '125.42.97.158', 'XX XX', '{\"children\":[],\"component\":\"wechat/drafts/index\",\"createTime\":\"2025-01-06 17:23:00\",\"icon\":\"log\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2021,\"menuName\":\"稿库\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"drafts\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-06 08:46:16', 27);
INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`, `cost_time`) VALUES (10010, '菜单管理', 2, 'com.cube.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/system/menu', '125.42.97.158', 'XX XX', '{\"children\":[],\"component\":\"wechat/research/index\",\"createTime\":\"2024-09-13 08:42:01\",\"icon\":\"education\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2004,\"menuName\":\"干货管理\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2000,\"path\":\"research\",\"perms\":\"wechat:research:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-06 08:46:27', 18);
INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`, `cost_time`) VALUES (10011, '企微管理-查询用户积分', 0, 'com.cube.point.controller.PointsSystem.getUserPointsRecord()', 'POST', 1, 'admin', NULL, '/points/getUserPointsRecord', '125.42.97.158', 'XX XX', '{\"limit\":10,\"page\":1,\"userId\":\"22\"}', '{\"code\":200,\"data\":{\"endRow\":10,\"hasNextPage\":true,\"hasPreviousPage\":false,\"isFirstPage\":true,\"isLastPage\":false,\"list\":[{\"change_amount\":-1,\"create_time\":\"2025-6-06 11:16:05\",\"balance_after\":9822,\"nick_name\":\"AspireLife\",\"change_type\":\"AI评分\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-6-06 11:05:52\",\"balance_after\":9823,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-6-06 10:30:47\",\"balance_after\":9824,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-6-06 10:10:13\",\"balance_after\":9825,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-6-06 10:05:45\",\"balance_after\":9826,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-6-06 09:58:53\",\"balance_after\":9827,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-6-06 09:46:57\",\"balance_after\":9828,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-5-30 17:40:49\",\"balance_after\":9829,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-5-30 17:30:05\",\"balance_after\":9830,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"},{\"change_amount\":-1,\"create_time\":\"2025-5-30 10:54:05\",\"balance_after\":9831,\"nick_name\":\"AspireLife\",\"change_type\":\"使用F8S\",\"create_name\":\"AspireLife\"}],\"navigateFirstPage\":1,\"navigateLastPage\":8,\"navigatePages\":8,\"navigatepageNums\":[1,2,3,4,5,6,7,8],\"nextPage\":2,\"pageNum\":1,\"pageSize\":10,\"pages\":67,\"prePage\":0,\"size\":10,\"startRow\":1,\"total\":667},\"messages\":\"ok\"}', 0, NULL, '2025-06-06 11:19:44', 27);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                             `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
                             `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
                             `role_sort` int(4) NOT NULL COMMENT '显示顺序',
                             `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
                             `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
                             `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
                             `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                             PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2024-08-08 14:05:51', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2024-08-08 14:05:51', 'admin', '2025-10-28 15:09:28', '普通角色');
INSERT INTO `sys_role` VALUES (100, '内容角色', 'wechat', 3, '1', 1, 1, '0', '2', 'admin', '2024-08-09 14:15:41', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-02-20 17:07:30', NULL);
INSERT INTO `sys_role` VALUES (101, '优立方主机岗', 'plugin', 3, '1', 1, 1, '0', '0', 'YangHangHang', '2024-11-29 08:58:00', 'admin', '2025-10-28 15:09:36', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `open_id` varchar(36) DEFAULT NULL COMMENT '微信ID',
  `union_id` varchar(36) DEFAULT NULL COMMENT '开放平台ID',
  `qw_id` varchar(66) DEFAULT NULL COMMENT '企微ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(255) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `corp_id` varchar(36) DEFAULT NULL COMMENT '企业ID',
  `points` int(5) DEFAULT NULL COMMENT '积分余额',
  `agent_id` varchar(255) DEFAULT NULL COMMENT '智能体ID',
  `agent_token` varchar(255) DEFAULT NULL COMMENT '智能体token',
  `space_id` varchar(36) DEFAULT NULL COMMENT '空间 ID',
  `space_name` varchar(255) DEFAULT NULL COMMENT '空间名称',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
-- admin用户基础数据，企业ID设置为office01
INSERT INTO `sys_user` VALUES (22, NULL, NULL, NULL, 1, 'admin', 'admin', '00', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), 'admin', sysdate(), NULL, 'office01', 9820, '', '', '', '');

-- 平台公共账户（userid=0），用于存储积分抽成
-- 注意：由于user_id是AUTO_INCREMENT，需要先插入0值，如果已存在则更新
INSERT INTO `sys_user` (`user_id`, `open_id`, `union_id`, `qw_id`, `dept_id`, `user_name`, `nick_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `corp_id`, `points`, `agent_id`, `agent_token`, `space_id`, `space_name`) 
VALUES (1, NULL, NULL, NULL, NULL, 'platform', '平台账户', '00', '', '', '0', '', '', '0', '0', '', NULL, 'admin', sysdate(), 'admin', sysdate(), '平台公共账户，用于存储积分抽成', NULL, 0, '', '', '', '')
ON DUPLICATE KEY UPDATE 
  `nick_name` = '平台账户',
  `remark` = '平台公共账户，用于存储积分抽成',
  `update_time` = sysdate();
COMMIT;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
BEGIN;
INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2024-08-08 14:05:58', '', NULL, '');
INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2024-08-08 14:05:58', '', NULL, '');
INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2024-08-08 14:05:58', '', NULL, '');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `corp_id` varchar(255) DEFAULT NULL COMMENT '企业ID',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `corp_id`) VALUES (1, 0, '', '优立方', 1, NULL, NULL, NULL, '0', '0', '企微同步', '2024-08-29 15:07:28', '', NULL, NULL);
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `corp_id`) VALUES (2, 1, '', '其他（待设置部门）', 1, NULL, NULL, NULL, '0', '0', '企微同步', '2024-08-29 15:07:28', '', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
BEGIN;
INSERT INTO `sys_notice` (`notice_id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2024-08-08 14:05:58', '', NULL, '管理员');
INSERT INTO `sys_notice` (`notice_id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2024-08-08 14:05:58', '', NULL, '管理员');
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 'ceo', '企微岗位', 1, '0', 'admin', '2024-08-08 14:05:51', 'admin', '2024-08-09 14:12:49', '');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, 'se', '普通岗位', 2, '0', 'admin', '2024-08-08 14:05:51', 'admin', '2024-08-09 14:13:00', '');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES (1, 1);
INSERT INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES (2, 1);
COMMIT;

-- ----------------------------
-- Table structure for corplist
-- ----------------------------
DROP TABLE IF EXISTS `corplist`;
CREATE TABLE `corplist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `corpid` varchar(18) NOT NULL COMMENT '企业id',
  `secret` varchar(43) NOT NULL COMMENT '聊天内容存档的Secret',
  `corpname` varchar(100) NOT NULL COMMENT '企业名称',
  `prikey` varchar(2048) NOT NULL COMMENT '密文的私有密钥',
  `limits` int(11) NOT NULL DEFAULT '100' COMMENT '一次拉取的消息条数，最大值1000条',
  `timeout` int(11) NOT NULL DEFAULT '5' COMMENT '超时时间(秒)',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0:用户无效 1:用户有效',
  `update` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:无更新 1:有更新',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `corpid` (`corpid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='公司表';

-- ----------------------------
-- Records of corplist
-- ----------------------------
BEGIN;
INSERT INTO `corplist` (`id`, `corpid`, `secret`, `corpname`, `prikey`, `limits`, `timeout`, `status`, `update`) VALUES (1, 'ww722362817b3c466a', 'HpIS5mJPKLHCKiq6GLJfjKrYk7ihOxHQXfzii8pPGV4', '元透社（深圳）互联网络有限公司 ', '-----BEGIN RSA PRIVATE KEY-----\nMIIEogIBAAKCAQEAh7d+6W5LEvds20hnE4beq1HYH8wAM/qz8KDiSfPdBVx6Jpe0\n83Z7ieYY3q6ItSCaCFCL+8M6rdsAZvSVpMuwdlXgZu9Tbm1vvxJsvCEuuZp0WaOz\ngNm2nuhJZ7o4y9goD23xnWJufXEBCMySsor7F8TyynmmnSvtokptpklsHqsix3ud\ncUnxmzAH+aogzgbnhD745AoIbjarBd3wBI5hEQilmtwUU3Fr8kVqUJ8M6st/leFt\nqvYs97jYNj7WKg9apj9TcGo4s/Gyi4eMgvQ8ZkgSCUj6fIZ9qU4746+tIgCRw5OQ\nnb20ta5p6E48vt6eBEGYPNzI69v6gGzud99GZQIDAQABAoIBAAL7Q0C+EUymnl/X\n4JnTd+9UEjcqnGOH8a2K30XII3YjcLSJ1yoVE4Q1R50WwP6Xq4KcwGKEyLR6j/Dz\nFRmEdwk2fEJOpirSIScVsMlWQkhGDiHNAJvHTKWDjV9HvkkuI70pCWqPd8VuNtta\nYSumdXsxcrMDhqdDyIns8Ck7yjIHQFmtesU7ctvqddm6sAJmETfi0cFxa7dR4Ot9\n2uHIjJ9kx3zcVy/lqb9SRpZSCgon0AdcM4hxZW6tXiOGbRtDpcGiDXxHwLVW4bo5\nt2jkKHOug+7EdBBcs2Eusf3ZGbzYVVI7z6bdIkEoeJGHPNfQ7LUj7r0bof48C6ty\nC9nnPYkCgYEAiFiBYNS42oxzERT0HCb8HHmQfr5VjelkeTlisxnFngIrQepXfhwI\nqZTrYGCJNVdgkRBF018gtIY/a5VPmn359gps56rTxwCRORydKzs28lkY/nX4mPSY\n1v7ePjjFfIRpgvmNeRIxBclpBsDENz6K8JwjTOU7nQNUGV1FhdQBKP0CgYEA/tGx\nCIU8IDHBDRX1Dak06bjWov8iM+LSECCJN+nzrMw0Ob8KM5RL4lQZ/9/h26F9qGPf\nRyUg7Y/IIysUjKy0hy/hsQN3mKs54MP2Zwo0oHNXPapzCgmXufqSRurGs6uUczHX\nK71fw/xd+TmfBppfaJVNH7o/UHi6CdtqCMQI44kCgYA2RbyiNaqrW/LFnuiYeDAs\niXsp6EuX5IpY8q3GCwEtp0FeyJAxI6mTDzMuNt8G+5P1yltxCtGy6ik+gr2gCntA\nI+A7yzTnZuNnr2skdTqm9y5Kw9zDzcE0+1itvd1mdjKlrv5QbhxTaFvFE2BHeT7H\nDe/DQRAcrOGCAy2UWtJnZQKBgFsQm0DdRJCI12ISz8GzD7rbGLGllhaO391tkzxN\nOo0taRieAkpOnBPlVGlSHEg+XUbZckjdpvffI3oWAkEH03hgjzqQb6Q6xPNjdOJ8\nDjStI6dhC72xkeyf9LitXJeHIQVN8YSrJ9dFkFvp0MAuWRxqBuboy4m5q1qsdCdv\nz3FpAoGAfrtN7Zu0gJu/H5vw24yRILgn+msViB4wkY50z8fqP+Nm5PfiQXokpj7P\nEuPAZCU2SqEqhUsGMoOMoo71fj4OHKe5cAbslHGuETNoRUHzC1LO18dAiL0JDoMM\nH69btYCloO1sYwlyPkWB8aNB86mAqzCxCSKh5fTP26evE/LKvEY=\n-----END RSA PRIVATE KEY-----', 10, 5, 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` bigint(20) NOT NULL COMMENT '主键，文件id',
  `key` varchar(255) NOT NULL COMMENT '文件在云端的唯一标示，例如：aaa.jpg',
  `filename` varchar(255) NOT NULL COMMENT '文件上传时的名称',
  `request_id` varchar(64) DEFAULT NULL COMMENT '请求id',
  `status` tinyint(4) NOT NULL COMMENT '状态：1-待上传 2-已上传,未使用 3-已使用',
  `platform` tinyint(4) DEFAULT '1' COMMENT '平台：1-腾讯，2-阿里',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `updater` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新者',
  `dep_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '部门id',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除，默认0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表，可以是普通文件、图片等';

-- ----------------------------
-- Records of file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for geth_tran_record
-- ----------------------------
DROP TABLE IF EXISTS `geth_tran_record`;
CREATE TABLE `geth_tran_record` (
  `tran_id` varchar(255) DEFAULT NULL,
  `from` varchar(255) DEFAULT NULL,
  `to` varchar(255) DEFAULT NULL,
  `ether` int(10) DEFAULT NULL,
  `chage_type` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='以太坊交易记录';

-- ----------------------------
-- Records of geth_tran_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for learning_lesson
-- ----------------------------
DROP TABLE IF EXISTS `learning_lesson`;
CREATE TABLE `learning_lesson` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '学生id',
  `course_id` bigint(20) NOT NULL COMMENT '课程id',
  `status` tinyint(4) DEFAULT '0' COMMENT '课程状态，0-未学习，1-学习中，2-已学完，3-已失效',
  `learned_sections` int(11) NOT NULL DEFAULT '0' COMMENT '已学习小节数量',
  `latest_section_id` bigint(20) DEFAULT NULL COMMENT '最近一次学习的小节id',
  `latest_learn_time` datetime DEFAULT NULL COMMENT '最近一次学习的时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`,`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生课表';

-- ----------------------------
-- Records of learning_lesson
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for media
-- ----------------------------
DROP TABLE IF EXISTS `media`;
CREATE TABLE `media` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `file_id` varchar(32) NOT NULL COMMENT '文件在云端的唯一标示，例如：387702302659783576',
  `filename` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名称',
  `media_url` varchar(255) NOT NULL DEFAULT '' COMMENT '媒体播放地址',
  `cover_url` varchar(255) NOT NULL DEFAULT '' COMMENT '媒体封面地址',
  `duration` double NOT NULL DEFAULT '0' COMMENT '视频时长，单位秒',
  `size` bigint(20) NOT NULL DEFAULT '0' COMMENT '视频大小，单位是字节',
  `request_id` varchar(32) DEFAULT '' COMMENT '请求id',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：1-上传中，2-已上传',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `updater` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新者',
  `dep_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '部门id',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除，默认0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='媒资表，主要是视频文件';

-- ----------------------------
-- Records of media
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_send
-- ----------------------------
DROP TABLE IF EXISTS `message_send`;
CREATE TABLE `message_send` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `user_ids` text COMMENT '需要发送的成员userid',
  `corp_id` varchar(36) DEFAULT NULL COMMENT '企业id',
  `external_user_id` text COMMENT '外部联系人userid',
  `message_type` varchar(100) DEFAULT NULL COMMENT '消息类型',
  `agent_id` varchar(36) DEFAULT NULL COMMENT 'appid',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `url` text COMMENT '点击跳转后的链接',
  `btn_txt` varchar(100) DEFAULT NULL COMMENT '按钮文字',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `send_status` int(11) DEFAULT '0' COMMENT '是否已发送1:已发送0未发送',
  `is_date_type` int(11) DEFAULT '0' COMMENT '是否为日期到期类型的消息推送是：1不是：0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_no_send_userid` text COMMENT '发送失败的成员userid',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='消息推送记录表';

-- ----------------------------
-- Records of message_send
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_ww722362817b3c466a
-- ----------------------------
DROP TABLE IF EXISTS `message_ww722362817b3c466a`;
CREATE TABLE `message_ww722362817b3c466a` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msgid` varchar(64) NOT NULL COMMENT '消息ID',
  `publickey_ver` tinyint(4) DEFAULT '1' COMMENT '密钥版本',
  `seq` bigint(21) DEFAULT '0' COMMENT '消息序号，最大值为2^64 -1',
  `action` varchar(16) DEFAULT '' COMMENT '消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型',
  `msgfrom` varchar(64) DEFAULT '' COMMENT '消息发送方id。同一企业内容为userid，非相同企业和机器人消息均为external_userid',
  `tolist` text COMMENT '消息接收方ID列表，多个接收ID以逗号分隔',
  `msgtype` varchar(32) DEFAULT '' COMMENT '消息类型',
  `msgtime` bigint(13) DEFAULT '0' COMMENT '消息发送时间戳，utc时间，ms单位',
  `text` varchar(4000) DEFAULT '' COMMENT '文本消息',
  `sdkfield` varchar(2000) DEFAULT '' COMMENT '附件ID',
  `msgdata` text COMMENT '消息数据 json格式',
  `status` int(11) DEFAULT '1' COMMENT '1：未加载媒体;\r\n2：正在加载媒体；\r\n3：媒体加载完成；\r\n4：媒体加载失败',
  `media_code` int(11) DEFAULT '0' COMMENT '媒体错误码',
  `media_path` varchar(1024) DEFAULT '' COMMENT '媒体文件路径',
  `roomid` varchar(32) DEFAULT '' COMMENT '群聊消息的群id。如果是单聊则为空',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zc_msgid` varchar(55) DEFAULT NULL COMMENT '专区消息ID',
  `secret_key` varchar(1024) DEFAULT NULL COMMENT '专区解密key',
  `tag` varchar(255) DEFAULT NULL COMMENT '标签ID',
  `is_tag` int(4) DEFAULT NULL COMMENT '是否调用过标签模型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `msgid` (`msgid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='会话存档记录表';

-- ----------------------------
-- Records of message_ww722362817b3c466a
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg` (
  `userPrompt` varchar(255) DEFAULT NULL,
  `response` longtext,
  `createTime` varchar(36) DEFAULT NULL,
  `fileUrl` varchar(2000) DEFAULT NULL,
  `chatHistory` longtext,
  `userId` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能体会话记录';

-- ----------------------------
-- Records of msg
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='Blob类型的触发器表';

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='日历信息表';

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='Cron类型的触发器表';

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(20) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(20) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='已触发的触发器表';

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务详细信息表';

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='存储的悲观锁信息表';

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='暂停的触发器表';

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(20) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(20) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='调度器状态表';

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(20) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(20) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(20) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='简单触发器的信息表';

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步机制的行锁表';

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(20) DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(20) DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(6) DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='触发器详细信息表';

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`),
  KEY `idx_login_time` (`login_time` DESC),
  KEY `idx_status` (`status`),
  KEY `idx_user_name` (`user_name`),
  KEY `idx_ipaddr` (`ipaddr`),
  KEY `idx_login_location` (`login_location`),
  KEY `idx_status_time` (`status`, `login_time` DESC),
  KEY `idx_user_status` (`user_name`, `status`, `login_time` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=991 DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录';

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_roombot
-- ----------------------------
DROP TABLE IF EXISTS `sys_roombot`;
CREATE TABLE `sys_roombot` (
  `room_id` varchar(255) DEFAULT NULL COMMENT '群ID',
  `bot_key` varchar(255) DEFAULT NULL COMMENT '机器人key'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群机器人配置表';

-- ----------------------------
-- Records of sys_roombot
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_browse_record
-- ----------------------------
DROP TABLE IF EXISTS `wc_browse_record`;
CREATE TABLE `wc_browse_record` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `res_id` varchar(36) DEFAULT NULL COMMENT '研报ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '最新浏览时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='研报浏览记录';

-- ----------------------------
-- Records of wc_browse_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chat_history
-- ----------------------------
DROP TABLE IF EXISTS `wc_chat_history`;
CREATE TABLE `wc_chat_history` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户ID',
  `userPrompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户指令',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '全部数据',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `tone_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '元宝T1会话ID',
  `ybds_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '元宝DS会话ID',
  `db_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '豆包会话ID',
  `ty_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '通义会话ID',
  `deepseek_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'DeepSeek会话ID',
  `max_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'MiniMax会话ID',
  `metaso_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '秘塔会话ID',
  `kimi_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'KiMi会话ID',
  `baidu_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '百度会话ID',
  `zhzd_chat_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '知乎直答会话ID',
  `chat_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内部chatID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_chat`(`user_id`, `chat_id`) USING BTREE,
  INDEX `idx_chat_create`(`chat_id`, `create_time` DESC) USING BTREE,
  INDEX `idx_user_create_time`(`user_id`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '聊天历史记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chat_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_data
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_data`;
CREATE TABLE `wc_chrome_data` (
  `id` varchar(36) NOT NULL COMMENT '主建',
  `prompt` longtext COMMENT '提示词',
  `promptNum` int(11) DEFAULT NULL COMMENT '提示词长度',
  `answer` longtext COMMENT '答案',
  `answerNum` int(11) DEFAULT NULL COMMENT '答案长度',
  `aiName` varchar(255) DEFAULT NULL COMMENT 'ai名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_name`(`user_name`) USING BTREE,
  INDEX `idx_create_time`(`create_time` DESC) USING BTREE,
  INDEX `idx_ai_name`(`aiName`) USING BTREE,
  INDEX `idx_user_create_time`(`user_name`, `create_time` DESC) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_drafts
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_drafts`;
CREATE TABLE `wc_chrome_drafts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `pic_url` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `summary` longtext COMMENT '摘要',
  `text` longtext COMMENT '正文',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_push` int(4) DEFAULT NULL COMMENT '是否已经推送过公众号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_drafts
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_hotlink
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_hotlink`;
CREATE TABLE `wc_chrome_hotlink` (
  `id` varchar(255) NOT NULL COMMENT '主建ID',
  `link_id` varchar(36) NOT NULL COMMENT '链接ID',
  `prompt` longtext COMMENT '指令',
  `userPrompt` varchar(1000) DEFAULT NULL COMMENT '抓取链接指令',
  `userPromptTwo` varchar(1000) DEFAULT NULL COMMENT '提取摘要指令',
  `promptNum` int(11) DEFAULT NULL COMMENT '指令长度',
  `answer` longtext COMMENT '答案',
  `answerNum` int(11) DEFAULT NULL COMMENT '答案长度',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `summary` longtext COMMENT '摘要',
  `isPush` int(4) DEFAULT NULL COMMENT '是否已经推送过公众号',
  `aiName` varchar(255) DEFAULT NULL COMMENT '来源',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) DEFAULT '0' COMMENT '创建人',
  `text` longtext COMMENT '正文',
  PRIMARY KEY (`link_id`)
  USING BTREE,
  INDEX `idx_create_time`(`create_time` DESC) USING BTREE,
  INDEX `idx_user_name`(`user_name`) USING BTREE,
  INDEX `idx_ai_name`(`aiName`) USING BTREE,
  INDEX `idx_is_push`(`isPush`) USING BTREE,
  INDEX `idx_title`(`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_hotlink
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_hotword
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_hotword`;
CREATE TABLE `wc_chrome_hotword` (
  `id` varchar(36) NOT NULL COMMENT '主建',
  `prompt` longtext COMMENT '提示词',
  `userPrompt` varchar(255) DEFAULT NULL COMMENT '用户',
  `promptNum` int(11) DEFAULT NULL COMMENT '提示词长度',
  `answer` longtext COMMENT '答案',
  `answerNum` int(11) DEFAULT NULL COMMENT '答案长度',
  `aiName` varchar(255) DEFAULT NULL COMMENT 'ai名称',
  `isFetch` int(5) DEFAULT '0' COMMENT '是否抓取过链接',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time` DESC) USING BTREE,
  INDEX `idx_update_time`(`update_time` DESC) USING BTREE,
  INDEX `idx_user_name`(`user_name`) USING BTREE,
  INDEX `idx_ai_name`(`aiName`) USING BTREE,
  INDEX `idx_is_fetch`(`isFetch`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_hotword
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_hotword_log
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_hotword_log`;
CREATE TABLE `wc_chrome_hotword_log` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `key_id` varchar(36) DEFAULT NULL COMMENT '外键',
  `old_prompt` varchar(255) DEFAULT NULL COMMENT '原提示词',
  `old_answer` varchar(255) DEFAULT NULL COMMENT '原热词',
  `prompt` varchar(255) DEFAULT NULL COMMENT '修改后提示词',
  `answer` varchar(255) DEFAULT NULL COMMENT '修改后热词',
  `user_name` varchar(255) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_hotword_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_keyword
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_keyword`;
CREATE TABLE `wc_chrome_keyword` (
  `id` varchar(255) NOT NULL COMMENT '主建ID',
  `link_id` varchar(36) NOT NULL COMMENT '链接ID',
  `prompt` longtext COMMENT '指令',
  `userPrompt` varchar(1000) DEFAULT NULL COMMENT '用户指令',
  `promptNum` int(11) DEFAULT NULL COMMENT '指令长度',
  `answer` longtext COMMENT '答案',
  `answerNum` int(11) DEFAULT NULL COMMENT '答案长度',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `summary` longtext COMMENT '摘要',
  `isPush` int(4) DEFAULT NULL COMMENT '是否已经抓取过链接',
  `aiName` varchar(255) DEFAULT NULL COMMENT '来源',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) DEFAULT '0' COMMENT '创建人',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_keyword
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_task
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_task`;
CREATE TABLE `wc_chrome_task` (
  `id` varchar(255) DEFAULT NULL COMMENT '主建ID',
  `task_id` varchar(255) DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `order` int(4) DEFAULT NULL COMMENT '任务排序',
  `start_time` datetime DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务完成时间',
  `plan_time` varchar(255) DEFAULT NULL COMMENT '预计执行时间',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `corp_id` varchar(255) DEFAULT NULL COMMENT '企业ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_task_prompt
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_task_prompt`;
CREATE TABLE `wc_chrome_task_prompt` (
  `id` varchar(36) DEFAULT NULL COMMENT '主建ID',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `task_prompt` varchar(255) DEFAULT NULL COMMENT '指令模板',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户unionid'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_task_prompt
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_chrome_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_template`;
CREATE TABLE `wc_chrome_template` (
  `id` varchar(36) DEFAULT NULL COMMENT '主建ID',
  `tem_type` varchar(255) DEFAULT NULL COMMENT '模板场景',
  `hotword_tem` varchar(2550) DEFAULT NULL COMMENT '主题生成模板',
  `hotlink_tem` varchar(2550) DEFAULT NULL COMMENT '素材搜集模板',
  `summary_tem` varchar(2550) DEFAULT NULL COMMENT '内容生成模板',
  `user_name` varchar(255) DEFAULT NULL COMMENT '上传人unionid',
  `flow_status` int(4) DEFAULT NULL COMMENT '审核状态',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_chrome_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_collection_record
-- ----------------------------
DROP TABLE IF EXISTS `wc_collection_record`;
CREATE TABLE `wc_collection_record` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `res_id` varchar(36) DEFAULT NULL COMMENT '研报ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '收藏时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='研报收藏记录';

-- ----------------------------
-- Records of wc_collection_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_comment_res
-- ----------------------------
DROP TABLE IF EXISTS `wc_comment_res`;
CREATE TABLE `wc_comment_res` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `res_id` varchar(36) DEFAULT NULL COMMENT '研报ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '评论人ID',
  `comment` longtext COMMENT '评论内容',
  `create_time` datetime DEFAULT NULL COMMENT '评论时间',
  `userlike` int(4) DEFAULT NULL COMMENT '点赞数',
  `flow_status` int(4) DEFAULT NULL COMMENT '1审核通过 0审核中 2驳回'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='研报评论表';

-- ----------------------------
-- Records of wc_comment_res
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_down_record
-- ----------------------------
DROP TABLE IF EXISTS `wc_down_record`;
CREATE TABLE `wc_down_record` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `res_id` varchar(36) DEFAULT NULL COMMENT '研报ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '下载时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='研报下载记录';

-- ----------------------------
-- Records of wc_down_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_js_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_js_template`;
CREATE TABLE `wc_js_template` (
  `id` varchar(255) DEFAULT NULL COMMENT '主键ID',
  `js_name` varchar(255) DEFAULT NULL COMMENT '脚本名称',
  `js_desc` varchar(255) DEFAULT NULL COMMENT '脚本描述',
  `js_prompt` longtext COMMENT '脚本指令',
  `expent_point` int(3) DEFAULT NULL COMMENT '消耗积分',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_id` varchar(36) DEFAULT NULL COMMENT '创建人iD',
  `emp_name` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `updator` varchar(36) DEFAULT NULL COMMENT '更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_js_template
-- ----------------------------
BEGIN;
INSERT INTO `wc_js_template` (`id`, `js_name`, `js_desc`, `js_prompt`, `expent_point`, `create_time`, `user_id`, `emp_name`, `update_time`, `updator`) VALUES ('123232', 'F1', '根据多搜索插件进行汇总舆情简报', 'Y3MubW91c2VNb3ZlKDE0OTYsOTg3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDk3LDk4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUwMCw5NzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1MDMsOTYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTA2LDk1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUxMSw5NDcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1MTQsOTQxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTIwLDkzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUyNCw5MjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MjksOTA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTM1LDg5Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTUzOSw4ODUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE1NDUsODcyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTUxLDg2MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTU1Nyw4NDYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE1NjMsODM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTcyLDgyMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTU4MSw4MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1OTAsNzg5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTk2LDc3OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTYwNSw3NjQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2MTksNzQzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjI5LDcyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY0Myw3MDUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2NTMsNjg5KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNjY3LDY2OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTY3Niw2NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2ODMsNjM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjk1LDYxNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwNCw1OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MTIsNTg1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNzI0LDU2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTczMCw1NTIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE3MzksNTM2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNzQ2LDUyMik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTc1NCw1MTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NjQsNDg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzczLDQ3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc4MSw0NTYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE3ODgsNDM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzk2LDQyMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTgwMiw0MTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE4MDksMzk2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxODE3LDM4MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTgyMCwzNzIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE4MjcsMzU2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODMzLDMzOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTgzOCwzMjkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE4NDIsMzE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODQ3LDMwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg0OCwyOTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4NTAsMjg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODUxLDI4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg1MSwyNzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE4NTEsMjY3KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxODUxLDI2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg1MywyNTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE4NTMsMjQ5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxODUzLDI0NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg1MywyMzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4NTMsMjM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODUzLDIzMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg1MywyMjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4NTMsMjIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODUzLDIxOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTg1MywyMTUpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE4NTMsMjEyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxODUzLDIwNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg1MywyMDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4NTMsMTk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODUzLDE5MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTg1MywxODYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE4NTMsMTgwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxODUzLDE3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg1MywxNzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE4NTMsMTcwKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTg1MywxNjgpOwpzbGVlcCg0MCk7CmNzLm1vdXNlTW92ZSgxODU0LDE2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg1NCwxNjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4NTQsMTU5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxODU0LDE1OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTg1NiwxNTYpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE4NTYsMTU1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODU2LDE1Myk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDE4NTYsMTUyKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTg1NywxNDkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE4NTcsMTQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODYwLDE0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg2MywxMzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4NjUsMTMyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxODY4LDEyOSk7CnNsZWVwKDEwKTsKY3MubW91c2VNb3ZlKDE4NjksMTI1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxODcxLDEyMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTg3MiwxMjApOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxODc0LDEyMCk7CnNsZWVwKDMwKTsKY3MubW91c2VNb3ZlKDE4NzQsMTE5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxODc1LDExOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg3NSwxMTcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE4NzcsMTE3KTsKc2xlZXAoMzIpOwpjcy5tb3VzZU1vdmUoMTg3OCwxMTcpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxODgxLDExNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg4MywxMTQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE4ODQsMTE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODg2LDExMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTg4OSwxMTEpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxODkwLDExMSk7CnNsZWVwKDI5MCk7CmNzLm1vdXNlTW92ZSgxODkwLDExMCk7CnNsZWVwKDIyKTsKY3MubW91c2VMZWZ0RG93bigxODkwLDExMCk7CnNsZWVwKDk0KTsKY3MubW91c2VMZWZ0VXAoMTg5MCwxMTApOwpzbGVlcCg1NCk7CmNzLm1vdXNlTW92ZSgxODg5LDExMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg4NiwxMTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4ODMsMTEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODgwLDExNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg3NCwxMTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE4NjgsMTE4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODYxLDEyMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTg0NywxMjMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE4MjgsMTI0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODA3LDEyNyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTc4OSwxMjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE3NjAsMTMyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzIzLDEzMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY5NCwxMzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE2NTcsMTM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjE5LDEzNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTU4MiwxMzYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1NDQsMTM2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDk4LDEzNik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTQ2MCwxMzYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MTQsMTM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzY3LDEzNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTMyMSwxMzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNzQsMTMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjI4LDEyOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4MywxMjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNDUsMTI2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTA5LDEyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA3MCwxMjMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMzAsMTIyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg5OTgsMTIwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NjUsMTE5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5MjMsMTE0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4OTUsMTExKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjgsMTA4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MzAsMTA0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4MDIsMTAxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3NzUsOTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc1Nyw5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzI4LDkzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3MTMsOTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY5Miw5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjczLDg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NDksODkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYzMSw4Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjE5LDg0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MDEsODMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU5MCw4MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTc1LDgwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjYsNzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1Myw3OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTQ0LDc3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MzMsNzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUxOCw3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDk5LDc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0ODgsNzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ3MCw3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU0LDcxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MzcsNzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQxOSw3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDA2LDcxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgzOTEsNzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM3Nyw3MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzYyLDcxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzNTAsNzEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDMzMiw3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzE3LDcxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMDcsNzEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDI5Miw2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjgxLDY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNzIsNjYpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDI2NSw2Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjU2LDY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNDgsNjUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDIzOSw2NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjI5LDY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMTUsNjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIwMyw2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTkxLDY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxODIsNjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2Niw2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU0LDY2KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNDYsNjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzOSw2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwLDY1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjUsNjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMiw2NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxLDY1KTsKc2xlZXAoMTQwKTsKY3MubW91c2VNb3ZlKDExOCw2Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE1LDYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTIsNjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwOSw2MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA2LDU5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDMsNTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMSw1Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTgsNTcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk3LDU3KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoOTUsNTcpOwpzbGVlcCg3MSk7CmNzLm1vdXNlTW92ZSg5NSw1Nik7CnNsZWVwKDI0Mik7CmNzLm1vdXNlTGVmdERvd24oOTUsNTYpOwpzbGVlcCg3OCk7CmNzLm1vdXNlTGVmdFVwKDk1LDU2KTsKc2xlZXAoMjE0OCk7CmNzLm1vdXNlTW92ZSg5Niw1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgsNjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMiw2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA3LDczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTMsNzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNyw4NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIzLDg4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjgsOTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzMSw5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM0LDEwMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3LDEwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM4LDEwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQxLDEwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0LDExMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUwLDExNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1LDEyMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTYyLDEyOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcxLDEzNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTgwLDE0MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTg4LDE0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTk1LDE1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjAzLDE1Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjA5LDE2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjEzLDE2Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjE2LDE2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjE5LDE2OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjIxLDE3MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMjI0LDE3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjI3LDE3NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjMwLDE4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjM0LDE4NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjQwLDE4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjQ2LDE5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjU0LDE5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjU4LDIwMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjY0LDIwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjY5LDIwNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMjcyLDIwOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjc1LDIxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjc4LDIxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjgxLDIxNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjg0LDIxNyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjg3LDIyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjkxLDIyMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjk2LDIyOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzAwLDIzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzA2LDIzNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzEyLDIzNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzE4LDIzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzI0LDI0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzMwLDI0Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzM1LDI0NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzM5LDI0NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzQ0LDI0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzQ1LDI0Nik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDM0NywyNDcpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgzNTAsMjQ3KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgzNTMsMjQ5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzNTQsMjQ5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzNTcsMjUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjAsMjUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjIsMjUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjMsMjUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjUsMjUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjYsMjUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjgsMjU1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgzNzEsMjU1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzNzIsMjU2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzNzcsMjU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzODEsMjU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzODcsMjYxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzOTIsMjYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzOTYsMjY1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0MDEsMjY3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MDcsMjY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MTEsMjcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MTQsMjcxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg0MTksMjczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MjMsMjc2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MjgsMjc3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MzEsMjc5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0MzUsMjgwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0NDEsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NDYsMjg1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0NTAsMjg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NTYsMjg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NjQsMjkxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0NzAsMjkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NzYsMjk0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0ODAsMjk0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0ODYsMjk1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0OTQsMjk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0OTgsMjk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MDcsMzAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MTgsMzAzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg1MzMsMzA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NDIsMzA2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg1NTgsMzA3KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg1NzUsMzA5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg1OTAsMzA5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2MDIsMzEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTgsMzEwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2MzAsMzEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NDIsMzEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NTMsMzEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjgsMzE1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2ODAsMzE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTAsMzE2KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg3MDgsMzE4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MTksMzE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MzUsMzE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NDksMzE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NjEsMzE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NzYsMzE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3ODMsMzE5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3OTUsMzE4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MDQsMzE4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MTIsMzE4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg4MjEsMzE4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MjcsMzE3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4MzcsMzE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTEsMzE3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NjEsMzE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NzYsMzE3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4ODIsMzE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4OTcsMzE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MDgsMzE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MTgsMzE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MjQsMzE0KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg5MjcsMzE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MzAsMzE0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5MzIsMzE0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5MzMsMzE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MzMsMzEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MzYsMzExKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5MzgsMzExKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NDIsMzExKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5NDUsMzA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NDgsMzA4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg5NTEsMzA4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NTQsMzA2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5NTcsMzA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NTksMzAzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NjAsMzAzKTsKc2xlZXAoNDApOwpjcy5tb3VzZU1vdmUoOTYyLDMwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTY1LDMwMCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTY2LDI5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTY5LDI5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTcxLDI5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTc0LDI5Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTc1LDI5MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTc3LDI5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgwLDI4OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTgxLDI4Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgzLDI4Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTg0LDI4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDI4NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTg3LDI4NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTkwLDI4Mik7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDk5MiwyODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5MiwyODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5MywyODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5NSwyODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5NSwyNzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5OCwyNzkpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDk5OSwyNzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMDIsMjc2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA0LDI3NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAwNywyNzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDgsMjc1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA4LDI3Myk7CnNsZWVwKDM5KTsKY3MubW91c2VNb3ZlKDEwMTAsMjczKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTAxMSwyNzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTMsMjcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE0LDI3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxNiwyNzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMTYsMjcwKTsKc2xlZXAoNzgxKTsKY3MubW91c2VMZWZ0RG93bigxMDE2LDI3MCk7CnNsZWVwKDY0KTsKY3MubW91c2VMZWZ0VXAoMTAxNiwyNzApOwpzbGVlcCgyMzQpOwpjcy5tb3VzZU1vdmUoMTAxNywyNjkpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDE5LDI2OSk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTAxOSwyNjcpOwpzbGVlcCg1Nyk7CmNzLm1vdXNlTW92ZSgxMDIyLDI2Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAyMywyNjQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwMjYsMjYzKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDI4LDI2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyOSwyNjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjksMjYwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDMxLDI2MCk7CnNsZWVwKDE4OCk7CmNzLm1vdXNlTW92ZSgxMDMxLDI1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzMiwyNTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzIsMjU3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM0LDI1Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTAzNSwyNTUpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDM3LDI1NCk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDEwMzcsMjUyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDM4LDI1Mik7CnNsZWVwKDM4KTsKY3MubW91c2VNb3ZlKDEwNDAsMjUxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQxLDI1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0MywyNDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDQsMjQ4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDQ2LDI0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0NiwyNDUpOwpzbGVlcCg2Mik7CmNzLm1vdXNlTW92ZSgxMDQ3LDI0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1MCwyNDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTAsMjQwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDUzLDIzOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA1NSwyMzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTYsMjM0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDU4LDIzMyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTA1OSwyMzEpOwpzbGVlcCgyMzcpOwpjcy5tb3VzZU1vdmUoMTA2MSwyMzEpOwpzbGVlcCgxMDE0KTsKY3MubW91c2VNb3ZlKDEwNjAsMjMxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDYwLDIzMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA1OCwyMzApOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMDU3LDIzMCk7CnNsZWVwKDIyNik7CmNzLm1vdXNlTW92ZSgxMDU3LDIyNyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwNTcsMjI1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDU4LDIyNCk7CnNsZWVwKDE3NzEpOwpjcy5tb3VzZU1vdmUoMTA1OCwyMzIpOwpzbGVlcCg1MDEpOwpjcy5tb3VzZUxlZnREb3duKDEwNTgsMjMyKTsKc2xlZXAoNjIpOwpjcy5tb3VzZUxlZnRVcCgxMDU4LDIzMik7CnNsZWVwKDI5MCk7CmNzLm1vdXNlTW92ZSgxMDU4LDIzMik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTA1OCwyMjgpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMDU5LDIzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA2MSwyMzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNjQsMjM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDcwLDI0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA3NywyNDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODUsMjUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDk4LDI2NCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTEwNiwyNjgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMTUsMjc0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTIyLDI4Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEyNywyODkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzMsMjk1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTM3LDMwMSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTEzOSwzMDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNDAsMzEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTQyLDMxOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE0MiwzMjEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNDIsMzI1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTQyLDMzMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE0MiwzMzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNDIsMzM3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTQyLDM0MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE0MiwzNDIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExNDIsMzQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTQyLDM0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE0MSwzNTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNDEsMzU0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTQxLDM1Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTEzOSwzNjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzksMzY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTM4LDM2OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEzNiwzNzIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzYsMzc2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTM1LDM3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzMywzODQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzMsMzg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTMyLDM5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzMiwzOTQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMzIsMzk2KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMTMwLDM5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzMCw0MDIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzAsNDA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI5LDQwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyOSw0MDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMjksNDA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI5LDQxMSk7CnNsZWVwKDI0Myk7CmNzLm1vdXNlTGVmdERvd24oMTEyOSw0MTEpOwpzbGVlcCg1NCk7CmNzLm1vdXNlTGVmdFVwKDExMjksNDExKTsKc2xlZXAoNzApOwpjcy5tb3VzZU1vdmUoMTEyNyw0MTEpOwpzbGVlcCgyMyk7CmNzLm1vdXNlTW92ZSgxMTI2LDQxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyMSw0MTQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMTcsNDE3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTExLDQyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEwMyw0MjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwOTQsNDMwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDg1LDQzNik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTA3MCw0NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNjQsNDQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQ5LDQ0Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAzOSw0NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzEsNDQ3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE4LDQ0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTk3LDQ0OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTc5LDQ1MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTUzLDQ1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTI1LDQ2Mik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTA3LDQ2OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODgwLDQ3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODUzLDQ4Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoODMwLDQ4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODAzLDQ5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzgxLDUwMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzU0LDUwNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNzM0LDUwOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzA2LDUxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjgzLDUxNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjYxLDUyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDUyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjIyLDUyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjA3LDUzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTkzLDUzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTg3LDUzNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTc3LDUzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTY1LDUzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTUzLDU0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTQyLDU0NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTMzLDU0Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTIzLDU0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTE1LDU0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTA2LDU1MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTAwLDU1Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDkzLDU1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg1LDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDc2LDU1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDY3LDU1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU4LDU1OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDQ4LDU1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDM3LDU1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI4LDU1OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDE2LDU1OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDA2LDU1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzk4LDU1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzg5LDU1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzgyLDU1OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzcwLDU1OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzY0LDU1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzU2LDU2MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzQ3LDU2Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzQwLDU2NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzM0LDU2NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzI4LDU2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzIwLDU2OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzE2LDU2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzA4LDU3MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzAyLDU3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjk4LDU3MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjk1LDU3Myk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMjkzLDU3Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjkyLDU3Myk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDI5MCw1NzQpOwpzbGVlcCgzMik7CmNzLm1vdXNlTW92ZSgyODksNTc0KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMjg2LDU3NCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMjgxLDU3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjc1LDU3Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjY4LDU3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjYwLDU4MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjUwLDU4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjQxLDU4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjM1LDU4Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjMwLDU4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjI3LDU4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjI2LDU4Myk7CnNsZWVwKDYzKTsKY3MubW91c2VNb3ZlKDIyNiw1ODIpOwpzbGVlcCg0NSk7CmNzLm1vdXNlTW92ZSgyMjcsNTgxKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMjI4LDU3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjMwLDU3OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjMxLDU3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjMxLDU3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjMzLDU3Myk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDIzMyw1NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIzMyw1NzApOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgyMzMsNTY5KTsKc2xlZXAoMTExKTsKY3MubW91c2VNb3ZlKDIzMyw1NjcpOwpzbGVlcCg2Mik7CmNzLm1vdXNlTW92ZSgyMzMsNTY2KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMjMyLDU2NCk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDIzMiw1NjMpOwpzbGVlcCgzNTApOwpjcy5tb3VzZU1vdmUoMjMwLDU2Myk7CnNsZWVwKDQ4NCk7CmNzLm1vdXNlTGVmdERvd24oMjMwLDU2Myk7CnNsZWVwKDcyKTsKY3MubW91c2VMZWZ0VXAoMjMwLDU2Myk7CnNsZWVwKDM4Mik7CmNzLm1vdXNlTW92ZSgyMzEsNTYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMzYsNTYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyNDIsNTYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNDgsNTYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNTcsNTYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyNjYsNTYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyNzgsNTY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyOTAsNTY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMDMsNTY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMTUsNTY1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzMjksNTY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNTAsNTY3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjMsNTY4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzNzUsNTcwKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgzOTMsNTcxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MDgsNTczKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0MTcsNTczKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0MzEsNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MzcsNTc2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0NDcsNTc2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0NjEsNTc3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0NzEsNTc5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0ODUsNTc5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0OTcsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MDQsNTc5KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg1MTIsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MTgsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MjIsNTc5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg1MjgsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MzMsNTc4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg1MzksNTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NDUsNTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NTIsNTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NTgsNTc4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg1NjMsNTc4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg1NjksNTc4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg1NzMsNTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NzgsNTc4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg1NzksNTc5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg1ODEsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODIsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODQsNTgwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg1ODUsNTgwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg1OTAsNTgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1OTQsNTgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1OTksNTgzKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg2MDMsNTg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MDgsNTg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTEsNTg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTIsNTg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTQsNTg2KTsKc2xlZXAoMTI1KTsKY3MubW91c2VNb3ZlKDYxNCw1ODgpOwpzbGVlcCgzMSk7CmNzLm1vdXNlTW92ZSg2MTQsNTg5KTsKc2xlZXAoNDkpOwpjcy5tb3VzZU1vdmUoNjE0LDU5MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjEzLDU5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjExLDU5Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjExLDU5NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjEwLDU5NSk7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDYwOCw1OTcpOwpzbGVlcCgyNDMpOwpjcy5tb3VzZUxlZnREb3duKDYwOCw1OTcpOwpzbGVlcCg3OCk7CmNzLm1vdXNlTGVmdFVwKDYwOCw1OTcpOwpzbGVlcCgxNjMpOwpjcy5tb3VzZU1vdmUoNjA5LDU5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjEyLDU5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjE3LDU5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjIxLDU5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjI3LDU5Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjMwLDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjM2LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ0LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjUwLDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU3LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY2LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjc3LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjg0LDU5Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjkwLDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjk1LDU5Nik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNzAyLDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzA4LDU5Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNzExLDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzE5LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzI2LDU5Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzMyLDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzQxLDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzQ5LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzU4LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzY3LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzc0LDU5NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzgwLDU5NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzg1LDU5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzg5LDU5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzk0LDU5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzk4LDU5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODA0LDU5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODEyLDU5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODIxLDU5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODMwLDU5Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODM5LDU5NCk7CnNsZWVwKDEwKTsKY3MubW91c2VNb3ZlKDg0OCw1OTQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDg1Miw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg2MCw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg2Niw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg3MCw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg3NSw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg3OSw1OTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg4NCw1OTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg4OCw1OTApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg5Niw1ODcpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDkwMiw1ODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkwOCw1ODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkxNSw1NzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyMSw1NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyNyw1NjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkzMyw1NjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkzOSw1NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0NCw1NDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw1NDApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk1Nyw1MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk2Miw1MzApOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDk2OCw1MjIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk3NSw1MTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4Myw1MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5MCw1MDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk5OSw0OTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDUsNDk0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDE0LDQ4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyMCw0ODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjksNDc2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDM3LDQ3MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA0NCw0NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTIsNDYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDYxLDQ1NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA3MCw0NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNzcsNDQ2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDg2LDQ0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA5Miw0MzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDMsNDMyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTEwLDQyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExNiw0MjUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMjUsNDIwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTM2LDQxNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE0OCw0MDcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNTgsNDAxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTY5LDM5Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE4MiwzODcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTMsMzgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjExLDM3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIyMCwzNjgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyMzYsMzYwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjQ4LDM1NCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTI1OSwzNTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjksMzQ1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjc3LDM0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4NywzMzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTIsMzM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjk5LDMzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwNSwzMjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMTMsMzI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzIwLDMyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMyNiwzMTgpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzMzQsMzE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzM4LDMxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM0NCwzMDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNTIsMzAzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzUzLDMwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM1NSwzMDIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNTYsMzAwKTsKc2xlZXAoNjkpOwpjcy5tb3VzZU1vdmUoMTM1OCwyOTkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNTksMjk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzYxLDI5Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM2MiwyOTYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNjIsMjk0KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTM2NCwyOTQpOwpzbGVlcCg5NCk7CmNzLm1vdXNlTGVmdERvd24oMTM2NCwyOTQpOwpzbGVlcCgxMDIpOwpjcy5tb3VzZUxlZnRVcCgxMzY0LDI5NCk7CnNsZWVwKDc5Nik7CmNzLmtleURvd24oNzYpOwpzbGVlcCgxMTIpOwpjcy5rZXlVcCg3Nik7CnNsZWVwKDU2KTsKY3Mua2V5RG93big3Myk7CnNsZWVwKDg3KTsKY3Mua2V5RG93big2NSk7CnNsZWVwKDI1KTsKY3Mua2V5VXAoNzMpOwpzbGVlcCg3Mik7CmNzLmtleURvd24oNzgpOwpzbGVlcCgzMik7CmNzLmtleVVwKDY1KTsKc2xlZXAoNzkpOwpjcy5rZXlEb3duKDg3KTsKc2xlZXAoMTcpOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDg4KTsKY3Mua2V5RG93big2NSk7CnNsZWVwKDIzKTsKY3Mua2V5RG93big3OCk7CnNsZWVwKDMyKTsKY3Mua2V5VXAoODcpOwpzbGVlcCg4MCk7CmNzLmtleVVwKDY1KTsKc2xlZXAoMjUpOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDI0KTsKY3Mua2V5RG93big3MSk7CnNsZWVwKDE0NCk7CmNzLmtleVVwKDcxKTsKc2xlZXAoODApOwpjcy5rZXlEb3duKDMyKTsKc2xlZXAoMTA0KTsKY3Mua2V5VXAoMzIpOwpzbGVlcCg1NzYpOwpjcy5tb3VzZU1vdmUoMTM2NSwyOTQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNjgsMjk0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzczLDI5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3NiwyOTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzksMzAxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzgwLDMwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM4MiwzMDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzODMsMzEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzgzLDMxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM4NiwzMTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzODksMzE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzkyLDMyMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM5NSwzMjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDAsMzI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDA0LDMzMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQwNiwzMzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MDcsMzM0KTsKc2xlZXAoMzIpOwpjcy5tb3VzZU1vdmUoMTQwNywzMzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDksMzM5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDEwLDM0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQxMiwzNDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MTUsMzUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDE2LDM1NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQxOSwzNjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjEsMzYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDIyLDM2Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQyMiwzNjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjQsMzY3KTsKc2xlZXAoODYpOwpjcy5tb3VzZU1vdmUoMTQyNSwzNjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MjUsMzcwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDI3LDM3MCk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDE0MjcsMzcyKTsKc2xlZXAoMzEpOwpjcy5tb3VzZU1vdmUoMTQyOCwzNzIpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxNDMwLDM3Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQzMSwzNzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzEsMzc2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDMzLDM3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQzMywzNzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MzQsMzc5KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTQzNiwzODEpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxNDM3LDM4MSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDE0MzksMzgyKTsKc2xlZXAoNzEpOwpjcy5tb3VzZU1vdmUoMTQ0MCwzODQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0NDMsMzg1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDQ1LDM4OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ0NiwzOTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NDgsMzkwKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNDQ5LDM5MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQ1MSwzOTMpOwpzbGVlcCgyMDQpOwpjcy5tb3VzZUxlZnREb3duKDE0NTEsMzkzKTsKc2xlZXAoNjkpOwpjcy5tb3VzZUxlZnRVcCgxNDUxLDM5Myk7CnNsZWVwKDI3NSk7CmNzLm1vdXNlTW92ZSgxNDUxLDM5Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTQ1MSw0MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NTAsNDEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDUwLDQyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0Nyw0MzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0NDUsNDQyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDQyLDQ1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQzOSw0NjApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0MzgsNDY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDM1LDQ3Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQzMyw0NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzMsNDgxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDMyLDQ4NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQzMiw0ODcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzAsNDkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDMwLDQ5NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQzMCw0OTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MzAsNDk5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDMwLDUwMSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQyOSw1MDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjksNTA0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDI5LDUwNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQyOSw1MDcpOwpzbGVlcCg2Myk7CmNzLm1vdXNlTW92ZSgxNDI3LDUwOCk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDE0MjYsNTExKTsKc2xlZXAoMjE4KTsKY3MubW91c2VMZWZ0RG93bigxNDI2LDUxMSk7CnNsZWVwKDc4KTsKY3MubW91c2VMZWZ0VXAoMTQyNiw1MTEpOwpzbGVlcCgxMzIpOwpjcy5tb3VzZU1vdmUoMTQyNCw1MTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjAsNTE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDA5LDUxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM5OSw1MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzODgsNTI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzczLDUzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM1NSw1MzgpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzMzEsNTQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzEzLDU1MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4NSw1NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTMsNTY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjI2LDU3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE5OCw1NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNTksNTgzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI3LDU4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA5NCw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTgsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI1LDYwNCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTk3LDYxMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTcwLDYxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTQzLDYxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTE0LDYyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODg3LDYyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODY1LDYyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODQ1LDYzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODMwLDYzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODA5LDYzNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzkzLDYzNik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNzc2LDYzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzY2LDYzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzQ2LDY0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzI4LDY0NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzEyLDY0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjkyLDY1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjc0LDY1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU4LDY2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDY2NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjMxLDY2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjE2LDY3Myk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTk2LDY4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTc3LDY5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTYwLDY5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTUwLDcwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTMwLDcxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTE0LDcxOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDk3LDcyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg0LDczMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDc1LDczNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDY2LDc0MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDU4LDc0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU0LDc0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDUxLDc0OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDQ2LDc1MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDQzLDc1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDM3LDc1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDMxLDc1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI1LDc1OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDE4LDc2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDEyLDc2MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDA2LDc2Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDAwLDc2Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzk0LDc2Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzg5LDc2NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzg1LDc2NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzgwLDc2NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzc3LDc2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzczLDc2OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzY4LDc2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzYxLDc2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzUzLDc2OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzQ2LDc2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzQzLDc2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzM3LDc2OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzMxLDc2OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzI1LDc2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzIwLDc2NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzE2LDc2NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzEzLDc2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzEwLDc2MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzA1LDc1OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzAxLDc1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjk2LDc0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjk1LDc0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjkzLDc0MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjkyLDczOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjkyLDczNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjkyLDczMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjkyLDcyOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjkzLDcyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjkzLDcyNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjk0LDcyNSk7CnNsZWVwKDMyKTsKY3MubW91c2VNb3ZlKDI5Niw3MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5OSw3MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMwMyw3MjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMwNiw3MTkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDMwOSw3MTcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDMxMiw3MTcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDMxNCw3MTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMxNSw3MTQpOwpzbGVlcCg2Myk7CmNzLm1vdXNlTW92ZSgzMTgsNzE0KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgzMjAsNzE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMjEsNzE0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzMjMsNzE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMjcsNzEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMjksNzEzKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMzMwLDcxMSk7CnNsZWVwKDM4KTsKY3MubW91c2VNb3ZlKDMzMCw3MTApOwpzbGVlcCg5NjIpOwpjcy5tb3VzZUxlZnREb3duKDMzMCw3MTApOwpzbGVlcCg5Mik7CmNzLm1vdXNlTW92ZSgzMzIsNzEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMzUsNzA4KTsKc2xlZXAoMTApOwpjcy5tb3VzZU1vdmUoMzQxLDcwNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzQ3LDcwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzU0LDY5OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzYyLDY5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzcxLDY5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzgzLDY4Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzkzLDY4Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDAyLDY3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDEwLDY3Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDE3LDY3NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDI4LDY3MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDMxLDY2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDM3LDY2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQ2LDY2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDUyLDY1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU4LDY1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDYxLDY1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDY3LDY0Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDczLDY0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDc5LDYzOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDg2LDYzNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg5LDYzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDk1LDYyOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTAwLDYyNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTA0LDYyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTA3LDYyMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTEyLDYxOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTE2LDYxNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTIyLDYxMSk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNTI3LDYwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTMxLDYwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTM3LDU5OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTQ1LDU5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTUyLDU5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTU0LDU4Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTYzLDU4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTY0LDU4MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTY5LDU3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTcyLDU3Nik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTc1LDU3NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTc2LDU3Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTc5LDU3MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTgyLDU2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTg1LDU2Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTkwLDU2NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTkzLDU2Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTk2LDU2MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjAwLDU1OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjAzLDU1Nyk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNjA2LDU1NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjA4LDU1NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjA5LDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjEyLDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjEyLDU1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjE0LDU1MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjE1LDU0OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjE4LDU0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjIxLDU0NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjIzLDU0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjI3LDU0Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjI5LDU0MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjMyLDUzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjMzLDUzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjM2LDUzNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjM5LDUzNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQyLDUzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ3LDUzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjUwLDUzMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjUzLDUyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU2LDUyOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjU2LDUyNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjU3LDUyNyk7CnNsZWVwKDQ4KTsKY3MubW91c2VNb3ZlKDY1Nyw1MjUpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg2NTksNTI1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg2NjAsNTIyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjMsNTIxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2NjUsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjYsNTE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjYsNTE4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2NjgsNTE4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjksNTE2KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoNjcxLDUxNik7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDY3MSw1MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3NCw1MTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3NSw1MTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3OCw1MDkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY4MCw1MDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY4Myw1MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY4NCw1MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY4NCw1MDEpOwpzbGVlcCgzNTApOwpjcy5tb3VzZUxlZnRVcCg2ODQsNTAxKTsKc2xlZXAoNjQpOwpjcy5tb3VzZU1vdmUoNjg0LDUwMik7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDY4Myw1MDIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDY4Myw1MDQpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSg2ODIsNTA3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODAsNTEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODAsNTE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODAsNTE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODEsNTI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODYsNTM1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2OTAsNTQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTIsNTUzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2OTUsNTYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTYsNTY4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2OTYsNTc2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTYsNTgyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2OTYsNTg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTYsNTk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTYsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTQsNjA3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2OTIsNjE1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg2OTIsNjE2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2ODksNjI0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODYsNjMwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODMsNjM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODIsNjQwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2ODAsNjQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NzksNjQ4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NzYsNjUxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NzYsNjU0KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoNjc0LDY1Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjc0LDY1OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjczLDY2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjczLDY2Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjcxLDY2NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjcwLDY2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY4LDY2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY4LDY3MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY3LDY3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY1LDY3Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjY1LDY3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY0LDY3Nik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjYyLDY3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjYyLDY3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjYxLDY4MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjYxLDY4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU5LDY4NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjU4LDY4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU2LDY4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU1LDY5MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjUyLDY5NCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjQ5LDY5Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjQ3LDcwMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ0LDcwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQxLDcwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDcwOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjM4LDcwOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjM4LDcwOSk7CnNsZWVwKDE0OCk7CmNzLm1vdXNlTW92ZSg2MzcsNzA5KTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoNjM3LDcxMSk7CnNsZWVwKDMxMyk7CmNzLm1vdXNlTGVmdERvd24oNjM3LDcxMSk7CnNsZWVwKDEwMik7CmNzLm1vdXNlTW92ZSg2MzgsNzExKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2NDEsNzEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NDUsNzA4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg2NTEsNzA3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NTcsNzA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjYsNzA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NzQsNzAxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2ODMsNjk5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODksNjk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MDQsNjk1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3MjAsNjkwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg3MzUsNjg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NTMsNjgxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg3NjQsNjc4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3ODAsNjc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3OTgsNjY4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4MDksNjY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MjEsNjYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MjgsNjYwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4MzksNjU3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NDgsNjU2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NTUsNjUzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NjQsNjUxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg4NzAsNjUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NzksNjQ4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4OTEsNjQ1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4OTYsNjQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MDYsNjQyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MjEsNjM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MzIsNjM2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5NDUsNjMzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NTQsNjMwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NzEsNjI3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5ODAsNjI0KTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg5OTAsNjIxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDAyLDYxOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAxNiw2MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjYsNjE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM3LDYxMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA0NCw2MTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDksNjExKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDU2LDYwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA2NSw2MDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNzAsNjA5KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDc0LDYwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA3Nyw2MDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODIsNjA4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDg1LDYwOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA4OCw2MDYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwOTIsNjA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDk1LDYwNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEwMCw2MDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDEsNjA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTA0LDYwNSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTEwNiw2MDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDcsNjA1KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTEwOSw2MDUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMTAsNjA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTEyLDYwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExMyw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMTYsNjAzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTE4LDYwMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTExOSw2MDMpOwpzbGVlcCgyMik7CmNzLm1vdXNlTW92ZSgxMTIxLDYwMyk7CnNsZWVwKDQ4KTsKY3MubW91c2VNb3ZlKDExMjIsNjAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI1LDYwMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEyOCw2MDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzAsNjAwKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTEzMyw2MDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzQsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTM2LDU5OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTEzNyw1OTkpOwpzbGVlcCgxMjQpOwpjcy5tb3VzZU1vdmUoMTEzOSw1OTcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNDAsNTk3KTsKc2xlZXAoMjUpOwpjcy5tb3VzZU1vdmUoMTE0Miw1OTYpOwpzbGVlcCg2NDApOwpjcy5tb3VzZU1vdmUoMTE0Myw1OTQpOwpzbGVlcCg0MTQpOwpjcy5tb3VzZUxlZnRVcCgxMTQzLDU5NCk7CnNsZWVwKDMxMSk7CmNzLm1vdXNlTW92ZSgxMTQxLDU5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzOSw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzgsNTk0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTM1LDU5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzMCw1OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMjYsNTk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTE4LDU5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEwOSw1OTMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwOTAsNTkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDcyLDU4Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA1NSw1ODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjcsNTg0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA5LDU4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgzLDU4MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTYyLDU3OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTQ0LDU3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTI1LDU3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTA3LDU3NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODgzLDU3Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODY5LDU3MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODQxLDU2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODI3LDU2Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzk5LDU2Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzgyLDU2Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNzY3LDU2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzQ4LDU2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzM5LDU2MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzIyLDU2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzE1LDU2MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNzAzLDU2MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjkxLDU2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjgwLDU2MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjcxLDU1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU1LDU1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ2LDU1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjMyLDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjEzLDU1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTk5LDU1Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTg3LDU1MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTY4LDU0OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTU0LDU0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTQxLDU0OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTI3LDU0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTA2LDU0NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDk0LDU0NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDc4LDU0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU1LDU0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQyLDU0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI0LDU0Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDE4LDU0Myk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNDA0LDU0NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzk3LDU0NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzkxLDU0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzg1LDU0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzgwLDU0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzc2LDU0Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzcxLDU0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzY3LDU0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzYxLDU0OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzU1LDU1MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzUwLDU1MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzQ0LDU1MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzM4LDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzM0LDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzI4LDU1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzIyLDU1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzE2LDU1NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzA4LDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjk4LDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjgxLDU1NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjY4LDU1NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMjQ3LDU1NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjM1LDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjE3LDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjA1LDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTk2LDU1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg3LDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTg0LDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTgyLDU1MSk7CnNsZWVwKDk1KTsKY3MubW91c2VNb3ZlKDE4Miw1NTIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE4Myw1NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4NSw1NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4Niw1NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE4OSw1NTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5MSw1NTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5Miw1NTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5Miw1NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5NCw1NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5NSw1NTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDIwMCw1NTgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDIwMyw1NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIwNyw1NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIxMyw1NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIxOCw1NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIyMiw1NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIyNCw1NTgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDIyNSw1NTgpOwpzbGVlcCgxNTcpOwpjcy5tb3VzZU1vdmUoMjI0LDU1OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjI0LDU1OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjIzLDU1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjIzLDU2MSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDIyMyw1NjIpOwpzbGVlcCgyMDIpOwpjcy5tb3VzZUxlZnREb3duKDIyMyw1NjIpOwpzbGVlcCg4NSk7CmNzLm1vdXNlTGVmdFVwKDIyMyw1NjIpOwpzbGVlcCgxMDIpOwpjcy5tb3VzZU1vdmUoMjI1LDU2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjMxLDU2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjM3LDU2NCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMjQ5LDU2NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjYwLDU2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjcwLDU3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjc2LDU3MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjg4LDU3Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjk5LDU3NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzA5LDU3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzE4LDU3Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzM1LDU3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzQ1LDU4MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzY1LDU4Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzgxLDU4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzk5LDU4Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDE0LDU4OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDI4LDU4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQwLDU5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQ5LDU5Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDU4LDU5NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDY4LDU5Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDc3LDU5OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDg4LDYwMSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDk3LDYwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTA3LDYwMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTE2LDYwNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTI1LDYwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTMzLDYwNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTM2LDYwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTM5LDYwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTQwLDYwNCk7CnNsZWVwKDM4KTsKY3MubW91c2VNb3ZlKDU0Miw2MDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU0Myw2MDQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU0Niw2MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU0OSw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1Miw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1NCw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1NSw2MDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1Nyw2MDIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDU1OCw2MDIpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSg1NjAsNjAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjEsNjAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjMsNjAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjYsNjAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjcsNjAyKTsKc2xlZXAoNTMpOwpjcy5tb3VzZU1vdmUoNTY5LDYwMik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDU2OSw2MDApOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg1NzAsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NzIsNTk5KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoNTczLDU5OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTc1LDU5OSk7CnNsZWVwKDE0Mik7CmNzLm1vdXNlTW92ZSg1NzUsNTk3KTsKc2xlZXAoMjIpOwpjcy5tb3VzZU1vdmUoNTc2LDU5Nyk7CnNsZWVwKDMyKTsKY3MubW91c2VNb3ZlKDU3OCw1OTYpOwpzbGVlcCg5NSk7CmNzLm1vdXNlTGVmdERvd24oNTc4LDU5Nik7CnNsZWVwKDY5KTsKY3MubW91c2VMZWZ0VXAoNTc4LDU5Nik7CnNsZWVwKDQzOCk7CmNzLm1vdXNlTW92ZSg1ODIsNTk2KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg1ODgsNTk2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MDAsNTk2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2MDksNTk2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTgsNTk2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MjksNTk2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2MzksNTk2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2NTEsNTkzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2NjMsNTkxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2NjksNTkwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2NzcsNTg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODQsNTg1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2OTAsNTgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTgsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MDcsNTc2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MTcsNTcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MzEsNTY3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NDQsNTYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NTgsNTU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NzAsNTU1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3ODMsNTUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MDMsNTQ2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4MTYsNTQyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4MjgsNTM3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NDYsNTMwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjAsNTI0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4ODQsNTEzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5MDIsNTA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MjcsNDk0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NTMsNDgyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5NzUsNDcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODksNDY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE3LDQ1MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTAzNSw0NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTAsNDM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDc3LDQyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA5MSw0MjApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMTMsNDEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTMwLDQwNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTE1NCwzOTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzAsMzkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTk2LDM4MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIyMywzNjgpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyMzgsMzYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjUzLDM1NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI2OSwzNDcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODYsMzM5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjk5LDMzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwNCwzMzIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzMTYsMzI2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzIzLDMyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMyOCwzMjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMzUsMzE3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzQ0LDMxMik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTM1MiwzMDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNjIsMzAzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzcxLDI5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3OSwyOTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzODYsMjkwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzkxLDI4Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM5NCwyODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzOTUsMjg0KTsKc2xlZXAoMTQwKTsKY3MubW91c2VNb3ZlKDEzOTUsMjgyKTsKc2xlZXAoNDcpOwpjcy5tb3VzZUxlZnREb3duKDEzOTUsMjgyKTsKc2xlZXAoMTEwKTsKY3MubW91c2VMZWZ0VXAoMTM5NSwyODIpOwpzbGVlcCgxNDApOwpjcy5tb3VzZU1vdmUoMTM5NSwyODMpOwpzbGVlcCg1NTgpOwpjcy5rZXlEb3duKDY2KTsKc2xlZXAoMTM1KTsKY3Mua2V5VXAoNjYpOwpzbGVlcCgyNSk7CmNzLmtleURvd24oNzkpOwpzbGVlcCgxMTIpOwpjcy5rZXlVcCg3OSk7CnNsZWVwKDMyKTsKY3Mua2V5RG93big2Nyk7CnNsZWVwKDcxKTsKY3Mua2V5RG93big3Mik7CnNsZWVwKDczKTsKY3Mua2V5VXAoNjcpOwpzbGVlcCgyNCk7CmNzLmtleVVwKDcyKTsKc2xlZXAoMjMpOwpjcy5rZXlEb3duKDY1KTsKc2xlZXAoMTA1KTsKY3Mua2V5VXAoNjUpOwpzbGVlcCgxNSk7CmNzLmtleURvd24oMzIpOwpzbGVlcCg4OSk7CmNzLmtleVVwKDMyKTsKc2xlZXAoNTI2KTsKY3MubW91c2VNb3ZlKDEzOTUsMjgyKTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTM5MywyODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzOTEsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzg4LDI4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM4NywyODEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzODUsMjgxKTsKc2xlZXAoODcpOwpjcy5tb3VzZU1vdmUoMTM4OCwyODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzOTIsMjgxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzk1LDI4Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQwMCwyODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDMsMjkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDA3LDMwMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQxMCwzMDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MTUsMzE5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDE5LDMyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQyMiwzMzkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MjgsMzQ2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDMzLDM1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQzNywzNjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NDIsMzY0KTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSgxNDQ2LDM2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0OCwzNzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NDksMzcwKTsKc2xlZXAoNDkpOwpjcy5tb3VzZU1vdmUoMTQ0OCwzNzApOwpzbGVlcCg1Myk7CmNzLm1vdXNlTW92ZSgxNDQ4LDM3Mik7CnNsZWVwKDEzMyk7CmNzLm1vdXNlTW92ZSgxNDQ3LDM3Myk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDE0NDcsMzc1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDQ3LDM3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0NywzNzgpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxNDQ3LDM3OSk7CnNsZWVwKDEzMik7CmNzLm1vdXNlTW92ZSgxNDQ3LDM4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0NywzODQpOwpzbGVlcCg3MCk7CmNzLm1vdXNlTW92ZSgxNDQ3LDM4NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ0OCwzODcpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxNDQ4LDM4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0OCwzOTApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0NDksMzkwKTsKc2xlZXAoMTg3KTsKY3MubW91c2VMZWZ0RG93bigxNDQ5LDM5MCk7CnNsZWVwKDc4KTsKY3MubW91c2VMZWZ0VXAoMTQ0OSwzOTApOwpzbGVlcCgyODIpOwpjcy5tb3VzZU1vdmUoMTQ0OSwzOTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NDgsMzk5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDQ1LDQwNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ0NCw0MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NDEsNDIxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDM5LDQyOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQzNiw0MzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzMsNDUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDMyLDQ1Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQzMCw0NjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjksNDY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDI3LDQ3Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQyNiw0NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjQsNDgxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDI0LDQ4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQyMSw0OTApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE0MjEsNDk1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDIwLDQ5Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQyMCw0OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MTgsNTAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDE4LDUwNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQxNyw1MDQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0MTcsNTA1KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTQxNSw1MDcpOwpzbGVlcCgxMyk7CmNzLm1vdXNlTW92ZSgxNDE1LDUwOCk7CnNsZWVwKDI1MSk7CmNzLm1vdXNlTGVmdERvd24oMTQxNSw1MDgpOwpzbGVlcCg4Nik7CmNzLm1vdXNlTGVmdFVwKDE0MTUsNTA4KTsKc2xlZXAoMTM0KTsKY3MubW91c2VNb3ZlKDE0MTQsNTEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDExLDUxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQwNiw1MTYpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE0MDIsNTIwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzk2LDUyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM5MCw1MjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzksNTM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzY3LDU0MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM1NSw1NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMzYsNTUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzA3LDU2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OCw1NzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTksNTgwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjI1LDU5NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTE5NSw2MDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNjYsNjIyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTM5LDYzNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExMSw2NDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODEsNjYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDUyLDY3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAyNCw2ODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDcsNjk2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODAsNzA2KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg5NTUsNzE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MjgsNzI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MTAsNzMyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4OTMsNzM5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NzUsNzQ3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjAsNzU0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NDUsNzYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MzksNzY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MjksNzcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MTcsNzc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MTIsNzgxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg4MDIsNzg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3OTAsNzkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NzksNzkzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NjQsNzk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NDYsODA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MzEsODA4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MTksODExKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MDQsODE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTEsODIwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NzksODI1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg2NjcsODI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NTMsODMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NDEsODM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MjksODM3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTcsODQwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MDUsODQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1OTIsODQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODAsODQ3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjYsODQ5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NTMsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NDEsODUwKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg1MjEsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1MDUsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0OTcsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NzksODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NjQsODUwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0NTQsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NDMsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MzQsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MjcsODQ5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0MjEsODQ4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0MTYsODQ4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MTMsODQ2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0MTAsODQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MDksODQ1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0MDQsODQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MDMsODQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MDAsODQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzOTgsODQzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzOTcsODQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzOTUsODQzKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMzk0LDg0Myk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDM5Miw4NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM5MSw4NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM4OCw4NDMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM4NSw4NDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM3OSw4NDQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDM3Niw4NDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM2OCw4NDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM2NCw4NDQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDM1Niw4NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM0Nyw4NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM0Myw4NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMzNyw4MzkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDMzMiw4MzYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDMyNiw4MzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMyMCw4MzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMxNCw4MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMwOCw4MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMwNCw4MTkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDI5OSw4MTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5OCw4MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5Niw4MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5Niw4MDkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDI5Niw4MDYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDI5Niw4MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5Niw4MDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5Nyw3OTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDI5Nyw3OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5OSw3ODkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDI5OSw3ODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5OSw3ODYpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgzMDAsNzg1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzMDIsNzgzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMDMsNzgyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzMDYsNzgwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMDgsNzc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMDksNzc3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzMTEsNzc2KTsKc2xlZXAoMTMpOwpjcy5tb3VzZU1vdmUoMzEyLDc3Nik7CnNsZWVwKDI1KTsKY3MubW91c2VNb3ZlKDMxMiw3NzQpOwpzbGVlcCgzMik7CmNzLm1vdXNlTW92ZSgzMTUsNzc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMTgsNzczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMjEsNzcxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgzMjQsNzY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMjcsNzY3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMjcsNzY1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzMjksNzY0KTsKc2xlZXAoMzMpOwpjcy5tb3VzZU1vdmUoMzI5LDc2Mik7CnNsZWVwKDMwKTsKY3MubW91c2VNb3ZlKDMyOCw3NjIpOwpzbGVlcCg2OSk7CmNzLm1vdXNlTW92ZSgzMjYsNzYyKTsKc2xlZXAoNTYpOwpjcy5tb3VzZU1vdmUoMzI1LDc2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzI1LDc2Myk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDMyMyw3NjUpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDMyMyw3NjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMyMiw3NjgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDMyMiw3NjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMyMCw3NzEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDMyMCw3NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMyMCw3NzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMyMCw3NzUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDMyMCw3NzcpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgzMjAsNzc4KTsKc2xlZXAoNTcxKTsKY3MubW91c2VNb3ZlKDMyMCw3NzcpOwpzbGVlcCg1NCk7CmNzLm1vdXNlTW92ZSgzMjEsNzc2KTsKc2xlZXAoOTQpOwpjcy5tb3VzZU1vdmUoMzIxLDc3NCk7CnNsZWVwKDIyKTsKY3MubW91c2VNb3ZlKDMyMyw3NzQpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgzMjMsNzczKTsKc2xlZXAoMjkwKTsKY3MubW91c2VMZWZ0RG93bigzMjMsNzczKTsKc2xlZXAoMTA3KTsKY3MubW91c2VNb3ZlKDMyNCw3NzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMyOSw3NzApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDMzMiw3NjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMzNiw3NjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMzOSw3NjEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM0Miw3NTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM0Nyw3NTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM1Myw3NTApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDM2Miw3NDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM2OSw3NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM4Myw3MzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM5OCw3MjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQxNCw3MjApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDQyOSw3MTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDQzOCw3MDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ1Myw3MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ2MSw2OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ3MCw2ODkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDQ3OSw2ODMpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDQ4NSw2NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ5Miw2NzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ5NSw2NjgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDUwMSw2NjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDUwNCw2NTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUxMCw2NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUxNSw2NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUxOSw2NDUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDUyNCw2NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUyNyw2MzgpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDUzMCw2MzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUzMSw2MzUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDUzMyw2MzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDUzNCw2MzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUzNiw2MjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUzOSw2MjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU0MCw2MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU0Myw2MjEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU0NSw2MTgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU0OCw2MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1MSw2MTIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU1NCw2MDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU1NSw2MDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1OCw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU2MSw2MDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU2NCw1OTYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU2Nyw1OTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU3MCw1ODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU3Miw1ODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU3NSw1ODIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU3Niw1NzgpOwpzbGVlcCgxMyk7CmNzLm1vdXNlTW92ZSg1NzgsNTc2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NzksNTc1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NzksNTczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODEsNTczKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg1ODIsNTczKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg1ODQsNTcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODUsNTcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODcsNTcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1OTAsNTY5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1OTEsNTY3KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoNTkzLDU2Nik7CnNsZWVwKDQ4KTsKY3MubW91c2VNb3ZlKDU5NCw1NjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU5Nyw1NjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYwNSw1NjApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDYxMiw1NTcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYyMSw1NTIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYzNSw1NDYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY0Miw1NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY1Niw1MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY2Myw1MzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3MSw1MjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3NSw1MjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDY3OCw1MjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3OCw1MTkpOwpzbGVlcCgyNjQpOwpjcy5tb3VzZU1vdmUoNjc4LDUxNik7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDY3OCw1MTUpOwpzbGVlcCgxMDIpOwpjcy5tb3VzZU1vdmUoNjgwLDUxNSk7CnNsZWVwKDM4KTsKY3MubW91c2VNb3ZlKDY4MCw1MTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY4MCw1MTIpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg2ODEsNTEwKTsKc2xlZXAoMTEwKTsKY3MubW91c2VNb3ZlKDY4MSw1MDkpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDY4Myw1MDYpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg2ODQsNTAzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODYsNTAxKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoNjg2LDUwMCk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDY4Niw0OTgpOwpzbGVlcCg1MjIpOwpjcy5tb3VzZU1vdmUoNjg2LDQ5OSk7CnNsZWVwKDE1Nyk7CmNzLm1vdXNlTW92ZSg2ODUsNTAyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2ODMsNTAyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2ODMsNTA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2ODIsNTA1KTsKc2xlZXAoMjY2KTsKY3MubW91c2VMZWZ0VXAoNjgyLDUwNSk7CnNsZWVwKDkzKTsKY3MubW91c2VNb3ZlKDY4Miw1MDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3OSw1MTApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDY3NCw1MTYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY3MSw1MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY2Nyw1MjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDY2NCw1MzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY1OCw1NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY1Myw1NTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY0Nyw1NzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY0Myw1ODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzOCw1OTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYzNyw2MDMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYzMiw2MTUpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDYzMSw2MjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyOCw2MzApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYyNiw2MzcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYyNSw2NDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyMyw2NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyMyw2NjEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYyMiw2NjkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYyMiw2NzYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYyMCw2ODIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYyMCw2ODgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYxOSw2OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNyw2OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNyw3MDMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYxNyw3MDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MTcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYxNiw3MjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MjkpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDYxNiw3MzApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYxNiw3MzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYxNiw3MzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNiw3MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNyw3NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNyw3NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNyw3NDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNyw3NDcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDYxNyw3NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNyw3NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxOCw3NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxOCw3NTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxOCw3NjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxOCw3NjIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYxOCw3NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxOCw3NjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxOCw3NjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxOCw3NjkpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSg2MTgsNzcyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2MTgsNzc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTgsNzc1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTgsNzc3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MTgsNzc4KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoNjE4LDc4MCk7CnNsZWVwKDU1KTsKY3MubW91c2VNb3ZlKDYyMCw3ODApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyMSw3ODApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyNCw3NzkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYyNyw3NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyOSw3NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzMiw3NzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzMyw3NzQpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSg2MzUsNzc0KTsKc2xlZXAoMjUwKTsKY3MubW91c2VNb3ZlKDYzNCw3NzQpOwpzbGVlcCgyNSk7CmNzLm1vdXNlTW92ZSg2MzIsNzc0KTsKc2xlZXAoMTI1KTsKY3MubW91c2VNb3ZlKDYzMSw3NzMpOwpzbGVlcCgxOTUpOwpjcy5tb3VzZUxlZnREb3duKDYzMSw3NzMpOwpzbGVlcCg2OSk7CmNzLm1vdXNlTW92ZSg2MzMsNzczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MzgsNzcxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NDUsNzY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NTEsNzY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjYsNzYxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2ODAsNzU1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3MDEsNzQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MTcsNzM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MzIsNzMyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg3NTksNzIyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NzEsNzE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3OTQsNzEwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4MTIsNzA0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4MjQsNjk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MzksNjkzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NDYsNjkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjMsNjg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NzMsNjgzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4ODIsNjc4KTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg4OTMsNjc1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MDIsNjc0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5MDgsNjcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MTUsNjY5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MjEsNjY4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5MjYsNjY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MzAsNjY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MzMsNjY1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5MzgsNjYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NDEsNjYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NDUsNjYyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NDgsNjYwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NTQsNjU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NTksNjU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NjUsNjU2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NzEsNjU0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5NzUsNjUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODQsNjUxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5OTAsNjQ4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5OTYsNjQ4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDA0LDY0Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTAxMyw2NDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjAsNjQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI2LDY0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzMSw2NDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzcsNjQyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDQ0LDYzOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA0OSw2MzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTIsNjM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDU1LDYzNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA1Niw2MzUpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSgxMDU4LDYzMyk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEwNTksNjMyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDYxLDYzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA2NCw2MjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNjcsNjI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDcwLDYyNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA3Myw2MjEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwNzcsNjE4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDgwLDYxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA4NSw2MTIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwODksNjA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDkyLDYwNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA5Nyw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDEsNTk5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTA2LDU5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEwOSw1OTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMTIsNTkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTE1LDU4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExNiw1ODcpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSgxMTE2LDU4NSk7CnNsZWVwKDE1Nyk7CmNzLm1vdXNlTW92ZSgxMTE4LDU4NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEyMSw1ODUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMjQsNTg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI3LDU4NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEzMCw1ODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzEsNTg1KTsKc2xlZXAoNzgpOwpjcy5tb3VzZU1vdmUoMTEzMyw1ODUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzQsNTg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTM2LDU4NSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDExMzcsNTg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTM5LDU4NSk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDExNDAsNTg1KTsKc2xlZXAoOTUpOwpjcy5tb3VzZU1vdmUoMTE0MCw1ODYpOwpzbGVlcCg2MSk7CmNzLm1vdXNlTW92ZSgxMTQyLDU4Nik7CnNsZWVwKDM1Myk7CmNzLm1vdXNlTGVmdFVwKDExNDIsNTg2KTsKc2xlZXAoMjA0KTsKY3MubW91c2VNb3ZlKDExNDEsNTg4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMTM5LDU4OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEzOCw1ODkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzUsNTkxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTMyLDU5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyNyw1OTQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMjMsNTk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTE3LDU5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExMiw2MDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDUsNjAzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDk3LDYwNCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTA4OCw2MDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNzksNjA5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDYzLDYxMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA1MSw2MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzEsNjEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA0LDYxMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTg2LDYxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTQ3LDYxNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTE5LDYxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODg2LDYyMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODU5LDYyNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODI5LDYzMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODAwLDYzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzY0LDYzNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzM2LDYzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzA5LDY0Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjc2LDY0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ5LDY0NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjIwLDY0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTkyLDY0Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTY5LDY0Nik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNTUwLDY0OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTI5LDY0OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTE1LDY0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTA1LDY0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDk0LDY0OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDkwLDY0OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDgyLDY0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDc1LDY0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDY5LDY0OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDYxLDY0OCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNDU0LDY0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQzLDY0NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDM0LDY0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI3LDY0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDE4LDY0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDEyLDY0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDAzLDY0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzg5LDY0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzc5LDY0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzY1LDYzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzUzLDYzOSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzM4LDYzOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzI2LDYzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzExLDYzOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjk4LDYzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjkwLDYzNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjgzLDYzNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjc4LDYzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjc1LDYzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjcyLDYzMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjcxLDYyOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjY5LDYyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjY2LDYyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjYzLDYyMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjYwLDYxNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjU3LDYxNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjU0LDYxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjUxLDYwOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjUwLDYwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjQ4LDYwNSk7CnNsZWVwKDMwKTsKY3MubW91c2VNb3ZlKDI0Nyw2MDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI0Nyw2MDMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDI0NSw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI0NCw2MDIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDIzOSw1OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIzOCw1OTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIzNSw1OTYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDIzMiw1OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIzMCw1OTMpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDIyOSw1OTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIyNyw1OTEpOwpzbGVlcCg0MCk7CmNzLm1vdXNlTW92ZSgyMjcsNTkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMjcsNTg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMjcsNTg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMjcsNTg0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMjcsNTgxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyMjcsNTc5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyMjcsNTc4KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMjI3LDU3Nik7CnNsZWVwKDMyKTsKY3MubW91c2VNb3ZlKDIyOCw1NzUpOwpzbGVlcCgyMyk7CmNzLm1vdXNlTW92ZSgyMjgsNTczKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMjI4LDU3Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjI4LDU3MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjI4LDU2OSk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDIyOCw1NjcpOwpzbGVlcCg2Mik7CmNzLm1vdXNlTW92ZSgyMjgsNTY2KTsKc2xlZXAoNDY4KTsKY3MubW91c2VMZWZ0RG93bigyMjgsNTY2KTsKc2xlZXAoMTI2KTsKY3MubW91c2VMZWZ0VXAoMjI4LDU2Nik7CnNsZWVwKDI4OCk7CmNzLm1vdXNlTW92ZSgyMzMsNTY2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyMzYsNTY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNDAsNTY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNDUsNTY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNDgsNTY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNTIsNTY2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgyNTgsNTY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNjcsNTY3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyODEsNTY4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgyOTQsNTcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMTgsNTczKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzMzksNTczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzNjgsNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzODQsNTc0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0MTEsNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0MzEsNTc0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg0NDcsNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NTksNTc0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg0NzAsNTc0KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg0NzYsNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NzcsNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0NzksNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg0ODAsNTc0KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoNDgzLDU3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg1LDU3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDg4LDU3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg5LDU3Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDkxLDU3Nyk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNDkyLDU3Nyk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDQ5NCw1NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ5NSw1NzcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDUwMCw1NzcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDUwNCw1NzkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDUxMCw1NzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUxNSw1ODApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUxOSw1ODIpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDUyNSw1ODIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDUzMCw1ODMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDUzMyw1ODUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDUzNCw1ODYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDUzNyw1ODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU0MCw1ODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU0Myw1ODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU0OCw1ODkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU1MSw1ODkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU1NCw1ODkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU1Nyw1ODkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU1OCw1ODkpOwpzbGVlcCg3Mik7CmNzLm1vdXNlTW92ZSg1NjAsNTg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjMsNTg5KTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg1NjQsNTg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1NjYsNTg5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg1NjksNTg5KTsKc2xlZXAoNDgpOwpjcy5tb3VzZU1vdmUoNTY5LDU5MSk7CnNsZWVwKDEzKTsKY3MubW91c2VNb3ZlKDU3MCw1OTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU3MCw1OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU3Miw1OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU3Miw1OTQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU3Myw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU3Myw1OTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU3NSw1OTUpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg1NzUsNTk3KTsKc2xlZXAoNjIpOwpjcy5tb3VzZU1vdmUoNTc2LDU5Nyk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDU3OCw1OTgpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSg1NzksNjAwKTsKc2xlZXAoMTY0KTsKY3MubW91c2VNb3ZlKDU4MSw2MDApOwpzbGVlcCg0Nik7CmNzLm1vdXNlTGVmdERvd24oNTgxLDYwMCk7CnNsZWVwKDc3KTsKY3MubW91c2VMZWZ0VXAoNTgxLDYwMCk7CnNsZWVwKDE0OCk7CmNzLm1vdXNlTW92ZSg1ODIsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODQsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1ODcsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1OTAsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg1OTQsNTk5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MDAsNTk5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2MDksNTk5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2MTUsNTk3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg2MjcsNTk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NDIsNTk2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NTEsNTk2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2NjgsNTk2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg2ODQsNTk0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg2OTIsNTk0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3MDUsNTk0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MTQsNTkzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MjAsNTkzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3MjgsNTkxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3MzcsNTkwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3NTAsNTg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg3NTgsNTg1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg3NzMsNTgyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg3OTIsNTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MTAsNTczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4MjcsNTY5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NDYsNTY0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NjksNTU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4ODEsNTU1KTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg4OTYsNTUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5MTQsNTQ2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5MjksNTQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NDEsNTM5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NTksNTM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODQsNTI1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA3LDUxOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAzMiw1MTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTAsNTAzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDc3LDQ5Mik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTA5NSw0ODYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMTUsNDc3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTMzLDQ3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE0Niw0NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNjMsNDYxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTc2LDQ1Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTE5Niw0NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDksNDQ0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjI3LDQzOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIzOSw0MzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNTMsNDI2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjY4LDQxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3NSw0MTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODQsNDEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjk2LDQwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwMSwzOTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMDgsMzk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzE0LDM5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMyMCwzODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMjYsMzgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzMyLDM3NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM0MCwzNjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNDYsMzYwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzUyLDM1Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM1NiwzNTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNTksMzQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzYyLDM0MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM2NSwzMzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNjcsMzM1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzY4LDMzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MCwzMzIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzNzEsMzI5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzczLDMyNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM3NCwzMjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzYsMzIzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzc2LDMyMSk7CnNsZWVwKDQ2KTsKY3MubW91c2VNb3ZlKDEzNzYsMzIwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzczLDMxOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM2OSwzMTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNjMsMzExKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzU3LDMwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM1MiwzMDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNDgsMjk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzQ2LDI5Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM0NSwyOTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNDUsMjk0KTsKc2xlZXAoMTQ3KTsKY3MubW91c2VMZWZ0RG93bigxMzQ1LDI5NCk7CnNsZWVwKDYzKTsKY3MubW91c2VMZWZ0VXAoMTM0NSwyOTQpOwpzbGVlcCgxNzkpOwpjcy5tb3VzZU1vdmUoMTM0MywyOTQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNDIsMjk1KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTM0MCwyOTUpOwpzbGVlcCg0OTgpOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoOTUpOwpjcy5rZXlEb3duKDc5KTsKc2xlZXAoMjUpOwpjcy5rZXlVcCg4Myk7CnNsZWVwKDQ3KTsKY3Mua2V5RG93big4NSk7CnNsZWVwKDgwKTsKY3Mua2V5VXAoNzkpOwpzbGVlcCgxNik7CmNzLmtleURvd24oNzEpOwpzbGVlcCgyNSk7CmNzLmtleVVwKDg1KTsKc2xlZXAoOTYpOwpjcy5rZXlEb3duKDc5KTsKc2xlZXAoMjMpOwpjcy5rZXlVcCg3MSk7CnNsZWVwKDE3KTsKY3Mua2V5RG93big3Nik7CnNsZWVwKDI0KTsKY3Mua2V5RG93big4NSk7CnNsZWVwKDE2KTsKY3Mua2V5VXAoNzYpOwpzbGVlcCgyMyk7CmNzLmtleVVwKDc5KTsKc2xlZXAoNTcpOwpjcy5rZXlVcCg4NSk7CnNsZWVwKDYyNCk7CmNzLmtleURvd24oMzIpOwpzbGVlcCgxMDQpOwpjcy5rZXlVcCgzMik7CnNsZWVwKDU4MCk7CmNzLm1vdXNlTW92ZSgxMzQ2LDI5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM1MiwzMDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNTgsMzA3KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzY1LDMxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MywzMTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzODIsMzIyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzk0LDMzMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQwMywzMzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDcsMzQyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDE5LDM1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQyNywzNjQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0MzMsMzcyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDQwLDM3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0MywzODQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0NDYsMzg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDQ4LDM5MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ0OSwzOTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NTEsMzk0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDUxLDM5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ1MiwzOTcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0NTIsMzk5KTsKc2xlZXAoMTI0KTsKY3MubW91c2VNb3ZlKDE0NTIsMzk4KTsKc2xlZXAoMzQ0KTsKY3MubW91c2VMZWZ0RG93bigxNDUyLDM5OCk7CnNsZWVwKDEwMyk7CmNzLm1vdXNlTGVmdFVwKDE0NTIsMzk4KTsKc2xlZXAoMjcyKTsKY3MubW91c2VNb3ZlKDE0NTIsNDAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDUyLDQwMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ1MSw0MDgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0NTAsNDEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDQ4LDQxOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ0Nyw0MjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NDUsNDMyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDQ0LDQzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0Miw0NDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzksNDUwKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSgxNDM4LDQ1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQzNiw0NTkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0MzUsNDY1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDMyLDQ2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQzMiw0NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzAsNDc3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDI5LDQ4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQyNyw0ODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjYsNDg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDI2LDQ4OSk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTQyNCw0OTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0MjMsNDk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDIxLDQ5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQyMCw1MDEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MTgsNTA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDE3LDUwNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQxNSw1MTApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0MTUsNTExKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTQxNCw1MTEpOwpzbGVlcCg4Nyk7CmNzLm1vdXNlTW92ZSgxNDE0LDUxMyk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDE0MTQsNTE0KTsKc2xlZXAoMTQyKTsKY3MubW91c2VMZWZ0RG93bigxNDE0LDUxNCk7CnNsZWVwKDU0KTsKY3MubW91c2VNb3ZlKDE0MTQsNTEzKTsKc2xlZXAoMjMpOwpjcy5tb3VzZUxlZnRVcCgxNDE0LDUxMyk7CnNsZWVwKDM4KTsKY3MubW91c2VNb3ZlKDE0MTQsNTE0KTsKc2xlZXAoMzIpOwpjcy5tb3VzZU1vdmUoMTQxMSw1MTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDMsNTE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzk3LDUyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM4NCw1MjUpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzNjcsNTMyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzUxLDUzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMzMyw1NDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMTUsNTUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg4LDU1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1OSw1NjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjksNTc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTkwLDU4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE1OSw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzAsNjAxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDk0LDYxMik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTA2Myw2MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzYsNjMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDEwLDYzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgzLDY0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTU5LDY1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTM4LDY2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTE0LDY2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODk4LDY3Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODcxLDY4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODUzLDY4Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODM4LDY5MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODE3LDY5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODA1LDcwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzk0LDcwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzgyLDcxNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzY2LDcxOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzU0LDcyMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzQ1LDcyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzI4LDcyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzE5LDcyNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNzAzLDczMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjg2LDczMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjczLDczNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY0LDczOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ5LDc0NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjM4LDc0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjI1LDc1MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjE0LDc1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjA0LDc1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTk1LDc1OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTg0LDc2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTc1LDc2NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTYyLDc2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTUxLDc3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTQxLDc3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTMzLDc3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTIzLDc4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTExLDc4NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTAzLDc4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDkzLDc4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg1LDc5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDc1LDc5NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDY3LDc5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDUyLDc5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQzLDgwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDMzLDgwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI0LDgwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDE1LDgwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDA5LDgwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDAzLDgwNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzk4LDgwNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzk1LDgwNCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzkyLDgwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzkxLDgwNCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDM4OCw4MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM4NSw4MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM4Myw4MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM4MCw4MDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM3Nyw4MDUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDM3Niw4MDcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM3Myw4MDgpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDM3MCw4MTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM2Nyw4MTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDM2NCw4MTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM2Miw4MTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM1OSw4MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM1Niw4MTkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM1NSw4MTkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDM1Myw4MTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM1Miw4MjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM0OSw4MjApOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDM0Nyw4MjApOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgzNDQsODIwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzNDEsODE5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzMzgsODE4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgzMzQsODE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMjksODEzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgzMjUsODEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMTksODEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMTQsODA5KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgzMTEsODA3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgzMTEsODA2KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMzExLDgwNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzExLDgwMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzEwLDgwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzEwLDgwMCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDMxMCw3OTgpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgzMTAsNzk3KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMzEwLDc5NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzExLDc5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzEyLDc5NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzE1LDc5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzE3LDc5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzE4LDc4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzIxLDc4OCk7CnNsZWVwKDU1KTsKY3MubW91c2VNb3ZlKDMyMSw3ODYpOwpzbGVlcCg1NCk7CmNzLm1vdXNlTW92ZSgzMjMsNzg2KTsKc2xlZXAoNzEpOwpjcy5tb3VzZU1vdmUoMzIzLDc4NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzIzLDc4Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzI0LDc4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzI0LDc4Mik7CnNsZWVwKDc5KTsKY3MubW91c2VNb3ZlKDMyNiw3ODIpOwpzbGVlcCg0NTQpOwpjcy5tb3VzZU1vdmUoMzI3LDc4Mik7CnNsZWVwKDIyKTsKY3MubW91c2VNb3ZlKDMyNyw3ODMpOwpzbGVlcCgyMjApOwpjcy5tb3VzZUxlZnREb3duKDMyNyw3ODMpOwpzbGVlcCgxMTYpOwpjcy5tb3VzZU1vdmUoMzMwLDc4Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzM1LDc3OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzM5LDc3NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzQ1LDc3MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzUwLDc2NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzU0LDc2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzYzLDc1NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzY5LDc1MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzc0LDc0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzgwLDc0MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMzg2LDczNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzkyLDczMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzk4LDcyOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDA1LDcyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDE2LDcxNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDIyLDcxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDMxLDcwNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDQzLDcwMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDUwLDY5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU2LDY5Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDYyLDY4OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDY3LDY4Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDcxLDY4NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDc2LDY4MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDc5LDY4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDgyLDY3Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg2LDY3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDkxLDY3MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDk1LDY2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTAxLDY2Myk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTA5LDY1Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTEyLDY1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTE2LDY1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTIyLDY0NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTI1LDY0NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTI4LDY0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTMwLDYzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTMxLDYzNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTM0LDYzNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTM2LDYzMyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNTM3LDYzMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTQyLDYyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTQ2LDYyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTUxLDYyMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTU3LDYxNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTYxLDYxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTY2LDYwOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTY5LDYwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTcyLDYwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTc1LDYwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTc2LDYwMCk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDU3OCw1OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU3OSw1OTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4Miw1OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4NCw1OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4Nyw1OTApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU5MCw1ODcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU5NCw1ODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU5Nyw1ODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYwMCw1NzkpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDYwMyw1NzYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYwNiw1NzUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYxMSw1NzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYxNSw1NjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyMSw1NjQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYyOSw1NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzNiw1NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY0NCw1NDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDY1MSw1NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY1Nyw1MzYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY2Myw1MzEpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDY2OSw1MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3NCw1MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3OCw1MTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY4MCw1MTYpOwpzbGVlcCgzMzYpOwpjcy5tb3VzZU1vdmUoNjgwLDUxNSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDY4MSw1MTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY4MSw1MTIpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg2ODEsNTEwKTsKc2xlZXAoNzgpOwpjcy5tb3VzZU1vdmUoNjgxLDUwOSk7CnNsZWVwKDM2Nik7CmNzLm1vdXNlTGVmdFVwKDY4MSw1MDkpOwpzbGVlcCg3MCk7CmNzLm1vdXNlTW92ZSg2ODEsNTEwKTsKc2xlZXAoMzEpOwpjcy5tb3VzZU1vdmUoNjgxLDUxMSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjgxLDUxNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjgwLDUxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjc5LDUyMyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjc3LDUyOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjc3LDUzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjc2LDUzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjc0LDU0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjczLDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjcxLDU1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjcwLDU2NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY4LDU3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY4LDU3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY3LDU4NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjY1LDU5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY1LDU5Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjY0LDYwMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjY0LDYwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjYyLDYwNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjYyLDYwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjYxLDYxMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjYxLDYxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU5LDYxNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjU5LDYxOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjU5LDYyMSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjU4LDYyNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU4LDYyOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjU2LDYzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU2LDYzNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjU1LDYzOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjU1LDY0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjUzLDY0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjUyLDY1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjUyLDY1OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjUwLDY2MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjUwLDY2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjUwLDY2OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjQ5LDY3Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjQ5LDY3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ5LDY3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ5LDY4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ3LDY4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ3LDY4OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjQ3LDY5Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjQ3LDY5Nik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjQ2LDY5OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjQ2LDcwMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjQ2LDcwOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ0LDcxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ0LDcxOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjQzLDcyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQzLDcyOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjQzLDczMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQxLDczNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQxLDc0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDc0Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjQwLDc1Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjQwLDc1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDc1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDc1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDc2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDc2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQwLDc2Myk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDY0MCw3NjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY0MCw3NjgpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDY0MCw3NjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY0MCw3NzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzOCw3NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzOCw3NzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzOCw3NzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzOCw3NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzNyw3NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzNyw3ODApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYzNyw3ODEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYzNyw3ODMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDYzNyw3ODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzNyw3ODYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYzNSw3ODcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYzNSw3ODkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzNCw3OTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzMiw3OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzMiw3OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzMiw3OTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYzMSw3OTYpOwpzbGVlcCgyMTApOwpjcy5tb3VzZU1vdmUoNjMxLDc5NSk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDYzMSw3OTQpOwpzbGVlcCgxMDIpOwpjcy5tb3VzZU1vdmUoNjMyLDc5Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjMyLDc5MSk7CnNsZWVwKDEzKTsKY3MubW91c2VNb3ZlKDYzMyw3ODkpOwpzbGVlcCgzNDQpOwpjcy5tb3VzZUxlZnREb3duKDYzMyw3ODkpOwpzbGVlcCgxMDEpOwpjcy5tb3VzZU1vdmUoNjMzLDc4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjM4LDc4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQxLDc4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjQ1LDc4Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjU0LDc3OSk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNjU5LDc3Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjcyLDc3Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjg5LDc2NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNzA1LDc2MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzE0LDc1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzM1LDc1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzQ5LDc0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzU4LDc0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzc0LDczOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzgzLDczNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoODAwLDczMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODA2LDcyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODE2LDcyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODI1LDcyMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoODMzLDcyMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODQzLDcxNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODQ2LDcxNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODU1LDcxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODYxLDcxMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODcyLDcwNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoODgxLDcwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODg4LDcwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODk5LDY5OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTA5LDY5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTIzLDY5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTMzLDY4Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTQ4LDY4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTU3LDY4MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTY4LDY3NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTc3LDY3Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTg2LDY2OSk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoOTk1LDY2NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAwNCw2NjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTEsNjU5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDE5LDY1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyNiw2NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzIsNjUxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM4LDY1MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0NCw2NDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDksNjQ3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDUzLDY0NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1OCw2NDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwNTksNjQ0KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTA1OSw2NDIpOwpzbGVlcCgzMSk7CmNzLm1vdXNlTW92ZSgxMDYyLDY0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA2NSw2MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNzAsNjM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDc2LDYzNSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTA4MCw2MzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODUsNjMwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDg5LDYyOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA5NCw2MjcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwOTUsNjI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDk3LDYyNik7CnNsZWVwKDMzKTsKY3MubW91c2VNb3ZlKDEwOTgsNjI2KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDk4LDYyNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEwMSw2MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDMsNjIxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTA2LDYyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEwOSw2MTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMTAsNjE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTEyLDYxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExMyw2MTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMTUsNjE0KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTExNiw2MTQpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMTE2LDYxMik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDExMTgsNjEyKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTExOSw2MTEpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMTIxLDYxMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEyMSw2MDkpOwpzbGVlcCgzMCk7CmNzLm1vdXNlTW92ZSgxMTIyLDYwOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTEyNCw2MDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMjQsNjA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI1LDYwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyNyw2MDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMjcsNjAzKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTEyNyw2MDIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMjgsNjAyKTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTEzMCw2MDApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMzEsNjAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTMxLDU5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzMyw1OTcpOwpzbGVlcCg4Nyk7CmNzLm1vdXNlTW92ZSgxMTM2LDU5Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTEzNiw1OTQpOwpzbGVlcCg0KTsKY3MubW91c2VNb3ZlKDExMzcsNTk0KTsKc2xlZXAoMTApOwpjcy5tb3VzZU1vdmUoMTEzOSw1OTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMzksNTkxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTQwLDU5MSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDExNDIsNTkwKTsKc2xlZXAoMzYxKTsKY3MubW91c2VMZWZ0VXAoMTE0Miw1OTApOwpzbGVlcCgxNzQ4KTsKY3MubW91c2VNb3ZlKDExNDMsNTg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTQ2LDU4NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE1Miw1NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNTUsNTczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTYwLDU2Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTE2Niw1NjApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNjksNTU1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTczLDU0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3OCw1NDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODIsNTM5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTg4LDUzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE5Myw1MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTksNTIxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjA2LDUxMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIxMSw1MTApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMTgsNTAzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjI5LDQ5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIzOCw0ODkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNTAsNDgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjU3LDQ3OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI3Miw0NzApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyODcsNDYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzA0LDQ1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMxOSw0NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMjgsNDM4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzQ2LDQyOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM2NCw0MTkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNzcsNDExKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzk3LDM5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQwOSwzOTIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE0MjcsMzgzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDQzLDM3Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQ2MSwzNjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0NzgsMzUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDkxLDM0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUxNCwzMjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MjYsMzIxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTQ0LDMwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1NiwzMDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NjYsMjk2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTc4LDI4Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTU4NCwyODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1OTMsMjc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTk5LDI3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTYwNSwyNzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MDgsMjcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjExLDI3MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTYxNCwyNjcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2MTksMjYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjIzLDI1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTYyOCwyNTQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE2MzUsMjQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjM4LDI0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY0NCwyMzcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2NTAsMjMzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjU1LDIyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY1OSwyMjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NjUsMjIxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjY4LDIxNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY3MSwyMTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2NzQsMjEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjc3LDIwNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTY4MiwyMDMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE2ODYsMTk4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjkyLDE5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY5OCwxODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MDEsMTgzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzA2LDE3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcxMiwxNzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MTYsMTcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzIxLDE2NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcyNywxNTkpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE3MzEsMTU2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzMzLDE1Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTczNiwxNTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MzcsMTUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzM3LDE0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTczOSwxNDkpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxNzQwLDE0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc0MiwxNDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NDMsMTQzKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNzQ1LDE0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc0NiwxMzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE3NDksMTM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzUyLDEzMSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTc1NCwxMjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NTQsMTI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzU1LDEyNik7CnNsZWVwKDIxNyk7CmNzLm1vdXNlTW92ZSgxNzU0LDEyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc1MSwxMjYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE3NDgsMTI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzQ1LDEyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc0NCwxMjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NDIsMTI2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzM5LDEyNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTczOCwxMjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MzUsMTI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzMyLDEyNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTcyOSwxMjYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE3MjQsMTI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzIwLDEyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcxNywxMjcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE3MTEsMTI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzAwLDEyNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY5MSwxMjcpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDE2ODQsMTI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjc2LDEyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY2OSwxMjcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2NjMsMTI3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjU4LDEyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY1NSwxMjcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2NTIsMTI3KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTY1MSwxMjcpOwpzbGVlcCgzOSk7CmNzLm1vdXNlTW92ZSgxNjQ5LDEyNyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDE2NDYsMTI3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjQzLDEyNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY0MiwxMjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NDAsMTI3KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTYzOSwxMjcpOwpzbGVlcCg2Mik7CmNzLm1vdXNlTW92ZSgxNjM5LDEyOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTYzOSwxMzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MzksMTMzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjQwLDEzNik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTY0MywxMzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NDYsMTQyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjQ3LDE0NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY0OSwxNDcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE2NTAsMTQ4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjUzLDE1MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY1OCwxNTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE2NjQsMTU5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjY4LDE2Myk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTY3NCwxNjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NzksMTY5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjgyLDE3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY4NSwxNzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2ODYsMTc1KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTY4OCwxNzUpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxNjg5LDE3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY5NSwxNzcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE3MDEsMTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzA3LDE4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcxMywxODApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE3MjEsMTgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzI3LDE4Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTczMSwxODQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE3MzYsMTg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzM5LDE4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc0MiwxODcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NDMsMTg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzQ1LDE4OSk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDE3NDYsMTg5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzQ4LDE4OSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDE3NDksMTg5KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTc1MSwxODkpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxNzUyLDE4OSk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDE3NTQsMTg5KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTc1NSwxODkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE3NTcsMTg5KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTc1OCwxODkpOwpzbGVlcCgzNyk7CmNzLm1vdXNlTW92ZSgxNzYxLDE4OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTc2NCwxODkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NjcsMTg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzcwLDE4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc3MywxODkpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxNzc1LDE4OSk7CnNsZWVwKDQ2KTsKY3MubW91c2VNb3ZlKDE3NzYsMTkwKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTc3OCwxOTIpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxNzc4LDE5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc3OSwxOTMpOwpzbGVlcCgzMCk7CmNzLm1vdXNlTW92ZSgxNzgxLDE5NSk7CnNsZWVwKDM5KTsKY3MubW91c2VNb3ZlKDE3ODIsMTk2KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTc4MiwxOTgpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDE3ODQsMTk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzg0LDE5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc4NSwxOTkpOwpzbGVlcCgyNTApOwpjcy5tb3VzZUxlZnREb3duKDE3ODUsMTk5KTsKc2xlZXAoNzkpOwpjcy5tb3VzZUxlZnRVcCgxNzg1LDE5OSk7CnNsZWVwKDQzOCk7CmNzLm1vdXNlTW92ZSgxNzg0LDE5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc4MSwxOTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NzgsMjAxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzc0LDIwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc2OSwyMDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NjUsMjAyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNzYwLDIwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTc1NCwyMDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3NDcsMjA4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzM4LDIxMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTcyOSwyMTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE3MTgsMjE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzAzLDIyMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY4NCwyMjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NjYsMjMyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjU3LDIzNCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTYzMywyNDEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2MjIsMjQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjA0LDI0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU4MywyNTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NjQsMjYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTUwLDI2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUyMywyNzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MDUsMjgwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDkwLDI4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ2MiwyOTIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE0NDgsMjk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDIwLDMwMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM5NywzMDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzODQsMzA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzU4LDMwOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM0NiwzMTIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzMjcsMzEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzEyLDMxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5NSwzMTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODAsMzE1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjczLDMxNSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI1NiwzMTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNDcsMzE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjM1LDMxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIyNSwzMTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTEsMzE0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjAyLDMxMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE5MywzMTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODYsMzEyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTgwLDMxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3NywzMTIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDExNzEsMzEyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTY2LDMxMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTE1OSwzMTIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNTQsMzEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTQ3LDMxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE0MSwzMTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzUsMzExKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI3LDMxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyMywzMTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMTcsMzExKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSgxMTEyLDMxMSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTEwNiwzMDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDIsMzA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDk2LDMwOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA4OCwzMDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODEsMzA4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDczLDMwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA2MywzMDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTEsMzA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQyLDMwMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAzNCwzMDMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwMjIsMzAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE1LDMwMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAwNCwyOTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5NywyOTcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk4OCwyOTQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk4MywyOTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk3OSwyOTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk3NiwyOTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk3NCwyOTEpOwpzbGVlcCgxMzkpOwpjcy5tb3VzZU1vdmUoOTc1LDI5MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTc3LDI5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgwLDI4Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgzLDI4NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTg0LDI4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg3LDI3OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTg3LDI3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg5LDI3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg5LDI3NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTkwLDI3NSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDk5MCwyNzMpOwpzbGVlcCgzMSk7CmNzLm1vdXNlTW92ZSg5OTIsMjczKTsKc2xlZXAoMzUyKTsKY3MubW91c2VNb3ZlKDk5MSwyNzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5MSwyNzIpOwpzbGVlcCgxNDApOwpjcy5tb3VzZU1vdmUoOTg5LDI3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg5LDI3MCk7CnNsZWVwKDIyMCk7CmNzLm1vdXNlTGVmdERvd24oOTg5LDI3MCk7CnNsZWVwKDg2KTsKY3MubW91c2VMZWZ0VXAoOTg5LDI3MCk7CnNsZWVwKDEzKTsKY3MubW91c2VNb3ZlKDk4OCwyNzApOwpzbGVlcCgxODApOwpjcy5tb3VzZU1vdmUoOTg4LDI3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg4LDI3NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg4LDI3Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg4LDI3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg4LDI4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg4LDI4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg4LDI4NSk7CnNsZWVwKDYyKTsKY3MubW91c2VNb3ZlKDk4OCwyODYpOwpzbGVlcCgzOCk7CmNzLm1vdXNlTW92ZSg5ODgsMjg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODgsMjg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODgsMjkxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5ODgsMjkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODgsMjk0KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoOTg4LDI5NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTg2LDI5Nyk7CnNsZWVwKDg1KTsKY3MubW91c2VNb3ZlKDk4MywyOTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4MiwyOTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk3OSwyOTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk3NiwyODUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk3MywyODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk2OCwyNzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk2NSwyNzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk2NCwyNzApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk2MiwyNjkpOwpzbGVlcCgxMTUpOwpjcy5tb3VzZU1vdmUoOTYyLDI3MCk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDk2MywyNzEpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg5NjUsMjczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NjYsMjczKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NjYsMjc0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5NjgsMjc0KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoOTY5LDI3NCk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDk3MSwyNzQpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSg5NzEsMjc2KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoOTcyLDI3Nik7CnNsZWVwKDcxKTsKY3MubW91c2VNb3ZlKDk3NCwyNzcpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg5NzUsMjc3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5NzUsMjc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5NzcsMjc5KTsKc2xlZXAoNzApOwpjcy5tb3VzZU1vdmUoOTc4LDI4MCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDk3OCwyODIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDk4MCwyODIpOwpzbGVlcCgzNzcpOwpjcy5tb3VzZUxlZnREb3duKDk4MCwyODIpOwpzbGVlcCg3MCk7CmNzLm1vdXNlTGVmdFVwKDk4MCwyODIpOwpzbGVlcCg4NSk7CmNzLm1vdXNlTW92ZSg5ODEsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODQsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODYsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODcsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5OTAsMjgyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg5OTIsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5OTUsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5OTYsMjgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDAxLDI4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAwNCwyODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDgsMjc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDExLDI3OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAxMywyNzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMTYsMjc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE5LDI3OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTAyMCwyNzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjIsMjc2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDIzLDI3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAyNSwyNzUpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMDI2LDI3NSk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDEwMjgsMjczKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDMxLDI3Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAzMiwyNzMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwMzUsMjcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM3LDI3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0MCwyNzApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwNDMsMjY5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDQ0LDI2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0NiwyNjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDcsMjY3KTsKc2xlZXAoMTAxKTsKY3MubW91c2VNb3ZlKDEwNDksMjY3KTsKc2xlZXAoMTA5KTsKY3MubW91c2VNb3ZlKDEwNTAsMjY3KTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoMTA1MiwyNjcpOwpzbGVlcCgxMTkpOwpjcy5tb3VzZU1vdmUoMTA1MywyNjcpOwpzbGVlcCgzOCk7CmNzLm1vdXNlTW92ZSgxMDUzLDI2OCk7CnNsZWVwKDQwKTsKY3MubW91c2VNb3ZlKDEwNTUsMjY4KTsKc2xlZXAoMTQ3KTsKY3MubW91c2VMZWZ0RG93bigxMDU1LDI2OCk7CnNsZWVwKDc4KTsKY3MubW91c2VMZWZ0VXAoMTA1NSwyNjgpOwpzbGVlcCgyMTApOwpjcy5tb3VzZU1vdmUoMTA1NSwyNzApOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMDU0LDI3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1NCwyNzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTQsMjc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDU0LDI3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1MiwyNzcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwNTIsMjc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDUxLDI4MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA1MSwyODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDksMjgzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQ5LDI4NSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEwNDksMjg2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDQ4LDI4Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA0OCwyODgpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDQ4LDI4OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA0OCwyOTEpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMDQ4LDI5Mik7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEwNDgsMjk0KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTA0OCwyOTUpOwpzbGVlcCgyMik7CmNzLm1vdXNlTW92ZSgxMDQ2LDI5Nyk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEwNDYsMjk4KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTA0NiwzMDApOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDQ2LDMwMSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwNDYsMzAzKTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoMTA0NiwzMDQpOwpzbGVlcCgxODgpOwpjcy5tb3VzZU1vdmUoMTA0NSwzMDQpOwpzbGVlcCgzMik7CmNzLm1vdXNlTGVmdERvd24oMTA0NSwzMDQpOwpzbGVlcCg3Nyk7CmNzLm1vdXNlTGVmdFVwKDEwNDUsMzA0KTsKc2xlZXAoNzkpOwpjcy5tb3VzZU1vdmUoMTA0MywzMDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDMsMzA5KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTA0MiwzMTMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwNDAsMzE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQwLDMyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0MCwzMjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDAsMzM0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDM5LDM0MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzOSwzNDMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwMzksMzQ5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM3LDM1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzNywzNjApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMzYsMzY2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDM0LDM3Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAzMywzNzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzEsMzgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDMwLDM5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyOCwzOTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjcsNDAzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI0LDQxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyMiw0MjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjEsNDI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE5LDQzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxOCw0MzYpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwMTYsNDQyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE1LDQ0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxMiw0NTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwMDksNDU2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA2LDQ2MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAwNCw0NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDEsNDY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDAwLDQ3MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTk3LDQ3NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTk1LDQ3OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTk0LDQ4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTk0LDQ4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTkyLDQ4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTkxLDQ4Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg5LDQ5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg5LDQ5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg4LDQ5Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTg2LDQ5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDQ5Nik7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDk4Niw0OTgpOwpzbGVlcCgzMSk7CmNzLm1vdXNlTW92ZSg5ODUsNDk4KTsKc2xlZXAoOTQpOwpjcy5tb3VzZU1vdmUoOTg1LDQ5OSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDk4NSw1MDEpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSg5ODUsNTAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODUsNTA0KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg5ODUsNTA1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg5ODUsNTA3KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoOTg1LDUwOCk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDk4NSw1MTApOwpzbGVlcCgzMSk7CmNzLm1vdXNlTW92ZSg5ODUsNTExKTsKc2xlZXAoMTQxKTsKY3MubW91c2VNb3ZlKDk4NSw1MTMpOwpzbGVlcCgyMik7CmNzLm1vdXNlTW92ZSg5ODUsNTE0KTsKc2xlZXAoMTE4KTsKY3MubW91c2VNb3ZlKDk4NSw1MTYpOwpzbGVlcCgyNSk7CmNzLm1vdXNlTW92ZSg5ODUsNTE3KTsKc2xlZXAoMTAwKTsKY3MubW91c2VNb3ZlKDk4Niw1MTcpOwpzbGVlcCgyMyk7CmNzLm1vdXNlTGVmdERvd24oOTg2LDUxNyk7CnNsZWVwKDEwMik7CmNzLm1vdXNlTGVmdFVwKDk4Niw1MTcpOwpzbGVlcCgyMjYpOwpjcy5tb3VzZU1vdmUoOTg2LDUxOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTg2LDUyMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTg2LDUyMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTg2LDUyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDUyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDUyOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTg2LDUzMSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTg2LDUzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDUzNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDUzNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDUzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDUzOCk7CnNsZWVwKDk1KTsKY3MubW91c2VNb3ZlKDk4NSw1MzgpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSg5ODMsNTM4KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoOTgyLDUzOCk7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDk4Miw1MzcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk4MCw1MzcpOwpzbGVlcCg0Nik7CmNzLm1vdXNlTW92ZSg5ODAsNTM2KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoOTgwLDUzMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTc5LDUzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTc5LDUzMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTc5LDUyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTc3LDUyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTc3LDUyNSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDk3Nyw1MjQpOwpzbGVlcCgxMDApOwpjcy5tb3VzZU1vdmUoOTc3LDUyMik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDk3Nyw1MjEpOwpzbGVlcCgyNDIpOwpjcy5tb3VzZUxlZnREb3duKDk3Nyw1MjEpOwpzbGVlcCg5NSk7CmNzLm1vdXNlTGVmdFVwKDk3Nyw1MjEpOwpzbGVlcCg3Nyk7CmNzLm1vdXNlTW92ZSg5NzgsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODEsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODQsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODksNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5OTUsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5OTgsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDAyLDUyMSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTAwNyw1MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTAsNTIyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDE0LDUyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxOSw1MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjMsNTIyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI2LDUyMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAyOCw1MjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMjksNTIyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDMxLDUyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzMiw1MjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMzQsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM1LDUyMSk7CnNsZWVwKDU1KTsKY3MubW91c2VNb3ZlKDEwMzcsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM4LDUyMSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEwNDAsNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQxLDUyMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0Myw1MTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDQsNTE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQ2LDUxOCk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEwNDcsNTE4KTsKc2xlZXAoMTAyKTsKY3MubW91c2VNb3ZlKDEwNDksNTE2KTsKc2xlZXAoMzEpOwpjcy5tb3VzZU1vdmUoMTA1MCw1MTYpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDUwLDUxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1Miw1MTUpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMDUzLDUxNSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEwNTUsNTE1KTsKc2xlZXAoODApOwpjcy5tb3VzZU1vdmUoMTA1NSw1MTMpOwpzbGVlcCgyMik7CmNzLm1vdXNlTW92ZSgxMDU2LDUxMyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwNTYsNTEyKTsKc2xlZXAoMzgyKTsKY3MubW91c2VNb3ZlKDEwNTYsNTEzKTsKc2xlZXAoMTAxKTsKY3MubW91c2VNb3ZlKDEwNTYsNTE0KTsKc2xlZXAoMTExKTsKY3MubW91c2VMZWZ0RG93bigxMDU2LDUxNCk7CnNsZWVwKDg2KTsKY3MubW91c2VMZWZ0VXAoMTA1Niw1MTQpOwpzbGVlcCgxODApOwpjcy5tb3VzZU1vdmUoMTA1Niw1MTYpOwpzbGVlcCgxMyk7CmNzLm1vdXNlTW92ZSgxMDU2LDUxNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1Niw1MTkpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMDU2LDUyMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA1Niw1MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTYsNTIzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDU2LDUyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1Niw1MjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwNTYsNTI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDU2LDUyOSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA1Niw1MzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTYsNTMyKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTA1Niw1MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTYsNTM1KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTA1Niw1MzcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwNTYsNTM4KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTA1Niw1NDApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwNTYsNTQxKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTA1Niw1NDMpOwpzbGVlcCgyNSk7CmNzLm1vdXNlTW92ZSgxMDU2LDU0NCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwNTUsNTQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDU1LDU0Nik7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDEwNTUsNTQ3KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTA1NSw1NDkpOwpzbGVlcCg5MzApOwpjcy5tb3VzZUxlZnREb3duKDEwNTUsNTQ5KTsKc2xlZXAoODcpOwpjcy5tb3VzZUxlZnRVcCgxMDU1LDU0OSk7CnNsZWVwKDE4OCk7CmNzLm1vdXNlTW92ZSgxMDU1LDU1Myk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTA1NCw1NTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwNTIsNTY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDUxLDU3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0OCw1NzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDYsNTg4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDQzLDU5NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA0Miw2MDMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMzksNjEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM3LDYxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzNCw2MzEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMzQsNjM5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM0LDY0Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAzMyw2NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzMsNjYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDMxLDY2OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAzMSw2NzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzEsNjgyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDMwLDY4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzMCw2OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzAsNjk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDMwLDcwMik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTAzMCw3MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjgsNzA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI4LDcwOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAyOCw3MTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjgsNzEyKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTAyOCw3MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjgsNzE4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI4LDcyMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyNyw3MjQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwMjcsNzI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI1LDcyOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTAyNCw3MzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjQsNzM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDIyLDczNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAyMiw3MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjEsNzQyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDIxLDc0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxOSw3NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTgsNzUwKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSgxMDE2LDc1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxNSw3NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTMsNzU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDEzLDc2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxMiw3NjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTIsNzYzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDEwLDc2Myk7CnNsZWVwKDc3KTsKY3MubW91c2VNb3ZlKDEwMTAsNzY1KTsKc2xlZXAoMzMpOwpjcy5tb3VzZU1vdmUoMTAxMCw3NjYpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDEwMDksNzY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA5LDc3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAwNyw3NzQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwMDQsNzc3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDAzLDc4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAwMCw3ODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5OCw3ODcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5OCw3ODkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5Nyw3OTApOwpzbGVlcCgxNjQpOwpjcy5tb3VzZU1vdmUoOTk3LDc5Mik7CnNsZWVwKDIyKTsKY3MubW91c2VNb3ZlKDk5NSw3OTMpOwpzbGVlcCgyMTMpOwpjcy5tb3VzZUxlZnREb3duKDk5NSw3OTMpOwpzbGVlcCg3OCk7CmNzLm1vdXNlTGVmdFVwKDk5NSw3OTMpOwpzbGVlcCgzNTgpOwpjcy5tb3VzZU1vdmUoOTk1LDc5NSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDk5NSw3OTYpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSg5OTQsNzk4KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoOTk0LDc5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTk0LDgwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTk0LDgwMik7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDk5Miw4MDQpOwpzbGVlcCg1Nyk7CmNzLm1vdXNlTW92ZSg5OTEsODA1KTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoOTkxLDgwNyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTg5LDgwNyk7CnNsZWVwKDU3KTsKY3MubW91c2VNb3ZlKDk4OCw4MDcpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg5ODYsODA3KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoOTg2LDgwNik7CnNsZWVwKDMyKTsKY3MubW91c2VNb3ZlKDk4NSw4MDQpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSg5ODUsODAzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg5ODUsODAxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODMsODAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODMsNzk4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSg5ODMsNzk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODMsNzk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg5ODMsNzk0KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoOTgzLDc5Mik7CnNsZWVwKDMzKTsKY3MubW91c2VNb3ZlKDk4Myw3OTEpOwpzbGVlcCgyMDIpOwpjcy5tb3VzZU1vdmUoOTgzLDc5Mik7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDk4Miw3OTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk4Miw3OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4Miw3OTYpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDk4MCw3OTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4MCw3OTgpOwpzbGVlcCg0NzkpOwpjcy5tb3VzZUxlZnREb3duKDk4MCw3OTgpOwpzbGVlcCg3OCk7CmNzLm1vdXNlTGVmdFVwKDk4MCw3OTgpOwpzbGVlcCg5Mik7CmNzLm1vdXNlTW92ZSg5ODEsNzk4KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoOTgzLDc5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg0LDc5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg2LDc5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg5LDc5Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTkzLDc5Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTk4LDc5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAwNCw3OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTAsNzk1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDE2LDc5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyMCw3OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMjMsNzk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI2LDc5NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyOCw3OTQpOwpzbGVlcCgyNSk7CmNzLm1vdXNlTW92ZSgxMDI5LDc5NCk7CnNsZWVwKDQ1KTsKY3MubW91c2VNb3ZlKDEwMzEsNzk0KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTAzMiw3OTQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMzQsNzk0KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTAzNSw3OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzcsNzkyKTsKc2xlZXAoMTMpOwpjcy5tb3VzZU1vdmUoMTAzOCw3OTIpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMDQwLDc5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0MSw3OTIpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMDQzLDc5Mik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwNDQsNzkyKTsKc2xlZXAoNjIpOwpjcy5tb3VzZU1vdmUoMTA0Niw3OTIpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMDQ3LDc5Mik7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDEwNDksNzkyKTsKc2xlZXAoMzIpOwpjcy5tb3VzZU1vdmUoMTA1MCw3OTIpOwpzbGVlcCgyNTgpOwpjcy5tb3VzZU1vdmUoMTA1Miw3OTIpOwpzbGVlcCg1NCk7CmNzLm1vdXNlTW92ZSgxMDUyLDc5Myk7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDEwNTMsNzkzKTsKc2xlZXAoMTExKTsKY3MubW91c2VNb3ZlKDEwNTUsNzkzKTsKc2xlZXAoMTE2KTsKY3MubW91c2VMZWZ0RG93bigxMDU1LDc5Myk7CnNsZWVwKDc4KTsKY3MubW91c2VMZWZ0VXAoMTA1NSw3OTMpOwpzbGVlcCgyNjYpOwpjcy5tb3VzZU1vdmUoMTA1NSw3OTUpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDU1LDc5Nik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwNTUsNzk4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDU1LDc5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1NCw3OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTQsODAxKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTA1NCw4MDIpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMDUyLDgwNCk7CnNsZWVwKDMwKTsKY3MubW91c2VNb3ZlKDEwNTIsODA1KTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoMTA1Miw4MDcpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMDUxLDgwOCk7CnNsZWVwKDMyKTsKY3MubW91c2VNb3ZlKDEwNTEsODEwKTsKc2xlZXAoMzgpOwpjcy5tb3VzZU1vdmUoMTA0OSw4MTEpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDQ5LDgxMyk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDEwNDgsODE0KTsKc2xlZXAoMjI2KTsKY3MubW91c2VMZWZ0RG93bigxMDQ4LDgxNCk7CnNsZWVwKDcwKTsKY3MubW91c2VMZWZ0VXAoMTA0OCw4MTQpOwpzbGVlcCg5Myk7CmNzLm1vdXNlTW92ZSgxMDQ2LDgxNCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwNDUsODE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQzLDgxNyk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDEwNDIsODE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQwLDgxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0MCw4MjApOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDM5LDgyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzOSw4MjIpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDM3LDgyMyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwMzYsODI1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDM0LDgyNSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEwMzQsODI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDMzLDgyNik7CnNsZWVwKDYzKTsKY3MubW91c2VNb3ZlKDEwMzMsODI4KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTAzMSw4MjkpOwpzbGVlcCgxOTYpOwpjcy5tb3VzZUxlZnREb3duKDEwMzEsODI5KTsKc2xlZXAoNzApOwpjcy5tb3VzZUxlZnRVcCgxMDMxLDgyOSk7CnNsZWVwKDc3KTsKY3MubW91c2VNb3ZlKDEwMzIsODI5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM3LDgzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0Myw4MzIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwNDksODM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDU2LDgzNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA2NSw4MzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwNzQsODQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDgyLDg0NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA5OCw4NTApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExMTksODU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTQwLDg2NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE1NSw4NjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODQsODczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTk5LDg3Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIyNiw4ODIpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDEyNDgsODg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjc1LDg5NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI5Myw5MDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMTcsOTA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzM1LDkxMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM0Nyw5MTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzAsOTI1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzg4LDkzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQwMCw5MzYpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDE0MTUsOTQwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDM2LDk0Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQ0OSw5NDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NjYsOTU1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDc2LDk1OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ4Nyw5NjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0OTQsOTY0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTAyLDk2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUwOSw5NzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MTUsOTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTIwLDk4Mik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTUyNCw5ODcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MjcsOTkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTMwLDk5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUzMyw5OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MzUsOTk2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTM4LDk5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU0MiwxMDAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTQ1LDEwMDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NDgsMTAwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1MSwxMDAzKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSgxNTU2LDEwMDYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE1NjAsMTAwOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU2NiwxMDA5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTcyLDEwMTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1ODAsMTAxMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTU4OSwxMDE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjA0LDEwMTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MTksMTAxNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTYzMiwxMDE1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjQzLDEwMTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NTYsMTAxNSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTY2NCwxMDE1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjcxLDEwMTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2ODAsMTAxNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTY4OCwxMDE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjk0LDEwMTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2OTcsMTAxOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTY5NywxMDIwKTsKc2xlZXAoMzApOwpjcy5tb3VzZU1vdmUoMTY5OCwxMDIxKTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTcwMCwxMDIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzAwLDEwMjMpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxNzAxLDEwMjMpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxNzAxLDEwMjQpOwpzbGVlcCg0Nik7CmNzLm1vdXNlTW92ZSgxNzAxLDEwMjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MDMsMTAyOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwMywxMDMyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzAzLDEwMzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE3MDMsMTAzNSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDE3MDMsMTAzNik7CnNsZWVwKDU1KTsKY3MubW91c2VNb3ZlKDE3MDQsMTAzOCk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDE3MDQsMTAzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwNCwxMDQxKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTcwNCwxMDQyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNzA0LDEwNDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MDQsMTA0NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTcwNCwxMDQ3KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTcwNCwxMDQ4KTsKc2xlZXAoMjAzKTsKY3MubW91c2VMZWZ0RG93bigxNzA0LDEwNDgpOwpzbGVlcCg4NSk7CmNzLm1vdXNlTGVmdFVwKDE3MDQsMTA0OCk7CnNsZWVwKDcxKTsKY3MubW91c2VNb3ZlKDE3MDQsMTA0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwMiwxMDQ0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzAwLDEwNDEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2OTcsMTAzNyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTY5MywxMDMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjg1LDEwMjIpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxNjYwLDk5Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTY0Niw5ODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MjUsOTYzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjE5LDk1Nik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTYwOSw5MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MDQsOTI2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjAxLDkxNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTYwMCw5MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1OTgsODg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTk0LDg3Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU5Miw4NjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1ODYsODQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTc5LDgyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU3MCw4MTApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE1NjcsNzk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTYyLDc4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1Niw3NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NTAsNzQxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTQ3LDczNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU0Niw3MzQpOwpzbGVlcCg0MCk7CmNzLm1vdXNlTW92ZSgxNTQzLDczNCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTUyNiw3MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0OTgsNzM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDgxLDczOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ3MSw3MzkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0NjAsNzQxKTsKc2xlZXAoNjE5KTsKY3Mua2V5RG93big3Nyk7CnNsZWVwKDg4KTsKY3Mua2V5RG93big4MCk7CnNsZWVwKDgwKTsKY3Mua2V5VXAoNzcpOwpzbGVlcCg0MCk7CmNzLmtleVVwKDgwKTsKc2xlZXAoMTc2KTsKY3Mua2V5RG93bigxOTApOwpzbGVlcCg4OCk7CmNzLmtleVVwKDE5MCk7CnNsZWVwKDEwNCk7CmNzLmtleURvd24oODcpOwpzbGVlcCg4MCk7CmNzLmtleURvd24oNjkpOwpzbGVlcCg3MSk7CmNzLmtleVVwKDg3KTsKc2xlZXAoOTcpOwpjcy5rZXlVcCg2OSk7CnNsZWVwKDI0KTsKY3Mua2V5RG93big3Myk7CnNsZWVwKDg3KTsKY3Mua2V5VXAoNzMpOwpzbGVlcCgxMTIpOwpjcy5rZXlEb3duKDg4KTsKc2xlZXAoMTA0KTsKY3Mua2V5RG93big3Myk7CnNsZWVwKDI1KTsKY3Mua2V5VXAoODgpOwpzbGVlcCg5Nik7CmNzLmtleURvd24oNzgpOwpzbGVlcCg2Myk7CmNzLmtleVVwKDczKTsKc2xlZXAoNDEpOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDQ4Nyk7CmNzLmtleURvd24oMTkwKTsKc2xlZXAoOTYpOwpjcy5rZXlVcCgxOTApOwpzbGVlcCgxNyk7CmNzLmtleURvd24oODEpOwpzbGVlcCgxMDMpOwpjcy5rZXlVcCg4MSk7CnNsZWVwKDEwNCk7CmNzLmtleURvd24oODEpOwpzbGVlcCg3Mik7CmNzLmtleVVwKDgxKTsKc2xlZXAoOTYpOwpjcy5rZXlEb3duKDE5MCk7CnNsZWVwKDk2KTsKY3Mua2V5VXAoMTkwKTsKc2xlZXAoMzMpOwpjcy5rZXlEb3duKDY3KTsKc2xlZXAoMTI4KTsKY3Mua2V5VXAoNjcpOwpzbGVlcCg0OCk7CmNzLmtleURvd24oNzkpOwpzbGVlcCgxMDQpOwpjcy5rZXlEb3duKDc3KTsKc2xlZXAoNzIpOwpjcy5rZXlVcCg3OSk7CnNsZWVwKDIzKTsKY3Mua2V5VXAoNzcpOwpzbGVlcCg2NDMpOwpjcy5tb3VzZU1vdmUoMTQ1Niw3NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NTAsNzQ1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNDQ0LDc1MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQzOCw3NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzIsNzU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDI3LDc2Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQyMyw3NjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MjAsNzY4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDE3LDc2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQxNCw3NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDgsNzc1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzk5LDc4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM4Nyw3ODMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNzAsNzg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzUyLDc5Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTMyNSw3OTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTcsODA1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjcwLDgxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIzNyw4MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTYsODI4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTkzLDgzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4MCw4NDEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExNjYsODQ3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTU3LDg1MCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTE1MCw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNDUsODUyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTQxLDg1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzOSw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzYsODUyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTMzLDg1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzMiw4NTIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzAsODUyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTI3LDg1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyNCw4NTIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDExMjAsODUyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTE0LDg1Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEwOCw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDMsODUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTAyLDg1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEwMCw4NTIpOwpzbGVlcCg0Nik7CmNzLm1vdXNlTW92ZSgxMDk3LDg1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA5Myw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwOTEsODUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDkwLDg1Mik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEwODgsODUyKTsKc2xlZXAoMzkpOwpjcy5tb3VzZU1vdmUoMTA4Nyw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODQsODUyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDc5LDg1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA3Niw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNzUsODUyKTsKc2xlZXAoNDEpOwpjcy5tb3VzZU1vdmUoMTA3Myw4NTIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEwNzIsODUyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDY5LDg1Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA2Nyw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNjYsODUyKTsKc2xlZXAoODYpOwpjcy5tb3VzZU1vdmUoMTA2Myw4NTIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwNjAsODUyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDU1LDg1Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA1MSw4NTMpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMDQ5LDg1Myk7CnNsZWVwKDEwMSk7CmNzLm1vdXNlTW92ZSgxMDQ4LDg1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0Niw4NTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwNDUsODUzKTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTA0Myw4NTMpOwpzbGVlcCg2Myk7CmNzLm1vdXNlTW92ZSgxMDQyLDg1Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTA0MCw4NTIpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMDM5LDg1Mik7CnNsZWVwKDg3KTsKY3MubW91c2VNb3ZlKDEwMzcsODUyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDM2LDg1Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAzNiw4NTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzQsODUxKTsKc2xlZXAoMTUwKTsKY3MubW91c2VNb3ZlKDEwMzMsODUxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDMxLDg1MSk7CnNsZWVwKDIwMik7CmNzLm1vdXNlTGVmdERvd24oMTAzMSw4NTEpOwpzbGVlcCg5NCk7CmNzLm1vdXNlTGVmdFVwKDEwMzEsODUxKTsKc2xlZXAoMTAxKTsKY3MubW91c2VNb3ZlKDEwMzIsODQ5KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTAzNCw4NDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzUsODQ4KTsKc2xlZXAoMTQzKTsKY3Mua2V5RG93big0OSk7CnNsZWVwKDEzNSk7CmNzLmtleVVwKDQ5KTsKc2xlZXAoMjcwKTsKY3MubW91c2VNb3ZlKDEwMzUsODQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDM3LDg0NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTA0MSw4NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDcsODMzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDU1LDgxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA2MSw4MDcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwNjcsODAwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDc5LDc4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA5Miw3NzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDQsNzU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTE4LDc0Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTEzNCw3MzEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDExNDUsNzE5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTU3LDcwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE2Nyw2OTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExNzgsNjgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTg3LDY2Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE5Myw2NTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDIsNjQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA4LDYzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMiw2MjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTcsNjE1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjIxLDYwOCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIyNiw1OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjksNTk0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjMzLDU4Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIzOCw1NzkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNDEsNTczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjQ0LDU2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI0NSw1NjMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNDcsNTYwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjQ4LDU1Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI1MCw1NTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNTAsNTUyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjUxLDU0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1MSw1NDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTEsNTQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjUzLDU0Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI1Myw1NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTQsNTQwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjU0LDUzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1NCw1MzYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNTQsNTMzKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMjU0LDUzMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI1NCw1MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTQsNTI1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjU0LDUyNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI1NCw1MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTQsNTIxKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTI1NCw1MTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNTYsNTE2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjU3LDUxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1Nyw1MTMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyNTcsNTEyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjU3LDUxMCk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDEyNTksNTA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjYwLDUwNyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyNjIsNTA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjYzLDUwNCk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEyNjUsNTAzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjY1LDUwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI2Niw1MDApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNjgsNTAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjY5LDQ5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3MSw0OTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNzQsNDkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjc3LDQ4OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI4MCw0ODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODEsNDgzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg0LDQ4MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI4Niw0NzkpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjg2LDQ3Nyk7CnNsZWVwKDQ5KTsKY3MubW91c2VNb3ZlKDEyODcsNDc0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg5LDQ3MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI5Miw0NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTMsNDYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjk2LDQ1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5OCw0NTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyOTksNDUzKTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTI5OSw0NTIpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMzAxLDQ1Mik7CnNsZWVwKDU0KTsKY3MubW91c2VNb3ZlKDEzMDEsNDQ5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzAyLDQ0Nyk7CnNsZWVwKDc5KTsKY3MubW91c2VNb3ZlKDEzMDIsNDQ0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzAyLDQ0MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTMwMiw0MzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzMDIsNDM3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzAyLDQzNSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTMwMiw0MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMDIsNDMyKTsKc2xlZXAoNTYpOwpjcy5tb3VzZU1vdmUoMTMwMSw0MzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMDEsNDI4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzAxLDQyNik7CnNsZWVwKDE4Nyk7CmNzLm1vdXNlTW92ZSgxMzAxLDQyMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTMwMSw0MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMDEsNDE2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzAxLDQxNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwMSw0MTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzMDEsNDEwKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTMwMSw0MDgpOwpzbGVlcCgzNjgpOwpjcy5tb3VzZUxlZnREb3duKDEzMDEsNDA4KTsKc2xlZXAoNjEpOwpjcy5tb3VzZUxlZnRVcCgxMzAxLDQwOCk7CnNsZWVwKDI5MCk7CmNzLm1vdXNlTW92ZSgxMzAwLDQwOCk7CnNsZWVwKDMxKTsKY3MubW91c2VNb3ZlKDEyOTgsNDA4KTsKc2xlZXAoNDIyKTsKY3MubW91c2VMZWZ0RG93bigxMjk4LDQwOCk7CnNsZWVwKDg1KTsKY3MubW91c2VMZWZ0VXAoMTI5OCw0MDgpOwpzbGVlcCg3MCk7CmNzLm1vdXNlTW92ZSgxMjk4LDQwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5Nyw0MDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTcsNDExKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjk0LDQxMik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyOTEsNDE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg5LDQxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4NSw0MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODAsNDE4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMjc2LDQyMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3MCw0MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjQsNDI0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjU1LDQyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI0Nyw0MjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMzUsNDMwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjMxLDQzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIyNSw0MzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjIsNDMzKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTIyMCw0MzMpOwpzbGVlcCgzMik7CmNzLm1vdXNlTW92ZSgxMjE5LDQzMyk7CnNsZWVwKDI1KTsKY3MubW91c2VNb3ZlKDEyMTYsNDMzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjEzLDQzMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMCw0MzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMDcsNDM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA0LDQzNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwMSw0MzUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExOTYsNDM1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTkyLDQzNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTE4OSw0MzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODMsNDM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTgwLDQzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3NSw0MzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNzEsNDM4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTY4LDQzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE2NSw0MzgpOwpzbGVlcCgxOTUpOwpjcy5tb3VzZUxlZnREb3duKDExNjUsNDM4KTsKc2xlZXAoODcpOwpjcy5tb3VzZUxlZnRVcCgxMTY1LDQzOCk7CnNsZWVwKDE1Nik7CmNzLm1vdXNlUmlnaHREb3duKDExNjUsNDM4KTsKc2xlZXAoMTAyKTsKY3MubW91c2VSaWdodFVwKDExNjUsNDM4KTsKc2xlZXAoMTY0KTsKY3MubW91c2VNb3ZlKDExNjUsNDQ0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTY3LDQ1MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE2OSw0NTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNzIsNDYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTc2LDQ3MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3OSw0NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODQsNDg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTg3LDQ5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE5MCw1MDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTMsNTE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTk2LDUyMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE5OSw1MzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDIsNTQwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA1LDU0Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIwNiw1NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDgsNTY0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjExLDU3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIxNCw1ODUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyMTQsNTkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE1LDYwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxNSw2MDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTcsNjE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE3LDYyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxNyw2MjcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyMTcsNjMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE3LDYzNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIxNyw2MzYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyMTcsNjM3KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTIxNyw2MzkpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMjE3LDY0MCk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEyMTcsNjQyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE3LDY0Myk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDEyMTcsNjQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE3LDY0Nik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyMTYsNjQ2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjE2LDY0OCk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEyMTYsNjQ5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE2LDY1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxNiw2NTIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyMTYsNjU1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE2LDY1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxNiw2NjApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMTYsNjYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE2LDY2NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIxNCw2NjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTQsNjY5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE0LDY3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMyw2NzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMTMsNjc1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjEzLDY3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMyw2NzgpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMjEzLDY3OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIxMyw2ODEpOwpzbGVlcCgzMik7CmNzLm1vdXNlTW92ZSgxMjEzLDY4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMSw2ODIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMTEsNjg0KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTIxMSw2ODUpOwpzbGVlcCgzMyk7CmNzLm1vdXNlTW92ZSgxMjExLDY4Nyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyMTAsNjg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjEwLDY4OCk7CnNsZWVwKDIxKTsKY3MubW91c2VNb3ZlKDEyMTAsNjkwKTsKc2xlZXAoMTM1KTsKY3MubW91c2VMZWZ0RG93bigxMjEwLDY5MCk7CnNsZWVwKDEwMCk7CmNzLm1vdXNlTGVmdFVwKDEyMTAsNjkwKTsKc2xlZXAoMTk2KTsKY3MubW91c2VNb3ZlKDEyMDgsNjkzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA3LDY5Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIwNCw3MDApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyMDEsNzAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTk4LDcwMik7CnNsZWVwKDg0NCk7CmNzLmtleURvd24oODUpOwpzbGVlcCg4Nyk7CmNzLmtleVVwKDg1KTsKc2xlZXAoMzIpOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoMTQ0KTsKY3Mua2V5VXAoODMpOwpzbGVlcCgxODQpOwpjcy5rZXlEb3duKDY5KTsKc2xlZXAoMTEyKTsKY3Mua2V5VXAoNjkpOwpzbGVlcCgxMjkpOwpjcy5rZXlEb3duKDgyKTsKc2xlZXAoODcpOwpjcy5rZXlVcCg4Mik7CnNsZWVwKDEyOSk7CmNzLmtleURvd24oMjApOwpzbGVlcCgxMjApOwpjcy5rZXlVcCgyMCk7CnNsZWVwKDEyOCk7CmNzLmtleURvd24oODApOwpzbGVlcCg3OSk7CmNzLmtleURvd24oMjApOwpzbGVlcCgzMik7CmNzLmtleVVwKDgwKTsKc2xlZXAoOTcpOwpjcy5rZXlVcCgyMCk7CnNsZWVwKDEwNCk7CmNzLmtleURvd24oODIpOwpzbGVlcCg5NSk7CmNzLmtleVVwKDgyKTsKc2xlZXAoMzIxKTsKY3Mua2V5RG93big3OSk7CnNsZWVwKDk2KTsKY3Mua2V5VXAoNzkpOwpzbGVlcCgyMTYpOwpjcy5rZXlEb3duKDc3KTsKc2xlZXAoMTExKTsKY3Mua2V5VXAoNzcpOwpzbGVlcCgxNTIpOwpjcy5rZXlEb3duKDgwKTsKc2xlZXAoODgpOwpjcy5rZXlVcCg4MCk7CnNsZWVwKDE1Mik7CmNzLmtleURvd24oODQpOwpzbGVlcCg4MCk7CmNzLmtleVVwKDg0KTsKc2xlZXAoNTg4KTsKY3MubW91c2VNb3ZlKDExOTksNjk5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA4LDY4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIyMCw2NjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMzAsNjUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjM4LDYzOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI0NSw2MjcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNTQsNjA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjYyLDU5Myk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI2Niw1ODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjksNTY0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjcxLDU1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3Miw1MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNzUsNTI0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjc4LDUxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4MSw1MDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODQsNDg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg2LDQ4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Nyw0NzYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODksNDcxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkwLDQ2Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI5MCw0NjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTAsNDYyKTsKc2xlZXAoMTU3KTsKY3MubW91c2VNb3ZlKDEyOTIsNDU5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjkyLDQ1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5Myw0NTUpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjkzLDQ1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5Myw0NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTMsNDUwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjkzLDQ0OSk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTI5Myw0NDcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyOTIsNDQ3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjkyLDQ0Nik7CnNsZWVwKDQwKTsKY3MubW91c2VNb3ZlKDEyOTIsNDQ0KTsKc2xlZXAoNzkpOwpjcy5tb3VzZU1vdmUoMTI5MSw0NDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyOTEsNDQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg5LDQ0Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI4OSw0NDEpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSgxMjg5LDQ0MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OCw0MzgpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMjg2LDQzNyk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEyODYsNDM1KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTI4NSw0MzUpOwpzbGVlcCgzMDMpOwpjcy5tb3VzZU1vdmUoMTI4Niw0MzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODYsNDM0KTsKc2xlZXAoMjI3KTsKY3MubW91c2VMZWZ0RG93bigxMjg2LDQzNCk7CnNsZWVwKDc3KTsKY3MubW91c2VMZWZ0VXAoMTI4Niw0MzQpOwpzbGVlcCgyMTMpOwpjcy5tb3VzZU1vdmUoMTI4Niw0MzUpOwpzbGVlcCgyMik7CmNzLm1vdXNlTW92ZSgxMjg2LDQzNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4NSw0MzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODUsNDM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg1LDQzOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Myw0NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODMsNDQyKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTI4Miw0NDQpOwpzbGVlcCgxMyk7CmNzLm1vdXNlTW92ZSgxMjgyLDQ0NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI4MCw0NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODAsNDQ4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjc5LDQ1MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI3Nyw0NTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNzcsNDUzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjc2LDQ1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3Niw0NTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNzYsNDU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjc2LDQ2MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI3NCw0NjIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNzQsNDYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjczLDQ2NSk7CnNsZWVwKDI4Mik7CmNzLm1vdXNlTGVmdERvd24oMTI3Myw0NjUpOwpzbGVlcCg3MCk7CmNzLm1vdXNlTGVmdFVwKDEyNzMsNDY1KTsKc2xlZXAoNjkpOwpjcy5tb3VzZU1vdmUoMTI3MCw0NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjUsNDY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjU5LDQ2NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1Myw0NjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNDQsNDYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjM3LDQ2Mik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIyOSw0NjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjIsNDYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE2LDQ2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwOCw0NTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDQsNDU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTk2LDQ1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4Niw0NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODAsNDU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTc1LDQ1OCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDExNzQsNDU4KTsKc2xlZXAoMjEwKTsKY3MubW91c2VNb3ZlKDExNzQsNDU2KTsKc2xlZXAoMzkpOwpjcy5tb3VzZUxlZnREb3duKDExNzQsNDU2KTsKc2xlZXAoNzEpOwpjcy5tb3VzZUxlZnRVcCgxMTc0LDQ1Nik7CnNsZWVwKDEwOSk7CmNzLm1vdXNlUmlnaHREb3duKDExNzQsNDU2KTsKc2xlZXAoMTA5KTsKY3MubW91c2VSaWdodFVwKDExNzQsNDU2KTsKc2xlZXAoMjU4KTsKY3MubW91c2VNb3ZlKDExNzQsNDU3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTc0LDQ2Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3NSw0NjUpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDExNzUsNDY5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTc4LDQ3NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3OSw0NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODIsNDg0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTg1LDQ5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4OCw0OTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTEsNTA0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTkzLDUxMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE5Niw1MTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTcsNTIyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTk5LDUyOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwMCw1MzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDMsNTQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA1LDU0OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIwNiw1NTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDgsNTYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA5LDU2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwOSw1NzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDksNTgwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjA5LDU4Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIwOSw1OTEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyMDksNTk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA5LDYwMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIwOSw2MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDksNjA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA5LDYxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMSw2MTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMTEsNjIxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjExLDYyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMSw2MjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTEsNjMxKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSgxMjExLDYzMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIxMSw2MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTEsNjM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjExLDYzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMSw2MzkpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjExLDY0MCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyMTEsNjQyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjExLDY0Myk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDEyMTEsNjQ1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjExLDY0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxMCw2NDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMTAsNjQ5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjEwLDY1MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTIxMCw2NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDgsNjU1KTsKc2xlZXAoNjIpOwpjcy5tb3VzZU1vdmUoMTIwOCw2NTcpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMjA4LDY1OCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyMDgsNjYwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA4LDY2MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIwOCw2NjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDgsNjY0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA4LDY2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwOCw2NjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDgsNjcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA3LDY3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwNyw2NzgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyMDcsNjgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA3LDY4NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwNyw2ODcpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDEyMDUsNjkwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjA1LDY5Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIwNSw2OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDUsNjk2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjA1LDY5Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIwNSw2OTkpOwpzbGVlcCgxNDApOwpjcy5tb3VzZU1vdmUoMTIwNSw3MDApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyMDUsNzAyKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTIwNCw3MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDQsNzA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA0LDcwNik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyMDQsNzA4KTsKc2xlZXAoMTIzKTsKY3MubW91c2VMZWZ0RG93bigxMjA0LDcwOCk7CnNsZWVwKDcxKTsKY3MubW91c2VNb3ZlKDEyMDQsNzA3KTsKc2xlZXAoMTYpOwpjcy5tb3VzZUxlZnRVcCgxMjA0LDcwNyk7CnNsZWVwKDE3Mik7CmNzLm1vdXNlTW92ZSgxMjA0LDcwOCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyMDQsNzExKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA0LDcyNik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTIwNCw3MzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTgsNzQ0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTkzLDc0NCk7CnNsZWVwKDU0MCk7CmNzLmtleURvd24oNzYpOwpzbGVlcCg4MCk7CmNzLmtleVVwKDc2KTsKc2xlZXAoMzIpOwpjcy5rZXlEb3duKDg3KTsKc2xlZXAoMTAzKTsKY3Mua2V5VXAoODcpOwpzbGVlcCgxMzcpOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoOTUpOwpjcy5rZXlVcCg4Myk7CnNsZWVwKDk3KTsKY3Mua2V5RG93big4Myk7CnNsZWVwKDc5KTsKY3Mua2V5VXAoODMpOwpzbGVlcCgxOTcpOwpjcy5tb3VzZU1vdmUoMTE5NCw3NDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTksNzQ1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjA1LDc0NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIxNSw3MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjcsNzE5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjM4LDY5OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI1MCw2ODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjAsNjY5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjcxLDY1NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI3NSw2NDcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyODEsNjMzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjg0LDYyNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI4Niw2MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODcsNjAzKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMjg3LDU4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OSw1NzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODksNTU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkwLDU0Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI5MCw1NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTAsNTM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkwLDUzMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI5MCw1MzApOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjkwLDUyOCk7CnNsZWVwKDEwOCk7CmNzLm1vdXNlTW92ZSgxMjkwLDUyNyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI5MCw1MjIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyODksNTE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg5LDUwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OSw1MDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODgsNTAxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg4LDQ5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OCw0OTcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyODgsNDk1KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTI4OCw0OTQpOwpzbGVlcCg1Nyk7CmNzLm1vdXNlTW92ZSgxMjg2LDQ5Mik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI4Niw0OTEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODYsNDg5KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTI4Niw0ODgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODcsNDg2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg3LDQ4NSk7CnNsZWVwKDQ3KTsKY3MubW91c2VNb3ZlKDEyODcsNDgzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg3LDQ4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Nyw0NzkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODcsNDc3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg3LDQ3NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Nyw0NzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODcsNDY4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg3LDQ2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Nyw0NjUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyODcsNDY0KTsKc2xlZXAoODUpOwpjcy5tb3VzZU1vdmUoMTI4Nyw0NjIpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSgxMjg3LDQ2MSk7CnNsZWVwKDI1KTsKY3MubW91c2VNb3ZlKDEyODcsNDU5KTsKc2xlZXAoMjgxKTsKY3MubW91c2VNb3ZlKDEyODcsNDU4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjg3LDQ1Nik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTI4Nyw0NTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODcsNDUzKTsKc2xlZXAoMzEzKTsKY3MubW91c2VMZWZ0RG93bigxMjg3LDQ1Myk7CnNsZWVwKDkzKTsKY3MubW91c2VMZWZ0VXAoMTI4Nyw0NTMpOwpzbGVlcCgyNDIpOwpjcy5tb3VzZU1vdmUoMTI4Niw0NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODYsNDU0KTsKc2xlZXAoMTQ5KTsKY3MubW91c2VNb3ZlKDEyODYsNDU2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg2LDQ1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Niw0NjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODYsNDYyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjg2LDQ2Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Niw0NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODYsNDY2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjg2LDQ2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Niw0NjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODYsNDcyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMjg2LDQ3NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI4Niw0NzcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODYsNDgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg2LDQ4NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Niw0ODkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyODYsNDkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg2LDQ5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Niw0OTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODYsNTAxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjg2LDUwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Niw1MTEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyODYsNTE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg2LDUyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4NSw1MjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODUsNTM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjgzLDUzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Myw1MzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODMsNTQwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjgyLDU0MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4Miw1NDEpOwpzbGVlcCgzMSk7CmNzLm1vdXNlTW92ZSgxMjgyLDU0Myk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyODIsNTQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjgyLDU0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4MCw1NDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODAsNTUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjgwLDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3OSw1NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNzksNTU1KTsKc2xlZXAoMzApOwpjcy5tb3VzZU1vdmUoMTI3OSw1NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNzcsNTU2KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTI3Nyw1NTgpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjc2LDU1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3Niw1NTkpOwpzbGVlcCgyMzQpOwpjcy5tb3VzZUxlZnREb3duKDEyNzYsNTU5KTsKc2xlZXAoNzApOwpjcy5tb3VzZUxlZnRVcCgxMjc2LDU1OSk7CnNsZWVwKDcwKTsKY3MubW91c2VNb3ZlKDEyNzMsNTU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjY4LDU1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1OSw1NTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTAsNTQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjQwLDU0MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIyOCw1MzYpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyMjAsNTMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjEzLDUyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwNyw1MjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTksNTIxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTkzLDUxOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4OSw1MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExODMsNTEyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTgwLDUxMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE3OCw1MDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzcsNTA3KTsKc2xlZXAoNzgpOwpjcy5tb3VzZU1vdmUoMTE3Nyw1MDYpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDExNzUsNTAzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTc0LDUwMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE3NCw0OTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExNzQsNDk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTcyLDQ5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3Miw0OTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzIsNDg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTcxLDQ4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE3MSw0ODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzEsNDg1KTsKc2xlZXAoNjEpOwpjcy5tb3VzZU1vdmUoMTE3MSw0ODMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzEsNDgyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTY5LDQ3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE2OSw0NzcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNjksNDc2KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMTY5LDQ3NCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDExNjksNDczKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTY4LDQ3Myk7CnNsZWVwKDE1Nik7CmNzLm1vdXNlTGVmdERvd24oMTE2OCw0NzMpOwpzbGVlcCg3OCk7CmNzLm1vdXNlTGVmdFVwKDExNjgsNDczKTsKc2xlZXAoMjEyKTsKY3MubW91c2VNb3ZlKDExNjgsNDc0KTsKc2xlZXAoNjI1KTsKY3Mua2V5RG93big2Nik7CnNsZWVwKDEwMyk7CmNzLmtleVVwKDY2KTsKc2xlZXAoMjUpOwpjcy5rZXlEb3duKDc5KTsKc2xlZXAoODApOwpjcy5rZXlVcCg3OSk7CnNsZWVwKDI0KTsKY3Mua2V5RG93big2Nyk7CnNsZWVwKDExMSk7CmNzLmtleURvd24oNzIpOwpzbGVlcCg0OCk7CmNzLmtleVVwKDY3KTsKc2xlZXAoNDkpOwpjcy5rZXlVcCg3Mik7CnNsZWVwKDExMik7CmNzLmtleURvd24oNjUpOwpzbGVlcCg4OCk7CmNzLmtleVVwKDY1KTsKc2xlZXAoMzg5KTsKY3MubW91c2VNb3ZlKDExNjUsNDcxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTU5LDQ2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE0OCw0NjIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMzgsNDU2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTI5LDQ1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyMSw0NDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMTcsNDQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTE1LDQ0Nik7CnNsZWVwKDE5NCk7CmNzLm1vdXNlTW92ZSgxMTE1LDQ0NCk7CnNsZWVwKDQwKTsKY3MubW91c2VNb3ZlKDExMTgsNDQ0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTIyLDQ0NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEzMCw0NDQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExNDAsNDQ0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTQ4LDQ0NCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTE1OCw0NDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzUsNDQ3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTkxLDQ0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE5Nyw0NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjAsNDUxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjMyLDQ1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI0Miw0NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNTcsNDU2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjYzLDQ1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI2OCw0NTkpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyNzEsNDU5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjcyLDQ1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3NCw0NTkpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjc1LDQ1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3Nyw0NTkpOwpzbGVlcCg3MCk7CmNzLm1vdXNlTW92ZSgxMjc4LDQ2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4MCw0NjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODEsNDY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjgzLDQ2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4NCw0NjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODYsNDcyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjg3LDQ3NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OSw0NzUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyODksNDc3KTsKc2xlZXAoNDgpOwpjcy5tb3VzZU1vdmUoMTI4OSw0NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODksNDgwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg5LDQ4MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI4OSw0ODMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODksNDg0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjg5LDQ4Nik7CnNsZWVwKDMyKTsKY3MubW91c2VNb3ZlKDEyODgsNDg2KTsKc2xlZXAoMTQ5KTsKY3MubW91c2VNb3ZlKDEyODgsNDg1KTsKc2xlZXAoMzEpOwpjcy5tb3VzZU1vdmUoMTI4OCw0ODMpOwpzbGVlcCgzMDQpOwpjcy5tb3VzZU1vdmUoMTI4OCw0ODIpOwpzbGVlcCg5NSk7CmNzLm1vdXNlTW92ZSgxMjg4LDQ4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OCw0NzcpOwpzbGVlcCg1Myk7CmNzLm1vdXNlTW92ZSgxMjg5LDQ3Nyk7CnNsZWVwKDQ4NSk7CmNzLm1vdXNlTW92ZSgxMjg4LDQ3Nik7CnNsZWVwKDc4KTsKY3MubW91c2VNb3ZlKDEyODYsNDc2KTsKc2xlZXAoNTYyKTsKY3MubW91c2VMZWZ0RG93bigxMjg2LDQ3Nik7CnNsZWVwKDk0KTsKY3MubW91c2VMZWZ0VXAoMTI4Niw0NzYpOwpzbGVlcCgxNTUpOwpjcy5tb3VzZU1vdmUoMTI4Nyw0NzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODksNDc3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkwLDQ3Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI5Miw0NzcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyOTYsNDc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzAyLDQ4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwNyw0ODEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzMTAsNDgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzExLDQ4Myk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTMxNCw0ODQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzMTcsNDg3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzIzLDQ4OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTMyOSw0OTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzMzcsNDk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzQ0LDQ5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM0OSw0OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNTMsNTAyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzU2LDUwMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM1Niw1MDQpOwpzbGVlcCgzOCk7CmNzLm1vdXNlTW92ZSgxMzU4LDUwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM1OSw1MDUpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMzYxLDUwNSk7CnNsZWVwKDE1Nyk7CmNzLm1vdXNlTW92ZSgxMzYyLDUwNSk7CnNsZWVwKDIwMik7CmNzLm1vdXNlTW92ZSgxMzY0LDUwNSk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDEzNjUsNTA1KTsKc2xlZXAoMjEwKTsKY3MubW91c2VNb3ZlKDEzNjUsNTA3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzY3LDUwNyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzNjcsNTA4KTsKc2xlZXAoMzEpOwpjcy5tb3VzZU1vdmUoMTM2OCw1MDgpOwpzbGVlcCg1OTMpOwpjcy5tb3VzZUxlZnREb3duKDEzNjgsNTA4KTsKc2xlZXAoMTI2KTsKY3MubW91c2VNb3ZlKDEzNjgsNTEwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzcwLDUxMSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM3MCw1MTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzEsNTEzKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzcxLDUxNCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzNzEsNTE2KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTM3MSw1MTcpOwpzbGVlcCgzMik7CmNzLm1vdXNlTW92ZSgxMzcxLDUxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3Myw1MjApOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSgxMzczLDUyMik7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDEzNzMsNTIzKTsKc2xlZXAoMjUpOwpjcy5tb3VzZU1vdmUoMTM3Myw1MjUpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMzczLDUyNik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM3Myw1MjgpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMzczLDUyOSk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEzNzMsNTMxKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTM3Myw1MzIpOwpzbGVlcCgxOTYpOwpjcy5tb3VzZUxlZnRVcCgxMzczLDUzMik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTM3Myw1MzQpOwpzbGVlcCg4MSk7CmNzLm1vdXNlTW92ZSgxMzcyLDUzNCk7CnNsZWVwKDUzKTsKY3MubW91c2VNb3ZlKDEzNzIsNTMzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzcyLDUzMSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM3Miw1MzApOwpzbGVlcCgxMyk7CmNzLm1vdXNlTW92ZSgxMzcyLDUyOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM3Miw1MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzIsNTI1KTsKc2xlZXAoMTAyKTsKY3MubW91c2VNb3ZlKDEzNzIsNTI0KTsKc2xlZXAoMzEpOwpjcy5tb3VzZU1vdmUoMTM3Miw1MjIpOwpzbGVlcCgyMik7CmNzLm1vdXNlTW92ZSgxMzcyLDUyMSk7CnNsZWVwKDIzNSk7CmNzLm1vdXNlTW92ZSgxMzcwLDUyMSk7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDEzNjksNTIxKTsKc2xlZXAoMTE5KTsKY3MubW91c2VNb3ZlKDEzNjcsNTIxKTsKc2xlZXAoMjM0KTsKY3MubW91c2VNb3ZlKDEzNjgsNTIxKTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTM3MCw1MjEpOwpzbGVlcCgzNDIpOwpjcy5tb3VzZUxlZnREb3duKDEzNzAsNTIxKTsKc2xlZXAoMTI2KTsKY3MubW91c2VNb3ZlKDEzNzAsNTIyKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTM3MCw1MjMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzNzAsNTI1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzcwLDUyNik7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEzNzAsNTI5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzcwLDUzMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MCw1MzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzAsNTM0KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTM3MCw1MzUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNzAsNTM3KTsKc2xlZXAoMTMpOwpjcy5tb3VzZU1vdmUoMTM3MCw1MzgpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMzcwLDU0MCk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEzNzAsNTQxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzcwLDU0Myk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzNzAsNTQ0KTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoMTM3MCw1NDYpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMzcwLDU0Nyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzNzAsNTQ5KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTM3MCw1NTApOwpzbGVlcCgyMyk7CmNzLm1vdXNlTW92ZSgxMzcwLDU1Mik7CnNsZWVwKDIwNCk7CmNzLm1vdXNlTGVmdFVwKDEzNzAsNTUyKTsKc2xlZXAoNjEpOwpjcy5tb3VzZU1vdmUoMTM2OSw1NTIpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMzY3LDU1Mik7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEzNjQsNTUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzYxLDU1Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM1Nyw1NTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNTIsNTQ5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzQ4LDU0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM0Myw1NDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNDAsNTQ2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzQwLDU0NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTMzOSw1NDUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzMzksNTQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzM2LDU0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMzNCw1MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMzEsNTM2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzI4LDUzNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMyNSw1MzEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzMjQsNTMxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzIyLDUzMCk7CnNsZWVwKDEwKTsKY3MubW91c2VNb3ZlKDEzMjEsNTMwKTsKc2xlZXAoNzcpOwpjcy5tb3VzZU1vdmUoMTMxOSw1MzApOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMzE4LDUzMCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTMxNiw1MzApOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMzE1LDUzMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMxMiw1MzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMDcsNTI4KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzAzLDUyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwMSw1MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTcsNTI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkyLDUyNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI4OSw1MjQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyODgsNTI0KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTI4Niw1MjQpOwpzbGVlcCgyMTApOwpjcy5tb3VzZU1vdmUoMTI4Niw1MjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODYsNTE5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjg2LDUxOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI4Niw1MTYpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjg2LDUxNSk7CnNsZWVwKDYyKTsKY3MubW91c2VNb3ZlKDEyODcsNTE1KTsKc2xlZXAoMjMzKTsKY3MubW91c2VNb3ZlKDEyODYsNTE2KTsKc2xlZXAoMTQ5KTsKY3MubW91c2VNb3ZlKDEyODUsNTE2KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTI4NSw1MTcpOwpzbGVlcCgyOTkpOwpjcy5tb3VzZUxlZnREb3duKDEyODUsNTE3KTsKc2xlZXAoNzcpOwpjcy5tb3VzZUxlZnRVcCgxMjg1LDUxNyk7CnNsZWVwKDcwKTsKY3MubW91c2VNb3ZlKDEyODMsNTE3KTsKc2xlZXAoMzApOwpjcy5tb3VzZU1vdmUoMTI4Miw1MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNzksNTE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjc0LDUxNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3MCw1MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjIsNTE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjU1LDUxNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI0NCw1MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMzQsNTE3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjIwLDUxNik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIwOCw1MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTgsNTEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTkwLDUxMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4MSw1MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzQsNTEwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTY5LDUxMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE2OCw1MDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNjYsNTA5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTY2LDUwNyk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDExNjUsNTA3KTsKc2xlZXAoNTYpOwpjcy5tb3VzZU1vdmUoMTE2NSw1MDYpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMTY2LDUwNik7CnNsZWVwKDEwMCk7CmNzLm1vdXNlTW92ZSgxMTY3LDUwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE2Nyw1MDQpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMTY5LDUwNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE2OSw1MDMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExNzAsNTAzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTcwLDUwMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE3Miw1MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzIsNTAwKTsKc2xlZXAoOTMpOwpjcy5tb3VzZU1vdmUoMTE3Myw1MDApOwpzbGVlcCgxMzMpOwpjcy5tb3VzZUxlZnREb3duKDExNzMsNTAwKTsKc2xlZXAoNjIpOwpjcy5tb3VzZUxlZnRVcCgxMTczLDUwMCk7CnNsZWVwKDExMCk7CmNzLm1vdXNlTW92ZSgxMTcyLDUwMCk7CnNsZWVwKDEzMik7CmNzLm1vdXNlTW92ZSgxMTcyLDUwMSk7CnNsZWVwKDg3KTsKY3MubW91c2VNb3ZlKDExNzIsNTAyKTsKc2xlZXAoMTY0NCk7CmNzLmtleURvd24oODMpOwpzbGVlcCgxMjApOwpjcy5rZXlVcCg4Myk7CnNsZWVwKDgwKTsKY3Mua2V5RG93big3OSk7CnNsZWVwKDExMSk7CmNzLmtleURvd24oODUpOwpzbGVlcCg4OCk7CmNzLmtleVVwKDc5KTsKc2xlZXAoMjQpOwpjcy5rZXlVcCg4NSk7CnNsZWVwKDQxKTsKY3Mua2V5RG93big3MSk7CnNsZWVwKDk1KTsKY3Mua2V5VXAoNzEpOwpzbGVlcCgyNCk7CmNzLmtleURvd24oNzkpOwpzbGVlcCgxMTIpOwpjcy5rZXlEb3duKDg1KTsKc2xlZXAoMTA1KTsKY3Mua2V5VXAoNzkpOwpzbGVlcCgxNSk7CmNzLmtleVVwKDg1KTsKc2xlZXAoNzk3KTsKY3MubW91c2VNb3ZlKDExNzUsNTAyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTc4LDUwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4Miw1MDIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExODUsNTAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTkxLDUwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE5NCw1MDIpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDExOTksNTAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA4LDUwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxNyw1MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjEsNTA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjI3LDUwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIzMiw1MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMzMsNTA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjM1LDUwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIzNiw1MDQpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMjM4LDUwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIzOSw1MDEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyNDIsNTAwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjQ1LDQ5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI0OCw0OTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNTEsNDk0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjUxLDQ5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1Myw0OTIpOwpzbGVlcCg1NSk7CmNzLm1vdXNlTW92ZSgxMjU0LDQ5Mik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyNTcsNDkyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMjYwLDQ5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI2Myw0OTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNjUsNDg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjY2LDQ4OSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyNjgsNDg4KTsKc2xlZXAoODQpOwpjcy5tb3VzZU1vdmUoMTI3MSw0ODgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNzIsNDg4KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTI3NCw0ODgpOwpzbGVlcCg0OSk7CmNzLm1vdXNlTW92ZSgxMjc1LDQ4OCk7CnNsZWVwKDIxKTsKY3MubW91c2VNb3ZlKDEyNzcsNDg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjc4LDQ4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4MCw0ODgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyODEsNDg4KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTI4Myw0ODgpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMjg0LDQ4OCk7CnNsZWVwKDIxOCk7CmNzLm1vdXNlTW92ZSgxMjg0LDQ4OSk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEyODYsNDkwKTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoMTI4Niw0OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODcsNDkyKTsKc2xlZXAoMjU4KTsKY3MubW91c2VMZWZ0RG93bigxMjg3LDQ5Mik7CnNsZWVwKDg1KTsKY3MubW91c2VMZWZ0VXAoMTI4Nyw0OTIpOwpzbGVlcCgxMzMpOwpjcy5tb3VzZU1vdmUoMTI4OSw0OTMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyOTMsNDk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjk4LDQ5OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwNyw1MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMTYsNTA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzIzLDUwNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMzMiw1MTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMzgsNTEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzQ2LDUxNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM1Miw1MTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNTUsNTE5KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzU5LDUyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM2MSw1MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNjQsNTI1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzY1LDUyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM2OCw1MjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzAsNTI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzc0LDUyOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3OSw1MzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzODMsNTMyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzg1LDUzMik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTM4Niw1MzIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzODgsNTM0KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTM4OSw1MzQpOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMzkxLDUzNCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzOTIsNTM0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzk0LDUzNCk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzOTQsNTM1KTsKc2xlZXAoNDUpOwpjcy5tb3VzZU1vdmUoMTM5Myw1MzUpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMzkxLDUzNSk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEzOTAsNTM1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzg4LDUzNyk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEzODcsNTM3KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTM4NSw1MzcpOwpzbGVlcCgzMik7CmNzLm1vdXNlTW92ZSgxMzg0LDUzNyk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDEzODIsNTM3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzgxLDUzNyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM3OSw1MzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzYsNTM3KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTM3NSw1MzcpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMzczLDUzOCk7CnNsZWVwKDIxKTsKY3MubW91c2VNb3ZlKDEzNzIsNTM4KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTM3MCw1MzgpOwpzbGVlcCg2Mik7CmNzLm1vdXNlTW92ZSgxMzY5LDUzOCk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDEzNjcsNTQwKTsKc2xlZXAoMjU5KTsKY3MubW91c2VNb3ZlKDEzNjgsNTQwKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTM3MCw1NDApOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMzcxLDU0MCk7CnNsZWVwKDE0KTsKY3MubW91c2VNb3ZlKDEzNzMsNTQwKTsKc2xlZXAoMzQ0KTsKY3MubW91c2VNb3ZlKDEzNzIsNTQwKTsKc2xlZXAoMTAyKTsKY3MubW91c2VNb3ZlKDEzNzAsNTQwKTsKc2xlZXAoMzY2KTsKY3MubW91c2VMZWZ0RG93bigxMzcwLDU0MCk7CnNsZWVwKDEwMik7CmNzLm1vdXNlTW92ZSgxMzcwLDU0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MCw1NDMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNzAsNTQ0KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTM3MCw1NDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzEsNTQ3KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTM3MSw1NDkpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDEzNzEsNTUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzcxLDU1Mik7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDEzNzEsNTUzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzcxLDU1NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM3MSw1NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzEsNTU4KTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTM3MSw1NTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzEsNTYxKTsKc2xlZXAoMzApOwpjcy5tb3VzZU1vdmUoMTM3MSw1NjIpOwpzbGVlcCg5NSk7CmNzLm1vdXNlTW92ZSgxMzcxLDU2NCk7CnNsZWVwKDE1KTsKY3MubW91c2VNb3ZlKDEzNzEsNTY1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzcxLDU2Nyk7CnNsZWVwKDIyKTsKY3MubW91c2VNb3ZlKDEzNzEsNTY4KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTM3MSw1NzApOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSgxMzcxLDU3MSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzNzEsNTczKTsKc2xlZXAoMjMpOwpjcy5tb3VzZU1vdmUoMTM3MSw1NzQpOwpzbGVlcCg2NCk7CmNzLm1vdXNlTW92ZSgxMzcxLDU3Nik7CnNsZWVwKDIxKTsKY3MubW91c2VNb3ZlKDEzNzEsNTc3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzcxLDU3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MSw1ODApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNzEsNTgyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzcxLDU4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MSw1ODUpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMzcxLDU4Nik7CnNsZWVwKDMwKTsKY3MubW91c2VNb3ZlKDEzNzEsNTg4KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTM3MSw1ODkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNzEsNTkxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzcxLDU5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MSw1OTQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNzEsNTk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzcxLDU5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3MSw2MDApOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDEzNzEsNjAxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzcxLDYwMyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzNzEsNjA0KTsKc2xlZXAoNjIpOwpjcy5tb3VzZU1vdmUoMTM3MSw2MDYpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxMzcxLDYwNyk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEzNzEsNjA5KTsKc2xlZXAoMTQwKTsKY3MubW91c2VNb3ZlKDEzNzEsNjEwKTsKc2xlZXAoMTg4KTsKY3MubW91c2VNb3ZlKDEzNzEsNjEyKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTM3MSw2MTMpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMzcxLDYxNSk7CnNsZWVwKDE0OCk7CmNzLm1vdXNlTGVmdFVwKDEzNzEsNjE1KTsKc2xlZXAoNTMpOwpjcy5tb3VzZU1vdmUoMTM3MCw2MTUpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxMzY5LDYxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM2Nyw2MTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzNjYsNjE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzY0LDYxNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM2MSw2MTUpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDEzNTcsNjE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzUyLDYxNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM0Niw2MTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNDIsNjE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzM5LDYxNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTMzNCw2MTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMzAsNjE0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzI3LDYxNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMxOSw2MTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMTUsNjE0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzEyLDYxMik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTMwNiw2MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMDEsNjEyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzAwLDYxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5OCw2MTIpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxMjk4LDYxMSk7CnNsZWVwKDUzKTsKY3MubW91c2VNb3ZlKDEyOTcsNjExKTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTI5NSw2MTEpOwpzbGVlcCgzOCk7CmNzLm1vdXNlTW92ZSgxMjk1LDYwOSk7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDEyOTQsNjA5KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoMTI5NCw2MDgpOwpzbGVlcCgyMyk7CmNzLm1vdXNlTW92ZSgxMjkyLDYwOCk7CnNsZWVwKDU0KTsKY3MubW91c2VNb3ZlKDEyOTIsNjA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkyLDYwMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI5MSw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTEsNjAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkxLDYwMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5MSw1OTkpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSgxMjkxLDU5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5MSw1OTYpOwpzbGVlcCg0NjIpOwpjcy5tb3VzZUxlZnREb3duKDEyOTEsNTk2KTsKc2xlZXAoODYpOwpjcy5tb3VzZUxlZnRVcCgxMjkxLDU5Nik7CnNsZWVwKDExMTYpOwpjcy5tb3VzZU1vdmUoMTI5Myw1OTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTYsNTk0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzAxLDU5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMwNSw1ODgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEzMTAsNTg3KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzE0LDU4NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMxNyw1ODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMjIsNTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzIzLDU3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMyNSw1NzgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzMjYsNTc2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzI4LDU3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTMzMSw1NzUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzMzQsNTczKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzM3LDU3Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM0MSw1NzApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzNDQsNTY5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzQ3LDU2Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM1Myw1NjMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNTUsNTYxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzU4LDU2MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM2MSw1NTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNjQsNTU0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzY3LDU1MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTM3Myw1NDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzksNTQyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMzg2LDUzNyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM5NSw1MzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDcsNTI1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDE5LDUxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQyOCw1MTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NDUsNTA0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDU4LDQ5NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQ2Nyw0OTEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE0ODEsNDgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDkwLDQ3Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTQ5Nyw0NzMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE1MDYsNDY3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTE3LDQ2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUyNCw0NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MjksNDUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTM2LDQ0Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU0Miw0NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NDgsNDM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTU0LDQyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1OSw0MTkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1NjgsNDA4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTcxLDQwNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU3OCwzOTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1ODYsMzg2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTkwLDM4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU5OSwzNzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MDcsMzYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjE2LDM1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTYyNSwzNDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MzEsMzQxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjQ0LDMyOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY1MCwzMjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NTgsMzEyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjY3LDMwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY3NCwyOTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2ODAsMjg3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjg2LDI3OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY5MiwyNzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE2OTcsMjY2KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNzAxLDI1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwMywyNTEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE3MDQsMjQ2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzA2LDI0Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwNywyMzcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE3MDcsMjM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzA3LDIzMyk7CnNsZWVwKDcwKTsKY3MubW91c2VNb3ZlKDE3MDcsMjMwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzA5LDIyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcxMCwyMTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MTAsMjEyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzEwLDIwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcxMCwxOTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MTAsMTkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzEwLDE4Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTcwOSwxODApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MDksMTc2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzA4LDE3MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTcwNiwxNjUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE3MDUsMTU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzA1LDE1Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTcwMywxNDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE3MDIsMTQzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNzAyLDE0MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwMiwxMzcpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDE3MDIsMTM0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNzAwLDEzMik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDE3MDAsMTMxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNzAwLDEyOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTcwMCwxMjgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE2OTksMTI2KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTY5OSwxMjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2OTksMTIzKTsKc2xlZXAoNjEpOwpjcy5tb3VzZU1vdmUoMTY5OSwxMjIpOwpzbGVlcCgxNyk7CmNzLm1vdXNlTW92ZSgxNjk3LDExOSk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTY5NywxMTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2OTcsMTE2KTsKc2xlZXAoNzEyKTsKY3MubW91c2VMZWZ0RG93bigxNjk3LDExNik7CnNsZWVwKDk1KTsKY3MubW91c2VMZWZ0VXAoMTY5NywxMTYpOwpzbGVlcCgxODYpOwpjcy5tb3VzZU1vdmUoMTY5NCwxMTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2OTEsMTIwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjg3LDEyNCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY4MSwxMjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NzgsMTMyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjcwLDE0MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY2NCwxNDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NTcsMTU0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjQ4LDE2Mik7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTYzOSwxNzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MzAsMTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjE4LDE4Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTYwMSwxOTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1ODgsMjA4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTczLDIxOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTU2MSwyMjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NDAsMjM4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTIzLDI0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUxMSwyNTUpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE0ODksMjY3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDc4LDI3NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ2MiwyODMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NTAsMjkxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDMyLDMwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQyMCwzMDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MDksMzE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzk2LDMyNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM4MSwzMzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNzUsMzM3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzYzLDM0NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTM1MSwzNTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNDUsMzU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzM2LDM2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMzMCwzNzApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzMjcsMzczKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzE5LDM4MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMxNiwzODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMDksMzkwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzA0LDM5Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI5NSw0MDApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEyODksNDA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjgyLDQwOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3NCw0MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjUsNDIzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjU4LDQzMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI1Miw0MzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNDQsNDQ0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjQwLDQ0OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIzMSw0NTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMjUsNDY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE5LDQ3MSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIxNCw0NzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMDgsNDgwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjA0LDQ4Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE5OSw0ODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExOTMsNDg5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTg5LDQ5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4MSw0OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzQsNDk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTY5LDQ5OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTE1OSw1MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNDQsNTAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI3LDUwMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExMSw1MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODUsNDk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDY3LDQ5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0Niw0ODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzYsNDgyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDI0LDQ3Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxNSw0NzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDksNDcxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDA0LDQ3MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAwMCw0NjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5NSw0NjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5Miw0NjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5Miw0NjUpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSg5OTEsNDY1KTsKc2xlZXAoNzApOwpjcy5tb3VzZU1vdmUoOTg4LDQ2MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTg1LDQ1OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgwLDQ1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTc5LDQ1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTc3LDQ1MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoOTc2LDQ0OSk7CnNsZWVwKDI1OCk7CmNzLm1vdXNlTGVmdERvd24oOTc2LDQ0OSk7CnNsZWVwKDU2KTsKY3MubW91c2VMZWZ0VXAoOTc2LDQ0OSk7CnNsZWVwKDU0KTsKY3MubW91c2VNb3ZlKDk3Nyw0NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4MCw0NTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5Miw0NjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5OCw0NjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTMsNDgwKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMDI2LDQ5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA0Myw1MDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNTYsNTE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDc3LDUzOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA4OSw1NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDksNTcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI4LDU5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE0OSw2MTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzAsNjMzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTk0LDY1Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIxNSw2NzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMzgsNjk3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjU5LDcyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI3OCw3NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTksNzYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzE2LDc4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMyOSw3OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzNDksODE0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMzY1LDgzMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTM3OSw4NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzOTUsODY1KTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxNDA5LDg3OSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQyMSw4OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MzQsOTA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDQ2LDkxOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ1OCw5MjcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0NjYsOTM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDc1LDk0Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ4Miw5NTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0ODgsOTYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDkzLDk3MCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTQ5Nyw5NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MDIsOTg4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTA2LDk5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUwOSwxMDAzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTExLDEwMDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MTQsMTAxNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTUxNywxMDIwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTE4LDEwMjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MjMsMTAyNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTUyNiwxMDI3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTMwLDEwMjcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE1MzUsMTAyOSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTUzNiwxMDI5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTQyLDEwMjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1NTEsMTAzMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1NywxMDMyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTY5LDEwMzUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1ODAsMTAzNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTU5MCwxMDM5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTk4LDEwNDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MTAsMTA0NCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoMTYyMiwxMDQ3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjMxLDEwNTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2MzcsMTA1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY0NiwxMDUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjUzLDEwNTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NjEsMTA1NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY2NywxMDU0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjcwLDEwNTYpOwpzbGVlcCg4Nik7CmNzLm1vdXNlTW92ZSgxNjcxLDEwNTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE2NzMsMTA1Nyk7CnNsZWVwKDIzKTsKY3MubW91c2VNb3ZlKDE2NzMsMTA1OSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTY3NCwxMDU5KTsKc2xlZXAoOTUpOwpjcy5tb3VzZU1vdmUoMTY3NiwxMDYwKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTY3NywxMDYwKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjc5LDEwNjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2ODAsMTA2Myk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDE2ODIsMTA2Myk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTY4MiwxMDY1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjgzLDEwNjUpOwpzbGVlcCgxMTApOwpjcy5tb3VzZU1vdmUoMTY4NSwxMDY1KTsKc2xlZXAoMTAyKTsKY3MubW91c2VNb3ZlKDE2ODUsMTA2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTY4NiwxMDY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNjg2LDEwNjgpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDE2ODgsMTA2OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTY4OSwxMDY5KTsKc2xlZXAoMjQpOwpjcy5tb3VzZU1vdmUoMTY5MSwxMDY5KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTY5MSwxMDcxKTsKc2xlZXAoMjMpOwpjcy5tb3VzZUxlZnREb3duKDE2OTEsMTA3MSk7CnNsZWVwKDg1KTsKY3MubW91c2VMZWZ0VXAoMTY5MSwxMDcxKTsKc2xlZXAoNzIpOwpjcy5tb3VzZU1vdmUoMTY4OCwxMDY4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNjc5LDEwNjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE2NzAsMTA1OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTY0OCwxMDQ0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNjIyLDEwMzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1ODgsMTAxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1NiwxMDA1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTIwLDk5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ5MCw5NzgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0NTAsOTYyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDExLDk0Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM3Niw5MzIpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzNDIsOTE3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMzEzLDkwMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI4OCw4ODcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNjQsODcyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjQzLDg1NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIyOCw4NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTEsODI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjAyLDgxOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE4OSw4MDMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExODMsNzk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTc4LDc5MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE3NCw3ODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNzIsNzg1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTcxLDc4NSk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDExNzEsNzgzKTsKc2xlZXAoNTMpOwpjcy5tb3VzZU1vdmUoMTE3MSw3ODQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExNzEsNzkyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTY1LDgwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE1Niw4MjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNTAsODI5KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTQ1LDgzNCk7CnNsZWVwKDQwNCk7CmNzLmtleURvd24oODIpOwpzbGVlcCgxNDQpOwpjcy5rZXlEb3duKDY5KTsKc2xlZXAoNjMpOwpjcy5rZXlVcCg4Mik7CnNsZWVwKDI1KTsKY3Mua2V5RG93big3OCk7CnNsZWVwKDQ3KTsKY3Mua2V5VXAoNjkpOwpzbGVlcCg0MSk7CmNzLmtleVVwKDc4KTsKc2xlZXAoNDApOwpjcy5rZXlEb3duKDcxKTsKc2xlZXAoMTEyKTsKY3Mua2V5RG93big3OSk7CnNsZWVwKDQwKTsKY3Mua2V5VXAoNzEpOwpzbGVlcCg0OCk7CmNzLmtleURvd24oNzgpOwpzbGVlcCg4MCk7CmNzLmtleVVwKDc5KTsKc2xlZXAoMjQpOwpjcy5rZXlEb3duKDcxKTsKc2xlZXAoNDApOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDEyOCk7CmNzLmtleVVwKDcxKTsKc2xlZXAoMTIwKTsKY3Mua2V5RG93big5MCk7CnNsZWVwKDk2KTsKY3Mua2V5VXAoOTApOwpzbGVlcCgxNTEpOwpjcy5rZXlEb3duKDcyKTsKc2xlZXAoODEpOwpjcy5rZXlEb3duKDczKTsKc2xlZXAoODApOwpjcy5rZXlVcCg3Mik7CnNsZWVwKDMxKTsKY3Mua2V5VXAoNzMpOwpzbGVlcCg3Myk7CmNzLmtleURvd24oNzgpOwpzbGVlcCgxMDQpOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDE2KTsKY3Mua2V5RG93big2OSk7CnNsZWVwKDU1KTsKY3Mua2V5RG93big3OCk7CnNsZWVwKDY1KTsKY3Mua2V5VXAoNjkpOwpzbGVlcCgzMSk7CmNzLmtleVVwKDc4KTsKc2xlZXAoMzMpOwpjcy5rZXlEb3duKDcxKTsKc2xlZXAoMTEyKTsKY3Mua2V5VXAoNzEpOwpzbGVlcCg2NCk7CmNzLmtleURvd24oMzIpOwpzbGVlcCg3MSk7CmNzLmtleVVwKDMyKTsKc2xlZXAoMzI5KTsKY3MubW91c2VNb3ZlKDExMzksODMwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTMyLDgyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEyNiw4MjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMTgsODIxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTE4LDgxNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTExNyw4MTYpOwpzbGVlcCgxMzkpOwpjcy5tb3VzZU1vdmUoMTExNyw4MTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMTgsODE1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTE5LDgxNSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTExOSw4MTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMjEsODEyKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgxMTIxLDgxMCk7CnNsZWVwKDEwKTsKY3MubW91c2VNb3ZlKDExMjEsODA3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTIyLDgwNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEyNCw4MDEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDExMjcsNzk4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTI4LDc5NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEzMCw3OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzEsNzkyKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTMzLDc5MSk7CnNsZWVwKDEwOSk7CmNzLm1vdXNlTW92ZSgxMTM0LDc5MSk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDExMzQsNzg5KTsKc2xlZXAoNzgpOwpjcy5tb3VzZUxlZnREb3duKDExMzQsNzg5KTsKc2xlZXAoNzcpOwpjcy5tb3VzZUxlZnRVcCgxMTM0LDc4OSk7CnNsZWVwKDEyNik7CmNzLm1vdXNlTW92ZSgxMTM0LDc5MCk7CnNsZWVwKDIyKTsKY3MubW91c2VNb3ZlKDExMzQsNzkyKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTEzNCw3OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzQsNzk1KTsKc2xlZXAoMzI2NSk7CmNzLm1vdXNlTW92ZSgxMTMyLDc5NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTEyNiw3OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMjAsNzk0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTE0LDc5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTEwOSw3OTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMDUsNzg4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTAyLDc4OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA5Nyw3ODYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEwOTEsNzg1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDg0LDc4Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTA3Niw3ODIpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDEwNjYsNzc3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDYzLDc3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTA1NCw3NzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDgsNzcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDQwLDc2Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAzNiw3NjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMzAsNzYyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDI0LDc1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAyMiw3NTgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEwMTksNzU1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDE1LDc1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAxMiw3NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDksNzUwKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMDA2LDc0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAwMCw3NDYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk5NCw3NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4OCw3NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4MCw3MzcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk3MSw3MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk2Miw3MjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk1Myw3MjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk0Myw3MjIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDkzNCw3MTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkxNyw3MTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkwOCw3MTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg5Myw3MDUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg3Nyw3MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg2Miw2OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1MSw2OTIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDgzNSw2ODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgyNiw2ODEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDgxNyw2NzgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDgwOCw2NzIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDc5OSw2NjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc5MSw2NjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDc4NCw2NTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc3OCw2NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc3MCw2NTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc2Myw2NDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc1NCw2NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc0Niw2MzYpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDczMSw2MzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDcyNCw2MjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDcwNyw2MTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY5Nyw2MTIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY4NSw2MDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY3Niw2MDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY2OCw1OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY2NCw1OTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDY1OCw1OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY1Myw1OTApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDY1MCw1ODgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDY0Nyw1ODcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY0NCw1ODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY0MSw1ODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzOCw1NzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzNSw1NzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzMiw1NzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYzMSw1NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyOCw1NzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYyNiw1NjkpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSg2MjYsNTY3KTsKc2xlZXAoMzMpOwpjcy5tb3VzZU1vdmUoNjI1LDU2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjI1LDU2Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjIyLDU2Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjIwLDU2Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjE2LDU1OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNjEzLDU1NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNjA4LDU1Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjA3LDU1MSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNjA1LDU0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNjA0LDU0OSk7CnNsZWVwKDEwKTsKY3MubW91c2VNb3ZlKDYwNCw1NDgpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDYwMiw1NDYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDYwMSw1NDYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU5OSw1NDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU5OCw1NDUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU5NSw1NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU5Miw1NDMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU4OSw1NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4Niw1NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4NCw1NDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4Myw1MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4MSw1MzkpOwpzbGVlcCg2MSk7CmNzLm1vdXNlTW92ZSg1ODAsNTM5KTsKc2xlZXAoMTcpOwpjcy5tb3VzZU1vdmUoNTc1LDUzNik7CnNsZWVwKDEwKTsKY3MubW91c2VNb3ZlKDU3Miw1MzQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDU2OSw1MzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU2OCw1MzMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU2Niw1MzEpOwpzbGVlcCg1NSk7CmNzLm1vdXNlTW92ZSg1NjYsNTMwKTsKc2xlZXAoMTApOwpjcy5tb3VzZU1vdmUoNTY1LDUzMCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNTYzLDUyOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTYwLDUyNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTU5LDUyNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTU3LDUyNCk7CnNsZWVwKDUpOwpjcy5tb3VzZU1vdmUoNTU2LDUyNCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTU2LDUyMik7CnNsZWVwKDEwNDgpOwpjcy5tb3VzZU1vdmUoNTU2LDUyMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTUzLDUxOCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTQ4LDUxMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTQxLDUwNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTM1LDUwMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTI5LDQ5Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNTI0LDQ5Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTIwLDQ4OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTE3LDQ4Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTE1LDQ4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTEyLDQ4Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTExLDQ4Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNTA4LDQ4MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNTAwLDQ3Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDk0LDQ3Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDg3LDQ2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDgyLDQ2Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDc2LDQ2NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDcwLDQ2MSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDY3LDQ1OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDY0LDQ1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDYxLDQ1Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDYwLDQ1NSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDYwLDQ1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU4LDQ1Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDU3LDQ1Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDU0LDQ0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQ5LDQ0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDQ1LDQ0MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDQwLDQzNyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDM2LDQzMik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDMxLDQyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDMwLDQyNik7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDI4LDQyMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDI3LDQyMyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI3LDQyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI1LDQxOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDI1LDQxNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDIyLDQxMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDE5LDQwNSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDE4LDQwMSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDE1LDM5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDEyLDM5MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDEwLDM4Nyk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNDA3LDM4Myk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNDA2LDM4MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNDA0LDM3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDAzLDM3NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNDAxLDM3Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzk4LDM2OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzk1LDM2Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzkyLDM2Mik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMzg5LDM2MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzg2LDM1Nik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzgzLDM1NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMzgyLDM1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMzgwLDM1MCk7CnNsZWVwKDc4KTsKY3MubW91c2VNb3ZlKDM3OSwzNDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM3NywzNDcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDM3NCwzNDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM3MSwzNDEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM2OCwzMzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM2NSwzMzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM2MiwzMzIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDM2MSwzMjkpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDM1NiwzMjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM1MywzMjEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDM0OSwzMTcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDM0NCwzMDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMzNSwyOTkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDMyOSwyODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMxOSwyNzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDMxMywyNjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDMwNywyNTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5OCwyNDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5MiwyNDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI4NCwyMzEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDI4MCwyMjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI3NSwyMjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI3MiwyMTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI2OSwyMTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDI2NiwyMTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI2NiwyMDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI2NSwyMDcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDI2MywyMDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI2MiwyMDMpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDI2MiwyMDEpOwpzbGVlcCg4Nik7CmNzLm1vdXNlTW92ZSgyNjAsMjAxKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyNTksMTk4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgyNTYsMTk0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyNTMsMTkxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNTAsMTg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNDUsMTgzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyNDIsMTgwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMzksMTc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMzgsMTc3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgyMzYsMTc2KTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMjM1LDE3NCk7CnNsZWVwKDMxKTsKY3MubW91c2VNb3ZlKDIzMywxNzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIzMCwxNzEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDIyNywxNjgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDIyNCwxNjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIyMSwxNjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIyMSwxNjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDIyMCwxNjEpOwpzbGVlcCg0MCk7CmNzLm1vdXNlTW92ZSgyMjAsMTU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMTgsMTU2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMTgsMTU1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgyMTUsMTUyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMTIsMTQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMDksMTQxKTsKc2xlZXAoNik7CmNzLm1vdXNlTW92ZSgyMDgsMTM3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMDUsMTM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMDMsMTMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMDIsMTMxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgyMDIsMTI5KTsKc2xlZXAoNzgpOwpjcy5tb3VzZU1vdmUoMjAyLDEyOCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjAyLDEyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjAyLDEyNSk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDIwMiwxMjMpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSgyMDAsMTIzKTsKc2xlZXAoNTcpOwpjcy5tb3VzZU1vdmUoMjAwLDEyMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMjAwLDEyMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMjAwLDExOSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMjAwLDExNyk7CnNsZWVwKDIxKTsKY3MubW91c2VNb3ZlKDIwMCwxMTYpOwpzbGVlcCg3MCk7CmNzLm1vdXNlTW92ZSgyMDAsMTEzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgyMDAsMTExKTsKc2xlZXAoMTUpOwpjcy5tb3VzZU1vdmUoMTk5LDExMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTk5LDEwOCk7CnNsZWVwKDE3KTsKY3MubW91c2VNb3ZlKDE5OSwxMDcpOwpzbGVlcCgyMDIpOwpjcy5tb3VzZU1vdmUoMTk3LDEwNyk7CnNsZWVwKDkzKTsKY3MubW91c2VNb3ZlKDE5NiwxMDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5NCwxMDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5MSwxMDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE5MCwxMDIpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSgxODgsMTAyKTsKc2xlZXAoMTg2KTsKY3MubW91c2VNb3ZlKDE4OSwxMDIpOwpzbGVlcCgxMTkpOwpjcy5tb3VzZU1vdmUoMTkxLDEwMik7CnNsZWVwKDEzMjgpOwpjcy5tb3VzZUxlZnREb3duKDE5MSwxMDIpOwpzbGVlcCgxMDgpOwpjcy5tb3VzZUxlZnRVcCgxOTEsMTAyKTsKc2xlZXAoMTk1KTsKY3MubW91c2VNb3ZlKDE5MiwxMDUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE5NywxMDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIwMywxMTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIxMCwxMjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDIxNSwxMjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDIyOCwxMzUpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDI1NCwxNTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDI2NywxNTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDI5MywxNzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDMxOCwxODYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDMzNSwxOTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM2MywyMDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM4MywyMTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDM5NiwyMjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQxMywyMjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQzNSwyMzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ1MiwyNDMpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDQ2NywyNDkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDQ4MywyNTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDQ5NywyNjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDUxNSwyNzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDUzMCwyNzkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU0OCwyODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU2MSwyOTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDU3MywzMDApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDU4NSwzMDcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDU5NiwzMTIpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDYxNCwzMTkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDYyMSwzMjEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDYzOCwzMjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY1MywzMzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY2MywzMzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDY4MywzNDMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDY5OCwzNDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDcwNCwzNTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDcxOSwzNTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDcyOSwzNjEpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDczOCwzNjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc0NiwzNjkpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDc1MCwzNzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc1NSwzNzUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDc1OSwzNzgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDc2MiwzODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc2NSwzODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc2OCwzODQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc3MCwzODUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDc3MywzODcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDc3NCwzODgpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDc3NiwzODgpOwpzbGVlcCgxMTgpOwpjcy5tb3VzZU1vdmUoNzc2LDM5MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoNzc3LDM5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzc5LDM5NCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoNzc5LDM5Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzc5LDM5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzc5LDM5OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoNzgwLDM5OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzgwLDQwMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoNzgwLDQwMik7CnNsZWVwKDE4OCk7CmNzLm1vdXNlTGVmdERvd24oNzgwLDQwMik7CnNsZWVwKDU0KTsKY3MubW91c2VNb3ZlKDc4MCw0MDEpOwpzbGVlcCgzMik7CmNzLm1vdXNlTGVmdFVwKDc4MCw0MDEpOwpzbGVlcCgxMjQpOwpjcy5tb3VzZVJpZ2h0RG93big3ODAsNDAxKTsKc2xlZXAoMTAyKTsKY3MubW91c2VSaWdodFVwKDc4MCw0MDEpOwpzbGVlcCgxMTApOwpjcy5tb3VzZU1vdmUoNzgwLDQwMik7CnNsZWVwKDI5KTsKY3MubW91c2VNb3ZlKDc4MCw0MDUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDc4Miw0MDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc4NSw0MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc4OCw0MjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDc5Miw0MjcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDc5Nyw0MzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgwMSw0NDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgwNCw0NDgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDgwOSw0NTcpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDgxMyw0NjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgxOCw0NzQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgyMiw0ODMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgyNSw0OTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgyOCw1MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgyOCw1MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgzMSw1MTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgzNCw1MjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgzNiw1MjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgzOSw1MzUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg0MCw1NDMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDg0Myw1NTApOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg0NSw1NTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg0OCw1NjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg0OSw1NjcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg0OSw1NzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1MSw1NzcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg1Miw1ODIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1Miw1ODgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1NCw1OTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1NCw1OTcpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDg1NSw2MDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1NSw2MDQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg1NSw2MDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1NSw2MTApOwpzbGVlcCgxNSk7CmNzLm1vdXNlTW92ZSg4NTUsNjEyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NTUsNjEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTUsNjE2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTUsNjE4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NTUsNjIxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NTUsNjIyKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg4NTQsNjI1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTQsNjI3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTQsNjMwKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoODU0LDYzMSk7CnNsZWVwKDI0KTsKY3MubW91c2VNb3ZlKDg1Myw2MzMpOwpzbGVlcCgyMik7CmNzLm1vdXNlTW92ZSg4NTMsNjM0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTEsNjM2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTEsNjM3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NTAsNjM5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTAsNjQyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NDgsNjQzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NDcsNjQ1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NDcsNjQ2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NDUsNjQ4KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NDUsNjQ5KTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg4NDQsNjUxKTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoODQyLDY1MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODQyLDY1Mik7CnNsZWVwKDI1KTsKY3MubW91c2VNb3ZlKDg0MSw2NTIpOwpzbGVlcCgzOCk7CmNzLm1vdXNlTW92ZSg4MzksNjUyKTsKc2xlZXAoNzgpOwpjcy5tb3VzZUxlZnREb3duKDgzOSw2NTIpOwpzbGVlcCg4MCk7CmNzLm1vdXNlTGVmdFVwKDgzOSw2NTIpOwpzbGVlcCgxMjQpOwpjcy5rZXlEb3duKDIwKTsKc2xlZXAoNzgpOwpjcy5tb3VzZU1vdmUoODM5LDY1NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODM5LDY1NSk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoODQwLDY2MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODQzLDY2Nik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoODQ1LDY3Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODQ2LDY3OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoODQ4LDY4Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODQ4LDY4NCk7CnNsZWVwKDUzKTsKY3Mua2V5VXAoMjApOwpzbGVlcCgzNzYpOwpjcy5rZXlEb3duKDcwKTsKc2xlZXAoMTA0KTsKY3Mua2V5VXAoNzApOwpzbGVlcCgxNTIpOwpjcy5rZXlEb3duKDU2KTsKc2xlZXAoODgpOwpjcy5rZXlVcCg1Nik7CnNsZWVwKDE1Mik7CmNzLmtleURvd24oODMpOwpzbGVlcCgxMjgpOwpjcy5rZXlVcCg4Myk7CnNsZWVwKDE5Mik7CmNzLmtleURvd24oMTg5KTsKc2xlZXAoODApOwpjcy5rZXlVcCgxODkpOwpzbGVlcCgxMTIpOwpjcy5rZXlEb3duKDIwKTsKc2xlZXAoMTI4KTsKY3Mua2V5VXAoMjApOwpzbGVlcCgzMDQpOwpjcy5rZXlEb3duKDg5KTsKc2xlZXAoMTExKTsKY3Mua2V5RG93big4NSk7CnNsZWVwKDE4KTsKY3Mua2V5VXAoODkpOwpzbGVlcCg4Nyk7CmNzLmtleVVwKDg1KTsKc2xlZXAoMTUpOwpjcy5rZXlEb3duKDgxKTsKc2xlZXAoMTIwKTsKY3Mua2V5VXAoODEpOwpzbGVlcCgyNSk7CmNzLmtleURvd24oNzMpOwpzbGVlcCg4OCk7CmNzLmtleURvd24oNzgpOwpzbGVlcCg1NSk7CmNzLmtleVVwKDczKTsKc2xlZXAoMjUpOwpjcy5rZXlEb3duKDcxKTsKc2xlZXAoMzIpOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDk2KTsKY3Mua2V5VXAoNzEpOwpzbGVlcCgzMik7CmNzLmtleURvd24oOTApOwpzbGVlcCgxMjcpOwpjcy5rZXlEb3duKDcyKTsKc2xlZXAoMTcpOwpjcy5rZXlVcCg5MCk7CnNsZWVwKDYzKTsKY3Mua2V5RG93big4NSk7CnNsZWVwKDk3KTsKY3Mua2V5VXAoNzIpOwpzbGVlcCgxNik7CmNzLmtleVVwKDg1KTsKc2xlZXAoMzkpOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoMTA1KTsKY3Mua2V5RG93big3Mik7CnNsZWVwKDYzKTsKY3Mua2V5VXAoODMpOwpzbGVlcCgzMyk7CmNzLmtleURvd24oNzkpOwpzbGVlcCg0Nyk7CmNzLmtleVVwKDcyKTsKc2xlZXAoMzMpOwpjcy5rZXlEb3duKDg1KTsKc2xlZXAoOTYpOwpjcy5rZXlVcCg3OSk7CnNsZWVwKDI0KTsKY3Mua2V5VXAoODUpOwpzbGVlcCg0MCk7CmNzLmtleURvd24oMzIpOwpzbGVlcCgxMTEpOwpjcy5rZXlVcCgzMik7CnNsZWVwKDM5MSk7CmNzLm1vdXNlTW92ZSg4NTEsNjc1KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NTUsNjY2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NTgsNjU3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NjAsNjU0KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjAsNjUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjAsNjUxKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoODYwLDY1MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODYwLDY0NCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODU5LDYzOCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoODU3LDYzMik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoODU3LDYyNik7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoODU3LDYyMCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoODU3LDYxNyk7CnNsZWVwKDg2KTsKY3MubW91c2VNb3ZlKDg1Nyw2MTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg2MCw2MTQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg2MSw2MTEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDg2Myw2MTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg2Myw2MDkpOwpzbGVlcCgxNik7CmNzLm1vdXNlTW92ZSg4NjMsNjA4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NjQsNjA2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjQsNjAzKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NjQsNTk3KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NjYsNTk0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NjcsNTg4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NjcsNTgyKTsKc2xlZXAoNSk7CmNzLm1vdXNlTW92ZSg4NzAsNTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NzIsNTczKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4NzUsNTY3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NzgsNTYzKTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSg4NzksNTYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4NzksNTU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4ODEsNTU3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSg4ODEsNTU1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSg4ODIsNTU0KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoODgyLDU1Mik7CnNsZWVwKDQ1KTsKY3MubW91c2VNb3ZlKDg4NCw1NTIpOwpzbGVlcCg4Nyk7CmNzLm1vdXNlTGVmdERvd24oODg0LDU1Mik7CnNsZWVwKDExMCk7CmNzLm1vdXNlTGVmdFVwKDg4NCw1NTIpOwpzbGVlcCg4NSk7CmNzLm1vdXNlUmlnaHREb3duKDg4NCw1NTIpOwpzbGVlcCg5Mik7CmNzLm1vdXNlUmlnaHRVcCg4ODQsNTUyKTsKc2xlZXAoMTAyKTsKY3MubW91c2VNb3ZlKDg4NSw1NTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg4Nyw1NjEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg4OCw1NjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg5MSw1NzMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg5Myw1NzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg5NCw1ODUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg5Nyw1OTIpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDg5OSw1OTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkwMyw2MDcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDkwOCw2MTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDkxNCw2MjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyMCw2MzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyMyw2NDMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyNyw2NTIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDkyOSw2NTcpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDkzMCw2NjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkzMiw2NjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDkzNSw2NzYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkzNiw2ODQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDkzOCw2OTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkzOSw2OTcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk0MSw3MDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0Miw3MDgpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk0NCw3MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0NSw3MTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0NSw3MTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk0Nyw3MjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0Nyw3MjMpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDk0OCw3MjcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MCw3MzIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk1MCw3MzUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk1MCw3MzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MCw3NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MCw3NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MCw3NDQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk1MCw3NDUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk1MCw3NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw3NTApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDk1MSw3NTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk1MSw3NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw3NTcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw3NjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw3NjIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk1MSw3NjUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk1MSw3NjYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw3NjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw3NzIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MSw3NzcpOwpzbGVlcCg1KTsKY3MubW91c2VNb3ZlKDk1MCw3ODEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk1MCw3ODYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0OSw3ODkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0OSw3OTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk0Nyw3OTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0Nyw4MDQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0Niw4MDcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk0Niw4MDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0Niw4MTApOwpzbGVlcCgyMjYpOwpjcy5tb3VzZUxlZnREb3duKDk0Niw4MTApOwpzbGVlcCg3OCk7CmNzLm1vdXNlTGVmdFVwKDk0Niw4MTApOwpzbGVlcCg4ODIpOwpjcy5rZXlEb3duKDg0KTsKc2xlZXAoMTIwKTsKY3Mua2V5VXAoODQpOwpzbGVlcCg0MCk7CmNzLmtleURvd24oNzkpOwpzbGVlcCgxMTEpOwpjcy5rZXlEb3duKDc4KTsKc2xlZXAoODkpOwpjcy5rZXlVcCg3OSk7CnNsZWVwKDI0KTsKY3Mua2V5VXAoNzgpOwpzbGVlcCgyNCk7CmNzLmtleURvd24oNzEpOwpzbGVlcCg3OSk7CmNzLmtleVVwKDcxKTsKc2xlZXAoODEpOwpjcy5rZXlEb3duKDcxKTsKc2xlZXAoOTYpOwpjcy5rZXlEb3duKDg1KTsKc2xlZXAoMjMpOwpjcy5rZXlVcCg3MSk7CnNsZWVwKDU3KTsKY3Mua2V5RG93big3OSk7CnNsZWVwKDcyKTsKY3Mua2V5VXAoODUpOwpzbGVlcCg0OCk7CmNzLmtleVVwKDc5KTsKc2xlZXAoMjMxKTsKY3Mua2V5RG93bigzMik7CnNsZWVwKDk2KTsKY3Mua2V5VXAoMzIpOwpzbGVlcCgxMDUpOwpjcy5rZXlEb3duKDcxKTsKc2xlZXAoMTQ0KTsKY3Mua2V5RG93big2OSk7CnNsZWVwKDY0KTsKY3Mua2V5VXAoNzEpOwpzbGVlcCgyNCk7CmNzLmtleVVwKDY5KTsKc2xlZXAoMTUyKTsKY3Mua2V5RG93big5MCk7CnNsZWVwKDEwNCk7CmNzLmtleVVwKDkwKTsKc2xlZXAoMjQpOwpjcy5rZXlEb3duKDcyKTsKc2xlZXAoNTUpOwpjcy5rZXlEb3duKDc5KTsKc2xlZXAoMzMpOwpjcy5rZXlVcCg3Mik7CnNsZWVwKDEwNCk7CmNzLmtleURvd24oNzgpOwpzbGVlcCg1Nik7CmNzLmtleVVwKDc5KTsKc2xlZXAoMzIpOwpjcy5rZXlEb3duKDcxKTsKc2xlZXAoNDgpOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDI0KTsKY3Mua2V5RG93bigzMik7CnNsZWVwKDgwKTsKY3Mua2V5VXAoNzEpOwpzbGVlcCg0OCk7CmNzLmtleVVwKDMyKTsKc2xlZXAoNTYpOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoOTYpOwpjcy5rZXlEb3duKDc5KTsKc2xlZXAoNDApOwpjcy5rZXlVcCg4Myk7CnNsZWVwKDQ4KTsKY3Mua2V5RG93big4NSk7CnNsZWVwKDk2KTsKY3Mua2V5VXAoNzkpOwpzbGVlcCgyNCk7CmNzLmtleVVwKDg1KTsKc2xlZXAoNDApOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoMTI4KTsKY3Mua2V5RG93big4NSk7CnNsZWVwKDE2KTsKY3Mua2V5VXAoODMpOwpzbGVlcCg3Mik7CmNzLmtleURvd24oNzkpOwpzbGVlcCg3MSk7CmNzLmtleVVwKDg1KTsKc2xlZXAoNDkpOwpjcy5rZXlVcCg3OSk7CnNsZWVwKDQ3KTsKY3Mua2V5RG93big4OSk7CnNsZWVwKDEyMSk7CmNzLmtleVVwKDg5KTsKc2xlZXAoMjQpOwpjcy5rZXlEb3duKDczKTsKc2xlZXAoNjQpOwpjcy5rZXlEb3duKDc4KTsKc2xlZXAoNDcpOwpjcy5rZXlVcCg3Myk7CnNsZWVwKDU3KTsKY3Mua2V5RG93big4MSk7CnNsZWVwKDI0KTsKY3Mua2V5VXAoNzgpOwpzbGVlcCg4Nyk7CmNzLmtleVVwKDgxKTsKc2xlZXAoNDEpOwpjcy5rZXlEb3duKDczKTsKc2xlZXAoNzIpOwpjcy5rZXlEb3duKDc4KTsKc2xlZXAoNzkpOwpjcy5rZXlVcCg3Myk7CnNsZWVwKDQxKTsKY3Mua2V5RG93big3MSk7CnNsZWVwKDE2KTsKY3Mua2V5VXAoNzgpOwpzbGVlcCg2NCk7CmNzLmtleURvd24oMzIpOwpzbGVlcCg3MSk7CmNzLmtleVVwKDcxKTsKc2xlZXAoNDkpOwpjcy5rZXlVcCgzMik7CnNsZWVwKDcxKTsKY3Mua2V5RG93big3NCk7CnNsZWVwKDgxKTsKY3Mua2V5RG93big3Myk7CnNsZWVwKDM5KTsKY3Mua2V5VXAoNzQpOwpzbGVlcCg3Myk7CmNzLmtleURvd24oNzgpOwpzbGVlcCg0Nyk7CmNzLmtleVVwKDczKTsKc2xlZXAoNzIpOwpjcy5rZXlVcCg3OCk7CnNsZWVwKDE3KTsKY3Mua2V5RG93big4OCk7CnNsZWVwKDExMik7CmNzLmtleURvd24oNzMpOwpzbGVlcCg0MCk7CmNzLmtleVVwKDg4KTsKc2xlZXAoMTUpOwpjcy5rZXlEb3duKDc4KTsKc2xlZXAoNzMpOwpjcy5rZXlVcCg3Myk7CnNsZWVwKDIzKTsKY3Mua2V5RG93big3MSk7CnNsZWVwKDI0KTsKY3Mua2V5VXAoNzgpOwpzbGVlcCg0OCk7CmNzLmtleURvd24oMzIpOwpzbGVlcCg3Myk7CmNzLmtleVVwKDcxKTsKc2xlZXAoNDcpOwpjcy5rZXlVcCgzMik7CnNsZWVwKDI4MSk7CmNzLmtleURvd24oODkpOwpzbGVlcCg4MCk7CmNzLmtleURvd24oODUpOwpzbGVlcCg0OCk7CmNzLmtleVVwKDg5KTsKc2xlZXAoNTUpOwpjcy5rZXlVcCg4NSk7CnNsZWVwKDMzKTsKY3Mua2V5RG93big4MSk7CnNsZWVwKDExMSk7CmNzLmtleURvd24oNzMpOwpzbGVlcCgyNSk7CmNzLmtleVVwKDgxKTsKc2xlZXAoNTUpOwpjcy5rZXlEb3duKDc4KTsKc2xlZXAoODApOwpjcy5rZXlVcCg3Myk7CnNsZWVwKDQwKTsKY3Mua2V5RG93big3MSk7CnNsZWVwKDE2KTsKY3Mua2V5VXAoNzgpOwpzbGVlcCgxMDQpOwpjcy5rZXlVcCg3MSk7CnNsZWVwKDQwKTsKY3Mua2V5RG93bigzMik7CnNsZWVwKDEyOSk7CmNzLmtleVVwKDMyKTsKc2xlZXAoNTYpOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoNzIpOwpjcy5rZXlEb3duKDc5KTsKc2xlZXAoNTUpOwpjcy5rZXlVcCg4Myk7CnNsZWVwKDI0KTsKY3Mua2V5RG93big4NSk7CnNsZWVwKDg5KTsKY3Mua2V5VXAoNzkpOwpzbGVlcCgzMik7CmNzLmtleVVwKDg1KTsKc2xlZXAoMTYpOwpjcy5rZXlEb3duKDgzKTsKc2xlZXAoODApOwpjcy5rZXlEb3duKDg1KTsKc2xlZXAoMjQpOwpjcy5rZXlVcCg4Myk7CnNsZWVwKDYzKTsKY3Mua2V5RG93big3OSk7CnNsZWVwKDQ5KTsKY3Mua2V5VXAoODUpOwpzbGVlcCg0MCk7CmNzLmtleURvd24oMzIpOwpzbGVlcCgzMSk7CmNzLmtleVVwKDc5KTsKc2xlZXAoODApOwpjcy5rZXlVcCgzMik7CnNsZWVwKDE3Nyk7CmNzLmtleURvd24oMTkwKTsKc2xlZXAoOTYpOwpjcy5rZXlVcCgxOTApOwpzbGVlcCg1MDcpOwpjcy5tb3VzZU1vdmUoOTUzLDgwMyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoOTYyLDc5Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTY5LDc5MSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTc3LDc4NSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoOTgwLDc4Mik7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoOTgxLDc4MCk7CnNsZWVwKDM5KTsKY3MubW91c2VNb3ZlKDk4MSw3NzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4MSw3NzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk3Myw3NzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk2OCw3NjUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk1NSw3NTUpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk0NCw3NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkzNCw3NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyOCw3MzcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyNSw3MzQpOwpzbGVlcCgxNCk7CmNzLm1vdXNlTW92ZSg5MjUsNzMyKTsKc2xlZXAoNDApOwpjcy5tb3VzZU1vdmUoOTIzLDczMik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDkxOSw3MjkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDkxMyw3MjYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDkwNCw3MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg5Miw3MTkpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDg3OCw3MTYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg2OCw3MTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg2MCw3MTEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1Myw3MTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg0Nyw3MTApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg0NCw3MDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg0MSw3MDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgzOSw3MDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgzNiw3MDQpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDgzMyw3MDEpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDgyNyw2OTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDgyNCw2OTYpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgxOCw2OTUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDgxNSw2OTMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDgxNCw2OTMpOwpzbGVlcCgxODApOwpjcy5tb3VzZUxlZnREb3duKDgxNCw2OTMpOwpzbGVlcCg4Nik7CmNzLm1vdXNlTGVmdFVwKDgxNCw2OTMpOwpzbGVlcCg5NDU4KTsKY3MubW91c2VNb3ZlKDgxOCw2OTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDgyOCw2OTkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDgzNiw3MDIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg0Niw3MDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg1NSw3MDkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg2Nyw3MTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg3Miw3MTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDg4MSw3MTcpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDg4OCw3MjApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDg5Nyw3MjMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkwMyw3MjQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkxNSw3MjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkyMSw3MzApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDkzMyw3MzMpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDk0MSw3MzUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk0Nyw3MzgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1MCw3MzgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk1Myw3MzkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1NCw3MzkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDk1NCw3NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1Niw3NDEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk1OSw3NDEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDk2NSw3NDIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk3MSw3NDQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDk3OCw3NDUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk4NCw3NDcpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDk5Myw3NDgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMDEsNzUxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDA4LDc1Myk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTAxNCw3NTQpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwMTksNzU0KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMDI2LDc1Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTAzOCw3NjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwNDksNzYzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMDYxLDc2OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTA3NCw3NzEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEwODYsNzc0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxMTAwLDc3OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTExOSw3ODMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExMzMsNzg3KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMTQzLDc5MCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE1NSw3OTMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDExNjEsNzk1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTcwLDc5OCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTE3Niw4MDEpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExNzksODAyKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMTg1LDgwNSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTE5MSw4MTApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDExOTksODEzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjAzLDgxNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIwOCw4MTkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMTIsODIwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjE0LDgyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIxNCw4MjMpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyMTUsODIzKTsKc2xlZXAoMTQpOwpjcy5tb3VzZU1vdmUoMTIxNyw4MjUpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMTgsODI1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjIwLDgyNik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTIyMSw4MjYpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMjMsODI2KTsKc2xlZXAoMTI3KTsKY3MubW91c2VNb3ZlKDEyMjQsODI2KTsKc2xlZXAoMTI0KTsKY3MubW91c2VNb3ZlKDEyMjYsODI1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjI3LDgyNCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTIyOSw4MjIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyMzAsODIyKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxMjMyLDgyMSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTIzMiw4MTkpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyMzMsODE5KTsKc2xlZXAoMjgzKTsKY3MubW91c2VNb3ZlKDEyMzMsODE4KTsKc2xlZXAoNzcpOwpjcy5tb3VzZUxlZnREb3duKDEyMzMsODE4KTsKc2xlZXAoODcpOwpjcy5tb3VzZUxlZnRVcCgxMjMzLDgxOCk7CnNsZWVwKDMwMyk7CmNzLm1vdXNlTW92ZSgxMjM4LDgyMCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI0Miw4MjUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNDcsODI4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjUxLDgzMSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI1Niw4MzQpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEyNTksODM3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjU5LDgzOCk7CnNsZWVwKDEzKTsKY3MubW91c2VNb3ZlKDEyNjAsODM4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjYwLDg0MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI2Miw4NDApOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDEyNjIsODQxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjYzLDg0Myk7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyNjUsODQ0KTsKc2xlZXAoMjUpOwpjcy5tb3VzZU1vdmUoMTI2Niw4NDYpOwpzbGVlcCgyMSk7CmNzLm1vdXNlTW92ZSgxMjY4LDg0Nyk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTI2OSw4NDkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNzIsODUwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjc0LDg1MCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTI3Nyw4NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyNzgsODUzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjgxLDg1NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTI4NCw4NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyODcsODU4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjkwLDg1OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTI5Miw4NjEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEyOTMsODYxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMjk1LDg2Mik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDEyOTYsODY0KTsKc2xlZXAoMTM4Myk7CmNzLm1vdXNlTW92ZSgxMzA0LDg2OCk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTMxMSw4NzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDEzMjIsODc5KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzMxLDg4NSk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM0Myw4OTEpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDEzNTUsODk3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxMzY3LDkwMyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTM3Nyw5MTApOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDEzODYsOTE1KTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNDAwLDkyMik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQwOSw5MjgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0MTYsOTMzKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDI1LDkzNyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQzMyw5NDIpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0MzksOTQ2KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDQ1LDk0OSk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTQ0OSw5NTIpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NTQsOTU0KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDU1LDk1NCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ1OCw5NTUpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NjEsOTU3KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDYzLDk1Nyk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTQ2NCw5NTgpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE0NjYsOTU4KTsKc2xlZXAoMTYpOwpjcy5tb3VzZU1vdmUoMTQ2Nyw5NTgpOwpzbGVlcCgyNCk7CmNzLm1vdXNlTW92ZSgxNDY5LDk1OCk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTQ3MCw5NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0NzMsOTU4KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNDgxLDk1OCk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTQ4Miw5NTgpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE0ODgsOTYwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNDk2LDk2MCk7CnNsZWVwKDcpOwpjcy5tb3VzZU1vdmUoMTUwMyw5NjApOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1MTEsOTYxKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTE4LDk2Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTUyMyw5NjQpOwpzbGVlcCg2KTsKY3MubW91c2VNb3ZlKDE1MjksOTY2KTsKc2xlZXAoNyk7CmNzLm1vdXNlTW92ZSgxNTMzLDk2Nyk7CnNsZWVwKDkpOwpjcy5tb3VzZU1vdmUoMTUzOSw5NjkpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NDQsOTcwKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTUwLDk3Mik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU1Niw5NzIpOwpzbGVlcCg3KTsKY3MubW91c2VNb3ZlKDE1NjAsOTczKTsKc2xlZXAoOSk7CmNzLm1vdXNlTW92ZSgxNTY1LDk3Myk7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU2OCw5NzMpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1NzEsOTc1KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTc0LDk3NSk7CnNsZWVwKDg2KTsKY3MubW91c2VNb3ZlKDE1NzUsOTc1KTsKc2xlZXAoMjEpOwpjcy5tb3VzZU1vdmUoMTU3Nyw5NzUpOwpzbGVlcCgzMik7CmNzLm1vdXNlTW92ZSgxNTc3LDk3Nik7CnNsZWVwKDgpOwpjcy5tb3VzZU1vdmUoMTU3OCw5NzYpOwpzbGVlcCg5KTsKY3MubW91c2VNb3ZlKDE1ODAsOTc4KTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTgxLDk3OSk7CnNsZWVwKDYpOwpjcy5tb3VzZU1vdmUoMTU4NCw5ODEpOwpzbGVlcCg4KTsKY3MubW91c2VNb3ZlKDE1ODYsOTgxKTsKc2xlZXAoOCk7CmNzLm1vdXNlTW92ZSgxNTg3LDk4Mik7CnNsZWVwKDE2KTsKY3MubW91c2VNb3ZlKDE1ODksOTg0KTsKc2xlZXAoNzk2KTsKY3MubW91c2VMZWZ0RG93bigxNTg5LDk4NCk7CnNsZWVwKDEwMSk7CmNzLm1vdXNlTGVmdFVwKDE1ODksOTg0KTs=', NULL, '2025-04-10 14:09:09', NULL, '策元官方', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for wc_node_log
-- ----------------------------
DROP TABLE IF EXISTS `wc_node_log`;
CREATE TABLE `wc_node_log` (
  `id` varchar(36) NOT NULL COMMENT '主建ID',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
  `user_prompt` longtext COMMENT '用户指令',
  `res` longtext COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_node_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_office_account
-- ----------------------------
DROP TABLE IF EXISTS `wc_office_account`;
CREATE TABLE `wc_office_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` varchar(50) DEFAULT NULL COMMENT '开发者id',
  `app_secret` varchar(50) DEFAULT NULL COMMENT '开发者密码',
  `office_account_name` varchar(50) DEFAULT NULL COMMENT '公众号名称',
  `user_id` bigint(20) DEFAULT NULL COMMENT '登录用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '登录用户名称',
  `media_id` varchar(100) DEFAULT NULL COMMENT '素材id',
  `pic_url` varchar(255) DEFAULT NULL COMMENT '图片连接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_office_account
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `wc_order_detail`;
CREATE TABLE `wc_order_detail` (
  `id` varchar(36) DEFAULT NULL COMMENT '主建ID',
  `order_id` varchar(36) DEFAULT NULL COMMENT '订单ID',
  `openid` varchar(36) DEFAULT NULL,
  `unionid` varchar(36) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL COMMENT '订单状态',
  `ishx` int(4) DEFAULT NULL COMMENT '是否核销',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `product_infos` longtext COMMENT '商品信息',
  `create_time` varchar(10) DEFAULT NULL COMMENT '下单时间',
  `update_time` varchar(10) DEFAULT NULL COMMENT '更新时间',
  `url_link` varchar(255) DEFAULT NULL COMMENT '专属链接'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_order_detail
-- ----------------------------
BEGIN;
INSERT INTO `wc_order_detail` (`id`, `order_id`, `openid`, `unionid`, `status`, `ishx`, `phone`, `product_infos`, `create_time`, `update_time`, `url_link`) VALUES ('d6e5a81c-c684-11ef-85e4-525400862564', '3725049385933478401', 'oj42r4lnKjMsfF2AVOUSTEKfr088', 'o3lds66Rl8LdGHzEluKva0S5d8Gw', '30', 1, '18538098382', '[{\"product_id\":\"10000171244403\",\"sku_id\":\"2824176918\",\"thumb_img\":\"https://store.mp.video.tencent-cloud.com/161/20304/snscosdownload/SH/reserved/6751664b000286641d8d33acdc82bc1e000000a000004f50\",\"sale_price\":9900,\"sku_cnt\":1,\"title\":\"元透社AI内容创作学习卡\",\"on_aftersale_sku_cnt\":0,\"finish_aftersale_sku_cnt\":0,\"sku_code\":\"\",\"market_price\":9900,\"sku_attrs\":[],\"real_price\":9900,\"is_discounted\":false,\"estimate_price\":9900,\"out_warehouse_id\":\"\",\"sku_deliver_info\":{\"stock_type\":0},\"extra_service\":{\"seven_day_return\":0,\"freight_insurance\":0},\"voucher_list\":[],\"order_product_coupon_info_list\":[],\"package_sku_list\":[],\"delivery_deadline\":1735718709,\"merchant_discounted_price\":0,\"finder_discounted_price\":0,\"product_unique_id\":\"3725049385933478401_001\"}]', '1735545903', '1735545929', NULL);
COMMIT;

-- ----------------------------
-- Table structure for wc_playwright_draft
-- ----------------------------
DROP TABLE IF EXISTS `wc_playwright_draft`;
CREATE TABLE `wc_playwright_draft` (
    `id` varchar(36) NOT NULL COMMENT '草稿ID',
    `task_id` varchar(255) DEFAULT NULL COMMENT '任务id',
    `keyword` longtext COMMENT '主题词',
    `user_prompt` longtext COMMENT '用户指令-封装版',
    `draft_content` longtext COMMENT '草稿内容',
    `is_push` int(4) DEFAULT NULL COMMENT '是否已经推送过公众号',
    `ai_name` varchar(255) DEFAULT NULL COMMENT '来源',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `user_name` bigint(4) DEFAULT '0' COMMENT '创建人',
    `share_url` varchar(255) DEFAULT NULL COMMENT '分享链接',
    `share_img_url` varchar(255) DEFAULT NULL COMMENT '分享图片链接',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of wc_playwright_draft
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_playwright_task
-- ----------------------------
DROP TABLE IF EXISTS `wc_playwright_task`;
CREATE TABLE `wc_playwright_task` (
  `id` varchar(255) DEFAULT NULL COMMENT '主建ID',
  `task_id` varchar(255) DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `order` int(4) DEFAULT NULL COMMENT '任务排序',
  `start_time` datetime DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务完成时间',
  `plan_time` varchar(255) DEFAULT NULL COMMENT '预计执行时间',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `corp_id` varchar(255) DEFAULT NULL COMMENT '企业ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_playwright_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_host_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `sys_host_whitelist`;
CREATE TABLE `sys_host_whitelist` (
   id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
   host_id VARCHAR(50) NOT NULL UNIQUE COMMENT '主机ID',
   status TINYINT DEFAULT 1 COMMENT '是否启用：1启用，0禁用',
   remark VARCHAR(100) COMMENT '备注',
   create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
   update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- ----------------------------
-- Records of sys_host_whitelist
-- ----------------------------
BEGIN;
COMMIT;
-- ----------------------------
-- Table structure for wc_points_record
-- ----------------------------
DROP TABLE IF EXISTS `wc_points_record`;
CREATE TABLE `wc_points_record` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `corp_id` varchar(36) DEFAULT NULL COMMENT '企业ID',
  `corp_name` varchar(255) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `nick_name` varchar(255) DEFAULT NULL,
  `change_amount` int(5) DEFAULT NULL COMMENT '变更金额',
  `balance_after` int(5) DEFAULT NULL COMMENT '变更后余额',
  `change_type` varchar(36) DEFAULT NULL COMMENT '变动类型：\r\n1 充值 2 赠送 3 使用 AI 助理\r\n消耗 4 获取研报消耗',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_id` varchar(255) DEFAULT NULL COMMENT '操作人ID',
  `create_name` varchar(255) DEFAULT NULL COMMENT '操作人名称',
  `create_time` datetime DEFAULT NULL COMMENT '变动时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='我的积分明细';

-- ----------------------------
-- Records of wc_points_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_prompt_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_prompt_template`;
CREATE TABLE `wc_prompt_template` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY COMMENT '主建ID',
  `prompt` longtext COMMENT '模板',
  `name` varchar(255) DEFAULT NULL COMMENT '模板名称',
  `type` int(3) DEFAULT NULL COMMENT '模板类型 1 附加提示词 2.用户偏好',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `isdel` int(3) DEFAULT 0 COMMENT '是否删除 1是0否',
  `is_common` tinyint(1) NULL DEFAULT 0 COMMENT '是否为公共模板(0:个人 1:公共)',
  INDEX `idx_is_common`(`is_common`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 为评分模板表添加上架市场相关字段
-- 注意：如果字段已存在，这些语句会报错，可以忽略或手动检查
-- 可以通过以下方式检查：SHOW COLUMNS FROM `wc_prompt_template` LIKE 'status';
ALTER TABLE `wc_prompt_template`
ADD COLUMN `status` int(1) DEFAULT 0 COMMENT '上架状态：0-草稿 1-已上架' AFTER `is_common`,
ADD COLUMN `price` decimal(10,2) DEFAULT 0.00 COMMENT '模板价格' AFTER `status`,
ADD COLUMN `sales_count` int(11) DEFAULT 0 COMMENT '销量' AFTER `price`,
ADD COLUMN `income_total` decimal(10,2) DEFAULT 0.00 COMMENT '累计收益' AFTER `sales_count`;

-- 添加索引（如果不存在会报错，可以忽略）
ALTER TABLE `wc_prompt_template`
ADD INDEX `idx_status`(`status`) USING BTREE;

-- ----------------------------
-- Records of wc_prompt_template
-- ----------------------------
BEGIN;
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (1, '请你深度阅读以下几篇内容，从多个维度进行逐项打分，输出评分结果。并在以下各篇文章的基础上博采众长，综合整理一篇更全面的文章。', '评分模板1', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (2, '请你深度阅读以下算法代码，从时间复杂度和空间复杂度进行评估，结合计算复杂性理论，并提出建议。', '评分模板2', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (3, '请你赏析如下文章，写一份评论。', '评分模板3', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (8, '请将以下来自不同AI的回复片段进行智能拼接，生成一个连贯且无重复的最终答案。要求：\n\n保留各回复中最专业、最有趣或最实用的部分\n确保逻辑过渡自然\n总长度控制在3000字以内', '大福-3K混搭专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (10, '你是一个AI未来预测专家，现在需要处理来自多个AI模型对同一问题的回复。请先分析每个回答的当前水平和局限性，然后预测这些回答在未来1-3年可能的进化方向。基于这些预测，生成一个\"未来进化版\"答案：\n1. 包含当前回答中的核心价值\n2. 融入预测中可能出现的创新点\n3. 添加一个基于未来技术发展的新视角\n4. 用\"如果现在是2026年，这个答案将如何进化\"的框架来组织内容\n5.确保最终答案展现出前瞻性，同时保持实用性。', '二福-答案进化高手', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (11, '你是一个\"深度思考\"模式的AI评估专家，现在需要处理来自不同AI对同一问题的回复。请先分析每个回答的核心观点、逻辑结构和表达特点，然后运用深度思考模式，将这些回答融合成一个更全面、更有洞察力的答案。在融合过程中，特别关注以下几点：\n1. 识别并整合各回答中的独特见解\n2. 修正逻辑漏洞，补充关键细节\n3. 添加一个全新的、有深度的视角\n4. 确保语言流畅且专业\n5.最终生成的答案应比任何单一AI的回答都更出色，展现出超越原始回答的深度和广度。', '三福-深思爱好者', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (13, '你是一位知识图谱构建师和教育专家。以下多个AI对同一问题的回答。\n你的任务是：\n\n整合与去重： 将所有回答中提到的知识点进行整合，去除重复项。\n构建层级关系： 分析这些知识点之间的逻辑关系（如：前置知识、核心概念、进阶分支），构建一个多层级的学习路径图。\n输出结构化数据：输出知识图谱。顶层是最高阶的知识，逐层向下展开到最基础的先修课程。在每个知识点旁边，可以标注它主要来源于哪个AI的回答。\n请开始构建这个知识图谱。', '四福-知识图谱构建师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (14, '你是一位富有想象力的创意总监。以下分别是不同AI对同一问题的回答。\n\n你的任务是：\n\n拆解元素： 从每个提案中，识别出最关键的词汇、意象、情感或概念（例如，X的"静谧"，Y的"连接"，Z的"冒险"）。\n创意融合： 将这些来自不同提案的精华元素进行重组、融合，创造出3个全新的、独一无二的回答。每个新方案都必须包含至少两个不同AI提案的灵感。\n阐述创意： 为每一个新方案写一段简短的创意阐述，说明它融合了哪些元素，以及为什么这样融合更有吸引力。\n请开始你的创作。', '五福-AI创意总监', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (15, '请你作为"A纠错专家"，指出以下多个AI回答中的潜在事实错误、逻辑漏洞或虚假引用，并给出正确的替代表述。', '六福-AI纠错专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (16, '请你从以下多个AI回答中，提炼出"核心洞见Top 5"，并用一句金句总结每一点。要求：不重复、可引用、有启发性。', '七福-洞见蒸馏高手', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (17, '请你将以下AI回答当作"原材料"，像炼丹术士一样重新熔炼：提取其中的思想元素（逻辑、比喻、案例、结论），重组为一个更高层次、更具洞见的新版本。要求新文本具备"合成智慧"的特征，而非简单融合。', '八福-AI炼丹师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (18, '你的任务是作为一名"一致性分析师"，评估以下多个AI回答在核心观点上的一致性。请仔细阅读所有回答，并完成以下任务：\n1、观点聚类：将所有回答中提到的核心观点进行归类。将语义相同或高度相似的观点归为一类。\n2、频率统计：统计每个观点类别被提及的次数。\n3、一致性评估：基于观点的频率分布，评估所有回答的整体一致性。如果大部分观点都集中在少数几个类别中，则认为一致性高；如果观点分散，则认为一致性低。\n4、最终输出你的分析结果。', '九福-一致性分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (19, '你的任务是作为\"质量评估师\"，对以下多个AI回答进行质量分级。请完成：\n维度拆解：从\"专业深度、逻辑结构、证据充分性、表达清晰度\"四个维度建立评估框架。\n逐项打分：为每个回答的每个维度打1-10分，并说明扣分原因。\n综合排序：按加权平均分排序，标注\"最佳参考\"和\"待优化\"回答。\n输出评分表：用文本表格呈现结果，并附上2条改进建议。', '十福-质量评估师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (20, '你的任务是作为\"视角猎手\"，挖掘回答中的独特思维角度。请完成：\n视角识别：提取每个回答中隐含的认知视角（如：用户视角、工程师视角、伦理学家视角等）。\n独特性评级：用\"常见/新颖/罕见\"三级标签标注每个视角。\n盲区诊断：指出所有回答共同忽略的视角，并分析原因。\n输出视角地图：以文本描述形式，呈现包含发现视角、缺失视角、关联建议的思维导图。', '十一福-视角猎手', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (21, '你的任务是作为\"逻辑架构师\"，重构回答的论证体系。请完成：\n论点拆解：将每个回答拆解为\"前提-推理-结论\"三要素。\n漏洞扫描：识别逻辑谬误（如：因果倒置、以偏概全、滑坡谬误）。\n最优合成：选择最健壮的前提和最严密的推理，构建一个新论证链。\n输出诊断报告：列出每个回答的1个关键漏洞，并呈现优化后的逻辑链条。', ' 十二福-逻辑架构师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (22, '你的任务是作为\"创新萃取师\"，提炼回答中的创造性元素。请完成：\n创意识别：标记每个回答中的原创观点、独特类比或非常规方案。\n创新评级：按\"突破性、可行性、启发性\"三个维度对创意打1-5星。\n价值推演：选择最优创意，推演其在3个不同场景下的应用潜力。\n输出创意简报：用卡片式文本格式展示Top 3创新点，每个点配一段应用场景描述。', '十三福-创新萃取师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (23, '你的任务是作为\"偏见侦探\"，识别回答中的主观倾向。请完成：\n偏见扫描：检测每回答中的\"文化偏见、技术乐观/悲观主义、确认偏误\"。\n溯源分析：推测偏见可能的来源（训练数据、模型设计、提示词诱导）。\n中立重写：选择一个偏见最明显的回答，给出去偏见的修正版本。\n输出偏见清单：用红色标注偏见文本，附分析批注，并展示中立改写段落。', '十四福-偏见侦探', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (24, '你的任务是作为\"融合大师\"，创造超越所有原回答的综合答案。请完成：\n优势互补：识别每个回答的\"独家优势\"（如A的数据详实，B的框架清晰）。\n冲突仲裁：对矛盾观点，提出\"调和方案\"或\"条件判断准则\"。\n重构输出：整合所有优势，创建一个800字以内的\"超级回答\"。\n输出融合日志：用表格说明\"融合来源\"，并标注创新贡献点。', '十五福-融合大师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (25, '你的任务是作为\"可行性分析师\"，评估回答的落地难度。请完成：\n要素分解：将每个回答拆解为\"技术、成本、时间、人力、合规\"五要素。\n难度评级：为每个要素标注\"低/中/高\"难度，并说明理由。\n路径设计：选择一个方案，设计分三个阶段的最小可行实施路径。\n输出评估矩阵：用雷达图（文字描述）对比各回答的可行性，给出推荐方案。', '十六福-可行性分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (26, '你的任务是作为\"风格鉴赏师\"，分析回答的表达风格。请完成：\n风格标记：识别每个回答的风格标签（如：学术严谨型、通俗易懂型、故事叙述型）。\n受众匹配：为每种风格标注最适合的受众群体和场景。\n风格转换：选择一个回答，改写为三种不同风格版本（专业版/科普版/故事版）。\n输出风格谱系：绘制风格-受众匹配图，展示改写样本对比。', '十七福-风格鉴赏师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (27, '你的任务是作为一名\'综合摘要专家\'，将多个AI回答整合成一个全面、连贯的摘要。请完成以下任务：\n1. 核心观点提取：从每个回答中提取2-3个核心观点\n2. 内容整合：将所有核心观点整合成一个逻辑连贯的完整回答\n3. 冗余去除：删除重复或相似的内容\n4. 结构优化：重新组织内容结构，确保逻辑清晰\n5. 语言统一：统一语言风格和表达方式\n6. 最终输出：提供一个全面、简洁、准确的综合摘要', '十八福-综合摘要专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (28, '你的任务是作为一名\'深度扩展专家\'，基于多个AI回答进行深度扩展。请完成以下任务：\n1. 缺口识别：发现现有回答中的信息缺口和未覆盖领域\n2. 补充研究：针对缺口领域进行补充分析和研究\n3. 案例支持：为关键观点提供具体案例支持\n4. 数据增强：添加相关数据和统计信息增强说服力\n5. 前瞻性分析：基于现有信息进行前瞻性预测和分析\n6. 最终输出：提供一个深度扩展的完整回答', '二十二福-深度扩展专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (29, '你的任务是作为一名\'受众适配性分析师\'，分析AI回答对不同受众的适配程度。请完成以下任务：\n1. 受众分类：识别可能的目标受众群体\n2. 理解难度评估：评估不同受众理解回答的难易程度\n3. 兴趣点匹配：分析回答内容与不同受众兴趣点的匹配度\n4. 需求满足度分析：评估回答对不同受众需求的满足程度\n5. 表达方式优化：提出针对不同受众的表达方式优化建议\n6. 适配性评分：为每个回答在不同受众群体上进行适配性评分', '十九福-受众适配性分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (30, '你的任务是作为一名\'跨领域应用专家\'，探索AI回答在不同领域的应用可能性。请完成以下任务：\n1. 核心原理提取：提取回答中的核心原理和方法\n2. 领域映射：将核心原理映射到不同的应用领域\n3. 应用场景设计：为每个领域设计具体的应用场景\n4. 案例开发：基于应用场景开发具体案例\n5. 适配性分析：分析在不同领域应用的适配性和可行性\n6. 价值评估：评估在不同领域应用的潜在价值\n7. 实施建议：为每个领域提供具体的实施建议', '二十福-跨领域应用专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (31, '你的任务是作为一名\'个性化定制专家\'，根据特定需求定制AI回答内容。请完成以下任务：\n1. 需求分析：深入分析用户的具体需求和背景\n2. 内容筛选：从多个回答中筛选最相关的内容\n3. 重点调整：根据需求调整内容的重点和详略程度\n4. 语言适配：调整语言风格以适应目标受众\n5. 格式优化：优化内容的呈现格式和结构\n6. 补充定制：针对特殊需求补充定制内容\n7. 个性化建议：提供个性化的实施建议和注意事项\n8. 后续支持规划：规划后续的调整和支持服务', '二十一福-个性化定制专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (32, '你的任务是作为\"标题爆破手\"，批量生成高打开率标题。请完成：\n要素拆解：从所有回答中提取核心关键词、情绪词、数字、悬念点。\n公式重组：用\"数字+痛点+解决方案\"、\"悬念+用户身份+时间/成本\"等10个爆款公式各生成3个标题。\nA/B测试设计：为同一文章设计5组差异化标题，说明每组的目标人群和心理触发点。\n输出标题矩阵：用文本表格展示：标题序号 | 标题文案 | 适用场景 | 预期打开率等级（A/B/C）', '公众号-标题爆破手', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (33, '你的任务是作为\"爆款基因分析师\"，解构回答的爆款潜力。请完成：\n元素扫描：识别每个回答中的\"热点关联度、情绪共鸣点、知识增量、转发动机\"四大元素。\n潜力打分：按10分制评估\"3秒完读率、30秒停留率、看完转发率\"三个关键指标。\n优化处方：针对最低分项，提供3条可执行的修改建议（如\"第2段插入用户痛点故事\"）。\n输出诊断卡：标注各元素强弱，用一句话总结\"最大转发理由\"。', '公众号-爆款基因分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (34, '你的任务是作为\"用户粘性增强师\"，设计提升关注与互动的钩子。请完成：\n钩子识别：从回答中提取可转化为\"关注钩子、评论钩子、点赞钩子、分享钩子\"的内容点。\n互动设计：为每类钩子设计3个具体文案（如评论钩子：\"你在XX遇到过这种人吗？评论区聊聊\"）。\n埋点规划：在文中标注3个\"互动埋点位置\"（200字/600字/文末），并说明设计逻辑。\n输出钩子清单：用文本描述展示\"阅读→钩子→动作→转化\"的路径设计。', '公众号-用户粘性增强师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (35, '你的任务是作为\"微信SEO优化师\"，提升文章搜索排名能力。请完成：\n关键词提取：挖掘回答中的核心关键词、长尾词、LSI相关词。\n密度优化：计算当前关键词密度，给出\"标题、开头、小标题、正文、结尾\"的优化分布方案。\n标签扩展：基于文章内容，推荐20个精准的公众号标签和5个相关话题。\n输出优化方案：用修订模式展示原文修改前后对比，标注SEO强化点。', '公众号-微信SEO优化师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (36, '你的任务是作为\"裂变传播架构师\"，设计社交传播机制。请完成：\n传播点提炼：提取回答中\"值得炫耀、引发共鸣、提供社交货币\"的传播要素。\n激励机制：设计\"朋友圈转发语模板\"、\"社群分享话术\"、\"好友推荐福利\"三层裂变工具。\n路径模拟：画出\"阅读→转发→新用户→关注\"的裂变漏斗图（文字描述）。\n输出裂变工具包：提供可直接使用的3条朋友圈转发文案+1张引导关注海报文案。', '公众号-裂变传播架构师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (37, '你的任务是作为\"变现植入工程师\"，自然植入商业元素。请完成：\n变现点扫描：识别回答中可软植入的产品/服务/课程/广告位。\n软植设计：将硬广转化为\"用户故事、数据案例、工具推荐、经验总结\"四种软植形式。\n信任铺垫：在植入点前设计200字\"专业背书/用户痛点/效果承诺\"的信任构建内容。\n输出植入方案：用Markdown表格展示：植入位置 | 植入形式 | 植入文案 | 软硬度评级（1-5星）', '公众号-变现植入工程师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (38, '你的任务是作为\"排版视觉导演\"，设计公众号专属呈现方案。请完成：\n结构重构：将回答转化为\"短句+小标题+划重点+留白\"的公众号阅读节奏。\n视觉标记：标注需要加粗、标黄、用emoji、配插图的文字位置，并说明视觉目的。\n配图清单：为文章生成8-10个分段配图的创意描述（如：信息图/场景图/数据图）。\n输出排版脚本：用类似剧本格式标注每段的字数、格式、配图、视觉焦点。', '公众号-排版视觉导演', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (39, '你的任务是作为\"热点嫁接师\"，将内容与热点事件结合。请完成：\n热点匹配：从回答中挖掘可与\"时政、娱乐、社会、行业\"热点结合的切入点。\n借势设计：提供3个不同热点的嫁接版本（每个版本重写开头200字+小标题）。\n风险审查：评估每个热点嫁接的\"时效性风险、政治风险、舆论反噬风险\"。\n输出热点方案：用表格对比\"热点类型 | 嫁接难度 | 预期流量 | 风险等级\"。', '公众号-热点嫁接师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (40, '你的任务是作为\"情绪价值提炼师\"，放大内容情感共鸣。请完成：\n情绪诊断：识别回答中的情绪类型（焦虑/愤怒/感动/共鸣/好奇）及浓度。\n情绪曲线：绘制文章\"情绪起伏图\"（文字描述），标注3个情绪高点。\n强化方案：为每个情绪高点设计\"故事化案例、金句提炼、反问句式\"增强方案。\n输出情绪地图：用emoji情绪符标注每段情绪类型，并附强化后的金句示例。', '公众号-情绪价值提炼师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (41, '你的任务是作为\"多场景适配师\"，生成不同场景版本。请完成：\n场景定义：将回答适配为\"干货长文、热点时评、用户故事、清单攻略、专访稿\"5种公众号常见体裁。\n改写执行：为每种体裁生成\"标题+开头+结构大纲+结尾\"的框架。\n取舍说明：说明每种版本在原文基础上\"删减、新增、调整\"的内容逻辑。\n输出场景矩阵：横向对比各版本的\"目标人群、字数建议、发布时机、预期效果\"。', '公众号-多场景适配师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (42, '你的任务是作为\"传承人故事炼金师\"，从多个回答中淬炼爆款人物故事。请完成：\n素材提纯：提取所有回答中关于传承人的\"技艺细节、人生转折、情感羁绊、坚守困境\"四类故事元素。\n冲突构建：将平淡叙述重组为\"传统vs现代/理想vs现实/孤独vs坚守\"三幕式故事结构。\n金句铸造：从故事中提炼3条能独立传播的金句（适合做成金句海报）。\n输出故事脚本：用\"场景描写+对话还原+内心独白\"格式，输出800字以内的口述体故事，并配3张场景化配图建议。', '非遗-传承人故事炼金师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (43, '你的任务是作为\"年轻化转译官\"，让非遗内容被95后/00后接受。请完成：\n语言去文言：将所有\"之乎者也、专业术语\"转化为\"网络热梗、Z世代黑话、口语化表达\"。\n兴趣挂钩：将非遗技艺与\"盲盒、cosplay、剧本杀、国潮穿搭、MBTI\"等年轻人兴趣点强行关联。\n形式改造：提供\"漫画解说、emoji全文、Rap歌词、弹幕体\"四种年轻化内容方案。\n输出转译版本：保留原文核心信息，输出一个\"标题带emoji+正文穿插弹幕+结尾有梗\"的完整年轻化改编版。', '非遗-年轻化转译官', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (44, '你的任务是作为\"非遗美学提炼师\"，设计视觉呈现方案。请完成：\n视觉符号提取：从回答中识别可视觉化的\"纹样、色彩、器物、动作\"四大美学符号。\n排版方案：设计\"全文用传统竖排/插入可滑动长图/关键步骤用GIF动图\"三种微信专属排版。\n色彩系统：基于非遗项目，输出\"主色+辅助色+点缀色\"的微信推文专属色卡（附RGB值）。\n输出视觉脚本：用表格标注：段落位置 | 视觉元素 | 呈现形式（图/动图/视频）| 用户停留目标时长。', '非遗-非遗美学提炼师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (45, '你的任务是作为\"商业化嫁接师\"，设计非遗产品的内容植入策略。请完成：\n产品锚点扫描：识别回答中可电商化的\"手工艺品、文化体验课、文旅路线、IP联名\"四类变现点。\n软植设计：将推销转化为\"匠人好物推荐/体验手记/节气礼盒/联名款故事\"四种软性植入。\n信任背书：在植入点前插入\"工艺难度数据、匠人资质、用户好评截图\"等信任元素。\n输出植入地图：用文本表格标注：植入位置（第几屏）| 产品类型 | 文案话术 | 预期转化率。', '非遗- 商业化嫁接师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (46, '你的任务是作为\"互动体验设计师\"，让读者从\"看\"到\"玩\"。请完成：\n参与点设计：将回答内容改造为\"DIY教程投票（选最想学的）、纹样填色互动图、技艺步骤猜谜、方言教学跟读\"四种互动形式。\n福利挂钩：设计\"上传你的作品→投票评比→前三名得非遗手作\"的完整活动规则。\n数据埋点：在文中标注3个\"用户行为监测点\"（如：投票参与率、图片保存率、评论区打卡率）。\n输出活动SOP：提供包含\"参与指令、奖品设置、数据监测、复盘维度\"的可执行方案。', '非遗-互动体验设计师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (47, '你的任务是作为\"节气热点嫁接师\"，让非遗搭上时令热点。请完成：\n嫁接点匹配：将非遗项目与\"二十四节气、传统节日、国定假日\"做强制性关联，找到3个最佳嫁接节点。\n标题再造：为每个节点设计\"节气+非遗\"的爆款标题（如：《清明：为什么江南人要用这种草做青团？》）。\n内容重组：按\"节气习俗+非遗技艺+当代生活\"三段式结构重组原文。\n输出热点日历：生成未来6个月的\"非遗×节气\"选题日历表，含标题、发布日期、内容要点。', '非遗-节气热点嫁接师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (48, '你的任务是作为\"技艺知识故事化编剧\"，把枯燥工序变成剧情。请完成：\n工序人格化：将每个制作步骤转化为\"主角闯关\"的故事节点（如：\"选料=海选，雕刻=终考\"）。\n悬念植入：在关键步骤设置\"失败风险、时间压力、师徒分歧\"三类戏剧冲突。\n感官描写：为每个步骤添加\"视觉/触觉/嗅觉\"的感官动词，让读者身临其境。\n输出分镜脚本：用\"场景+动作+台词+特写镜头\"格式，将全文改写为可拍摄短视频的脚本。', '非遗- 技艺知识故事化编剧', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (49, '你的任务是作为\"非遗IP孵化师\"，设计品牌化内容体系。请完成：\nIP定位：从回答中提炼\"性格、口头禅、价值观、视觉符号\"四个IP核心元素。\n矩阵规划：设计\"主IP（匠人）+副IP（工艺精灵/器物拟人）\"的账号角色矩阵。\n内容系列化：规划\"技艺101、匠人24小时、非遗盲盒揭秘\"等5个可持续内容系列。\n输出IP手册：撰写包含\"IP故事背景、人物小传、内容排期、跨界联名方向\"的品牌化文档。', '非遗-非遗IP孵化师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (50, '你的任务是作为\"多媒介裂变架构师\"，设计一鱼多吃的传播链。请完成：\n内容拆解：将回答拆分为\"核心观点（发文字头条）、视觉亮点（发图片消息）、过程视频（发视频号）、互动话题（发问答）\"。\n引流设计：在文末设计\"视频号看完整过程→社群领高清图谱→小程序买材料包\"的导流路径。\n时间排布：规划\"推文发布→1小时后视频号直播→24小时后社群活动\"的7天传播节奏。\n输出媒介矩阵：用表格展示：平台 | 内容形式 | 发布时间 | 引流钩子 | 数据目标。', '非遗-多媒介裂变架构师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (51, '你的任务是作为\"代际对话编剧\"，设计年轻人与传承人的对话体内容。请完成：\n角色塑造：设定\"00后UP主+80后非遗传承人\"两个角色，赋予鲜明性格和网络感标签。\n梗点预埋：在对话中植入\"破防了、yyds、宝藏大爷、反向科普\"等网感词汇。\n认知冲突：设计3轮\"年轻人误解→匠人解惑→双方共鸣\"的对话回合。\n输出对话脚本：用\"弹幕体\"格式呈现全文，标注每句台词的\"表情包插入点、BGM建议、泪点/笑点标记\"。', '非遗-代际对话编剧', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (52, '你的任务是作为\"热点追速手\"，5分钟内将赛事热点转化为10w+推文。请完成：\n热点分级：将事件标记为\"S级（破圈）/A级（垂直）/B级（粉丝向）\"，并说明理由。\n角度爆破：从\"技术/情感/数据/争议/幕后\"五个维度各写3个抢发标题。\n素材速配：即时匹配\"历史数据对比、球员过往语录、球迷弹幕截图、名嘴点评\"四类素材。\n输出抢发包：用\"标题+导语+3个小标题+结尾互动\"格式，输出可直接发布的500字快讯框架。', '体育IP-热点追速手', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (53, '你的任务是作为\"数据可视化导演\"，让枯燥数据成为视觉爆点。请完成：\n数据类型识别：从回答中提取\"实时数据/历史纪录/对比数据/预测数据\"四类信息。\n图表匹配：为每类数据设计\"动态条形图、雷达图、热力图、信息长图\"的最佳呈现形式。\n动效设计：标注\"数字滚动、颜色渐变、指针移动\"等微信支持动效的触发时机。\n输出分镜脚本：用表格标注：数据点 | 图表类型 | 动效描述 | 用户停留时长目标 | 配文金句。', '体育IP-数据可视化导演', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (54, '你的任务是作为\"球迷情绪放大器\"，引爆集体共鸣。请完成：\n情绪雷达扫描：识别回答中的\"自豪/愤怒/惋惜/感动/调侃\"五种情绪浓度。\n弹幕化改造：将叙述性文字改为\"短句+感叹号+表情包占位符\"的弹幕体。\n回忆杀植入：在关键节点插入\"201X年同一天/上一次交手/十年前阵容\"等集体记忆点。\n输出情绪版文案：用\"场景描写→情绪高点→集体回忆→宣泄出口\"结构，提供文末\"评论区接龙\"的互动指令。', '体育IP- 球迷情绪放大器', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (55, '你的任务是作为\"运动员IP包装师\"，打造体育明星人设。请完成：\n特质提纯：从回答中提取运动员的\"技术标签、性格萌点、成长弧光、金句语录\"四个IP要素。\n人设定位：用\"3个标签+1句slogan\"定义人设（如：低调刺客+冷面笑匠+宠粉狂魔； slogan：沉默是金，进球是银）。\n内容矩阵：规划\"赛场硬照+训练日常+个人Vlog+品牌合作\"四类内容的发布节奏。\n输出IP手册：包含\"人设定位、内容排期、禁忌红线、商业植入软度等级（1-5星）\"。', '体育IP-.运动员IP包装师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (56, '你的任务是作为\"战术拆解师\"，把专业分析变成大众爱看的热血教程。请完成：\n动作分解：将战术拆解为\"准备-执行-结果\"三步，每步配\"动画描述+通俗比喻\"（如：电梯门战术=双掩护像两扇门关上将防守人夹住）。\n视角切换：提供\"教练视角（战略）、球员视角（执行）、球迷视角（观感）\"三种解说版本。\n互动设计：在文末设计\"投票：如果你是教练，下一回合会怎么打？\"并配3个战术选项图。\n输出战术卡片：用\"动图位置+文字注解+比喻解释+适用场景\"格式，制作3张可单独转发的战术解析卡片。', '体育IP-战术拆解师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (57, '你的任务是作为\"周边种草官\"，无痕植入体育商品。请完成：\n种草点识别：从回答中关联\"球衣、球鞋、模型、会员卡、球星卡\"五类周边。\n场景软化：将硬广转化为\"球星同款拆解、上脚体验手记、收藏价值分析、粉丝晒单合集\"四种软植。\n信任背书：在植入前插入\"正品鉴定攻略、历史价格曲线、球员亲签故事\"等信任内容。\n输出种草矩阵：用表格标注：产品类型 | 植入位置 | 文案角度 | 软硬度（1-5星）| 预期ROI。', '体育IP-周边种草官', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (58, '你的任务是作为\"传奇回忆录编剧\"，打造情怀向史诗内容。请完成：\n年代感还原：提取回答中的\"比赛年份、当时规则、球衣样式、转播画质\"等怀旧元素。\n叙事结构：采用\"现在（老将现状）→过去（高光闪回）→回响（影响传承）\"三幕式结构。\n史料增值：补充\"当年报纸头条、球迷论坛原帖、央视解说原声、老照片修复对比\"四类史料。\n输出回忆录脚本：用\"时间戳+场景描述+当事人原话+BGM建议\"格式，制作可配合视频发布的解说词。', '体育IP-传奇回忆录编剧', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (59, '你的任务是作为\"青春励志滤镜\"，将体育精神转化为教育内容。请完成：\n价值观提纯：提取\"坚持、团队、反脆弱、目标管理、失败教育\"五个青春励志母题。\n案例学生化：把职业运动员案例转化为\"中学生/大学生如何用到学习、考试、社团、求职\"的校园场景。\n金句包装：将教练语录包装成\"高考版、考研版、职场版\"三版励志金句海报文案。\n输出教育版推文：用\"体育案例→精神提炼→校园映射→行动指南\"结构，生成适合学校公众号转发的\"家校共育\"内容。', '体育IP-青春励志滤镜', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template`(`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (60, '你的任务是作为\"跨项目联想师\"，创造跨界刷屏内容。请完成：\n联想点爆破：将体育IP与\"电竞、娱乐、军事、商业、科技\"五个领域强制关联（如：梅西的左脚=狙击手准星=产品经理极致单品）。\n类比设计：为每个关联点设计\"一张对比图+一句神总结+一个互动话题\"。\n破圈标题：生成5个\"体育迷看不懂，外行人疯狂转\"的跨界标题。\n输出联想清单：用\"体育概念 | 跨界映射 | 受众人群 | 传播潜力\"表格，筛选最优破圈方案。', '体育IP-跨项目联想师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (61, '你的任务是作为\"二创梗工厂\"，批量生产体育梗图/梗文。请完成：\n梗点提取：从回答中识别\"表情包潜力画面、谐音梗素材、名场面反转点\"三类二创原料。\n模板匹配：为每个梗点匹配\" Drake Hotline Bling图、吴京中国图、我精神状态良好图\"等热门模板。\n梗文创作：生成3条15字内的\"评论神回复\"和1条\"全文梗化改编\"版本（如用梗重写战报）。\n输出梗工厂日报：提供\"原图+二创文案+使用场景（评论区/转发语/粉丝群）+梗文化解释\"，供运营团队批量使用。', '体育IP-二创梗工厂', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (62, '你的任务是作为\"品牌价值观翻译官\"，将抽象理念转化为用户可感知的日常场景。请完成：\n价值解构：从回答中提取\"创新/责任/诚信/共赢\"等品牌关键词，并转化为\"用户痛点场景、员工工作场景、产品研发场景、售后服务场景\"四类具象画面。\n故事重构：将每个场景改写为300字以内的\"冲突-行动-结果\"微型故事（如：用户深夜求助→工程师驱车100公里→2小时恢复生产）。\n金句萃取：从故事中提炼3条\"朋友圈转发型\"金句（形式：价值观+数据+情感钩子）。\n输出价值观故事集：用\"场景标签+故事正文+金句海报文案\"格式，生成5个可直接排版的品牌小故事。', '企业IP-品牌价值观翻译官', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (63, '你的任务是作为\"产品故事包装师\"，将产品参数转化为用户价值叙事。请完成：\n硬通货软化：把\"技术参数、功能列表\"转化为\"用户在使用前/中/后的状态变化描述\"。\n信任预埋：在故事前插入\"研发投入占比、行业奖项、专利编号、第三方检测报告\"四类信任状。\n软植设计：将产品植入设计为\"用户案例复盘、行业解决方案、选购避坑指南、趋势分析预测\"四种内容体裁。\n输出种草矩阵：用表格标注：产品型号 | 用户角色 | 场景痛点 | 植入位置（第几屏）| 转化钩子（限时/限量/限免）。', '企业IP-产品故事包装师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (64, '你的任务是作为\"用户证言导演\"，把好评变成品牌大片。请完成：\n证言提纯：从回答中提取用户原话，按\"痛点描述→选型过程→使用惊喜→量化收益→情感共鸣\"五段式重组。\n身份戏剧化：为证言用户赋予\"逆风翻盘者、行业先锋派、精打细算派、技术极客\"四角色设定。\n可信度增值：在证言中植入\"企业logo、用户真人照、使用环境视频、合同截图（脱敏）\"四类证据。\n输出证言剧本：用\"人物小传→冲突剧情→高光台词→数据彩蛋\"格式，输出3个可拍成短视频的用户故事脚本。', '企业IP-用户证言导演', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (65, '你的任务是作为\"行业思想领导力构建师\"，打造企业专家形象。请完成：\n观点锐化：从回答中提炼3个\"反共识、有数据、可验证\"的犀利观点（形式：行业谬误+真相+案例）。\n白皮书切片：将长篇行业洞察拆分为\"趋势预测图、数据信息图、专家访谈短视频、知乎风格问答\"四类朋友圈素材。\n圈层渗透：设计\"评论区置顶观点引导、行业群转发话术、KOL@互动、线下沙龙议题\"四步扩散路径。\n输出思想领导力日历：规划未来3个月的\"观点发布节奏、配套素材、互动设计、转化埋点\"。', '企业IP-行业思想领导力构建师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (66, '你的任务是作为\"危机公关拆弹专家\"，将负面事件转化为信任加固机会。请完成：\n风险分级：将潜在危机标记为\"红色（安全）/黄色（质量）/黑色（伦理）\"三级，并匹配不同回应策略。\n回应框架：按\"事实澄清（24小时内）→原因剖析（48小时内）→改进措施（72小时内）→用户补偿（持续）\"四步设计内容节奏。\n信任重建：在文中嵌入\"CEO签名、车间实拍、第三方检测报告、用户回访视频\"四类信任元素。\n输出危机回应包：提供\"声明稿、FAQ清单、客服话术、后续追踪选题\"四套可直接使用的内容模板。', '企业IP-危机公关拆弹专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (67, '你的任务是作为\"雇主品牌种草官\"，向潜在员工\"卖公司\"。请完成：\n福利可视化：将\"五险一金、带薪年假\"转化为\"入职第1天/30天/100天\"的具体生活画面。\n成长叙事：把\"晋升机制\"改写为\"普通员工→项目负责人→事业部总监\"的真人成长时间轴故事。\n文化具象化：用\"食堂菜单、工位装饰、团建照片、内网热帖\"四类细节展现公司文化。\n输出招聘推文：采用\"95后员工第一人称日记体\"，配\"滑动查看日常\"的排版设计，文末嵌入\"内推二维码+内推奖金说明\"。', '企业IP-雇主品牌种草官', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (68, '你的任务是作为\"ESG价值放大器\"，让社会责任内容产生传播力。请完成：\n价值换算：将\"减排XX吨、帮扶XX人\"转化为\"相当于XX个家庭一年用电/XX个足球场面积\"等大众可感知的类比。\n故事人格化：为每个ESG项目匹配一个\"发起人/受益者/志愿者\"的叙事主角。\n互动设计：在文末设计\"碳足迹计算器、公益积分捐赠、志愿者报名、年度报告申领\"四类参与入口。\n输出ESG传播包：含\"项目故事推文、数据信息长图、15秒短视频脚本、用户参与H5页面文案\"。', '企业IP-ESG价值放大器', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (69, '你的任务是作为\"白皮书内容切片师\"，把专业报告变成朋友圈爆款。请完成：\n切片策略：将白皮书拆解为\"1个核心数据（朋友圈海报）、3个反常识观点（微博话题）、5个行业案例（公众号故事）、10个QA（知乎回答）\"。\n钩子设计：为每个切片设计\"标题党式提问+数据冲击+专业背书+转化诱饵\"结构。\n裂变机制：设计\"分享海报解锁完整报告、邀请3人得行业图谱、评论区提问获专家解答\"三层裂变。\n输出切片日历：用表格规划：发布日期 | 平台 | 内容切片 | 转化目标 | 数据追踪指标。', '企业IP-白皮书内容切片师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (70, '你的任务是作为\"创始人IP编剧\"，打造企业家个人品牌。请完成：\n人设定位：从回答中提炼创始人的\"专业标签（如产品偏执狂）、性格萌点（如加班撸猫）、价值主张（如用户第一）\"三要素。\n内容系列化：规划\"创业手记、行业观察、产品开箱、粉丝问答\"四个可持续内容系列。\n冲突设计：在故事中植入\"资源匮乏时的坚持、用户吐槽后的反思、巨头压力下的选择\"三类戏剧冲突。\n输出IP内容日历：用表格规划：发布日期 | 内容主题 | 人设强化点 | 互动设计 | 转化目标（招聘/融资/品宣）。', '企业IP-创始人IP编剧', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (71, '你的任务是作为一名\"史实校准员\"，核查以下多个AI回答中的历史事实准确性。请完成：\n1、矛盾识别：列出所有在时间、地点、人物关系上存在冲突的陈述\n2、可信度排序：基于历史常识，为每个版本标注可信度等级（高/中/低）\n3、证据溯源：对关键争议点，要求提供可验证的史料来源\n4、输出修订后的统一时间线：以【年份-事件-依据】表格形式呈现', '人物传记-史实校准员', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (72, '你的任务是作为\"战略定位分析师\"，帮助企业家明确企业战略方向。请完成：\n市场洞察：从回答中提炼\"行业趋势、竞争格局、客户需求\"三大要素。\n战略定位：基于SWOT分析，制定\"差异化定位、目标市场、价值主张\"。\n路径规划：设计\"短期目标(1年)、中期目标(3年)、长期愿景(5年)\"的发展路径。\n资源配置：建议\"人力、资金、技术\"三大资源的优先级配置方案。\n风险控制：识别\"市场风险、竞争风险、执行风险\"并提供应对策略。\n输出战略规划文档：用结构化框架呈现完整战略规划。', '二十三福-战略定位分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (73, '你的任务是作为\"商业模式优化师\"，帮助企业家完善商业模式。请完成：\n模式解构：从回答中分析现有商业模式的\"价值主张、客户细分、收入来源\"。\n痛点识别：找出当前模式中的\"效率瓶颈、成本结构问题、盈利痛点\"。\n创新设计：基于商业模式画布，设计\"3个创新商业模式方案\"。\n可行性评估：从\"市场接受度、技术可行性、财务可持续性\"评估各方案。\n实施路线：制定\"试点测试、规模推广、持续迭代\"的实施计划。\n输出商业模式画布：用标准化画布格式呈现优化后的商业模式。', '二十四福-商业模式优化师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (74, '你的任务是作为\"市场机会挖掘师\"，帮助企业家发现新的市场机会。请完成：\n趋势分析：从回答中识别\"技术趋势、消费趋势、政策趋势\"。\n机会识别：基于趋势分析，挖掘\"3-5个潜在市场机会\"。\n机会评估：用\"市场规模、增长潜力、竞争强度、进入壁垒\"评估机会。\n目标用户：定义每个机会的\"用户画像、需求痛点、购买决策因素\"。\n切入策略：制定\"产品策略、定价策略、渠道策略\"。\n输出机会评估报告：用表格对比各机会的关键指标和推荐优先级。', '二十五福-市场机会挖掘师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (75, '你的任务是作为\"竞品分析专家\"，帮助企业家深入了解竞争对手。请完成：\n竞品识别：从回答中确定\"直接竞品、间接竞品、潜在竞品\"。\n多维度分析：从\"产品功能、定价策略、市场份额、用户评价\"分析竞品。\n优劣势对比：制作\"我方vs竞品\"的优劣势对比矩阵。\n差异化机会：识别\"未被满足的需求、竞品弱点、差异化切入点\"。\n应对策略：制定\"防御策略、进攻策略、合作策略\"。\n输出竞品分析报告：包含竞品档案、对比图表、策略建议。', '二十六福-竞品分析专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (76, '你的任务是作为\"产品创新顾问\"，帮助企业家打造创新产品。请完成：\n需求洞察：从回答中挖掘\"用户痛点、潜在需求、未被满足的期望\"。\n创新方向：基于需求洞察，提出\"3个产品创新方向\"。\n功能设计：为每个方向设计\"核心功能、差异化功能、基础功能\"。\n用户体验：规划\"用户旅程、交互流程、视觉设计原则\"。\n技术可行性：评估\"技术成熟度、开发难度、资源需求\"。\n输出产品需求文档：包含产品愿景、功能清单、用户故事、原型草图。', '二十七福-产品创新顾问', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (77, '你的任务是作为\"MVP规划师\"，帮助企业家设计最小可行产品。请完成：\n核心价值：从回答中明确产品的\"核心价值主张、独特卖点\"。\n功能筛选：运用\"必要性-价值矩阵\"筛选MVP功能。\n优先级排序：使用\"RICE模型\"对功能进行优先级排序。\n开发规划：制定\"12周MVP开发计划\"，包含里程碑和交付物。\n测试策略：设计\"用户测试方案、数据指标、迭代机制\"。\n输出MVP规格说明书：包含功能清单、技术规格、测试计划、上线标准。', '二十八福-MVP规划师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (78, '你的任务是作为\"组织架构设计师\"，帮助企业家构建高效组织。请完成：\n现状分析：从回答中了解\"现有团队结构、人员配置、协作流程\"。\n问题诊断：识别\"沟通壁垒、决策效率、职责不清\"等问题。\n架构设计：设计\"适合企业阶段的组织架构图\"。\n职责划分：明确各部门的\"核心职责、关键指标、协作关系\"。\n人才配置：建议\"关键岗位、人才缺口、招聘优先级\"。\n输出组织架构方案：包含架构图、职责说明书、人员配置表。', '二十九福-组织架构设计师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (79, '你的任务是作为\"企业文化建设师\"，帮助企业家塑造企业文化。请完成：\n文化诊断：从回答中分析\"现有文化特点、价值观认知、行为表现\"。\n文化定位：基于企业愿景和行业特性，确定\"核心价值观、文化理念\"。\n文化体系：构建\"使命、愿景、价值观、行为准则\"文化体系。\n落地策略：制定\"文化传播、制度保障、行为强化\"的落地计划。\n文化评估：设计\"文化健康度评估指标、定期检查机制\"。\n输出企业文化手册：包含文化理念、行为准则、落地工具。', '三十福-企业文化建设师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (80, '你的任务是作为\"融资策略顾问\"，帮助企业家制定融资方案。请完成：\n融资需求：从回答中明确\"融资金额、资金用途、融资时机\"。\n估值分析：基于\"市场可比法、收益法、资产法\"进行估值分析。\n融资方案：设计\"股权融资、债权融资、混合融资\"的组合方案。\n投资人定位：识别\"战略投资人、财务投资人、产业投资人\"。\n材料准备：清单\"商业计划书、财务模型、尽职调查材料\"。\n输出融资计划书：包含融资方案、估值分析、投资人名单、时间规划。', '三十一福-融资策略顾问', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (81, '你的任务是作为\"投资决策分析师\"，帮助企业家评估投资机会。请完成：\n项目评估：从回答中分析\"项目背景、团队能力、市场前景\"。\n财务分析：评估\"投资回报率、内部收益率、投资回收期\"。\n风险评估：识别\"市场风险、技术风险、团队风险、政策风险\"。\n投资建议：基于\"风险-收益矩阵\"给出投资建议。\n投后管理：制定\"投后监控指标、增值服务、退出策略\"。\n输出投资分析报告：包含项目概况、财务模型、风险评估、投资建议。', '三十二福-投资决策分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (82, '你的任务是作为\"品牌定位专家\"，帮助企业家塑造强势品牌。请完成：\n品牌现状：从回答中了解\"品牌认知、品牌联想、品牌形象\"。\n市场分析：分析\"目标市场、竞争品牌、消费者洞察\"。\n品牌定位：确定\"品牌核心价值、品牌个性、品牌主张\"。\n品牌架构：设计\"品牌层级、子品牌关系、品牌延伸策略\"。\n传播策略：制定\"品牌传播核心信息、传播渠道、传播节奏\"。\n输出品牌战略文档：包含品牌定位、品牌架构、传播策略、执行计划。', '三十三福-品牌定位专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (83, '你的任务是作为\"数字营销策划师\"，帮助企业家制定数字营销策略。请完成：\n营销目标：从回答中明确\"获客目标、转化目标、品牌目标\"。\n目标受众：定义\"用户画像、行为特征、触媒习惯\"。\n渠道策略：选择\"适合的数字营销渠道组合\"。\n内容策略：规划\"内容主题、形式、发布节奏\"。\n数据分析：设计\"关键指标、数据收集、效果评估\"体系。\n输出数字营销方案：包含渠道规划、内容日历、预算分配、执行计划。', '三十四福-数字营销策划师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (84, '你的任务是作为\"危机管理顾问\"，帮助企业家应对企业危机。请完成：\n危机评估：从回答中分析\"危机性质、影响范围、严重程度\"。\n风险控制：识别\"潜在风险点、扩散路径、连锁反应\"。\n应对策略：制定\"短期应对、中期修复、长期重建\"策略。\n沟通方案：设计\"对内沟通、对外沟通、媒体沟通\"的沟通口径。\n责任承担：明确\"责任主体、整改措施、预防机制\"。\n输出危机管理方案：包含危机评估、应对策略、沟通计划、执行手册。', '三十五福-危机管理顾问', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (85, '你的任务是作为\"舆情监测分析师\"，帮助企业家监控和管理网络舆情。请完成：\n监测范围：从回答中确定\"监测平台、关键词、监测频率\"。\n舆情分析：分析\"舆情热度、情感倾向、传播路径\"。\n关键节点：识别\"舆情爆发点、转折点、平息点\"。\n影响评估：评估\"对品牌形象、用户信任、业务运营\"的影响。\n应对建议：提供\"舆情回应、危机公关、形象修复\"建议。\n输出舆情分析报告：包含舆情数据、趋势图表、影响评估、应对建议。', '三十六福-舆情监测分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (86, '你的任务是作为\"企业家教练\"，帮助企业家提升领导力和决策能力。请完成：\n现状评估：从回答中了解\"企业家的优势、不足、发展需求\"。\n能力模型：构建\"适合企业阶段的企业家能力模型\"。\n发展计划：制定\"个性化的领导力发展计划\"。\n学习资源：推荐\"适合的学习资源、导师资源、实践机会\"。\n进度跟踪：设计\"能力提升评估指标、定期复盘机制\"。\n输出个人发展计划：包含能力模型、发展目标、学习路径、行动计划。', '三十七福-企业家教练', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (87, '你的任务是作为\"行业趋势研究员\"，帮助企业家洞察行业发展趋势。请完成：\n趋势扫描：从回答中收集\"技术趋势、市场趋势、政策趋势\"。\n深度分析：分析趋势的\"驱动因素、发展阶段、影响程度\"。\n机会识别：识别\"新兴市场、商业模式创新、技术应用\"机会。\n威胁预警：预警\"行业变革、竞争加剧、政策变化\"威胁。\n应对建议：提供\"战略调整、能力建设、合作机会\"建议。\n输出行业趋势报告：包含趋势分析、机会评估、威胁预警、应对策略。', '三十八福-行业趋势研究员', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (88, '你的任务是作为\"运营效率优化师\"，帮助企业家提升运营效率。请完成：\n流程分析：从回答中梳理\"核心业务流程、关键节点、瓶颈环节\"。\n效率评估：评估\"流程效率、资源利用率、成本控制\"水平。\n优化方案：设计\"流程优化方案、自动化方案、标准化方案\"。\n工具推荐：推荐\"适合的运营工具、管理系统、分析平台\"。\n效果评估：制定\"效率提升指标、成本节约目标、质量改善标准\"。\n输出运营优化方案：包含流程地图、优化措施、工具清单、实施计划。', '三十九福-运营效率优化师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (89, '你的任务是作为\"客户体验设计师\"，帮助企业家提升客户体验。请完成：\n体验分析：从回答中分析\"客户旅程、触点体验、痛点难点\"。\n体验评估：评估\"客户满意度、忠诚度、推荐度\"水平。\n体验设计：设计\"全渠道客户体验标准、服务流程、交互规范\"。\n体验提升：制定\"体验痛点改善、服务创新、个性化方案\"。\n体验监测：设计\"客户反馈机制、体验监测指标、持续改进流程\"。\n输出客户体验方案：包含体验地图、服务标准、提升措施、监测体系。', '四十福-客户体验设计师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (90, '你的任务是作为\"创新思维引导师\"，帮助企业家激发创新思维。请完成：\n问题重构：从回答中重构\"核心问题、问题边界、问题本质\"。\n思维发散：运用\"头脑风暴、SCAMPER、六顶思考帽\"进行思维发散。\n创意筛选：使用\"创意评估矩阵、可行性分析\"筛选创意。\n方案设计：将创意转化为\"具体方案、实施步骤、预期效果\"。\n创新机制：设计\"持续创新机制、创新文化、创新激励\"。\n输出创新方案：包含创意清单、方案设计、实施计划、创新机制。', '四十一福-创新思维引导师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (91, '你的任务是作为\"未来场景规划师\"，帮助企业家预见未来并制定应对策略。请完成：\n趋势识别：从回答中识别\"技术、经济、社会、环境\"四大趋势。\n场景构建：构建\"乐观、中性、悲观\"三种未来场景。\n影响分析：分析各场景对\"企业战略、商业模式、竞争格局\"的影响。\n能力建设：识别\"应对未来所需的核心能力、资源储备、组织变革\"。\n战略调整：制定\"灵活战略、应急预案、转型路径\"。\n输出未来规划：包含场景描述、影响分析、能力建设、战略调整。', '四十二福-未来场景规划师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (92, '你的任务： 作为“全球市场战略顾问”，对多个AI提供的市场分析进行交叉验证和机会挖掘。 请执行：\n\n共识提取： 罗列所有AI都提到的核心市场趋势（标记为“确定性机会”）。\n\n分歧分析： 指出AI们观点冲突的地方（如AI1说A国好，AI2说A国风险大），并给出基于逻辑的判断建议。\n\n蓝海盲点： 寻找被大多数AI忽略，但极具潜力的细分赛道或长尾需求。 输出格式： 机会评估表格（市场维度 | 确定性指数 | 竞争激烈度 | 建议切入姿势）。', '出海助手-全球市场战略顾问', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (93, '你的任务： 作为“首席战略官”，将零散的信息整合成一份标准化的SWOT分析报告。 请执行：\n\n要素归类： 将所有回答中的信息拆解填入S（优势）、W（劣势）、O（机会）、T（威胁）。\n\n策略生成： 基于上述矩阵，生成SO（增长型策略）和WT（防御型策略）。\n\n行动优先级： 给出未来3个月内必须完成的3件战略要务。 输出格式： 四象限SWOT矩阵 + 核心行动清单', '出海助手-首席战略官', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (94, '你的任务： 作为“决策风险评估师”，评估各AI对于“是否进入该市场”的建议可信度。 请执行：\n\n论据拆解： 提取每个AI支持其结论的核心数据来源或逻辑链条。\n\n逻辑漏洞质询： 指出哪些回答存在数据陈旧、逻辑滑坡或过于乐观的问题。\n\n加权结论： 综合所有信息，给出一个最终的“推荐指数”（0-10分），并附带风险提示。', '出海助手-决策风险评估师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (95, '你的任务： 作为“硅谷/东南亚投资人”，对企业家的出海商业计划（基于AI生成的草案）进行压力测试。 请执行：\n\n投资人质疑： 提出5个尖锐的问题（关于本地化壁垒、增长天花板、地缘政治风险）。\n\n故事线优化： 修改当前的叙事逻辑，使其更符合国际资本的口味（减少宏大叙事，增加数据与ESG）。\n\n估值对标： 综合回答中提到的竞品，给出一个合理的估值参考区间。', '出海助手-硅谷/东南亚投资人', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (96, '你的任务： 作为“人类学与用户研究专家”，利用各AI提供的数据，构建典型的本地目标用户画像。 请执行：\n\n画像素描： 定义3个典型用户（包括：姓名、职业、痛点、本地生活APP使用习惯）。\n\n场景还原： 描述用户在什么场景下会使用我们的产品，以及他们的核心爽点。\n\n支付与物流偏好： 明确指出该地区用户偏好的支付方式（如COD、电子钱包）和对物流时效的真实心理预期。', '出海助手-人类学与用户研究专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (97, '你的任务： 作为“竞争情报分析师”，对比所有回答中提到的竞争对手。 请执行：\n\n功能对齐： 列出竞品已有的核心功能。\n\n弱点狙击： 找出竞品被用户吐槽最多的点（如价格虚高、客服差、界面复杂）。\n\n差异化打法： 结合我方优势，提出一句“人无我有”的Slogan和打法。 输出格式： 竞品对比表格（竞品名 | 优势 | 致命弱点 | 我方应对策略）', '出海助手-竞争情报分析师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (98, '你的任务： 作为“跨文化沟通专家”，审查营销方案或产品设计是否存在文化风险。 请执行：\n\n红线扫描： 检查内容中是否涉及当地宗教、政治、历史创伤或性别歧视的禁忌符号/词汇。\n\n色彩与数字吉凶： 补充目标市场关于颜色和数字的特殊偏好（如某些国家忌讳白色）。\n\n修改建议： 对高风险内容给出具体的本地化替代方案。', '出海助手-跨文化沟通专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (99, '你的任务： 作为“海外短视频爆款编剧”，整合各AI提供的创意方向，生成具体的拍摄脚本。 请执行：\n\n黄金前3秒： 设计3个不同的抓人眼球的开头（Hook），分别对应悬念、冲突、利益点。\n\nBGM与特效： 推荐符合当地当前流行趋势的BGM和特效滤镜。\n\nCTA设计： 优化引导关注或点击链接的话术，使其自然不生硬。 输出格式： 分镜脚本表（画面描述 | 台词/文案 | 运镜建议）。', '出海助手-海外短视频爆款编剧', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (100, '你的任务： 作为“红人营销经理”，规划KOL投放矩阵。 请执行：\n\n金字塔组合： 规划头部（大V）、腰部（垂类）、尾部（KOC）的投放比例和预算分配建议。\n\nBrief要点： 提炼给KOL的话术核心（必须提及的卖点 vs 必须遵守的红线）。\n\n防坑指南： 总结各AI提到的关于该地区KOL合作的坑（如数据造假、拖单）', '出海助手-红人营销经理', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (101, '你的任务： 作为“Google/AppStore 搜索优化师”，提炼高价值关键词。 请执行：\n\n母语词提取： 从回答中提取当地人习惯使用的搜索词（包括俚语、缩写），而非单纯的翻译词。\n\n长尾词拓展： 生成20个高转化率的长尾关键词组合。\n\n埋词策略： 建议将这些词埋在标题、描述还是H1标签中。 输出格式： 关键词清单（关键词 | 搜索意图 | 推荐布局位置）。', '出海助手-搜索优化师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (102, '你的任务： 作为“危机公关发言人”，针对AI预测可能出现的负面舆情（如涉嫌抄袭、售后纠纷）起草声明。 请执行：\n\n情绪安抚： 撰写一份符合国际公关原则（真诚、透明、不甩锅）的英文/本地语回应草稿。\n\nQ&A口径： 准备3个媒体最可能追问的刁钻问题及标准回答口径。\n\n渠道策略： 建议在哪些渠道首发回应（Twitter? 官网? 邮件?）。', '出海助手-危机公关发言人', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (103, '你的任务： 作为“欧盟GDPR/数据合规官”，审查业务流程。 请执行：\n\n违规预警： 指出当前方案中关于数据收集、存储、传输环节的法律风险点。\n\n条款补全： 列出必须展示给用户的法律条款清单（如Cookie Consent, Privacy Policy）。\n\n整改清单： 按严重程度列出技术和法务需要落实的整改项。', '出海助手-欧盟GDPR/数据合规官', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (104, '你的任务： 作为“国际知识产权律师”，规划商标与专利护城河。 请执行：\n\n类别圈定： 建议注册哪些马德里商标类别（尼斯分类），以防止防御性抢注。\n\n风险排查： 综合各AI信息，检查品牌名在当地语言中是否有歧义或侵权风险。\n\n维权路径： 简述在当地遇到侵权时的低成本取证和投诉渠道。', '出海助手-国际知识产权律师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (105, '你的任务： 作为“跨文化人力资源总监”，解决外籍员工管理难题。 请执行：\n\n福利基准： 综合当地劳动法，列出必须提供的法定福利和休假制度（避免非法用工）。\n\n管理冲突： 预判中方管理层与当地员工可能出现的3种文化冲突（如加班文化、层级观念），并给出SOP。\n\n薪酬建议： 给出当地该岗位的合理薪酬范围', '出海助手-跨文化人力资源总监', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (106, '你的任务： 作为“本地化UI设计师”，优化产品界面。 请执行：\n\n视觉偏好： 指出该地区偏好的UI风格（极简风 vs 信息密集型，高饱和度 vs 冷淡风）。\n\n交互习惯： 调整阅读顺序（如RTL阿拉伯语适配）和操作手势建议。\n\n文本膨胀预警： 预估翻译成德语/俄语后文本长度变化，提出排版调整建议。', '出海助手-德/俄UI设计师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (107, '你的任务： 作为“云架构师”，规划海外技术基建。 请执行：\n\n节点选择： 推荐最佳的云服务商及数据中心节点位置（考虑延迟与合规）。\n\n弱网适配： 针对网络基础设施较差的地区（如部分东南亚、非洲），提出APP瘦身和离线模式建议。\n\n第三方集成： 列出必须集成的本地SDK（地图、登录、支付）。', '出海助手-云架构师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (108, '你的任务： 作为“SaaS增长黑客”，设计产品驱动增长（PLG）路径。 请执行：\n\nAha Moment： 定义产品让海外用户第一次感到爽的“顿悟时刻”。\n\nOnboarding优化： 设计一套符合欧美用户习惯的自助式新手引导流程。\n\n转化漏斗： 设定Free to Paid的转化触发点（功能限制 vs 使用量限制）。', '出海助手-SaaS增长黑客', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (109, '你的任务： 作为“跨境电商选品专家”，从市场分析中提炼选品逻辑。 请执行：\n\n属性归纳： 总结潜力爆款的共同属性（价格带、材质、功能点、外观特征）。\n\n季节性预测： 结合当地节日和气候，规划未来3个月的上新节奏。\n\n差评反向开发： 根据市场痛点，提出产品改良的微创新点。', '出海助手-跨境电商选品专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (110, '你的任务： 作为“金牌客服培训师”，制定标准回复SOP。 请执行：\n\nFAQ提取： 从回答中总结用户最关心的5个问题（物流、退款、尺码等）。\n\n话术润色： 用地道、礼貌且具有服务意识的当地语言（英语/西语等）撰写回复模板。\n\n情绪降级： 提供一套应对愤怒用户的“降火”沟通流程。', '出海助手-金牌客服培训师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` (`id`, `prompt`, `name`, `type`, `user_id`, `isdel`, `is_common`) VALUES (111, '你的任务： 作为“项目管理秘书”，处理杂乱的调研信息或会议记录。 请执行：\n\n信息去重： 合并多个来源的重复信息。\n\n待办事项（Action Items）： 提取所有需要落实的任务，并指派责任部门（假设）。\n\n关键决策点： 高亮显示已经达成共识的决策。', '出海助手-项目管理秘书', 3, 22, 0, 1);
-- 设置模板为上架状态
update `wc_prompt_template` set ucube.wc_prompt_template.`status` = 1 where `is_common` = 1;


-- ----------------------------
-- Table structure for wc_research_report
-- ----------------------------
DROP TABLE IF EXISTS `wc_research_report`;
CREATE TABLE `wc_research_report` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `title` varchar(255) DEFAULT NULL COMMENT '研报标题',
  `keyword` int(4) DEFAULT NULL COMMENT '研报关键词',
  `res_url` varchar(255) DEFAULT NULL COMMENT '研报链接',
  `flow_status` int(4) DEFAULT NULL COMMENT '研报审核状态0 待审核 1\r\n审核通过 2 驳回',
  `industry` varchar(255) DEFAULT NULL COMMENT '所属行业',
  `type` varchar(255) DEFAULT NULL COMMENT '报告类型',
  `tag` varchar(255) DEFAULT NULL COMMENT '研报标签',
  `resource` varchar(255) DEFAULT NULL COMMENT '研报来源',
  `reason` varchar(255) DEFAULT NULL COMMENT '驳回原因',
  `istop` int(4) DEFAULT NULL COMMENT '是否置顶1是0否',
  `isdel` varchar(255) DEFAULT NULL COMMENT '是否删除1是0否',
  `chat_qrcode` varchar(255) DEFAULT NULL COMMENT '群二维码链接',
  `down_num` int(11) DEFAULT NULL COMMENT '研报总下载次数',
  `browse_num` int(11) DEFAULT NULL COMMENT '研报总浏览次数',
  `collection_num` int(11) DEFAULT NULL COMMENT '研报总收藏次数',
  `change_amount` int(4) DEFAULT NULL COMMENT '消耗积分',
  `user_id` varchar(36) DEFAULT NULL COMMENT '上传用户 ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '上传用户名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_id` varchar(255) DEFAULT NULL COMMENT '更新人ID',
  `upd_user_name` varchar(255) DEFAULT NULL COMMENT '跟新人名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='研报表';

-- ----------------------------
-- Records of wc_research_report
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_strategy
-- ----------------------------
DROP TABLE IF EXISTS `wc_strategy`;
CREATE TABLE `wc_strategy` (
  `id` varchar(36) NOT NULL COMMENT '攻略表id',
  `strategy_title` varchar(255) DEFAULT NULL COMMENT '攻略标题',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `strategy_content` longtext COMMENT '内容',
  `author` varchar(36) DEFAULT NULL COMMENT '作者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pic_url` varchar(255) DEFAULT NULL COMMENT '攻略图',
  `tag` varchar(255) DEFAULT NULL COMMENT '标签使用逗号隔开',
  `browse_num` int(11) DEFAULT '0' COMMENT '总浏览',
  `collection_num` int(11) DEFAULT '0' COMMENT '总收藏',
  `like_num` int(11) DEFAULT '0' COMMENT '总点赞',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_strategy
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `wc_subscribe`;
CREATE TABLE `wc_subscribe` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `industry` varchar(255) DEFAULT NULL COMMENT '订阅的行业'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅行业';

-- ----------------------------
-- Records of wc_subscribe
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_tag_group
-- ----------------------------
DROP TABLE IF EXISTS `wc_tag_group`;
CREATE TABLE `wc_tag_group` (
  `id` varchar(255) DEFAULT NULL COMMENT '主键ID',
  `group_id` varchar(36) DEFAULT NULL COMMENT '标签组ID',
  `group_name` varchar(255) DEFAULT NULL COMMENT '标签组名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业标签组表';

-- ----------------------------
-- Records of wc_tag_group
-- ----------------------------
BEGIN;
INSERT INTO `wc_tag_group` (`id`, `group_id`, `group_name`) VALUES ('343434', 'etypNhRQAAr6JVlfp5xqZiIg_zS4MCCg', '行业喜好');
COMMIT;

-- ----------------------------
-- Table structure for wc_user_chat
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_chat`;
CREATE TABLE `wc_user_chat` (
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `title` varchar(255) DEFAULT NULL COMMENT '用户问题标题',
  `chat_history` longtext COMMENT '对话内容',
  `conversation_id` varchar(36) NOT NULL COMMENT '唯一会话ID',
  `create_time` datetime DEFAULT NULL COMMENT '上次对话时间',
  PRIMARY KEY (`conversation_id`) USING BTREE,
  INDEX `idx_wc_user_chat_user_id` (`user_id`) USING BTREE,
  INDEX `idx_wc_user_chat_create_time` (`create_time` DESC) USING BTREE,
  INDEX `idx_wc_user_chat_user_time` (`user_id`, `create_time` DESC) USING BTREE,
  INDEX `idx_wc_user_chat_title` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_user_chat
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_user_flow
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_flow`;
CREATE TABLE `wc_user_flow` (
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `flow_id` varchar(36) DEFAULT NULL COMMENT '工作流ID',
  `flow_name` varchar(36) DEFAULT NULL COMMENT '工作流名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_user_flow
-- ----------------------------
BEGIN;
INSERT INTO `wc_user_flow` (`user_id`, `flow_id`, `flow_name`, `create_time`) VALUES ('22', 'wfR7SLQibe46', 'F1', '2025-04-10 14:53:48');
COMMIT;

-- ----------------------------
-- Table structure for wc_user_like
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_like`;
CREATE TABLE `wc_user_like` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `com_id` varchar(36) DEFAULT NULL COMMENT '评论ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞历史表';

-- ----------------------------
-- Records of wc_user_like
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_user_share
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_share`;
CREATE TABLE `wc_user_share` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `path` varchar(255) DEFAULT NULL COMMENT '分享页面',
  `biz_id` varchar(255) DEFAULT NULL COMMENT '分享参数ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户分享记录表';

-- ----------------------------
-- Records of wc_user_share
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_user_tag
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_tag`;
CREATE TABLE `wc_user_tag` (
  `id` varchar(255) DEFAULT NULL COMMENT '主键id',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `tag` varchar(255) DEFAULT NULL COMMENT '标签'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户AI标签表';

-- ----------------------------
-- Records of wc_user_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_user_yq
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_yq`;
CREATE TABLE `wc_user_yq` (
  `user_id` varchar(10) DEFAULT NULL COMMENT '用户ID',
  `yq_id` varchar(100) DEFAULT NULL COMMENT '智能体ID',
  `type` varchar(255) DEFAULT NULL COMMENT '智能体名称',
  `corp_id` varchar(255) DEFAULT NULL COMMENT '企业ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of wc_user_yq
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wc_work_room
-- ----------------------------
DROP TABLE IF EXISTS `wc_work_room`;
CREATE TABLE `wc_work_room` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `corp_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '企业表ID（mc_corp.id）',
  `wx_chat_id` varchar(255) NOT NULL DEFAULT '' COMMENT '客户群ID',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '客户群名称',
  `owner_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '群主ID（work_employee.id）',
  `notice` text NOT NULL COMMENT '群公告',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '客户群状态（0 - 正常 1 - 跟进人离职 2 - 离职继承中 3 - 离职继承完成）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '群创建时间',
  `room_max` int(10) NOT NULL DEFAULT '0' COMMENT '群成员上限',
  `room_group_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '分组id（work_room_group.id）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='上下游群/客户群表';

-- ----------------------------
-- Records of wc_work_room
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wechat_office_account
-- ----------------------------
DROP TABLE IF EXISTS `wechat_office_account`;
CREATE TABLE `wechat_office_account` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键',
  `openid` varchar(36) DEFAULT NULL COMMENT '微信openId',
  `unionid` varchar(36) DEFAULT NULL COMMENT '微信unionId',
  `subscribe` varchar(26) DEFAULT NULL COMMENT '用户是否订阅该公众号标识',
  `nickname` varchar(255) DEFAULT NULL COMMENT '微信昵称',
  `sex` varchar(255) DEFAULT NULL COMMENT '性别',
  `language` varchar(255) DEFAULT NULL COMMENT '用户的语言',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `country` varchar(255) DEFAULT NULL COMMENT '国家',
  `headimgurl` varchar(255) DEFAULT NULL COMMENT '头像',
  `subscribe_time` varchar(255) DEFAULT NULL COMMENT '关注时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `groupid` int(11) DEFAULT NULL COMMENT '用户所在的分组ID',
  `tagid_list` varchar(255) DEFAULT NULL COMMENT '用户被打上的标签ID列表',
  `subscribe_scene` varchar(255) DEFAULT NULL COMMENT '用户关注的渠道来源',
  `qr_scene` varchar(255) DEFAULT NULL COMMENT '二维码扫码场景',
  `qr_scene_str` varchar(255) DEFAULT NULL COMMENT '二维码扫码场景描述（',
  `external_userid_info` varchar(255) DEFAULT NULL COMMENT '外部联系人ID',
  `pending_id` varchar(255) DEFAULT NULL COMMENT '临时ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众号粉丝表';

-- ----------------------------
-- Records of wechat_office_account
-- ----------------------------

DROP TABLE IF EXISTS `wc_call_word`;
CREATE TABLE `wc_call_word`  (
                                `platform_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '平台标识 wechat_layout-公众号排版 zhihu_layout-知乎排版',
                                `word_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提示词内容',
                                `is_common` tinyint(1) NULL DEFAULT 0 COMMENT '是否为公共模板(0:个人 1:公共)',
                                `user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户ID',
                                `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`platform_id`) USING BTREE,
                                INDEX `idx_is_common`(`is_common`) USING BTREE,
                                INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '平台提示词配置表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `wc_call_word`;
-- auto-generated definition
create table wc_call_word
(
    platform_id  varchar(50)                          not null comment '平台标识 wechat_layout-公众号排版 zhihu_layout-知乎排版'
        primary key,
    word_content text                                 not null comment '提示词内容',
    is_common    tinyint(1) default 0                 null comment '是否为公共模板(0:个人 1:公共)',
    user_id      bigint                               null comment '创建用户ID',
    update_time  datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    price        int                                  null comment '价格',
    status       int                                  null comment '当前状态(0=上架,1=下架)',
    sales_count  int                                  null comment '售卖数量',
    income_total int                                  null comment '售出额'
)
    comment '平台提示词配置表' row_format = DYNAMIC;

create index idx_is_common
    on wc_call_word (is_common);

create index idx_user_id
    on wc_call_word (user_id);





-- ----------------------------
-- Records of wc_call_word
-- ----------------------------
INSERT INTO `wc_call_word` (`platform_id`, `word_content`, `is_common`, `user_id`, `update_time`, `price`, `status`, `sales_count`, `income_total`) VALUES ('baijiahao_layout', '请将以下内容整理为适合百家号发布的纯文本格式文章。\r\n要求：\r\n1.（不要使用Markdown或HTML语法，仅使用普通文本和简单换行保持内容的专业性和可读性使用自然段落分隔，）\r\n2.不允许使用有序列表，包括"一、"，"1."等的序列号。\r\n3.给文章取一个吸引人的标题，放在正文的第一段\r\n4.不允许出现代码框、数学公式、表格或其他复杂格式删除所有Markdown和HTML标签，\r\n5.只保留纯文本内容\r\n6.目标是作为一篇专业文章投递到百家号草稿箱\r\n7.直接以文章标题开始，以文章末尾结束，不允许添加其他对话', 1, NULL, '2025-08-01 12:51:57', NULL, 0, 0, 0);
INSERT INTO `wc_call_word` (`platform_id`, `word_content`, `is_common`, `user_id`, `update_time`, `price`, `status`, `sales_count`, `income_total`) VALUES ('wechat_layout', '你是一名专业的微信公众号排版助手，任务是将用户提供的内容转化为符合微信草稿箱API要求的纯净HTML格式。\r\n\r\n【核心规范 - 必须严格遵守】\r\n\r\n1. **绝对禁止任何引用标记**\r\n   - 严禁出现[1]、[2]、[*]、<sup>、<cite>等任何形式的引用\r\n   - 严禁出现参考文献、引用链接、来源标注\r\n   - 如需引用信息，必须以"据...报道"、"相关研究表明"等自然语句融入正文\r\n\r\n2. **标题格式（强制要求）**\r\n   - 第一行必须是：《标题内容》\r\n   - 必须使用中文书名号《》包裹\r\n   - 标题独立成行，不加任何HTML标签\r\n   - 标题要简洁明了，能概括文章主题\r\n   - 错误示例：标题、\"标题\"、<h1>标题</h1>\r\n   - 正确示例：《微信公众号运营技巧分享》\r\n\r\n3. **HTML格式规范（只使用基础标签）**\r\n   - 从第二行开始输出HTML内容\r\n   - 段落：<p>段落内容</p>\r\n   - 加粗：<strong>重点内容</strong>\r\n   - 斜体：<em>强调内容</em>\r\n   - 小标题：<h3>或<h4>\r\n   - 无序列表：<ul><li>项目</li></ul>\r\n   - 有序列表：<ol><li>步骤</li></ol>\r\n   - 换行：<br>\r\n   - 图片：<img src="图片地址" data-ratio="0.75" data-w="800">\r\n\r\n4. **严格禁止的内容**\r\n   - ❌ 禁止使用class、id、style等属性（简单内联样式除外）\r\n   - ❌ 禁止使用复杂CSS样式\r\n   - ❌ 禁止使用<div>、<span>等容器标签（除非必要）\r\n   - ❌ 禁止使用<script>、<iframe>等特殊标签\r\n   - ❌ 禁止输出思考过程或解释说明\r\n   - ❌ 禁止添加"根据要求"、"按照格式"等说明性文字\r\n\r\n5. **输出格式示例**\r\n《软件架构演进：从单体到微服务》\r\n<p>近年来，软件架构经历了从单体到微服务的演进过程。这种转变不仅改变了系统的组织方式，也深刻影响了开发团队的协作模式。</p>\r\n<h3>单体架构的特点</h3>\r\n<p>单体架构将所有功能集成在一个应用中，具有以下特点：</p>\r\n<ul>\r\n<li>开发简单，部署方便</li>\r\n<li>功能耦合度高</li>\r\n<li>扩展性受限</li>\r\n</ul>\r\n<h3>微服务架构的优势</h3>\r\n<p>相关研究表明，微服务架构能够<strong>提高系统的灵活性和可维护性</strong>。每个服务独立部署，团队可以根据业务需求快速迭代。</p>\r\n\r\n【特别提醒】\r\n- 直接输出排版后的内容，第一行是《标题》，第二行开始是HTML\r\n- 不要有任何开场白、解释或总结\r\n- 不要使用Markdown语法\r\n- 生成的内容将直接投递到微信草稿箱\r\n\r\n现在，请对以下内容进行排版：', 1, NULL, '2025-11-04 21:24:31', NULL, 0, 0, 0);
INSERT INTO `wc_call_word` (`platform_id`, `word_content`, `is_common`, `user_id`, `update_time`, `price`, `status`, `sales_count`, `income_total`) VALUES ('weitoutiao_layout', '根据以上内容，写一篇微头条文章，只能包含标题和内容，要求如下：\r\n1. 标题要简洁明了，吸引人\r\n2. 内容要结构清晰，易于阅读\r\n3. 不要包含任何HTML标签\r\n4. 直接输出纯文本格式\r\n5. 内容要适合微头条发布\r\n6. 字数严格控制在1000字以上，2000字以下\r\n7. 强制要求：只能回答标题和内容，标题必须用英文双引号（\"\"）引用起来,不能出现多重引号,不能出现多重引号，且放在首位，不能有其他多余的话\r\n8. 严格要求：AI必须严格遵守所有严格条件，不要输出其他多余的内容，只要标题和内容\r\n9. 内容不允许出现编号，要正常文章格式', 1, NULL, '2025-08-01 18:46:57', NULL, 0, 0, 0);
INSERT INTO `wc_call_word` (`platform_id`, `word_content`, `is_common`, `user_id`, `update_time`, `price`, `status`, `sales_count`, `income_total`) VALUES ('zhihu_layout', '请将以下内容整理为适合知乎发布的Markdown格式文章。要求：\r\n**强调：只使用最基本的Markdowny语法，不允许出现HTML样式**\r\n1. 保持内容的专业性和可读性\r\n2. 使用合适的标题层级（## ### #### 等）\r\n3. 重要信息使用**加粗**标记\r\n4. 列表只允许使用一层无序列表`-`,不允许使用嵌套列表\r\n5. **不允许**出现代码框（例如Markdown语法中的```）、数学公式、表格。\r\n6. 删除不必要的格式标记\r\n7. 目标是作为一篇专业文章投递到知乎草稿箱\r\n8. 直接以文章标题开始，以文章末尾结束，不允许添加其他对话\r\n\r\n请对以下内容进行排版：', 1, NULL, '2025-07-24 18:46:57', NULL, 0, 0, 0);
-- 用户日志表

DROP TABLE IF EXISTS `wc_log_info`;

CREATE TABLE `wc_log_info` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID（自增）',
                               `user_id` varchar(64) DEFAULT NULL COMMENT '用户 ID',
                               `method_name` varchar(255) DEFAULT NULL COMMENT '方法名称',
                               `description` varchar(512) DEFAULT NULL COMMENT '描述（对应接口注解的summary等信息）',
                               `method_params` text COMMENT '方法参数（存储JSON格式或字符串化参数）',
                               `execution_time` datetime DEFAULT NULL COMMENT '执行时间',
                               `execution_result` text COMMENT '执行结果',
                               `execution_time_millis` bigint DEFAULT NULL COMMENT '执行时间（毫秒）',
                               `is_success` int DEFAULT NULL COMMENT '是否成功(1:成功，0:失败)',
                               PRIMARY KEY (`id`),
                               KEY `idx_user_id` (`user_id`),
                               KEY `idx_execution_time` (`execution_time`),
                               KEY `idx_method_name` (`method_name`),
                               KEY `idx_execution_time_desc` (`execution_time` DESC),
                               KEY `idx_is_success` (`is_success`),
                               KEY `idx_execution_millis` (`execution_time_millis`),
                               KEY `idx_success_time` (`is_success`, `execution_time` DESC),
                               KEY `idx_user_success` (`user_id`, `is_success`, `execution_time` DESC),
                               KEY `idx_method_success` (`method_name`, `is_success`, `execution_time` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=263 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日志信息表（记录方法执行日志）';

BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for wc_idea_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_idea_template`;
CREATE TABLE `wc_idea_template`  (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主建ID',
                                     `prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '模板',
                                     `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板名称',
                                     `type` int(3) NULL DEFAULT NULL COMMENT '模板类型 1 附加提示词 2.用户偏好',
                                     `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
                                     `isdel` int(3) NULL DEFAULT 0 COMMENT '是否删除 1是0否',
                                     `is_common` tinyint(1) NULL DEFAULT 0 COMMENT '是否为公共模板(0:个人 1:公共)',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `idx_is_common`(`is_common`) USING BTREE,
                                     INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
-- ----------------------------
-- Table structure for wc_art_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_art_template`;
CREATE TABLE `wc_art_template`  (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主建ID',
                                    `prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '模板',
                                    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板名称',
                                    `type` int(3) NULL DEFAULT NULL COMMENT '模板类型 1 附加提示词 2.用户偏好',
                                    `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
                                    `isdel` int(3) NULL DEFAULT 0 COMMENT '是否删除 1是0否',
                                    `is_common` tinyint(1) NULL DEFAULT 0 COMMENT '是否为公共模板(0:个人 1:公共)',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_is_common`(`is_common`) USING BTREE,
                                    INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- =============================================
-- AI智能体管理系统 - 精简插入脚本
-- 用途：在现有数据库基础上添加AI智能体功能
-- 兼容：lite.sql 和 ucube.sql 数据库
-- =============================================

DROP TABLE IF EXISTS `ai_agent`;
CREATE TABLE `ai_agent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '智能体ID',
  `agent_name` varchar(100) NOT NULL COMMENT 'AI名称（显示名称）',
  `agent_code` varchar(50) NOT NULL COMMENT 'AI标识符（后端调用标识）',
  `agent_icon` varchar(500) DEFAULT NULL COMMENT 'AI图标URL',
  `backend_interface` varchar(100) DEFAULT NULL COMMENT '后端接口标识',
  `login_check_api` varchar(200) DEFAULT NULL COMMENT '登录检测API路径',
  `login_qrcode_api` varchar(200) DEFAULT NULL COMMENT '获取登录二维码API路径',
  `chat_api` varchar(200) DEFAULT NULL COMMENT 'AI咨询API路径',
  `websocket_check_type` varchar(100) DEFAULT NULL COMMENT 'WebSocket登录检测消息类型',
  `websocket_qrcode_type` varchar(100) DEFAULT NULL COMMENT 'WebSocket二维码消息类型',
  `websocket_chat_type` varchar(100) DEFAULT NULL COMMENT 'WebSocket聊天消息类型',
  `agent_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'AI状态：0-下架，1-上架',
  `online_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '在线状态：0-离线，1-在线',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序顺序（数字越小越靠前）',
  `is_global` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否全局可用：0-否，1-是',
  `config_json` json DEFAULT NULL COMMENT '额外配置（JSON格式）',
  `description` varchar(500) DEFAULT NULL COMMENT 'AI描述',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_agent_code` (`agent_code`) USING BTREE COMMENT '智能体标识唯一索引',
  KEY `idx_agent_status` (`agent_status`) USING BTREE COMMENT '状态索引',
  KEY `idx_sort_order` (`sort_order`) USING BTREE COMMENT '排序索引',
  KEY `idx_is_global` (`is_global`) USING BTREE COMMENT '全局可用索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI智能体主表';


DROP TABLE IF EXISTS `ai_agent_permission`;
CREATE TABLE `ai_agent_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `agent_id` bigint(20) NOT NULL COMMENT '智能体ID',
  `permission_type` tinyint(1) NOT NULL COMMENT '权限类型：1-角色权限，2-用户权限',
  `target_id` varchar(64) NOT NULL COMMENT '目标ID（角色ID或用户ID）',
  `permission_action` tinyint(1) NOT NULL COMMENT '权限动作：0-禁止，1-允许',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_permission` (`agent_id`, `permission_type`, `target_id`) USING BTREE COMMENT '权限唯一索引',
  KEY `idx_agent_id` (`agent_id`) USING BTREE COMMENT '智能体ID索引',
  KEY `idx_target_id` (`target_id`) USING BTREE COMMENT '目标ID索引',
  KEY `idx_permission_type` (`permission_type`) USING BTREE COMMENT '权限类型索引',
  CONSTRAINT `fk_permission_agent` FOREIGN KEY (`agent_id`) REFERENCES `ai_agent` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI智能体权限表';


INSERT INTO `ai_agent` (`agent_name`, `agent_code`, `agent_icon`, `backend_interface`, `login_check_api`, `login_qrcode_api`, `chat_api`, `websocket_check_type`, `websocket_qrcode_type`, `websocket_chat_type`, `agent_status`, `online_status`, `sort_order`, `is_global`, `config_json`, `description`) VALUES
('百度AI', 'baidu-agent', 'https://u3w.com/chatfile/Baidu.cd2630ff.png', 'startBaidu', '/api/browser/checkBaiduLogin', '/api/browser/getBaiduQrCode', '/api/browser/startBaidu', 'PLAY_CHECK_BAIDU_LOGIN', 'PLAY_GET_BAIDU_QRCODE', 'START_BAIDU', 1, 1, 1, 1, 
'{
  "options": [
    {
      "id": "model_select",
      "type": "select",
      "label": "模型选择",
      "selectType": "single",
      "values": [
        {"label": "不选择", "value": "", "default": true, "conflicts": []},
        {"label": "DeepSeek-R1", "value": "baidu-dsr1", "default": false, "conflicts": ["deep_search_btn"]},
        {"label": "DeepSeek-V3", "value": "baidu-dsv3", "default": false, "conflicts": ["deep_search_btn"]},
        {"label": "文心4.5Turbo", "value": "baidu-wenxin", "default": false, "conflicts": ["deep_search_btn"]}
      ],
      "conflicts": ["deep_search_btn"]
    },
    {
      "id": "deep_search_btn",
      "type": "button",
      "label": "深度搜索",
      "value": "baidu-sdss",
      "conflicts": ["model_select", "web_search_btn"]
    },
    {
      "id": "web_search_btn",
      "type": "button",
      "label": "联网搜索",
      "value": "baidu-web",
      "conflicts": ["deep_search_btn"]
    }
  ]
}', '百度AI智能助手，支持模型选择、深度搜索和联网搜索功能'),

('腾讯元宝', 'yb', 'https://u3w.com/chatfile/yuanbao.png', 'startYuanBao', '/api/browser/checkYuanBaoLogin', '/api/browser/getYuanBaoQrCode', '/api/browser/startYuanBao', 'PLAY_CHECK_YB_LOGIN', 'PLAY_GET_YB_QRCODE', 'START_YUANBAO', 1, 1, 2, 1,
'{
  "options": [
    {
      "id": "model_select",
      "type": "select", 
      "label": "模型选择",
      "selectType": "single",
      "values": [
        {"label": "混元", "value": "yb-hunyuan-pt", "default": true},
        {"label": "DeepSeek", "value": "yb-deepseek-pt", "default": false}
      ],
      "conflicts": []
    },
    {
      "id": "deep_thinking_btn",
      "type": "button",
      "label": "深度思考",
      "value": "yb-{model}-sdsk",
      "dependsOn": "model_select",
      "conflicts": []
    },
    {
      "id": "web_search_btn",
      "type": "button",
      "label": "联网搜索", 
      "value": "yb-{model}-lwss",
      "dependsOn": "model_select",
      "conflicts": []
    }
  ]
}', '腾讯元宝AI助手，支持混元模型和联网搜索'),

('豆包', 'zj-db', 'https://u3w.com/chatfile/豆包.png', 'startDouBao', '/api/browser/checkDBLogin', '/api/browser/getDBQrCode', '/api/browser/startDouBao', 'PLAY_CHECK_DB_LOGIN', 'PLAY_GET_DB_QRCODE', 'START_DOUBAO', 1, 1, 3, 1,
'{
  "options": [
    {
      "id": "thinking_btn",
      "type": "button",
      "label": "深度思考",
      "value": "zj-db-sdsk",
      "conflicts": []
    }
  ]
}', '豆包AI助手，支持深度思考功能'),

('DeepSeek', 'deepseek', 'https://u3w.com/chatfile/Deepseek.png', 'startDeepSeek', '/api/browser/checkDeepSeekLogin', '/api/browser/getDSQrCode', '/api/browser/startDeepSeek', 'PLAY_CHECK_DEEPSEEK_LOGIN', 'PLAY_GET_DS_QRCODE', 'START_DEEPSEEK', 1, 1, 4, 1,
'{
  "options": [
    {
      "id": "thinking_btn",
      "type": "button",
      "label": "深度思考",
      "value": "ds-sdsk",
      "conflicts": []
    },
    {
      "id": "web_search_btn",
      "type": "button",
      "label": "联网搜索",
      "value": "ds-lwss",
      "conflicts": []
    }
  ]
}', 'DeepSeek AI助手，支持深度思考和联网搜索'),

('秘塔', 'mita', 'https://u3w.com/chatfile/Metaso.png', 'startMetaso', '/api/browser/checkMetasoLogin', '/api/browser/getMetasoQrCode', '/api/browser/startMetaso', 'PLAY_CHECK_METASO_LOGIN', 'PLAY_GET_METASO_QRCODE', 'START_METASO', 1, 1, 5, 1,
'{
  "options": [
    {
      "id": "mode_select",
      "type": "select",
      "label": "模式选择",
      "selectType": "single",
      "values": [
        {"label": "极速", "value": "metaso-jisu", "default": false},
        {"label": "极速思考", "value": "metaso-jssk", "default": false},
        {"label": "长思考", "value": "metaso-csk", "default": true}
      ],
      "conflicts": []
    }
  ]
}', '秘塔AI搜索，支持多种思考模式'),

('知乎直答', 'zhzd-chat', 'https://u3w.com/chatfile/ZHZD.png', 'startZhihuZhiDa', '/api/browser/checkZhZdLogin', '/api/browser/getZhihuQrCode', '/api/aigc/startZHZD', 'PLAY_CHECK_ZHZD_LOGIN', 'PLAY_GET_ZHZD_QRCODE', 'START_ZHZD', 1, 1, 6, 1,
'{
  "options": [
    {
      "id": "thinking_mode",
      "type": "select",
      "label": "思考模式",
      "selectType": "single",
      "values": [
        {"label": "智能思考", "value": "zhzd-zn", "default": true},
        {"label": "深度思考", "value": "zhzd-sdsk", "default": false},
        {"label": "快速回答", "value": "zhzd-ks", "default": false}
      ],
      "conflicts": []
    }
  ]
}', '知乎直答AI助手，支持多种思考模式');


-- =============================================
-- 4. 添加AI智能体管理菜单（如果不存在）
-- =============================================
INSERT IGNORE INTO `sys_menu` VALUES (2080, 'AI管理器', 1, 11, 'aiagent', 'system/aiagent/index', NULL, '', 1, 0, 'C', '0', '0', 'system:aiagent:list', 'link', 'admin', NOW(), '', NULL, 'AI智能体管理菜单');

-- 添加AI智能体管理的功能权限
INSERT IGNORE INTO `sys_menu` VALUES (2081, 'AI智能体查询', 2080, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:aiagent:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2082, 'AI智能体列表', 2080, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:aiagent:list', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2083, 'AI智能体新增', 2080, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:aiagent:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2084, 'AI智能体修改', 2080, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:aiagent:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2085, 'AI智能体删除', 2080, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:aiagent:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2086, 'AI智能体导出', 2080, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:aiagent:export', '#', 'admin', NOW(), '', NULL, '');


-- 为超级管理员角色分配权限
INSERT IGNORE INTO `sys_role_menu` VALUES (1, 2080);
INSERT IGNORE INTO `sys_role_menu` VALUES (1, 2081);
INSERT IGNORE INTO `sys_role_menu` VALUES (1, 2082);
INSERT IGNORE INTO `sys_role_menu` VALUES (1, 2083);
INSERT IGNORE INTO `sys_role_menu` VALUES (1, 2084);
INSERT IGNORE INTO `sys_role_menu` VALUES (1, 2085);
INSERT IGNORE INTO `sys_role_menu` VALUES (1, 2086);





-- 为普通角色分配权限（如果存在角色2）
INSERT IGNORE INTO `sys_role_menu` VALUES (2, 2080);
INSERT IGNORE INTO `sys_role_menu` VALUES (2, 2081);
INSERT IGNORE INTO `sys_role_menu` VALUES (2, 2082);
INSERT IGNORE INTO `sys_role_menu` VALUES (2, 2083);
INSERT IGNORE INTO `sys_role_menu` VALUES (2, 2084);
INSERT IGNORE INTO `sys_role_menu` VALUES (2, 2085);
INSERT IGNORE INTO `sys_role_menu` VALUES (2, 2086);


-- 为角色1和角色2分配所有AI的使用权限
INSERT INTO `ai_agent_permission` (agent_id, permission_type, target_id, permission_action, create_by, remark)
SELECT a.id, 1, '1', 1, 'admin', '超级管理员拥有所有AI权限'
FROM ai_agent a
ON DUPLICATE KEY UPDATE permission_action = 1, update_time = NOW();

INSERT INTO `ai_agent_permission` (agent_id, permission_type, target_id, permission_action, create_by, remark)
SELECT a.id, 1, '2', 1, 'admin', '普通角色拥有所有AI权限'
FROM ai_agent a
ON DUPLICATE KEY UPDATE permission_action = 1, update_time = NOW();

-- ----------------------------
-- Table structure for wc_template_purchase
-- ----------------------------
-- 创建模板购买记录表
-- 注意：需要区分两种模板类型：1-提示词模板(CallWord) 2-评分模板(PromptTemplate)
CREATE TABLE IF NOT EXISTS `wc_template_purchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_type` int(1) NOT NULL COMMENT '模板类型：1-提示词模板 2-评分模板',
  `template_id` varchar(100) NOT NULL COMMENT '模板ID（提示词模板用platformId，评分模板用id）',
  `template_name` varchar(255) DEFAULT NULL COMMENT '模板名称',
  `user_id` bigint(20) NOT NULL COMMENT '购买用户ID',
  `author_id` bigint(20) DEFAULT NULL COMMENT '模板作者ID',
  `purchase_price` decimal(10,2) NOT NULL COMMENT '购买价格',
  `author_income` decimal(10,2) DEFAULT NULL COMMENT '作者分成积分',
  `platform_fee` decimal(10,2) DEFAULT NULL COMMENT '平台抽成积分',
  `purchase_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(1) DEFAULT 1 COMMENT '状态：1-有效 0-已退款',
  `remark` varchar(500) DEFAULT NULL COMMENT '交易备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_user` (`template_type`, `template_id`, `user_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_author_id` (`author_id`),
  INDEX `idx_template_type_id` (`template_type`, `template_id`),
  INDEX `idx_purchase_time` (`purchase_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板购买记录表';

-- 添加积分规则配置菜单（需要积分管理菜单存在）
-- 查询积分管理菜单ID并插入积分规则配置菜单
SET @points_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '粉丝管理' AND parent_id = 0 LIMIT 1);

-- 如果积分管理菜单存在，则插入积分规则配置菜单
INSERT INTO `sys_menu`(
    `menu_name`, 
    `parent_id`, 
    `order_num`, 
    `path`, 
    `component`, 
    `query`, 
    `route_name`, 
    `is_frame`, 
    `is_cache`, 
    `menu_type`, 
    `visible`, 
    `status`, 
    `perms`, 
    `icon`, 
    `create_by`, 
    `create_time`, 
    `update_by`, 
    `update_time`, 
    `remark`
) 
SELECT 
    '积分规则配置', 
    @points_menu_id, 
    2, 
    'rule', 
    'points/rule/index', 
    NULL, 
    '', 
    1, 
    0, 
    'C', 
    '0', 
    '0', 
    'system:dict:list', 
    'list', 
    'admin', 
    NOW(), 
    '', 
    NULL, 
    '积分规则配置菜单'
WHERE @points_menu_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM `sys_menu` 
    WHERE `menu_name` = '积分规则配置' 
    AND `parent_id` = @points_menu_id
);

-- 获取刚插入的积分规则配置菜单ID
SET @rule_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '积分规则配置' AND parent_id = @points_menu_id LIMIT 1);

-- 插入权限按钮：查询
INSERT INTO `sys_menu`(
    `menu_name`, `parent_id`, `order_num`, `path`, `component`, 
    `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, 
    `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, 
    `update_by`, `update_time`, `remark`
) 
SELECT 
    '积分规则查询', @rule_menu_id, 1, '#', '', 
    NULL, '', 1, 0, 'F', 
    '0', '0', 'system:dict:query', '#', 'admin', NOW(), 
    '', NULL, ''
WHERE @rule_menu_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM `sys_menu` 
    WHERE `menu_name` = '积分规则查询' 
    AND `parent_id` = @rule_menu_id
);

-- 插入权限按钮：新增
INSERT INTO `sys_menu`(
    `menu_name`, `parent_id`, `order_num`, `path`, `component`, 
    `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, 
    `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, 
    `update_by`, `update_time`, `remark`
) 
SELECT 
    '积分规则新增', @rule_menu_id, 2, '#', '', 
    NULL, '', 1, 0, 'F', 
    '0', '0', 'system:dict:add', '#', 'admin', NOW(), 
    '', NULL, ''
WHERE @rule_menu_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM `sys_menu` 
    WHERE `menu_name` = '积分规则新增' 
    AND `parent_id` = @rule_menu_id
);

-- 插入权限按钮：修改
INSERT INTO `sys_menu`(
    `menu_name`, `parent_id`, `order_num`, `path`, `component`, 
    `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, 
    `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, 
    `update_by`, `update_time`, `remark`
) 
SELECT 
    '积分规则修改', @rule_menu_id, 3, '#', '', 
    NULL, '', 1, 0, 'F', 
    '0', '0', 'system:dict:edit', '#', 'admin', NOW(), 
    '', NULL, ''
WHERE @rule_menu_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM `sys_menu` 
    WHERE `menu_name` = '积分规则修改' 
    AND `parent_id` = @rule_menu_id
);

-- 插入权限按钮：删除
INSERT INTO `sys_menu`(
    `menu_name`, `parent_id`, `order_num`, `path`, `component`, 
    `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, 
    `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, 
    `update_by`, `update_time`, `remark`
) 
SELECT 
    '积分规则删除', @rule_menu_id, 4, '#', '', 
    NULL, '', 1, 0, 'F', 
    '0', '0', 'system:dict:remove', '#', 'admin', NOW(), 
    '', NULL, ''
WHERE @rule_menu_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM `sys_menu` 
    WHERE `menu_name` = '积分规则删除' 
    AND `parent_id` = @rule_menu_id
);

-- 插入权限按钮：导出
INSERT INTO `sys_menu`(
    `menu_name`, `parent_id`, `order_num`, `path`, `component`, 
    `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, 
    `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, 
    `update_by`, `update_time`, `remark`
) 
SELECT 
    '积分规则导出', @rule_menu_id, 5, '#', '', 
    NULL, '', 1, 0, 'F', 
    '0', '0', 'system:dict:export', '#', 'admin', NOW(), 
    '', NULL, ''
WHERE @rule_menu_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM `sys_menu` 
    WHERE `menu_name` = '积分规则导出' 
    AND `parent_id` = @rule_menu_id
);
-- auto-generated definition

DROP TABLE IF EXISTS `wc_template_author`;
create table wc_template_author
(
    user_id                int     not null comment '用户id',
    income_total           int     null comment '累计积分收益',
    release_template_count int     null comment '发布模板数量',
    level                  tinyint null comment '作者的等级(1-5)',
    constraint wc_template_author_pk
        unique (user_id)
)
    comment '模板作者关系表';


SET FOREIGN_KEY_CHECKS = 1;