import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    'url': '/login',
    headers: {
      isToken: false
    },
    'method': 'post',
    'data': data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    'url': '/getInfo',
    'method': 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    'url': '/logout',
    'method': 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    'url': '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

// 微信小程序登录
export function wechatMiniAppLogin(code, nickName, avatar) {
  const data = {
    code,
    nickName,
    avatar
  }
  console.log('=== [API] 发起微信登录请求 ===')
  console.log('请求URL: /wechat/miniapp/login')
  console.log('请求数据:', data)
  
  return request({
    url: '/wechat/miniapp/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 微信服务号登录
export function wechatOfficialLogin(code) {
  const data = {
    code
  }
  return request({
    url: '/wechat/official/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
