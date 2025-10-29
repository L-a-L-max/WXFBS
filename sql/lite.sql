/*
 Navicat Premium Data Transfer

 Source Server         : u3w
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 175.178.154.216:3306
 Source Schema         : ucube

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Da0te: 15/10/2025 13:38:33-- 修复wc_chat_history表结构，添加唯一约束和主键
-- 作用：支持ON DUPLICATE KEY UPDATE，避免重复保存历史记录

-- 1. 先清理可能存在的重复数据（保留最新的记录）
DELETE t1 FROM wc_chat_history t1
INNER JOIN wc_chat_history t2
WHERE t1.chat_id = t2.chat_id
  AND t1.user_id = t2.user_id
  AND t1.create_time < t2.create_time
> Affected rows: 63
> 查询时间: 0.051s


-- 2. 修改id字段为主键
ALTER TABLE `wc_chat_history`
MODIFY COLUMN `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID'
> OK
> 查询时间: 0.263s


-- 3. 添加主键（如果不存在）
ALTER TABLE `wc_chat_history`
ADD PRIMARY KEY (`id`) USING BTREE
> OK
> 查询时间: 1.256s


-- 4. 在chat_id和user_id上添加唯一索引（用于ON DUPLICATE KEY UPDATE）
ALTER TABLE `wc_chat_history`
ADD UNIQUE INDEX `uk_user_chat` (`user_id`, `chat_id`) USING BTREE
> OK
> 查询时间: 0.078s


-- 5. 添加普通索引提升查询性能
ALTER TABLE `wc_chat_history`
ADD INDEX `idx_user_create_time` (`user_id`, `create_time`) USING BTREE
> OK
> 查询时间: 0.055s

*/
-- 1. 创建数据库（指定字符集，适配中文和特殊字符）
CREATE DATABASE IF NOT EXISTS ucube 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_general_ci;

-- 2. 选择要操作的数据库（必须执行，否则后续操作无对象）
USE ucube;
-- ----------------------------
-- Table structure for corplist-- ----------------------------
DROP TABLE IF EXISTS `corplist`;
CREATE TABLE `corplist`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `corpid` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '企业id',
  `secret` varchar(43) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '聊天内容存档的Secret',
  `corpname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '企业名称',
  `prikey` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密文的私有密钥',
  `limits` int(11) NOT NULL DEFAULT 100 COMMENT '一次拉取的消息条数，最大值1000条',
  `timeout` int(11) NOT NULL DEFAULT 5 COMMENT '超时时间(秒)',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0:用户无效 1:用户有效',
  `update` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:无更新 1:有更新',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `corpid`(`corpid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公司表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of corplist
