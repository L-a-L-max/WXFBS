-- =============================================
-- 日更助手功能完整初始化脚本
-- 包含：表结构 + 菜单配置
-- =============================================

USE ucube;

-- =============================================
-- 第一部分：创建数据表
-- =============================================

-- =============================================
-- 1. 创建日更助手文章表
-- =============================================
DROP TABLE IF EXISTS `daily_article`;
CREATE TABLE `daily_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `article_title` varchar(500) NOT NULL COMMENT '原始文章标题',
  `optimized_content` longtext COMMENT '优化后的文章内容（来自腾讯元器智能体）',
  `model1_content` longtext COMMENT '大模型1未优化的文章内容',
  `model2_content` longtext COMMENT '大模型2未优化的文章内容',
  `model3_content` longtext COMMENT '大模型3未优化的文章内容',
  `model1_name` varchar(100) DEFAULT NULL COMMENT '大模型1名称',
  `model2_name` varchar(100) DEFAULT NULL COMMENT '大模型2名称',
  `model3_name` varchar(100) DEFAULT NULL COMMENT '大模型3名称',
  `agent_task_id` varchar(200) DEFAULT NULL COMMENT '腾讯元器智能体任务ID',
  `process_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '处理状态：0-处理中，1-已完成，2-失败',
  `error_message` varchar(1000) DEFAULT NULL COMMENT '错误信息',
  `selected_models` varchar(50) DEFAULT '1,2,3' COMMENT '已选择的模型，格式如"1,2,3"',
  `publish_count` int(11) DEFAULT 0 COMMENT '发布次数',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
  KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引',
  KEY `idx_process_status` (`process_status`) USING BTREE COMMENT '处理状态索引',
  KEY `idx_agent_task_id` (`agent_task_id`) USING BTREE COMMENT '智能体任务ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日更助手文章表';

-- =============================================
-- 2. 创建腾讯元器智能体配置表
-- =============================================
DROP TABLE IF EXISTS `yuanqi_agent_config`;
CREATE TABLE `yuanqi_agent_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `agent_id` varchar(200) NOT NULL COMMENT '腾讯元器智能体ID',
  `agent_name` varchar(100) DEFAULT NULL COMMENT '智能体名称',
  `api_key` varchar(500) DEFAULT NULL COMMENT 'API密钥（加密存储）',
  `api_endpoint` varchar(500) DEFAULT NULL COMMENT 'API端点URL',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
  `config_json` json DEFAULT NULL COMMENT '其他配置（JSON格式）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
  KEY `idx_agent_id` (`agent_id`) USING BTREE COMMENT '智能体ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='腾讯元器智能体配置表';

-- =============================================
-- 第二部分：配置系统菜单
-- =============================================

-- 插入日更助手菜单
-- 注意：menu_id 需要根据实际系统中最大的menu_id来设置，这里假设从5000开始

-- 1. 插入工具菜单下的日更助手主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
(5000, '日更助手', 3, 4, 'daily-assistant', 'tool/dailyAssistant', 1, 0, 'C', '0', '0', 'tool:daily:view', 'edit', 'admin', sysdate(), '', NULL, '日更助手功能菜单');

-- 2. 插入日更助手相关权限按钮
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
(5001, '文章查询', 5000, 1, '#', '', 1, 0, 'F', '0', '0', 'tool:daily:query', '#', 'admin', sysdate(), '', NULL, ''),
(5002, '文章新增', 5000, 2, '#', '', 1, 0, 'F', '0', '0', 'tool:daily:add', '#', 'admin', sysdate(), '', NULL, ''),
(5003, '文章删除', 5000, 3, '#', '', 1, 0, 'F', '0', '0', 'tool:daily:remove', '#', 'admin', sysdate(), '', NULL, ''),
(5004, '智能体配置', 5000, 4, '#', '', 1, 0, 'F', '0', '0', 'tool:daily:config', '#', 'admin', sysdate(), '', NULL, '');

-- =============================================
-- 使用说明
-- =============================================
-- 1. parent_id = 3 表示挂载到"系统工具"菜单下，如果你的工具菜单ID不是3，需要修改
-- 2. menu_id 从5000开始，确保不与现有菜单ID冲突
-- 3. path = 'daily-assistant' 对应前端路由路径
-- 4. component = 'tool/dailyAssistant' 对应前端组件路径（相对于views目录）
-- 5. perms 为权限标识，用于后端权限验证
-- 6. 如果需要所有用户都能访问，可以在角色菜单关联表中添加相应记录

-- 可选：为管理员角色分配日更助手菜单权限（假设管理员角色ID为1）
-- INSERT INTO sys_role_menu (role_id, menu_id)
-- VALUES
-- (1, 5000),
-- (1, 5001),
-- (1, 5002),
-- (1, 5003),
-- (1, 5004);
