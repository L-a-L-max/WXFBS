<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled'}" title="昵称" :rightText="user.nickName" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'contact-filled'}" title="用户ID" :rightText="user.userName" />
      <uni-list-item v-if="onlineStatus !== null" showExtraIcon="true" :extraIcon="{type: 'circle-filled', color: onlineStatus ? '#67C23A' : '#909399'}" :title="onlineStatus ? '在线' : '离线'" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'phone-filled'}" title="手机号码" :rightText="user.phonenumber" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'email-filled'}" title="邮箱" :rightText="user.email" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled'}" title="岗位" :rightText="postGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled'}" title="角色" :rightText="roleGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'calendar-filled'}" title="创建日期" :rightText="user.createTime" />
    </uni-list>
  </view>
</template>

<script>
  import { getUserProfile } from "@/api/system/user"

  export default {
    data() {
      return {
        user: {},
        roleGroup: "",
        postGroup: "",
        onlineStatus: null,
        statusCheckTimer: null
      }
    },
    onLoad() {
      this.getUser()
      this.checkOnlineStatus()
      // 每30秒检查一次在线状态
      this.statusCheckTimer = setInterval(() => {
        this.checkOnlineStatus()
      }, 30000)
    },
    onUnload() {
      // 清除定时器
      if (this.statusCheckTimer) {
        clearInterval(this.statusCheckTimer)
      }
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data
          this.roleGroup = response.roleGroup
          this.postGroup = response.postGroup
        })
      },
      checkOnlineStatus() {
        // 通过获取用户信息判断是否在线（如果token有效则在线）
        const token = uni.getStorageSync('token')
        if (token) {
          this.onlineStatus = true
        } else {
          this.onlineStatus = false
        }
      }
    }
  }
</script>

<style lang="scss">
  page {
    background-color: #ffffff;
  }
</style>
