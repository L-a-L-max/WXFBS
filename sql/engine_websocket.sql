-- ========================================
-- Engine WebSocket 通信相关数据库表
-- 版本: 1.2.0
-- 创建日期: 2025-12-15
-- 更新日期: 2025-12-15
-- ========================================
-- 设计说明：
-- 1. ws_connection_log：连接时插入，断开时更新统计信息
-- 2. ws_engine_online：实时在线状态，下线时物理删除
-- 3. ws_host_whitelist/ws_ip_blacklist：配置表，逻辑删除
-- ========================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 主机白名单表
-- 说明：只有在此表中且状态启用的主机ID才允许连接
-- 写入时机：管理员手动添加
-- ----------------------------
DROP TABLE IF EXISTS `ws_host_whitelist`;
CREATE TABLE `ws_host_whitelist` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `host_id` varchar(50) NOT NULL COMMENT '主机ID（用户申请后由管理员分配）',
    `host_name` varchar(100) DEFAULT NULL COMMENT '主机名称/描述',
    `owner_name` varchar(50) DEFAULT NULL COMMENT '负责人姓名',
    `owner_contact` varchar(100) DEFAULT NULL COMMENT '负责人联系方式',
    `is_team` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否团队主机：0-个人，1-团队（仅用于统计分类）',
    `team_name` varchar(100) DEFAULT NULL COMMENT '团队名称（is_team=1时填写）',
    `allowed_ips` varchar(500) DEFAULT NULL COMMENT '允许的IP地址列表（逗号分隔，为空表示不限制）',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `expire_time` datetime DEFAULT NULL COMMENT '过期时间（为空表示永不过期）',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志：0-正常，1-已删除',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_host_id` (`host_id`),
    INDEX `idx_is_team` (`is_team`),
    INDEX `idx_status` (`status`),
    INDEX `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='WebSocket主机白名单表';

-- 插入示例数据
INSERT INTO `ws_host_whitelist` (`host_id`, `host_name`, `owner_name`, `is_team`, `team_name`, `status`, `remark`) VALUES
('engine-001', '默认Engine节点', '管理员', 0, NULL, 1, '默认配置的Engine节点，对应application.yml中的host-id'),
('engine-dev-001', '开发测试节点1', '张三', 0, NULL, 1, '开发环境测试用'),
('engine-prod-001', '生产节点-运维组', '运维组', 1, '运维团队', 1, '生产环境主节点');

-- ----------------------------
-- 2. IP黑名单表
-- 说明：被封禁的IP地址，拒绝连接
-- 写入时机：管理员手动添加 或 系统自动封禁
-- ----------------------------
DROP TABLE IF EXISTS `ws_ip_blacklist`;
CREATE TABLE `ws_ip_blacklist` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `ip_address` varchar(50) NOT NULL COMMENT 'IP地址',
    `block_reason` varchar(255) DEFAULT NULL COMMENT '封禁原因',
    `block_type` tinyint DEFAULT 1 COMMENT '封禁类型：1-临时封禁（有过期时间），2-永久封禁',
    `expire_time` datetime DEFAULT NULL COMMENT '解封时间（block_type=1时有效，为空表示永久封禁）',
    `hit_count` int DEFAULT 0 COMMENT '命中次数（该IP尝试连接被拒绝的次数）',
    `last_hit_time` datetime DEFAULT NULL COMMENT '最后命中时间',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：1-生效，0-已解除',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志：0-正常，1-已删除',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_ip_address` (`ip_address`),
    INDEX `idx_status` (`status`),
    INDEX `idx_expire_time` (`expire_time`),
    INDEX `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='WebSocket IP黑名单表';

-- ----------------------------
-- 3. WebSocket连接记录表
-- 说明：记录所有连接的完整生命周期
-- 写入时机：
--   - 连接建立时：插入记录（status=0 连接中）
--   - 注册成功时：更新记录（status=1 已注册）
--   - 连接拒绝时：插入记录（status=4/5/6）
--   - 连接断开时：更新记录（status=2/3/7 + 统计信息）
-- 状态说明：
--   - 0-连接中：TCP握手成功，等待Engine发送注册消息
--   - 1-已注册：Engine发送注册消息并验证通过，正常工作中
--   - 2-正常断开：Engine主动断开或正常关闭
--   - 3-异常断开：网络异常、心跳超时等非正常断开
--   - 4-被拒绝（白名单）：主机ID不在白名单、已禁用、已过期
--   - 5-被拒绝（黑名单）：IP在黑名单中
--   - 6-被拒绝（重复连接）：该主机ID已有活跃连接
--   - 7-被管理员断开：管理员主动断开连接
-- ----------------------------
DROP TABLE IF EXISTS `ws_connection_log`;
CREATE TABLE `ws_connection_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `session_id` varchar(64) NOT NULL COMMENT 'WebSocket会话ID',
    `host_id` varchar(50) DEFAULT NULL COMMENT '主机ID（客户端声称的，注册前可能为空）',
    `device_id` varchar(128) DEFAULT NULL COMMENT '设备指纹ID（基于硬件信息生成，注册时上报）',
    `engine_version` varchar(20) DEFAULT NULL COMMENT 'Engine版本号（注册时上报）',
    `remote_ip` varchar(50) NOT NULL COMMENT '客户端IP地址',
    `remote_port` int DEFAULT NULL COMMENT '客户端端口',
    `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI路径',
    `os_name` varchar(50) DEFAULT NULL COMMENT '操作系统名称（注册时上报）',
    `os_version` varchar(50) DEFAULT NULL COMMENT '操作系统版本（注册时上报）',
    `java_version` varchar(30) DEFAULT NULL COMMENT 'Java版本（注册时上报）',
    `hostname` varchar(100) DEFAULT NULL COMMENT '客户端主机名（注册时上报）',
    `mac_address` varchar(50) DEFAULT NULL COMMENT 'MAC地址（注册时上报）',
    `connect_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '连接时间（TCP握手成功时间）',
    `register_time` datetime DEFAULT NULL COMMENT '注册成功时间（验证通过时间）',
    `disconnect_time` datetime DEFAULT NULL COMMENT '断开时间',
    `duration_seconds` bigint DEFAULT NULL COMMENT '连接持续时间（秒，断开时计算）',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-连接中，1-已注册，2-正常断开，3-异常断开，4-被拒绝（白名单），5-被拒绝（黑名单），6-被拒绝（重复连接），7-被管理员断开，8-主节点重启导致断开',
    `reject_reason` varchar(255) DEFAULT NULL COMMENT '拒绝/断开原因',
    `error_code` varchar(10) DEFAULT NULL COMMENT '错误码（E1001-E9999：E1xxx认证错误，E2xxx授权错误，E3xxx连接错误，E4xxx系统错误）',
    `close_code` int DEFAULT NULL COMMENT 'WebSocket关闭状态码',
    `close_reason` varchar(255) DEFAULT NULL COMMENT '关闭原因',
    `message_sent` bigint DEFAULT 0 COMMENT '发送消息数（断开时统计）',
    `message_received` bigint DEFAULT 0 COMMENT '接收消息数（断开时统计）',
    `heartbeat_count` int DEFAULT 0 COMMENT '心跳次数（断开时统计）',
    `error_count` int DEFAULT 0 COMMENT '错误次数（断开时统计）',
    `last_error` varchar(500) DEFAULT NULL COMMENT '最后一次错误信息',
    `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志：0-正常，1-已删除',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_session_id` (`session_id`),
    INDEX `idx_host_id` (`host_id`),
    INDEX `idx_device_id` (`device_id`),
    INDEX `idx_remote_ip` (`remote_ip`),
    INDEX `idx_connect_time` (`connect_time`),
    INDEX `idx_status` (`status`),
    INDEX `idx_error_code` (`error_code`),
    INDEX `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='WebSocket连接记录表';

-- ----------------------------
-- 说明：在线Engine状态不使用数据库表存储
-- 原因：
--   1. 实时状态存储在内存中（EngineSessionManager），查询更快
--   2. 主节点重启后，所有连接都会断开，Engine会重新注册
--   3. 历史记录已在 ws_connection_log 表中保存
--   4. 通过 /ws/admin/engines 接口查询实时在线状态
-- ----------------------------

-- ----------------------------
-- 4. 创建视图：连接统计视图
-- ----------------------------
DROP VIEW IF EXISTS `v_ws_connection_stats`;
CREATE VIEW `v_ws_connection_stats` AS
SELECT 
    DATE(connect_time) AS stat_date,
    COUNT(*) AS total_connections,
    SUM(CASE WHEN status IN (1, 2) THEN 1 ELSE 0 END) AS success_connections,
    SUM(CASE WHEN status IN (4, 5, 6) THEN 1 ELSE 0 END) AS rejected_connections,
    SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) AS abnormal_disconnections,
    SUM(CASE WHEN status = 7 THEN 1 ELSE 0 END) AS admin_disconnections,
    COUNT(DISTINCT remote_ip) AS unique_ips,
    COUNT(DISTINCT host_id) AS unique_hosts,
    COUNT(DISTINCT device_id) AS unique_devices
FROM `ws_connection_log`
WHERE del_flag = 0
GROUP BY DATE(connect_time);

-- ----------------------------
-- 6. 创建视图：可疑连接视图（同一设备使用不同主机ID）
-- ----------------------------
DROP VIEW IF EXISTS `v_ws_suspicious_connections`;
CREATE VIEW `v_ws_suspicious_connections` AS
SELECT 
    device_id,
    GROUP_CONCAT(DISTINCT host_id) AS used_host_ids,
    COUNT(DISTINCT host_id) AS host_id_count,
    GROUP_CONCAT(DISTINCT remote_ip) AS used_ips,
    COUNT(*) AS connection_count,
    MAX(connect_time) AS last_connect_time
FROM `ws_connection_log`
WHERE device_id IS NOT NULL AND del_flag = 0
GROUP BY device_id
HAVING COUNT(DISTINCT host_id) > 1;

-- ----------------------------
-- 6. 创建视图：团队主机统计视图
-- 说明：在线数量需要通过接口查询内存数据
-- ----------------------------
DROP VIEW IF EXISTS `v_ws_team_stats`;
CREATE VIEW `v_ws_team_stats` AS
SELECT 
    COALESCE(w.team_name, '个人') AS team_name,
    w.is_team,
    COUNT(DISTINCT w.host_id) AS host_count,
    SUM(CASE WHEN w.status = 1 THEN 1 ELSE 0 END) AS enabled_count,
    SUM(CASE WHEN w.status = 0 THEN 1 ELSE 0 END) AS disabled_count
FROM `ws_host_whitelist` w
WHERE w.del_flag = 0
GROUP BY w.team_name, w.is_team;

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 主机管理模块 - 菜单权限配置
-- 日期: 2025-12-23
-- 说明: 添加主机管理一级菜单及其子菜单和按钮权限
-- ========================================

-- 一级菜单：主机管理（menu_id: 7，只有管理员和只读管理员可见）
INSERT INTO sys_menu VALUES(
    '7',                          -- menu_id
    '主机管理',                    -- menu_name
    '0',                          -- parent_id（顶级菜单）
    '7',                          -- order_num
    'host',                       -- path
    NULL,                         -- component
    '',                           -- query
    '',                           -- route_name
    1,                            -- is_frame
    0,                            -- is_cache
    'M',                          -- menu_type（M=目录）
    '0',                          -- visible
    '0',                          -- status
    '',                           -- perms
    'server',                     -- icon
    'admin',                      -- create_by
    sysdate(),                    -- create_time
    '',                           -- update_by
    NULL,                         -- update_time
    '主机管理目录'                 -- remark
);

-- 二级菜单：主机ID白名单管理（menu_id: 123）
INSERT INTO sys_menu VALUES(
    '123',                        -- menu_id
    '主机ID白名单',                -- menu_name
    '7',                          -- parent_id（主机管理）
    '1',                          -- order_num
    'whitelist',                  -- path
    'business/host/whitelist/index',  -- component
    '',                           -- query
    '',                           -- route_name
    1,                            -- is_frame
    0,                            -- is_cache
    'C',                          -- menu_type（C=菜单）
    '0',                          -- visible
    '0',                          -- status
    'business:host:whitelist:view',  -- perms
    'list',                       -- icon
    'admin',                      -- create_by
    sysdate(),                    -- create_time
    '',                           -- update_by
    NULL,                         -- update_time
    '主机ID白名单管理菜单'         -- remark
);

-- 二级菜单：IP黑名单管理（menu_id: 124）
INSERT INTO sys_menu VALUES(
    '124',                        -- menu_id
    'IP黑名单',                    -- menu_name
    '7',                          -- parent_id（主机管理）
    '2',                          -- order_num
    'blacklist',                  -- path
    'business/host/blacklist/index',  -- component
    '',                           -- query
    '',                           -- route_name
    1,                            -- is_frame
    0,                            -- is_cache
    'C',                          -- menu_type（C=菜单）
    '0',                          -- visible
    '0',                          -- status
    'business:host:blacklist:view',  -- perms
    'lock',                       -- icon
    'admin',                      -- create_by
    sysdate(),                    -- create_time
    '',                           -- update_by
    NULL,                         -- update_time
    'IP黑名单管理菜单'             -- remark
);

-- 二级菜单：主机连接记录与在线列表（menu_id: 125）
INSERT INTO sys_menu VALUES(
    '125',                        -- menu_id
    '连接记录与在线',              -- menu_name
    '7',                          -- parent_id（主机管理）
    '3',                          -- order_num
    'connection',                 -- path
    'business/host/connection/index',  -- component
    '',                           -- query
    '',                           -- route_name
    1,                            -- is_frame
    0,                            -- is_cache
    'C',                          -- menu_type（C=菜单）
    '0',                          -- visible
    '0',                          -- status
    'business:host:connection:view',  -- perms
    'monitor',                    -- icon
    'admin',                      -- create_by
    sysdate(),                    -- create_time
    '',                           -- update_by
    NULL,                         -- update_time
    '主机连接记录与在线列表管理菜单' -- remark
);
-- ========================================
-- 菜单：WebSocket调试工具（menu_id: 126，父菜单: 7-主机管理）
-- ========================================
INSERT INTO sys_menu VALUES('126', 'WebSocket调试', '7', '4', 'debug', 'business/debug/index', '', '', 1, 0, 'C', '0', '0', 'business:debug:view', 'bug', 'admin', sysdate(), '', NULL, 'WebSocket调试工具，用于开发测试');


-- ========================================
-- 按钮权限：主机ID白名单（menu_id: 1079-1083）
-- ========================================
INSERT INTO sys_menu VALUES('1079', '白名单查询', '123', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:whitelist:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1080', '白名单新增', '123', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:whitelist:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1081', '白名单修改', '123', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:whitelist:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1082', '白名单删除', '123', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:whitelist:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1083', '白名单导出', '123', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:whitelist:export', '#', 'admin', sysdate(), '', NULL, '');


-- ========================================
-- 按钮权限：IP黑名单（menu_id: 1084-1088）
-- ========================================
INSERT INTO sys_menu VALUES('1084', '黑名单查询', '124', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:blacklist:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1085', '黑名单新增', '124', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:blacklist:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1086', '黑名单修改', '124', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:blacklist:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1087', '黑名单删除', '124', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:blacklist:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1088', '黑名单导出', '124', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:blacklist:export', '#', 'admin', sysdate(), '', NULL, '');

-- ========================================
-- 按钮权限：连接记录与在线列表（menu_id: 1089-1092）
-- ========================================
INSERT INTO sys_menu VALUES('1089', '连接记录查询', '125', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:connection:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1090', '连接记录删除', '125', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:connection:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1091', '在线主机查询', '125', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:online:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1092', '在线主机下线', '125', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:host:online:offline', '#', 'admin', sysdate(), '', NULL, '');

-- ========================================
-- 按钮权限：WebSocket调试工具（menu_id: 1093-1096）
-- ========================================
INSERT INTO sys_menu VALUES('1093', '调试工具查看', '126', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:debug:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1094', '发送消息', '126', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:debug:send', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1095', '清空消息', '126', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:debug:clear', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('1096', '导出日志', '126', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'business:debug:export', '#', 'admin', sysdate(), '', NULL, '');

-- ========================================
-- 角色菜单关联：为开发者角色分配权限
-- 说明：只有角色1（超级管理员）和角色2（管理员）有权限
-- ========================================


-- 角色2：管理员（拥有所有权限）
INSERT INTO sys_role_menu VALUES ('2', '126');  -- WebSocket调试工具
INSERT INTO sys_role_menu VALUES ('2', '1093'); -- 调试工具查看
INSERT INTO sys_role_menu VALUES ('2', '1094'); -- 发送消息
INSERT INTO sys_role_menu VALUES ('2', '1095'); -- 清空消息
INSERT INTO sys_role_menu VALUES ('2', '1096'); -- 导出日志

-- ========================================
-- 角色菜单关联：为管理员角色分配权限（role_id: 1=超级管理员, 2=管理员）
-- ========================================

-- 管理员拥有所有权限
INSERT INTO sys_role_menu VALUES ('2', '7');    -- 主机管理目录
INSERT INTO sys_role_menu VALUES ('2', '123');  -- 主机ID白名单
INSERT INTO sys_role_menu VALUES ('2', '124');  -- IP黑名单
INSERT INTO sys_role_menu VALUES ('2', '125');  -- 连接记录与在线
INSERT INTO sys_role_menu VALUES ('2', '1079'); -- 白名单查询
INSERT INTO sys_role_menu VALUES ('2', '1080'); -- 白名单新增
INSERT INTO sys_role_menu VALUES ('2', '1081'); -- 白名单修改
INSERT INTO sys_role_menu VALUES ('2', '1082'); -- 白名单删除
INSERT INTO sys_role_menu VALUES ('2', '1083'); -- 白名单导出
INSERT INTO sys_role_menu VALUES ('2', '1084'); -- 黑名单查询
INSERT INTO sys_role_menu VALUES ('2', '1085'); -- 黑名单新增
INSERT INTO sys_role_menu VALUES ('2', '1086'); -- 黑名单修改
INSERT INTO sys_role_menu VALUES ('2', '1087'); -- 黑名单删除
INSERT INTO sys_role_menu VALUES ('2', '1088'); -- 黑名单导出
INSERT INTO sys_role_menu VALUES ('2', '1089'); -- 连接记录查询
INSERT INTO sys_role_menu VALUES ('2', '1090'); -- 连接记录删除
INSERT INTO sys_role_menu VALUES ('2', '1091'); -- 在线主机查询
INSERT INTO sys_role_menu VALUES ('2', '1092'); -- 在线主机下线

-- 只读管理员只拥有查询权限（role_id: 3=只读权限）
INSERT INTO sys_role_menu VALUES ('3', '7');    -- 主机管理目录
INSERT INTO sys_role_menu VALUES ('3', '123');  -- 主机ID白名单
INSERT INTO sys_role_menu VALUES ('3', '124');  -- IP黑名单
INSERT INTO sys_role_menu VALUES ('3', '125');  -- 连接记录与在线
INSERT INTO sys_role_menu VALUES ('3', '1079'); -- 白名单查询
INSERT INTO sys_role_menu VALUES ('3', '1084'); -- 黑名单查询
INSERT INTO sys_role_menu VALUES ('3', '1089'); -- 连接记录查询
INSERT INTO sys_role_menu VALUES ('3', '1091'); -- 在线主机查询
