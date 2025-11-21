import request from '@/utils/request'

// 查询AI智能体列表
export function listAiagent(query) {
  return request({
    url: '/system/aiagent/list',
    method: 'get',
    params: query
  })
}

// 查询所有上架的AI智能体列表
export function listActiveAiagent() {
  return request({
    url: '/system/aiagent/listActive',
    method: 'get'
  })
}

// 查询用户可用的AI智能体列表
export function listUserAvailableAiagent() {
  return request({
    url: '/system/aiagent/listUserAvailable',
    method: 'get'
  })
}

// 查询AI智能体详细
export function getAiagent(id) {
  return request({
    url: '/system/aiagent/' + id,
    method: 'get'
  })
}

// 根据智能体代码查询AI智能体详细
export function getAiagentByCode(agentCode) {
  return request({
    url: '/system/aiagent/code/' + agentCode,
    method: 'get'
  })
}

// 新增AI智能体
export function addAiagent(data) {
  return request({
    url: '/system/aiagent',
    method: 'post',
    data: data
  })
}

// 修改AI智能体
export function updateAiagent(data) {
  return request({
    url: '/system/aiagent',
    method: 'put',
    data: data
  })
}

// 删除AI智能体
export function delAiagent(id) {
  return request({
    url: '/system/aiagent/' + id,
    method: 'delete'
  })
}

// 更新AI在线状态
export function updateOnlineStatus(data) {
  return request({
    url: '/system/aiagent/updateOnlineStatus',
    method: 'put',
    data: data
  })
}

// 批量更新AI在线状态
export function batchUpdateOnlineStatus(agentCodes, onlineStatus) {
  return request({
    url: '/system/aiagent/batchUpdateOnlineStatus',
    method: 'put',
    data: agentCodes,
    params: { onlineStatus }
  })
}

// 检查用户权限
export function checkPermission(agentCode) {
  return request({
    url: '/system/aiagent/checkPermission/' + agentCode,
    method: 'get'
  })
}

// 刷新缓存
export function refreshCache() {
  return request({
    url: '/system/aiagent/refreshCache',
    method: 'delete'
  })
}

// 清除缓存
export function clearCache(agentCode) {
  return request({
    url: '/system/aiagent/clearCache',
    method: 'delete',
    params: { agentCode }
  })
}
