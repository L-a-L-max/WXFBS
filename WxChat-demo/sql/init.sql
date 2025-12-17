-- ----------------------------
-- WxChat-demo 数据库初始化脚本
-- ----------------------------

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `wxchat_demo` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `wxchat_demo`;

-- ----------------------------
-- 企业微信配置表
-- ----------------------------
DROP TABLE IF EXISTS wework_config;
CREATE TABLE wework_config (
    id              BIGINT(20)      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    team_id         BIGINT(20)      NOT NULL                COMMENT '团队ID',
    corp_id         VARCHAR(100)    NOT NULL                COMMENT '企业ID',
    agent_id        VARCHAR(50)                             COMMENT '应用AgentId',
    agent_secret    VARCHAR(200)                            COMMENT '应用Secret',
    group_chat_id   VARCHAR(100)                            COMMENT '群聊ID',
    status          CHAR(1)         DEFAULT '1'             COMMENT '状态：0停用 1正常',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_team_id (team_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业微信配置表';

-- 插入默认配置（需要替换为实际值）
INSERT INTO wework_config (team_id, corp_id, agent_id, agent_secret) VALUES
(1, 'your_corp_id', 'your_agent_id', 'your_agent_secret');

-- ----------------------------
-- 企业微信成员表
-- ----------------------------
DROP TABLE IF EXISTS wework_member;
CREATE TABLE wework_member (
    id              BIGINT(20)      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    wework_user_id  VARCHAR(100)    NOT NULL                COMMENT '企业微信用户ID',
    team_id         BIGINT(20)                              COMMENT '团队ID',
    member_name     VARCHAR(100)                            COMMENT '成员姓名',
    department      VARCHAR(500)                            COMMENT '部门',
    mobile          VARCHAR(20)                             COMMENT '手机号',
    email           VARCHAR(100)                            COMMENT '邮箱',
    avatar          VARCHAR(500)                            COMMENT '头像URL',
    in_group        TINYINT(1)      DEFAULT 1               COMMENT '是否在群内',
    status          CHAR(1)         DEFAULT '1'             COMMENT '状态',
    last_verify_time DATETIME                               COMMENT '最后验证时间',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_wework_user_id (wework_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业微信成员表';

-- ----------------------------
-- AI对话消息表
-- ----------------------------
DROP TABLE IF EXISTS ai_chat_message;
CREATE TABLE ai_chat_message (
    id              BIGINT(20)      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    session_id      VARCHAR(100)    NOT NULL                COMMENT '会话ID（用户标识）',
    role            VARCHAR(20)     NOT NULL                COMMENT '角色：user-用户, assistant-AI助手',
    content         TEXT                                    COMMENT '消息内容',
    message_id      VARCHAR(100)                            COMMENT '消息唯一标识（用于去重）',
    is_read         TINYINT(1)      DEFAULT 0               COMMENT '是否已读：0-未读, 1-已读',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_session_id (session_id),
    INDEX idx_session_read (session_id, is_read),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI对话消息表';
