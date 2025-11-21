import request from '@/utils/request'

// 查询所有上架的AI智能体列表（不区分用户，显示所有上架的AI）
export function listActiveAiagent() {
  return request({
    url: '/system/aiagent/listActive',
    method: 'GET',
    header: {
      isToken: false  // 不需要token，未登录也可以访问
    }
  })
}

// 查询用户可用的AI智能体列表（需要登录）
export function listUserAvailableAiagent() {
  return request({
    url: '/system/aiagent/listUserAvailable',
    method: 'GET'
  })
}

// 根据智能体代码查询AI智能体详细
export function getAiagentByCode(agentCode) {
  return request({
    url: '/system/aiagent/code/' + agentCode,
    method: 'GET'
  })
}

// 更新AI在线状态
export function updateOnlineStatus(data) {
  return request({
    url: '/system/aiagent/updateOnlineStatus',
    method: 'PUT',
    data: data
  })
}

// 批量更新AI在线状态
export function batchUpdateOnlineStatus(agentCodes, onlineStatus) {
  return request({
    url: '/system/aiagent/batchUpdateOnlineStatus',
    method: 'PUT',
    data: agentCodes,
    params: { onlineStatus }
  })
}

// 检查用户权限
export function checkPermission(agentCode) {
  return request({
    url: '/system/aiagent/checkPermission/' + agentCode,
    method: 'GET'
  })
}
