const TokenKey = 'App-Token'

export function getToken() {
  return uni.getStorageSync(TokenKey)
}

export function setToken(token) {
  return uni.setStorageSync(TokenKey, token)
}

export function removeToken() {
  return uni.removeStorageSync(TokenKey)
}

// 检查是否已登录
export function isLoggedIn() {
  return !!getToken()
}

// 需要登录才能访问的页面跳转
export function requireLogin(callback) {
  if (isLoggedIn()) {
    // 已登录，执行回调
    if (typeof callback === 'function') {
      callback()
    }
    return true
  } else {
    // 未登录，提示并跳转到个人中心
    uni.showToast({
      title: '请先登录',
      icon: 'none',
      duration: 2000
    })
    setTimeout(() => {
      uni.switchTab({
        url: '/pages/mine/index'
      })
    }, 500)
    return false
  }
}