-- ----------------------------

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` bigint(20) NOT NULL COMMENT '主键，文件id',
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件在云端的唯一标示，例如：aaa.jpg',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件上传时的名称',
  `request_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求id',
  `status` tinyint(4) NOT NULL COMMENT '状态：1-待上传 2-已上传,未使用 3-已使用',
  `platform` tinyint(4) NULL DEFAULT 1 COMMENT '平台：1-腾讯，2-阿里',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `creater` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建者',
  `updater` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新者',
  `dep_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '部门id',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除，默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件表，可以是普通文件、图片等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1, 'qywx_third_company', '企业微信三方应用授权公司', NULL, NULL, 'QywxThirdCompany', 'crud', 'element-ui', 'com.cube.system', 'system', 'company', '企业微信三方应用授权公司', 'ruoyi', '0', '/', '{}', 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15', NULL);
INSERT INTO `gen_table` VALUES (2, 'sys_user', '用户信息表', NULL, NULL, 'SysUser', 'crud', '', 'com.cube.system', 'system', 'user', '用户信息', 'ruoyi', '0', '/', NULL, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (3, 'wc_strategy', '攻略表', '', '', 'WcStrategy', 'crud', 'element-ui', 'com.cube.wechat.selfapp.app', 'admin', 'strategy', '攻略记录', 'keke', '0', '/', '{}', 'keweiren', '2024-11-12 11:12:47', '', '2024-11-12 15:25:26', NULL);
INSERT INTO `gen_table` VALUES (5, 'wc_chrome_data', '', NULL, NULL, 'WcChromeData', 'crud', '', 'com.cube.system', 'system', 'data', NULL, 'ruoyi', '0', '/', NULL, 'YangHangHang', '2024-11-22 10:01:23', '', NULL, NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint(20) NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1, 1, 'id', '主键id', 'varchar(36)', 'String', 'id', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (2, 1, 'corp_id', '企业id', 'varchar(45)', 'String', 'corpId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (3, 1, 'permanent_code', '企业永久授权码', 'varchar(512)', 'String', 'permanentCode', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 3, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (4, 1, 'corp_name', '企业名称', 'varchar(50)', 'String', 'corpName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (5, 1, 'corp_full_name', '企业全称', 'varchar(100)', 'String', 'corpFullName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 5, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (6, 1, 'subject_type', '企业类型', 'varchar(512)', 'String', 'subjectType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 6, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (7, 1, 'verified_end_time', '企业认证到期时间', 'varchar(512)', 'String', 'verifiedEndTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 7, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (8, 1, 'suite_id', '应用唯一ID', 'varchar(200)', 'String', 'suiteId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (9, 1, 'agent_id', '授权应用id', 'varchar(255)', 'String', 'agentId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (10, 1, 'status', '账户状态，-1为删除，禁用为0 启用为1', 'int(11)', 'Long', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 10, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (11, 1, 'addtime', '创建时间', 'datetime', 'Date', 'addtime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 11, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (12, 1, 'modtime', '修改时间', 'datetime', 'Date', 'modtime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 12, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (13, 1, 'rectime', '变动时间', 'datetime', 'Date', 'rectime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 13, 'admin', '2024-08-08 14:11:52', '', '2025-10-15 17:21:15');
INSERT INTO `gen_table_column` VALUES (14, 2, 'user_id', '用户ID', 'bigint(20)', 'Long', 'userId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (15, 2, 'open_id', '微信ID', 'varchar(36)', 'String', 'openId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (16, 2, 'union_id', '开放平台ID', 'varchar(36)', 'String', 'unionId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (17, 2, 'qw_id', '企微ID', 'varchar(66)', 'String', 'qwId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (18, 2, 'dept_id', '部门ID', 'bigint(20)', 'Long', 'deptId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (19, 2, 'user_name', '用户账号', 'varchar(255)', 'String', 'userName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 6, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (20, 2, 'nick_name', '用户昵称', 'varchar(30)', 'String', 'nickName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 7, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (21, 2, 'user_type', '用户类型（00系统用户）', 'varchar(2)', 'String', 'userType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 8, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (22, 2, 'email', '用户邮箱', 'varchar(50)', 'String', 'email', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (23, 2, 'phonenumber', '手机号码', 'varchar(11)', 'String', 'phonenumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (24, 2, 'sex', '用户性别（0男 1女 2未知）', 'char(1)', 'String', 'sex', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 11, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (25, 2, 'avatar', '头像地址', 'varchar(255)', 'String', 'avatar', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'DuRuiNing', '2024-09-12 09:43:00', '', NULL);
INSERT INTO `gen_table_column` VALUES (26, 2, 'password', '密码', 'varchar(100)', 'String', 'password', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (27, 2, 'status', '帐号状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 14, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (28, 2, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 15, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (29, 2, 'login_ip', '最后登录IP', 'varchar(128)', 'String', 'loginIp', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 16, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (30, 2, 'login_date', '最后登录时间', 'datetime', 'Date', 'loginDate', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 17, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (31, 2, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 18, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (32, 2, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 19, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (33, 2, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 20, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (34, 2, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 21, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (35, 2, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 22, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (36, 2, 'corp_id', '企业ID', 'varchar(36)', 'String', 'corpId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 23, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (37, 2, 'points', '积分余额', 'int(5)', 'Integer', 'points', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 24, 'DuRuiNing', '2024-09-12 09:43:01', '', NULL);
INSERT INTO `gen_table_column` VALUES (38, 3, 'id', '攻略表id', 'varchar(36)', 'String', 'id', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'keweiren', '2024-11-12 11:12:47', '', '2024-11-12 15:25:26');
INSERT INTO `gen_table_column` VALUES (39, 3, 'strategy_title', '攻略标题', 'varchar(255)', 'String', 'strategyTitle', '0', '0', '0', '1', '1', '1', '0', 'LIKE', 'input', '', 2, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (41, 3, 'strategy_content', '内容', 'varchar(2048)', 'String', 'strategyContent', '0', '0', '0', '1', '1', '1', '0', 'LIKE', 'editor', '', 3, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (42, 3, 'author', '作者', 'varchar(36)', 'String', 'author', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (43, 3, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, '1', NULL, 'EQ', 'datetime', '', 5, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (48, 3, 'comment_id', '评论id', 'varchar(36)', 'String', 'commentId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 10, 'keweiren', '2024-11-12 11:12:48', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (49, 3, 'view_id', '浏览id', 'int(11)', 'Long', 'viewId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 6, '', '2024-11-12 14:04:52', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (50, 3, 'like_id', '点赞id', 'int(11)', 'Long', 'likeId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 7, '', '2024-11-12 14:04:53', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (51, 3, 'collections_id', '收藏id', 'int(11)', 'Long', 'collectionsId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 8, '', '2024-11-12 14:04:53', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (53, 3, 'tag_id', '标签id', 'varchar(255)', 'String', 'tagId', '0', '0', '0', '1', '1', '0', '0', 'EQ', 'input', '', 11, '', '2024-11-12 14:04:53', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (58, 3, 'pic_url', '攻略图', 'varchar(255)', 'String', 'picUrl', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'imageUpload', '', 9, '', '2024-11-12 15:24:29', '', '2024-11-12 15:25:27');
INSERT INTO `gen_table_column` VALUES (59, 5, 'id', '主建', 'varchar(36)', 'String', 'id', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` VALUES (60, 5, 'prompt', '提示词', 'varchar(255)', 'String', 'prompt', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` VALUES (61, 5, 'answer', '答案', 'varchar(1000)', 'String', 'answer', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 3, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` VALUES (62, 5, 'name', 'ai名称', 'varchar(255)', 'String', 'name', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);
INSERT INTO `gen_table_column` VALUES (63, 5, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'YangHangHang', '2024-11-22 10:01:23', '', NULL);

-- ----------------------------
-- Table structure for geth_tran_record
-- ----------------------------
DROP TABLE IF EXISTS `geth_tran_record`;
CREATE TABLE `geth_tran_record`  (
  `tran_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `from` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `to` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ether` int(10) NULL DEFAULT NULL,
  `chage_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '以太坊交易记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of geth_tran_record
-- ----------------------------

-- ----------------------------
-- Table structure for geth_user_rel
-- ----------------------------
DROP TABLE IF EXISTS `geth_user_rel`;
CREATE TABLE `geth_user_rel`  (
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `privateKey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户私钥',
  `publicKey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公钥',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '以太坊账户'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '以太坊账户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of geth_user_rel
-- ----------------------------
INSERT INTO `geth_user_rel` VALUES (22, 'b8bde749d1ba80d1663cf83970327f83702015a19a07fd70dc63b27c7e8c271f', '36833e719e9d5b96ceced08ce0d1157623d112735b3f95cc468977e7e505d78eb4f44479e92b9943a375d5589874303c252009214d6539d72639a4695cf80217', '0x8a23bde7103320b2f5d9eee6c90faa0e8e1ba925');

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `userPrompt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `response` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `createTime` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fileUrl` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `chatHistory` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '智能体会话记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of msg
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-08-08 14:05:58', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业ID',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '', '优立方', 1, NULL, NULL, NULL, '0', '0', '企微同步', '2024-08-29 15:07:28', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (2, 1, '', '其他（待设置部门）', 1, NULL, NULL, NULL, '0', '0', '企微同步', '2024-08-29 15:07:28', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:58', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-08-08 14:05:58', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-08-08 14:05:58', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 1, '科技传媒', '科技传媒', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (101, 2, '大消费', '大消费', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:24', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (102, 3, '出海专题', '出海专题', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (103, 4, '健康医疗', '健康医疗', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:50:47', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (104, 5, '金融地产', '金融地产', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (105, 6, '能源矿产', '能源矿产', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (106, 7, '工业制造', '工业制造', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:19', 'YangHangHang', '2024-09-24 13:51:32', NULL);
INSERT INTO `sys_dict_data` VALUES (107, 8, '交通物流', '交通物流', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (108, 9, '公共服务', '公共服务', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:42', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (109, 10, '农林牧渔', '农林牧渔', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:51:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (110, 11, '综合其他', '综合其他', 'sys_industry', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:52:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (111, 1, '新鲜出炉', '新鲜出炉', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (112, 2, '深度研究', '深度研究', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (113, 3, '热门', '热门', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:43', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (114, 4, '英文', '英文', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 13:59:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (115, 5, '行业研究', '行业研究', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (116, 6, '公司研究', '公司研究', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (117, 7, '产业政策', '产业政策', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (118, 8, 'AI', 'AI', 'sys_user_tag', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-24 14:00:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (119, 0, '首次登录', '200', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:52:05', 'admin', '2025-05-29 09:07:07', NULL);
INSERT INTO `sys_dict_data` VALUES (120, 1, '行业订阅', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:53:01', 'YangHangHang', '2024-09-27 13:56:50', '单位：每个行业');
INSERT INTO `sys_dict_data` VALUES (121, 2, '首次完善资料', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:53:47', 'YangHangHang', '2024-09-27 14:57:23', NULL);
INSERT INTO `sys_dict_data` VALUES (122, 3, '每日首次登录', '2', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:54:53', 'DuRuiNing', '2024-12-09 13:50:39', NULL);
INSERT INTO `sys_dict_data` VALUES (123, 4, '每日首次分享', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:55:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (124, 5, '下载干货', '-5', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-09-27 13:56:31', 'YangHangHang', '2024-09-27 13:58:10', NULL);
INSERT INTO `sys_dict_data` VALUES (125, 6, '每日评论', '2', 'sys_point_rule', NULL, 'default', 'N', '0', 'YangHangHang', '2024-11-18 13:45:58', 'DuRuiNing', '2024-12-09 13:52:28', '每天上限10分');
INSERT INTO `sys_dict_data` VALUES (126, 7, '插件首次登录', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'DuRuiNing', '2024-12-09 13:51:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (127, 8, '每日优立方登录', '50', 'sys_point_rule', NULL, 'default', 'N', '0', 'DuRuiNing', '2024-12-09 13:52:01', 'admin', '2025-05-28 23:18:56', NULL);
INSERT INTO `sys_dict_data` VALUES (128, 1, '元透社AI内容创作学习卡', '99', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-25 09:43:19', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-25 09:43:38', NULL);
INSERT INTO `sys_dict_data` VALUES (129, 9, '邀请新用户', '10', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:43:03', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (130, 10, '积分99充值卡', '99', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:43:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (131, 11, '解绑能力使用', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:44:35', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:47:08', NULL);
INSERT INTO `sys_dict_data` VALUES (132, 12, '收录公众号', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:45:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (133, 12, '工作站自动化使用', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:47:30', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (134, 13, '每日打卡', '1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:47:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (135, 14, '学习课程视频', '1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:49:40', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (136, 15, '公众号修改', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:54:58', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (137, 16, '社群分享内容', '1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 14:59:48', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (138, 9, '12D学习卡', '12', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2024-12-31 15:00:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (139, 17, '调用公众号智能体API', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-21 13:57:24', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-21 13:57:38', NULL);
INSERT INTO `sys_dict_data` VALUES (140, 18, '首次绑定元宝账号', '5', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:54:46', 'admin', '2025-03-27 15:08:57', '');
INSERT INTO `sys_dict_data` VALUES (141, 19, '模板配置', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:55:02', 'admin', '2025-03-27 15:16:53', '附加提示词配置');
INSERT INTO `sys_dict_data` VALUES (142, 20, '记忆修改', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:55:19', 'admin', '2025-03-27 15:17:00', '记忆描述配置');
INSERT INTO `sys_dict_data` VALUES (143, 21, '使用F8S', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-03-26 15:55:34', 'admin', '2025-03-27 15:09:21', '');
INSERT INTO `sys_dict_data` VALUES (144, 22, 'AI评分', '-1', 'sys_point_rule', NULL, 'default', 'N', '0', 'admin', '2025-05-15 09:23:28', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2024-08-08 14:05:57', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, '行业分类', 'sys_industry', '0', 'YangHangHang', '2024-09-24 13:48:31', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (101, '用户标签', 'sys_user_tag', '0', 'YangHangHang', '2024-09-24 13:58:45', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (102, '积分规则', 'sys_point_rule', '0', 'YangHangHang', '2024-09-27 13:51:25', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_host_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `sys_host_whitelist`;
CREATE TABLE `sys_host_whitelist`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `host_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主机ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '是否启用：1启用，0禁用',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `host_id`(`host_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_host_whitelist
-- ----------------------------
INSERT INTO `sys_host_whitelist` VALUES (1, 'office01', 1, NULL, '2025-10-27 17:29:56', '2025-10-27 17:29:56');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2024-08-08 14:05:58', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2024-08-08 14:05:58', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2024-08-08 14:05:58', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status`) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 993 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2049 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2024-08-08 14:05:51', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2024-08-08 14:05:51', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2024-08-08 14:05:51', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2024-08-08 14:05:51', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2024-08-08 14:05:51', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2024-08-08 14:05:51', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2024-08-08 14:05:51', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2024-08-08 14:05:51', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2024-08-08 14:05:51', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2024-08-08 14:05:51', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2024-08-08 14:05:51', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2024-08-08 14:05:51', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2024-08-08 14:05:51', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2024-08-08 14:05:51', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '1', 'monitor:druid:list', 'druid', 'admin', '2024-08-08 14:05:51', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2024-08-08 14:05:51', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2024-08-08 14:05:52', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2024-08-08 14:05:52', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2024-08-08 14:05:52', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2024-08-08 14:05:52', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2024-08-08 14:05:52', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2024-08-08 14:05:52', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2024-08-08 14:05:52', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2024-08-08 14:05:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2024-08-08 14:05:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '内容管理', 0, 0, 'wechat', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'education', 'admin', '2024-08-08 14:14:32', 'YangHangHang', '2024-11-12 10:59:05', '');
INSERT INTO `sys_menu` VALUES (2001, '用户列表', 2051, 1, 'user-list', 'wechat/company/index', NULL, '', 1, 0, 'C', '0', '0', 'wechat:company:list', 'tree-table', 'admin', '2024-08-08 14:17:40', 'admin', '2025-05-13 09:48:53', '');
INSERT INTO `sys_menu` VALUES (2007, '积分设置', 2051, 2, 'points', 'wechat/company/index', NULL, '', 1, 0, 'C', '0', '0', 'wechat:points:list', 'money', 'DuHongChao-YuanTouShe', '2024-09-13 14:35:36', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, 'AI精选一站通', 2000, 0, 'chrome', 'wechat/chrome/index', NULL, '', 1, 0, 'C', '0', '0', 'wechat:chrome:list', 'international', 'YangHangHang', '2024-11-22 09:59:55', 'admin', '2025-10-29 12:00:00', '');
INSERT INTO `sys_menu` VALUES (2021, '稿库', 2000, 1, 'drafts', 'wechat/drafts/index', NULL, '', 1, 0, 'C', '0', '0', '', 'log', 'o3lds60Lfe6_MaGyB-COxgGcItnM', '2025-01-06 17:23:00', 'admin', '2025-06-06 08:46:16', '');
INSERT INTO `sys_menu` VALUES (2022, '用户日志', 108, 1, 'userLog', 'monitor/userLog/index', NULL, '', 1, 0, 'C', '0', '0', 'monitor:userLog:list', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '用户日志菜单');
INSERT INTO `sys_menu` VALUES (2023, '用户日志查询', 2022, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'monitor:userLog:query', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '用户日志新增', 2022, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'monitor:userLog:add', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2025, '用户日志修改', 2022, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'monitor:userLog:edit', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2026, '用户日志删除', 2022, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'monitor:userLog:remove', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2027, '用户日志导出', 2022, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'monitor:userLog:export', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2028, '平台提示词配置', 2050, 1, 'callWord', 'wechat/callWord/index', NULL, '', 1, 0, 'C', '0', '0', 'wechat:callWord:list', 'guide', 'admin', '2025-10-15 17:05:34', '', NULL, '平台提示词配置菜单');
INSERT INTO `sys_menu` VALUES (2029, '平台提示词配置新增', 2028, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:callWord:add', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2030, '平台提示词配置修改', 2028, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:callWord:edit', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2031, '平台提示词配置删除', 2028, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:callWord:remove', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2032, '平台提示词配置导出', 2028, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:callWord:export', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2033, '评分模板配置', 2050, 2, 'prompt', 'wechat/prompt/index', NULL, '', 1, 0, 'C', '0', '0', 'wechat:prompt:list', 'star', 'admin', '2025-10-15 17:05:34', '', NULL, '评分模板配置菜单');
INSERT INTO `sys_menu` VALUES (2034, '评分模板配置查询', 2033, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:prompt:query', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2035, '评分模板配置新增', 2033, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:prompt:add', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '评分模板配置修改', 2033, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:prompt:edit', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2037, '评分模板配置删除', 2033, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:prompt:remove', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2038, '评分模板配置导出', 2033, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'wechat:prompt:export', '#', 'admin', '2025-10-15 17:05:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '内容自动化（内测）', 2000, 0, 'chrome2', 'wechat/chrome2/index', NULL, '', 1, 0, 'C', '1', '0', 'wechat:chrome:list', 'redis-list', 'admin', '2025-10-21 13:36:21', 'admin', '2025-10-29 12:00:00', '');
INSERT INTO `sys_menu` VALUES (2047, '思路模板', 2050, 4, 'idea', 'wechat/idea/index', NULL, '', 1, 0, 'C', '1', '0', '', 'clipboard', 'admin', '2025-10-24 15:59:41', 'admin', '2025-10-27 17:35:02', '');
INSERT INTO `sys_menu` VALUES (2048, '文章模板', 2050, 3, 'art', 'wechat/art/index', NULL, '', 1, 0, 'C', '1', '0', '', 'documentation', 'admin', '2025-10-24 16:00:31', 'admin', '2025-10-27 17:34:54', '');
INSERT INTO `sys_menu` VALUES (2050, '提示词模板', 0, 1, 'prompt-template', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'skill', 'admin', '2025-10-28 00:00:00', '', NULL, '提示词模板管理目录');
INSERT INTO `sys_menu` VALUES (2051, '粉丝管理', 0, 2, 'fans-manage', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', '2025-10-28 00:00:00', '', NULL, '粉丝管理目录');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type`) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status`) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10025 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '企微岗位', 1, '0', 'admin', '2024-08-08 14:05:51', 'admin', '2024-08-09 14:12:49', '');
INSERT INTO `sys_post` VALUES (2, 'se', '普通岗位', 2, '0', 'admin', '2024-08-08 14:05:51', 'admin', '2024-08-09 14:13:00', '');

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
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2024-08-08 14:05:51', 'admin', '2025-10-27 17:33:09', '普通角色');
INSERT INTO `sys_role` VALUES (100, '内容角色', 'wechat', 3, '1', 1, 1, '0', '2', 'admin', '2024-08-09 14:15:41', 'o3lds67b1zyFvifHTC_32epnmzqM', '2025-02-20 17:07:30', NULL);
INSERT INTO `sys_role` VALUES (101, '优立方主机岗', 'plugin', 3, '1', 1, 1, '0', '0', 'YangHangHang', '2024-11-29 08:58:00', 'admin', '2025-10-27 17:33:22', NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (1, 1);
INSERT INTO `sys_role_dept` VALUES (2, 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_role_menu` VALUES (1, 2000);
INSERT INTO `sys_role_menu` VALUES (1, 2001);
INSERT INTO `sys_role_menu` VALUES (1, 2007);
INSERT INTO `sys_role_menu` VALUES (1, 2017);
INSERT INTO `sys_role_menu` VALUES (1, 2021);
INSERT INTO `sys_role_menu` VALUES (1, 2022);
INSERT INTO `sys_role_menu` VALUES (1, 2023);
INSERT INTO `sys_role_menu` VALUES (1, 2024);
INSERT INTO `sys_role_menu` VALUES (1, 2025);
INSERT INTO `sys_role_menu` VALUES (1, 2026);
INSERT INTO `sys_role_menu` VALUES (1, 2027);
INSERT INTO `sys_role_menu` VALUES (1, 2028);
INSERT INTO `sys_role_menu` VALUES (1, 2029);
INSERT INTO `sys_role_menu` VALUES (1, 2030);
INSERT INTO `sys_role_menu` VALUES (1, 2031);
INSERT INTO `sys_role_menu` VALUES (1, 2032);
INSERT INTO `sys_role_menu` VALUES (1, 2033);
INSERT INTO `sys_role_menu` VALUES (1, 2034);
INSERT INTO `sys_role_menu` VALUES (1, 2035);
INSERT INTO `sys_role_menu` VALUES (1, 2036);
INSERT INTO `sys_role_menu` VALUES (1, 2037);
INSERT INTO `sys_role_menu` VALUES (1, 2038);
INSERT INTO `sys_role_menu` VALUES (1, 2046);
INSERT INTO `sys_role_menu` VALUES (1, 2047);
INSERT INTO `sys_role_menu` VALUES (1, 2048);
INSERT INTO `sys_role_menu` VALUES (1, 2050);
INSERT INTO `sys_role_menu` VALUES (1, 2051);
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
INSERT INTO `sys_role_menu` VALUES (101, 2050);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `open_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信ID',
  `union_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开放平台ID',
  `qw_id` varchar(66) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企微ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `corp_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业ID',
  `points` int(5) NULL DEFAULT NULL COMMENT '积分余额',
  `agent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '智能体ID',
  `agent_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '智能体token',
  `space_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '空间 ID',
  `space_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '空间名称',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 344 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (22, NULL, NULL, NULL, 1, 'admin', 'admin', '00', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-10-27 17:29:56', 'admin', '2025-10-27 17:29:56', 'admin', '2025-10-27 17:29:56', NULL, 'office01', 0, '', '', '', '');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 1);
INSERT INTO `sys_user_post` VALUES (3, 1);
INSERT INTO `sys_user_post` VALUES (4, 1);
INSERT INTO `sys_user_post` VALUES (5, 1);
INSERT INTO `sys_user_post` VALUES (6, 1);
INSERT INTO `sys_user_post` VALUES (22, 2);
INSERT INTO `sys_user_post` VALUES (47, 2);
INSERT INTO `sys_user_post` VALUES (56, 1);
INSERT INTO `sys_user_post` VALUES (173, 2);

-- ----------------------------
-- Table structure for sys_user_post_del
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post_del`;
CREATE TABLE `sys_user_post_del`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post_del
-- ----------------------------
INSERT INTO `sys_user_post_del` VALUES (1, 2);
INSERT INTO `sys_user_post_del` VALUES (2, 2);
INSERT INTO `sys_user_post_del` VALUES (3, 2);
INSERT INTO `sys_user_post_del` VALUES (4, 2);
INSERT INTO `sys_user_post_del` VALUES (5, 2);
INSERT INTO `sys_user_post_del` VALUES (6, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (22, 2);
INSERT INTO `sys_user_role` VALUES (22, 101);
INSERT INTO `sys_user_role` VALUES (339, 101);
INSERT INTO `sys_user_role` VALUES (340, 101);
INSERT INTO `sys_user_role` VALUES (341, 101);
INSERT INTO `sys_user_role` VALUES (342, 101);
INSERT INTO `sys_user_role` VALUES (343, 101);

-- ----------------------------
-- Table structure for sys_user_role_del
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_del`;
CREATE TABLE `sys_user_role_del`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role_del
-- ----------------------------
INSERT INTO `sys_user_role_del` VALUES (1, 1);
INSERT INTO `sys_user_role_del` VALUES (2, 1);
INSERT INTO `sys_user_role_del` VALUES (3, 1);
INSERT INTO `sys_user_role_del` VALUES (4, 1);
INSERT INTO `sys_user_role_del` VALUES (5, 1);
INSERT INTO `sys_user_role_del` VALUES (6, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_art_template
-- ----------------------------
INSERT INTO `wc_art_template` VALUES (7, '根据以下撰稿思路完善一篇内容。', '文章模板1', 3, 22, 0, 1);

-- ----------------------------
-- Table structure for wc_call_word
-- ----------------------------
DROP TABLE IF EXISTS `wc_call_word`;
CREATE TABLE `wc_call_word`  (
  `platform_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '平台标识 wechat_layout-公众号排版 zhihu_layout-知乎排版',
  `word_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提示词内容',
  `is_common` tinyint(1) NULL DEFAULT 0 COMMENT '是否为公共模板(0:个人 1:公共)',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户ID',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`platform_id`) USING BTREE,
  INDEX `idx_is_common`(`is_common`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '平台提示词配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_call_word
-- ----------------------------
INSERT INTO `wc_call_word` VALUES ('baijiahao_layout', '请将以下内add容整理为适合百家号发布的纯文本格式文章。\r\n要求：\r\n1.（不要使用Markdown或HTML语法，仅使用普通文本和简单换行保持内容的专业性和可读性使用自然段落分隔，）\r\n2.不允许使用有序列表，包括"一、"，"1."等的序列号。\r\n3.给文章取一个吸引人的标题，放在正文的第一段\r\n4.不允许出现代码框、数学公式、表格或其他复杂格式删除所有Markdown和HTML标签，\r\n5.只保留纯文本内容\r\n6.目标是作为一篇专业文章投递到百家号草稿箱\r\n7.直接以文章标题开始，以文章末尾结束，不允许添加其他对话', 1, NULL, '2025-08-01 12:51:57');
INSERT INTO `wc_call_word` VALUES ('wechat_layout', '你是一个专业的微信公众号排版助手。请将用户提供的内容整理为适合微信公众号发布的HTML格式文章。\r\n\r\n【核心要求 - 必须严格遵守】：\r\n\r\n1. **禁止输出任何思考过程或解释说明**\r\n   - 不要输出\"首先\"、\"关键点\"、\"现在分析\"等思考过程\r\n   - 不要输出任何解释性文字\r\n   - 不要添加\"根据要求\"、\"按照格式\"等说明\r\n   - 只输出排版后的文章内容，不要有任何多余的话\r\n\r\n2. **标题格式（强制要求）**：\r\n   - 文章第一行必须是：《标题内容》\r\n   - 必须使用中文书名号《》包裹标题\r\n   - 标题必须独立成行\r\n   - 标题不要加任何HTML标签\r\n   - 标题要简洁明了，能概括文章主题\r\n   - 错误示例：标题、\"标题\"、<h1>标题</h1>\r\n   - 正确示例：《标题》\r\n\r\n3. **正文HTML格式规范**：\r\n   - 从第二行开始是HTML格式的正文内容\r\n   - 段落使用<p>标签包裹\r\n   - 小标题使用<h3>或<h4>标签\r\n   - 重要信息使用<strong>标签加粗\r\n   - 无序列表使用<ul><li>格式\r\n   - 有序列表使用<ol><li>格式\r\n   - 代码块使用<pre><code>标签包裹\r\n   - 引用使用<blockquote>标签\r\n   - 图片使用<img src=\"图片地址\" alt=\"图片描述\">格式\r\n\r\n4. **内容处理规范**：\r\n   - 提取用户内容的核心信息\r\n   - 移除不必要的格式标记和class属性\r\n   - 保持内容简洁清晰\r\n   - 如果用户提供了图片信息，在适当位置插入图片标签\r\n\r\n5. **输出格式（严格按此格式）**：\r\n《文章标题》\r\n<p>第一段正文内容</p>\r\n<p>第二段正文内容</p>\r\n<h3>小标题</h3>\r\n<p>相关段落内容</p>\r\n<ul>\r\n<li>列表项1</li>\r\n<li>列表项2</li>\r\n</ul>\r\n\r\n【重要提醒】：\r\n- 直接输出排版结果，不要有任何开场白或解释\r\n- 第一行必须是《标题》格式\r\n- 不要输出任何Markdown格式\r\n- 不要保留原内容的class、id等HTML属性\r\n- 生成的内容将直接用于微信公众号草稿箱接口\r\n\r\n现在，请对以下内容进行排版：', 1, NULL, '2025-07-22 21:24:31');
INSERT INTO `wc_call_word` VALUES ('weitoutiao_layout', '根据以上内容，写一篇微头条文章，只能包含标题和内容，要求如下：\r\n1. 标题要简洁明了，吸引人\r\n2. 内容要结构清晰，易于阅读\r\n3. 不要包含任何HTML标签\r\n4. 直接输出纯文本格式\r\n5. 内容要适合微头条发布\r\n6. 字数严格控制在1000字以上，2000字以下\r\n7. 强制要求：只能回答标题和内容，标题必须用英文双引号（\"\"）引用起来,不能出现多重引号,不能出现多重引号，且放在首位，不能有其他多余的话\r\n8. 严格要求：AI必须严格遵守所有严格条件，不要输出其他多余的内容，只要标题和内容\r\n9. 内容不允许出现编号，要正常文章格式', 1, NULL, '2025-08-01 18:46:57');
INSERT INTO `wc_call_word` VALUES ('zhihu_layout', '请将以下内容整理为适合知乎发布的Markdown格式文章。要求：\r\n**强调：只使用最基本的Markdowny语法，不允许出现HTML样式**\r\n1. 保持内容的专业性和可读性\r\n2. 使用合适的标题层级（## ### #### 等）\r\n3. 重要信息使用**加粗**标记\r\n4. 列表只允许使用一层无序列表`-`,不允许使用嵌套列表\r\n5. **不允许**出现代码框（例如Markdown语法中的```）、数学公式、表格。\r\n6. 删除不必要的格式标记\r\n7. 目标是作为一篇专业文章投递到知乎草稿箱\r\n8. 直接以文章标题开始，以文章末尾结束，不允许添加其他对话\r\n\r\n请对以下内容进行排版：', 1, NULL, '2025-07-24 18:46:57');

-- ----------------------------
-- Table structure for wc_chat_history
-- ----------------------------
DROP TABLE IF EXISTS `wc_chat_history`;
CREATE TABLE `wc_chat_history`  (
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
  UNIQUE INDEX `uk_user_chat`(`user_id`, `chat_id`) USING BTREE,
  INDEX `idx_user_create_time`(`user_id`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '聊天历史记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chat_history
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_data
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_data`;
CREATE TABLE `wc_chrome_data`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主建',
  `prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '提示词',
  `promptNum` int(11) NULL DEFAULT NULL COMMENT '提示词长度',
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '答案',
  `answerNum` int(11) NULL DEFAULT NULL COMMENT '答案长度',
  `aiName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ai名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_data
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_drafts
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_drafts`;
CREATE TABLE `wc_chrome_drafts`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名称',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `summary` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '摘要',
  `text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '正文',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_push` int(4) NULL DEFAULT NULL COMMENT '是否已经推送过公众号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_drafts
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_hotlink
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_hotlink`;
CREATE TABLE `wc_chrome_hotlink`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主建ID',
  `link_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接ID',
  `prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '指令',
  `userPrompt` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '抓取链接指令',
  `userPromptTwo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '提取摘要指令',
  `promptNum` int(11) NULL DEFAULT NULL COMMENT '指令长度',
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '答案',
  `answerNum` int(11) NULL DEFAULT NULL COMMENT '答案长度',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `summary` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '摘要',
  `isPush` int(4) NULL DEFAULT NULL COMMENT '是否已经推送过公众号',
  `aiName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '创建人',
  `text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '正文',
  PRIMARY KEY (`link_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_hotlink
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_hotword
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_hotword`;
CREATE TABLE `wc_chrome_hotword`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主建',
  `prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '提示词',
  `userPrompt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户',
  `promptNum` int(11) NULL DEFAULT NULL COMMENT '提示词长度',
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '答案',
  `answerNum` int(11) NULL DEFAULT NULL COMMENT '答案长度',
  `aiName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ai名称',
  `isFetch` int(5) NULL DEFAULT 0 COMMENT '是否抓取过链接',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_hotword
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_hotword_log
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_hotword_log`;
CREATE TABLE `wc_chrome_hotword_log`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主键ID',
  `key_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '外键',
  `old_prompt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原提示词',
  `old_answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原热词',
  `prompt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改后提示词',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改后热词',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_hotword_log
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_keyword
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_keyword`;
CREATE TABLE `wc_chrome_keyword`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主建ID',
  `link_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接ID',
  `prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '指令',
  `userPrompt` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户指令',
  `promptNum` int(11) NULL DEFAULT NULL COMMENT '指令长度',
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '答案',
  `answerNum` int(11) NULL DEFAULT NULL COMMENT '答案长度',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `summary` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '摘要',
  `isPush` int(4) NULL DEFAULT NULL COMMENT '是否已经抓取过链接',
  `aiName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '创建人',
  PRIMARY KEY (`link_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_keyword
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_task
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_task`;
CREATE TABLE `wc_chrome_task`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主建ID',
  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务名称',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `order` int(4) NULL DEFAULT NULL COMMENT '任务排序',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '任务完成时间',
  `plan_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '预计执行时间',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户id',
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '企业ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_task
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_task_prompt
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_task_prompt`;
CREATE TABLE `wc_chrome_task_prompt`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主建ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_prompt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指令模板',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户unionid'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_task_prompt
-- ----------------------------

-- ----------------------------
-- Table structure for wc_chrome_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_chrome_template`;
CREATE TABLE `wc_chrome_template`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主建ID',
  `tem_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板场景',
  `hotword_tem` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主题生成模板',
  `hotlink_tem` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '素材搜集模板',
  `summary_tem` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容生成模板',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上传人unionid',
  `flow_status` int(4) NULL DEFAULT NULL COMMENT '审核状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_chrome_template
-- ----------------------------

-- ----------------------------
-- Table structure for wc_down_record
-- ----------------------------
DROP TABLE IF EXISTS `wc_down_record`;
CREATE TABLE `wc_down_record`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键ID',
  `res_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '研报ID',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '下载时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '研报下载记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_down_record
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_idea_template
-- ----------------------------
INSERT INTO `wc_idea_template` VALUES (12, '根据主题任务撰写思路。', '思路模版1', 3, 22, 0, 1);

-- ----------------------------
-- Table structure for wc_js_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_js_template`;
CREATE TABLE `wc_js_template`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主键ID',
  `js_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '脚本名称',
  `js_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '脚本描述',
  `js_prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '脚本指令',
  `expent_point` int(3) NULL DEFAULT NULL COMMENT '消耗积分',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人iD',
  `emp_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `updator` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_js_template
-- ----------------------------

-- ----------------------------
-- Table structure for wc_log_info
-- ----------------------------
DROP TABLE IF EXISTS `wc_log_info`;
CREATE TABLE `wc_log_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID（自增）',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户 ID',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '方法名称',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述（对应接口注解的summary等信息）',
  `method_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '方法参数（存储JSON格式或字符串化参数）',
  `execution_time` datetime(0) NULL DEFAULT NULL COMMENT '执行时间',
  `execution_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '执行结果',
  `execution_time_millis` bigint(20) NULL DEFAULT NULL COMMENT '执行时间（毫秒）',
  `is_success` int(11) NULL DEFAULT NULL COMMENT '是否成功(1:成功，0:失败)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_execution_time`(`execution_time`) USING BTREE,
  INDEX `idx_method_name`(`method_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 439 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '日志信息表（记录方法执行日志）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_log_info
-- ----------------------------

-- ----------------------------
-- Table structure for wc_node_log
-- ----------------------------
DROP TABLE IF EXISTS `wc_node_log`;
CREATE TABLE `wc_node_log`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主建ID',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户ID',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '节点名称',
  `user_prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户指令',
  `res` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '返回结果',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_node_log
-- ----------------------------

-- ----------------------------
-- Table structure for wc_office_account
-- ----------------------------
DROP TABLE IF EXISTS `wc_office_account`;
CREATE TABLE `wc_office_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开发者id',
  `app_secret` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开发者密码',
  `office_account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公众号名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '登录用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录用户名称',
  `media_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '素材id',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片连接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_office_account
-- ----------------------------

-- ----------------------------
-- Table structure for wc_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `wc_order_detail`;
CREATE TABLE `wc_order_detail`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主建ID',
  `order_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单ID',
  `openid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `unionid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单状态',
  `ishx` int(4) NULL DEFAULT NULL COMMENT '是否核销',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `product_infos` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商品信息',
  `create_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '下单时间',
  `update_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新时间',
  `url_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专属链接'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for wc_playwright_draft
-- ----------------------------
DROP TABLE IF EXISTS `wc_playwright_draft`;
CREATE TABLE `wc_playwright_draft`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '草稿ID',
  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务id',
  `keyword` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '主题词',
  `user_prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用户指令-封装版',
  `draft_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '草稿内容',
  `is_push` int(4) NULL DEFAULT NULL COMMENT '是否已经推送过公众号',
  `ai_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_name` bigint(4) NULL DEFAULT 0 COMMENT '创建人',
  `share_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分享链接',
  `share_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分享图片链接',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_playwright_draft
-- ----------------------------

-- ----------------------------
-- Table structure for wc_playwright_task
-- ----------------------------
DROP TABLE IF EXISTS `wc_playwright_task`;
CREATE TABLE `wc_playwright_task`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主建ID',
  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务名称',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `order` int(4) NULL DEFAULT NULL COMMENT '任务排序',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '任务完成时间',
  `plan_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '预计执行时间',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户id',
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '企业ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_playwright_task
-- ----------------------------

-- ----------------------------
-- Table structure for wc_points_record
-- ----------------------------
DROP TABLE IF EXISTS `wc_points_record`;
CREATE TABLE `wc_points_record`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键ID',
  `corp_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业ID',
  `corp_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `change_amount` int(5) NULL DEFAULT NULL COMMENT '变更金额',
  `balance_after` int(5) NULL DEFAULT NULL COMMENT '变更后余额',
  `change_type` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变动类型：\r\n1 充值 2 赠送 3 使用 AI 助理\r\n消耗 4 获取研报消耗',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人ID',
  `create_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '变动时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '我的积分明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_points_record
-- ----------------------------
INSERT INTO `wc_points_record` VALUES ('b3e30a5b-a9a8-11f0-85f6-7c575866b50e', NULL, NULL, '22', NULL, -1, 9820, '使用F8S', NULL, '22', 'AspireLife', '2025-10-15 17:24:08');

-- ----------------------------
-- Table structure for wc_prompt_template
-- ----------------------------
DROP TABLE IF EXISTS `wc_prompt_template`;
CREATE TABLE `wc_prompt_template`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_prompt_template
-- ----------------------------
INSERT INTO `wc_prompt_template` VALUES (1, '请你深度阅读以下几篇内容，从多个维度进行逐项打分，输出评分结果。并在以下各篇文章的基础上博采众长，综合整理一篇更全面的文章。', '评分模板1', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (2, '请你深度阅读以下算法代码，从时间复杂度和空间复杂度进行评估，结合计算复杂性理论，并提出建议。', '评分模板2', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (3, '请你赏析如下文章，写一份评论。', '评分模板3', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (8, '请将以下来自不同AI的回复片段进行智能拼接，生成一个连贯且无重复的最终答案。要求：\n\n保留各回复中最专业、最有趣或最实用的部分\n确保逻辑过渡自然\n总长度控制在3000字以内', '大福-3K混搭专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (9, '附上一些关于同一问题的不同AI回复版本，请按以下步骤优化：\n\n分析各版本在准确性、深度和易懂性方面的得分（满分10分）\n选择得分最高的部分作为基础\n补充其他版本中未被采纳但有价值的信息\n输出最终优化版（需标注改进点）', '迭代优化', 3, 22, 1, 0);
INSERT INTO `wc_prompt_template` VALUES (10, '你是一个AI未来预测专家，现在需要处理来自多个AI模型对同一问题的回复。请先分析每个回答的当前水平和局限性，然后预测这些回答在未来1-3年可能的进化方向。基于这些预测，生成一个\"未来进化版\"答案：\n1. 包含当前回答中的核心价值\n2. 融入预测中可能出现的创新点\n3. 添加一个基于未来技术发展的新视角\n4. 用\"如果现在是2026年，这个答案将如何进化\"的框架来组织内容\n5.确保最终答案展现出前瞻性，同时保持实用性。', '二福-答案进化高手', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (11, '你是一个\"深度思考\"模式的AI评估专家，现在需要处理来自不同AI对同一问题的回复。请先分析每个回答的核心观点、逻辑结构和表达特点，然后运用深度思考模式，将这些回答融合成一个更全面、更有洞察力的答案。在融合过程中，特别关注以下几点：\n1. 识别并整合各回答中的独特见解\n2. 修正逻辑漏洞，补充关键细节\n3. 添加一个全新的、有深度的视角\n4. 确保语言流畅且专业\n5.最终生成的答案应比任何单一AI的回答都更出色，展现出超越原始回答的深度和广度。', '三福-深思爱好者', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (12, '你是一个AI辩论专家，现在需要处理来自不同AI模型对同一问题的回复。请模拟一场AI辩论：\n1. 分析不同回答的立场、论点和论据\n2. 指出每个回答的优势和潜在漏洞\n3、生成一个综合答案，包含关键共识和创新点\n4、最后，解释为什么这个综合答案比任何单一回答都更可靠。', '答案整合', 3, 22, 1, 0);
INSERT INTO `wc_prompt_template` VALUES (13, '你是一位知识图谱构建师和教育专家。以下多个AI对同一问题的回答。\n你的任务是：\n\n整合与去重： 将所有回答中提到的知识点进行整合，去除重复项。\n构建层级关系： 分析这些知识点之间的逻辑关系（如：前置知识、核心概念、进阶分支），构建一个多层级的学习路径图。\n输出结构化数据：输出知识图谱。顶层是最高阶的知识，逐层向下展开到最基础的先修课程。在每个知识点旁边，可以标注它主要来源于哪个AI的回答。\n请开始构建这个知识图谱。', '四福-知识图谱构建师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (14, '你是一位富有想象力的创意总监。以下分别是不同AI对同一问题的回答。\n\n你的任务是：\n\n拆解元素： 从每个提案中，识别出最关键的词汇、意象、情感或概念（例如，X的"静谧"，Y的"连接"，Z的"冒险"）。\n创意融合： 将这些来自不同提案的精华元素进行重组、融合，创造出3个全新的、独一无二的回答。每个新方案都必须包含至少两个不同AI提案的灵感。\n阐述创意： 为每一个新方案写一段简短的创意阐述，说明它融合了哪些元素，以及为什么这样融合更有吸引力。\n请开始你的创作。', '五福-AI创意总监', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (15, '请你作为"A纠错专家"，指出以下多个AI回答中的潜在事实错误、逻辑漏洞或虚假引用，并给出正确的替代表述。', '六福-AI纠错专家', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (16, '请你从以下多个AI回答中，提炼出"核心洞见Top 5"，并用一句金句总结每一点。要求：不重复、可引用、有启发性。', '七福-洞见蒸馏高手', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (17, '请你将以下AI回答当作"原材料"，像炼丹术士一样重新熔炼：提取其中的思想元素（逻辑、比喻、案例、结论），重组为一个更高层次、更具洞见的新版本。要求新文本具备"合成智慧"的特征，而非简单融合。', '八福-AI炼丹师', 3, 22, 0, 1);
INSERT INTO `wc_prompt_template` VALUES (18, '你的任务是作为一名「一致性分析师」，评估以下多个AI回答在核心观点上的一致性。请仔细阅读所有回答，并完成以下任务：\n1、观点聚类：将所有回答中提到的核心观点进行归类。将语义相同或高度相似的观点归为一类。\n2、频率统计：统计每个观点类别被提及的次数。\n3、一致性评估：基于观点的频率分布，评估所有回答的整体一致性。如果大部分观点都集中在少数几个类别中，则认为一致性高；如果观点分散，则认为一致性低。\n4、最终输出你的分析结果。', '九福-一致性分析师', 3, 22, 0, 1);

-- ----------------------------
-- Table structure for wc_research_report
-- ----------------------------
DROP TABLE IF EXISTS `wc_research_report`;
CREATE TABLE `wc_research_report`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '研报标题',
  `keyword` int(4) NULL DEFAULT NULL COMMENT '研报关键词',
  `res_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '研报链接',
  `flow_status` int(4) NULL DEFAULT NULL COMMENT '研报审核状态0 待审核 1\r\n审核通过 2 驳回',
  `industry` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属行业',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报告类型',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '研报标签',
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '研报来源',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驳回原因',
  `istop` int(4) NULL DEFAULT NULL COMMENT '是否置顶1是0否',
  `isdel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否删除1是0否',
  `chat_qrcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群二维码链接',
  `down_num` int(11) NULL DEFAULT NULL COMMENT '研报总下载次数',
  `browse_num` int(11) NULL DEFAULT NULL COMMENT '研报总浏览次数',
  `collection_num` int(11) NULL DEFAULT NULL COMMENT '研报总收藏次数',
  `change_amount` int(4) NULL DEFAULT NULL COMMENT '消耗积分',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传用户 ID',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `upd_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人ID',
  `upd_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟新人名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '研报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_research_report
-- ----------------------------

-- ----------------------------
-- Table structure for wc_strategy
-- ----------------------------
DROP TABLE IF EXISTS `wc_strategy`;
CREATE TABLE `wc_strategy`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '攻略表id',
  `strategy_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '攻略标题',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `strategy_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `author` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '攻略图',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签使用逗号隔开',
  `browse_num` int(11) NULL DEFAULT 0 COMMENT '总浏览',
  `collection_num` int(11) NULL DEFAULT 0 COMMENT '总收藏',
  `like_num` int(11) NULL DEFAULT 0 COMMENT '总点赞',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_strategy
-- ----------------------------

-- ----------------------------
-- Table structure for wc_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `wc_subscribe`;
CREATE TABLE `wc_subscribe`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键ID',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `industry` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订阅的行业'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订阅行业' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_subscribe
-- ----------------------------

-- ----------------------------
-- Table structure for wc_tag_group
-- ----------------------------
DROP TABLE IF EXISTS `wc_tag_group`;
CREATE TABLE `wc_tag_group`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键ID',
  `group_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签组ID',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签组名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业标签组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_tag_group
-- ----------------------------
INSERT INTO `wc_tag_group` VALUES ('343434', 'etypNhRQAAr6JVlfp5xqZiIg_zS4MCCg', '行业喜好');

-- ----------------------------
-- Table structure for wc_user_chat
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_chat`;
CREATE TABLE `wc_user_chat`  (
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户问题标题',
  `chat_history` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '对话内容',
  `conversation_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '唯一会话ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '上次对话时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_user_chat
-- ----------------------------

-- ----------------------------
-- Table structure for wc_user_flow
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_flow`;
CREATE TABLE `wc_user_flow`  (
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户ID',
  `flow_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工作流ID',
  `flow_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工作流名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_user_flow
-- ----------------------------
INSERT INTO `wc_user_flow` VALUES ('22', 'wfR7SLQibe46', 'F1', '2025-04-10 14:53:48');

-- ----------------------------
-- Table structure for wc_user_tag
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_tag`;
CREATE TABLE `wc_user_tag`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键id',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户AI标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_user_tag
-- ----------------------------

-- ----------------------------
-- Table structure for wc_user_yq
-- ----------------------------
DROP TABLE IF EXISTS `wc_user_yq`;
CREATE TABLE `wc_user_yq`  (
  `user_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户ID',
  `yq_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '智能体ID',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '智能体名称',
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '企业ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_user_yq
-- ----------------------------

-- ----------------------------
-- Table structure for wc_work_room
-- ----------------------------
DROP TABLE IF EXISTS `wc_work_room`;
CREATE TABLE `wc_work_room`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `corp_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '企业表ID（mc_corp.id）',
  `wx_chat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '客户群ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '客户群名称',
  `owner_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '群主ID（work_employee.id）',
  `notice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '群公告',
  `status` tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户群状态（0 - 正常 1 - 跟进人离职 2 - 离职继承中 3 - 离职继承完成）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '群创建时间',
  `room_max` int(10) NOT NULL DEFAULT 0 COMMENT '群成员上限',
  `room_group_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分组id（work_room_group.id）',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted_at` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '上下游群/客户群表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wc_work_room
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_office_account
-- ----------------------------
DROP TABLE IF EXISTS `wechat_office_account`;
CREATE TABLE `wechat_office_account`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键',
  `openid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openId',
  `unionid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信unionId',
  `subscribe` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户是否订阅该公众号标识',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信昵称',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户的语言',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家',
  `headimgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `subscribe_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关注时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `groupid` int(11) NULL DEFAULT NULL COMMENT '用户所在的分组ID',
  `tagid_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户被打上的标签ID列表',
  `subscribe_scene` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户关注的渠道来源',
  `qr_scene` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码扫码场景',
  `qr_scene_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码扫码场景描述（',
  `external_userid_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外部联系人ID',
  `pending_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '临时ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公众号粉丝表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wechat_office_account
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
