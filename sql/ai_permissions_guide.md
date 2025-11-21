# AI智能体权限管理操作指南

## 概述

本文档详细说明了AI智能体管理系统的权限控制机制和常用操作方法。系统通过数据库配置管理AI智能体，支持细粒度的权限控制、选项配置和状态管理。

## 数据库表结构

### 1. ai_agent (AI智能体主表)
存储所有AI智能体的基础信息和配置。

**核心字段说明：**
- `agent_name`: AI显示名称
- `agent_code`: AI标识符（后端调用标识）
- `agent_status`: AI状态（0-下架，1-上架）
- `online_status`: 在线状态（0-离线，1-在线）
- `is_global`: 是否全局可用（0-否，1-是）
- `config_json`: 额外配置（JSON格式，包含选项框和按钮配置）

### 2. ai_agent_permission (AI智能体权限表)
管理用户和角色对AI智能体的访问权限。

**核心字段说明：**
- `agent_id`: 智能体ID
- `permission_type`: 权限类型（1-角色权限，2-用户权限）
- `target_id`: 目标ID（角色ID或用户ID）
- `permission_action`: 权限动作（0-禁止，1-允许）

## 权限控制逻辑

### 权限优先级（从高到低）
1. **用户权限** (permission_type = 2) - 最高优先级
2. **角色权限** (permission_type = 1) - 中等优先级  
3. **全局默认** (is_global = 1) - 最低优先级

### 权限判断流程
1. **检查AI是否上架** (agent_status = 1)
   - 如果未上架，直接拒绝访问

2. **检查用户权限** (permission_type = 2)
   - 如果存在用户权限记录：
     - permission_action = 1：允许访问（忽略角色权限和全局设置）
     - permission_action = 0：禁止访问（忽略角色权限和全局设置）

3. **检查角色权限** (permission_type = 1)
   - 遍历用户的所有角色：
     - 如果任一角色有 permission_action = 1：允许访问
     - 如果所有角色都是 permission_action = 0：禁止访问

4. **检查全局设置** (is_global)
   - is_global = 1：允许访问
   - is_global = 0：禁止访问

## 状态管理

### AI状态说明
- **agent_status（AI状态）**
  - 0: 下架 - AI不可用，不会在任何列表中显示
  - 1: 上架 - AI可用，会根据权限显示给用户

- **online_status（在线状态）**
  - 0: 离线 - AI已上架但当前不可用
  - 1: 在线 - AI已上架且当前可用

- **is_global（全局可用）**
  - 0: 否 - 需要通过权限表明确授权才能使用
  - 1: 是 - 所有用户默认可以使用（除非被明确禁止）

## 常用操作示例

### 1. 查询所有上架的AI（按排序顺序）
```sql
SELECT * FROM ai_agent WHERE agent_status = 1 ORDER BY sort_order ASC;
```

### 2. 查询用户可用的AI列表（完整权限判断）
```sql
SELECT DISTINCT a.* 
FROM ai_agent a
LEFT JOIN ai_agent_permission p ON a.id = p.agent_id
WHERE a.agent_status = 1 
  AND (
    -- 用户权限：明确允许
    (p.permission_type = 2 AND p.target_id = '用户ID' AND p.permission_action = 1)
    -- 角色权限：明确允许（且无用户权限禁止）
    OR (p.permission_type = 1 AND p.target_id IN ('角色ID列表') AND p.permission_action = 1
        AND NOT EXISTS (
          SELECT 1 FROM ai_agent_permission p2 
          WHERE p2.agent_id = a.id 
            AND p2.permission_type = 2 
            AND p2.target_id = '用户ID' 
            AND p2.permission_action = 0
        ))
    -- 全局可用：无权限记录或无禁止记录
    OR (a.is_global = 1 
        AND NOT EXISTS (
          SELECT 1 FROM ai_agent_permission p3 
          WHERE p3.agent_id = a.id 
            AND ((p3.permission_type = 2 AND p3.target_id = '用户ID' AND p3.permission_action = 0)
                 OR (p3.permission_type = 1 AND p3.target_id IN ('角色ID列表') AND p3.permission_action = 0))
        ))
  )
ORDER BY a.sort_order ASC;
```

