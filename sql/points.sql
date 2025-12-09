-- 积分系统数据库表结构
-- 创建时间: 2025-12-08

-- ----------------------------
-- 积分规则配置表
-- ----------------------------
DROP TABLE IF EXISTS `wx_points_rule`;
CREATE TABLE `wx_points_rule` (
  `rule_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_code` VARCHAR(50) NOT NULL COMMENT '规则编码（唯一标识，用于业务索引）',
  `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称（用于显示，可修改）',
  `points_value` INT NOT NULL COMMENT '积分值（正数为奖励，负数为扣减）',
  `limit_type` VARCHAR(20) DEFAULT NULL COMMENT '限频类型：DAILY/WEEKLY/MONTHLY/TOTAL',
  `limit_value` INT DEFAULT NULL COMMENT '限频次数',
  `max_amount` INT DEFAULT NULL COMMENT '累计上限',
  `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `uk_rule_code` (`rule_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分规则配置表';

-- ----------------------------
-- 积分明细记录表
-- ----------------------------
DROP TABLE IF EXISTS `wx_points_record`;
CREATE TABLE `wx_points_record` (
  `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `rule_code` VARCHAR(50) NOT NULL COMMENT '规则编码（关联wx_points_rule.rule_code）',
  `change_amount` INT NOT NULL COMMENT '变动金额（正数为增加，负数为扣减）',
  `balance_before` INT NOT NULL COMMENT '变动前余额',
  `balance_after` INT NOT NULL COMMENT '变动后余额',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注说明',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_rule_code` (`rule_code`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_time` (`user_id`, `create_time`),
  CONSTRAINT `fk_points_record_rule` FOREIGN KEY (`rule_code`) REFERENCES `wx_points_rule` (`rule_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分明细记录表';

-- ----------------------------
-- 积分统计表（可选，用于统计分析）
-- ----------------------------
DROP TABLE IF EXISTS `wx_points_statistics`;
CREATE TABLE `wx_points_statistics` (
  `stat_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `stat_date` DATE NOT NULL COMMENT '统计日期',
  `points_gain` INT DEFAULT 0 COMMENT '当日获得积分',
  `points_used` INT DEFAULT 0 COMMENT '当日使用积分',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `stat_date`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分统计表';

-- ----------------------------
-- 初始化积分规则数据
-- ----------------------------
INSERT INTO `wx_points_rule` (`rule_code`, `rule_name`, `points_value`, `limit_type`, `limit_value`, `max_amount`, `status`, `sort_order`, `remark`, `create_by`, `create_time`) VALUES
('FIRST_LOGIN_BONUS', '首次登录奖励', 5000, NULL, NULL, NULL, '0', 2, '用户首次登录奖励', 'admin', NOW()),
('DAILY_LOGIN', '每日登录', 10, 'DAILY', 1, NULL, '0', 1, '用户每日首次登录奖励', 'admin', NOW()),
('USE_DAILY_ASSISTANT', '使用日更助手', -1, NULL, NULL, NULL, '0', 2, '使用日更助手生成文章时扣减', 'admin', NOW()),
('TEMPLATE_PUBLISH', '模板上架', 50, NULL, NULL, NULL, '0', 3, '用户上架模板到市场奖励', 'admin', NOW()),
('TEMPLATE_BUY', '模板购买', 0, NULL, NULL, NULL, '0', 4, '购买模板时扣减', 'admin', NOW()),
('TEMPLATE_REWARD', '模板分成', 0, NULL, NULL, NULL, '0', 5, '模板被购买时的作者分成', 'admin', NOW());


-- 检查 sys_user 表是否有 points 字段，如果没有则添加
SET @exist := (SELECT COUNT(*) FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE()
               AND TABLE_NAME = 'sys_user'
               AND COLUMN_NAME = 'points');
SET @sqlstmt := IF(@exist = 0,
    'ALTER TABLE `sys_user` ADD COLUMN `points` INT(5) DEFAULT 0 COMMENT ''积分余额'' AFTER `status`',
    'SELECT ''points字段已存在'' AS message');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- 积分系统菜单数据
-- ----------------------------

-- 一级菜单：积分管理（parent_id=6）
insert into sys_menu values('6', '积分管理', '0', '6', 'points', null, '', '', 1, 0, 'M', '0', '0', '', 'money', 'admin', sysdate(), '', null, '积分管理目录');
-- 积分管理子菜单（parent_id=6）
insert into sys_menu values('120',   '积分总览', '6',   '1', 'points-overview', 'business/points/overview/index', '', '', 1, 0, 'C', '0', '0', 'business:points:view', 'money', 'admin', sysdate(), '', null, '积分总览菜单');
insert into sys_menu values('121',   '积分规则配置', '6', '2', 'points-rule', 'system/points/rule/index', '', '', 1, 0, 'C', '0', '0', 'points:rule:list', 'edit', 'admin', sysdate(), '', null, '积分规则配置菜单');
-- 积分总览按钮(parent_id=120)
insert into sys_menu values('1070', '积分查询', '120', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:points:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('1071', '明细查询', '120', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:points:record:query', '#', 'admin', sysdate(), '', null, '');
-- 积分规则配置按钮(parent_id=121)
insert into sys_menu values('1072', '规则查询', '121', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'points:rule:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1073', '规则新增', '121', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'points:rule:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1074', '规则修改', '121', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'points:rule:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1075', '规则删除', '121', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'points:rule:remove', '#', 'admin', sysdate(), '', null, '');


insert into sys_role_menu values ('2', '6');    -- 积分管理


-- 按钮权限-积分管理
insert into sys_role_menu values ('2', '1070');  -- 积分查询
insert into sys_role_menu values ('2', '1071');  -- 明细查询
insert into sys_role_menu values ('2', '1072');  -- 规则查询
insert into sys_role_menu values ('2', '1073');  -- 规则新增
insert into sys_role_menu values ('2', '1074');  -- 规则修改
insert into sys_role_menu values ('2', '1075');  -- 规则删除

insert into sys_role_menu values ('3', '6');    -- 积分管理

insert into sys_role_menu values ('10', '1070'); -- 积分查询
insert into sys_role_menu values ('10', '1071'); -- 明细查询
insert into sys_role_menu values ('10', '1072'); -- 规则查询




