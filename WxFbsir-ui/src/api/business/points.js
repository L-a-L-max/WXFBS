import request from '@/utils/request'

// 积分发放/扣减
export function changePoints(ruleCode, changeAmount) {
  return request({
    url: '/points/changePoints',
    method: 'post',
    params: {
      ruleCode,
      changeAmount
    }
  })
}

// 查询用户积分余额
export function getUserPoints() {
  return request({
    url: '/points/getUserPoints',
    method: 'get'
  })
}

// 查询积分明细记录
export function getPointsRecord(query) {
  return request({
    url: '/points/getPointsRecord',
    method: 'get',
    params: query
  })
}

// 获取积分概览
export function getPointsSummary() {
  return request({
    url: '/points/getPointsSummary',
    method: 'get'
  })
}

// 获取积分任务清单
export function getPointTaskList() {
  return request({
    url: '/points/getPointTaskList',
    method: 'get'
  })
}

