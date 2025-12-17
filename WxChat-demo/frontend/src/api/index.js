import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = sessionStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 企业微信认证
export function handleOAuthCallback(code, state) {
  return request({
    url: '/auth/callback',
    method: 'get',
    params: { code, state }
  })
}

// 发送消息
export function sendMessage(data) {
  return request({
    url: '/chat/send',
    method: 'post',
    data: data
  })
}

// 轮询获取回复
export function pollReply(sessionId) {
  return request({
    url: '/chat/poll',
    method: 'get',
    params: { sessionId }
  })
}

export default request
