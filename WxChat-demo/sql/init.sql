-- ----------------------------
-- WxChat-demo 数据库初始化脚本
-- 说明：所有配置信息（企业微信、元器）统一在 application.yml 中配置
-- ----------------------------

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `demo` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `demo`;

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
