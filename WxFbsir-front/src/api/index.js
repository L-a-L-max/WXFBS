import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = sessionStorage.getItem('token') || localStorage.getItem('token')
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

// 发送消息
export function sendMessage(data) {
  // 如果是FormData直接发送（文件上传情况）
  if (data instanceof FormData) {
    return request({
      url: '/chat/send',
      method: 'post',
      data: data
      // 移除手动Content-Type设置，保留请求拦截器的Authorization头
    })
  }
  // 普通文本消息也使用FormData发送，确保与后端@RequestParam注解兼容
  const formData = new FormData()
  formData.append('message', data.message)
  if (data.sessionId) {
    formData.append('sessionId', data.sessionId)
  }
  return request({
    url: '/chat/send',
    method: 'post',
    data: formData
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
