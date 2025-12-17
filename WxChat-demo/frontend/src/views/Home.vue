<template>
  <div class="home-container">
    <el-card class="login-card">
      <div class="logo">
        <el-icon :size="64" color="#667eea"><ChatDotRound /></el-icon>
      </div>
      <h1>WxChat Demo</h1>
      <p class="desc">企业微信登录 + 元器AI对话回显</p>
      
      <div class="actions">
        <el-button type="primary" size="large" @click="loginWithWeWork">
          <el-icon><Connection /></el-icon>
          企业微信登录
        </el-button>
        
        <el-button size="large" @click="goToChat">
          <el-icon><ChatLineSquare /></el-icon>
          直接体验对话
        </el-button>
      </div>
      
      <div class="tips">
        <p>提示：企业微信登录需要在企业微信客户端中打开</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ChatDotRound, Connection, ChatLineSquare } from '@element-plus/icons-vue'

const router = useRouter()

function loginWithWeWork() {
  // 跳转到企业微信OAuth授权
  const teamId = 1  // 默认团队ID
  window.location.href = `/api/auth/authorize?teamId=${teamId}`
}

function goToChat() {
  router.push('/chat')
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  text-align: center;
  padding: 40px 20px;
}

.logo {
  margin-bottom: 20px;
}

h1 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 28px;
}

.desc {
  color: #909399;
  margin-bottom: 30px;
}

.actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tips {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.tips p {
  color: #909399;
  font-size: 12px;
  margin: 0;
}
</style>
