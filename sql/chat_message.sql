/*
 Navicat Premium Dump SQL

 Source Server         : wxfbsir
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : 43.139.254.160:3306
 Source Schema         : wxfbsir

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 31/12/2025 10:46:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`  (
  `chat_id` bigint NOT NULL AUTO_INCREMENT COMMENT '对话信息ID',
  `message_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息ID，用于去重',
  `session_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话ID（用户标识）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
  `role` enum('USER','AI') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'USER' COMMENT '角色\"谁发的消息\"',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读：0-未读, 1-已读',
  `point_cost` int NULL DEFAULT NULL COMMENT '对话消耗的积分',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_message
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
