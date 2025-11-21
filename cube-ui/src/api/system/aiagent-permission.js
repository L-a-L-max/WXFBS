import request from '@/utils/request'

// =============================================
// AI智能体权限管理API
// =============================================

/**
 * 查询AI智能体权限列表
 */
export function listAiagentPermission(query) {
  return request({
    url: '/system/aiagent/permission/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询用户的所有AI权限
 */
export function listUserPermissions() {
  return request({
    url: '/system/aiagent/permission/listUserPermissions',
    method: 'get'
  })
}

/**
 * 查询AI智能体权限详细
 */
export function getAiagentPermission(id) {
  return request({
    url: '/system/aiagent/permission/' + id,
    method: 'get'
  })
}

/**
 * 检查用户对某个AI的权限
 * @param agentId AI智能体ID
 */
export function checkPermission(agentId) {
  return request({
    url: '/system/aiagent/permission/check/' + agentId,
    method: 'get'
  })
}

/**
 * 新增AI智能体权限
 */
export function addAiagentPermission(data) {
  return request({
    url: '/system/aiagent/permission',
    method: 'post',
    data: data
  })
}

/**
 * 修改AI智能体权限
 */
export function updateAiagentPermission(data) {
  return request({
    url: '/system/aiagent/permission',
    method: 'put',
    data: data
  })
}

/**
 * 删除AI智能体权限
 */
export function deleteAiagentPermission(id) {
  return request({
    url: '/system/aiagent/permission/' + id,
    method: 'delete'
  })
}

/**
 * 批量删除AI智能体权限
 */
export function deleteAiagentPermissions(ids) {
  return request({
    url: '/system/aiagent/permission/' + ids,
    method: 'delete'
  })
}

/**
 * 设置用户权限
 * @param agentId AI智能体ID
 * @param userId 用户ID
 * @param permissionAction 权限动作（0-禁止，1-允许）
 */
export function setUserPermission(agentId, userId, permissionAction) {
  return request({
    url: '/system/aiagent/permission/setUserPermission',
    method: 'post',
    params: {
      agentId: agentId,
      userId: userId,
      permissionAction: permissionAction
    }
  })
}

/**
 * 设置角色权限
 * @param agentId AI智能体ID
 * @param roleId 角色ID
 * @param permissionAction 权限动作（0-禁止，1-允许）
 */
export function setRolePermission(agentId, roleId, permissionAction) {
  return request({
    url: '/system/aiagent/permission/setRolePermission',
    method: 'post',
    params: {
      agentId: agentId,
      roleId: roleId,
      permissionAction: permissionAction
    }
  })
}

/**
 * 批量设置权限
 * @param permissions 权限列表
 */
export function batchSetPermissions(permissions) {
  return request({
    url: '/system/aiagent/permission/batchSetPermissions',
    method: 'post',
    data: permissions
  })
}

/**
 * 删除智能体的所有权限
 * @param agentId AI智能体ID
 */
export function deleteByAgentId(agentId) {
  return request({
    url: '/system/aiagent/permission/deleteByAgentId/' + agentId,
    method: 'delete'
  })
}

/**
 * 刷新权限缓存
 * @param userId 用户ID（可选，为空则刷新所有用户缓存）
 */
export function refreshPermissionCache(userId) {
  return request({
    url: '/system/aiagent/permission/refreshCache',
    method: 'delete',
    params: {
      userId: userId
    }
  })
}

/**
 * 清除权限缓存
 * @param userId 用户ID（可选，为空则清除所有用户缓存）
 */
export function clearPermissionCache(userId) {
  return request({
    url: '/system/aiagent/permission/clearCache',
    method: 'delete',
    params: {
      userId: userId
    }
  })
}

/**
 * 导出AI智能体权限列表
 */
export function exportAiagentPermission(query) {
  return request({
    url: '/system/aiagent/permission/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

/**
 * 查询某个AI的所有权限
 * @param agentId AI智能体ID
 */
export function getAiAgentPermissions(agentId) {
  return request({
    url: '/system/aiagent/permission/list',
    method: 'get',
    params: {
      agentId: agentId
    }
  })
}

/**
 * 查询某个AI的用户权限
 * @param agentId AI智能体ID
 */
export function getAiAgentUserPermissions(agentId) {
  return request({
    url: '/system/aiagent/permission/list',
    method: 'get',
    params: {
      agentId: agentId,
      permissionType: 2
    }
  })
}

/**
 * 查询某个AI的角色权限
 * @param agentId AI智能体ID
 */
export function getAiAgentRolePermissions(agentId) {
  return request({
    url: '/system/aiagent/permission/list',
    method: 'get',
    params: {
      agentId: agentId,
      permissionType: 1
    }
  })
}
