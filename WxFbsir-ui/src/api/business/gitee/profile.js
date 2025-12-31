import request from "@/utils/request"

export function getGiteeStatus() {
  return request({
    url: "/business/gitee/status",
    method: "get"
  })
}

export function getGiteeAuthorizeUrl(params = {}) {
  return request({
    url: "/business/gitee/authorize",
    method: "get",
    params
  })
}

export function fetchGiteeProfile() {
  return request({
    url: "/business/gitee/profile",
    method: "get"
  })
}

export function fetchGiteeRepos(params = {}) {
  return request({
    url: "/business/gitee/repos",
    method: "get",
    params
  })
}

export function fetchGiteeIssues(params = {}) {
  return request({
    url: "/business/gitee/issues",
    method: "get",
    params
  })
}

export function fetchGiteeNotifications(params = {}) {
  return request({
    url: "/business/gitee/notifications",
    method: "get",
    params
  })
}
