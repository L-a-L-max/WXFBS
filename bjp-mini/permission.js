import { getToken } from '@/utils/auth'

// 页面白名单（允许未登录用户访问）
const whiteList = [
  '/pages/index',                    // 首页
  '/pages/work/index',               // 工作台
  '/pages/mine/index',               // 我的页面
  '/pages/mine/about/index',         // 关于我们
  '/pages/mine/help/index',          // 常见问题
  '/pages/register',                 // 注册页
  '/pages/common/webview/index',     // 网页浏览
  '/pages/common/textview/index'     // 文本浏览
]

// 需要登录才能访问的页面
const authList = [
  '/pages/mine/info/index',          // 个人信息
  '/pages/mine/info/edit',           // 编辑资料
  '/pages/mine/avatar/index',        // 修改头像
  '/pages/mine/pwd/index',           // 修改密码
  '/pages/mine/setting/index'        // 应用设置
]

// 检查地址是否在白名单
function checkWhite(url) {
  const path = url.split('?')[0]
  return whiteList.indexOf(path) !== -1
}

// 检查地址是否需要登录
function checkAuth(url) {
  const path = url.split('?')[0]
  return authList.indexOf(path) !== -1
}

// 页面跳转验证拦截器
let list = ["navigateTo", "redirectTo", "reLaunch", "switchTab"]
list.forEach(item => {
  uni.addInterceptor(item, {
    invoke(to) {
      // 已登录用户可以访问任何页面
      if (getToken()) {
        return true
      }
      
      // 未登录用户
      // 如果在白名单中，允许访问
      if (checkWhite(to.url)) {
        return true
      }
      
      // 如果需要登录，跳转到我的页面提示登录
      if (checkAuth(to.url)) {
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        })
        uni.switchTab({ url: '/pages/mine/index' })
        return false
      }
      
      // 默认允许访问
      return true
    },
    fail(err) {
      console.log(err)
    }
  })
})
