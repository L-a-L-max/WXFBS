import request from '@/utils/request'

export function listUsageReport(query) {
  return request({
    url: '/business/gitee/admin/usage-report/list',
    method: 'get',
    params: query
  })
}

export function generateUsageReport(reportDate) {
  return request({
    url: '/business/gitee/admin/usage-report/generate',
    method: 'post',
    params: reportDate ? { reportDate } : {}
  })
}
