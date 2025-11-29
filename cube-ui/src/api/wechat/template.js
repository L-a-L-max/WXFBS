import request from '@/utils/request'

// 查询我创建的模板
export function getMyCreatedTemplates(query) {
  return request({
    url: '/mini/getMyCreatedTemplates',
    method: 'get',
    params: query
  })
}

// 查询我购买的模板
export function getMyPurchasedTemplates(query) {
  return request({
    url: '/mini/getMyPurchasedTemplates',
    method: 'get',
    params: query
  })
}

// 查询市场模板（已上架的模板）
export function getMarketTemplates(query) {
  return request({
    url: '/mini/getMarketTemplates',
    method: 'get',
    params: query
  })
}

// 购买模板
export function purchaseTemplate(templateId) {
  return request({
    url: `/mini/purchaseTemplate/${templateId}`,
    method: 'post'
  })
}

// 获取我的积分
export function getMyPoints() {
  return request({
    url: '/mini/getMyPoints',
    method: 'get'
  })
}

// ========== 提示词模板相关接口 ==========

// 查询我创建的提示词模板
export function getMyCreatedCallWords(query) {
  return request({
    url: '/mini/getMyCreatedCallWords',
    method: 'get',
    params: query
  })
}

// 查询我购买的提示词模板
export function getMyPurchasedCallWords(query) {
  return request({
    url: '/mini/getMyPurchasedCallWords',
    method: 'get',
    params: query
  })
}

// 查询市场中的提示词模板
export function getMarketCallWords(query) {
  return request({
    url: '/mini/getMarketCallWords',
    method: 'get',
    params: query
  })
}

// 购买提示词模板
export function purchaseCallWord(platformId) {
  return request({
    url: `/mini/purchaseCallWord/${platformId}`,
    method: 'post'
  })
}

// 获取我的积分概览
export function getMyPointsSummary() {
  return request({
    url: '/mini/getMyPointsSummary',
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