### 3. 更新AI在线状态
```sql
-- 设置AI为在线
UPDATE ai_agent SET online_status = 1 WHERE agent_code = 'baidu-agent';

-- 设置AI为离线
UPDATE ai_agent SET online_status = 0 WHERE agent_code = 'baidu-agent';
```

### 4. 禁止某个用户使用某个AI
```sql
INSERT INTO ai_agent_permission (agent_id, permission_type, target_id, permission_action, remark)
VALUES (1, 2, '用户ID', 0, '禁止该用户使用百度AI')
ON DUPLICATE KEY UPDATE permission_action = 0, update_time = NOW();
```

### 5. 允许某个角色使用某个AI
```sql
INSERT INTO ai_agent_permission (agent_id, permission_type, target_id, permission_action, remark)
VALUES (1, 1, '角色ID', 1, '允许该角色使用百度AI')
ON DUPLICATE KEY UPDATE permission_action = 1, update_time = NOW();
```

### 6. 查询某个用户的所有AI权限
```sql
SELECT 
  a.agent_name,
  a.agent_code,
  a.online_status,
  a.is_global,
  COALESCE(up.permission_action, rp.permission_action, a.is_global) as final_permission
FROM ai_agent a
LEFT JOIN ai_agent_permission up ON a.id = up.agent_id AND up.permission_type = 2 AND up.target_id = '用户ID'
LEFT JOIN ai_agent_permission rp ON a.id = rp.agent_id AND rp.permission_type = 1 AND rp.target_id IN ('角色ID列表')
WHERE a.agent_status = 1
ORDER BY a.sort_order ASC;
```

## 权限配置场景示例

### 场景1：全局可用AI，特定用户禁止
- **AI设置**：is_global = 1
- **用户权限**：permission_action = 0
- **结果**：该用户无法使用（用户权限优先级最高）

### 场景2：全局禁用AI，特定角色允许
- **AI设置**：is_global = 0
- **角色权限**：permission_action = 1
- **结果**：该角色的用户可以使用（角色权限覆盖全局设置）

### 场景3：角色禁止，但特定用户允许
- **角色权限**：permission_action = 0
- **用户权限**：permission_action = 1
- **结果**：该用户可以使用（用户权限优先级最高）

### 场景4：全局可用，无特殊权限配置
- **AI设置**：is_global = 1
- **无权限记录**
- **结果**：所有用户都可以使用

## 配置JSON说明

AI智能体的 `config_json` 字段支持复杂的前端配置，包括：

### 选项类型
- **select**: 下拉选择框
- **button**: 按钮

### 互斥配置
- **选项级别互斥**：在选项的conflicts数组中配置
- **值级别互斥**：在values数组中每个值的conflicts中配置
- **互斥优先级**：值级别互斥 > 选项级别互斥

### 配置示例
```json
{
  "options": [
    {
      "id": "model_select",
      "type": "select",
      "label": "模型选择",
      "selectType": "single",
      "values": [
        {"label": "不选择", "value": "", "default": true},
        {"label": "DeepSeek-R1", "value": "baidu-dsr1", "default": false, "conflicts": ["deep_search_btn"]}
      ],
      "conflicts": ["deep_search_btn"]
    },
    {
      "id": "deep_search_btn",
      "type": "button",
      "label": "深度搜索",
      "value": "baidu-sdss",
      "conflicts": ["model_select"]
    }
  ]
}
```

## 注意事项

1. **权限变更后需要清除缓存**：系统使用Redis缓存权限信息，权限变更后需要清除相关缓存
2. **批量操作建议使用事务**：涉及多个权限记录的操作建议使用数据库事务
3. **定期清理无效权限**：定期清理已删除用户或角色的权限记录
4. **权限审计**：建议定期审计权限配置，确保符合安全要求

## 菜单权限配置

系统已自动配置AI智能体管理相关的菜单权限：
- AI智能体管理菜单 (menu_id: 2080)
- 相关功能权限 (menu_id: 2081-2086)
- 默认为角色1和角色2分配完整权限

如需调整菜单权限，请参考 `sys_menu` 和 `sys_role_menu` 表的配置。
