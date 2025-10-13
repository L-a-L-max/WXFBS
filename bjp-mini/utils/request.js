import store from '@/store'
import config from '@/config'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { toast, showConfirm, tansParams } from '@/utils/common'

let timeout = 10000
const baseUrl = config.baseUrl

const request = config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  config.header = config.header || {}
  if (getToken() && !isToken) {
    config.header['Authorization'] = 'Bearer ' + getToken()
  }
  // get请求映射params参数
  if (config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.url = url
  }
  
  const fullUrl = config.baseUrl || baseUrl + config.url
  console.log('=== [Request] 发起HTTP请求 ===')
  console.log('请求方法:', config.method || 'get')
  console.log('请求地址:', fullUrl)
  console.log('请求数据:', config.data)
  console.log('请求头:', config.header)
  
  return new Promise((resolve, reject) => {
    uni.request({
        method: config.method || 'get',
        timeout: config.timeout ||  timeout,
        url: fullUrl,
        data: config.data,
        header: config.header,
        dataType: 'json'
      }).then(response => {
        console.log('=== [Request] 收到HTTP响应 ===')
        console.log('响应数据:', response)
        let [error, res] = response
        if (error) {
          toast('后端接口连接异常')
          reject('后端接口连接异常')
          return
        }
        const code = res.data.code || 200
        const msg = errorCode[code] || res.data.msg || errorCode['default']
        if (code === 401) {
          console.log('=== [Request] 401未授权，清除登录状态 ===')
          // 静默清除所有登录状态
          store.dispatch('ClearLoginStatus').then(() => {
            console.log('=== [Request] 登录状态已清除 ===')
          })
          
          // 静默处理，不显示弹窗打扰用户
          // 用户下次访问需要登录的功能时会自动提示
          reject('无效的会话，或者会话已过期，请重新登录。')
        } else if (code === 500) {
          toast(msg)
          reject('500')
        } else if (code !== 200) {
          toast(msg)
          reject(code)
        }
        resolve(res.data)
      })
      .catch(error => {
        let { message } = error
        
        // 处理微信会话过期错误
        if (message && (message.includes('无效的会话') || message.includes('会话已过期'))) {
          console.log('=== [Request] 检测到微信会话过期 ===')
          
          // 清除登录状态
          store.dispatch('ClearLoginStatus').then(() => {
            console.log('=== [Request] 登录状态已清除 ===')
          })
          
          // 友好提示用户
          uni.showToast({
            title: '登录已过期，请重新登录',
            icon: 'none',
            duration: 2000
          })
          
          // 延迟跳转到个人中心页面
          setTimeout(() => {
            uni.switchTab({
              url: '/pages/mine/index'
            })
          }, 500)
          
          reject(error)
          return
        }
        
        if (message === 'Network Error') {
          message = '后端接口连接异常'
        } else if (message.includes('timeout')) {
          message = '系统接口请求超时'
        } else if (message.includes('Request failed with status code')) {
          message = '系统接口' + message.substr(message.length - 3) + '异常'
        }
        toast(message)
        reject(error)
      })
  })
}

export default request
