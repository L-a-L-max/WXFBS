<template>
  <view class="mine-container" :style="{height: `${windowHeight}px`}">
    <!--顶部个人信息栏-->
    <view class="header-section">
      <view class="flex padding justify-between align-center">
        <view class="flex align-center">
          <!-- 头像 -->
          <view v-if="!avatar" class="cu-avatar xl round bg-white" @click="handleAvatarClick" :class="{'fade-in': showAvatar}">
            <view class="iconfont icon-people text-gray icon"></view>
          </view>
          <image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar xl round" :class="{'fade-in': showAvatar}" mode="widthFix">
          </image>
          
          <!-- 用户信息 -->
          <transition name="slide-fade">
            <view v-if="name" @click="handleToInfo" class="user-info">
              <view class="u_title">
                {{ nickName || name }}
                <text class="online-badge" v-if="isOnline">● 在线</text>
              </view>
              <view class="u_subtitle">
                ID: {{ id }}
              </view>
            </view>
          </transition>
          
          <!-- 未登录提示 -->
          <transition name="slide-fade">
            <view v-if="!name" class="login-tip">
              <view class="login-text">未登录</view>
              <view class="login-subtitle">点击右侧按钮登录</view>
            </view>
          </transition>
        </view>
        
        <!-- 右侧操作按钮 -->
        <view class="header-action">
          <!-- #ifdef MP-WEIXIN -->
          <!-- 未登录时显示登录按钮 -->
          <transition name="fade">
            <button v-if="!name" class="wechat-login-btn" @click="handleWeChatLogin" open-type="getUserProfile">
              <text class="cuIcon-weixin"></text>
              <text class="btn-text">微信登录</text>
            </button>
          </transition>
          <!-- #endif -->
          
          <!-- 已登录时显示个人信息入口 -->
          <transition name="fade">
            <view v-if="name" @click="handleToInfo" class="info-btn flex align-center">
              <text>个人信息</text>
              <view class="iconfont icon-right"></view>
            </view>
          </transition>
        </view>
      </view>
    </view>

    <view class="content-section">
      <view class="mine-actions grid col-4 text-center">
        <view class="action-item" @click="handleJiaoLiuQun">
          <view class="iconfont icon-friendfill text-pink icon"></view>
          <text class="text">交流群</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-service text-blue icon"></view>
          <text class="text">在线客服</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-community text-mauve icon"></view>
          <text class="text">反馈社区</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-dianzan text-green icon"></view>
          <text class="text">点赞我们</text>
        </view>
      </view>

      <view class="menu-list">
        <view v-if="name" class="list-cell list-cell-arrow" @click="handleToEditInfo">
          <view class="menu-item-box">
            <view class="iconfont icon-user menu-icon"></view>
            <view>编辑资料</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow" @click="handleHelp">
          <view class="menu-item-box">
            <view class="iconfont icon-help menu-icon"></view>
            <view>常见问题</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow" @click="handleAbout">
          <view class="menu-item-box">
            <view class="iconfont icon-aixin menu-icon"></view>
            <view>关于我们</view>
          </view>
        </view>
        <view v-if="name" class="list-cell list-cell-arrow" @click="handleToSetting">
          <view class="menu-item-box">
            <view class="iconfont icon-setting menu-icon"></view>
            <view>应用设置</view>
          </view>
        </view>
        <view v-if="name" class="list-cell list-cell-arrow" @click="handleLogout">
          <view class="menu-item-box">
            <view class="iconfont icon-exit menu-icon" style="color: #f56c6c;"></view>
            <view style="color: #f56c6c;">退出登录</view>
          </view>
        </view>
      </view>

    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        id: this.$store.state.user.id,
        name: this.$store.state.user.name,
        nickName: this.$store.state.user.nickName,
        isOnline: true,
        showAvatar: false
      }
    },
    mounted() {
      // 页面加载后显示头像动画
      setTimeout(() => {
        this.showAvatar = true
      }, 100)
    },
    computed: {
      avatar() {
        return this.$store.state.user.avatar
      },
      windowHeight() {
        return uni.getSystemInfoSync().windowHeight - 50
      }
    },
    onShow() {
      // 页面显示时更新用户信息
      this.refreshUserInfo()
      this.checkOnlineStatus()
    },
    methods: {
      // 刷新用户信息
      refreshUserInfo() {
        this.id = this.$store.state.user.id
        this.name = this.$store.state.user.name
        this.nickName = this.$store.state.user.nickName
      },
      handleToInfo() {
        this.$tab.navigateTo('/pages/mine/info/index')
      },
      handleAvatarClick() {
        if (!this.name) {
          this.$modal.showToast('请先登录')
        }
      },
      handleToEditInfo() {
        if (!this.name) {
          this.$modal.showToast('请先登录')
          return
        }
        this.$tab.navigateTo('/pages/mine/info/edit')
      },
      handleToSetting() {
        if (!this.name) {
          this.$modal.showToast('请先登录')
          return
        }
        this.$tab.navigateTo('/pages/mine/setting/index')
      },
      handleToAvatar() {
        if (!this.name) {
          this.$modal.showToast('请先登录')
          return
        }
        this.$tab.navigateTo('/pages/mine/avatar/index')
      },
      handleHelp() {
        this.$tab.navigateTo('/pages/mine/help/index')
      },
      handleAbout() {
        this.$tab.navigateTo('/pages/mine/about/index')
      },
      handleJiaoLiuQun() {
        this.$modal.showToast('QQ群：①133713780(满)、②146013835(满)、③189091635')
      },
      handleBuilding() {
        this.$modal.showToast('模块建设中~')
      },
      // 微信一键授权登录
      handleWeChatLogin() {
        // #ifdef MP-WEIXIN
        this.$modal.loading("正在登录...")
        // 获取微信用户信息
        uni.getUserProfile({
          desc: '用于完善用户资料',
          success: (res) => {
            const userInfo = res.userInfo
            console.log('获取用户信息成功', userInfo)
            
            // 获取微信登录code
            uni.login({
              success: (loginRes) => {
                const code = loginRes.code
                console.log('获取微信code成功', code)
                
                // 调用vuex中的微信登录action
                this.$store.dispatch('WeChatLogin', {
                  code: code,
                  nickName: userInfo.nickName,
                  avatar: userInfo.avatarUrl
                }).then(() => {
                  this.$modal.closeLoading()
                  this.$modal.msgSuccess("登录成功")
                  // 刷新页面显示
                  this.refreshUserInfo()
                  this.isOnline = true
                }).catch((error) => {
                  this.$modal.closeLoading()
                  this.$modal.msgError(error.msg || "登录失败，请重试")
                })
              },
              fail: (error) => {
                console.log('获取微信code失败', error)
                this.$modal.closeLoading()
                this.$modal.msgError("获取微信登录信息失败")
              }
            })
          },
          fail: (error) => {
            console.log('获取用户信息失败', error)
            this.$modal.closeLoading()
            this.$modal.msgError("需要授权才能登录")
          }
        })
        // #endif
      },
      // 检查在线状态
      checkOnlineStatus() {
        const token = this.$store.state.user.token
        this.isOnline = !!token
      },
      // 退出登录
      handleLogout() {
        this.$modal.confirm('确定要退出登录吗？').then(() => {
          this.$modal.loading("退出中...")
          this.$store.dispatch('LogOut').then(() => {
            this.$modal.closeLoading()
            this.$modal.msgSuccess("退出成功")
            // 刷新页面显示
            this.refreshUserInfo()
            this.isOnline = false
          }).catch(() => {
            this.$modal.closeLoading()
          })
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  page {
    background-color: #f5f6f7;
  }

  .mine-container {
    width: 100%;
    height: 100%;


    .header-section {
      padding: 15px 15px 45px 15px;
      background-color: #3c96f3;
      color: white;

      .cu-avatar {
        border: 2px solid #eaeaea;
        transition: all 0.3s ease;
        opacity: 0;
        transform: scale(0.8);

        .icon {
          font-size: 40px;
        }
      }
      
      .cu-avatar.fade-in {
        opacity: 1;
        transform: scale(1);
      }
      
      .cu-avatar:active {
        transform: scale(0.95) !important;
      }

      .login-tip {
        margin-left: 15px;
        flex: 1;
        
        .login-text {
          font-size: 18px;
          line-height: 28px;
          font-weight: 500;
        }
        
        .login-subtitle {
          font-size: 13px;
          line-height: 20px;
          opacity: 0.8;
        }
      }

      .user-info {
        margin-left: 15px;
        flex: 1;
        transition: opacity 0.3s ease;

        .u_title {
          font-size: 18px;
          line-height: 28px;
          font-weight: 500;
          display: flex;
          align-items: center;
          
          .online-badge {
            font-size: 12px;
            color: #67C23A;
            margin-left: 8px;
            font-weight: 400;
          }
        }
        
        .u_subtitle {
          font-size: 13px;
          line-height: 20px;
          opacity: 0.8;
        }
      }
      
      .header-action {
        .wechat-login-btn {
          background: linear-gradient(135deg, #07c160 0%, #06ad56 100%);
          color: white;
          font-size: 14px;
          padding: 10px 20px;
          border-radius: 25px;
          border: none;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          box-shadow: 0 4px 12px rgba(7, 193, 96, 0.4);
          min-width: 110px;
          height: 50px;
          transition: all 0.3s ease;
          
          .cuIcon-weixin {
            margin-right: 6px;
            font-size: 18px;
          }
          
          .btn-text {
            font-weight: 500;
          }
        }
        
        .wechat-login-btn:active {
          transform: scale(0.96);
          box-shadow: 0 2px 8px rgba(7, 193, 96, 0.3);
        }
        
        .wechat-login-btn::after {
          border: none;
        }
        
        .info-btn {
          color: white;
          font-size: 14px;
          padding: 10px 15px;
          border-radius: 25px;
          background: rgba(255, 255, 255, 0.2);
          backdrop-filter: blur(10px);
          min-width: 110px;
          height: 50px;
          justify-content: center;
          transition: all 0.3s ease;
          
          .iconfont {
            margin-left: 4px;
          }
        }
        
        .info-btn:active {
          background: rgba(255, 255, 255, 0.3);
          transform: scale(0.96);
        }
      }
    }

    .content-section {
      position: relative;
      top: -50px;

      .mine-actions {
        margin: 15px 15px;
        padding: 20px 0px;
        border-radius: 8px;
        background-color: white;

        .action-item {
          .icon {
            font-size: 28px;
          }

          .text {
            display: block;
            font-size: 13px;
            margin: 8px 0px;
          }
        }
      }
    }
  }
  
  /* 过渡动画 */
  .slide-fade-enter-active {
    transition: all 0.3s ease;
  }
  
  .slide-fade-leave-active {
    transition: all 0.2s ease;
  }
  
  .slide-fade-enter {
    transform: translateX(-10px);
    opacity: 0;
  }
  
  .slide-fade-leave-to {
    transform: translateX(10px);
    opacity: 0;
  }
  
  .fade-enter-active {
    transition: all 0.3s ease;
  }
  
  .fade-leave-active {
    transition: all 0.2s ease;
  }
  
  .fade-enter {
    opacity: 0;
    transform: scale(0.9);
  }
  
  .fade-leave-to {
    opacity: 0;
    transform: scale(0.9);
  }
</style>
