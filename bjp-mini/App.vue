<script>
  import config from './config'
  import { getToken } from '@/utils/auth'

  export default {
    onLaunch: function() {
      this.initApp()
      this.startLoginStatusCheck()
    },
    onShow: function() {
      // 每次应用显示时检查登录状态
      this.checkLoginStatus()
    },
    onHide: function() {
      // 应用隐藏时清除定时器
      if (this.loginCheckTimer) {
        clearInterval(this.loginCheckTimer)
        this.loginCheckTimer = null
      }
    },
    data() {
      return {
        loginCheckTimer: null,
        lastLoginCheckTime: 0
      }
    },
    methods: {
      // 初始化应用
      initApp() {
        // 初始化应用配置
        this.initConfig()
        // 检查用户登录状态
        //#ifdef H5
        this.checkLogin()
        //#endif
      },
      initConfig() {
        this.globalData.config = config
      },
      checkLogin() {
        if (!getToken()) {
          this.$tab.reLaunch('/pages/login') 
        }
      },
      // 启动登录状态定时检查（每30秒检查一次）
      startLoginStatusCheck() {
        // 清除之前的定时器
        if (this.loginCheckTimer) {
          clearInterval(this.loginCheckTimer)
        }
        
        // 每30秒检查一次登录状态
        this.loginCheckTimer = setInterval(() => {
          this.checkLoginStatus()
        }, 30000)
        
        console.log('=== 登录状态定时检查已启动 ===')
      },
      // 检查登录状态
      checkLoginStatus() {
        const now = Date.now()
        // 避免频繁检查，至少间隔10秒
        if (now - this.lastLoginCheckTime < 10000) {
          return
        }
        this.lastLoginCheckTime = now
        
        const token = getToken()
        const storeToken = this.$store.state.user.token
        const storeName = this.$store.state.user.name
        
        // 如果store中有用户信息但没有token，说明登录已过期
        if (storeName && !token) {
          console.log('=== 检测到登录已过期，清除用户信息 ===')
          this.$store.dispatch('ClearLoginStatus')
        }
        
        // 如果token不一致，同步状态
        if (token && token !== storeToken) {
          console.log('=== Token不一致，同步状态 ===')
          this.$store.commit('SET_TOKEN', token)
        }
      }
    }
  }
</script>

<style lang="scss">
  @import '@/static/scss/index.scss'
</style>
